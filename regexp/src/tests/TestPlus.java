package tests;

import exceptions.REException;
import org.junit.Test;
import parser.Parser;
import parser.RECompiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 * User: ekaterina_tuzova
 * Test class for plus symbol 
 */
public class TestPlus {
	@Test
	public void testMatchCase() {

		Parser parser = null;
		try {
			parser = RECompiler.compile("a+b");
			assertTrue(parser.matchRE("aaaab"));
			assertTrue(parser.matchRE("ab"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testNotMatchCase() {

		Parser parser;
		try {
			parser = RECompiler.compile("a+b");
			assertFalse(parser.matchRE("b"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
}
