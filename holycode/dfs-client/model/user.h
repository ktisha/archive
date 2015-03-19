#ifndef USER_H
#define USER_H

#include <QStringList>
#include <QString>
#include <QDebug>

// clas-model reflected our knowledge about user
class User{
// information from login dialog
    public:
        User() {}
        ~User() {qDebug() << "~User()";}

        void setSessionKey(QString key) { mySessionKey = key;}
        void setLogin(QString login) { myLogin = login;}
        void setPass(QString pass) { myPassword = pass;}

        QString getLogin() { return myLogin;}
        QString getPass() { return myPassword;}
        QString getSessionKey() { return mySessionKey;}

    private:
        QString myLogin;
        QString myPassword;
        QString mySessionKey;

};

#endif // USER_H
