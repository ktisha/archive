#include "xmlbuilder.h"
# include <QSharedPointer>
XmlBuilder::XmlBuilder(MainModel* model) : myModel(model) {
    myState = myModel->getRequestState();
}

QByteArray XmlBuilder::makeXml() {
    QSharedPointer<QDomDocument> doc(new QDomDocument());
    requestToDom(*doc);
    QByteArray block;
    block = doc->toByteArray();
    return block;

/* write request to file(temporary)
    QFile file("request.xml");
    if (!file.open(QIODevice::WriteOnly | QIODevice::Text))
        return 0;
    QTextStream out(&file);
    out << block;*/
}

void XmlBuilder::requestToDom(QDomDocument& doc) {
    // create tag, common for all requests
    QDomElement root = doc.createElement("request");
    root.setAttribute("xmlns", "http://dfs.org/server");
    doc.appendChild(root);
    QDomElement client = doc.createElement("client");
    root.appendChild(client);

    typedef void (XmlBuilder::*RequestHandler)(QDomDocument&, QDomElement);
    typedef std::map< State, RequestHandler > Handlers;

    static Handlers handlers;       // associate name with function pointer (once)
    if (handlers.empty()) {
        handlers[LOGIN]       = &XmlBuilder::loginToDom;
        handlers[SEARCH]      = &XmlBuilder::searchToDom;
        handlers[LISTREQUEST] = &XmlBuilder::listRequestToDom;
        handlers[DOWNLOAD]    = &XmlBuilder::fileActionToDom;
        handlers[ADD]         = &XmlBuilder::fileActionToDom;
        handlers[DELETEFILE]  = &XmlBuilder::fileActionToDom;
        handlers[OPEN]        = &XmlBuilder::listActionToDom;
        handlers[DELETEFLIST] = &XmlBuilder::listActionToDom;
        handlers[NEW]         = &XmlBuilder::listActionToDom;
        handlers[PASTE]         = &XmlBuilder::fileActionToDom;
    }
    Handlers::const_iterator it = handlers.find(myState->getState());
    if ( it != handlers.end() ) {
        (this->*it->second)(doc, client);
    }
}
// create sessionkey tag to request (common for all requests except login)
void XmlBuilder::addSessionKey(QDomDocument &doc, QDomElement root) {
    QDomElement sessionkey = doc.createElement("sessionkey");
    QDomText sessionkeyText = doc.createTextNode(myModel->getUser()->getSessionKey());
    sessionkey.appendChild(sessionkeyText);
    root.appendChild(sessionkey);
}
// create action tag (common for all requests)
void XmlBuilder::addAction(QDomDocument &doc, QDomElement root,
                           const char* strAction) {
    QDomElement action = doc.createElement("action");
    QDomText actionText = doc.createTextNode(strAction);
    action.appendChild(actionText);
    root.appendChild(action);
}
// create string tag
void XmlBuilder::addStr(QDomDocument &doc, QDomElement root, const char* tag,
                                                            QString const& name) {
    QDomElement fileName = doc.createElement(tag);
    QDomText nameText = doc.createTextNode(name);
    fileName.appendChild(nameText);
    root.appendChild(fileName);
}
// create numeric tag
void XmlBuilder::addNum(QDomDocument &doc, QDomElement root, const char* tag,
                        int id) {
    QDomElement fileListID = doc.createElement(tag);
    QDomText idText = doc.createTextNode(QString::number(id));
    fileListID.appendChild(idText);
    root.appendChild(fileListID);
}

void XmlBuilder::loginToDom(QDomDocument& doc, QDomElement root) {
    addAction(doc, root, "login");
    QDomElement login = doc.createElement("login");
    root.appendChild(login);
    addStr(doc, login, "user", myModel->getUser()->getLogin());
    addStr(doc, login, "pass", myModel->getUser()->getPass());
}

void XmlBuilder::searchToDom(QDomDocument& doc, QDomElement root) {
    addSessionKey(doc, root);
    addAction(doc, root, "search");
    QDomElement search = doc.createElement("search");
    root.appendChild(search);
    addStr(doc, search, "query", "%" + myState->
           getCurrentName() + "%");

    if(myState->getCurrentFileListID() != 0)
        addNum(doc, search, "id", myState->getCurrentFileListID());
}

void XmlBuilder::listRequestToDom(QDomDocument& doc, QDomElement root) {
    addSessionKey(doc, root);
    addAction(doc, root, "get-all-list");
}

void XmlBuilder::listActionToDom(QDomDocument& doc, QDomElement root) {
    addSessionKey(doc, root);
    addAction(doc, root, "list-action");
    QDomElement listAction = doc.createElement("list-action");
    root.appendChild(listAction);
    switch(myState->getState()) {
        case OPEN:
            addAction(doc, listAction, "get");
            break;
        case DELETEFILE:
            addAction(doc, listAction, "remove");
            break;
        case DELETEFLIST:
            addAction(doc, listAction, "remove");
            break;
        case NEW:
            addAction(doc, listAction, "create");
            addStr(doc, listAction, "filelistname", myState->getFListName());
            return;
        default:
            return;
    }
    addNum(doc, listAction, "id", myState->getCurrentFileListID());
}

void XmlBuilder::fileActionToDom(QDomDocument& doc, QDomElement root) {
    addSessionKey(doc, root);
    addAction(doc, root, "file-action");

    QDomElement fileAction = doc.createElement("file-action");
    root.appendChild(fileAction);

    switch (myState->getState()) {
        case PASTE:
            addAction(doc, fileAction,"move");
            addNum(doc, fileAction, "id", myState->getCurrentFileID());
            addNum(doc, fileAction, "filelist-id", myState->getCurrentFileListID());
            break;
        case ADD:
            addAction(doc, fileAction,"upload");
            addNum(doc, fileAction, "filesize", myState->getCurrentSize());
            addStr(doc, fileAction, "filename", myState->getCurrentName());
            addNum(doc, fileAction, "filelist-id", myState->getCurrentFileListID());
            addNum(doc, fileAction, "copy", myState->getNumberOfCopies());
            break;
        case DOWNLOAD:
            addAction(doc, fileAction,"download");
            addNum(doc, fileAction, "id", myState->getCurrentFileID());
            break;
        case DELETEFILE:
            addAction(doc, fileAction, "remove");
            addNum(doc, fileAction, "id", myState->getCurrentFileID());
            break;
        default:
            return;
    }
}
