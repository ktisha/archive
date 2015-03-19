#include "logindialog.h"
#include "fail.h"

LoginDialog::LoginDialog(MainModel* loginModel, QWidget *parent): QDialog(parent), myModel(loginModel) {
    setupUi(this);      // setup form
    connect(loginButton, SIGNAL(clicked()), this, SLOT(fillModel()));

    myState = myModel->getRequestState();

    frame->hide();                      // hide preferences
    serverLine->setText("127.0.0.1");   // det default preferences
    portLine->setText("8000");

    setAttribute(Qt::WA_DeleteOnClose);
}

void LoginDialog::fillModel() {
    if((userLine->text() !=0) && (passLine->text() !=0 ) &&
       (portLine->text() !=0) && (serverLine->text() !=0 )) {
        myModel->getUser()->setLogin(userLine->text());
        myModel->getUser()->setPass(passLine->text());
        myState->setState(LOGIN);
        if(portLine->text().toInt() == 0) {
            Fail fail("Login failed", " Unavailable port");
            fail.exec();
            return;
        }
        emit login(serverLine->text(), portLine->text());
        return;
    } else {
                // if any input line is empty
        Fail fail("Login failed", " Enter login and password!");
        fail.exec();
        return;
    }
}
