#ifndef TESTS

#include "./controller/controller.h"
#include "../model/user.h"
#include "../model/main/mainmodel.h"

#include <QtGui/QApplication>
#include <QSharedPointer>

int main(int argc, char *argv[]) {
    QApplication a(argc, argv);
    QSharedPointer<User> user(new User());
    Logger::write("Created user for model");
    QSharedPointer<MainModel> mainModel(new MainModel(user.data()));
    Logger::write("Created main model");

    QSharedPointer<QIODevice> tcp(new TcpConnector);
    QSharedPointer<Connector> connector(new Connector(mainModel.data(), tcp.data()));
    Logger::write("Created connector");

    LoginDialog * loginDialog(new LoginDialog(mainModel.data()));
    Logger::write("Created Login dialog");
    MainWindow * mainWindow(new MainWindow(mainModel.data()));
    Logger::write("Created main window");

    QSharedPointer <Controller> controller(new Controller(mainModel.data()));
    controller->connectSignals(loginDialog, mainWindow, connector.data());
    Logger::write("Connected signals");
    
    if(loginDialog->exec()) {
        mainWindow->show();
        return a.exec();
    }
    return 0;
}
#endif
