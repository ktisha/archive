package assembler;

import parser.Parser;
import parser.Repetition;
import sequence.Sequence;

/**
 * User: ekaterina_tuzova
 * Pop parser from sequence's stack and push back new
 * Repetition of it.
 */
public class StarAssembler implements IAssembler {

	public void workOn(Sequence sequence) {
		sequence.push(new Repetition((Parser) sequence.pop()));
	}
}
