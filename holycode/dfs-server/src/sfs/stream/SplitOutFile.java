package sfs.stream;

import sfs.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author an
 */
public class SplitOutFile extends OutputStream {
	static private Logger log = Logger.getLogger("MainLog");

	private boolean isClose = false;

	private Iterator<FilePartDescr> it;
	private FilePartDescr filepart;

	private int filepartOffset = 0;

	public SplitOutFile(List<FilePartDescr> filepartdescrList) {
		this.it = filepartdescrList.iterator();
		this.filepart = it.next();
	}

	@Override
	public void write(int b) throws IOException {
		if(isClose)
			throw new IOException("Attempted write to closed stream");
		if(filepartOffset == filepart.size) {
			// we read this filepart and need to read from the next filepart
			try {
				nextPart();
			} catch(NoSuchElementException ex) {
				isClose = true;
				throw new IOException("Attempted write to closed stream");
			}
		}
		filepart.outStream.write(b);
		filepartOffset++;
	}

	@Override
	public void close() throws IOException {
		isClose = true;
		filepart.outStream.close();
	}

	private void nextPart() throws NoSuchElementException, IOException {
		filepart.outStream.close();
		filepart = it.next();
		filepartOffset = 0;
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