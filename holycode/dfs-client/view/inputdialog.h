#ifndef INPUTDIALOG_H
#define INPUTDIALOG_H

#include"../ui_inputdialog.h"
#include<QComboBox>

namespace Ui{
    class Dialog;
}

// calss-widget do connection with user (input name for new file list, or input search request)
class InputDialog: public Ui::Dialog, public QDialog {
    public:
        InputDialog(QString name = 0, QString label = 0, bool isSearch = false,
                    QWidget *parent = 0): QDialog(parent) {
            setupUi(this);

            setWindowTitle(name);
            text->setText(label);
            if(!isSearch)               // for new file list
                comboBox->hide();
            else {                              // show comboBox for search
                comboBox->show();
                comboText->setText("File list");
                comboBox->addItem("All lists");
            }

        }
        ~InputDialog() {}

    public:
        QString getInput() {
            return lineEdit->text();
        }
};

#endif // INPUTDIALOG_H
