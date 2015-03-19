#include "deletehandler.h"

#include "../../view/fail.h"
#include "parser.h"

REGISTER_HANDLER(DeleteHandler, DELETEFILE);

DeleteHandler::DeleteHandler(MainModel* model): myModel(model) {
    myState = myModel->getRequestState();
}

bool DeleteHandler::characters (const QString& strText) {
    myStrText = strText;
    return true;
}

bool DeleteHandler::startElement (const QString& , const QString& , const QString& ,
                                    const QXmlAttributes& ) {
        return true;
}

bool DeleteHandler::endElement (const QString&, const QString&, const QString& name) {
    if (name == "status") {
        if(myStrText == "ok") {
            try {
                int id = myState->getCurrentFileID();
                myModel->getFileList(myState->getFListName()).
                        deleteFile(id);
                if( myModel->getCopyBuffer().getID() == id)
                    myModel->getCopyBuffer().setID(0);
            } catch (MainModelException& e) {
                Logger::write("DeleteHandler" + e.message());
            }
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

