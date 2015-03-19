package parser.terminals;

/**
 * User: ekaterina_tuzova
 *
 * represent Specific character in RE, such as "(", ")", "*", "."
 */
public class SpecificChar extends Terminal {
	protected Character myCharacter;
	public SpecificChar(char c) {
		this(new Character(c));
	}
	 public SpecificChar(Character character) {
		myCharacter = character;
	}
	@Override
	public boolean qualifies(Object other) {
		if (other != null) {
			Character character = (Character) other;
			return myCharacter.equals('.') || character.charValue() == myCharacter.charValue();
		}
		return false;
	}

	public String toString() {
		return myCharacter.toString();
	}
	public Character getChar() {
		return myCharacter;
	}
}
