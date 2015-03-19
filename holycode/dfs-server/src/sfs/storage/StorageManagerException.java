package sfs.storage;

import dfsexceptions.DfsException;

/**
 *
 * @author an
 */

public class StorageManagerException extends DfsException {
	public StorageManagerException(Exception e) {
		super(e);
	}
	public StorageManagerException(String string) {
		super(string);
	}
}