#include "mainwindow.h"
#include "../exceptions/filelistexception.h"
#include "fail.h"
#include "logger/logger.h"
#include "numericwidget.h"
#include "confirmation.h"
#include "addfiledialog.h"

#include <QBuffer>
#include <QFileInfo>
#include <QProgressDialog>

QString MainWindow::ourConfigFilePath = QDir::currentPath() + QDir::separator()
                                        + "data/PNG/";

MainWindow::MainWindow(MainModel* model, QWidget *parent)
    : QMainWindow(parent), myModel(model), progress(0) {
    setupUi(this);
    setIcons();
    myState = myModel->getRequestState();
    myModel->setActiveSub(0);
    //updateMenus();
    setWindowTitle(tr("Distributed File Storage"));
    setAttribute(Qt::WA_DeleteOnClose);

    // connect signals inside this window
    connect(actionOpen, SIGNAL(triggered()), this, SLOT(open()));
    connect(action_Export_to_PDF, SIGNAL(triggered()), this, SLOT(toPdf()));
    connect(action_Remove, SIGNAL(triggered()), this, SLOT(remove()));

    connect(action_Cut, SIGNAL(triggered()), this, SLOT(cut()));
    connect(action_Paste, SIGNAL(triggered()), this, SLOT(paste()));

    connect(action_Add, SIGNAL(triggered ()), this, SLOT(addFile()));
    connect(actionDownload, SIGNAL(triggered ()), this, SLOT(download()));
    connect(action_Delete, SIGNAL(triggered()), this, SLOT(deleteFile()));
    connect(action_Exit, SIGNAL(triggered()), this, SLOT(close()));

    //connect(mdiArea, SIGNAL(subWindowActivated(QMdiSubWindow*)),
    //        this, SLOT(updateMenus()));
    actionDownload->setEnabled(true);
    action_Delete->setEnabled(true);
    action_Cut->setEnabled(true);
    action_Export_to_PDF->setEnabled(true);
    action_Add->setEnabled(true);
}

void MainWindow::updateTable() {
    Logger::write("MainWindow::updateTable()" + stateToStr(myState->getState()));
    // create/get active subwindow
    if (myState->getState() == PASTE) {
        updatePaste();
        return;
    }
    MdiChild* subwind = mdiArea->findChild<MdiChild*>(myState->getFListName());
    if(subwind == 0) {
        if(myState->getState() == UPDATE)
            subwind = activeMdiChild();
        else {
            subwind = createMdiChild();
            if (myState->getState() != SEARCH)
                subwind->newList(myState->getFListName(), 2);
            else
                subwind->newList(myState->getFListName(), 3);
        }
    }
    // fill table
    fillTable(subwind->getTable());
    subwind->showMaximized();
}
// operations with filelist
void MainWindow::open() {
    // show open dialog for choose list
    QSharedPointer<OpenDialog> openDialog = QSharedPointer<OpenDialog>
                                                        (new OpenDialog());
    fillOpenDialog(openDialog.data());
    if(!openDialog->exec())
        return;
    //fill model
    MdiChild* c = mdiArea->findChild<MdiChild*>(openDialog->getActive());
    if ( c != 0) {
        c->showMaximized();
        return;
    }
    myModel->setActiveSub(activeMdiChild());
    emit openClicked(openDialog->getActive());
}

void MainWindow::toPdf() {
    QString listName = activeMdiChild()->getName();
    QString fileName = QFileDialog::getSaveFileName(this,
                    tr("Export PDF"),QString(), "*.pdf");
    if (!fileName.isEmpty()) {
        if (QFileInfo(fileName).suffix().isEmpty())
                     fileName.append(".pdf");
        emit toPdfClicked(fileName, listName);
    }
    Ui::MainWindow::statusBar->showMessage("Pdf generated");
}

void MainWindow::remove() {
    QSharedPointer<OpenDialog> openDialog = QSharedPointer<OpenDialog>
                                                (new OpenDialog("Remove"));
    fillOpenDialog(openDialog.data());
    if(!openDialog->exec())
        return;
    if(mdiArea->findChild<MdiChild*>(openDialog->getActive()) != 0) {
        Fail fail("Remove failed", "Close this filelist firstly");
        fail.exec();
        return;
    }
    Confirmation c("Do you really want to delete filelist '" + openDialog->getActive()
                   + " ?");
    if(!c.exec())
        return;
    emit removeClicked(openDialog->getActive());
}
// operations with files
void MainWindow::paste() {
    if(myModel->getCopyBuffer().getID() == 0)
        return;
    QString fileListName = activeMdiChild()->getName();
    emit pasteClicked(fileListName);
}

void MainWindow::cut() {
    FileDescription file;
    int row = activeMdiChild()->getTable()->currentRow();
    QString name = activeMdiChild()->getTable()->item(row, 0)->text();
    int id = activeMdiChild()->getTable()->item(row, 2)->text().toInt();
    file.setName(name);
    file.setSize(activeMdiChild()->getTable()->item(row, 1)->text().toInt());
    file.setCopy(activeMdiChild()->getTable()->item(row, 3)->text().toInt());
    file.setFileList(activeMdiChild()->getName());
    try {
        file.setID(id);
    } catch (MainModelException const& e) {
        Logger::write("MainWindow::cut() : EXCEPTION : " + e.message());
        qDebug() << e.message();
    } catch (FileListException const& e) {
        Logger::write("MainWindow::cut() : EXCEPTION : " + e.message());
        qDebug() << e.message();
    }
    myModel->fillCopyBuffer(file);
}

void MainWindow::addFile() {
    // get neccesary information about file/filelist
    myModel->setActiveSub(activeMdiChild());
    AddFileDialog add;
    if(!add.exec())
        return;
    int numb = add.spinBox->text().toInt();
    QStringList f = add.getList();
    if (f.isEmpty()) {
        Fail fail("Add fails", "Please, choose file to upload");
        connect(fail.tryAgainButton, SIGNAL(clicked()), this, SLOT(addFile()));
        fail.exec();
        return;
    }
    emit addClicked(activeMdiChild()->getName(), f, numb);
}

void MainWindow::deleteFile() {
    //fill model
    int row = activeMdiChild()->getTable()->currentRow();
    QString name = activeMdiChild()->getName();
    int id = activeMdiChild()->getTable()->item(row, 2)->text().toInt();

    emit deleteClicked(name, id);
}

void MainWindow::download() {
    myModel->setActiveSub(activeMdiChild());
    int row = activeMdiChild()->getTable()->currentRow();
    QString name = activeMdiChild()->getTable()->item(row, 0)->text();
    int id = activeMdiChild()->getTable()->item(row, 2)->text().toInt();

    QString filePath = QFileDialog::getSaveFileName(this,
                    tr("Save file"), name);
    if(filePath == 0)
        return;
    myState->setPath(filePath);
    emit downloadClicked(name, id);
}

MdiChild *MainWindow::createMdiChild() {
    MdiChild *child = new MdiChild;
    mdiArea->addSubWindow(child);
    child->showMaximized();
    return child;
}

MdiChild *MainWindow::activeMdiChild() {
    if (QMdiSubWindow *activeSubWindow = mdiArea->activeSubWindow()) {
        return qobject_cast<MdiChild *>(activeSubWindow->widget());
    }
    return myModel->getActiveSub();
}
// actions with progress dialog( in download/upload)
void MainWindow::createProgress() {
    if(myState->getState() == GETFILE)
        progress = QSharedPointer<QProgressDialog>(
                new QProgressDialog("Download file...", 0, 0, 100, this));
    else
        progress = QSharedPointer<QProgressDialog>(
                new QProgressDialog("Upload file...", 0, 0, 100, this));
    progress->setWindowModality(Qt::WindowModal);
    progress->show();
}

void MainWindow::setProgressValue(int i) {
    static int counter = 0;
    if(i <= 100) {
        for(int j = counter + 1; j <= i; ++j )
            progress->setValue(j);
        counter = i;
    } else {
       for(int j = counter + 1; j <= 100; ++j )
            progress->setValue(j);
        counter = 0;
        progress->hide();
    }
}

void MainWindow::updateMenus( ) {
    Logger::write("MainWindow::updateMenus() ");
    actionDownload->setDisabled(true);
    action_Add->setDisabled(true);
    action_Delete->setDisabled(true);
    action_Cut->setDisabled(true);
    action_Paste->setDisabled(true);
    action_Export_to_PDF->setDisabled(true);
    if ( mdiArea->subWindowList().size() == 0 ) {
        return;
    }
    if(mdiArea->activeSubWindow() == 0)
        return;
    FileList list;
    try {
        list = myModel->getFileList(activeMdiChild()->getName());
    } catch (MainModelException const& e) {
        Logger::write("MainWindow::updateMenus() : EXCEPTION : " + e.message());
    }
    if(list.getIsSearch()) {
        if(list.size() != 0 ) {
            actionDownload->setEnabled(true);
            action_Export_to_PDF->setEnabled(true);
        }
    } else {
        action_Add->setEnabled(true);
        action_Paste->setEnabled(true);
        if(list.size() == 0 )
            return;
        else {
            actionDownload->setEnabled(true);
            action_Delete->setEnabled(true);
            action_Cut->setEnabled(true);
            action_Export_to_PDF->setEnabled(true);
        }
    }
}

void MainWindow::updatePaste() {
    Logger::write("MainWindow::updatePaste()");
    MdiChild* subwind = activeMdiChild();
    // fill table
    fillTable(subwind->getTable());
    subwind->show();
    myState->setFListName(myModel->getCopyBuffer().getFileList());
    QString a = myModel->getCopyBuffer().getFileList();
    subwind = mdiArea->findChild<MdiChild*>(a);
    if(subwind == 0)
        return;
    // fill table
    fillTable(subwind->getTable());
    subwind->show();
    myModel->getCopyBuffer().setID(0);
    //myModel->getCopyBuffer().setFileList(activeMdiChild()->getName());
}

// set icons from ourConfigFilePath
void MainWindow::setIcons() {
    QIcon icon;
    icon.addPixmap(QPixmap(QString::fromUtf8((ourConfigFilePath +"open_24.png").
                            toStdString().c_str())), QIcon::Normal, QIcon::Off);
    actionOpen->setIcon(icon);
    icon.addPixmap(QPixmap(QString::fromUtf8((ourConfigFilePath + "cut_24.png").
                            toStdString().c_str())), QIcon::Normal, QIcon::Off);
    action_Cut->setIcon(icon);
    icon.addPixmap(QPixmap(QString::fromUtf8((ourConfigFilePath + "copy_24.png").
                            toStdString().c_str())), QIcon::Normal, QIcon::Off);
    action_Copy->setIcon(icon);
    icon.addPixmap(QPixmap(QString::fromUtf8((ourConfigFilePath +"paste_24.png").
                            toStdString().c_str())), QIcon::Normal, QIcon::Off);
    action_Paste->setIcon(icon);
    icon.addPixmap(QPixmap(QString::fromUtf8((ourConfigFilePath + "docs_24.png").
                            toStdString().c_str())), QIcon::Normal, QIcon::Off);
    action_New->setIcon(icon);
    icon.addPixmap(QPixmap(QString::fromUtf8((ourConfigFilePath + "srch_24.png").
                            toStdString().c_str())), QIcon::Normal, QIcon::Off);
    actionSearch->setIcon(icon);
    icon.addPixmap(QPixmap(QString::fromUtf8((ourConfigFilePath +"image_24.png").
                            toStdString().c_str())), QIcon::Normal, QIcon::Off);
    action_Export_to_PDF->setIcon(icon);
    icon.addPixmap(QPixmap(QString::fromUtf8((ourConfigFilePath + "add_24.png").
                            toStdString().c_str())), QIcon::Normal, QIcon::Off);
    action_Add->setIcon(icon);
    icon.addPixmap(QPixmap(QString::fromUtf8((ourConfigFilePath + "down_24.png").
                            toStdString().c_str())), QIcon::Normal, QIcon::Off);
    actionDownload->setIcon(icon);
    icon.addPixmap(QPixmap(QString::fromUtf8((ourConfigFilePath +"cancl_24.png").
                            toStdString().c_str())), QIcon::Normal, QIcon::Off);
    action_Delete->setIcon(icon);
}

void MainWindow::showStatus(QString str) {
    Ui::MainWindow::statusBar->showMessage(str);
}

void MainWindow::fillOpenDialog(OpenDialog *o) {
    Logger::write("MainWindow::fillOpenDialog");
    QListIterator<FileList> i = myModel->getIterator();
    while (i.hasNext()) {
        FileList file = i.next();
        if(!file.getIsSearch())
            o->addItem(file.getName());
    }
    if(myModel->size() == 0) {
        o->okButton->setDisabled(true);
    }
    connect(o->list, SIGNAL(doubleClicked(QModelIndex)),
                    o, SLOT(accept()));
}

void MainWindow::fillTable(QTableWidget *table) {
    Logger::write("MainWindow::fillTable");
    table->clearContents();
    table->setSortingEnabled(false);
    FileList list;
    try {
        qDebug() << "tipa";
        list = myModel->getFileList(myState->getFListName());
        qDebug() << "tipa1";
    } catch (MainModelException const& e) {
        qDebug() << "tipa2";
        Logger::write("MainWindow::updateTable() : EXCEPTION : " + e.message());
        qDebug() << e.message();
    }
    table->setRowCount(list.size());
    int row = 0;
    QMapIterator<int, FileDescription> i(list.getList());
    while (i.hasNext()) {
        i.next();
        FileDescription file = i.value();
        QTableWidgetItem* name = new QTableWidgetItem(file.getName());
        name->setFlags(Qt::ItemIsEnabled | Qt::ItemIsSelectable);
        table->setItem(row, 0, name);
        NumericWidget* size = new NumericWidget(file.getSize());
        size->setFlags(Qt::ItemIsEnabled | Qt::ItemIsSelectable);
        table->setItem(row, 1, size);
        NumericWidget* id = new NumericWidget(file.getID());
        id->setFlags(Qt::ItemIsEnabled | Qt::ItemIsSelectable);
        table->setItem(row, 2, id);
        if(myState->getState() == SEARCH) {
            table->setItem(row, 3, new QTableWidgetItem(file.getFileList()));
            NumericWidget* id = new NumericWidget(file.getCopy());
            id->setFlags(Qt::ItemIsEnabled | Qt::ItemIsSelectable);
            table->setItem(row, 4, id);
        } else {
            NumericWidget* copy = new NumericWidget(file.getCopy());
            copy->setFlags(Qt::ItemIsEnabled | Qt::ItemIsSelectable);
            table->setItem(row, 3, copy);
        }
        ++row;
     }
    table->setSortingEnabled(true);
    if(list.size() != 0)
        table->selectRow(row-1);
    //updateMenus();
}
