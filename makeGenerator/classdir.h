// classDirectory

#ifndef _CLASSDIR_H_
#define _CLASSDIR_H_

#include <dirent.h>
#include <string>
#include <vector>

struct Directory
{
    private:
        std::string myNameDir;
        std::vector< std::string > myArray;
		bool isWrongDirectory;
    
    public:
        Directory(std::string const & nameDir);
        ~Directory();
    
        size_t size()                                const { return myArray.size(); }
        std::string const& getDirName()              const { return myNameDir; }
        std::string const& dirGetFileName(int index) const { return myArray[index]; }
		bool isWrong() const {return isWrongDirectory;}
    
    private:
        Directory &operator = (Directory &Other);
        Directory(Directory &Other); 
       
        static bool checkExt(std::string const& s, std::string const & ext); 
};

#endif
