package sfs;

import java.util.List;

/**
 *
 * @author an
 */
public class SfsFileDescr {

	public enum State {

		UPLOADING, // file is uploading
		UPLOADED, // file is uploaded, but Storage manager doesn't chech it
		READY, // ready for action
		INCOMPLETE, // one or more fileparts aren't avaliable now
		DEAD, // one or more filepart is lost
		REMOVE; // file for remove
	}
	public String user;
	public String name;
	public String filelist;
	public int filelistId;

	public int id;
	public State state = State.UPLOADING;
	public int copy;
	public int size;

	public List<SfsFilePartDescr> fileparts;
}
