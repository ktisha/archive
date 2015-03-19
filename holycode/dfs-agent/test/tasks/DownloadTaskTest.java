/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tasks;

import dfsagent.datastructures.FileList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author student
 */
public class DownloadTaskTest {

	public DownloadTaskTest() {
		FileList.Init(1234, "test2.ser");
	}

	/**
	 * Test of doTask method, of class DownloadTask.
	 */
	@Test
	public void testDoTask() throws Exception {
		System.out.println("doTask");
		FileInputStream fileIn = null;

		DownloadTask task = new DownloadTask(1);
		fileIn = new FileInputStream("./lib/input");
		FileOutputStream fileOut = new FileOutputStream("./lib/output");
		task.doTask(fileIn, fileOut);

		fileIn.close();

	}

	/**
	 * Test of toString method, of class DownloadTask.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		DownloadTask task = new DownloadTask(1);
		assertEquals(task.toString(), "DownloadTask -- [filePath=1]");
	}
}
