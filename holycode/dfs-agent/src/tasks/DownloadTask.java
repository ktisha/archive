package tasks;

import dfsagent.datastructures.FileDescription;
import dfsagent.datastructures.FileList;
import serverlibrary.exception.TaskDfsException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
public class DownloadTask implements serverlibrary.task.Task {

	private final int fileName;
	static private final int BUFFER_SIZE = 1024;

	public DownloadTask(int fileName) {
		this.fileName = fileName;
	}

	@Override
	public String doTask(InputStream in, OutputStream out) throws IOException {
		// create xml response
		ResponseDocument responseDocument = ResponseDocument.Factory.newInstance();
		Response response = responseDocument.addNewResponse();
		Agent agent = response.addNewAgent();
		agent.setStatus("ok");

		

		// downloading file
		FileInputStream fileinputstream = null;
		try {
			FileDescription fileDescription = FileList.GetInstance().getFile(fileName);

			if (fileDescription == null) {
				throw new IllegalStateException("File not found");
			}

			serverlibrary.headerprovider.Writer.write(out, responseDocument.toString().getBytes());

			fileinputstream = new FileInputStream(fileDescription.path);
			//byte bytearray[] = new byte[BUFFER_SIZE];
//
//			while (fileinputstream.available() > 0) {
//				int bytesReadNum = fileinputstream.read(bytearray);
//				out.write(bytearray, 0, bytesReadNum);
//			}
			while (true) {

				int b = -1;
				try {
					b = fileinputstream.read();
				} catch (IOException ex) {
					throw new IllegalStateException(ex);
				}

				if (b == -1) {
					break;
				}
				out.write(b);
			}
			
			return null;
		} catch (IllegalStateException ex) {
			agent.setMessage("File not found");
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			agent.setMessage("File not found in file system");
			// TODO log it 
		} finally {
			try {
				if (fileinputstream != null) {
					fileinputstream.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				// TODO log it
			}
		}
		agent.setStatus("error");

		return responseDocument.toString();
	}

	@Override
	public String toString() {
		return "DownloadTask -- [filePath=" + fileName + "]";
	}
}
