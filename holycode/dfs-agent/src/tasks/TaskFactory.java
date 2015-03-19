package tasks;

import dfsagent.datastructures.FileDescription;
import dfsagent.datastructures.FileList;
import org.apache.xmlbeans.XmlException;
import org.dfs.server.RequestDocument;
import serverlibrary.exception.TaskDfsException;
import serverlibrary.task.Task;
import org.dfs.server.Agent;
import org.dfs.server.Download;

/**
 *
 * @author student
 */
public class TaskFactory extends serverlibrary.task.TaskFactory {
    public TaskFactory() {};

    @Override
    public Task CreateTask(String xml) throws TaskDfsException {
        Task task = null;
        try {
            // parse request
            RequestDocument requestDocument = RequestDocument.Factory.parse(xml);
/*            if (requestDocument.getRequest().isSetAgent()) {
                System.err.println(requestDocument.getRequest().toString());
                throw new XmlException("Bad request: not found tag 'agent'");
            }
*/            Agent agentRequest = requestDocument.getRequest().getAgent();
            String action = agentRequest.getAction();

            if ("download".equals(action)) {
                // download action
                if(! agentRequest.isSetDownload())
                    throw new XmlException("Tag 'download' not found");

                int fileName = agentRequest.getDownload().getFileId().intValue();
                task = new DownloadTask(fileName);


            } else if ("upload".equals(action)) {
                // upload action
                if(! agentRequest.isSetUpload())
                    throw new XmlException("Tag 'upload' not found");
                int fileName = agentRequest.getUpload().getFileId().intValue();
                int fileSize = agentRequest.getUpload().getSize().intValue();
                task = new UploadTask(fileName, fileSize);


            } else if ("remove".equals(action)) {
                // delete action
                if(! agentRequest.isSetRemove())
                    throw new XmlException("Tag 'remove' not found");
                int fileName = agentRequest.getRemove().getFileId().intValue();
                task = new DeleteTask(fileName);


            } else if ("free-space".equals(action)) {
                // check free space action
                task = new CheckFreeSpace();


			} else if ("all-files".equals(action)) {
				task = new AllFilesTask();

				
            } else {
				task = new ErrorTask("Unsupported action '" + action + "'");
			}
        } catch (XmlException ex) {
            return new ErrorTask("Xml is not valid (" + ex.getMessage() + ")");
        }
        return task;
    }

}
