/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package graph;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author catherine
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
            MyGraph<String, String> g = new MyGraph<String, String>();
            g.readStringGraphFromFile(new File("./lib/graph2.txt"));
            g.printGraph();
            g.writeStringGraph();
    }

}
