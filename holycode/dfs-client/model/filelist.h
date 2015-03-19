#ifndef FILELIST_H
#define FILELIST_H

#include"filedescription.h"
#include "../exceptions/filelistexception.h"

#include <QMap>

class FileDescription;

// class-model reflect our knowledge about file lists in server
class FileList{
    public:
        FileList(): isSearch(false) {}       //all lists are not search list by default

        void setName(QString name) { myName = name; }
		const QString & getName() const { return myName; }

        void setID(int ID) { myID = ID; }
        int getID() const { return myID; }

        void setLoaded(bool b) { isLoaded = b; }        // file list can be not loaded means we know only
        bool getLoaded() const { return isLoaded; }           // its  name and don't know its files

        void setIsSearch(bool b) { isSearch = b; }      // list can be search result list means it can't be
        bool getIsSearch() const { return isSearch; }         // changed

        //operations with files
        const QMap <int, FileDescription> & getList() { return myFiles; }
        QListIterator<FileDescription> getIterator() const {
                return QListIterator<FileDescription>(myFiles.values());
        }
        void addFile(const FileDescription & file) { myFiles.insert(file.getID(), file); }
        FileDescription getFile(int id) throw (FileListException);
        void deleteFile(int id) throw (FileListException);
        int size() const { return myFiles.size(); }

    private:
        QString myName;
        QMap <int, FileDescription> myFiles;
        int myID;
        bool isLoaded;
        bool isSearch;
};

#endif // FILELIST_H
