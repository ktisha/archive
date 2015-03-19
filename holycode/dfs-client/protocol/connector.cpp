#include "connector.h"
#include <QDebug>
#include "fail.h"

Connector::Connector(MainModel* model, QIODevice* socket): myModel(model) {
    myXmlBuilder = QSharedPointer<XmlBuilder>(new XmlBuilder(myModel));
    myParser = QSharedPointer<Parser>(new Parser(myModel));
    myTcp = qobject_cast<TcpConnector* >(socket);
    myState = myModel->getRequestState();

    //connect signals inside protocol
    connect(myTcp, SIGNAL(error(QAbstractSocket::SocketError)), this, SLOT(error()));
    connect(myTcp, SIGNAL(disconnected()), this, SLOT(connectionClosed()));
    connect(myTcp, SIGNAL(connected()), this, SLOT(sendData()));
    //connect(myTcp, SIGNAL(readyRead()), this, SLOT(sendFile()));
}

void Connector::login(QString server, QString port) {
    myTcp->setServer(server);
    myTcp->setPort(port);
    connectToServer();
}

void Connector::connectionClosed() {
    if(myState->getState() == ERROR)
        return;
    parseResponse();
}

void Connector::sendData() {
    Logger::write("Connector::sendData() : ACTION : "
                        + stateToStr(myState->getState()));
    QByteArray arr = myXmlBuilder->makeXml();
    int a = arr.size();
    if( a == 0)
        return;
    myTcp->write((char *)&a, 4);     // send size of xml
    myTcp->sendData(arr);            // send request
    qDebug() << arr;
    if(myState->getState() == ADD)
        sendFile();
}

void Connector::sendFile() {
    if (myTcp->waitForDisconnected(300)) {
        myState->setState(UPDATE);
        emit updateTable();
        return;
    }
    QFile file(myState->getPath());
    if (file.open(QIODevice::ReadOnly) ) {
        Logger::write("Connector::sendFile() : file name : " +
                      myState->getCurrentName());
        //preparing to send file
        int size = myState->getCurrentSize();
        int tot = size/1024;
        uint mod = size%1024;
        //QSharedPointer<QByteArray> block(new QByteArray());
        QByteArray block;
        emit createProg();      // create progress dialog
        int counter = 0;
        // sending
        for (int i = 0; i < tot; ++i) {
            block = file.read(1024);
            myTcp->write(block);
            emit progress(100*counter/size+1);      // set progress dialog value
            counter += 1024;
        }
        block = file.read(mod);
        myTcp->write(block);
        emit endProg(101);              // close progress dialog
    }
}

void Connector::download() {
    static qint64 counter = 0;
    QFile file(myState->getPath());
    if(counter == 0) {
        emit createProg();
        qDebug() << "(counter == 0)";
        if (!file.open(QIODevice::WriteOnly) )      // creating file
            return;
    } else {
        if (!file.open(QIODevice::Append) )
            return;
    }
    int size = myState->getCurrentSize();       // file size
    int mod =  size - counter;
    // actions when download ends
    if( counter >= size) {
        qDebug() << "counter >= size" << counter;
        emit endProg(101);
        counter = 0;
        myTcp->close();
        myState->setState(UPDATE);
        file.close();
        return;
    }
    // download
    char ch[1024];
    qint64 i = 0;
    i = myTcp->read(ch, qMin(mod, 1024));
    qDebug() << "(i )" << i;
    emit progress(100*counter/size+1);
    if(i != -1) {
        counter += i;
        qDebug() << "(i != -1)" << counter;
        QDataStream out(&file);
        out.writeRawData(ch, i);
        file.close();
        download();
    } else {
        emit endProg(101);
        counter = 0;
    }
    file.close();
    return;
}

bool Connector::parseResponse() {
    Logger::write("Connector::parseResponse() : ACTION : "
                  + stateToStr(myState->getState()));

    QSharedPointer<QByteArray> block(new QByteArray);
    if(myState->getState() != GETFILE) {     // read xml from socket
        int a;
        myTcp->read((char *)&a, 4);
        *block = myTcp->read(a);
        qDebug() << *block;
    } else {            // download file
        download();
        return true;
    }
    QSharedPointer<QBuffer> buff(new QBuffer(block.data()));
    if(myParser->parse(buff.data())) {
        switch(myState->getState()) {
            case NEW:
            case OPEN:
            case SEARCH:
            case PASTE:
                emit updateTable();
                break;
            case ADD:
                emit showStatus("File has successfully uploaded");
                myState->setState(ADD);
                emit fillModel();
                break;
            case DELETEFILE:
                myState->setState(UPDATE);
                emit updateTable();
                break;
            case LISTREQUEST:
                closeConnection();
                emit getFileLists();
                break;
            case DELETEFLIST:
            case LOGIN:
                myState->setState(LISTREQUEST);
                connectToServer();
                break;
            case DOWNLOAD:
                myState->setState(GETFILE);
                if(myTcp->bytesAvailable() != 0)
                    download();
                emit showStatus("File has successfully downloaded");
                break;
            default:
                Logger::write("Unknown state\t" + stateToStr(myState->getState())   );
                qDebug() << "Unknown state" << stateToStr(myState->getState());
                return true;
        }
    } else {
        Logger::write("Connector::parseResponse(): error \t" + myState->getError());
        qDebug() << myState->getError();
        Fail fail("Fail", myState->getError());
        fail.exec();
        return false;
    }
    return true;
}

void Connector::connectToServer() {
    myTcp->connect();
}

void Connector::closeConnection() {
    myTcp->closeConnection();
}

void Connector::error() {
    Logger::write("Connector::error() : " + myTcp->errorString());
    if ((myTcp->error() == QAbstractSocket::RemoteHostClosedError) ||
        (myTcp->error() == QAbstractSocket::SocketTimeoutError))
        return;
    Fail fail("Error", myTcp->errorString());
    fail.exec();
}
