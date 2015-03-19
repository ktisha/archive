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
public class UploadTaskTest {

	public UploadTaskTest() throws FileNotFoundException, IOException {
		OutputStream out = new FileOutputStream("test.ser");
		out.close();
		FileList.Init(0, "test.ser");
	}

	/**
	 * Test of doTask method, of class UploadTask.
	 */
	@Test
	public void testDoTask() throws Exception {
		System.out.println("doTask");
		FileInputStream fileIn = null;

		UploadTask task = new UploadTask(1, 2413942);
		fileIn = new FileInputStream("./lib/1");
		FileOutputStream fileOut = new FileOutputStream("./lib/output");
		task.doTask(fileIn, fileOut);

		fileIn.close();

	}

	/**
	 * Test of toString method, of class UploadTask.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		UploadTask task = new UploadTask(1, 2413942);
		assertEquals(task.toString(), "UploadTask -- [filename=1] [filesize=2413942]");
	}
}
