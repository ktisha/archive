#ifndef MAINMODEL_H
#define MAINMODEL_H

#include "../user.h"
#include "../filelist.h"
#include "../requeststate.h"

#include <QStringList>
#include <QString>
#include <QMap>
#include "../view/mdichild.h"
#include "state.h"
#include "../../exceptions/mainmodelexception.h"

// main model in our program
class MainModel {
    public:
        MainModel(User* user): myUser(user) {
            myRequestState = new RequestState;
            myCopyBuffer.setID(0);
        }

        ~MainModel() { delete myRequestState; }

    public:
        User* getUser() { return myUser; }

        //operations with filelists
        void addFileList(FileList file) { myFileLists.insert(file.getName(), file);}

        void setActiveSub(MdiChild* sub) { myActiveWind = sub; }        //hasn't test
        MdiChild* getActiveSub() { return myActiveWind; }
        // operations with copy buffer
        void fillCopyBuffer(FileDescription f) { myCopyBuffer = f;}
        FileDescription& getCopyBuffer() { return myCopyBuffer; }
        RequestState* getRequestState() { return myRequestState; }

        QListIterator<FileList> getIterator() const {
                return QListIterator<FileList>(myFileLists.values());
        }

        FileList& getFileList(QString name);
        void clear() { myFileLists.clear(); }

        int size() const { return myFileLists.size(); }

    private:
        User* myUser;       // current user
        RequestState* myRequestState;           // contains all information about state
        QMap <QString, FileList> myFileLists;       // List of FileLists

        MdiChild * myActiveWind;            // active subwindow

        FileDescription myCopyBuffer;       // buffer for copy files
};

#endif // MAINMODEL_H
