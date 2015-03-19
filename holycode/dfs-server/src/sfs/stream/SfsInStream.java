package sfs.stream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import sfs.FilePartDescr;
import sfs.RemoteFileDescr;
import sfs.SfsFileDescr;
import sfs.SfsFilePartDescr;

/**
 *
 * @author an
 */
public class SfsInStream extends InputStream {

	private final int id;
	private final SplitInFile inStream;

	public SfsInStream(SfsFileDescr sfsFileDescr) {
		id = sfsFileDescr.id;

		List<FilePartDescr> filepartdescrList = new ArrayList<FilePartDescr>();

		int i = 0;
		for (SfsFilePartDescr sfsFilePartDescr : sfsFileDescr.fileparts) {

			FilePartDescr filePart = new FilePartDescr();
			List<InputStream> inStreamList = new ArrayList<InputStream>();

			for (RemoteFileDescr remoteFileDescr : sfsFilePartDescr.remotedescrs) {
				inStreamList.add(new RemoteInFile(remoteFileDescr));
				filePart.id = remoteFileDescr.id;
			}

			filePart.inStream = new DuplicateInFile(inStreamList);
			filePart.size = sfsFilePartDescr.size;
			filePart.num = i;

			filepartdescrList.add(filePart);

			i++;
		}
		inStream = new SplitInFile(filepartdescrList);
	}

	@Override
	public int read() throws IOException {
		return inStream.read();
	}

	@Override
	public void close() throws IOException {
		inStream.close();
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
