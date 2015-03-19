package parser.terminals;

/**
 * User: ekaterina_tuzova
 *
 * represents slash followed by specific character
 */
public class SlashChar extends Terminal {
	protected Character myCharacter;
	public SlashChar(Character character) {
		myCharacter = character;
	}
	@Override
	public boolean qualifies(Object other) {
		if (other != null) {
			Character character = (Character) other;
			return myCharacter.equals('w') && Character.isLetter(character.charValue()) ||
					myCharacter.equals('d') && Character.isDigit(character.charValue()) ||
					myCharacter.equals('t') && character == '\t' ||
					myCharacter.equals('W') && !Character.isLetter(character.charValue()) ||
					myCharacter.equals('D') && !Character.isDigit(character.charValue()) ||
					myCharacter.equals('s') && Character.isSpaceChar(character.charValue()) ||
					myCharacter.equals('S') && !Character.isSpaceChar(character.charValue());
		}
		return false;
	}
}
