package tests;

import exceptions.REException;
import org.junit.Test;
import parser.Parser;
import parser.RECompiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 * User: ekaterina_tuzova
 * Test class for slash symbol
 */
public class TestSlash {
	@Test
	public void testWordMatchCase() {

		Parser parser = null;
		try {
			parser = RECompiler.compile("\\w");
			assertTrue(parser.matchRE("aaaab"));
			parser = RECompiler.compile("\\wc");
			assertTrue(parser.matchRE("aaaabc"));
			parser = RECompiler.compile("a\\wc");
			assertTrue(parser.matchRE("aaaabc"));
			parser = RECompiler.compile("\\W");
			assertTrue(parser.matchRE("1234"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testWordNotMatchCase() {

		Parser parser = null;
		try {
			parser = RECompiler.compile("\\w");
			assertFalse(parser.matchRE("\\w"));
			assertFalse(parser.matchRE("aaaabc34"));
			assertFalse(parser.matchRE("aaaabc[]"));
			parser = RECompiler.compile("\\W");
			assertFalse(parser.matchRE("qwer"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testDigitMatchCase() {
		Parser parser = null;
		try {
			parser = RECompiler.compile("\\d");
			assertTrue(parser.matchRE("2"));
			parser = RECompiler.compile("\\d*a");
			assertTrue(parser.matchRE("34a"));
			parser = RECompiler.compile("q\\da");
			assertTrue(parser.matchRE("q2a"));
			parser = RECompiler.compile("\\D");
			assertTrue(parser.matchRE("q"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDigitNotMatchCase() {

		Parser parser = null;
		try {
			parser = RECompiler.compile("\\d");
			assertFalse(parser.matchRE("\\d"));
			assertFalse(parser.matchRE("aaaabc34"));
			assertFalse(parser.matchRE("234[]"));
			parser = RECompiler.compile("\\D*");
			assertFalse(parser.matchRE("15"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testTabMatchCase() {
		Parser parser = null;
		try {
			parser = RECompiler.compile("aa\\tb");
			assertTrue(parser.matchRE("aa\tb"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testTabNotMatchCase() {
		Parser parser = null;
		try {
			parser = RECompiler.compile("\\t");
			assertFalse(parser.matchRE("aa2aab"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testSpaceMatchCase() {
		Parser parser = null;
		try {
			parser = RECompiler.compile("aa\\sb");
			assertTrue(parser.matchRE("aa b"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSpaceNotMatchCase() {
		Parser parser = null;
		try {
			parser = RECompiler.compile("\\sad");
			assertFalse(parser.matchRE("aa2aab"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
}
