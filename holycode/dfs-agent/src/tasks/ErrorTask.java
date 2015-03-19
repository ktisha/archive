/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tasks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.dfs.server.response.Agent;
import org.dfs.server.response.ResponseDocument;
import org.dfs.server.response.ResponseDocument.Response;

/**
 *
 * @author student
 */
public class ErrorTask implements serverlibrary.task.Task {
	private final String msg;

	ErrorTask(String msg) {
		this.msg = msg;
	}

	@Override
	public String doTask(InputStream in, OutputStream out) {
		// create xml response
		ResponseDocument responseDocument = ResponseDocument.Factory.newInstance();
		Response response = responseDocument.addNewResponse();
		Agent agent = response.addNewAgent();
		agent.setStatus("error");
		agent.setMessage(msg);
		
		return responseDocument.toString();
	}

	@Override
	public String toString() {
		return "Error task";
	}
}
