package sequence;

import java.util.Enumeration;
import java.util.Stack;

/**
 * User: ekaterina_tuzova
 *
 * Provides interface for sequence of smth
 * keeps RE elements in Stack
 */
public abstract class Sequence implements Cloneable, Enumeration {
	// Stack to keep progress
	protected Stack myStack = new Stack();

	// current index
	protected int myIndex = 0;

	@Override
	public Object clone() throws CloneNotSupportedException {
		Sequence sequence = (Sequence) super.clone();
		sequence.myStack = (Stack) myStack.clone();
		return sequence;
	}

	/**
	 * @return amount of already matched elements
	 */
	public int elementsConsumed() {
		return myIndex;
	}

	/**
	 * @return true if we have more elements on Stack, false otherwise
	 */
	public boolean hasMoreElements() {
		return elementsConsumed() < getLength();
	}

	/**
	 * @return length of sequence
	 */
	public abstract int getLength();

	/**
	 * @return copy of top element on stack
	 */
	public abstract Object peek();

	/**
	 * @return top element of stack
	 */
	public Object pop() {
		return myStack.pop();
	}

	/**
	 * @param elem element to put to stack
	 */
	public void push(Object elem) {
		myStack.push(elem);
	}

	/**
	 * @return true if stack is empty, false otherwise
	 */
	public boolean stackIsEmpty() {
		return myStack.isEmpty();
	}

}
