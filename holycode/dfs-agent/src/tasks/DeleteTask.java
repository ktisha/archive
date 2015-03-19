package tasks;

import dfsagent.datastructures.FileDescription;
import dfsagent.datastructures.FileList;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import org.dfs.server.response.Agent;
import org.dfs.server.response.ResponseDocument;
import org.dfs.server.response.ResponseDocument.Response;

/**
 *
 * @author student
 */
public class DeleteTask implements serverlibrary.task.Task {

	private final int fileName;

	public DeleteTask(int fileName) {
		this.fileName = fileName;
	}

	@Override
	public String doTask(InputStream in, OutputStream out) {
		// create xml response
		ResponseDocument responseDocument = ResponseDocument.Factory.newInstance();
		Response response = responseDocument.addNewResponse();
		Agent agent = response.addNewAgent();
		agent.setStatus("ok");

		FileDescription fileDescription = FileList.GetInstance().getFile(fileName);
		if (fileDescription == null) {
			agent.setStatus("error");
			agent.setMessage("File '" + fileName + "' doesn't exist");
		} else {
			// delete fom filelist
			FileList.GetInstance().delete(fileName);
			// delete file from filesystem
			try {
				deleteFile(fileDescription.path);
			} catch (IllegalArgumentException ex) {
				agent.setStatus("error");
				agent.setMessage("Can't delete file '" + fileName + "' from filesystem. " + ex.getMessage());
				// TODO log it
			}
		}
		return responseDocument.toString();
	}

	/**
	 * Deletes file from filesystem.
	 * @throws IllegalArgumentException if some error on deleting
	 */
	private void deleteFile(String filePath) throws IllegalArgumentException {
		// A File object to represent the filename
		File f = new File(filePath);

		// Make sure the file or directory exists and isn't write protected
		if (!f.exists()) {
			throw new IllegalArgumentException("No such file");
		}
		if (!f.canWrite()) {
			throw new IllegalArgumentException("Write protected");
		}

		// Attempt to delete it
		boolean success = f.delete();
		if (!success) {
			throw new IllegalArgumentException("Unknown error");
		}
	}

	@Override
	public String toString() {
		return "DeleteTask -- [filePath=" + fileName + "]";
	}
}
