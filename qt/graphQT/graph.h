#ifndef _CLASSDIR_H_
#define _CLASSDIR_H_

#include <QTextStream>
#include <QFile>
#include <QtXml>
#include <QString>
#include "GraphException.h"

class Edge {
    public:
        Edge(int v1, int v2, QString data)
            : myVertex1(v1), myVertex2(v2), myData(data){}
        ~Edge()	{}

        QString getData() { return myData; }
        int getLeftVertex() { return myVertex1; }
        int getRightVertex() { return myVertex2; }
    private:
        int myVertex1;
        int myVertex2;
        QString myData;
    private:
    //Edge &operator = (Edge &Other);
    //Edge(Edge &Other);
};

class Vertex {
    public:
        Vertex( int  ind, QString data)
            : myIndex (ind), myData (data) {}
        ~Vertex()	{}

        int getIndex()	{ return myIndex; }
        QString getData() { return myData; }
        bool setData( QString data);
    private:
        int myIndex;
        QString myData;
    private:
    //Vertex &operator = (Vertex &Other);
    //Vertex(Vertex &Other);
};

class Graph {
    public:
        Graph() : myVertexCounter(0)	{}
        ~Graph()	{}
        // operations with vertex
        Vertex & getVertex( int index);
        int addVertex(QString data );		//return index
        bool removeVertex(int ind);
        // operations with edge
        Edge & getEdge( int v1, int v2);
        bool addEdge( int vertex1, int vertex2, QString data);
        bool addEdge( QString vertex1, QString vertex2, QString data);
        bool removeEdge (int v1, int v2);
        // count edge/vertex
        int countVertexes ()	{return myVertexes.size(); }
        int countEdges ()		{return myEdges.size(); }
        // check if Edge incident to Vertex
        bool isIncident (Edge & e, Vertex & v);
        // serialization
        bool readFile(QFile& file );
        void writeFile(QFile & f);
        // print graph to console
        void printGraph();
    private:            // serialization functions
        void graphToDom(QDomDocument& doc);
        void analyseDomNode (const QDomNode & node);
    private:
        QMap <int, Vertex > myVertexes;
        QMap < QPair <int, int>, Edge > myEdges;
        int myVertexCounter;

        typedef QMap < QPair <int, int>, Edge >::iterator EdgeIt;
        typedef QMap <int, Vertex >::iterator VertexIt;
};

#endif
