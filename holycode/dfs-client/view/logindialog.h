#ifndef LOGINDIALOG_H
#define LOGINDIALOG_H

#include "../ui_logindialog.h"
#include "../model/main/mainmodel.h"

#include <QtGui>

#include <iostream>

namespace Ui {
    class LoginDialog;
}
// login widget ( contains server preferences)
// know about model
class LoginDialog: public QDialog, public Ui::LoginDialog {
    Q_OBJECT

    public:
        LoginDialog(MainModel* loginModel, QWidget *parent = 0);
        ~LoginDialog() { std::cerr << "~LoginDialog()\n"; }

    private:
        MainModel* myModel;
        RequestState* myState;

    private slots:
        void fillModel();

    signals:
        void login(QString, QString);       // emit for send request
};

#endif // LOGINDIALOG_H
