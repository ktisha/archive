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
public class DeleteTaskTest {

	public DeleteTaskTest() {
		FileList.Init(8888, "test.tesrt.ser");
	}

	/**
	 * Test of doTask method, of class DeleteTask.
	 */
	@Test
	public void testDoTask() throws Exception {
		System.out.println("doTask");
		FileInputStream fileIn = null;

		DeleteTask task = new DeleteTask(1);
		fileIn = new FileInputStream("./lib/input");
		FileOutputStream fileOut = new FileOutputStream("./lib/output");
		task.doTask(fileIn, fileOut);


		fileIn.close();

	}

	/**
	 * Test of toString method, of class DeleteTask.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		DeleteTask task = new DeleteTask(1);
		assertEquals(task.toString(), "DeleteTask -- [filePath=1]");
	}
}
