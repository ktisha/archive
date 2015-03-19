#include "filelist.h"

FileDescription FileList::getFile(int id) throw (FileListException) {
    if (!myFiles.contains(id)) {
        FileListException e;
        e.raise();
    }
    return myFiles.value(id);
}

void FileList::deleteFile(int id) throw (FileListException) {
    QMap<int, FileDescription>::iterator i = myFiles.find(id);
    if(i == myFiles.end()) {
        FileListException e;
        e.raise();
    }
    myFiles.erase(i);
}
