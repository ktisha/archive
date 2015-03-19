package sfs.stream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author an
 */
public class DuplicateInFile extends InputStream {

	static private Logger log = Logger.getLogger("MainLog");
	private final List<InputStream> streamlist;
	private Iterator<InputStream> it;
	private InputStream stream;
	private int offset = 0;

	public DuplicateInFile(List<InputStream> streamlist) {
		this.streamlist = streamlist;
		this.it = streamlist.iterator();
		this.stream = it.next();
	}

	@Override
	public int read() throws IOException {
		while (true) {
			try {
				int b = stream.read();
				offset++;
				return b;
			} catch (IOException ex) {
				nextStream();
			}
		}
	}

	@Override
	public void close() throws IOException {
		stream.close();
	}

	/**
	 * Closes current stream, opens next stream and skip readed bytes.
	 * @throws IOException - if no such streams in the pool.
	 */
	private void nextStream() throws IOException {
		try {
			System.err.println("switch next stream");
			try {
				stream.close();
			} catch (IOException ex) {
				log.log(Level.WARNING, "", ex);
			}
			stream = it.next();
			stream.skip(offset);
		} catch (NoSuchElementException ex) {
			throw new IOException("IO error on reading from the last stream");
		}
	}
}
