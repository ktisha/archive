package task;

import org.junit.Test;
import serverlibrary.task.Task;

/**
 *
 * @author an
 */
public class FailTaskTest {

    public FailTaskTest() {
    }


	/**
	 * Test of doTask method, of class FailTask.
	 */
	@Test
	public void testDoTask() throws Exception {
		System.out.println("doTask");
		Task instance = new FailTask("tp");
		System.err.println(instance.toString());
	}

}