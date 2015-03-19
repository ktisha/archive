package assembler;

import parser.terminals.SpecificChar;
import sequence.Sequence;

/**
 * User: ekaterina_tuzova
 * That assembler pop a Character from the stack and push back
 * SpecificChar parser.
 */
public class CharacterAssembler implements IAssembler {

	public void workOn(Sequence sequence) {
		sequence.push(new SpecificChar((Character) sequence.pop()));
	}
}
