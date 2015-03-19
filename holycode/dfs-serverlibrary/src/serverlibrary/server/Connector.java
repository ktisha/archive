package serverlibrary.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import serverlibrary.task.Task;
import serverlibrary.exception.TaskDfsException;

class Connector implements Runnable {

	static final private int TimeOut = 5000;
	static private Logger log = Logger.getLogger("server");
	private final serverlibrary.task.TaskFactory taskFactory;
	private SocketChannel socketChannel = null;
	private Socket socket = null;
	private OutputStream out = null;
	private InputStream in = null;

	public Connector(SocketChannel socketChannel, serverlibrary.task.TaskFactory taskFactory) {
		this.socketChannel = socketChannel;
		this.taskFactory = taskFactory;
		this.socket = socketChannel.socket();
	}

	/**
	 * Reads request from socket, build task, executes it, creates response document.
	 * @param in -- InputStream for reading request
	 * @param out -- OutputStream for writing response
	 * @return xml response, as result of task execution
	 * @throws IOException - if some I/O error occurp.
	 * @throws TaskDfsException - if some error in task execution
	 */
	public String process(InputStream in, OutputStream out) throws IOException, TaskDfsException {
		String xmlResponse = null;
		try {
			// Set socket timeout
			try {
				socket.setSoTimeout(TimeOut);
			} catch (SocketException ex) {
				log.log(Level.SEVERE, "Can't set SoTimeout for socket");
				throw new RuntimeException(ex);
			}

			// Read request from stream
			String req = new String(serverlibrary.headerprovider.Reader.read(in));
			log.log(Level.INFO, "Readed request '''" + req + "'''");

			Task task = taskFactory.CreateTask(req);
			log.info(task.toString());
			xmlResponse = task.doTask(in, out);


		} catch (SocketException e) {
			log.log(Level.WARNING, "SocketException " + e.getMessage());
		} catch (SocketTimeoutException e) {
			log.log(Level.WARNING, "SocketTimeoutException " + e.getMessage());
		}

		return xmlResponse;
	}

	public void run() {

		log.info("Start connector");
		try {
			// Get I/O streams from socket
			in = socket.getInputStream();
			out = socket.getOutputStream();

			// Process connection
			String xmlResponse = process(in, out);

			if (xmlResponse != null) {
				// write response to socket
				log.info("Start writing response");
				serverlibrary.headerprovider.Writer.write(out, xmlResponse.getBytes());
				log.info(xmlResponse);
			}

		} catch (IOException e) {
			log.log(Level.WARNING, "Can't get input/output stream " +
					"or on reading request exception", e);
		} catch (Exception ex) {
			log.log(Level.SEVERE, "", ex);
		} finally {
			close();
		}
	}

	/**
	 * Correct closes opened resource, logging exceptions if some error
	 */
	private void close() {
		if (socketChannel != null) {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				log.log(Level.SEVERE, "Close in stream ", e);
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				log.log(Level.SEVERE, "Close out stream ", e);
			}
			try {
				socketChannel.close();
			} catch (IOException ex) {
				log.log(Level.SEVERE, "Can't close socket channel", ex);
			}

		}
		//Defender.stopConnection();
	}
}
