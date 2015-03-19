package uk.ac.ebi.tuzova;

import org.junit.Test;
import org.w3c.dom.NodeList;
import uk.ac.ebi.tuzova.xml.XPathParser;

import static org.junit.Assert.*;


/**
 * User: tuzova
 * Date: Jul 8, 2010
 */
public class TestParser {
    @Test
    public void testExtractExperiment() {
        XPathParser experimentsParser = new XPathParser("data/test/experiments.xml");
        NodeList experiments = experimentsParser.extractExperiments();
        assertEquals(12581, experiments.getLength());
    }
}
