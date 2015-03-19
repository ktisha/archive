package assembler;

import parser.Parser;
import parser.ParserSequence;
import sequence.Sequence;

/**
 * User: ekaterina_tuzova
 * That assembler pop two parsers from the sequence stack and push back a new 
 * Sequence of them.
 */
public class AndAssembler implements IAssembler {

	public void workOn(Sequence sequence) {
		Object top = sequence.pop();
		ParserSequence parserSequence = new ParserSequence();
		parserSequence.add((Parser) sequence.pop());
		parserSequence.add((Parser) top);
		sequence.push(parserSequence);
	}
}
