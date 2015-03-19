package parser;

import exceptions.REException;
import sequence.Sequence;

import java.util.Vector;

/**
 * User: ekaterina_tuzova
 * That parser matches repeatedly against sequence (one or more times)
 */
public class PlusRepetition extends Parser{
	protected Parser myParser;
	public PlusRepetition(Parser parser) {
		myParser = parser;
	}

	@Override
	public Vector<Sequence> match(Vector<Sequence> in) throws REException {
		Vector<Sequence> out = new Vector<Sequence>();
		Vector<Sequence> sequences = in; // a working state
		while (!sequences.isEmpty()) {
			sequences = myParser.matchAndAssemble(sequences);
			add(out, sequences);
		}
		return out;
	}
}
