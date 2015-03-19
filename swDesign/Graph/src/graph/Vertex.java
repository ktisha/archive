/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

/**
 *
 * @author catherine
 */
public class Vertex<VertexData> {
    private VertexData myData;
    private int myIndex;

    Vertex(int ind, VertexData data) {
        myIndex = ind;
        myData = data;
    }

    VertexData getData() {
        return myData;
    }

    void setData(VertexData a) {
        myData = a;
    }

    int getIndex() {
        return myIndex;
    }

    public boolean equals(Vertex<VertexData> o) {
        if (myData.equals(o.myData)) {
            return true;
        }
        return false;
    }
}

interface Data {
    public Data getFromString(String s);
}

class MyData implements Data {
    private String myString = new String();

    public MyData getFromString(String s) {
        myString = s;
        return this;
    }
    MyData() {}
}
