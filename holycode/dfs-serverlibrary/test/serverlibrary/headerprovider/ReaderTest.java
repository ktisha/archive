/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverlibrary.headerprovider;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author an
 */
public class ReaderTest {
	private ByteArrayInputStream in = null;

	private void make(byte [] bytes) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putInt(bytes.length);

		byte [] b = new byte[4 + bytes.length];
		for(int i = 0; i < 4; ++i)
			b[i] = bb.get(i);
		for(int i = 0; i < 4; ++i)
			b[i] = bb.get(i);
		for(int i = 0; i < bytes.length; ++i)
			b[i+4] = bytes[i];
		in = new ByteArrayInputStream(b);
	}

	/**
	 * Test of read method, of class Reader.
	 */
	@Test
	public void testRead() throws Exception {
		String str = new String("test");
		make(str.getBytes());
		assertEquals( 0, (new String(Reader.read(this.in))).compareTo(str) );

		str = new String("Hello, very very big string \r\r");
		make(str.getBytes());
		assertEquals( 0, (new String(Reader.read(this.in))).compareTo(str) );

		str = new String("q");
		make(str.getBytes());
		assertEquals( 0, (new String(Reader.read(this.in))).compareTo(str) );

		str = new String("");
		make(str.getBytes());
		assertEquals( 0, (new String(Reader.read(this.in))).compareTo(str) );

	}

}