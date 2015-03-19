package task;

import serverlibrary.task.Task;
import dfsexceptions.DataBaseDfsException;
import dfsexceptions.LoginDfsException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import manager.dbmanager.DBManager;
import org.dfs.server.response.Body;
import org.dfs.server.response.Client;
import org.dfs.server.response.ResponseDocument;
import org.dfs.server.response.ResponseDocument.Response;
import sfs.SfsFileDescr;

/**
 *
 * @author an
 */
public class RemoveFileTask implements Task {

	private final DBManager dbManager = DBManager.GetInstance();
	private final String sessionkey;
	private final int fileId;

	RemoveFileTask(String sessionkey, int fileId) {
		this.sessionkey = sessionkey;
		this.fileId = fileId;
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
			sfsFileDescr.id = fileId;

			dbManager.moveDeadFiledescr(sfsFileDescr);
			client.setStatus("ok");

		} catch (DataBaseDfsException ex) {
			// TODO log it
			body.setMessage(ex.getMessage());
			client.setBody(body);
		} catch (LoginDfsException ex) {
			// TODO log it
			body.setMessage(ex.getMessage());
			client.setBody(body);
		}

		
		return responseDocument.toString();
	}

	@Override
	public String toString() {
		return "RemoveFileTask -- [fileId=" + fileId + "] " +
				"[sessionkey=" + sessionkey + "]";
	}
}
