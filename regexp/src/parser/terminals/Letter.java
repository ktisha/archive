package parser.terminals;

/**
 * User: ekaterina_tuzova
 * Represent Letters
 */
public class Letter extends Terminal {
	@Override
	public boolean qualifies(Object o) {
		Character c = (Character) o;
		return (Character.isLetter(c.charValue()));
	}
}
