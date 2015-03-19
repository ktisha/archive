package sfs;

import sfs.stream.DuplicateOutFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author an
 */
public class DuplicateOutFileTest {

	private static int testfileSize = 67;

    public DuplicateOutFileTest() {
    }

	/**
	 * Test of write method, of class DuplicateOutFile.
	 */
	@Test
	public void testWrite_1stream() throws Exception {

		ByteArrayOutputStream out = new ByteArrayOutputStream(testfileSize);

		System.out.println("write 1 stream");
		int b = 34;
		List<OutputStream> outlist = new ArrayList<OutputStream>();
		outlist.add(out);
		DuplicateOutFile instance = new DuplicateOutFile(outlist);
		instance.write(b);
		instance.close();
		instance.close();

		assertEquals(b, out.toByteArray()[0]);
	}


	@Test
	public void testWrite_2stream() throws Exception {
		// create otput streams
		ByteArrayOutputStream out1 = new ByteArrayOutputStream();
		ByteArrayOutputStream out2 = new ByteArrayOutputStream();
		List<OutputStream> outlist = new ArrayList<OutputStream>();
		outlist.add(out1);
		outlist.add(out2);

		System.out.println("write 2 streams");
		byte [] bytes = "tipa string".getBytes();

		DuplicateOutFile instance = new DuplicateOutFile(outlist);
		instance.write(bytes);
		instance.close();
		instance.close();

		for(byte b : bytes) {
			assertArrayEquals(bytes, out1.toByteArray());
			assertArrayEquals(bytes, out2.toByteArray());
		}
	}


	@Test
	public void testWrite_2stream_1fail() throws Exception {
		// create otput streams
		ByteArrayOutputStream out1 = new ByteArrayOutputStream(2);

		OutputStream out2 = new ByteArrayOutputStream();
		List<OutputStream> outlist = new ArrayList<OutputStream>();
		outlist.add(out1);
		out2 = new DuplicateOutFile(outlist);
		out2.close();
		outlist.add(out2);

		System.out.println("write 2 streams wuth fail in 1 stream");
		byte [] bytes = "tipa string".getBytes();

		DuplicateOutFile instance = new DuplicateOutFile(outlist);
		instance.write(bytes);
		instance.close();
		instance.close();

		for(byte b : bytes) {
			assertArrayEquals(bytes, out1.toByteArray());
		}
	}


	@Test (expected=IOException.class)
	public void testWrite_2stream_2fail() throws IOException {
		// create otput streams
		OutputStream out1 = new ByteArrayOutputStream(2);
		
		List<OutputStream> outlist = new ArrayList<OutputStream>();
		outlist.add(out1);
		OutputStream out2 = new DuplicateOutFile(outlist);
		out1 = new DuplicateOutFile(outlist);
		out1.close();
		out2.close();
		outlist = new ArrayList<OutputStream>();
		outlist.add(out1);
		outlist.add(out2);

		System.out.println("write 2 streams wuth fail in 2 stream");
		byte [] bytes = "tipa string".getBytes();

		DuplicateOutFile instance = new DuplicateOutFile(outlist);
		instance.write(bytes);
	}

}