package uk.ac.ebi.tuzova;

import org.junit.Test;
import uk.ac.ebi.tuzova.utils.SimilarityResult;
import static org.junit.Assert.*;
/**
 * User: tuzova
 * Date: Jul 8, 2010
 */
public class TestSimilarityResult {

    @Test
    public void testCompareTo() {
        SimilarityResult result1 = new SimilarityResult("A", 1);
        SimilarityResult result2 = new SimilarityResult("A", 1);
        SimilarityResult result3 = new SimilarityResult("A", 2);
        SimilarityResult result4 = new SimilarityResult("B", 1);
        SimilarityResult result5 = new SimilarityResult("B", 2);

        assertEquals(0, result1.compareTo(result2));
        assertEquals(-1, result1.compareTo(result3));
        assertEquals(-1, result1.compareTo(result4));
        assertEquals(-1, result1.compareTo(result5));

        assertEquals(1, result3.compareTo(result1));
        assertEquals(1, result4.compareTo(result1));
        assertEquals(1, result5.compareTo(result1));

    }
}
