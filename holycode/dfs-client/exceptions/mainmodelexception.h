#ifndef MAINMODELEXCEPTION_H
#define MAINMODELEXCEPTION_H

#include <QtCore>

class MainModelException: public QtConcurrent::Exception {
    public:
        MainModelException() {}

    public:
        void raise() const { throw *this; }
        virtual QString message() const throw() {
            return QString("Model hasn't such filelist");
        }
};
#endif // MAINMODELEXCEPTION_H
