package task;

import java.util.logging.Level;
import java.util.logging.Logger;
import serverlibrary.exception.TaskDfsException;
import serverlibrary.task.Task;
import dfsexceptions.DataBaseDfsException;
import dfsexceptions.LoginDfsException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.List;
import manager.dbmanager.DBManager;
import org.dfs.server.response.Body;
import org.dfs.server.response.Client;
import org.dfs.server.response.Filelist;
import org.dfs.server.response.ResponseDocument;
import org.dfs.server.response.ResponseDocument.Response;

public class GetListTask implements Task {

	private DBManager dbManager = null;
	private String sessionkey = null;

	public GetListTask(String sessionkey) {
		this.sessionkey = sessionkey;
	}

//	public Response doTask(InputStream in, OutputStream out) throws DataBaseDfsException, LoginDfsException {
//		dbManager = DBManager.getInstance();
//		String login = dbManager.getLogin(sessionkey).name;
//
//		Response response = Response.Factory.newInstance();
//		Body body = Body.Factory.newInstance();
//
//		List<FileDiscriptor.FileList> filelists = dbManager.getAllFilelist(login);
//		for(FileDiscriptor.FileList filelist : filelists) {
//			Filelist filelistElement = body.addNewFilelist();
//			filelistElement.setFilelistname(filelist.name);
//			filelistElement.setId(new BigInteger("" + filelist.id));
//		}
//
//
//		response.setStatus("ok");
//		response.setBody(body);
//
//		return response;
//	}
	@Override
	public String toString() {
		String str = "GetListTask -- [sessionkey=" + sessionkey + "]";
		return str;
	}

	public String doTask(InputStream in, OutputStream out) {// throws TaskDfsException {
		ResponseDocument responseDocument = ResponseDocument.Factory.newInstance();
		Response response = responseDocument.addNewResponse();
		Client client = response.addNewClient();
		Body body = Body.Factory.newInstance();

		client.setStatus("error");
		try {
			dbManager = DBManager.GetInstance();
			String login = dbManager.getLogin(sessionkey).name;

			List<FileDiscriptor.FileList> filelists = dbManager.getAllFilelist(login);
			for (FileDiscriptor.FileList filelist : filelists) {
				Filelist filelistElement = body.addNewFilelist();
				filelistElement.setFilelistname(filelist.name);
				filelistElement.setId(new BigInteger("" + filelist.id));
			}

			client.setStatus("ok");

		} catch (DataBaseDfsException ex) {
			Logger.getLogger(GetListTask.class.getName()).log(Level.SEVERE, null, ex);
			body.setMessage(ex.getMessage());
		} catch (LoginDfsException ex) {
			Logger.getLogger(GetListTask.class.getName()).log(Level.SEVERE, null, ex);
			body.setMessage(ex.getMessage());
		}
		client.setBody(body);
		return responseDocument.toString();
	}
}
