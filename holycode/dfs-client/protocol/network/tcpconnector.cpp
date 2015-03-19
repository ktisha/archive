#include "tcpconnector.h"
#include <QDebug>
#include "logger/logger.h"

QString TcpConnector::ourConfigFilePath = "./protocol/network/.config.ini";
QString TcpConnector::ourServer = "undefined";
qint16 TcpConnector::ourPort = 0;

TcpConnector::TcpConnector() {
    setSettings();
}

void TcpConnector::connect() {
    Logger::write("TcpConnector::connect() : Server " + ourServer
                  + " Port : " + QString::number(ourPort));
    qDebug() << "Connecting to server " << ourServer;
    qDebug() << "In port " << ourPort;
    connectToHost(ourServer, ourPort);
}

void TcpConnector::setSettings() {
    // get server/port from ourConfigFilePath
    QSettings settings(ourConfigFilePath, QSettings::IniFormat);
    ourServer = settings.value("network/server").toString();
    ourPort = settings.value("network/port").toInt();
}

void TcpConnector::closeConnection() {
    Logger::write("Close connection");
    qDebug() << "Close connection";
    close();
}

bool TcpConnector::sendData(QByteArray const& arr) {
    if(arr.isEmpty() || arr.isNull())
        return false;
    write(arr);
    return true;
}

void TcpConnector::setServer(QString const& str) {
    ourServer = str;
}

void TcpConnector::setPort(QString const& str) {
    ourPort = str.toInt();
}
