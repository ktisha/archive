package parser;

import assembler.*;
import exceptions.REException;
import parser.terminals.*;
import sequence.EmptySequence;
import sequence.Sequence;

/**
 * User: ekaterina_tuzova
 *
 * Parser for regular expressions
 *
 *     expression    	= term orTerm*;
 *     term          	= factor nextFactor*;
 *     orTerm        	= '|' term;
 *     factor        	= phrase | phraseStar | phraseQuestion | phrasePlus;
 *     nextFactor   	= factor;
 *     phrase       	= letterOrDigit | '(' expression ')' | slashExpr | '[' letters ']';
 *     phraseStar   	= phrase '*';
 *     phraseQuestion	= phrase '?';
 *     phrasePlus		= phrase '+'; 
 *     letterOrDigit 	= Letter | Digit | AnyChar;
 * 	   slashExpr		= '\' specialChar;
 * 	   specialChar		= w | d | t | W | D | s | S;
 *     letters			= letterOrDigit '-' letterOrDigit | letterRepetition;
 *	   letterRepetition = letterOrDigit nextLetter*;
 * 	   nextLetter		= letterOrDigit | Eps;
 */
public class RECompiler {
	protected ParserSequence myExpression;

	/**
	 * @return a parser that will recognize a regular expression.
	 */
	public Parser expression() {
		if (myExpression == null) {
			myExpression = new ParserSequence();
			myExpression.add(term());
			myExpression.add(new Repetition(orTerm()));
		}
		return myExpression;
	}

	/**
	 * @return parser for the specific rule
	 * term = factor nextFactor*;
	 */
	protected Parser term() {
		ParserSequence term = new ParserSequence();
		term.add(factor());
		term.add(new Repetition(nextFactor()));
		return term;
	}
	/**
	 * @return parser for the specific rule
	 * orTerm = '|' term;
	 */
	private Parser orTerm() {
		ParserSequence orTerm = new ParserSequence();
		orTerm.add(new SpecificChar('|').discard());
		orTerm.add(term());
		orTerm.setAssembler(new OrAssembler());
		return orTerm;
	}
	/**
	 * @return parser for the specific rule
	 * factor = phrase | phraseStar;
	 */
	protected Parser factor() {
		Alternation factor = new Alternation();
		factor.add(phrase());
		factor.add(phraseStar());
		factor.add(phraseQuestion());
		factor.add(phrasePlus());
		return factor;
	}
   	/**
	 * @return parser for the specific rule
	 * phraseStar = phrase '+';
	 */
	private Parser phrasePlus() {
		ParserSequence phrasePlus = new ParserSequence();
		phrasePlus.add(phrase());
		phrasePlus.add(new SpecificChar('+').discard());
		phrasePlus.setAssembler(new PlusAssembler());
		return  phrasePlus;
	}
	/**
	 * @return parser for the specific rule
	 * phraseStar = phrase '?';
	 */
	private Parser phraseQuestion() {
		ParserSequence phraseQuestion = new ParserSequence();
		phraseQuestion.add(phrase());
		phraseQuestion.add(new SpecificChar('?').discard());
		phraseQuestion.setAssembler(new QuestionAssembler());
		return  phraseQuestion;
	}

	/**
	 * @return parser for the specific rule
	 * nextFactor = factor;
	 */
	private Parser nextFactor() {
		Parser nextFactor = factor();
		nextFactor.setAssembler(new AndAssembler());
		return nextFactor;
	}
	/**
	 * @return parser for the specific rule
	 * phraseStar = phrase '*';
	 */
	private Parser phraseStar() {
		ParserSequence phraseStar = new ParserSequence();
		phraseStar.add(phrase());
		phraseStar.add(new SpecificChar('*').discard());
		phraseStar.setAssembler(new StarAssembler());
		return phraseStar;
	}
	/**
	 * @return parser for the specific rule
	 * parser accepts phrase = letterOrDigit | '(' expression ')';
	 */
	protected Parser phrase() {
		Alternation phrase = new Alternation();
		phrase.add(letterOrDigit());

		ParserSequence s = new ParserSequence();
		s.add(new SpecificChar('(').discard());
		s.add(expression());
		s.add(new SpecificChar(')').discard());
		phrase.add(s);

		phrase.add(slashExpr());

		ParserSequence letters = new ParserSequence();
		letters.add(new SpecificChar('[').discard());
		letters.add(letters());
		letters.add(new SpecificChar(']').discard());
		phrase.add(letters);
		letters.setAssembler(new BracketAssembler());
		return phrase;
	}
	/**
	 * @return parser for the specific rule
	 * letterOrDigit '-' letterOrDigit | letterRepetition;
	 */
	private Parser letters() {
		Alternation letters = new Alternation();
		letters.add(letterRepetition());

		ParserSequence sequence = new ParserSequence();
		sequence.add(letterOrDigit());
		sequence.add(new SpecificChar('-').discard());
		sequence.add(letterOrDigit());
		sequence.setAssembler(new DashAssembler());
		letters.add(sequence);
		return letters;
	}
	/**
	 * @return parser for the specific rule
	 * letterRepetition = letterOrDigit nextLetter*;
	 */	
	private Parser letterRepetition() {
		ParserSequence alt = new ParserSequence();
		alt.add(letterOrDigit());
		alt.add(new Repetition(nextLetter()));
		return alt;
	}

	/**
	 * @return parser for the specific rule
	 * nextLetter = letterOrDigit | Eps;
	 */
	private Parser nextLetter() {
		Alternation alt = new Alternation();
		alt.add(letterOrDigit());
		alt.add(new Eps());
		return alt;
	}

	/**
	 * @return parser for the specific rule
	 * slashExpr		= '\' specialChar;
	 */
	private Parser slashExpr() {
		ParserSequence slashExpr = new ParserSequence();
		slashExpr.add(new SpecificChar('\\').discard());

		Alternation alt = new Alternation();
		alt.add(new SpecificChar('w'));
		alt.add(new SpecificChar('d'));
		alt.add(new SpecificChar('t'));
		alt.add(new SpecificChar('W'));
		alt.add(new SpecificChar('D'));
		alt.add(new SpecificChar('s'));
		alt.add(new SpecificChar('S'));
		slashExpr.add(alt);
		slashExpr.setAssembler(new SlashAssembler());
		return slashExpr;
	}

	/**
	 * @return parser for the specific rule
	 * parser accepts letters or digits
	 */
	private Parser letterOrDigit() {
		Alternation letterOrDigit = new Alternation();
		letterOrDigit.add(new Letter());
		letterOrDigit.add(new Digit());
		letterOrDigit.add(new AnyCharacter());
		letterOrDigit.setAssembler(new CharacterAssembler());
		return letterOrDigit;
	}

	/**
	 * @return new instance of RECompiler
	 */
	public static Parser instance() {
		return new RECompiler().expression();
	}

	/**
	 * @param pattern pattern to compile
	 * @return parser to match given pattern
	 * @throws REException if there are improperly formed pattern or
	 * 			internal parser error
	 */
	public static Parser compile(String pattern) throws REException {	
		Sequence sequence = instance().completeMatch(pattern);
		
		if (sequence instanceof EmptySequence) {
			throw new REException(
				"Improperly formed regular expression");
		}
		Parser parser;
		try {
			parser = (Parser) sequence.pop();
		} catch (Exception e) {
			throw new REException("Internal error in RECompiler. " + e.getMessage());
		}
		return parser;
	}

}
