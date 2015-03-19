#ifndef _PARSER_H_
#define _PARSER_H_

#include "../model/main/mainmodel.h"
#include "logger/logger.h"

#include <QStringList>
#include <QtXml>
#include <QSharedPointer>

#include <typeinfo>

// abstract factory creating handlers
class RegisterParserHandler {
public:
    virtual QXmlDefaultHandler * create(MainModel * model) const = 0;
    virtual ~RegisterParserHandler(){}
};

// this class can get some handlers and parse available response
// know about model, needs to know current action to choose handler
class Parser: public QObject {
    Q_OBJECT
    public:
        Parser(MainModel* model);

    public:
        bool parse(QIODevice* input);

    public:
        // fill map of possible handlers
        static void registerFactory(RegisterParserHandler * h, State name) {
            getHandlers()[name] = h;
            Logger::write("Registered [ " + QString::number(name) + " ]");
            qDebug() << "Registered [" << name << "] = " << typeid(h).name();
        }

    private:
        MainModel* myModel;
        RequestState* myState;
        typedef std::map<State, QSharedPointer<QXmlDefaultHandler> > Impls;
        Impls myImpls;      // created handlers

    private:
        typedef std::map<State, RegisterParserHandler *> Handlers;
        static Handlers & getHandlers() {
            static Handlers handlers;       // handlers is not static field in class
            return handlers;                // because static fields initialize in
                                            // unknown order
        }
};

// template factory creates concrete hanler, depens on template
template< class H >
class RegisterParserHandlerImpl : RegisterParserHandler {
    public:
        RegisterParserHandlerImpl( State name ) {
            Parser::registerFactory(this, name);
        }

        virtual QXmlDefaultHandler * create(MainModel * model) const {
            return new H(model);
        }
};

// must be declared in each class (handler) we want to registrate
// when we declare it, constructor called, so it call parser method,
//that register our handler
#define REGISTER_HANDLER(name, action) static RegisterParserHandlerImpl <name> registrator##action(action)

#endif //_PARSER_H_
