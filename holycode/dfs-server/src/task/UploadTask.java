package task;

import serverlibrary.task.Task;
import dfsexceptions.DataBaseDfsException;
import dfsexceptions.LoginDfsException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import manager.dbmanager.DBManager;
import org.dfs.server.response.Body;
import org.dfs.server.response.Client;
import org.dfs.server.response.ResponseDocument;
import org.dfs.server.response.ResponseDocument.Response;
import sfs.FSException;
import sfs.SfsFileDescr;
import sfs.stream.SfsOutStream;

public class UploadTask implements Task {

	private final DBManager dbManager = DBManager.GetInstance();
	private final String sessionkey;
	private final String filename;
	private final int filesize;
	private final int filelistId;
	private final int copy;

	UploadTask(String sessionkey, String filename, int filesize, int copy,
			int filelistId) {
		this.sessionkey = sessionkey;
		this.filename = filename;
		this.filesize = filesize;
		this.filelistId = filelistId;
		this.copy = copy;
	}

	public String doTask(InputStream in, OutputStream out) throws IOException {
		ResponseDocument responseDocument = ResponseDocument.Factory.newInstance();
		Response response = responseDocument.addNewResponse();
		Client client = response.addNewClient();
		Body body = Body.Factory.newInstance();

		client.setStatus("error");

		try {
			String user = dbManager.getLogin(sessionkey).name;

			SfsFileDescr sfsFileDescr = new SfsFileDescr();
			sfsFileDescr.user = user;
			sfsFileDescr.copy = copy;
			sfsFileDescr.filelistId = filelistId;
			sfsFileDescr.size = filesize;
			sfsFileDescr.name = filename;

			dbManager.createFiledescr(sfsFileDescr);
			SfsOutStream outStream = new SfsOutStream(sfsFileDescr);

			try {
				// write response to socket
				org.dfs.server.response.File file = org.dfs.server.response.File.Factory.newInstance();
				file.setId(new BigInteger("" + sfsFileDescr.id));
				body.setFile(file);
				client.setStatus("ok");
				client.setBody(body);
				serverlibrary.headerprovider.Writer.write(out, responseDocument.toString().getBytes());
				client.setStatus("error");
				
				for (int i = 0; i < sfsFileDescr.size; ++i) {
					int b = in.read();
					if (b == -1) {
						throw new IOException("Unexpected end of file from input stream");
					}
					outStream.write(b);
				}

				outStream.close();
				client.setStatus("ok");
			} catch (SocketTimeoutException ex) {
				// TODO log it
				dbManager.moveDeadFiledescr(sfsFileDescr);
				body.unsetFile();
				body.setMessage("Timeout");
			} catch (IOException ex) {
				// TODO log it
				Logger.getLogger(UploadTask.class.getName()).log(Level.SEVERE, null, ex);
				body.setMessage("Error on writing to storage: " + ex.getMessage());
				dbManager.moveDeadFiledescr(sfsFileDescr);
				body.unsetFile();
				body.setMessage(ex.getMessage());
			}

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
		String str = "UploadTask -- [filename=" + filename + "] " +
				"[filelistId=" + filelistId + "] [sessionkey=" + sessionkey + "]";
		return str;
	}
}
