#include "searchhandler.h"
#include "../../view/fail.h"

#include <iostream>

#include "parser.h"

REGISTER_HANDLER(SearchHandler, SEARCH);
REGISTER_HANDLER(SearchHandler, OPEN);

bool SearchHandler::characters (const QString& strText) {
        myStrText = strText;
	return true;
}

bool SearchHandler::startElement (const QString& , const QString& , const QString& name,
                                  const QXmlAttributes& ) {
    if (name == "file") {
        myFile = QSharedPointer<FileDescription>(new FileDescription);
        return true;
    }
    return true;
}

bool SearchHandler::endElement (const QString&, const QString&, const QString& name) {
    if (name == "status") {
        if(myStrText == "error")
            return false;
        if (myState->getState() != SEARCH)
            return true;
        FileList searchFileList;
        searchFileList.setName(myState->getFListName());
        searchFileList.setIsSearch(true);
        myModel->addFileList(searchFileList);
    }

    if (name == "filename") {
        myFile->setName(myStrText);
        return true;
    }
    if (name == "id") {
        myFile->setID(myStrText.toInt());
        return true;
    }
    if (name == "copy") {
        myFile->setCopy(myStrText.toInt());
        return true;
    }
    if (name == "filesize") {
        myFile->setSize(myStrText.toInt());
        return true;
    }

    if (name == "filelist") {
        myFile->setFileList(myStrText);
        return true;
    }
    if (name == "file") {
        try {
            myModel->getFileList(myState->getFListName()).
                            addFile(*myFile.data());
        } catch (MainModelException const& e) {
            Logger::write( "SearchHandler::endElement : EXCEPTION : " + e.message());
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
