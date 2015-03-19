package parser;

import java.util.Vector;

/**
 * User: ekaterina_tuzova
 *
 * Class represents collection of parsers.
 * Represent abstract behavior.
 */
public abstract class ParserCollection extends Parser{
	/**
	 * this parser is a collection of the parsers 
	 */
	protected Vector<Parser> mySubParsers = new Vector<Parser>();

	public ParserCollection() {}

	/**
	 * @param parser the parser to add as subparser
	 */
	public void add(Parser parser) {
		mySubParsers.add(parser);
	}
	/**
	 * @return set of contained parsers
	 */
	public Vector<Parser> getSubParsers() {
		return mySubParsers;
	}
	
}