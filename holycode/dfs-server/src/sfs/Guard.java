package sfs;

/**
 *
 * @author an
 */
public class Guard implements Runnable {
	private static Guard instance = new Guard();

	public static Guard GetInstance() {
		return instance;
	}

	private Guard() {
		new Thread(this).start();
	}

	public void run() {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

}
