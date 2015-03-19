package serverlibrary.exception;

/**
 *
 * @author an
 */
public class DfsException extends Exception {
	public DfsException(Exception e) {
		super(e);
	}
	public DfsException(String string) {
		super(string);
	}
}
