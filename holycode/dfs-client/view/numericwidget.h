#ifndef NUMERICWIDGET_H
#define NUMERICWIDGET_H

#include <QWidget>
#include <QTableWidgetItem>

class NumericWidget: public QTableWidgetItem {
    public:
        NumericWidget(int v) : QTableWidgetItem(QString::number(v)), value(v) {
        }
        bool operator< ( const QTableWidgetItem & o ) const {
            const NumericWidget & other = static_cast<const NumericWidget &>(o);
            return value < other.value ? true : false;
        }
    private:
        int value;
};

#endif // NUMERICWIDGET_H
