package assembler;

import parser.Alternation;
import parser.EmptyParser;
import parser.Parser;
import sequence.Sequence;

/**
 * User: ekaterina_tuzova
 * That assembler pop parser from sequence's stack and push back new
 * Alternation of it and empty parser. 
 */
public class QuestionAssembler implements IAssembler {
	public void workOn(Sequence sequence) {
		Alternation alt = new Alternation();
		alt.add((Parser) sequence.pop());
		alt.add(new EmptyParser());
		sequence.push(alt);
	}
}
