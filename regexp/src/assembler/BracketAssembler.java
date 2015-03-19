package assembler;

import parser.Alternation;
import parser.terminals.SpecificChar;
import sequence.Sequence;

/**
 * User: ekaterina_tuzova
 * That assembler pop top parsers from sequence's stack
 * If there are instance of Alternation then just push back
 * Else -- create alternation of all items on stack
 */
public class BracketAssembler implements IAssembler{
	public void workOn(Sequence sequence) {
		Object top = sequence.pop();
		if (top instanceof Alternation) {
			sequence.push(top);
		}
		else {
			Alternation alt = new Alternation();
			while (!sequence.stackIsEmpty()) {
				alt.add((SpecificChar)top);
				top = sequence.pop();
			}
			sequence.push(alt);
		}
	}
}
