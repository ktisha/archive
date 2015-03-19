//class FileRecord

#ifndef _CLASSFILE_H_
#define _CLASSFILE_H_

#include "classdir.h"

#include <cstdio>
#include <string>
#include <vector>

struct FileRecord
{
    private:    
        std::string myFileName;
        std::vector< std::string > myIncludes;    
   
    public:   
		FileRecord(std::string const& fileName, Directory const& dir);
        ~FileRecord();
    
        std::string const& getFileName()         const { return myFileName; }
        std::string const& getInclude(int index) const { return myIncludes[index]; }
        size_t             getCountInclude()     const { return myIncludes.size(); }

    private:
//        FileRecord &operator = (FileRecord const& Other);
//        FileRecord(FileRecord const& Other);   
};

#endif
