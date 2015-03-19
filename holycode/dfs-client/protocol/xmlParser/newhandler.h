#ifndef NEWHANDLER_H
#define NEWHANDLER_H

#include "../model/main/mainmodel.h"
#include "../model/filedescription.h"

#include <QtXml>
#include <QString>

// handler for parsing new list response, add file response
class NewHandler : public QXmlDefaultHandler {
    public:
    NewHandler(MainModel* model): myModel(model) {
        myState = myModel->getRequestState();
    }

    private:
        bool characters (const QString& strText);
        bool endElement (const QString&, const QString&, const QString& str);
        bool startElement (const QString& , const QString& , const QString& name, const QXmlAttributes& );

    private:
        QString myStrText;
        MainModel* myModel;
        RequestState* myState;
};
#endif // NEWHANDLER_H
