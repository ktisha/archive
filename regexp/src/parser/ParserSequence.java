package parser;

import exceptions.REException;
import sequence.Sequence;

import java.util.Enumeration;
import java.util.Vector;

/**
 * User: ekaterina_tuzova
 *
 * ParserSequence is a collection of parsers,
 * all of which must one by one match against sequence
 */
public class ParserSequence extends ParserCollection {
	public ParserSequence() {}

	@Override
	public Vector<Sequence> match(Vector<Sequence> in) throws REException {
		Vector<Sequence> out = in;
		Enumeration enumeration = mySubParsers.elements();

		while (enumeration.hasMoreElements()) {
			Parser parser = (Parser) enumeration.nextElement();
			out = parser.matchAndAssemble(out);    				
			if (out.isEmpty()) {
				return out;
			}
		}
		return out;
	}

}
