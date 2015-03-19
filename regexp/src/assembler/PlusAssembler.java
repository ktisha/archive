package assembler;

import parser.Parser;
import parser.PlusRepetition;
import sequence.Sequence;

/**
 * User: ekaterina_tuzova
 * Pop parser from sequence's stack and push back new
 * PlusRepetition of it (one or more times).
 */
public class PlusAssembler implements IAssembler {
	public void workOn(Sequence sequence) {
		sequence.push(new PlusRepetition((Parser) sequence.pop()));
	}
}
