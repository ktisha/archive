//class FileRecord

#include "classfile.h"

#include <string>

FileRecord::FileRecord(std::string const& fileName, Directory const& dir): myFileName(fileName)
{
	std::string name = dir.getDirName() + myFileName;
    FILE * myFile = fopen(name.c_str(), "rt");
    if ( myFile != NULL )
    {
        char buff[1024] = {};
        while( fgets(buff, 1024, myFile) != NULL ) 
        {
            int i = 0;
            while( isspace(buff[i]) && buff[i]!= '\0' )
                ++i;
                
            if (buff[i] == '#')
            {
                ++i;
        
                while( isspace(buff[i]) && buff[i]!= '\0' )
                    ++i;

                std::string inc(&buff[i], 7);    
                if( inc == "include" )
                {
                    i += 7;
                    while( isspace(buff[i])&& buff[i]!= '\0' )
                        ++i;

                    if (buff[i] == '"')
                    {
                        int j = ++i;

                        while( buff[i] != '"' && buff[i] != '\0' )
                            ++i;

                        if ( buff[i] == '"' )
                        {
                            myIncludes.push_back(std::string(&buff[j], i - j));
                        }
                    }
                }
            }   
        }
        fclose(myFile);
    }
}

FileRecord::~FileRecord()
{
}
