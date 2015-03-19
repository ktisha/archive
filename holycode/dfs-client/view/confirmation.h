#ifndef CONFIRMATION_H
#define CONFIRMATION_H
#include "../ui_confirmation.h"
#include <QFileDialog>

namespace Ui {
    class Confirmation;
}

// class-widget for adding file with number of copies
class Confirmation : public QDialog, public Ui::Confirmation {
    Q_OBJECT

    public:
        Confirmation(QString str, QWidget *parent = 0 ): QDialog(parent) {
            setupUi(this);
            setFixedSize(size());
            textBrowser->setText(str);
        }
        ~Confirmation() {}

};
#endif // CONFIRMATION_H
