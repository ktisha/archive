package parser.terminals;

import exceptions.REException;
import parser.Parser;
import sequence.EmptySequence;
import sequence.Sequence;

import java.util.Enumeration;
import java.util.Vector;

/**
 * User: ekaterina_tuzova
 * Terminal is parser, but it doesn't pass matching to
 * its subparsers
 */
public class Terminal extends Parser{
	// if false Terminal should push itself to stack
	protected boolean myDiscard = false;
	public Terminal() {}

	public Terminal discard() {
		return setDiscard(true);
	}

	@Override
	public Vector<Sequence> match(Vector in) throws REException {
		Vector<Sequence> out = new Vector<Sequence>();
		Enumeration e = in.elements();
		while (e.hasMoreElements()) {
			Sequence a = (Sequence) e.nextElement();
			Sequence b = matchOne(a);
			if (!(b instanceof EmptySequence)) {
				out.addElement(b);
			}
		}
		return out;
	}

	/**
	 * @param in vector to match
	 * @return matched Sequence  of EmptySequence
	 * @throws exceptions.REException if can't clone Sequence
	 */
	protected Sequence matchOne(Sequence in) throws REException {
		if (!in.hasMoreElements()) {
			return new EmptySequence();
		}
		if (qualifies(in.peek())) {
			Sequence out;
			try {
				out = (Sequence) in.clone();
			} catch (CloneNotSupportedException e) {
				throw new REException("Internal parser error. " + e.getMessage());
			}
			Object next = out.nextElement();
			if (!myDiscard) {
				out.push(next);
			}
			return out;
		}
		return new EmptySequence();
	}

	/**
	 * @param o Object to compate to
	 * @return  true, if the object is the kind of terminal this
     *          parser seeks
	 */
	protected boolean qualifies(Object o) {
		return true;
	}
	public Terminal setDiscard(boolean myDiscard) {
		this.myDiscard = myDiscard;
		return this;
	}
}
