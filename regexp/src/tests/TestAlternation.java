package tests;

import exceptions.REException;
import org.junit.Test;
import parser.Parser;
import parser.RECompiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * User: ekaterina_tuzova
 *
 * Test class for Alternation
 */
public class TestAlternation {
	@Test
	public void testMatchCase() {
		try {
			Parser parser = RECompiler.compile("(a|b)");
			assertTrue(parser.matchRE("a"));
			assertTrue(parser.matchRE("b"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testNotMatchCase() {
		try {
			Parser parser = RECompiler.compile("(a|b)");
			assertFalse(parser.matchRE("c"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testAlternationStarMatchCase() {
		try {
			Parser parser = RECompiler.compile("(a|b)*");
			assertTrue(parser.matchRE("abbaaa"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testAlternationStarNotMatchCase() {
		try {
			Parser parser = RECompiler.compile("(a|b)*");
			assertFalse(parser.matchRE("abbaaaz"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
}
