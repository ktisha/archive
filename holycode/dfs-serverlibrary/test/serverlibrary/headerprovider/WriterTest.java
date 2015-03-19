/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverlibrary.headerprovider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author an
 */
public class WriterTest {

    public WriterTest() {
    }

	@Test
    public void testWrite() throws IOException {
		String str = "test";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Writer.write(out, str.getBytes());
		assertArrayEquals(Reader.read(new ByteArrayInputStream(out.toByteArray())),
						  str.getBytes());

		str = "";
		out = new ByteArrayOutputStream();
		Writer.write(out, str.getBytes());
		assertArrayEquals(Reader.read(new ByteArrayInputStream(out.toByteArray())),
						  str.getBytes());

		str = "sfj;asjfds;ajfs lksf \n\n\n\n\\nn\r\rr\rsff s\\ sd";
		out = new ByteArrayOutputStream();
		Writer.write(out, str.getBytes());
		assertArrayEquals(Reader.read(new ByteArrayInputStream(out.toByteArray())),
						  str.getBytes());
	}

}