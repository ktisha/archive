package sfs;

import dfsexceptions.DataBaseDfsException;
import sfs.storage.Storage;
import sfs.storage.StorageManagerException;
import sfs.stream.RemoteInFile;
import sfs.stream.DuplicateInFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import manager.dbmanager.DBManager;
import org.junit.Before;
import org.junit.Test;
import sfs.storage.StorageManager;
import static org.junit.Assert.*;

/**
 *
 * @author an
 */
public class DuplicateInFileTest {
	private List<sfs.RemoteFileDescr> filepartList = new ArrayList<sfs.RemoteFileDescr>();

	public DuplicateInFileTest() throws StorageManagerException, DataBaseDfsException, InterruptedException {
		DBManager.Init("dfs-test.db");
		sfs.storage.StorageManager.Init(6000000);
		StorageManager storageManager = sfs.storage.StorageManager.GetInstance();
		sfs.RemoteFileDescr filepart = new sfs.RemoteFileDescr();
		filepart.size = 67;
		filepart.id = 12;
		filepart.storage = storageManager.getStorageById(1);
		filepartList.add(filepart);

		filepart = new sfs.RemoteFileDescr();
		filepart.size = 67;
		filepart.id = 12;
		filepart.storage = storageManager.getStorageById(3);
		filepartList.add(filepart);

		filepart = new sfs.RemoteFileDescr();
		filepart.size = 67;
		filepart.id = 12;
		filepart.storage = storageManager.getStorageById(4);
		filepartList.add(filepart);

		filepart = new sfs.RemoteFileDescr();
		filepart.size = 67;
		filepart.id = 12;
		filepart.storage = storageManager.getStorageById(5);
		filepartList.add(filepart);
		Thread.sleep(1000);
    }


	@Test
	public void testRead_1stream() throws Exception {
		//System.gc();
		// run storage agent
		pseudoremot.Agent agent = new pseudoremot.Agent(filepartList.get(0).storage.port);
		new Thread(agent).start();
		Thread.yield();

		System.err.println("read from 1 stream");
		List<InputStream> tmpfilepartList = new ArrayList<InputStream>();
		tmpfilepartList.add(new RemoteInFile(filepartList.get(0)));
		DuplicateInFile instance = new DuplicateInFile(tmpfilepartList);
		int expResult = 'I';
		int result = instance.read();
		assertEquals(expResult, result);
		instance.close();
	}


	@Test
	public void testRead_2stream() throws Exception {
		//System.gc();
		// run storage agent
		pseudoremot.Agent agent = new pseudoremot.Agent(filepartList.get(1).storage.port);
		new Thread(agent).start();
		Thread.yield();
		// run storage agent
		pseudoremot.Agent agent2 = new pseudoremot.Agent(filepartList.get(2).storage.port);
		new Thread(agent2).start();
		Thread.yield();

		System.err.println("read from 2 stream");
		List<InputStream> tmpfilepartList = new ArrayList<InputStream>();
		tmpfilepartList.add(new RemoteInFile(filepartList.get(1)));
		tmpfilepartList.add(new RemoteInFile(filepartList.get(2)));
		DuplicateInFile instance = new DuplicateInFile(tmpfilepartList);
		int expResult = 'I';
		int result = instance.read();
		assertEquals(expResult, result);
		instance.close();
	}

	@Test
	public void testRead_2stream_withFailOn1() throws Exception {
		//System.gc();
		// run storage agent
		pseudoremot.Agent agent2 = new pseudoremot.Agent(filepartList.get(3).storage.port);
		new Thread(agent2).start();
		Thread.yield();

		System.err.println("read from 2 stream, with fail on first stream");
		List<InputStream> tmpfilepartList = new ArrayList<InputStream>();
		tmpfilepartList.add(new RemoteInFile(filepartList.get(0)));
		tmpfilepartList.add(new RemoteInFile(filepartList.get(3)));
		DuplicateInFile instance = new DuplicateInFile(tmpfilepartList);
		int expResult = 'I';
		int result = instance.read();
		assertEquals(expResult, result);
		instance.close();

	}


	@Test (expected=IOException.class)
	public void testRead_2stream_withFail() throws Exception {
		//System.gc();
		System.err.println("read from 2 stream, expect fail");
		List<InputStream> tmpfilepartList = new ArrayList<InputStream>();
		tmpfilepartList.add(new RemoteInFile(filepartList.get(0)));
		tmpfilepartList.add(new RemoteInFile(filepartList.get(1)));
		DuplicateInFile instance = new DuplicateInFile(tmpfilepartList);
		instance.read();
		instance.close();
		instance.close();
	}



}