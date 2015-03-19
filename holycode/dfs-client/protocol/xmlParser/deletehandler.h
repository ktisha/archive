#ifndef DELETEHANDLER_H
#define DELETEHANDLER_H
#include "../model/main/mainmodel.h"
#include "../model/filelist.h"

#include <QtXml>
#include <QString>

// handler for parsing delete response
class DeleteHandler : public QXmlDefaultHandler {
    public:
        DeleteHandler(MainModel* model);

        private:
        bool characters (const QString& strText);
        bool endElement (const QString&, const QString&, const QString& str);
        bool startElement (const QString& , const QString& , const QString& name, const QXmlAttributes& );

    private:
        QString myStrText;
        MainModel* myModel;
        RequestState* myState;
        FileList * myFileList;
};

#endif // DELETEHANDLER_H
