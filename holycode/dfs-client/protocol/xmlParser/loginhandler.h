#ifndef LOGINHANDLER_H
#define LOGINHANDLER_H

#include <QtXml>
#include <QString>

#include"../model/main/mainmodel.h"

// handler for parsing login response
// knows about model
class LoginHandler: public QXmlDefaultHandler{
    public:
        LoginHandler (MainModel* model): myModel(model){
            myState = myModel->getRequestState();
        }
        ~LoginHandler() {}

    private:
        bool characters (const QString& strText);
        bool endElement (const QString&, const QString&, const QString& str);
        bool startElement (const QString& , const QString& , const QString& name, const QXmlAttributes& );

    public:
        bool setSessionKey(QString key);

    private:
        QString myStrText;
        MainModel* myModel;
        RequestState* myState;
};

#endif // LOGINHANDLER_H
