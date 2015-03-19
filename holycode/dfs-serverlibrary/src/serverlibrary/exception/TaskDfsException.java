package serverlibrary.exception;

/**
 *
 * @author an
 */
public class TaskDfsException extends DfsException {
	public TaskDfsException(Exception e) {
		super(e);
	}
	public TaskDfsException(String string) {
		super(string);
	}
}
