#ifndef FILELISTEXCEPTION_H
#define FILELISTEXCEPTION_H

#include <QtCore>

class FileListException : public QtConcurrent::Exception {
    public:
        FileListException() {}

    public:
        void raise() const { throw *this; }

        virtual QString message() const throw() {
            return QString("FileList hasn't such file");
        }
};

#endif // FILELISTEXCEPTION_H
