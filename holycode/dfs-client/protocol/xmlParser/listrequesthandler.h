#ifndef LISTREQUESTHANDLER_H
#define LISTREQUESTHANDLER_H

#include "../model/main/mainmodel.h"
#include "../model/filelist.h"

#include <QtXml>
#include <QString>

// handler for parsing all lists response
class ListRequestHandler: public QXmlDefaultHandler {
    public:
        ListRequestHandler(MainModel* model) : myModel(model) {
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
        QSharedPointer<FileList> myFileList;      // for making new filelist in model
};

#endif // LISTREQUESTHANDLER_H
