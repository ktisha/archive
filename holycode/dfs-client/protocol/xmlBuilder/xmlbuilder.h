#ifndef XMLBUILDER_H
#define XMLBUILDER_H

#include "../model/main/mainmodel.h"

#include <QtXml>

// class builds xml for all requests
class XmlBuilder: public QObject {
    Q_OBJECT
    public:
        XmlBuilder(MainModel * model);

    private:
        void requestToDom(QDomDocument& doc);
        void loginToDom(QDomDocument& doc, QDomElement root);
        void searchToDom(QDomDocument& doc, QDomElement root);
        void listRequestToDom(QDomDocument& doc, QDomElement root);
        void fileActionToDom(QDomDocument& doc, QDomElement root);
        void listActionToDom(QDomDocument& doc, QDomElement root);

        void addSessionKey(QDomDocument& doc, QDomElement root);
        void addAction(QDomDocument& doc, QDomElement root, const char*);
        void addStr(QDomDocument& doc, QDomElement root, const char* tag,
                                                            QString const& name);
        void addNum(QDomDocument& doc, QDomElement root, const char* tag, int id);

    public slots:
        QByteArray makeXml();

    private:
        MainModel* myModel;
        RequestState* myState;
};

#endif // XMLBUILDER_H
