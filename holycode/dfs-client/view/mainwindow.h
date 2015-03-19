#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include "../ui_mainwindow.h"
#include "../protocol/xmlParser/loginhandler.h"
#include "../model/main/mainmodel.h"
#include "inputdialog.h"
#include "opendialog.h"

#include "mdichild.h"

#include <QtGui>

namespace Ui {
    class MainWindow;
}

// class main widget in our programm
// knows about main model
// input, open, pregress dialogs - its children
class MainWindow : public QMainWindow, public Ui::MainWindow {
    Q_OBJECT

    public:
        MainWindow(MainModel* model, QWidget *parent = 0);
        ~MainWindow() { qDebug() << "~MainWindow()"; }

    public slots:
        void updateTable();
        void showStatus(QString);

    signals:                    // list of signals can be emitted
        void openClicked(QString name);
        void downloadClicked(QString name, int id);
        void addClicked(QString fileListName, QStringList, int);
        void pasteClicked(QString fileListName);
        void deleteClicked(QString name, int id);
        void removeClicked(QString name);

        void toPdfClicked(const QString &, const QString &);

    private slots:                  // slots to connect signals inside this window
        void addFile();             // fill model fields for upload file
        void createProgress();      // create progress dialog
        void setProgressValue(int i);   // continue progress
        void download();            // fill model for download file
        void deleteFile();          // fill model for delete file
        void paste();               // paste from copybuffer
        void cut();
        void toPdf();
        void updateMenus();
        void open();
        void updatePaste();
        void remove();

    private:
        void setIcons();            // set good appearance
        MdiChild *activeMdiChild();     // return active subwindow
        MdiChild *createMdiChild();     // create new subwindow
        void fillOpenDialog(OpenDialog * o);
        void fillTable(QTableWidget* t);

    private:
        MainModel* myModel;
        RequestState* myState;
        QSharedPointer<QProgressDialog> progress;

        static QString ourConfigFilePath;   // path to pictures
};

#endif // MAINWINDOW_H
