#ifndef FAILLOGIN_H
#define FAILLOGIN_H

#include "../ui_fail.h"

namespace Ui {
    class Fail;
}

// class-widget for reporting about bugs (mostly in server response)
class Fail : public QDialog, public Ui::Fail {
    Q_OBJECT

    public:
        Fail(QString title, QString mess, QWidget *parent = 0): QDialog(parent) {
            setupUi(this);
            setFixedSize(size());
            setWindowTitle(title);
            label->setText(mess);
        }
        ~Fail() {}

};

#endif // FAILLOGIN_H
