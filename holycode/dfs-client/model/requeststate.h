#ifndef REQUESTSTATE_H
#define REQUESTSTATE_H

#include <QString>
#include "state.h"

class RequestState {
    public:
        RequestState(): myErrorMessage("Unknown error!") {}
        //operations with current filelist
        QString getFListName() const { return myFListName; }
        void setFListName(QString name) { myFListName = name; }
        void setCurrentFileListID(int ID) { myCurrentFileListID = ID; }
        int getCurrentFileListID() const { return myCurrentFileListID; }

        //operations with current file
        QString getCurrentName() const { return myCurrentFileName; }
        void setCurrentName(QString name) { myCurrentFileName = name; }
        void setCurrentFileID(int ID) { myCurrentFileID = ID; }
        int getCurrentFileID() const { return myCurrentFileID; }
        void setCurrentSize(int size) { myCurrentFileSize = size; }
        int getCurrentSize() const { return myCurrentFileSize; }
        QString getPath() { return myFilePath; }
        void setPath(QString name) { myFilePath = name; }

        void setNumberOfCopies(int numb) { myNumberOfCopies = numb; }
        int getNumberOfCopies() const { return myNumberOfCopies; }

        //operations with current state of system
        void setState(State st) { myState = st; }
        State getState() const { return myState; }

        // error message from server
        QString getError() const { return myErrorMessage; }
        void setError(QString error) { myErrorMessage = error; }

        //QString getCutList() { return myCutList; }
        //void setCutList(QString list) { myCutList = list; }

    private:
        State myState;              //current state(login, newList, openList...)

        QString myFListName;        // current FileList
        int myCurrentFileListID;    // currentFileList ID
        QString myCurrentFileName;        //current file name
        int myCurrentFileID;
        int myCurrentFileSize;            // current file size
        QString myFilePath;               // full path to current file in filesistem
        QString myErrorMessage;             // error message from xml
        int myNumberOfCopies;
        //QString myCutList;
};
#endif // REQUESTSTATE_H
