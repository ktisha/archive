package sfs;

import sfs.stream.SplitInFile;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author an
 */
public class SplitInFileTest {
	private static int testfileSize = 67;

	private List<FilePartDescr> getFilepartDescr(int number) throws IOException {
		List<FilePartDescr> list = new ArrayList<FilePartDescr>();
		for(int i = 0; i < number; ++i) {
			FilePartDescr fpd = new FilePartDescr();
			fpd.size = testfileSize;
			fpd.inStream = new FileInputStream("agentFile.test");
			list.add(fpd);
		}
		return list;
	}

	/**
	 * Test of read method, of class SplitInFile.
	 */
	@Test
	public void testRead_1part() throws Exception {
		// split file consists only 1 part
		System.out.println("read 1 part");
		SplitInFile instance = new SplitInFile( getFilepartDescr(1) );
		compareStream(instance, 1);
		instance.close();

		// split file consists 2 parts
		System.out.println("read 2 parts");
		instance = new SplitInFile( getFilepartDescr(2) );
		compareStream(instance, 2);
		instance.close();

		// split file consists 123 parts
		System.out.println("read 123 parts");
		instance = new SplitInFile( getFilepartDescr(123) );
		compareStream(instance, 123);
		instance.close();
		instance.close();
	}

	/**
	 * Compares streams, reads from its bytes until end, compares bates.
	 * @param splitfileStream
	 * @param count
	 * @throws Exception
	 */
	public void compareStream(SplitInFile splitfileStream, int count) throws Exception {
		InputStream filestream = new FileInputStream("agentFile.test");
		int bs, bf;
		int offset = 0;
		do {
			bs = splitfileStream.read();
			bf = filestream.read();
			assertEquals(bs, bf);
			offset++;
			if( ((offset % testfileSize) == 0) && --count != 0 )
				filestream = new FileInputStream("agentFile.test");
		} while(bs != -1);
	}

	/**
	 * Test of close method, of class SplitInFile.
	 */
	@Test
	public void testClose() throws Exception {
		System.out.println("close");
		SplitInFile instance = new SplitInFile( getFilepartDescr(123) );
		instance.close();
	}


}