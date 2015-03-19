#include "pastehandler.h"
#include "../../view/fail.h"

#include <iostream>

#include "parser.h"

REGISTER_HANDLER(PasteHandler, PASTE);

bool PasteHandler::characters (const QString& strText) {
        myStrText = strText;
        return true;
}

bool PasteHandler::startElement (const QString& , const QString& , const QString&,
                                  const QXmlAttributes& ) {
    return true;
}

bool PasteHandler::endElement (const QString&, const QString&, const QString& name) {
    if (name == "status") {
        if(myStrText == "ok") {
            QString name = myModel->getCopyBuffer().getFileList();
            myModel->getFileList(name).deleteFile(myState->getCurrentFileID());
            FileDescription file = myModel->getCopyBuffer();
            file.setFileList(myState->getFListName());
            myModel->getFileList(myState->getFListName()).addFile(file);
        }
        return true;
    }

    if(name == "message") {
        myState->setError(myStrText);
        myState->setState(ERROR);
        return false;
    }
    return true;
}
