#include "parser.h"
#include <QXmlDefaultHandler>

Parser::Parser(MainModel* model): myModel(model) {
    myState = myModel->getRequestState();
}

bool Parser::parse(QIODevice* input) {
    // try to find created handler
    Impls::const_iterator ii = myImpls.find(myState->getState());
    if ( ii == myImpls.end() ) {     // if that handler not created yet:
        Handlers::const_iterator it = getHandlers().find(myState->getState());
        // check is that handler registered
        if (it == getHandlers().end()) {
            Logger::write( "Parser::parse : Handler doesn't register");
            qDebug() << "Handler doesn't register";
            return false;
        }

        QSharedPointer<QXmlDefaultHandler> hand(it->second->create(myModel));   // creating handler
        ii = myImpls.insert( Impls::value_type(myState->getState(), hand) ).first;
    }

    QXmlInputSource source(input);
    QXmlSimpleReader reader;

    reader.setContentHandler(ii->second.data());
    return reader.parse(source);            // parse response
}
