// main

#include "classfile.h"
#include "classdir.h"
#include "dependencies.h"
#include "classUtils.h"

#include <iostream>

int main(int argc, char * argv[])
{
    std::vector < std::string > object;
    std::vector < Dependencies > allDependencies;
	std::vector < FileRecord > files;

	char flags[32] = {};
	std::string direct = "./";
	int k = 0;	

	for(int j = 1; j < argc; ++j)
	{
		int i = 0;
		if(argv[j][i] == '-')
		{
			++i;
			if (argv[j][i] == 'h')
			{
				FILE * help = fopen("help","rt");
				if ( help != NULL )
				{
					char buff[1024] = {};
					while( fgets(buff, 1024, help) != NULL ) 
					{
						std::cout << buff; 
					}
				}
				std::cout << "\n";
				fclose(help);
				return 0;
			}
			
			if (argv[j][i] == 'f')
			{
				++i;
				int l = 2;
				while (argv[j][l] != '\0')
				{
					flags[k] = argv[j][l];
					++k;
					++l;
				}
				flags[k] = ' ';
				++k;
			}
		}
		else
		{
			direct = argv[j];
		}	
	}

    const Directory a(direct);
	if (a.isWrong())
		return 0;
    
	myUtils::readAll(a, files);
	myUtils::topologicalSort (files);
	
    int count = 0;

	direct = direct + "Makefile";

    remove(direct.c_str());
    FILE * makeFile = fopen(direct.c_str(),"wt");
	
	fputs("CFLAGS=", makeFile);
	fputs(flags, makeFile);
	fputs("\n\nall: project\n\n", makeFile);

    for ( size_t i = 0 ; i < files.size(); ++i )
    {
         std::string name = myUtils::result[i];

        Dependencies k(name, files, allDependencies);
        allDependencies.push_back (k);

        if( k.getFileName().substr(k.getFileName().size() - 4) == ".cpp" )
        {
            object.push_back(k.getFileName().substr(0, k.getFileName().size() - 4) + ".o");

            fputs(k.getFileName().substr(0, k.getFileName().size() - 4).c_str(), makeFile);
            fputs(".o: ", makeFile);

            fputs(k.getFileName().c_str(), makeFile);
            fputs(" ", makeFile);

            for (size_t j = 0; j < k.getCountDependencies(); ++j )
            {
                fputs(k.getDependencies(j).c_str(), makeFile);
                fputs(" ", makeFile);
            }
            fputs("\n\tg++ -c ${CFLAGS} -c $<\n\n", makeFile);
            ++count;
        }
    }
    fputs("clean: \n\trm -f *.o \n\nproject:", makeFile);

    for ( int i = 0 ; i < count; ++i )
    {
        fputs(" ", makeFile);
        fputs(object[i].c_str(), makeFile); 
    }

    fputs("\n\tg++ ${CFLAGS} $^ -o project", makeFile);

    fclose(makeFile);
}
