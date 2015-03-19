#ifndef DOWNLOADHANDLER_H
#define DOWNLOADHANDLER_H

#include "../model/main/mainmodel.h"
#include "../model/filelist.h"

#include <QtXml>
#include <QString>

// handler for parsing download response
class DownloadHandler: public QXmlDefaultHandler{
    public:
        DownloadHandler(MainModel* model);

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

#endif // DOWNLOADHANDLER_H
