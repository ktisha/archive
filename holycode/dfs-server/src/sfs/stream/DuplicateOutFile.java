package sfs.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author an
 */
public class DuplicateOutFile extends OutputStream {

	private final List<OutputStream> streamlist;
	private final List<OutputStream> alivestreamlist;

	private boolean isClose = false;
	
	public DuplicateOutFile(List<OutputStream> streamlist) {
		this.streamlist = streamlist;
		this.alivestreamlist = streamlist;
	}

	@Override
	public void write(int b) throws IOException {
		if(isClose)
			throw new IOException("Attempted write to closed stream");
		for(ListIterator<OutputStream> it = alivestreamlist.listIterator(); it.hasNext() ; ) {
			try {
				it.next().write(b);
			} catch(IOException ex) {
				it.remove();
				ex.printStackTrace();
			}
		}
		if(alivestreamlist.isEmpty())
			throw new IOException("Exception on the last output stream");
	}

	@Override
	public void close() throws IOException {
		IOException ex2 = null;
		if(isClose)
			return;
		isClose = true;
		int comleteFile = streamlist.size();
		for(OutputStream stream : streamlist) {
			try {
				stream.close();
			} catch(IOException ex) {
				comleteFile--;
				ex2 = ex;
			}
		}
		if(comleteFile == 0)
			throw new IOException(ex2); //"Exception on the closing last output strteam");
	}

}
