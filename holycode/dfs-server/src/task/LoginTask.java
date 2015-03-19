package task;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import serverlibrary.exception.TaskDfsException;
import serverlibrary.task.Task;
import dfsexceptions.DataBaseDfsException;
import dfsexceptions.LoginDfsException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import manager.dbmanager.DBManager;

import org.dfs.server.RequestDocument;
import org.dfs.server.RequestDocument.Request;
import org.dfs.server.response.*;
import org.dfs.server.response.Body;
import org.dfs.server.response.ResponseDocument.Response;

public class LoginTask implements Task {

	private String user;
	private String pass;

	LoginTask(String user, String pass) {
		this.user = user;
		this.pass = pass;
	}

//	public Response doTask(InputStream in, OutputStream out) throws DataBaseDfsException, LoginDfsException {
//		Response response = Response.Factory.newInstance();
//		Body body = Body.Factory.newInstance();
//
//		String sessionKey = DBManager.getInstance().login(user, pass);
//		response.setStatus("ok");
//		body.setSessionkey(sessionKey);
//		response.setBody(body);
//
//		return response;
//	}


	@Override
	public String toString() {
		String str = "LoginTask -- [user=" + user + "] [pass=" + pass + "]";
		return str;
	}

	public String doTask(InputStream in, OutputStream out) {
		// Create response
		ResponseDocument responseDocument = ResponseDocument.Factory.newInstance();
		Response response = responseDocument.addNewResponse();
		Client clientResponse = response.addNewClient();
		Body body = Body.Factory.newInstance();
		

		clientResponse.setStatus("error");
		try {
			// get session key from database
			String sessionKey = DBManager.GetInstance().login(user, pass);
			clientResponse.setStatus("ok");
			body.setSessionkey(sessionKey);
		} catch (DataBaseDfsException ex) {
			body.setMessage("Server error: " + ex.toString());
			Logger.getLogger(LoginTask.class.getName()).log(Level.SEVERE, null, ex);
		} catch (LoginDfsException ex) {
			body.setMessage("Client error: bad login or pass");
			Logger.getLogger(LoginTask.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		clientResponse.setBody(body);

		return responseDocument.toString();
	}
}
