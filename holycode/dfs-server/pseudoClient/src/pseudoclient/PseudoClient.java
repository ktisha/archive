package pseudoclient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author an
 */
public class PseudoClient implements Runnable {

	private final Properties properties;

	public enum Command {

		DOWNLOAD,
		UPLOAD,
	}

	public PseudoClient(Properties properties) {
		this.properties = properties;
	}

	@Override
	public void run() {
		try {
			process();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void process() throws Exception {
		System.err.println("---------------------------------");
		System.err.println("---- Connect to " + properties.host + ":" + properties.port);
		Socket socket = new Socket(properties.host, properties.port);
		InputStream in = socket.getInputStream();
		OutputStream out = socket.getOutputStream();

		System.err.println("---- Reading request from " + properties.request);

		// Reading request from std in
		InputStream inRequest = new FileInputStream(properties.request);
		int ch;
		ByteArrayOutputStream byteArrayBuffer = new ByteArrayOutputStream();
		while ((ch = inRequest.read()) != -1) {
			byteArrayBuffer.write(ch);
		}

		System.err.println("---- Writing request to socket");
		// Writing request to socket
		serverlibrary.headerprovider.Writer.write(out, byteArrayBuffer.toByteArray());

		readResponse(in);

		if (properties.download != null) {
			System.err.println("---- Downloading file to '" + properties.download + "'");
			OutputStream outFile = new FileOutputStream(properties.download);
			while ((ch = in.read()) != -1) {
				outFile.write(ch);
			}
//			System.err.println("---- End downloding file to '" + properties.download + "'");
//			readResponse(in);

		} else if (properties.upload != null) {
			System.err.println("---- Uploading file '" + properties.upload + "'");
			InputStream inFile = new FileInputStream(properties.upload);
			while ((ch = inFile.read()) != -1) {
				out.write(ch);
			}
			System.err.println("---- End uploading file '" + properties.upload + "'");
			readResponse(in);
		}

		System.err.println("---------------------------------");
	}

	private void readResponse(InputStream in) throws IOException {
		System.err.println("---- Reading response from socket");
		// Reading response from socket
		byte[] bytes = serverlibrary.headerprovider.Reader.read(in);

		System.err.println("---- Writing response to std out");
		// Writing response to std out
		System.out.println(new String(bytes));
	}
}

class Properties {

	public Properties() {
	}
	public String request = null;
	public String upload = null;
	public String download = null;
	public String host = "localhost";
	public int port = 8000;
}
