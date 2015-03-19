package serverlibrary.task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Task {

	/**
	 * Does task, constructs xml response
	 * @param in - InputStream from socket
	 * @param out - OutputStream to socket
	 * @return xml response
	 * @throws IOException - if some error
	 */
	public String doTask(InputStream in, OutputStream out) throws IOException;

}
