#include "test.h"
#include "graph.h"

void Test::setGetVertex() {
    Graph g;
    g.addVertex("AA");
    g.addVertex("BB");

    QCOMPARE( 1, g.getVertex(1).getIndex());
}

void Test::setGetEdge() {
    Graph g;
    g.addVertex("a");
    g.addVertex("b");
    g.addEdge(1,2, "F");

    QCOMPARE(QString("F"), QString(g.getEdge(1, 2).getData()));
}

void Test::removeVertex() {
    Graph g;
    g.addVertex("A");
    g.addVertex("B");
    g.removeVertex(1);
    try {
        g.getVertex(1).getData();
    } catch(GraphException &e) {
        QCOMPARE(QString("Vertex with such index is not created yet"), e.message());
    }
}

void Test::removeEdge() {
    Graph g;
    g.addVertex("A");
    g.addVertex("B");
    g.addEdge(1,2, "AB");
    g.removeEdge(1,2);
    try {
        g.getEdge(1, 2).getData();
    } catch(GraphException const &e) {
        QCOMPARE(QString("That vertexes hasn't edge"), e.message());
    }
}

void Test::getNonExistentVertex() {
    Graph g;
    g.addVertex("A");
    g.addVertex("B");
    try {
        g.getVertex(10).getIndex();
    } catch(GraphException &e) {
        QCOMPARE(QString("Vertex with such index is not created yet"), e.message());
    }
}

void Test::getNonExistentEdge() {
    Graph g;
    g.addVertex("A");
    g.addVertex("B");
    g.addEdge(1,2, "AB");
    try {
        g.getEdge(6, 2).getData();
    } catch(GraphException &e) {
        QCOMPARE(QString("That vertexes hasn't edge"), e.message());
    }
}

void Test::countEdges() {
    Graph g;
    g.addVertex("A");
    g.addVertex("B");
    g.addEdge(1, 2, "AB");
    g.addEdge(2, 1, "AB");

    QCOMPARE(1, g.countEdges());
}

void Test::getRemovedEdge() {
    Graph g;
    g.addVertex("A");
    g.addVertex("B");
    g.addEdge(1, 2, "AB");
    g.removeVertex(1);
    try {
        g.getEdge(1, 2);
    } catch(GraphException const &e) {
        QCOMPARE(QString("That vertexes hasn't edge"), e.message());
    }
}

void Test::isIncident() {
    Graph g;
    g.addVertex("A");
    g.addVertex("B");
    g.addEdge(1, 2, "AB");
    QCOMPARE(true, g.isIncident(g.getEdge(1,2), g.getVertex(1)));
}

#ifdef TESTS
QTEST_MAIN(Test)
#endif
