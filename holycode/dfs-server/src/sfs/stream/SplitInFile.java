package sfs.stream;

import sfs.*;
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
public class SplitInFile extends InputStream {
	static private Logger log = Logger.getLogger("MainLog");

	private boolean isClose = false;

	private Iterator<FilePartDescr> it;
	private FilePartDescr filepart;

	private int filepartOffset = 0;

	public SplitInFile(List<FilePartDescr> filepartdescrList) {
		this.it = filepartdescrList.iterator();
		this.filepart = it.next();
	}

	@Override
	public int read() throws IOException {
		if(isClose)
			throw new IOException("Attempted read on closed stream");
		if(filepartOffset == filepart.size) {
			// we read this filepart and need to read from the next filepart
			try {
				nextPart();
			} catch(NoSuchElementException ex) {
				return -1;
			}
		}
		int b = filepart.inStream.read();
		filepartOffset++;
		return b;
	}

	@Override
	public void close() throws IOException {
		isClose = true;
		filepart.inStream.close();
	}

	/**
	 * Closes current stream, opens next.
	 * @throws NoSuchElementException - if stream has no more parts.
	 */
	private void nextPart() throws NoSuchElementException {
		try {
			filepart.inStream.close();
			filepart = it.next();
			filepartOffset = 0;
		} catch(IOException ex) {
			log.log(Level.WARNING, "", ex);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			close();
		} finally {
			super.finalize();
		}
	}

}
