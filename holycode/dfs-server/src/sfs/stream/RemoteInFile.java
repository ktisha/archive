package sfs.stream;

import sfs.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.dfs.server.Agent;
import org.dfs.server.Download;
import org.dfs.server.RequestDocument;
import org.dfs.server.RequestDocument.Request;
import org.dfs.server.response.ResponseDocument;
import org.dfs.server.response.ResponseDocument.Response;

/**
 *
 * @author an
 */
public class RemoteInFile extends InputStream {
	static private Logger log = Logger.getLogger("MainLog");

	private final RemoteFileDescr filepart;

	private boolean isOpen = false;
	private boolean isClose = false;
	private int offset = 0;

	private InputStream in = null;
	private OutputStream out = null;
	private Socket socket = null;


	public RemoteInFile(RemoteFileDescr filepart) {
		this.filepart = filepart;
	}

	/**
	 * Reads the next byte of data from the input stream.
	 * The value byte is returned as an int in the range 0 to 255.
	 * If no byte is available because the end of the stream has been reached,
	 * the value -1 is returned. This method blocks until input data is available,
	 * the end of the stream is detected, or an exception is thrown.
	 * @return the next byte of data, or -1 if the end of the stream is reached.
	 * @throws IOException - if an I/O error occurs.
	 */
	@Override
	public int read() throws IOException {
		open();
		if(offset++ == filepart.size)
			return -1;
		return in.read();
	}


	/**
	 * Closes this remote in file stream and releases any system resources associated with the stream.
	 * @throws IOException - if an I/O error occurs.
	 */
	@Override
	public void close() throws IOException {
		if(isOpen) {
			isOpen = false;
			isClose = true;
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
	

	/**
	 * Opens socket to storage agent, gets in/out streams from socket.
	 * @throws IOException if some I/O error
	 */
	private void openStream() throws IOException {
		// Open socket, get in/out stream
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(filepart.storage.host, filepart.storage.port), 5000);
		} catch(UnknownHostException ex) {
			throw new IOException(ex);
		}
		in = socket.getInputStream();
		out = socket.getOutputStream();
	}

	/**
	 * Makes request for storage agent, uses data from filepart.
	 * @return request document
	 */
	private RequestDocument makeRequest() {
		// construct request to storage
		RequestDocument requestDocument = RequestDocument.Factory.newInstance();
		Request request = requestDocument.addNewRequest();
		Agent requestAgent = request.addNewAgent();
		requestAgent.setAction("download");
		Download downloadAction = requestAgent.addNewDownload();
		downloadAction.setFileId(new BigInteger("" + filepart.id));

		return requestDocument;
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
				throw new IOException("Unexpected response: " + response.getAgent().getStatus());
		} catch (XmlException ex) {
			log.log(Level.SEVERE, null, ex);
			throw new IOException(ex);
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

	@Override
	protected void finalize() throws Throwable {
		try {
			close();
		} finally {
			super.finalize();
		}
	}
}
