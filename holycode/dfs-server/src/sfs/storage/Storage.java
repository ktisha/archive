package sfs.storage;

/**
 *
 * @author an
 */
public class Storage {

	public enum State {

		OFFLINE, DEAD, ONLINE;
	}
	public final int id;
	public final String host;
	public final int port;
	private State state = State.OFFLINE;

	synchronized public State getState() {
		return state;
	}

	synchronized void setState(State state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Storage [id=" + id + "] [host='" + host + "'] [port=" + port + "]";
	}

	Storage(int id, String host, int port) {
		this.id = id;
		this.host = host;
		this.port = port;
	}

	Storage(Storage other) {
		this.id = other.id;
		this.host = other.host;
		this.port = other.port;
	}

	Storage(manager.dbmanager.DBManager.Storage other) {
		this.id = other.id;
		this.host = other.host;
		this.port = other.port;
	}



	int blockedSpace = 0;
	private int freeSpace = 0;

	synchronized int getFreeSpace() {
		return freeSpace;
	}

	synchronized void setFreeSpace(int freeSpace) {
		this.freeSpace = freeSpace;
	}
}
