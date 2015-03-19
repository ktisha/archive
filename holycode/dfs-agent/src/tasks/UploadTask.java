package tasks;

import dfsagent.datastructures.FileDescription;
import dfsagent.datastructures.FileList;
import dfsagent.datastructures.NoFreeSpaceDfsAgentException;
import serverlibrary.exception.TaskDfsException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.dfs.server.response.ResponseDocument;
import org.dfs.server.response.Agent;
import org.dfs.server.response.ResponseDocument.Response;

/**
 *
 * @author student
 */
public class UploadTask implements serverlibrary.task.Task {

	static private final int BUFFER_SIZE = 1024;
	private final int fileName;
	private final int fileSize;
	static private final String path = "./storage/";

	public UploadTask(int fileName, int fileSize) {
		this.fileName = fileName;
		this.fileSize = fileSize;
	}

	@Override
	public String doTask(InputStream in, OutputStream out) throws IOException {
		// create xml response
		ResponseDocument responseDocument = ResponseDocument.Factory.newInstance();
		Response response = responseDocument.addNewResponse();
		Agent agent = response.addNewAgent();

		try {
			// adding file to FileList
			FileList fList = FileList.GetInstance();
			FileDescription file = new FileDescription(fileName, fileSize, path + fileName);

			fList.insert(file);

			agent.setStatus("ok");
			

			serverlibrary.headerprovider.Writer.write(out, responseDocument.toString().getBytes());

			agent.setStatus("error");

			// uploading file
			FileOutputStream fileoutputstream = null;
			fileoutputstream = new FileOutputStream(path + fileName);

			for (int i = 0; i < fileSize; ++i) {
				int b = -1;
				try {
					b = in.read();
				} catch (IOException ex) {
					throw new FileNotFoundException(ex.getMessage());
				}
				if (b == -1) {
					throw new FileNotFoundException("Unexpected end of file. Only " + i + " bytes were read");
				}
				fileoutputstream.write(b);
			}


			agent.setStatus("ok");
			fileoutputstream.close();
		} catch (FileNotFoundException ex) {
			agent.setMessage(ex.getMessage());
		} catch (NoFreeSpaceDfsAgentException ex) {
			agent.setMessage(ex.getMessage());
		}


		return responseDocument.toString();
	}

	@Override
	public String toString() {
		return "UploadTask -- [filename=" + fileName + "] " +
				"[filesize=" + fileSize + "]";
	}
}
