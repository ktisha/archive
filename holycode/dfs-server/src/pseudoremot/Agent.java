package pseudoremot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xmlbeans.XmlException;
import org.dfs.server.RequestDocument;
import org.dfs.server.RequestDocument.Request;
import org.dfs.server.response.ResponseDocument;
import org.dfs.server.response.ResponseDocument.Response;


public class Agent implements Runnable {

	private final int port;
	public String requestType = null;
	public int offset = 0;
	public int fileId = 0;
	public int fileSize = 0;
	public String expectedRes = "ok";

	public volatile byte [] readedBytes = null;


	public Agent(int port) {
		this.port = port;
	}

	public void run() {
		OutputStream out = null;
		ServerSocket serverSocket = null;
		try {
			System.err.println("Starting listening on "+port+" port...");

			try {
				serverSocket = new ServerSocket(port);
			} catch (IOException e) {
				System.err.println("Could not listen on port: "+port);
				System.exit(1);
			}
			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
			out = clientSocket.getOutputStream();
			InputStream in = clientSocket.getInputStream();


			// Read request
			byte[] bytes = serverlibrary.headerprovider.Reader.read(in);
			String str = new String(bytes);


			

			// Parse request
			RequestDocument requestDoc = RequestDocument.Factory.parse(new String(bytes));
			Request request = requestDoc.getRequest();
			requestType = request.getAgent(). getAction();

			System.err.println(request.getAgent().getAction());

			if ("download".compareTo(request.getAgent().getAction()) == 0) {
				fileId = request.getAgent().getDownload().getFileId().intValue();
				if(request.getAgent().getDownload().isSetOffset())
					offset = request.getAgent().getDownload().getOffset().intValue();

				// Create response
				ResponseDocument responseDoc = ResponseDocument.Factory.newInstance();
				Response response = responseDoc.addNewResponse();
				org.dfs.server.response.Agent agentResponse = response.addNewAgent();
				agentResponse.setStatus(expectedRes);
//				agentResponse.
				

				// Send response
				serverlibrary.headerprovider.Writer.write(out, responseDoc.toString().getBytes());


				// Open file
				FileInputStream fileinputstream = null;

				fileinputstream = new FileInputStream("agentFile.test");
				byte bytearray[] = new byte[10];

				while (fileinputstream.available() > 0) {
					int bytesReadNum = fileinputstream.read(bytearray);
					out.write(bytearray, 0, bytesReadNum);
					//System.err.print(new String(bytearray));
				}
			} else if("upload".compareTo(request.getAgent().getAction()) == 0) {
				Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, "1");
				fileId = request.getAgent().getUpload().getFileId().intValue();
				fileSize = request.getAgent().getUpload().getSize().intValue();

				// Create response
				ResponseDocument responseDoc = ResponseDocument.Factory.newInstance();
				Response response = responseDoc.addNewResponse();
				org.dfs.server.response.Agent agentResponse = response.addNewAgent();
				agentResponse.setStatus(expectedRes);

				// Send response
				serverlibrary.headerprovider.Writer.write(out, responseDoc.toString().getBytes());

				// Read data
				readedBytes = new byte[fileSize];

				for(int i = 0; i < fileSize; ++i)
					readedBytes[i] = (byte)in.read();

				Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, "send last response");
				// Send response
				serverlibrary.headerprovider.Writer.write(out, responseDoc.toString().getBytes());

			} else if ("free-space".compareTo(request.getAgent().getAction()) == 0) {
				// Create response
				ResponseDocument responseDoc = ResponseDocument.Factory.newInstance();
				Response response = responseDoc.addNewResponse();
				org.dfs.server.response.Agent agentResponse = response.addNewAgent();
				agentResponse.setFreeSpace(new BigInteger("123"));

				// Send response
				serverlibrary.headerprovider.Writer.write(out, responseDoc.toString().getBytes());

			} else {
				throw new Exception("Unexpected action");
			}

			out.close();
			in.close();
			clientSocket.close();
			serverSocket.close();
		} catch (XmlException ex) {
			Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
		} catch (Exception ex) {
			Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				out.close();
				serverSocket.close();
			} catch (IOException ex) {
				Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
