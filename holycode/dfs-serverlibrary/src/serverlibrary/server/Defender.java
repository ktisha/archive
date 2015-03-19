
package serverlibrary.server;

import java.util.concurrent.atomic.AtomicInteger;

public class Defender {
	
	static private final int MAX_CONNECTION = 10;
	static private AtomicInteger connectionCounter = new AtomicInteger(0);


	// Add working connection to counter
	// check is overload of allowed connection number
	static public boolean canStartConnection() {
		if(connectionCounter.incrementAndGet() > MAX_CONNECTION) {
			connectionCounter.decrementAndGet();
			return false;
		}
		return true;
	}

	// Remove working connection from counter
	static public void stopConnection() {
		connectionCounter.decrementAndGet();
	}


	// Check, is it good ip
	static public boolean checkIP(String ip) {
		return true;
	}

	// Check, is it good user.
	static public boolean checkUser(String user) {
		return true;
	}


	// Save fact of failed login trying
	static public void badLogin(String ip) {
	}


	private Defender() {}

}
