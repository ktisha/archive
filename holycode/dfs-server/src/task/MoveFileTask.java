package task;

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
public class MoveFileTask implements serverlibrary.task.Task {

	private final String session;
	private final int fileId;
	private final int filelistId;

	MoveFileTask(String session, int fileId, int filelistId) {
		this.session = session;
		this.fileId = fileId;
		this.filelistId = filelistId;
	}

	public String doTask(InputStream in, OutputStream out) {
		ResponseDocument responseDocument = ResponseDocument.Factory.newInstance();
		Response response = responseDocument.addNewResponse();
		Client client = response.addNewClient();
		Body body = Body.Factory.newInstance();

		client.setStatus("error");

		try {
			final DBManager dbManager = DBManager.GetInstance();
			final SfsFileDescr sfsFileDescr = new SfsFileDescr();
			sfsFileDescr.id = fileId;
			sfsFileDescr.user = dbManager.getLogin(session).name;

			dbManager.moveFiledescr(sfsFileDescr, filelistId);

			client.setStatus("ok");
		} catch (LoginDfsException ex) {
			body.setMessage(ex.getMessage());
			client.setBody(body);
		} catch (DataBaseDfsException ex) {
			body.setMessage(ex.getMessage());
			client.setBody(body);
		}


		return responseDocument.toString();
	}
}
