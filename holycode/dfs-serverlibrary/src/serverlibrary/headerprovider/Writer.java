package serverlibrary.headerprovider;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Class Writer provides to write byte [] to stream with the header.
 * @author an
 */
public class Writer {
	// the size of header
	// ATTENTION: if you set size > 4
	// change type from int to long
	static private final int HEADER_SIZE = 4;

	/**
	 * Writes the header of messages with the size if messages.
	 * Writes the bytes to stream.
	 * @param out - otputStream for writing.
	 * @param bytes - byte [] for writing to stream.
	 * @throws IOException if some I/O error occurs.
	 */
	static public void write(OutputStream out, byte [] bytes) throws IOException {
		int size = bytes.length;
		ByteBuffer bb = ByteBuffer.allocate(HEADER_SIZE);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putInt(size);
		out.write(bb.array());
		out.write(bytes);
	}

}
