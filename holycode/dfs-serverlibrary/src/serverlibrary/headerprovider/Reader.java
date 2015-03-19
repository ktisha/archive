package serverlibrary.headerprovider;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Reader provides read the body of messages.
 * It size is calculated from the header of the messages.
 * @author an
 */
public class Reader {
	// the size of header
	// ATTINTION: if you set size > 4
	// change type from int to long
	static private final int HEADER_SIZE = 4;

	/**
	 * Reads the header, calculates the size of body.
	 * Reads the body.
	 * @param in - The inputStream for reading.
	 * @return byte [] readed the body.
	 * @throws IOException if some I/O error occurs.
	 */
	static public byte [] read(InputStream in) throws IOException {
		int bodySize = readHeader(in);
		byte [] buffer = new byte[bodySize];

		int totalReadedNum = 0;
		byte [] tmpBytes = new byte[bodySize];

		// read from stream until read all needed bytes
		while(totalReadedNum < bodySize) {
			int readedNum = in.read(tmpBytes, 0, bodySize-totalReadedNum);
			for(int i = 0; i < readedNum; ++i)
				buffer[i + totalReadedNum] = tmpBytes[i];
			
			totalReadedNum += readedNum;
		}

		return buffer;
	}

	/**
	 * Reads the header, translate it to int
	 * @param in - The inputStream for reading.
	 * @return The size of next messages
	 * @throws IOException if some I/O error occurs.
	 */
	static private int readHeader(InputStream in) throws IOException {
		byte [] header = new byte[HEADER_SIZE];

		for(int i = 0; i < HEADER_SIZE; ++i)
			header[i] = (byte)in.read();
		int t0 = (int)header[0];
		int t1 = (int)header[1];
		int t2 = (int)header[2];
		int t3 = (int)header[3];


		ByteBuffer byteBuffer = ByteBuffer.wrap(header);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		int headerSize =  byteBuffer.getInt();
		if(headerSize < 0)
			headerSize = 0;
//		System.err.println("header val  = " + t0 + " " + t1 + " " + t2 + " " + t3 + " ");
//		System.err.println("header size = " + headerSize);
		return headerSize;
	}

}
