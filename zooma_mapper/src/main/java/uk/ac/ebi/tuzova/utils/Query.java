package uk.ac.ebi.tuzova.utils;

/**
 * User: tuzova
 * Date: Jul 6, 2010
 * Time: 2:39:11 PM
 *
 * deprecated
 */
public class Query {
    private final String myFirst;
    private final String mySecond;

    public Query(String first, String second) {
        myFirst = first;
        mySecond = second;
    }

    public String getFirst() {
        return myFirst;
    }

    public String getSecond() {
        return mySecond;
    }

    @Override
    public boolean equals( Object other )
    {
        /*
        if (other instanceof Query) {
            Query otherPair = (Query) other;
            return (( this.myFirst == otherPair.myFirst ||
                    ( this.myFirst != null && otherPair.myFirst != null &&
                      this.myFirst.equals(otherPair.myFirst))) &&
                    (this.mySecond == otherPair.mySecond ||
                    ( this.mySecond != null && otherPair.mySecond != null &&
                      this.mySecond.equals(otherPair.mySecond))) );
        }

        return false;
        */

        if (this == other) return true;
        if (null == other || !(other instanceof Query)) return false;
        Query query = (Query)other;

        return (myFirst.equals(query.getFirst()) && mySecond.equals(query.getSecond()))
                || (mySecond.equals(query.getFirst()) && myFirst.equals(query.getSecond()));

    }

    @Override
    public int hashCode()
    {
        int hashFirst = myFirst != null ? myFirst.hashCode() : 0;
        int hashSecond = mySecond != null ? mySecond.hashCode() : 0;
    
        return (hashFirst + hashSecond);// * hashSecond + hashFirst;

    }
    @Override
    public String toString() {
        return myFirst + " " + mySecond;
    }


}
