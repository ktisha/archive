package assembler;

import parser.Repetition;
import parser.terminals.SlashChar;
import sequence.Sequence;

/**
 * User: ekaterina_tuzova
 *
 * Pop parser from sequence's stack and push back new
 * Repetition of its Slash Character Parser.
 */
public class SlashAssembler implements IAssembler {
	public void workOn(Sequence sequence) {
		Character top = (Character)sequence.pop();
		if (top.charValue() == 'w' || top.charValue() == 'W') {
			sequence.push(new Repetition(new SlashChar(top)));
		}  else {
			sequence.push(new SlashChar(top));
		}
	}
}
