package parser;

import exceptions.REException;
import sequence.Sequence;

import java.util.Enumeration;
import java.util.Vector;

/**
 * User: ekaterina_tuzova
 *
 * Alternation is a collection of parsers,
 * any one of which can successfully match sequence.
 */
public class Alternation extends ParserCollection {

	@Override
	public Vector<Sequence> match(Vector<Sequence> in) throws REException {
		Vector<Sequence> out = new Vector<Sequence>();
		Enumeration enumeration = mySubParsers.elements();
		while (enumeration.hasMoreElements()) {
			Parser parser = (Parser) enumeration.nextElement();
			add(out, parser.matchAndAssemble(in));
		}
		return out;
	}
}
