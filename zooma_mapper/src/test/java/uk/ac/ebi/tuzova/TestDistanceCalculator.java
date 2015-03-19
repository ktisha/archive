package uk.ac.ebi.tuzova;

import org.junit.Test;
import uk.ac.ebi.tuzova.owl.OntologyDistanceCalculator;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * User: tuzova
 * Date: Jul 8, 2010
 */
public class TestDistanceCalculator {
    @Test
    public void testCalculateDistance() {
        OntologyDistanceCalculator distanceCalculator = new OntologyDistanceCalculator("data/test/efo.owl");

        Set set = distanceCalculator.getSimilarNodes("a");

        assertEquals(null, set);

        set = distanceCalculator.getSimilarNodes("<http://www.ebi.ac.uk/efo/EFO_0001088>");
        assertEquals(2, set.size());
    }
}
