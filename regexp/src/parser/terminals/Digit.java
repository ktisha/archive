package parser.terminals;

/**
 * User: ekaterina_tuzova
 * Represent Digits
 */
public class Digit extends Terminal {
	@Override
	public boolean qualifies(Object o) {
		Character c = (Character) o;
		return Character.isDigit(c.charValue());
	}
}
