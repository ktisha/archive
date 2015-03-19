#ifndef CONNECTOR_H
#define CONNECTOR_H

#include "./network/tcpconnector.h"
#include "./xmlBuilder/xmlbuilder.h"
#include "../model/main/mainmodel.h"
#include "./xmlParser/parser.h"

//class do connection between classes in protocol( xmlBuilder, Parser, TcpConnector)
// this class know about model
class Connector: public QObject {
    Q_OBJECT

    public:
        Connector( MainModel* model, QIODevice* socket);
        ~Connector() { qDebug() << "~Connector()"; }

    public slots:
        void connectToServer();      // connect using tcpConnector
        void closeConnection();
        void connectionClosed();        // notification
        void error();                // include all errors such as break connection
        void sendData();            // build xml and send it
        bool parseResponse();           // parse incoming xml and get files
        void sendFile();
        void login(QString, QString);

    signals:                        // list of signals  can be generated
        void loginAccepted();       //emit when login response - OK ( use to begin all lists request)
        void getFileLists();        // emit when get all list (for close login dialog and open main window)
        void createProg();          // emit for create progress dialog
        void progress(int i);       // emit for continue progress dialog
        void endProg(int);          // emit for end progress dialog
        void addFile();             // emit for upload file

        void updateTable();         // emit when we need to update table
        void showStatus(QString);
        void fillModel();

    private:
        TcpConnector* myTcp;
        QSharedPointer<XmlBuilder> myXmlBuilder;
        QSharedPointer<Parser> myParser;
        MainModel* myModel;
        RequestState * myState;

    private:
        void download();
};

#endif // CONNECTOR_H
