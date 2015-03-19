#ifndef PASTEHANDLER_H
#define PASTEHANDLER_H

#include "../model/main/mainmodel.h"
#include "../model/filedescription.h"

#include <QtXml>
#include <QString>

// handler for parsing paste response
class PasteHandler : public QXmlDefaultHandler {
    public:
        PasteHandler(MainModel* model) : myModel(model) {
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

#endif // PASTEHANDLER_H
