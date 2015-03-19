/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

/**
 *
 * @author catherine
 */
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

interface Graph<T1, T2> {
    Vertex<T1> addVertex(T1 data);		//return Vertex
    Vertex<T1> getVertex(T1 data);		//return vertex
    void removeVertex(T1 data);			// exception if didn't exist
    boolean hasVertex(T1 data);		// has out graph vertex with data
    boolean hasVertex(Vertex<T1> o);		// has concrete vertex
    Edge<T2> getEdge(Vertex<T1> v1, Vertex<T1> v2) throws GraphException;	// return edge
    void addEdge(Vertex<T1> vertex1, Vertex<T1> vertex2, T2 data);// if vertexes didn't exist - create them
    void removeEdge(Vertex<T1> v1, Vertex<T1> v2) throws GraphException;// exception if edge or vertex didn't exist
    boolean hasEdge(Vertex<T1> v1, Vertex<T1> v2);	// edge between v1, v2
    boolean hasEdge(int first, int second);		// has edge with such indexes
    int countVertexes();				// count vertex
    int countEdges();					// count edge
    Boolean isIncident(Edge<T2> e, Vertex<T1> v);		// check incident
    void readStringGraphFromFile(File f) throws IOException;	// read Graph<String, String> from file
    boolean equals(Graph<T1, T2> o);				// graph equals
    void printGraph();					// print graph to console
}

public class MyGraph<T1, T2> implements Graph<T1, T2> {

    private ArrayList<Edge<T2>> myEdges;			// edge array
    private HashMap<Vertex<T1>, HashSet<Integer>> myTable;	// adjacency list
    private int myVertexCounter = 0;
    private int myEdgeCounter = 0;

    MyGraph() {
        myTable = new HashMap<Vertex<T1>, HashSet<Integer>>();
        myEdges = new ArrayList<Edge<T2>>();
    }

    public void addEdge(Vertex<T1> v1, Vertex<T1> v2, T2 data) {
        Iterator < Edge <T2> > it = null;
        Edge <T2> tmp = new Edge<T2>(0, 0);
        for (it = myEdges.iterator(); it.hasNext();) {
            tmp = it.next();
            if ((v1.getIndex() == tmp.getFirstVertex() && v2.getIndex() == tmp.getSecondVertex())
                    || (v2.getIndex() == tmp.getFirstVertex() && v1.getIndex() == tmp.getSecondVertex())) {
                return;
            }
        }

        if (hasVertex(v1.getData()) == false) {
            ++myVertexCounter;
            myTable.put(v1, new HashSet<Integer>());
        }
        if (hasVertex(v2.getData()) == false) {
            ++myVertexCounter;
            myTable.put(v2, new HashSet<Integer>());
        }
        ++myEdgeCounter;

        myEdges.add(new Edge<T2>(v1.getIndex(), v2.getIndex(), data));
        myTable.get(v1).add(myEdgeCounter);
        myTable.get(v2).add(myEdgeCounter);
    }

    public Vertex<T1> addVertex(T1 data) {
        ++myVertexCounter;
        Vertex<T1> tmp = new Vertex<T1>(myVertexCounter, data);
        myTable.put(tmp, new HashSet<Integer>());
        return tmp;
    }

    public Edge<T2> getEdge(Vertex<T1> v1, Vertex<T1> v2) throws GraphException {
        Iterator<Edge<T2>> it = null;
        Edge<T2> tmp = new Edge<T2>(1, 1);
        for (it = myEdges.iterator(); it.hasNext();) {
            tmp = it.next();
            if ((v1.getIndex() == tmp.getFirstVertex() && v2.getIndex() == tmp.getSecondVertex())
                    || (v2.getIndex() == tmp.getFirstVertex() && v1.getIndex() == tmp.getSecondVertex())) {
                return tmp;
            }
            if (v2.getIndex() == tmp.getFirstVertex() && v1.getIndex() == tmp.getSecondVertex()) {
                return tmp;
            }
        }
//		EXCEPTION
        throw new GraphException("No Edge between " + v1.getData().toString() + " and "
                + v2.getData().toString());
    }

    public Vertex<T1> getVertex(T1 data) {
        Iterator<Vertex<T1>> it = null;
        Vertex<T1> tmp = new Vertex<T1>(0, data);
        for (it = myTable.keySet().iterator(); it.hasNext();) {
            tmp = it.next();

            if (data.equals(tmp.getData())) {
                return tmp;
            }
        }
        //EXCEPTION
        throw new NullPointerException("No Vertex with data " + data.toString());
    }

    public void removeEdge(Vertex<T1> v1, Vertex<T1> v2) throws GraphException {
        myEdges.remove(getEdge(v1, v2));
        --myEdgeCounter;
    }

    public void removeVertex(T1 data) {
        Iterator it = myTable.get(getVertex(data)).iterator();
        while (it.hasNext()) {
            removeEdge((Integer) it.next() - 1);
        }
        myTable.remove(getVertex(data));
        --myVertexCounter;
    }

    public int countVertexes() {
        return myTable.size();
    }

    public int countEdges() {
        return myEdges.size();
    }

    public boolean equals(Graph<T1, T2> o) {
        Iterator<Edge<T2>> it = null;
        if (o.countEdges() != countEdges())
            return false;
        if (o.countVertexes() != countVertexes())
            return false;
        for (it = myEdges.iterator(); it.hasNext();) {
            Edge<T2> tmp = it.next();
            if (o.hasEdge(((Edge<T2>) tmp).getFirstVertex(), ((Edge<T2>) tmp).getSecondVertex()) == false) {
                return false;
            }
        }
        Iterator<Vertex<T1>> it1 = null;

        for (it1 = myTable.keySet().iterator(); it1.hasNext();) {
            Vertex<T1> tmp = it1.next();

            if (o.hasVertex(((Vertex<T1>) tmp).getData()) == false) {
                return false;
            }
        }
        return true;
    }

    public boolean hasVertex(Vertex<T1> o) {
        return myTable.containsKey(o);
    }

    public boolean hasEdge(int first, int second) {
        Iterator<Edge<T2>> it = null;
        for (it = myEdges.iterator(); it.hasNext();) {
            if (it.next().equals(new Edge<T2>(first, second))) {
                return true;
            }
        }
        return false;

    }

    public boolean hasEdge(Vertex<T1> v1, Vertex<T1> v2) {
        return hasEdge(v1.getIndex(), v2.getIndex());
    }

    @SuppressWarnings("unchecked")
    public boolean hasVertex(T1 data) {
        Iterator it = null;
        for (it = myTable.keySet().iterator(); it.hasNext();) {
            if (new Vertex<T1>(0, data).equals((Vertex<T1>) it.next())) {
                return true;
            }
        }
        return false;
    }

    public Boolean isIncident(Edge<T2> e, Vertex<T1> v) {
        if ((e.getFirstVertex() == v.getIndex()) || (e.getSecondVertex() == v.getIndex())) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public void readStringGraphFromFile(File f) throws IOException { //читает только граф с данными-строками
        BufferedReader in = new BufferedReader(new FileReader(f.getAbsolutePath()));
        String s = new String();
        s = in.readLine();
        StringTokenizer str = new StringTokenizer(s);

        while (str.hasMoreTokens()) {
            addVertex((T1) str.nextToken());
        }

        while ((s = in.readLine()) != null) {
            str = new StringTokenizer(s);
            String v1 = str.nextToken();
            String v2 = str.nextToken();
            while (str.hasMoreTokens()) {
                addEdge(getVertex((T1) v1), getVertex((T1) v2), (T2) str.nextToken());
            }
        }
        in.close();
    }

    public void writeStringGraph() {
        Iterator<Vertex<T1>> it = null;
        Vertex<T1> tmp = null;
        FileWriter fstream = null;
        try {
            fstream = new FileWriter("./lib/out.txt");
            BufferedWriter out = new BufferedWriter(fstream);

            for (it = myTable.keySet().iterator(); it.hasNext();) {
                tmp = it.next();
                out.write(tmp.getData() + " ");
                //System.out.println(tmp.getData());
            }
            out.write("\n");
            Iterator<Edge<T2>> it1 = myEdges.iterator();
            Edge<T2> tmp1 = null;
            while(it1.hasNext()) {
                tmp1 = it1.next();
                out.write(getVertex(tmp1.getFirstVertex()).getData()
                    + " " + getVertex(tmp1.getSecondVertex()).getData()
                        + " " + tmp1.getData() + "\n");
            }
        out.close();
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

    public void printGraph() {
        System.out.println("Vertexes:");

        Iterator<Vertex<T1>> it = null;
        Vertex<T1> tmp = null;
        for (it = myTable.keySet().iterator(); it.hasNext();) {
            tmp = it.next();
            System.out.println(tmp.getData());
        }

        System.out.println("Edges:");
        Iterator<Edge<T2>> it1 = myEdges.iterator();
        Edge<T2> tmp1 = null;
        while(it1.hasNext()) {
            tmp1 = it1.next();
            System.out.println(tmp1.getData() + " between "
                    + getVertex(tmp1.getFirstVertex()).getData() + " "
                    + getVertex(tmp1.getSecondVertex()).getData());
        }
    }

    private Vertex<T1> getVertex(int ind) {
        Iterator<Vertex<T1>> it = myTable.keySet().iterator();
        Vertex<T1> tmp = null;
        while(it.hasNext()) {
            tmp = it.next();
            if (tmp.getIndex() == ind) {
                return tmp;
            }
        }
        return null;
    }

    private void removeEdge(int ind) {
        myEdges.remove(ind);
        --myEdgeCounter;
    }
}
