package uk.ac.ebi.tuzova.utils;

/**
 * User: tuzova
 * Date: Jul 7, 2010
 * Time: 12:22:26 PM
 *
 * Class used in DistanceCalculator,
 * contains String name of ontology node,
 * distance to it.
 * Comparable, 'cause it stored in Set
 */
public class SimilarityResult implements Comparable {
    private String myName;
    private int myDistance;

    public SimilarityResult(String name, int distance)  {
        this.myDistance = distance;
        this.myName = name;
    }

    public String getName () {
        return myName;
    }

    public int getDistance() {
        return myDistance;
    }

    public int compareTo(Object other) {
        SimilarityResult a = (SimilarityResult)other;

        if (myDistance == a.myDistance)
            return myName.compareTo(a.myName);
        return new Integer(myDistance).compareTo(a.myDistance);
    }

    public String toString() {
        return myName + " " + myDistance;
    }
}
