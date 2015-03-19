#ifndef TESTUSER_H
#define TESTUSER_H

#include <QtTest/QtTest>

class Test: public QObject {
    Q_OBJECT
    private slots:
    // test model
        void testUser();
        void testFileDescription();
        void testFileList();
        void testMainModel();

    // test xml builder
        void testXmlBuilder();
    // test xml parser
        void testXmlParser();
    // test xml parser
        void testTcpConnector();
    // test connector
        void testConnector();
};


#endif // TESTUSER_H
