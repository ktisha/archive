#include "logger.h"

void Logger::write(QString str){
    //write request to file
    QDateTime dt = QDateTime::currentDateTime();
    QFile file("./log/main.log");
    if (!file.open(QIODevice::Append))
        return;
    QTextStream out(&file);
    out << dt.toString() << ":\t" << str << "\n";
}

