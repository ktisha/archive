package tests;

import exceptions.REException;
import org.junit.Test;
import parser.Parser;
import parser.RECompiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * User: ekaterina_tuzova
 * Test class for question symbol 
 */
public class TestQuestion {
	@Test
	public void testMatchCase() {
		try {
			Parser parser = RECompiler.compile("ab?c");
			assertTrue(parser.matchRE("abc"));
			assertTrue(parser.matchRE("ac"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testNotMatchCase() {
		try {
			Parser parser = RECompiler.compile("ab?c");
			assertFalse(parser.matchRE("acc"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
}
