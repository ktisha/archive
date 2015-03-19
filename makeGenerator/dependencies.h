// classDependencies

#ifndef _DEPENDENCIES_H_
#define _DEPENDENCIES_H_

#include "classfile.h"
#include "classdir.h"
#include "classUtils.h"

#include <string>
#include <vector>

struct Dependencies
{
  private:    
        std::string myFileName;        
        std::vector< std::string > myDependencies;    
        
    public:   
        Dependencies(std::string const& name, std::vector <FileRecord> const & files, std::vector < Dependencies > const& allDependencies);
        ~Dependencies();
  
        std::string const& getFileName()              const { return myFileName; }
        std::string const& getDependencies(int index) const { return myDependencies[index]; }
        size_t             getCountDependencies()     const { return myDependencies.size(); }

    private:
//        Dependencies &operator = (Dependencies const& Other);
//        Dependencies(Dependencies const& Other);   
};


#endif
