package sfs;

import dfsexceptions.DataBaseDfsException;
import sfs.storage.StorageManagerException;
import sfs.storage.Storage;
import sfs.stream.RemoteInFile;
import java.io.IOException;
import manager.dbmanager.DBManager;
import org.junit.Test;
import sfs.storage.StorageManager;
import static org.junit.Assert.*;

/**
 *
 * @author an
 */
public class RemoteInFileTest {
	private final sfs.RemoteFileDescr remotefiledescr = new sfs.RemoteFileDescr();

    public RemoteInFileTest() throws StorageManagerException, DataBaseDfsException, InterruptedException  {
		DBManager.Init("dfs-test.db");
		sfs.storage.StorageManager.Init(6000000);
		StorageManager storageManager = sfs.storage.StorageManager.GetInstance();
		remotefiledescr.size = 67;
		remotefiledescr.id = 12;
		remotefiledescr.storage = storageManager.getStorageById(1);
		Thread.sleep(1000);
    }


	/**
	 * Test of read method, of class RemoteInFile.
	 */
	@Test
	public void testRead() throws Exception {
		// run storage agent
		//remotefiledescr.storagePort = 9000;
		pseudoremot.Agent agent = new pseudoremot.Agent(remotefiledescr.storage.port);
		new Thread(agent).start();
		Thread.yield();

		System.out.println("read");
		RemoteInFile instance = new RemoteInFile(remotefiledescr);

		// checks first readed byte
		int b = instance.read();
		// checks the request parametrs
		assertEquals(agent.fileId, remotefiledescr.id);
		assertEquals(agent.requestType, "download");
		assertEquals(agent.offset, 0);

		assertEquals(b, 'I');
		b = instance.read();
		assertEquals(b, ' ');

		// checks avaliable reading all file
		byte [] bytes = new byte[remotefiledescr.size -2];
		int readed = instance.read(bytes);
		assertEquals(readed, remotefiledescr.size -2);

		// checks next after last byte
		assertEquals(instance.read(), -1);

		instance.close();
		instance.close();
		System.err.println("end read");
	}

	/**
	 * Test of close method, of class RemoteInFile.
	 * Test genereate exception on read from closes stream.
	 */
	@Test (expected=IOException.class)
	public void testClose() throws Exception {
		// run storage agent
		//remotefiledescr.storagePort = 9000;
		pseudoremot.Agent agent = new pseudoremot.Agent(remotefiledescr.storage.port);
		new Thread(agent).start();
		Thread.yield();

		System.out.println("read");
		RemoteInFile instance = new RemoteInFile(remotefiledescr);
		instance.read();
		instance.close();
		// try read from closed stream
		instance.read();
	}

}