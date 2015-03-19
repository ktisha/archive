#include "graph.h"

int Graph::addVertex( QString data) {
    ++myVertexCounter;
    myVertexes.insert( myVertexCounter, Vertex(myVertexCounter, data));
    return myVertexCounter;
}

Vertex & Graph::getVertex (int index) {
    VertexIt it = myVertexes.find(index);
    if(it == myVertexes.end()) {            // vertex is not in our graph
        GraphException ex("Vertex with such index is not created yet");
        ex.raise();
    }
    return it.value();
}

bool Graph::removeVertex(int index) {
    // delete vertex
    VertexIt it = myVertexes.find(index);
    if(it == myVertexes.end())
        return false;           // vertex is not in our graph
    myVertexes.erase(it);
    // delete edges, connected with that vertex
    for( int i = 0; i <= countVertexes(); ++i) {
        EdgeIt it1 = myEdges.find(qMakePair(index, i));
        if(it1 != myEdges.end())
            myEdges.erase(it1);
        it1 = myEdges.find(qMakePair(i, index));
        if(it1 != myEdges.end())
            myEdges.erase(it1);
    }
    return true;
}

bool Graph::addEdge( int vertex1, int vertex2, QString data) {
    EdgeIt it = myEdges.find(qMakePair(vertex1, vertex2));
    if( it != myEdges.end())
        return false;           // that vertexes are already connected
    VertexIt it1, it2;
    it1 = myVertexes.find(vertex1);
    it2 = myVertexes.find(vertex2);

    if ((it1 == myVertexes.end()) || (it2 == myVertexes.end()))
            return false;                           // if necessary vertexes didn't exist
    if ((myEdges.find(qMakePair(vertex1, vertex2)) == myEdges.end())
                    && (myEdges.find(qMakePair(vertex2, vertex1)) == myEdges.end()))
            myEdges.insert(qMakePair(vertex1, vertex2), Edge(vertex1, vertex2, data));
    return true;
}

bool Graph::addEdge( QString vertex1, QString vertex2, QString data) {
    VertexIt it1, it2;
    // trying to find necessary vertexes
    for (it1 = myVertexes.begin(); it1 != myVertexes.end(); ++it1)
        if(it1.value().getData() == vertex1)
            break;
    for (it2 = myVertexes.begin(); it2 != myVertexes.end(); ++it2)
        if(it2.value().getData() == vertex2)
            break;
    if ((it1 == myVertexes.end()) || (it2 == myVertexes.end()))
        return false;           // necessary vertexes didn't exist
    myEdges.insert(qMakePair(it1.value().getIndex(), it2.value().getIndex()),
                   Edge(it1.value().getIndex(), it2.value().getIndex(), data));
    return true;
}

Edge & Graph::getEdge( int v1, int v2) {
    EdgeIt it = myEdges.find(qMakePair(v1, v2));
    if(it == myEdges.end()) {               // edge didn't exist
        GraphException ex("That vertexes hasn't edge");
        ex.raise();
    }
    return it.value();
}

bool Graph::removeEdge (int v1, int v2) {
    EdgeIt it = myEdges.find(qMakePair(v1, v2));
    if(it == myEdges.end())
        return false;           // edge didn't exist
    // remove vertexes, connected with edge
    myVertexes.erase(myVertexes.find(v1));
    myVertexes.erase(myVertexes.find(v2));
    myEdges.erase(it);
    return true;
}

bool Graph::isIncident(Edge & e, Vertex & v) {
    if ( (e.getLeftVertex() == v.getIndex() || (e.getRightVertex() == v.getIndex()) ))
        return true;
    return false;
}
// deserialization
void Graph::printGraph() {
    qDebug() << "Vertexes";
    for (VertexIt i = myVertexes.begin(); i != myVertexes.end(); ++i)
        qDebug() << i.value().getData();
    qDebug() << "Edges";
    for (EdgeIt i = myEdges.begin(); i != myEdges.end(); ++i)
        qDebug() << i.value().getData();
}

bool Graph::readFile(QFile& file ) {
    QDomDocument domDoc;
    if (file.open(QIODevice::ReadOnly)) {
        if (domDoc.setContent(&file)) {
            QDomElement domElement = domDoc.documentElement();
            analyseDomNode(domElement);
        }
        file.close();
    }
    return true;
}

void Graph::analyseDomNode (const QDomNode& node) {
    QDomNode domNode = node.firstChild();
    static const QString vertexTag = "vertex";
    static const QString edgeTag = "edge";
    while (!domNode.isNull()) {
        if (domNode.isElement()) {
            QDomElement domElement = domNode.toElement();
            if (!domElement.isNull()) {
                if (domElement.tagName() == vertexTag) {
                    QDomNode attribute = domNode.attributes().namedItem("value");
                    addVertex(attribute.nodeValue());
                } else if (domElement.tagName() == edgeTag) {
                    QDomNode attrName = domNode.attributes().namedItem("value");
                    QDomNode attrNode1 = domNode.attributes().namedItem("vertex1");
                    QDomNode attrNode2 = domNode.attributes().namedItem("vertex2");
                    addEdge(attrNode1.nodeValue(), attrNode2.nodeValue(),
                                                        attrName.nodeValue());
                }
            }
        }
        analyseDomNode(domNode);
        domNode = domNode.nextSibling();
    }
}
// serialization
void Graph::writeFile(QFile& f) {
    QDomDocument* doc = new QDomDocument();
    graphToDom(*doc);
    if (!f.open(QIODevice::WriteOnly)) {
        GraphException ex("cannot open file for writing");
        ex.raise();
    }
    QTextStream out(&f);
    doc->save(out, 4);
}

void Graph::graphToDom(QDomDocument& doc) {
    QDomElement root = doc.createElement("graph");
    doc.appendChild(root);
    static const QString vertexTag = "vertex";
    static const QString edgeTag = "edge";
    for (VertexIt it = myVertexes.begin(); it != myVertexes.end(); ++it) {
        QDomElement node = doc.createElement(vertexTag);
        node.setAttribute("value", it.value().getData());
        root.appendChild(node);
    }
    for (EdgeIt it1 = myEdges.begin(); it1 != myEdges.end(); ++it1) {
        QDomElement edge = doc.createElement(edgeTag);
        edge.setAttribute("value", it1.value().getData());

        QString a = myVertexes.find(it1.value().getLeftVertex()).value().getData();
        edge.setAttribute("vertex1", a);
        a = myVertexes.find(it1.value().getRightVertex()).value().getData();
        edge.setAttribute("vertex2", a);
        root.appendChild(edge);
    }
}
