//class Utils

#ifndef _UTILS_H_
#define _UTILS_H_

#include "classfile.h"
#include "classdir.h"

#include <string>
#include <vector>

class myUtils
{
    private:
        myUtils();
        static std::vector<bool> visited;

		static size_t getNumber(std::string const& name, std::vector <FileRecord> const & files);
        static void dfs (int v, std::vector <FileRecord> const & files);

    public:
        static std::vector <std::string> result;

    public:
        static void readAll(Directory const& dir, std::vector <FileRecord> & files);
        static void topologicalSort (std::vector <FileRecord> const& files);
};


#endif
