package sfs;

import sfs.storage.Storage;

/**
 *
 * @author an
 */
public class RemoteFileDescr {

	public enum State {
		ONLINE, OFFLINE;
	}

	public int id = 0;
	public int size = 0;
	//public String storageHostname = "";
	//public int storagePort = 0;
	//public State storageState;

	public Storage storage;
}
