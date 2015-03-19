package sfs.storage;

import dfsagent.datastructures.FileList;
import dfsexceptions.DataBaseDfsException;
import manager.dbmanager.DBManager;
import org.junit.Test;
import static org.junit.Assert.*;
import sfs.SfsFileDescr;

/**
 *
 * @author an
 */
public class StorageManagerTest {

	public StorageManagerTest() throws DataBaseDfsException, InterruptedException {
		System.err.println("StorageManagerTest");
		//dfsagent.Agent agent = new dfsagent.Agent();
		dfsagent.Agent agent0 = new dfsagent.Agent(9000);
		Thread agentThread0 = new Thread(agent0);
		agentThread0.start();

		dfsagent.Agent agent1 = new dfsagent.Agent(9001);
		Thread agentThread1 = new Thread(agent1);
		agentThread1.start();

		dfsagent.Agent agent2 = new dfsagent.Agent(9002);
		Thread agentThread2 = new Thread(agent2);
		agentThread2.start();


		DBManager.Init("dfs-test.db");

		Thread.yield();

		FileList.Init(7, "test.ser");

		StorageManager.Init(60000000);
		Thread.yield();
		Thread.sleep(1000);

	}

	/**
	 * Test of Init method, of class StorageManager.
	 */
//	@Test
	public void testInit() throws Exception {
		System.out.println("Init");
		StorageManager.Init(6000000);
		StorageManager storageManager = StorageManager.GetInstance();
		StorageManager.Init(600000);
		assertEquals(storageManager, StorageManager.GetInstance());
	}

	/**
	 * Test of GetInstance method, of class StorageManager.
	 */
//	@Test
	public void testGetInstance() throws DataBaseDfsException {
		System.out.println("GetInstance");
		StorageManager.Init(600000);
		StorageManager result = StorageManager.GetInstance();
		assertEquals(StorageManager.GetInstance(), result);
	}

	/**
	 * Test of getStorage method, of class StorageManager.
	 */
//	@Test
	public void testGetStorageById() throws Exception {
		System.out.println("getStorageById");
		int id = 1;
		StorageManager instance = StorageManager.GetInstance();
		Storage result = instance.getStorageById(id);
		assertEquals("localhost", result.host);
		assertEquals(1, result.id);
		assertEquals(9000, result.port);
		assertEquals(Storage.State.ONLINE, result.getState());
	}

	/**
	 * Test of place method, of class StorageManager.
	 */
	@Test
	public void testPlace_1copy() throws Exception {
		System.out.println("place 1 copy");
		SfsFileDescr fileDescr = new SfsFileDescr();
		fileDescr.name = "name";
		fileDescr.filelistId = 1;
		fileDescr.copy = 1;
		fileDescr.size = 7;
		fileDescr.user = "student";

		int partSize = 6;

		StorageManager instance = StorageManager.GetInstance();
		instance.place(fileDescr, partSize);

		assertEquals(2, fileDescr.fileparts.size());
		assertEquals(6, fileDescr.fileparts.get(0).size);
		assertEquals(1, fileDescr.fileparts.get(1).size);

		assertEquals(1, fileDescr.fileparts.get(0).remotedescrs.size());
		assertEquals(1, fileDescr.fileparts.get(1).remotedescrs.size());
	}

	/**
	 * Test of place method, of class StorageManager.
	 */
	@Test
	public void testPlace_2copy() throws Exception {
		System.out.println("place 2 copy");
		SfsFileDescr fileDescr = new SfsFileDescr();
		fileDescr.name = "name";
		fileDescr.filelistId = 1;
		fileDescr.copy = 2;
		fileDescr.size = 7;
		fileDescr.user = "student";

		int partSize = 6;

		StorageManager instance = StorageManager.GetInstance();
		instance.place(fileDescr, partSize);

		assertEquals(2, fileDescr.fileparts.size());
		assertEquals(6, fileDescr.fileparts.get(0).size);
		assertEquals(1, fileDescr.fileparts.get(1).size);

		assertEquals(2, fileDescr.fileparts.get(0).remotedescrs.size());
		assertEquals(6, fileDescr.fileparts.get(0).remotedescrs.get(0).size);

		assertEquals(2, fileDescr.fileparts.get(1).remotedescrs.size());
		assertEquals(1, fileDescr.fileparts.get(1).remotedescrs.get(0).size);
	}



	/**
	 * Test of place method, of class StorageManager.
	 */
	@Test
	public void testPlace() throws Exception {
		System.out.println("place");
		SfsFileDescr fileDescr = new SfsFileDescr();
		fileDescr.name = "name17";
		fileDescr.filelistId = 6;
		fileDescr.copy = 2;
		fileDescr.size = 7;
		fileDescr.user = "student";

		DBManager.GetInstance().createFiledescr(fileDescr);
	}
}
