#ifndef _SEARCHHANDLER_H_
#define _SEARCHHANDLER_H_

#include "../model/main/mainmodel.h"
#include "../model/filedescription.h"

#include <QtXml>
#include <QString>

// handler for parsing search response, open response
class SearchHandler : public QXmlDefaultHandler {
    public:
        SearchHandler(MainModel* model) : myModel(model) {
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
            QSharedPointer<FileDescription> myFile;        // for adding file to file list
};

#endif //_SEARCHHANDLER_H_


