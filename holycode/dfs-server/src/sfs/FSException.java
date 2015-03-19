package sfs;

import dfsexceptions.DfsException;

/**
 *
 * @author an
 */
public class FSException extends DfsException {

	public FSException(Exception e) {
		super(e);
	}

	public FSException(String string) {
		super(string);
	}
}
