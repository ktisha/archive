package tests;

import exceptions.REException;
import org.junit.Test;
import parser.Parser;
import parser.RECompiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * User: ekaterina_tuzova
 * Test class for Dot symbol 
 */
public class TestDotPattern {
	@Test
	public void testMatchCase() {
		try {
			Parser parser = RECompiler.compile(".");
			assertTrue(parser.matchRE("a"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testNotMatchCase() {
		try {
			Parser parser = RECompiler.compile(".");
			assertFalse(parser.matchRE("as"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testDotStarMatchCase() {
		try {
			Parser parser = RECompiler.compile("a.*e");
			assertTrue(parser.matchRE("abababaaqeoriuyqwee"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testDotStarNotMatchCase() {
		try {
			Parser parser = RECompiler.compile("a.*e");
			assertFalse(parser.matchRE("abbsd"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
}
