package sfs;

import dfsexceptions.DataBaseDfsException;
import sfs.storage.StorageManagerException;
import sfs.storage.Storage;
import sfs.stream.RemoteOutFile;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import manager.dbmanager.DBManager;
import org.junit.Test;
import sfs.storage.StorageManager;
import static org.junit.Assert.*;

/**
 *
 * @author an
 */
public class RemoteOutFileTest {
	private static int testfileSize = 67;
	private final RemoteFileDescr filedescr = new RemoteFileDescr();

    public RemoteOutFileTest() throws StorageManagerException, DataBaseDfsException {
		DBManager.Init("dfs-test.db");
		filedescr.id = 45;
		filedescr.size = testfileSize;
		sfs.storage.StorageManager.Init(6000000);
		StorageManager storageManager = sfs.storage.StorageManager.GetInstance();
		filedescr.storage = storageManager.getStorageById(1);
		//filedescr.storageHostname = "localhost";
		//filedescr.storagePort = 9000;
    }


	/**
	 * Test of write method, of class RemoteOutFile.
	 */
	@Test
	public void testWrite() throws Exception {
		Thread.sleep(1000);
		// run storage agent
		pseudoremot.Agent agent = new pseudoremot.Agent(filedescr.storage.port);
		Thread agentThread = new Thread(agent);
		agentThread.start();
		Thread.yield();

		// read data from file
		InputStream filestream = new FileInputStream("agentFile.test");
		byte [] filebytes = new byte[testfileSize];
		filestream.read(filebytes);

		System.out.println("write");
		RemoteOutFile instance = new RemoteOutFile(filedescr);
		instance.write(filebytes);
		instance.close();
		instance.close();

		assertEquals(filedescr.id, agent.fileId);
		assertEquals(filedescr.size, agent.fileSize);
		assertEquals("upload", agent.requestType);

		assertArrayEquals(filebytes, agent.readedBytes);
	}

	/**
	 * Test of close method, of class RemoteOutFile.
	 * Test fail on closing stream, when it wasn't written expected number of the bytes.
	 */
	@Test (expected=IOException.class)
	public void testClose_fail() throws IOException {
		// run storage agent
		//filedescr.storagePort = 9001;
		pseudoremot.Agent agent = new pseudoremot.Agent(filedescr.storage.port);
		Thread agentThread = new Thread(agent);
		agentThread.start();
		Thread.yield();

		System.out.println("close fail");
		RemoteOutFile instance = new RemoteOutFile(filedescr);
		instance.write(1);
		// close stream without writing whole data
		instance.close();
	}



}