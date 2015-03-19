package sequence;

/**
 * User: ekaterina_tuzova
 *
 * Class for empty Sequence
 * We return empty Sequence from match if string doesn't match pattern
 */
public class EmptySequence extends Sequence {
	@Override
	public int getLength() {
		return 0;
	}

	@Override
	public Object peek() {
		return null;
	}

	public Object nextElement() {
		return null;
	}

	public String toString() {
		return "Don't match";
	}
}
