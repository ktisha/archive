#include "listrequesthandler.h"
#include "../../view/fail.h"
#include "parser.h"

#include <iostream>

REGISTER_HANDLER(ListRequestHandler, LISTREQUEST);

bool ListRequestHandler::characters (const QString& strText) {
        myStrText = strText;
        return true;
}

bool ListRequestHandler::startElement (const QString& , const QString& ,
                                       const QString& name, const QXmlAttributes&) {
    if (name == "filelist") {
        myFileList = QSharedPointer<FileList>(new FileList());
        myFileList->setLoaded(false);
        return true;
    }
        return true;
}

bool ListRequestHandler::endElement (const QString&, const QString&,
                                     const QString& name) {
    if (name == "status") {
        if(myStrText != "error")
            myModel->clear();
    }
    if (name == "filelist"){
        myModel->addFileList(*myFileList.data());
        std::cout << " Adding filelist with ID " << myFileList->getID() <<
                " named " << myFileList->getName().toStdString()<< std::endl;
        return true;
    }

    if (name == "filelistname"){
        myFileList->setName(myStrText);
        return true;
    }
    if (name == "id"){
        myFileList->setID(myStrText.toInt());
        return true;
    }

    if(name == "message"){
        myState->setError(myStrText);
        myState->setState(ERROR);
        return false;
    }
    return true;
}
