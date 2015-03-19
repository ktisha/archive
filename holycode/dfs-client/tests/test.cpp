#include "test.h"
#include "../model/user.h"
#include "../protocol/xmlBuilder/xmlbuilder.h"
#include "../protocol/xmlParser/parser.h"
#include "../model/main/mainmodel.h"
#include "../protocol/xmlParser/loginhandler.h"
#include "../protocol/network/tcpconnector.h"
#include "../controller/controller.h"
#include <QSharedPointer>

void Test::testUser() {
    User user;
    user.setSessionKey("3f3479149b4b55f26ffc66850065e3c0ebde150f");
    user.setLogin("Adolf");
    user.setPass("C5raWr+6a6");
    QCOMPARE(user.getPass(), QString("C5raWr+6a6"));
    QCOMPARE(user.getLogin(), QString("Adolf"));
    QCOMPARE(user.getSessionKey(),
                    QString("3f3479149b4b55f26ffc66850065e3c0ebde150f"));
}
void Test::testXmlBuilder() {
    // created data
    QSharedPointer<User> user(new User());
    user->setLogin("student");
    user->setPass("student");
    user->setSessionKey("39eb7364d5b2b43c53f4a475492a3ab00e3ecb37");
    QSharedPointer<MainModel> model(new MainModel(user.data()));
    QByteArray block;
    QByteArray testBlock;
    QSharedPointer<XmlBuilder> xmlbuilder(new XmlBuilder(model.data()));
    RequestState * state = model->getRequestState();

    // test login
    QFile fileLogin("./expected/login");
    if (!fileLogin.open(QIODevice::ReadOnly))
        return;
    testBlock = fileLogin.readAll();
    fileLogin.close();    
    state->setState(LOGIN);
    block = xmlbuilder->makeXml();
    QCOMPARE(block, testBlock);
    block.clear();
    testBlock.clear();
    // test listrequest
    QFile fileListRequest("./expected/listrequest");
    if (!fileListRequest.open(QIODevice::ReadOnly))
        return;
    testBlock = fileListRequest.readAll();
    fileListRequest.close();
    state->setState(LISTREQUEST);
    block = xmlbuilder->makeXml();    
    QCOMPARE(block, testBlock);
    block.clear();
    testBlock.clear();
    // test search without id without query string
    QFile fileSearch("./expected/search1");
    if (!fileSearch.open(QIODevice::ReadOnly))
        return;
    testBlock = fileSearch.readAll();
    fileSearch.close();

    state->setState(SEARCH);
    state->setCurrentFileListID(0);
    block = xmlbuilder->makeXml();
    QCOMPARE(block, testBlock);
    block.clear();
    testBlock.clear();
    // test search with list id without query string
    QFile fileSearch1("./expected/search2");
    if (!fileSearch1.open(QIODevice::ReadOnly))
        return;
    testBlock = fileSearch1.readAll();
    fileSearch1.close();

    state->setState(SEARCH);
    state->setCurrentFileListID(1);
    block = xmlbuilder->makeXml();
    QCOMPARE(block, testBlock);
    block.clear();
    testBlock.clear();
    // test search with list id with query string
    QFile fileSearch2("./expected/search3");
    if (!fileSearch2.open(QIODevice::ReadOnly))
        return;
    testBlock = fileSearch2.readAll();
    fileSearch2.close();

    state->setState(SEARCH);
    state->setCurrentName("txt");       // query string
    state->setCurrentFileListID(1);
    block = xmlbuilder->makeXml();
    QCOMPARE(block, testBlock);
    block.clear();
    testBlock.clear();
    // test search without list id with query string
    QFile fileSearch3("./expected/search4");
    if (!fileSearch3.open(QIODevice::ReadOnly))
        return;
    testBlock = fileSearch3.readAll();
    fileSearch3.close();

    state->setState(SEARCH);
    state->setCurrentName("txt");       // query string
    state->setCurrentFileListID(0);
    block = xmlbuilder->makeXml();
    QCOMPARE(block, testBlock);
    block.clear();
    testBlock.clear();
    // test new filelist
    QFile fileNew("./expected/new");
    if (!fileNew.open(QIODevice::ReadOnly))
        return;
    testBlock = fileNew.readAll();
    fileNew.close();

    state->setState(NEW);
    state->setFListName("abcde");
    block = xmlbuilder->makeXml();
    QCOMPARE(block, testBlock);
    block.clear();
    testBlock.clear();
    // test open filelist
    QFile fileOpen("./expected/open");
    if (!fileOpen.open(QIODevice::ReadOnly))
        return;
    testBlock = fileOpen.readAll();
    fileOpen.close();

    state->setState(OPEN);
    state->setCurrentFileListID(1);
    block = xmlbuilder->makeXml();
    QCOMPARE(block, testBlock);
    block.clear();
    testBlock.clear();
    // test delete filelist
    QFile fileDelFile("./expected/deleteFile");
    if (!fileDelFile.open(QIODevice::ReadOnly))
        return;
    testBlock = fileDelFile.readAll();
    fileDelFile.close();

    state->setState(DELETEFILE);
    state->setCurrentFileID(1);
    block = xmlbuilder->makeXml();
    QCOMPARE(block, testBlock);
    block.clear();
    testBlock.clear();
    // test download file
    QFile fileDownload("./expected/download");
    if (!fileDownload.open(QIODevice::ReadOnly))
        return;
    testBlock = fileDownload.readAll();
    fileDownload.close();

    state->setState(DOWNLOAD);
    state->setCurrentFileID(1);
    block = xmlbuilder->makeXml();
    QCOMPARE(block, testBlock);
    block.clear();
    testBlock.clear();
    // test upload file
    QFile fileUpload("./expected/upload");
    if (!fileUpload.open(QIODevice::ReadOnly))
        return;
    testBlock = fileUpload.readAll();
    fileUpload.close();

    state->setState(ADD);

    state->setCurrentSize(100);
    state->setCurrentName( "filename" );
    state->setCurrentFileListID(1);
    block = xmlbuilder->makeXml();
    QCOMPARE(block, testBlock);
    block.clear();
    testBlock.clear();
}

void Test::testXmlParser() {
    QSharedPointer<User> user(new User());
    QSharedPointer<MainModel> model(new MainModel(user.data()));
    QSharedPointer<Parser> parser(new Parser(model.data()));
    QSharedPointer<QIODevice> fileBad (new QFile ("./parser_sourse/bad"));
    if (!fileBad->open(QIODevice::ReadOnly))
        return;
    bool ind = false;

    // test login response
    RequestState * state = model->getRequestState();
    state->setState(LOGIN);
    QSharedPointer<QIODevice> fileLogin(new QFile ("./parser_sourse/login"));
    if (!fileLogin->open(QIODevice::ReadOnly))
        return;

    ind = parser->parse(fileLogin.data());
    fileLogin->close();
    QCOMPARE(ind, true);
    // bad response
    ind = parser->parse(fileBad.data());
    QCOMPARE(ind, false);
    //test all listslist response
    ind = false;
    state->setState(LISTREQUEST);
    QSharedPointer<QIODevice> fileListRequest
                        (new QFile ("./parser_sourse/listrequest"));
    if (!fileListRequest->open(QIODevice::ReadOnly))
        return;

    ind = parser->parse(fileListRequest.data());
    fileListRequest->close();
    QCOMPARE(ind, true);
    // bad response
    ind = parser->parse(fileBad.data());
    QCOMPARE(ind, false);
    //test new file response
    ind = false;
    state->setState(NEW);
    QSharedPointer<QIODevice> fileNew(new QFile ("./parser_sourse/new"));
    if (!fileNew->open(QIODevice::ReadOnly))
        return;

    ind = parser->parse(fileNew.data());
    fileNew->close();
    QCOMPARE(ind, true);
    // bad response
    ind = parser->parse(fileBad.data());
    QCOMPARE(ind, false);
    //test open filelist response
    ind = false;
    state->setState(OPEN);
    QSharedPointer<QIODevice> fileOpen(new QFile ("./parser_sourse/open"));
    if (!fileOpen->open(QIODevice::ReadOnly))
        return;
    ind = parser->parse(fileOpen.data());
    fileOpen->close();
    QCOMPARE(ind, true);
    // bad response
    ind = parser->parse(fileBad.data());
    QCOMPARE(ind, false);
    //test search response
    ind = false;
    state->setState(SEARCH);
    QSharedPointer<QIODevice> fileSearch (new QFile ("./parser_sourse/search"));
    if (!fileSearch->open(QIODevice::ReadOnly))
        return;
    ind = parser->parse(fileSearch.data());
    fileSearch->close();
    QCOMPARE(ind, true);
    // bad response
    ind = parser->parse(fileBad.data());
    QCOMPARE(ind, false);
    //test upload
    ind = false;
    state->setState(SEARCH);
    QSharedPointer<QIODevice> fileUpload (new QFile ("./parser_sourse/add"));
    if (!fileUpload->open(QIODevice::ReadOnly))
        return;
    ind = parser->parse(fileUpload.data());
    fileUpload->close();
    QCOMPARE(ind, true);
    // bad response
    ind = parser->parse(fileBad.data());
    QCOMPARE(ind, false);
    //test download
    ind = false;
    state->setState(SEARCH);
    QSharedPointer<QIODevice> fileDownload (new QFile ("./parser_sourse/download"));
    if (!fileDownload->open(QIODevice::ReadOnly))
        return;
    ind = parser->parse(fileDownload.data());
    fileDownload->close();
    QCOMPARE(ind, true);
    // bad response
    ind = parser->parse(fileBad.data());
    QCOMPARE(ind, false);
}

void Test::testFileDescription() {
    // tests file description
    FileDescription file;
    file.setFileList("filelist");
    file.setID(1);
    file.setName("fileName");
    file.setSize(3128);
    QCOMPARE(file.getFileList(), QString("filelist"));
    QCOMPARE(file.getID(), 1);
    QCOMPARE(file.getName(), QString("fileName"));
    QCOMPARE(file.getSize(), 3128);
}

void Test::testFileList() {
    // tests filelist
    FileList l;
    l.setID(1);
    l.setLoaded(false);
    l.setName("listName");
    QCOMPARE(l.getID(), 1);
    QCOMPARE(l.getIsSearch(), false);
    QCOMPARE(l.getLoaded(), false);
    QCOMPARE(l.getName(), QString("listName"));

    FileDescription file;
    file.setName("fileName");
    file.setID(1);
    l.addFile(file);
    l.addFile(file);
    QCOMPARE(l.size(), 1);    
    try {
        QCOMPARE(l.getFile(1).getID(), 1);
        QCOMPARE(l.getFile(3).getID(), 1);           // this file didn't add
    } catch (FileListException const& e) {
        QCOMPARE(e.message(), QString("FileList hasn't such file"));
    }
    try {
        l.deleteFile(1);            // in filelist
        l.deleteFile(2);           // this file didn't add
    } catch (FileListException const& e) {
        QCOMPARE(e.message(), QString("FileList hasn't such file"));
    }     
    
    QCOMPARE(l.size(), 0);
}

void Test::testMainModel() {
    QSharedPointer<User> user(new User());
    user->setSessionKey("3f3479149b4b55f26ffc66850065e3c0ebde150f");
    QSharedPointer<MainModel> model(new MainModel(user.data()));
    QCOMPARE(model->getUser()->getSessionKey(), user->getSessionKey());
    RequestState* state = model->getRequestState();

    FileList l;
    l.setID(1);
    l.setLoaded(false);
    l.setName("listName");
    model->addFileList(l);
    try {
        QCOMPARE(model->getFileList("listName").getID(), l.getID());
        QCOMPARE(model->getFileList("list").getID(), l.getID());
    } catch (MainModelException const& e) {
        QCOMPARE(e.message(), QString("Model hasn't such filelist"));
    }
    // tests simple functions in mainmodel
    state->setCurrentFileID(1);
    QCOMPARE(state->getCurrentFileID(), 1);
    state->setCurrentFileListID(2);
    QCOMPARE(state->getCurrentFileListID(), 2);
    state->setCurrentName("file");
    QCOMPARE(state->getCurrentName(), QString("file"));
    state->setCurrentSize(3128);
    QCOMPARE(state->getCurrentSize(), 3128);
    state->setError("ERROR");
    QCOMPARE(state->getError(), QString("ERROR"));
    state->setFListName("filelist");
    //model->setFListName("filelist");
    QCOMPARE(state->getFListName(), QString("filelist"));
    state->setPath("./downloads/testfile");
    QCOMPARE(state->getPath(), QString("./downloads/testfile"));
    state->setState(DOWNLOAD);
    QCOMPARE(state->getState(), DOWNLOAD);
    FileDescription file;
    file.setID(1);
    model->fillCopyBuffer(file);
    QCOMPARE(model->getCopyBuffer().getID(), file.getID());
}

void Test::testTcpConnector() {
    TcpConnector tcp;
    tcp.connect();
    QByteArray block("");
    bool b = tcp.write(block);
    QCOMPARE(b, false);
    block.append("DATA");
    b = tcp.write(block);
    QCOMPARE(b, true);
}

void Test::testConnector() {
    QSharedPointer<User> user(new User());
    user->setLogin("student");
    user->setPass("student");
    QSharedPointer<MainModel> mainModel(new MainModel(user.data()));
    RequestState * state = mainModel->getRequestState();
    state->setState(LOGIN);
    QSharedPointer<QIODevice> tcp(new QFile("./tests/login"));
    if (!tcp->open(QIODevice::WriteOnly | QIODevice::Text))
        return;
    QSharedPointer<Connector> connector(new Connector(mainModel.data(), tcp.data()));

    // TODO
//    connector->login("127.0.0.1", "8000");
//    connector->sendData();
}

#ifdef TESTS
QTEST_MAIN(Test)
#endif
