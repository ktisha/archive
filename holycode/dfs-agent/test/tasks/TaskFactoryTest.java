/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tasks;

import dfsagent.datastructures.FileDescription;
import dfsagent.datastructures.FileList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import serverlibrary.task.Task;

/**
 *
 * @author student
 */
public class TaskFactoryTest {

	public TaskFactoryTest() throws FileNotFoundException, IOException {
		OutputStream out = new FileOutputStream("test.ser");
		out.close();
		FileList.Init(4545, "test.ser");
	}

	/**
	 * Test of CreateTask method, of class TaskFactory.
	 */
	@Test
	public void testCreateUploadTask() throws Exception {
		System.out.println("CreateTask");
		TaskFactory f = new TaskFactory();
		File file = new File("./test/source/xml_upload");
		StringBuilder contents = new StringBuilder();

		//use buffering, reading one line at a time
		BufferedReader input = new BufferedReader(new FileReader(file));
		try {
			String line = null;
			while ((line = input.readLine()) != null) {
				contents.append(line);
				contents.append(System.getProperty("line.separator"));
			}
		} finally {
			input.close();
		}

//        System.err.println(contents.toString());
		Task task = f.CreateTask(contents.toString());
		assertEquals(UploadTask.class, task.getClass());
	}

	@Test  
	public void testCreateDownloadTask() throws Exception {
		System.out.println("CreateTask");
		TaskFactory f = new TaskFactory();
		File file = new File("./test/source/xml_download");
		StringBuilder contents = new StringBuilder();

		//use buffering, reading one line at a time
		BufferedReader input = new BufferedReader(new FileReader(file));
		try {
			String line = null;
			while ((line = input.readLine()) != null) {
				contents.append(line);
				contents.append(System.getProperty("line.separator"));
			}
		} finally {
			input.close();
		}

				FileList fList = FileList.GetInstance();
		FileDescription file2 = new FileDescription(1, 2, "path");
		fList.insert(file2);

		Task task = f.CreateTask(contents.toString());
		assertEquals(task.getClass(), DownloadTask.class);
	}
}
