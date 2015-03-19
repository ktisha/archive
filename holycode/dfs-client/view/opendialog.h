#ifndef OPENDIALOG_H
#define OPENDIALOG_H

#include"../ui_opendialog.h"

namespace Ui{
    class openDialog;
}
// widget for open file lists ( user can choose file list, he wants to open)
class OpenDialog: public Ui::openDialog, public QDialog {
    public:
        OpenDialog(QString s = "Open", QWidget *parent = 0): QDialog(parent) {
            setupUi(this);
            setWindowTitle(s);
        }
        ~OpenDialog() {}

    public:
        void addItem(QString const& name) {          // adds file list to list of available filelist
            list->addItem(new QListWidgetItem(name));
        }
        QString getActive() {               // returns name of chosen file list
            return list->currentItem()->text();
        }
};

#endif // OPENDIALOG_H
