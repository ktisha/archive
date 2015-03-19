package parser;

import assembler.IAssembler;
import exceptions.REException;
import sequence.CharacterSequence;
import sequence.EmptySequence;
import sequence.Sequence;

import java.util.Enumeration;
import java.util.Vector;

/**
 * User: ekaterina_tuzova
 *
 * Class provides parser interface, implements common behavior
 * of all parsers
 */
public abstract class Parser {
	// Special assembler to work on sequence after successfully matching
	protected IAssembler myAssembler;

	public Parser() {}

	/**
	 * matches parser on input, then applies assembler 
	 * @param in vector of Sequences to match
	 * @return vector of Sequences after matching
	 * @throws exceptions.REException if there are
	 */
	public Vector<Sequence> matchAndAssemble(Vector<Sequence> in) throws REException {
		Vector<Sequence> out = match(in);
		if (myAssembler != null) {
			Enumeration enumeration = out.elements();		
			while (enumeration.hasMoreElements()) {
				myAssembler.workOn((Sequence) enumeration.nextElement());
			}
		}

		return out;
	}

	/**
	 * matches this parser against all of Sequences in input
	 * @param in vector of Sequences to match
	 * @return vector of Sequences after matching 
	 */
	public abstract Vector<Sequence> match(Vector<Sequence> in) throws REException;

	/**
	 *
	 * @param v the vector to clone
	 * @return a copy of the input vector
	 * @throws CloneNotSupportedException if clone operation not supported in
	 * 				elements of input
	 */
	public static Vector elementClone(Vector v) throws CloneNotSupportedException {
		Vector copy = new Vector();
		Enumeration enumeration = v.elements();
		while (enumeration.hasMoreElements()) {
			Sequence sequence = (Sequence) enumeration.nextElement();
			copy.addElement(sequence.clone());
		}
		return copy;
	}

	/**
	 * Adds elements of one vector to another.
	 * @param base is vector to add to
	 * @param extension provides elements to add to base
	 */
	public static void add(Vector base, Vector extension) {
		Enumeration enumeration = extension.elements();
		while (enumeration.hasMoreElements()) {
			base.addElement(enumeration.nextElement());
		}
	}

	/**
	 * @param assembler Assembler to set
	 * @return this
	 */
	public Parser setAssembler(IAssembler assembler) {
		myAssembler = assembler;
		return this;
	}

	/**
	 * @param string string to match
	 * @return matched version of input of EmptySequence if we cannot match
	 * @throws exceptions.REException if there are internal error
	 */
	public Sequence completeMatch(String string) throws REException {
		Sequence sequence = new CharacterSequence(string);
		Vector<Sequence> in = new Vector<Sequence>();
		in.addElement(sequence);
		Vector<Sequence> out = matchAndAssemble(in);
		Sequence best = best(out);
		
		if (best != null && !best.hasMoreElements()) {
			return best;
		}
		return new EmptySequence();
	}

	public boolean matchRE(String string) throws REException {
		return completeMatch(string) instanceof CharacterSequence;
	}

	/**
	 * @param vector set to look in
	 * @return the most-matched sequence in a vector.
	 */
	public Sequence best(Vector<Sequence> vector) {
		Sequence best = new EmptySequence();

		Enumeration enumeration = vector.elements();
		while (enumeration.hasMoreElements()) {
			
			Sequence sequence = (Sequence) enumeration.nextElement();
			if (!sequence.hasMoreElements()) {
				return sequence;
			}
			if (best instanceof EmptySequence) {
				best = sequence;
			} else
				if (   sequence.elementsConsumed() >
					best.elementsConsumed()) {
					best = sequence;
				}
		}
		return best;
	}
}
