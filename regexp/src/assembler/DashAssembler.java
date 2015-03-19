package assembler;

import parser.Alternation;
import parser.terminals.SpecificChar;
import sequence.Sequence;

/**
 * User: ekaterina_tuzova
 * That assembler pop two parsers from sequence's stack and push back new
 * Alternation of all letters between them.
 */
public class DashAssembler implements IAssembler {
	public void workOn(Sequence sequence) {
		Object top = sequence.pop();
		Object second = sequence.pop();
		Alternation alt = new Alternation();

		for (int i = ((SpecificChar)second).getChar(); i <= ((SpecificChar)top).getChar(); ++i) {
			alt.add(new SpecificChar((char) i));
		}
		sequence.push(alt);
	}
}
