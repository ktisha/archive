package task;

import serverlibrary.task.Task;
import dfsexceptions.DataBaseDfsException;
import dfsexceptions.LoginDfsException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import manager.dbmanager.DBManager;
import org.dfs.server.response.Body;
import org.dfs.server.response.Client;
import org.dfs.server.response.ResponseDocument;
import org.dfs.server.response.ResponseDocument.Response;
import sfs.FSException;
import sfs.SfsFileDescr;
import sfs.stream.SfsInStream;

public class DownloadTask implements Task {

	static private final int BUFFER_SIZE = 1024;
	private final DBManager dbManager = DBManager.GetInstance();
	private final String sessionkey;
	private final int fileId;
	private final int offset;

	DownloadTask(String sessionkey, int fileId, int offset) {
		this.sessionkey = sessionkey;
		this.fileId = fileId;
		this.offset = offset;
	}

	@Override
	public String doTask(InputStream in, OutputStream out) throws IOException {
		ResponseDocument responseDocument = ResponseDocument.Factory.newInstance();
		Response response = responseDocument.addNewResponse();
		Client client = response.addNewClient();
		Body body = Body.Factory.newInstance();


		client.setStatus("error");
		try {
			String login = dbManager.getLogin(sessionkey).name;

			SfsFileDescr sfsFileDescr = dbManager.getFileDescr(login, fileId);

			SfsInStream sfsInStream = new SfsInStream(sfsFileDescr);

			
			client.setStatus("ok");
			
			serverlibrary.headerprovider.Writer.write(out, responseDocument.toString().getBytes());
			System.err.println(responseDocument.toString());

			// write file to socket
			for (int i = 0; i < sfsFileDescr.size; ++i) {
				int b;
				try {
					b = sfsInStream.read();
				} catch (IOException ex) {
					client.setStatus("error");
					throw new FSException(ex);
				}
				if (b == -1) {
					throw new FSException("Unexpected end of file");
				}
				out.write(b);
			}

			System.err.println("File sent");

			return null;

		} catch (FSException ex) {
			// TODO log it
			body.setMessage(ex.getMessage());
		} catch (DataBaseDfsException ex) {
			// TODO log it
			body.setMessage(ex.getMessage());
		} catch (LoginDfsException ex) {
			// TODO log it
			body.setMessage(ex.getMessage());
		}

		client.setBody(body);

		return responseDocument.toString();
	}

	@Override
	public String toString() {
		return "DownloadTask -- [fileId=" + fileId + "] " +
				"[sessionkey=" + sessionkey + "]";
	}
}
