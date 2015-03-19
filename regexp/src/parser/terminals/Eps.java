package parser.terminals;

/**
 * User: ekaterina_tuzova
 * represents epsilon in grammar
 */
public class Eps extends Letter {
	@Override
	public boolean qualifies(Object o) {
		return false;
	}
}
