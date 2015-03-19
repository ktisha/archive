package sfs.stream;

import sfs.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.dfs.server.Agent;
import org.dfs.server.RequestDocument;
import org.dfs.server.RequestDocument.Request;
import org.dfs.server.Upload;
import org.dfs.server.response.ResponseDocument;
import org.dfs.server.response.ResponseDocument.Response;

/**
 * A remote output stream is an output stream for writing data to a remote storage like to simple output srteam.
 * The stream will be automatically closed after writing RemoteFileDescr.size bytes.
 * @author an
 */
public class RemoteOutFile extends OutputStream {
	static private Logger log = Logger.getLogger("MainLog");
	static private final int TimeOut = 3000;

	private final RemoteFileDescr filedescr;

	private boolean isOpen = false;
	private boolean isClose = false;

	private InputStream in = null;
	private OutputStream out = null;
	private Socket socket = null;

	private int offset = 0;

	public RemoteOutFile(RemoteFileDescr filedescr) {
		this.filedescr = filedescr;
	}

	/**
	 * Writes byte to output stream to storage.
	 * @param b -the byte
	 * @throws IOException - if an I/O error occurs.
	 */
	@Override
	public void write(int b) throws IOException {
		open();
		out.write(b);
		offset++;
		// if it was the last byte, close the stream
		if(offset == filedescr.size) {
			log.info("auto closing");
			close();
		}
	}


	/**
	 * Closes this remote in file stream and releases any system resources associated with the stream.
	 * Reads response from storage that uploding successful is ok.
	 * @throws IOException - if an I/O error occurs.
	 */
	@Override
	public void close() throws IOException {
		if(isClose)
			return;
		if(isOpen) {
			isOpen = false;
			isClose = true;

			try {
				// read response that everything is ok on storage
				readResponse();
			} finally {
				try {
					if (out != null)
						out.close();
				} finally {
					try {
						if (out != null)
							in.close();
					} finally {
						if (out != null)
							socket.close();
					}
				}
			}
		}
	}


	private void open() throws IOException {
		if(isClose)
			throw new IOException("Try use closed RemoteInFile");
		if(isOpen)
			return;
		isOpen = true;
		try {
			// Open socket, get in/out stream
			openStream();
			// Construct request to storage
			RequestDocument requestDocument = makeRequest();
			// Send request
			//log.info(requestDocument.toString());
			serverlibrary.headerprovider.Writer.write(out, requestDocument.toString().getBytes());
			// Read and check response
			readResponse();
		} catch(IOException ex) {
			close();
			throw new IOException(ex);
		}
	}

	/**
	 * Reads response from socket, checks it.
	 * @throws IOException if some I/O error on reading
	 * @throws if response is not ok
	 */
	private void readResponse() throws IOException {
		// Reads from stream
		String strResponse = new String(serverlibrary.headerprovider.Reader.read(in));
		try {
			ResponseDocument responseDocument = ResponseDocument.Factory.parse(strResponse);
			Response response = responseDocument.getResponse();
			if(!response.isSetAgent())
				throw new XmlException("Bad response: not found tag 'agent'");
			if(!"ok".equals(response.getAgent().getStatus()))
				throw new IOException(response.getAgent().getStatus());
		} catch (XmlException ex) {
			log.log(Level.SEVERE, null, ex);
			throw new IOException(ex);
		}
	}

	/**
	 * Opens socket to storage agent, gets in/out streams from socket.
	 * @throws IOException if some I/O error
	 */
	private void openStream() throws IOException {
		// Open socket, get in/out stream
		try {
			socket = new Socket(filedescr.storage.host, filedescr.storage.port);
		} catch(UnknownHostException ex) {
			throw new IOException(ex);
		}
		// Set socket timeout
		try {
			socket.setSoTimeout(TimeOut);
		} catch (SocketException ex) {
			log.log(Level.SEVERE, "Can't set SoTimeout for socket");
			throw new RuntimeException(ex);
		}
		in = socket.getInputStream();
		out = socket.getOutputStream();
	}


	/**
	 * Makes request for storage agent, uses data from filedescr.
	 * @return request document
	 */
	private RequestDocument makeRequest() {
		// construct request to storage
		RequestDocument requestDocument = RequestDocument.Factory.newInstance();
		Request request = requestDocument.addNewRequest();
		Agent requestAgent = request.addNewAgent();
		requestAgent.setAction("upload");
		Upload downloadAction = requestAgent.addNewUpload();
		downloadAction.setFileId(new BigInteger("" + filedescr.id));
		downloadAction.setSize(new BigInteger("" + filedescr.size));

		return requestDocument;
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
