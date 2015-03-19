#ifndef MDICHILD_H
#define MDICHILD_H

#include <QTableWidget>

// class-widget for subwindow in main window
// reflect our model
class MdiChild : public QWidget {
    Q_OBJECT
    public:
        MdiChild();

        void newList(QString const& name, int count);
        QTableWidget* getTable() { return table;}
        QString getName() { return myName;}

    private:
        QString myName;             // all subwindow has its own name
        QTableWidget* table;        // contains table-widget
};

#endif // MDICHILD_H
