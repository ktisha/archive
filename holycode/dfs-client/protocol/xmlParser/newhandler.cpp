#include "newhandler.h"

#include "../../view/fail.h"
#include "parser.h"

#include <iostream>

REGISTER_HANDLER(NewHandler, NEW);
REGISTER_HANDLER(NewHandler, ADD);

bool NewHandler::characters (const QString& strText) {
        myStrText = strText;
        return true;
}

bool NewHandler::startElement (const QString& , const QString& , const QString&,
                                  const QXmlAttributes& ) {
    return true;
}

bool NewHandler::endElement (const QString&, const QString&, const QString& name) {
    if (name == "id"){
        if(myState->getState() == NEW) {
            try {
                FileList list;
                list.setName(myState->getFListName());
                list.setIsSearch(false);
                list.setID(myStrText.toInt());
                myModel->addFileList(list);
            } catch (MainModelException const& e) {
                Logger::write( "NewHandler::endElement : EXCEPTION : " + e.message());
                qDebug() << e.message();
            }
        }
        if(myState->getState() == ADD) {
            FileDescription* file = new FileDescription();
            file->setName(myState->getCurrentName());
            file->setSize(myState->getCurrentSize());
            file->setID(myStrText.toInt());
            file->setFileList(myState->getFListName());
            file->setCopy(myState->getNumberOfCopies());
            try {
                myModel->getFileList(myState->getFListName()).addFile(*file);
            } catch (MainModelException const& e) {
                Logger::write( "NewHandler::endElement : EXCEPTION : " + e.message());
                qDebug() << e.message();
            }
        }
        return true;
    }
    if(name == "message"){
        myState->setError(myStrText);
        myState->setState(ERROR);
        return false;
    }
    return true;
}
