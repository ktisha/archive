package dfsagent;

import dfsagent.datastructures.FileDescription;
import dfsagent.datastructures.FileList;
import java.io.BufferedReader;
import java.io.File;
import serverlibrary.exception.TaskDfsException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import serverlibrary.task.Task;
import tasks.CheckFreeSpace;
import tasks.DownloadTask;
import tasks.TaskFactory;
import tasks.UploadTask;

//import dfsagent.datastructures.FileList;
/**
 *
 * @author student
 */
public class Main {

 //   FileDescription file = new FileDescription(1, 2, "path") ;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws TaskDfsException {
   /*     FileInputStream fileIn = null;
        try {
            CheckFreeSpace task = new CheckFreeSpace();
            fileIn = new FileInputStream("./lib/input");
            FileOutputStream fileOut = new FileOutputStream("./lib/output");
            task.doTask(fileIn, fileOut);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fileIn.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    * */
        TaskFactory f = new TaskFactory();
        File file = new File("./test/source/xml_upload");
        StringBuilder contents = new StringBuilder();
        try {
            //use buffering, reading one line at a time
            BufferedReader input = new BufferedReader(new FileReader(file));
            try {
                String line = null;
                while ((line = input.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
//        System.err.println(contents.toString());
        Task task = f.CreateTask(contents.toString());
    }

}
