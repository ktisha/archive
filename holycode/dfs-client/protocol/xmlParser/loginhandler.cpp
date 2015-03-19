#include "loginhandler.h"
#include "../../view/fail.h"
#include "../../view/mainwindow.h"
#include "parser.h"

#include <iostream>
#include <QTextBrowser>


REGISTER_HANDLER(LoginHandler, LOGIN);
REGISTER_HANDLER(LoginHandler, UPDATE);
REGISTER_HANDLER(LoginHandler, SENDFILE);
REGISTER_HANDLER(LoginHandler, ERROR);
REGISTER_HANDLER(LoginHandler, DELETEFLIST);

bool LoginHandler::characters (const QString& strText) {
        myStrText = strText;
        return true;
}

bool LoginHandler::startElement (const QString& , const QString& , const QString& ,
                                 const QXmlAttributes& ) {
        return true;
}

bool LoginHandler::endElement (const QString&, const QString&, const QString& name) {
    if (name == "message") {
        myState->setError(myStrText);
        myState->setState(ERROR);
        return false;
    }

    if (name == "sessionkey") {
        myModel->getUser()->setSessionKey(myStrText);
        return true;
    }
    return true;
}
