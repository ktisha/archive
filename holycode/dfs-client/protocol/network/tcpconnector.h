#ifndef TCPCONNECTOR_H
#define TCPCONNECTOR_H

#include <QTcpSocket>
#include <QtNetwork>

//class provides tcp connection
class TcpConnector: public QTcpSocket {
    Q_OBJECT
    private:
        static QString ourConfigFilePath;       // path to configuration file (server configs)
        static QString ourServer;
        static qint16 ourPort;

    public:
        void connect();
        void closeConnection();
        bool sendData(QByteArray const& arr);          // wrapper over socket.write
        void setServer(QString const& str);     // if it changed in login dialog
        void setPort(QString const& str);       // if it changed in login dialog

    private:
        void setSettings(); //set static fields from the config-file.        

    public:
        TcpConnector();
};

#endif // TCPCONNECTOR_H
