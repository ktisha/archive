package dfsagent;

import dfsagent.datastructures.FileList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import serverlibrary.server.Listener;
import tasks.TaskFactory;

/**
 *
 * @author student
 */
public class Agent implements Runnable {

	private final Listener listener;

	public static void main(String[] args) {
		String propetiesPath = "agent.properties";

		if (args.length == 1) {
			propetiesPath = args[0];
		}

        InputStream inPropertiesStream = null;
		try {
			inPropertiesStream = new FileInputStream(propetiesPath);
		} catch (FileNotFoundException ex) {
			System.err.println("ERROR: Can't open 'agent.properties' file");
			ex.printStackTrace();
			System.exit(1);
		}
		Properties properies = new Properties();
		try {
			properies.load(inPropertiesStream);
		} catch (IOException ex) {
			System.err.println("ERROR: Can't load 'agent.properties' file");
			ex.printStackTrace();
			System.exit(1);
		}

		// Read non string values for port
        int port = 0;
		try {
			port = new Integer(properies.getProperty("port", "9000"));
		} catch (NumberFormatException ex) {
			System.err.println("ERROR: Can't read value from agent.properties");
			System.err.println(ex.getMessage());
			System.exit(1);
		}
        // free space
        int freeSpace = 0;
		try {
			freeSpace = new Integer(properies.getProperty("freeSpace", "1073741824"));
		} catch (NumberFormatException ex) {
			System.err.println("ERROR: Can't read value from agent.properties");
			System.err.println(ex.getMessage());
			System.exit(1);
		}

        String pathToSerialize = "lib/myState.ser";
        pathToSerialize = properies.getProperty("pathToSerialize", "lib/myState.ser");

        // create logs
    	createLog(properies.getProperty("logPath", "log/main.log"));

        FileList.Init(freeSpace, pathToSerialize);
		Agent agent = new Agent(port);
		agent.run();
	}

	public Agent(int port) {
		TaskFactory factory = new tasks.TaskFactory();
		this.listener = new Listener(port, factory);
		FileList.Init(1024, "lib/myState.ser");
	}

    private static void createLog(String logPath) {
		FileHandler hand;
		try {
			hand = new FileHandler(logPath);
			Logger log = Logger.getLogger("agent");
			log.addHandler(hand);
			log.setLevel(Level.ALL);
			SimpleFormatter formatter = new SimpleFormatter();
			hand.setFormatter(formatter);
			log = Logger.getLogger("server");
			log.addHandler(hand);
			log.setLevel(Level.ALL);
		} catch (Exception ex) {
			System.err.println("ERROR: Can't open file " + logPath);
			ex.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void run() {
		listener.run();
	}
}
