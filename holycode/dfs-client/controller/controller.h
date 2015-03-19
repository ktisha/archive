#ifndef CONTROLLER_H
#define CONTROLLER_H

#include "../view/logindialog.h"
#include "../view/mainwindow.h"
#include "../protocol/connector.h"
#include "pdfGenerator/pdfGenerator.h"
#include "filelist.h"

#include<QStringList>

// class do connection between protocol and view
class Controller: public QObject {
    Q_OBJECT
    public:
        void connectSignals(LoginDialog* loginDialog, MainWindow* mainWind,
                   Connector* connector);
    public slots:
        void open(QString name);
        void download(QString name, int id);
        void search();
        void newList();
        void addFile(QString fileListName, QStringList f, int numb);
        void paste(QString fileListName);
        void deleteFile(QString name, int id);
        void remove(QString);
        QString toPdf(const QString fileName, const QString & listName) {
                return pdfGenerate(fileName, myModel->getFileList(listName));
        }
        void fillModel();
    signals:
        void sendData();
        void updateTable();

    public:
        Controller(MainModel * model): myModel(model) {
            myState = myModel->getRequestState();
        }
    private:
        MainModel * myModel;
        RequestState * myState;
        QStringListIterator* myIt;
        QString myListName;
        int myNumb;
};

#endif // CONTROLLER_H
