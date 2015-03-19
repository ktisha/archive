package uk.ac.ebi.tuzova;

import org.junit.Test;

import uk.ac.ebi.microarray.zooma.eval.OntologyMappingOutcome;
import uk.ac.ebi.microarray.zooma.eval.SuccessfulOutcome;
import uk.ac.ebi.microarray.zooma.lang.Value;
import uk.ac.ebi.tuzova.mapping.AEReports;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * User: tuzova
 * Date: Jul 8, 2010
 */
public class TestAEReports {
    @Test
    public void testGenerateAutomaticMappingsReport() {
        List<String> list = new ArrayList();
        list.add("BT474");

        AEReports report = new AEReports(new File(""));
        HashMap<String, OntologyMappingOutcome> map = report.generateAutomaticMappingsReport(list, "efo");


        assertEquals(true, map.containsKey("BT474"));
        assertEquals(true, map.get("BT474") instanceof SuccessfulOutcome);

    }
}
