#ifndef ADDFILEDIALOG_H
#define ADDFILEDIALOG_H

#include "../ui_addfiledialog.h"
#include <QFileDialog>
#include <QStringList>

namespace Ui {
    class AddFileDialog;
}

// class-widget for adding file with number of copies
class AddFileDialog : public QDialog, public Ui::AddFileDialog {
    Q_OBJECT

    private slots:
        void addShow() {
            QStringList f = QFileDialog::getOpenFileNames(this, "Add File");
            if( f.isEmpty())
                return;
            list = f;
            lineEdit->setText(f.join(" "));
        }
    public:
        AddFileDialog(QWidget *parent = 0): QDialog(parent) {
            setupUi(this);
            setFixedSize(size());
            connect(browse, SIGNAL(clicked()), this, SLOT(addShow()));
        }
        ~AddFileDialog() {}

    private:
        QStringList list;

    public:
        QStringList getList() {
            return list;
        }
};

#endif // ADDFILEDIALOG_H
