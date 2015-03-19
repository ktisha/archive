package parser;

import exceptions.REException;
import sequence.Sequence;

import java.util.Vector;

/**
 * User: ekaterina_tuzova
 *
 * Class represent repetition of tokens.
 * That parser matches repeatedly against sequence
 */
public class Repetition  extends Parser{
	protected Parser myParser;
	public Repetition (Parser parser) {
		myParser = parser;
	}

	@Override
	public Vector<Sequence> match(Vector<Sequence> in) throws REException {
		Vector<Sequence> out;
		try {
			out = elementClone(in);
		} catch (CloneNotSupportedException e) {
			throw new REException("Internal parser error. " + e.getMessage());
		}
		Vector<Sequence> sequences = in; // a working state
		while (!sequences.isEmpty()) {
			sequences = myParser.matchAndAssemble(sequences);
			add(out, sequences);
		}
		return out;
	}
}
