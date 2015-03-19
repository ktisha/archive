package task;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xmlbeans.XmlException;
import org.dfs.server.RequestDocument;
import org.dfs.server.Client;

import serverlibrary.exception.TaskDfsException;
import serverlibrary.task.Task;


import serverlibrary.task.TaskFactory;

public class CentralServerTaskFactory extends TaskFactory {

	@Override
	public Task CreateTask(String xml) throws TaskDfsException {
		Task task = null;
		try {
			// parse request
			RequestDocument requestDocument = RequestDocument.Factory.parse(xml);
			if (!requestDocument.validate()) {
				throw new XmlException("Xml is not valid");
			}

			if (!requestDocument.getRequest().isSetClient()) {
				throw new XmlException("Not found tag 'client'");
			}
			Client clientRequest = requestDocument.getRequest().getClient();

			String action = clientRequest.getAction();

			String session = clientRequest.getSessionkey();

			// login action
			if ("login".equals(action)) {
				if (!clientRequest.isSetLogin()) {
					throw new XmlException("Request error: not found tag 'login'");
				}
				task = new LoginTask(clientRequest.getLogin().getUser(),
						clientRequest.getLogin().getPass());


			} else if ("list-action".equals(action)) {
				if (!clientRequest.isSetListAction()) {
					throw new XmlException("Request error: not found tag 'list-action'");
				}

				if ("get".equals(clientRequest.getListAction().getAction())) {
					if (!clientRequest.getListAction().isSetId()) {
						throw new XmlException("Request error: not found tag 'id'");
					}
					task = new SearchTask(session, "%", clientRequest.getListAction().getId().intValue());
				} else {

					task = new FileListManageTask(session,
							clientRequest.getListAction().getAction(),
							clientRequest.getListAction());
				}

			} else if ("search".equals(action)) {
				if (!clientRequest.isSetSearch()) {
					throw new XmlException("Request error: not found tag 'search'");
				}
				int filelistId = 0;
				if (clientRequest.getSearch().isSetId()) {
					filelistId = clientRequest.getSearch().getId().intValue();
				}

				task = new SearchTask(session, clientRequest.getSearch().getQuery(), filelistId);


			} else if ("file-action".equals(action)) {
				if (!clientRequest.isSetFileAction()) {
					throw new XmlException("Request error: not found tag 'file-action'");
				}
				if ("upload".equals(clientRequest.getFileAction().getAction())) {
					// filesize filename filelist-id
					if (!clientRequest.getFileAction().isSetFilelistId()) {
						throw new XmlException("Request error: not found tag 'filelist-id'");
					} else if (!clientRequest.getFileAction().isSetFilename()) {
						throw new XmlException("Request error: not found tag 'filename'");
					} else if (!clientRequest.getFileAction().isSetFilesize()) {
						throw new XmlException("Request error: not found tag 'filesize'");
					} else if (!clientRequest.getFileAction().isSetCopy()) {
						throw new XmlException("Request error: not found tag 'copy'");
					}
					task = new UploadTask(session,
							clientRequest.getFileAction().getFilename(),
							clientRequest.getFileAction().getFilesize().intValue(),
							clientRequest.getFileAction().getCopy().intValue(),
							clientRequest.getFileAction().getFilelistId().intValue());


				} else if ("download".equals(clientRequest.getFileAction().getAction())) {
					if (!clientRequest.getFileAction().isSetId()) {
						throw new XmlException("Request error: not found tag 'id'");
					}
					task = new DownloadTask(session, clientRequest.getFileAction().getId().intValue(), 0);


				} else if ("remove".equals(clientRequest.getFileAction().getAction())) {
					if (!clientRequest.getFileAction().isSetId()) {
						throw new XmlException("Request error: not found tag 'id'");
					}
					task = new RemoveFileTask(session, clientRequest.getFileAction().getId().intValue());


				} else if ("move".equals(clientRequest.getFileAction().getAction())) {
					if (!clientRequest.getFileAction().isSetId()) {
						throw new XmlException("Request error: not found tag 'id'");
					}
					if (!clientRequest.getFileAction().isSetFilelistId()) {
						throw new XmlException("Request error: not found tag 'filelist-id'");
					}
					task = new MoveFileTask(session, clientRequest.getFileAction().getId().intValue(),
							clientRequest.getFileAction().getFilelistId().intValue());


				} else {
					throw new XmlException("Request error: unsupported action (" + clientRequest.getFileAction().getAction() + ")");
				}


			} else if ("get-all-list".equals(action)) {
				task = new GetListTask(session);


			} else {
				throw new XmlException("Bad request: unsupported action (" + action + ")");
			}


		} catch (XmlException ex) {
			task = new FailTask(ex.getMessage());
		}

		return task;
	}
}
