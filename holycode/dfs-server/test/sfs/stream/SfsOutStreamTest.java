/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfs.stream;

import dfsagent.datastructures.FileList;
import dfsexceptions.DataBaseDfsException;
import manager.dbmanager.DBManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sfs.SfsFileDescr;
import sfs.storage.StorageManager;
import static org.junit.Assert.*;

/**
 *
 * @author an
 */
public class SfsOutStreamTest {

	public SfsOutStreamTest() {
	}

	@BeforeClass
	static public void beforeClass() throws DataBaseDfsException, InterruptedException {
		DBManager.Init("dfs-test.db");
		FileList.Init(1024, "test2.ser");
		dfsagent.Agent agent = new dfsagent.Agent(9000);
		new Thread(agent).start();

		dfsagent.Agent agent2 = new dfsagent.Agent(9001);
		new Thread(agent2).start();

		Thread.sleep(1000);
		StorageManager.Init(9000000);
		Thread.sleep(1000);

	}

	/**
	 * Test of close method, of class SfsOutStream.
	 */
//	@Test
	public void testClose() throws Exception {
		System.out.println("place");
		SfsFileDescr fileDescr = new SfsFileDescr();
		fileDescr.name = "name19";
		fileDescr.filelistId = 1;
		fileDescr.copy = 1;
		fileDescr.size = 7;
		fileDescr.user = "student";

		DBManager.GetInstance().createFiledescr(fileDescr);

		SfsOutStream instance = new SfsOutStream(fileDescr);

		instance.close();
	}

	/**
	 * Test of finalize method, of class SfsOutStream.
	 */
//	@Test
	public void testFinalize() throws Exception, Throwable {
		System.out.println("finalize");
		SfsOutStream instance = null;
		instance.finalize();
	}

	/**
	 * Test of write method, of class SfsOutStream.
	 */
//	@Test
	public void testWrite() throws Exception {
		System.out.println("write");
		SfsFileDescr fileDescr = new SfsFileDescr();
		fileDescr.name = "name29";
		fileDescr.filelistId = 1;
		fileDescr.copy = 1;
		fileDescr.size = 1;
		fileDescr.user = "student";

		DBManager.GetInstance().createFiledescr(fileDescr);

		SfsOutStream instance = new SfsOutStream(fileDescr);
		instance.write(12);

		instance.close();

		Thread.sleep(1000);
	}
	/**
	 * Test of write method, of class SfsOutStream.
	 */
//	@Test
	public void testWrite_2copy() throws Exception {
		System.out.println("write 2 copy");
		SfsFileDescr fileDescr = new SfsFileDescr();
		fileDescr.name = "name32";
		fileDescr.filelistId = 1;
		fileDescr.copy = 2;
		fileDescr.size = 1;
		fileDescr.user = "student";

		DBManager.GetInstance().createFiledescr(fileDescr);

		SfsOutStream instance = new SfsOutStream(fileDescr);
		instance.write(12);

		instance.close();

		Thread.sleep(1000);
	}

		/**
	 * Test of write method, of class SfsOutStream.
	 */
	@Test
	public void testWrite_2parts() throws Exception {
		System.out.println("write 2 parts");
		SfsFileDescr fileDescr = new SfsFileDescr();
		fileDescr.name = "name344";
		fileDescr.filelistId = 1;
		fileDescr.copy = 1;
		fileDescr.size = 15;
		fileDescr.user = "student";

		DBManager.GetInstance().createFiledescr(fileDescr);

		SfsOutStream instance = new SfsOutStream(fileDescr);

		for (int i = 0; i < fileDescr.size; ++i) {
			System.err.println(i);
			instance.write(i);
		}

		instance.close();

		Thread.sleep(1000);
	}
}
