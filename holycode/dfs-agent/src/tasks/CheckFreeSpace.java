/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tasks;

import dfsagent.datastructures.FileList;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import org.dfs.server.response.Agent;
import org.dfs.server.response.ResponseDocument;
import org.dfs.server.response.ResponseDocument.Response;

/**
 *
 * @author student
 */
public class CheckFreeSpace implements serverlibrary.task.Task {

    @Override
    public String doTask(InputStream in, OutputStream out) {
        // create xml response
        ResponseDocument responseDocument = ResponseDocument.Factory.newInstance();
        Response response = Response.Factory.newInstance();
        Agent agent = response.addNewAgent();
        agent.setStatus("ok");
        // get free space
        FileList list = FileList.GetInstance();
        agent.setFreeSpace(new BigInteger(""+list.getFreeSpace()));
        responseDocument.setResponse(response);

		//serverlibrary.headerprovider.Writer.write(out, responseDocument.toString().getBytes());

        return responseDocument.toString();
    }
    @Override
    public String toString() {
        return "Check free space";
    }

}
