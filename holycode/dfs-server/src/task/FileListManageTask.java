package task;

import java.util.logging.Level;
import java.util.logging.Logger;
import serverlibrary.task.Task;
import dfsexceptions.DataBaseDfsException;
import dfsexceptions.LoginDfsException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import manager.dbmanager.DBManager;
import org.apache.xmlbeans.XmlException;
import org.dfs.server.response.Body;
import org.dfs.server.response.Client;
import org.dfs.server.response.Filelist;
import org.dfs.server.response.ResponseDocument;
import org.dfs.server.response.ResponseDocument.Response;

/**
 *
 * @author an
 */
public class FileListManageTask implements Task {

	private final String sessionkey;
	private final Action action;
	private final DBManager dbManager = DBManager.GetInstance();
	private String filelistName;
	private int filelistId;

	private enum Action {

		create,
		remove,
	}

	FileListManageTask(String sessionkey, String action, org.dfs.server.ListAction listAction) throws XmlException {
		this.sessionkey = sessionkey;

		if ("create".equals(action)) {
			this.action = Action.valueOf(action);
			if (!listAction.isSetFilelistname()) {
				throw new XmlException("Request error: not found tag 'filelistname'");
			}
			filelistName = listAction.getFilelistname();
		} else if ("remove".equals(action)) {
			this.action = Action.valueOf(action);
			if (!listAction.isSetId()) {
				throw new XmlException("Request error: not found tag 'id'");
			}
			filelistId = listAction.getId().intValue();
		} else {
			throw new XmlException("Request error: unknown action '" + action + "' for 'list-action'");
		}

	}

	@Override
	public String toString() {
		String str = "FileListManageTask -- [filelistName=" + filelistName +
				"] [action=" + action + "] [sessionkey=" + sessionkey + "]";
		return str;
	}

	public String doTask(InputStream in, OutputStream out) throws IOException {
		ResponseDocument responseDocument = ResponseDocument.Factory.newInstance();
		Response response = responseDocument.addNewResponse();
		Client client = response.addNewClient();
		Body body = Body.Factory.newInstance();

		client.setStatus("error");
		try {
			int userId = dbManager.getLogin(sessionkey).id;

			if (action == Action.create) {
				Filelist filelist = Filelist.Factory.newInstance();
				filelist.setId(new BigInteger("" + dbManager.createFileList(userId, filelistName)));
				filelist.setFilelistname(filelistName);
				body.setFilelist(filelist);
			} else if (action == Action.remove) {
				dbManager.removeFilelist(userId, filelistId);
			}
			
			client.setStatus("ok");
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
}
