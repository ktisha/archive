/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;


/**
 *
 * @author catherine
 */
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

public class MyTest extends TestCase {

    public void testGetVertex() {
        MyGraph<String, String> g = new MyGraph<String, String>();
        MyGraph<String, String> gg = new MyGraph<String, String>();
        try {
            g.readStringGraphFromFile(new File("./test/lib/graph1.txt"));
            gg.readStringGraphFromFile(new File("./test/lib/graph2.txt"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String expectedReturn = "A";
        String actualReturn = g.getVertex("A").getData();
        assertEquals("return value", true, g.equals(gg));
        assertEquals("return value", expectedReturn, actualReturn);
    }

    public void testGetEdge() {
        MyGraph<String, String> g = new MyGraph<String, String>();
        MyGraph<String, String> gg = new MyGraph<String, String>();
        try {
            g.readStringGraphFromFile(new File("./test/lib/graph3.txt"));
            gg.readStringGraphFromFile(new File("./test/lib/graph4.txt"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Vertex<String> a = g.addVertex("A");
        Vertex<String> b = g.addVertex("B");

        g.addEdge(a, b, "F");

        String expectedReturn = "F";
        String actualReturn = null;
        try {
            actualReturn = g.getEdge(a, b).getData();
        } catch (GraphException ex) {
            System.err.println(ex.toString());
        }
        assertEquals("return value", expectedReturn, actualReturn);
        assertEquals("return value", true, g.equals(gg));
    }

    public void testExceptRemoveGetVertex() {
        MyGraph<String, String> g = new MyGraph<String, String>();
        MyGraph<String, String> gg = new MyGraph<String, String>();
        try {
            g.readStringGraphFromFile(new File("./test/lib/graph1.txt"));
            gg.readStringGraphFromFile(new File("./test/lib/graph5.txt"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        g.removeVertex("A");
        String expectedReturn = "No Vertex with data A";

        try {
            g.getVertex("A").getData();

        } catch (Exception e) {
            String actualReturn = e.getMessage();
            assertEquals("return value", expectedReturn, actualReturn);
        }
        assertEquals("return value", true, g.equals(gg));
    }

    public void testExceptGetVertex() {
        MyGraph<String, String> g = new MyGraph<String, String>();
        try {
            g.readStringGraphFromFile(new File("./test/lib/graph1.txt"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String expectedReturn = "No Vertex with data C";
        try {
            g.getVertex("C");

        } catch (Exception e) {
            String actualReturn = e.getMessage();
            assertEquals("return value", expectedReturn, actualReturn);
        }
    }

    public void testExceptRemoveGetEdge() {
        MyGraph<String, String> g = new MyGraph<String, String>();
        MyGraph<String, String> gg = new MyGraph<String, String>();
        try {
            g.readStringGraphFromFile(new File("./test/lib/graph3.txt"));
            gg.readStringGraphFromFile(new File("./test/lib/graph6.txt"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Vertex<String> a = g.addVertex("A");
        Vertex<String> b = g.addVertex("B");

        g.addEdge(a, b, "F");
        try {
            g.removeEdge(a, b);
        } catch (GraphException ex) {
            System.err.println(ex.toString());
        }

        String expectedReturn = "No Edge between A and B";
        try {
            g.getEdge(a, b);

        } catch (Exception e) {
            String actualReturn = e.getMessage();
            assertEquals("return value", expectedReturn, actualReturn);
        }
        assertEquals("return value", true, g.equals(gg));
    }

    public void testExceptGetEdge() {
        MyGraph<String, String> g = new MyGraph<String, String>();

        Vertex<String> a = g.addVertex("A");
        Vertex<String> b = g.addVertex("B");
        Vertex<String> c = g.addVertex("C");

        g.addEdge(a, b, "F");

        String expectedReturn = "No Edge between C and B";

        try {
            g.getEdge(c, b);

        } catch (Exception e) {
            String actualReturn = e.getMessage();
            assertEquals("return value", expectedReturn, actualReturn);
        }
    }

    public void testExceptRemoveVertGetEdge() {
        MyGraph<String, String> g = new MyGraph<String, String>();
        MyGraph<String, String> gg = new MyGraph<String, String>();
        try {
            g.readStringGraphFromFile(new File("./test/lib/graph3.txt"));
            gg.readStringGraphFromFile(new File("./test/lib/graph7.txt"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Vertex<String> a = g.addVertex("A");
        Vertex<String> b = g.addVertex("B");

        g.addEdge(a, b, "F");

        g.removeVertex("A");

        String expectedReturn = "No Edge between A and B";

        try {
            g.getEdge(a, b);

        } catch (Exception e) {
            String actualReturn = e.getMessage();
            assertEquals("return value", expectedReturn, actualReturn);
        }
        assertEquals("return value", true, g.equals(gg));
    }

    public void testAddEdge() {
        MyGraph<String, String> g = new MyGraph<String, String>();
        MyGraph<String, String> gg = new MyGraph<String, String>();
        try {
            g.readStringGraphFromFile(new File("./test/lib/graph3.txt"));
            gg.readStringGraphFromFile(new File("./test/lib/graph8.txt"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Vertex<String> a = g.addVertex("A");
        Vertex<String> b = g.addVertex("B");

        g.addEdge(a, b, "F");
        g.addEdge(b, a, "F");

        int expectedReturn = 1;
        int actualReturn = g.countEdges();
        assertEquals("return value", expectedReturn, actualReturn);
        assertEquals("return value", true, g.equals(gg));
    }

    public void testIsIncident() {
        MyGraph<String, String> g = new MyGraph<String, String>();

        Vertex<String> a = g.addVertex("A");
        Vertex<String> b = g.addVertex("B");

        g.addEdge(a, b, "F");

        boolean expectedReturn = true;
        boolean actualReturn = false;
        try {
            actualReturn = g.isIncident(g.getEdge(a, b), g.getVertex("A"));
        } catch (GraphException ex) {
            System.err.println(ex.toString());
        }
        assertEquals("return value", expectedReturn, actualReturn);
    }

    public void testHasEdge() {
        MyGraph<String, String> g = new MyGraph<String, String>();
        Vertex<String> a = g.addVertex("A");
        Vertex<String> b = g.addVertex("B");

        g.addEdge(a, b, "F");

        boolean expectedReturn = true;
        boolean actualReturn = g.hasEdge(a, b);
        assertEquals("return value", expectedReturn, actualReturn);
    }

    public void testHasVertex() {
        MyGraph<String, String> g = new MyGraph<String, String>();
        Vertex<String> a = g.addVertex("A");
        Vertex<String> b = g.addVertex("B");

        g.addEdge(a, b, "F");

        boolean expectedReturn = true;
        boolean actualReturn = g.hasVertex("A");
        assertEquals("return value", expectedReturn, actualReturn);
    }

    public void testEqualsGraph() {
        MyGraph<String, String> g = new MyGraph<String, String>();
        Vertex<String> a = g.addVertex("A");
        Vertex<String> b = g.addVertex("B");

        g.addEdge(a, b, "F");

        MyGraph<String, String> g1 = new MyGraph<String, String>();
        Vertex<String> b1 = g1.addVertex("B");
        Vertex<String> a1 = g1.addVertex("A");
        g1.addEdge(b1, a1, "F");

        MyGraph<String, String> g3 = new MyGraph<String, String>();
        MyGraph<String, String> g2 = new MyGraph<String, String>();
        try {
            g3.readStringGraphFromFile(new File("./test/lib/graph9.txt"));
            g2.readStringGraphFromFile(new File("./test/lib/graph9.txt"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        boolean expectedReturn = true;
        boolean actualReturn = g.equals(g1);
        assertEquals("return value", expectedReturn, actualReturn);
        assertEquals("return value", true, g2.equals(g3));
        assertEquals("return value", true, g.equals(g3));
    }

}
