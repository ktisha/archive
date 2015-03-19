/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package graph;

/**
 *
 * @author catherine
 */
public class GraphException extends Exception{
    public GraphException(Exception e) {
            super(e);
    }
    public GraphException(String string) {
            super(string);
    }
}
