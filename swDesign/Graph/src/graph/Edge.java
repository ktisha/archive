/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package graph;

/**
 *
 * @author catherine
 */

public class Edge<EdgeData> {

    private int myVertex1;
    private int myVertex2;
    private EdgeData myData;

    Edge(int v1, int v2) {
        if (v1 < v2) {
            myVertex1 = v1;
            myVertex2 = v2;
        } else {
            myVertex1 = v2;
            myVertex2 = v1;
        }
    }

    Edge(int v1, int v2, EdgeData data) {
        this(v1, v2);
        myData = data;
    }

    EdgeData getData() {
        return myData;
    }

    int getFirstVertex() {
        return myVertex1;
    }

    int getSecondVertex() {
        return myVertex2;
    }

    void setData(EdgeData a) {
        myData = a;
    }

    public boolean equals(Edge<EdgeData> o) {
        if ((myVertex1 == o.myVertex1) && (myVertex2 == o.myVertex2)) {
            return true;
        }
        return false;
    }
}
