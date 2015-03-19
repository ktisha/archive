// classDirectory

#include "classdir.h"

#include <cstdlib>
#include <string>
#include <dirent.h>
#include <iostream>

Directory::Directory(std::string const & nameDir) : myNameDir(nameDir) 
{
    if ( DIR * myWorkDir = opendir(myNameDir.c_str()) )
    {
        while ( dirent * myStrDir = readdir (myWorkDir) ) 
        {
            if ( checkExt(myStrDir->d_name, ".cpp") )
                myArray.push_back(myStrDir->d_name);
            if ( checkExt(myStrDir->d_name, ".h") )
                myArray.push_back(myStrDir->d_name);
        }
        closedir (myWorkDir);
		isWrongDirectory = false;
    }
	else
	{
		std::cout << "Wrong name of directory\n";
		isWrongDirectory = true;
	}
}

Directory::~Directory()
{
}

bool Directory::checkExt(std::string const& name, std::string const & ext)
{
    int n = ext.size();
    return name.size() > 4 && name.substr(name.size() - n) == ext;
}
