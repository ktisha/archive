package task;

import java.io.InputStream;
import java.io.OutputStream;
import manager.dbmanager.DBManager;
import org.junit.Test;

/**
 *
 * @author an
 */
public class LoginTaskTest {

    public LoginTaskTest() {
		DBManager.Init("dfs-test.db");
    }


	/**
	 * Test of doTask method, of class LoginTask.
	 */
	@Test
	public void testDoTask() throws Exception {
		System.out.println("doTask");
		InputStream in = null;
		OutputStream out = null;
		LoginTask instance = new LoginTask("student", "student");
		String result = instance.doTask(in, out);
		System.err.println(result);
	}

}