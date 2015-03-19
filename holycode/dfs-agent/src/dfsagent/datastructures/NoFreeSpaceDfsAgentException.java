package dfsagent.datastructures;

/**
 *
 * @author an
 */
public class NoFreeSpaceDfsAgentException extends Exception {
	public NoFreeSpaceDfsAgentException(Exception e) {
		super(e);
	}
	public NoFreeSpaceDfsAgentException(String string) {
		super(string);
	}
}