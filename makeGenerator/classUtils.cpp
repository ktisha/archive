//class Utils
#include <iostream>
#include "classUtils.h"

std::vector <bool> myUtils::visited;
std::vector <std::string> myUtils::result; 

void myUtils::readAll(Directory const& dir, std::vector <FileRecord> & files)
{
    for ( size_t i = 0 ; i != dir.size(); ++i )
    {
        FileRecord b(dir.dirGetFileName(i), dir); 
        files.push_back(b); 
    }
}

size_t myUtils::getNumber(std::string const& name, std::vector <FileRecord> const & files)
{
    for (size_t i = 0; i != files.size(); ++i)
    {
        if (files[i].getFileName() == name)
            return i;
    }
	return -1;
}

void myUtils::dfs (int v, std::vector <FileRecord> const & files)
{
	visited[v] = true;
		
	for (size_t i = 0; i < files[v].getCountInclude(); ++i)
		{
            size_t j = getNumber(files[v].getInclude(i), files);
			if (j == -1)
				{
					std::cout << "There is no file " << files[v].getInclude(i) << "\n";
					break;
				}
			if (!visited[j])
				dfs (j, files);
        }
		result.push_back (files[v].getFileName());
}

void myUtils::topologicalSort (std::vector <FileRecord> const& files)
{
	visited.assign (files.size(), false);
	for (size_t i = 0; i < files.size(); ++i)
		if ( !visited[i] )
			dfs (i, files);
}

