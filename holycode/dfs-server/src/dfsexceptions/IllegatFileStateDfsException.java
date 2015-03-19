package dfsexceptions;


public class IllegatFileStateDfsException extends DfsException {
	public IllegatFileStateDfsException(Exception e) {
		super(e);
	}
	public IllegatFileStateDfsException(String string) {
		super(string);
	}
}