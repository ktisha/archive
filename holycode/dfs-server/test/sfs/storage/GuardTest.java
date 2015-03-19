package sfs.storage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author an
 */
public class GuardTest {

    public GuardTest() {
    }


	/**
	 * Test of start method, of class Guard.
	 */
	@Test
	public void testStartOffline() throws InterruptedException {
		System.out.println("start. offline");

		ConcurrentMap<Integer, Storage> map = new ConcurrentHashMap<Integer, Storage>();
		Storage st = new Storage(1, "localhost", 9000);
		map.put(new Integer(st.id), st);
		
		Guard instance = new Guard(map, 60000);
		instance.start();

		Thread.yield();
		Thread.sleep(1000);
		assertEquals(Storage.State.OFFLINE, map.get(new Integer(1)).getState());

	}

	/**
	 * Test of start method, of class Guard.
	 */
	@Test
	public void testStartOnline() throws InterruptedException {
		System.out.println("start. online");

		ConcurrentMap<Integer, Storage> map = new ConcurrentHashMap<Integer, Storage>();
		Storage st = new Storage(1, "localhost", 9000);
		map.put(new Integer(st.id), st);
		
		// start pseudoagent
		pseudoremot.Agent psAgent = new pseudoremot.Agent(st.port);
		Thread agentThread = new Thread(psAgent);
		agentThread.start();
		Thread.yield();

		Guard instance = new Guard(map, 6000000);

		instance.start();
		Thread.yield();
		Thread.sleep(1000);

//		assertEquals(Storage.State.ONLINE, map.get(new Integer(1)).getState());
		assertEquals(123, map.get(new Integer(1)).getFreeSpace());
		
		agentThread.interrupt();
	}

}