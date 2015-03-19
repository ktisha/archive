package tests;

import exceptions.REException;
import org.junit.Test;
import parser.Parser;
import parser.RECompiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * User: ekaterina_tuzova
 * Test class for star symbol
 */
public class TestStarPattern {
	@Test
	public void testMatchCase() {

		Parser parser = null;
		try {
			parser = RECompiler.compile("a*");
			assertTrue(parser.matchRE("aaa"));
			assertTrue(parser.matchRE(""));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testNotMatchCase() {
		Parser parser = null;
		try {
			parser = RECompiler.compile("a*");
			assertFalse(parser.matchRE("aaas"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testDoubleStarMatchCase() {
		Parser parser = null;
		try {
			parser = RECompiler.compile("a*b*");
			assertTrue(parser.matchRE("aaabbb"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testDoubleStarNotMatchCase() {
		Parser parser = null;
		try {
			parser = RECompiler.compile("a*b*");
			assertFalse(parser.matchRE("aaas"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testAndStarMatchCase() {
		Parser parser = null;
		try {
			parser = RECompiler.compile("(ab)*");
			assertTrue(parser.matchRE("ababab"));
			parser = RECompiler.compile("(ab)*c");
			assertTrue(parser.matchRE("c"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testAndStarNotMatchCase() {
		Parser parser = null;
		try {
			parser = RECompiler.compile("(ab)*");
			assertFalse(parser.matchRE("abababx"));
			assertFalse(parser.matchRE("abaaab"));
			assertFalse(parser.matchRE("aba"));
			
		} catch (REException e) {
			e.printStackTrace();
		}
	}

}
