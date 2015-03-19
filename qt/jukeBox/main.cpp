#include <QApplication>
#include <QDialog>

#include "jukeBox.h"

int main(int argc, char * argv[]) {
    QApplication app(argc, argv);
    JukeBox * j = new JukeBox();    
    return app.exec();
}
