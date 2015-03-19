#ifndef LOGGER_H
#define LOGGER_H

#include <QFile>
#include <QTextStream>
#include <QDateTime>

class Logger {
    public:
        static void write(QString str);
    private:
        Logger(){};
};
#endif // LOGGER_H
