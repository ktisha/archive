package assembler;

import sequence.Sequence;

/**
 * User: ekaterina_tuzova
 * Interface for assemblers.
 * Provides method workOn(Sequence).
 */
public interface IAssembler {

	/**
	 * What to do when successfully matched
	 * @param sequence  The sequence to work on
	 */
	public abstract void workOn(Sequence sequence);

}
