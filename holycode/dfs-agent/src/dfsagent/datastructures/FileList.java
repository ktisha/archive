package dfsagent.datastructures;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author student
 */
public final class FileList {

	private static ConcurrentMap<Integer, FileDescription> fileList =
			new ConcurrentHashMap<Integer, FileDescription>();
	private static volatile boolean isModified = false;
	private static String pathToSerializeFile = "./lib/myState.ser";
	private static final long sleepTime = 600;
	private int freeSpace = 1024 * 1024 * 1024;

	public synchronized int getFreeSpace() {
		return freeSpace;
	}
//
//	public static void setFreeSpace(int freeSpace) {
//		FileList.freeSpace = freeSpace;
//	}

	private class Guard implements Runnable {

		@Override
		public void run() {
			try {
				while (true) {
					Thread.sleep(sleepTime);
					if (FileList.isModified) {
						FileList.isModified = false;
						FileList.GetInstance().serialize();
					}
				}
			} catch (InterruptedException ex) {
				Logger.getLogger(FileList.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private FileList(int freeSpaseTmp, String  path) {
        // settings
        freeSpace = freeSpaseTmp;
        pathToSerializeFile = path;
        // deserialization
		try {
			deserialize();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// TODO remove bad files
//		for (FileDescription file : fileList.values()) {
//			if (file.getState() == FileDescription.State.UPLOADING) {
//				fileList.remove(file.fileName);
//			}
//		}

		// calculate free space
		for (FileDescription fileDescription : fileList.values()) {
			freeSpace -= fileDescription.fileSize;
		}

		Thread guardThread = new Thread(new Guard());
		guardThread.setDaemon(true);
		guardThread.start();
	}

	private static FileList ourInstance = null;

	public static void Init(int freeSpase, String  path) {
		if (ourInstance == null) {
			ourInstance = new FileList(freeSpase, path);
		}
	}

	public static FileList GetInstance() {
		if (ourInstance == null) {
			throw new RuntimeException("Uses uninit FileList");
		}
		return ourInstance;
	}

	public int size() {
		return fileList.size();
	}

	public synchronized void insert(FileDescription file) throws NoFreeSpaceDfsAgentException {
		if (file.fileSize > freeSpace) {
			throw new NoFreeSpaceDfsAgentException("No free space on storage");
		}
		freeSpace -= file.fileSize;
		fileList.put(file.fileName, file);
		isModified = true;
	}

	public FileDescription getFile(Integer name) {
		return fileList.get(name);
	}

	public synchronized boolean delete(Integer name) {
		FileDescription fileDescription = fileList.remove(name);
		if (fileDescription == null) {
			return false;
		}
		freeSpace += fileDescription.fileSize;
		isModified = true;
		return true;
	}

	public Collection<FileDescription> getAllFiles() {
		return fileList.values();
	}

	public final void serialize() {
		String filename = pathToSerializeFile;
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(fileList);
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public final void deserialize() throws IOException {
		String filename = pathToSerializeFile;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		fis = new FileInputStream(filename);
		in = new ObjectInputStream(fis);
		try {
			fileList = (ConcurrentMap<Integer, FileDescription>) in.readObject();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		in.close();
	}
}

