#include "downloadhandler.h"

#include "../../view/fail.h"
#include "parser.h"

REGISTER_HANDLER(DownloadHandler, DOWNLOAD);

DownloadHandler::DownloadHandler(MainModel* model): myModel(model) {
    myState = myModel->getRequestState();
}

bool DownloadHandler::characters (const QString& strText) {
    myStrText = strText;
    return true;
}

bool DownloadHandler::startElement (const QString& , const QString& , const QString& ,
                                    const QXmlAttributes& ) {
//    if ((name == "response") || (name == "body") || (name == "status")
//        || (name == "message") || (name == "size"))
        return true;
//    return false;
}

bool DownloadHandler::endElement (const QString&, const QString&, const QString& name) {
//    if ((name == "response")|| (name == "body") || (name == "status"))
//        return true;
    if (name == "size") {
        myState->setCurrentSize(myStrText.toInt());
        return true;
    }
    if(name == "message") {
        myState->setError(myStrText);
        myState->setState(ERROR);
        return false;
    }
    return true;
}
