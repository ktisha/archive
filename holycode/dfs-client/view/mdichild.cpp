#include <QtGui>

#include "mdichild.h"

MdiChild::MdiChild() {
    setAttribute(Qt::WA_DeleteOnClose);
}

void MdiChild::newList(QString const& name, int count) {
    myName = name;
    setObjectName(name);
    QVBoxLayout* verticalLayout_3 = new QVBoxLayout(this);
    verticalLayout_3->setSpacing(6);
    verticalLayout_3->setMargin(11);
    verticalLayout_3->setObjectName(QString::fromUtf8("verticalLayout_3"));
    table = new QTableWidget(this);
    table->setColumnCount(count + 2);
    QTableWidgetItem *__colItem = new QTableWidgetItem();
    table->setHorizontalHeaderItem(0, __colItem);
    QTableWidgetItem *__colItem1 = new QTableWidgetItem();
    table->setHorizontalHeaderItem(1, __colItem1);
    QTableWidgetItem *__colItem2 = new QTableWidgetItem();
    table->setHorizontalHeaderItem(2, __colItem2);
    QTableWidgetItem *__colItem3 = new QTableWidgetItem();
    table->setHorizontalHeaderItem(3, __colItem3);
    QTableWidgetItem *__colItem4 = new QTableWidgetItem();
    table->setHorizontalHeaderItem(4, __colItem4);

    table->setObjectName(QString::fromUtf8("table"));
    table->setShowGrid(false);
    table->setSortingEnabled(true);
    table->horizontalHeaderItem(0)->setText(QApplication::translate("MainWindow", "file", 0, QApplication::UnicodeUTF8));
    table->horizontalHeaderItem(1)->setText(QApplication::translate("MainWindow", "size", 0, QApplication::UnicodeUTF8));
    table->horizontalHeaderItem(2)->setText(QApplication::translate("MainWindow", "file ID", 0, QApplication::UnicodeUTF8));
    table->setColumnHidden(2, true);
    if(count >= 3) {
        table->horizontalHeaderItem(3)->setText(QApplication::translate("MainWindow", "filelist", 0, QApplication::UnicodeUTF8));
        table->horizontalHeaderItem(4)->setText(QApplication::translate("MainWindow", "number of copies", 0, QApplication::UnicodeUTF8));
    } else {
        table->horizontalHeaderItem(3)->setText(QApplication::translate("MainWindow", "number of copies", 0, QApplication::UnicodeUTF8));
    }
    table->setSelectionBehavior(QAbstractItemView::SelectRows);
    table->setSelectionMode(QAbstractItemView::SingleSelection);

    verticalLayout_3->addWidget(table);

    setWindowTitle(name);
    table->horizontalHeader()->setStretchLastSection(true);
}
