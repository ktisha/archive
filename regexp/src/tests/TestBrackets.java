package tests;

import exceptions.REException;
import org.junit.Test;
import parser.Parser;
import parser.RECompiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
        
/**
 * User: ekaterina_tuzova
 * Test class for brackets symbol
 */
public class TestBrackets {
	@Test
	public void testMatchCase() {
		try {
			Parser parser = RECompiler.compile("[vfba]b");
			assertTrue(parser.matchRE("ab"));
			assertTrue(parser.matchRE("bb"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testNotMatchCase() {
		try {
			Parser parser = RECompiler.compile("[ab]b");
			assertFalse(parser.matchRE("b"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testBracketsStarMatchCase() {
		try {
			Parser parser = RECompiler.compile("[vfba]*");
			assertTrue(parser.matchRE("abff"));
			assertTrue(parser.matchRE("bb"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testBracketsStarNotMatchCase() {
		try {
			Parser parser = RECompiler.compile("[ab]*");
			assertFalse(parser.matchRE("babad"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testDashBracketsMatchCase() {
		try {
			Parser parser = RECompiler.compile("[a-z]*");
			assertTrue(parser.matchRE("zz") );
			assertTrue(parser.matchRE("aza"));
			parser = RECompiler.compile("[A-C]*");
			assertTrue(parser.matchRE("BA") );
			parser = RECompiler.compile("[1-4]*");
			assertTrue(parser.matchRE("13"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testDashBracketsNotMatchCase() {
		try {
			Parser parser = RECompiler.compile("[a-z]*");
			assertFalse(parser.matchRE("abzA"));
		} catch (REException e) {
			e.printStackTrace();
		}
	}
}
