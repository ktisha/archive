#ifndef TEST_H
#define TEST_H

#include <QtTest/QtTest>

class Test: public QObject {
    Q_OBJECT
    private slots:
        void setGetVertex();
        void setGetEdge();
        void removeVertex();
        void removeEdge();
        void getNonExistentVertex();
        void getNonExistentEdge();
        void countEdges();
        void getRemovedEdge();
        void isIncident();
};

#endif // TEST_H
