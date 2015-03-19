package tasks;

import dfsagent.datastructures.FileDescription;
import dfsagent.datastructures.FileList;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import org.dfs.server.response.AFile;
import org.dfs.server.response.Agent;
import org.dfs.server.response.ResponseDocument;
import org.dfs.server.response.ResponseDocument.Response;

/**
 *
 * @author an
 */
public class AllFilesTask implements serverlibrary.task.Task {

	@Override
	public String doTask(InputStream in, OutputStream out) {
		// create xml response
		ResponseDocument responseDocument = ResponseDocument.Factory.newInstance();
		Response response = responseDocument.addNewResponse();
		Agent agent = response.addNewAgent();
		agent.setStatus("ok");

		for (FileDescription fileDescription : FileList.GetInstance().getAllFiles()) {
			AFile afile = agent.addNewAFile();
			afile.setId(new BigInteger("" + fileDescription.fileName));
		}

		return responseDocument.toString();
	}

	@Override
	public String toString() {
		return "AllFiles task";
	}
}
