#ifndef FILEDESCRIPTION_H
#define FILEDESCRIPTION_H

#include"filelist.h"

#include <QString>

class FileList;

// class-model reflect our knowledge about file in file list
class FileDescription {
    public:
        FileDescription() {}

        void setName(QString name) { myName = name; }
		const QString & getName() const { return myName; }

        void setSize(int size) { mySize = size; }
		int getSize() const { return mySize; }

        void setID(int ID) { myID = ID; }
        int getID() const { return myID; }

        void setFileList(QString fileList) { myFileList = fileList; }
		const QString & getFileList() const { return myFileList; }

        void setCopy(int copy) { myCopy = copy; }
		int getCopy() const { return myCopy; }

    private:
        QString myName;
        int mySize;
        int myID;
        QString myFileList;     // file list contains this file
        int myCopy;
};


#endif // FILEDESCRIPTION_H
