package assembler;

import parser.Alternation;
import parser.Parser;
import sequence.Sequence;

/**
 * User: ekaterina_tuzova
 * That assembler pop two parsers from sequence's stack and push back new
 * Alternation of them.
 */
public class OrAssembler implements IAssembler {

	public void workOn(Sequence sequence) {
		Object top = sequence.pop();
		Alternation alt = new Alternation();
		alt.add((Parser) sequence.pop());
		alt.add((Parser) top);
		sequence.push(alt);
	}
}
