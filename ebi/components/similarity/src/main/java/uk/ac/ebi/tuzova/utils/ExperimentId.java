package uk.ac.ebi.tuzova.utils;

/**
 * User: tuzova
 * Date: Jul 13, 2010
 *
 * Class to store in Zooma Map and in final xmlMap
 * contains experiment id and Receiving type (OWL or PUBMED)
 *
 */
public class ExperimentId implements Comparable{
    private String myAccession;
//    private ReceivingType myType;
    private int myOWLDist = Integer.MAX_VALUE;
    private int myPubMedDist = Integer.MAX_VALUE;

    public ExperimentId(String id, ReceivingType rType) {
        this.myAccession = id;
        //this.myType = rType;

        if (rType.equals(ReceivingType.PUBMED))
            myPubMedDist = 0;
        else if (rType.equals(ReceivingType.OWL))
            myOWLDist = 0;
    }

    public ExperimentId(String id, ReceivingType rType, int dist) {
        this.myAccession = id;
        //this.myType = rType;
        if (rType.equals(ReceivingType.PUBMED))
            myPubMedDist = dist;
        else if (rType.equals(ReceivingType.OWL))
            myOWLDist = dist;
    }

    public ExperimentId setDistance(ReceivingType rType, int dist) {
        //this.myType = rType;
        if (rType.equals(ReceivingType.PUBMED))
            myPubMedDist = dist;
        else if (rType.equals(ReceivingType.OWL))
            myOWLDist = dist;
        return this;
    }

    public String getId() {
        return myAccession;
    }

    public ReceivingType getType() {
        //return myType;
        if (myOWLDist != Integer.MAX_VALUE) {
            if (myPubMedDist != Integer.MAX_VALUE)
                return ReceivingType.PUBMED_AND_OWL;
            else
                return ReceivingType.OWL;
        }
        else if (myPubMedDist != Integer.MAX_VALUE)
            return ReceivingType.PUBMED;
        return ReceivingType.NONE;
    }

    public int compareTo(Object other) {
        ExperimentId a = (ExperimentId)other;


        int comp = new Integer(Math.min(myOWLDist, myPubMedDist)).compareTo(
                                        Math.min(a.myOWLDist, a.myPubMedDist));
        if (comp == 0)
            return myAccession.compareTo(a.myAccession);
        else
            return comp;

        /*
        if (myAccession.equals(a.myAccession))
            return getType().compareTo(a.getType());
        return myAccession.compareTo(a.myAccession);
        */
    }

    public int getPubMedDistance() {
        return myPubMedDist;
    }

    public int getOWLDistance() {
        return myOWLDist;
    }

    
    public boolean equals(Object other) {
        if (this == other) return true;
        if (null == other || getClass() != other.getClass()) return false;
        ExperimentId a = (ExperimentId)other;

        if (myAccession.equals(a.myAccession))
            return getType().equals(a.getType());
        return myAccession.equals(a.myAccession);
    }

    public String toString() {
        return "[" + myAccession + ", " + getType() + "]  ";
    }
}
