package sfs.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.xmlbeans.XmlException;

import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;

import java.util.logging.Logger;
import org.dfs.server.RequestDocument;
import org.dfs.server.RequestDocument.Request;
import org.dfs.server.response.ResponseDocument;
import org.dfs.server.response.ResponseDocument.Response;

/**
 * storage.Guard provides a correct information about storages.
 * Works in separate thread as daemon.
 * Checks free space on storages every SPACE_CHECK_SLEEP_TIME ms
 * Checks list of files on storage every FILES_CHECK_TIMES * SPACE_CHECK_SLEEP_TIME ms
 * @author an
 */
class Guard implements Runnable {

	private static final Logger log = Logger.getLogger("storage");
	private static int FILES_CHECK_TIMES = 10;
	private static final int SOCKET_TIMEOUT = 5000;
	private final ConcurrentMap<Integer, Storage> storages;
	private final byte[] freeSpaceRequestBytes;
	private final byte[] allFilesRequestBytes;
	private final int spaceChechSleepTime;


	Guard(ConcurrentMap<Integer, Storage> storages, int spaceChechSleepTime) {
		this.storages = storages;
		this.spaceChechSleepTime = spaceChechSleepTime;

		// create response to agent
		RequestDocument requestDocument = RequestDocument.Factory.newInstance();
		Request request = requestDocument.addNewRequest();
		org.dfs.server.Agent agentRequest = request.addNewAgent();
		// create free-space response to agent
		agentRequest.setAction("free-space");
		freeSpaceRequestBytes = requestDocument.toString().getBytes();
		// create all-files response to agent
		agentRequest.setAction("all-files");
		allFilesRequestBytes = requestDocument.toString().getBytes();

	}

	/**
	 * Starts this gurad in separate thread as daemon.
	 */
	void start() {
		log.info("Starting storage guard");
		Thread thread = new Thread(this, "Storage Guard");
		thread.setDaemon(true);
		thread.start();
	}

	/**
	 * Run loop. Checks free space and list of files.
	 */
	@Override
	public void run() {
		int count = FILES_CHECK_TIMES;
		try {
			while (true) {
				// check free space on storage
				checkSpace();

				if (count == FILES_CHECK_TIMES) {
					count = 0;
					// check all files on storage
					checkFiles();
				}
				count++;

				Thread.sleep(spaceChechSleepTime);
			}
		} catch (InterruptedException ex) {
			log.log(Level.WARNING, "Stoped storage.Guard", ex);
		} catch (Exception ex) {
			log.log(Level.SEVERE, "Unknow exception on storage.Guard", ex);
		}

	}

	/**
	 * Checks free space on storages.
	 */
	private void checkSpace() {
		log.info("Start check space on storages");
		// iterate by storage
		for (Storage storage : storages.values()) {
			// connect to agent, get response
			Response response = getResponse(storage, freeSpaceRequestBytes);

			if (response != null) {
				try {
					if (!response.isSetAgent()) {
						throw new XmlException("Response from agent does not contain 'agent' tag");
					}
					if (!response.getAgent().isSetFreeSpace()) {
						throw new XmlException("Response from agent does not contain 'free-space' tag");
					}

					// set data to storage
					storage.setFreeSpace(response.getAgent().getFreeSpace().intValue());
				} catch (XmlException ex) {
					log.log(Level.WARNING, null, ex);
				}

				// set data to storage
//				storage.freeSpace = response.getAgent().getFreeSpace().intValue();
			}
		}
		log.info("End check space on storages");
	}

	/**
	 * Checks the list of files on storages.
	 */
	private void checkFiles() {
		// iterate by storage
		for (Storage storage : storages.values()) {
			// connect to agent, get response
			Response response = getResponse(storage, allFilesRequestBytes);

			if (response != null) {
				try {
					if (!response.isSetAgent()) {
						throw new XmlException("Response from agent does not contain 'agent' tag");
					}
					if (!"ok".equals(response.getAgent().getStatus())) {
						throw new XmlException("Error response: " + response.getAgent().getStatus());
					}
//					if (!response.getAgent().isSetList()) {
//						throw new XmlException("Response from agent does not contain 'list' tag");
//					}

					// set data to storage
					// TODO compare inf about files on storage

//
//					System.err.println("is set" + response.getAgent().isSetAFile());
//					System.err.println("is set" + response.getAgent().getAFile());
//
//					response.getAgent().unsetAFile();
//					System.err.println("is set" + response.getAgent().isSetAFile());
//					System.err.println("is set" + response.getAgent().getAFile());
//
//					//response.getAgent().


				} catch (XmlException ex) {
					log.log(Level.WARNING, null, ex);
				}

				// set data to storage
//				storage.freeSpace = response.getAgent().getFreeSpace().intValue();
			} else {
				// storage is unavailable now
				

			}
		}
	}


	/**
	 * Makes request to agent on storage. Reads request and parses it. Supports storage.state.
	 * Returns Response or null if some error.
	 * @param storage where is agent
	 * @return Response or null
	 */
	private Response getResponse(Storage storage, byte[] requestBytes) {
		Response response = null;

		Socket socket = null;
		InputStream in = null;
		OutputStream out = null;

		try {
			// Open socket, get in/out stream
			//socket.setSoTimeout(SOCKET_TIMEOUT);
			socket = new Socket();//(storage.host, storage.port);
			socket.connect(new InetSocketAddress(storage.host, storage.port), SOCKET_TIMEOUT);
			in = socket.getInputStream();
			out = socket.getOutputStream();

			// writing xml request
			serverlibrary.headerprovider.Writer.write(out, requestBytes);

			// read and parse xml response
			byte[] bytes = serverlibrary.headerprovider.Reader.read(in);
			ResponseDocument responseDocument = ResponseDocument.Factory.parse(new String(bytes));
			response = responseDocument.getResponse();

			storage.setState(Storage.State.ONLINE);

		} catch (XmlException ex) {
			log.log(Level.WARNING, storage.toString() + " Got not valid xml response", ex);
			storage.setState(Storage.State.OFFLINE);
		} catch (UnknownHostException ex) {
			// TODO wath shall i do with dead storage, rm from map... shell i save it
			log.log(Level.WARNING, storage.toString() + " Host is UnknownHost", ex);
			storage.setState(Storage.State.OFFLINE);
			//storages.remove(new Integer(storage.id));
		} catch (IOException ex) {
			// Not connect to storage
			log.log(Level.INFO, storage.toString() + " is unavailable now");
			storage.setState(Storage.State.OFFLINE);
		} finally {
			// close streams
			try {
				try {
					if (in != null) {
						in.close();
					}
				} finally {
					if (out != null) {
						out.close();
					}
				}
			} catch (IOException ex) {
				log.log(Level.INFO, storage.toString() + " Exception on closing stream", ex);
			}

			// close socket
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException ex) {
				log.log(Level.INFO, storage.toString() + " Exception on cloasing socket", ex);
			}
		}
		return response;
	}

}
