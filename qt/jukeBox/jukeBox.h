#ifndef JUKEBOX_H
#define JUKEBOX_H

#include <QDialog>
#include <QTextStream>
#include <QFile>
#include <QtXml>
#include "ui_jukeBox.h"

class JukeBox: public QDialog, public Ui::JukeBox {
    Q_OBJECT
    
    public:
        JukeBox();
      
    private slots:
        void openClicked();    
        void saveClicked();
        
    private:
        void loadFile(const QString & str);        
        void analyse(const QDomNode& node);
        void entryToDom(QDomDocument& doc);
        void saveFile(const QString & str);    
};

#endif
