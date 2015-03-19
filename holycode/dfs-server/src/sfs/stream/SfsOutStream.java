package sfs.stream;

import java.io.IOException;
import java.io.OutputStream;
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
public class SfsOutStream extends OutputStream {

	private final int id;
	//private final SplitInFile inStream;
	private final SplitOutFile outStream;

	public SfsOutStream(SfsFileDescr sfsFileDescr) {
		id = sfsFileDescr.id;

		List<FilePartDescr> filepartdescrList = new ArrayList<FilePartDescr>();

		int i = 0;
		for (SfsFilePartDescr sfsFilePartDescr : sfsFileDescr.fileparts) {

			FilePartDescr filePart = new FilePartDescr();
			//List<InputStream> inStreamList = new ArrayList<InputStream>();
			List<OutputStream> outStreamList = new ArrayList<OutputStream>();

			for (RemoteFileDescr remoteFileDescr : sfsFilePartDescr.remotedescrs) {
				outStreamList.add(new RemoteOutFile(remoteFileDescr));
				filePart.id = remoteFileDescr.id;
			}

			filePart.outStream = new DuplicateOutFile(outStreamList);
			filePart.size = sfsFilePartDescr.size;
			filePart.num = i;

			filepartdescrList.add(filePart);

			i++;
		}
		outStream = new SplitOutFile(filepartdescrList);
	}


	@Override
	public void close() throws IOException {
		outStream.close();
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			close();
		} finally {
			super.finalize();
		}
	}

	@Override
	public void write(int b) throws IOException {
		outStream.write(b);
	}
}
