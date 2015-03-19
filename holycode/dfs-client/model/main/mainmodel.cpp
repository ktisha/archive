#include "mainmodel.h"

FileList& MainModel::getFileList(QString name) {
    if(myFileLists.find(name) == myFileLists.end()) {
        MainModelException e;
        e.raise();
    }
    return myFileLists.find(name).value();
}
