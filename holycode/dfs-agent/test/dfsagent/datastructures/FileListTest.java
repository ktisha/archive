/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dfsagent.datastructures;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
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
public class FileListTest {

	public FileListTest() throws FileNotFoundException, IOException {
		OutputStream out = new FileOutputStream("test.ser");
		out.close();
		FileList.Init(34344, "test.ser");
	}

	/**
	 * Test of insert method, of class FileList.
	 */
	@Test
	public void testInsert() throws NoFreeSpaceDfsAgentException {
		System.out.println("insert");

		FileList fList = FileList.GetInstance();
		FileDescription file = new FileDescription(1, 2, "path");
		fList.insert(file);
		assertEquals(1, fList.getFile(1).fileName);
		assertEquals(2, fList.getFile(1).fileSize);
		assertEquals("path", fList.getFile(1).path);
	}

	/**
	 * Test of delete method, of class FileList.
	 */
	@Test
	public void testDelete() throws NoFreeSpaceDfsAgentException {
		System.out.println("delete");

		FileList fList = FileList.GetInstance();
		FileDescription file = new FileDescription(1, 2, "path");
		fList.insert(file);
		fList.delete(1);
		assertEquals(0, fList.size());

	}

	/**
	 * Test of serialize method, of class FileList.
	 */
	@Test
	public void testSerialization() throws IOException, NoFreeSpaceDfsAgentException {
		System.out.println("serialize");
		FileList fList = FileList.GetInstance();
		FileDescription file = new FileDescription(1, 2, "path");
		fList.insert(file);

		fList.serialize();
		fList.delete(1);
		System.err.println(fList.size());

		fList.deserialize();

		assertEquals(fList.size(), 1);
		assertEquals(fList.getFile(1).fileName, 1);
		assertEquals(fList.getFile(1).fileSize, 2);
		assertEquals(fList.getFile(1).path, "path");

	}
}
