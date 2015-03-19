package parser;

import exceptions.REException;
import sequence.Sequence;

import java.util.Vector;

/**
 * User: ekaterina_tuzova
 *
 * represents empty parser. needed for question and plus symbols.
 */
public class EmptyParser extends Parser {
	@Override
	public Vector<Sequence> match(Vector<Sequence> in) throws REException {
		return in;
	}
}
