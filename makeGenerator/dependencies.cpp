// classDependencies

#include "dependencies.h"

Dependencies::Dependencies(std::string const & name, std::vector <FileRecord> const & files, std::vector < Dependencies > const& allDependencies): myFileName(name)
{
    for ( size_t k = 0; k < files.size(); ++k )
    {
        if ( myFileName == files[k].getFileName())
        {
            for ( size_t i = 0; i != files[k].getCountInclude(); ++i )
            {
                myDependencies.push_back(files[k].getInclude(i));
            }
        }
    }
    
    for ( size_t j = 0; j != myDependencies.size(); ++j )
    {
        for (size_t i = 0; i < allDependencies.size(); ++i)
        {
            if(getDependencies(j) == allDependencies[i].getFileName())
            {
                for (size_t l = 0; l < allDependencies[i].myDependencies.size(); ++l)
                {
                    int count = 0;
                    for ( size_t m = 0; m < myDependencies.size(); ++m )
                    {
                        if ( allDependencies[i].myDependencies[l] == myDependencies[m] )
                            ++count; 
                    }
                    if ( count == 0 )
                        myDependencies.push_back(allDependencies[i].myDependencies[l]);
                }
            }
        }
    }
}


Dependencies::~Dependencies()
{
}

