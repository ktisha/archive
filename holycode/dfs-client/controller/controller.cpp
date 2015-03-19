#include "controller.h"
#include "../view/opendialog.h"
#include "../view/fail.h"

void Controller::connectSignals(LoginDialog* loginDialog, MainWindow* mainWind,
                   Connector* connector) {
    // login signals
    connect(loginDialog, SIGNAL(login(QString, QString)), connector,
                                    SLOT(login(QString, QString)));
    //connector signals
    connect(connector, SIGNAL(getFileLists()), loginDialog, SLOT(accept()));

    connect(connector, SIGNAL(createProg()), mainWind, SLOT(createProgress()));
    connect(connector, SIGNAL(endProg(int)), mainWind, SLOT(setProgressValue(int)));
    connect(connector, SIGNAL(progress(int)), mainWind, SLOT(setProgressValue(int)));

    connect(connector, SIGNAL(updateTable()), mainWind, SLOT(updateTable()));
    connect(this, SIGNAL(updateTable()), mainWind, SLOT(updateTable()));
    connect(connector, SIGNAL(showStatus(QString)), mainWind,
            SLOT(showStatus(QString)));

    // my signals
    connect(this, SIGNAL(sendData()), connector, SLOT(connectToServer()));

    //main window signals
    connect(mainWind->action_New, SIGNAL(triggered()), this,
            SLOT(newList()));
    connect(mainWind, SIGNAL(openClicked(QString)), this,
            SLOT(open(QString)));
    connect(mainWind->actionSearch, SIGNAL(triggered()), this,
            SLOT(search()));
    connect(mainWind, SIGNAL(toPdfClicked(const QString, const QString &)),
                        this, SLOT(toPdf(const QString, const QString &)));
    connect(mainWind, SIGNAL(removeClicked(QString)),
             this, SLOT(remove(QString)));
    connect(mainWind, SIGNAL(pasteClicked(QString)),
             this, SLOT(paste(QString)));
    connect(mainWind, SIGNAL(addClicked(QString, QStringList, int )), this,
            SLOT(addFile(QString, QStringList, int)));
    connect(mainWind, SIGNAL(downloadClicked(QString, int)),
              this, SLOT(download(QString, int)));

    connect(mainWind, SIGNAL(deleteClicked(QString,int)),
             this, SLOT(deleteFile(QString,int)));

    connect(connector, SIGNAL(fillModel()), this, SLOT(fillModel()));

}
// operations with filelist
void Controller::newList() {
    // show input form for new file list name
    InputDialog inputDialog("New filelist", "Name:");
    if(!inputDialog.exec())
        return;
    // actions while wrong input information
    if(inputDialog.getInput().isEmpty() ||
        (inputDialog.getInput() == "All lists")) {
        Fail fail("Creating filelist failed", " Please, enter another list name!");
        connect(fail.tryAgainButton, SIGNAL(clicked()), this, SLOT(newList()));
        fail.exec();
        return;
    }
    Logger::write("newList() : " + inputDialog.getInput());
    // set neccesary information to model
    myState->setState(NEW);
    myState->setFListName(inputDialog.getInput());
    emit sendData();
}

void Controller::open(QString name) {
    try {
        myState->setCurrentFileListID( myModel->getFileList(name).getID());
        Logger::write("MainWindow::open() : filelist name : " +
            name + " ID : " + QString::number((myModel->getFileList(name).getID())));
    } catch (MainModelException const& e) {
        Logger::write("MainWindow::open() : EXCEPTION : " + e.message());
    }
    myState->setState(OPEN);
    myState->setFListName(name);
    emit sendData();
}

void Controller::search() {
    // create input dialog for search
    InputDialog inputDialog("Search", "Query:", true);
    QListIterator<FileList> i = myModel->getIterator();
    while (i.hasNext()) {
        FileList file = i.next();
        if(!file.getIsSearch())
            inputDialog.comboBox->addItem(file.getName());
    }
    if(!inputDialog.exec())
        return;
    // set information to model
    QString list = inputDialog.comboBox->currentText();
    if(list == "All lists")
        myState->setCurrentFileListID(0);
    else {
        try {
            myState->setCurrentFileListID(myModel->getFileList(list).getID());
        } catch (MainModelException const& e) {
            Logger::write("Controller::search() : EXCEPTION : " + e.message());
        }
    }
    myState->setState(SEARCH);
    myState->setFListName("Search results for " + inputDialog.getInput());
    myState->setCurrentName(inputDialog.getInput());    // search query
    sendData();
}

void Controller::remove(QString name) {
    if(myModel->getFileList(name).size() != 0) {
        Fail fail("Remove failed", QString(" This filelist contains files.")
                                                + "Please, remove files firstly");
        fail.exec();
        return;
    }
    try {
        int id = myModel->getFileList(name).getID();
        myState->setCurrentFileListID(id);
        Logger::write("MainWindow::removeFileList() : filelist name : " +
                      name + " ID : " +
                      QString::number((myModel->getFileList
                        (name).getID())) );
    } catch (MainModelException const& e) {
        Logger::write("MainWindow::removeFileList() : EXCEPTION : " + e.message());
    }
    myState->setState(DELETEFLIST);
    myState->setFListName(name);
    emit sendData();
}
//operations with files
void Controller::paste(QString fileListName) {
    myState->setState(PASTE);
    myState->setFListName(fileListName);
    myState->setCurrentFileID(myModel->getCopyBuffer().getID());
    try {
        int id = myModel->getFileList(fileListName).getID();
        myState->setCurrentFileListID(id);
    } catch (MainModelException const& e) {
        Logger::write("MainWindow::paste() : EXCEPTION : " + e.message());
        qDebug() << e.message();
    }
    emit sendData();
}

void Controller::addFile(QString fileListName, QStringList fl, int numb) {
    // get neccesary information about file/filelist
    myIt = new QStringListIterator(fl);
    myListName = fileListName;
    myNumb = numb;
    fillModel();
}

void Controller::fillModel() {
    if (!myIt->hasNext()) {
        myState->setState(UPDATE);
        emit updateTable();
        return;
    }
    QString f = myIt->next();
    QFileInfo fileInfo(f);
    QString name = fileInfo.fileName();
    QFile file(f);
    if (file.size() == 0) {
        Fail fail("Add fails", "File size is null!");
        fail.exec();
        return;
    }
    // set neccesary information to model
    myState->setFListName(myListName);
    try {
       int id = myModel->getFileList(myListName).getID();
       myState->setCurrentFileListID(id);
    } catch (MainModelException const& e) {
        Logger::write("MainWindow::addFile() : EXCEPTION : " + e.message());
        qDebug() << e.message();
    }
    myState->setState(ADD);
    myState->setCurrentSize(file.size());
    myState->setNumberOfCopies(myNumb);
    myState->setCurrentName(name );
    myState->setPath(f);
    emit sendData();
}

void Controller::download(QString name, int id) {
    myState->setState(DOWNLOAD);
    // fill model
    try {
        myState->setCurrentFileID(id);
    } catch (FileListException const& e) {
        Logger::write("MainWindow::download() : EXCEPTION : " + e.message());
        qDebug() << e.message();
    }
    myState->setCurrentName( name );
    emit sendData();
}

void Controller::deleteFile(QString name, int id) {
    try {
        myState->setCurrentFileID(id);
    } catch (MainModelException const& e) {
        Logger::write("MainWindow::deleteFile() : EXCEPTION : " + e.message());
    } catch (FileListException const& e) {
        Logger::write("MainWindow::deleteFile() : EXCEPTION : " + e.message());
    }
    myState->setState(DELETEFILE);
    myState->setFListName(name);
    emit sendData();
}
