#ifndef GRAPHEXCEPTION_H
#define GRAPHEXCEPTION_H
#include <QtCore>

class GraphException : public QtConcurrent::Exception {
    public:
        GraphException( QString mes) : myMess( mes) {}
        ~GraphException() throw () {}
        void raise() const { throw *this; }

        virtual QString message() const throw() { return myMess; }
    private:
        QString myMess;
};
#endif // GRAPHEXCEPTION_H
