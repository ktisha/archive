package task;

import java.util.logging.Level;
import java.util.logging.Logger;
import serverlibrary.task.Task;
import FileDiscriptor.FileDiscriptor;
import dfsexceptions.DataBaseDfsException;
import dfsexceptions.LoginDfsException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.List;
import manager.dbmanager.DBManager;
import org.dfs.server.response.Body;
import org.dfs.server.response.Client;
import org.dfs.server.response.File;
import org.dfs.server.response.ResponseDocument;
import org.dfs.server.response.ResponseDocument.Response;

public class SearchTask implements Task {

	private final DBManager dbManager = DBManager.GetInstance();
	private final String sessionkey;
	private final String query;
	private final int filelistId;

	SearchTask(String sessionkey, String query, int filelistId) {
		this.sessionkey = sessionkey;
		this.query = query;
		this.filelistId = filelistId;
	}

	public String doTask(InputStream in, OutputStream out) throws IOException {
		ResponseDocument responseDocument = ResponseDocument.Factory.newInstance();
		Response response = responseDocument.addNewResponse();
		Client client = response.addNewClient();
		Body body = Body.Factory.newInstance();

		client.setStatus("error");
		try {
			String login = dbManager.getLogin(sessionkey).name;
			List<FileDiscriptor> fileDiscriptionList = dbManager.getFiles(login, query, filelistId);

			for (FileDiscriptor fileDiscription : fileDiscriptionList) {
				File file = body.addNewFile();
				file.setId(new BigInteger("" + fileDiscription.id));
				file.setFilename(fileDiscription.name);
				file.setFilelist(fileDiscription.fileList);
				file.setFilesize(new BigInteger("" + fileDiscription.size));
				file.setCopy(new BigInteger("" + fileDiscription.copy));
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

	@Override
	public String toString() {
		String str = "SearchTask -- [query=" + query + "] [filelistId=" +
				filelistId + "] [sessionkey=" + sessionkey + "]";
		return str;
	}
}
