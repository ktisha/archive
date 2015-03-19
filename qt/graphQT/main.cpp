#include "graph.h"
#ifndef TESTS

int main() {
    QFile f("input.xml");
    Graph g1;
    g1.readFile(f);
    f.close();
	
    g1.printGraph();
    QFile f1("output.xml");
    g1.writeFile(f1);

    return 0;
}
#endif
