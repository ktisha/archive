package centarlserver;

import dfsexceptions.DataBaseDfsException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import manager.dbmanager.DBManager;
import serverlibrary.task.TaskFactory;
import serverlibrary.server.Listener;

/**
 *
 * @author an
 */
public class CentralServer {

	public static void main(String[] args) {
		InputStream inPropertiesStream = null;
		String propertiesPath = "centralserver.properties";

		if (args.length == 1) {
			propertiesPath = args[0];
		}

		try {
			inPropertiesStream = new FileInputStream(propertiesPath);
		} catch (FileNotFoundException ex) {
			System.err.println("ERROR: Can't open 'centralserver.properties' file");
			ex.printStackTrace();
			System.exit(1);
		}
		Properties properies = new Properties();
		try {
			properies.load(inPropertiesStream);
		} catch (IOException ex) {
			System.err.println("ERROR: Can't load 'centralserver.properties' file");
			ex.printStackTrace();
			System.exit(1);
		}

		// Read non string values
		int port = 0;
		int spaceChechSleepTime = 0;
		try {
			port = new Integer(properies.getProperty("port", "8000"));
			spaceChechSleepTime = new Integer(properies.getProperty("checkSpaceTime", "60000"));
		} catch (NumberFormatException ex) {
			System.err.println("ERROR: Can't read value from centralserver.properties");
			System.err.println(ex.getMessage());
			System.exit(1);
		}

		// create logs
		createLog(properies.getProperty("logPath", "log/main.log"));

		// init database manager
		DBManager.Init(properies.getProperty("dbPath", "dfs-test.db"));
		try {
			// load inf about storage to database from file 'storage.conf'
			loadStorage("storage.conf");
		} catch (IOException ex) {
			Logger.getLogger("centralServer").log(Level.SEVERE, "Can't load inf about storage to database", ex);
			System.err.println("ERROR: Can't start StorageManager, I/O problem. See more in log.");
			System.exit(1);
		} catch (DataBaseDfsException ex) {
			Logger.getLogger("centralServer").log(Level.SEVERE, "Can't load inf about storage to database", ex);
			System.err.println("ERROR: Can't start StorageManager, database problem. See more in log.");
			System.exit(1);
		}

		try {
			// starting storage manager
			sfs.storage.StorageManager.Init(spaceChechSleepTime);
		} catch (DataBaseDfsException ex) {
			Logger.getLogger("centralServer").log(Level.SEVERE, "Can't start StorageManager", ex);
			System.err.println("ERROR: Can't start StorageManager, database problem. See more in log.");
			System.exit(1);
		}

		TaskFactory taskFactory = new task.CentralServerTaskFactory();
		Listener listener = new Listener(port, taskFactory);
		listener.run();
	}

	private static void createLog(String logPath) {
		FileHandler hand;
		try {
			hand = new FileHandler(logPath);
			Logger log = Logger.getLogger("server");
			log.addHandler(hand);
			log.setLevel(Level.ALL);
			SimpleFormatter formatter = new SimpleFormatter();
			hand.setFormatter(formatter);

			log = Logger.getLogger("centralServer");
			log.setLevel(Level.ALL);
			log.addHandler(hand);

			log = Logger.getLogger("dblog");
			log.setLevel(Level.ALL);
			log.addHandler(hand);


			log = Logger.getLogger("dblog");
			log.setLevel(Level.ALL);
			log.addHandler(hand);


			log = Logger.getLogger("storage");
			log.setLevel(Level.ALL);
			log.addHandler(hand);


		} catch (Exception ex) {
			System.err.println("ERROR: Can't open file " + logPath);
			ex.printStackTrace();
			System.exit(1);
		}
	}

	private static void loadStorage(String storageConfPath) throws IOException, DataBaseDfsException {
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(storageConfPath));
			String line = null; //not declared within while loop

			int i = 0;

			while ((line = input.readLine()) != null) {
				i++;

				String[] str = line.split(" ");
				if (str.length != 2) {
					throw new IOException("Error: in line #" + i);
				}
				String host = str[0];
				int port = 0;
				try {
					port = new Integer(str[1]);
				} catch (NumberFormatException ex) {
					throw new IOException("Error: port is not number (in line #" + i + ")");
				}
				DBManager.GetInstance().addStorage(host, port);
			}
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}
}
