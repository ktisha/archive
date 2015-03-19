package parser.terminals;

/**
 * User: ekaterina_tuzova
 * Represents any character (symbol ".")
 */
public class AnyCharacter extends Terminal {
	 public AnyCharacter() {}
	@Override
	public boolean qualifies(Object other) {
		if (other != null) {
			Character character = (Character) other;
			return character.equals('.');
		}
		return false;
	}
}
