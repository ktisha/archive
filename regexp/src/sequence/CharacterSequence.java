package sequence;

/**
 * User: ekaterina_tuzova
 * 
 * Class for sequence of terminals
 */
public class CharacterSequence extends Sequence {
	protected String myString;
	
	public CharacterSequence(String string) {
		myString = string;
	}
	@Override
	public int getLength() {
		return myString.length();
	}

	@Override
	public Object peek() {
		if (myIndex < getLength()) {
			return new Character(myString.charAt(myIndex));
		} else {
			return null;
		}
	}

	/**
	 * @return next character from string 
	 */
	public Object nextElement() {
		return new Character(myString.charAt(myIndex++));
	}
	@Override
	public String toString() {
		return myString;
	}

}
