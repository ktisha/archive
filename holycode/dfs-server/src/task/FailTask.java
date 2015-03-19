package task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.dfs.server.response.Body;
import org.dfs.server.response.Client;
import org.dfs.server.response.ResponseDocument;
import org.dfs.server.response.ResponseDocument.Response;
import serverlibrary.exception.TaskDfsException;

/**
 *
 * @author an
 */
class FailTask implements serverlibrary.task.Task {

	private final String msg;
	private final String str;

	FailTask(String msg) {
		this.msg = msg;
		// create xml response
        ResponseDocument responseDocument = ResponseDocument.Factory.newInstance();
        Response response = responseDocument.addNewResponse();
        Client client = response.addNewClient();
        client.setStatus("error");
		Body body = Body.Factory.newInstance();
		body.setMessage(msg);
		client.setBody(body);
		str = responseDocument.toString();
	}

	@Override
	public String doTask(InputStream in, OutputStream out) {
        return str;
	}

	@Override
	public String toString() {
		return "FailTask [msg='" + msg + "']";
	}
}
