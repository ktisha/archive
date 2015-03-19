
#include "Function.h"

Function::Function(std::string const &validity_table) //opening validity table file
{
    std::ifstream tableFile (validity_table.c_str());
    if (!tableFile)
    {
        std::string temp="Impossible to open file: " + validity_table + "!";
        throw std::logic_error(temp.c_str());
    }
    
    //reading number of variables
    tableFile >> myInputsNumber >> myOutputsNumber;
    size_t maxIndex=((int32_t)1) << myInputsNumber;
    myTable.resize(maxIndex);

    //reading validity table and writing it to boolVector

    int counter=0;
    while (!tableFile.eof())
    {
        int index=0;
        for (int i=0; i < myInputsNumber; ++i)
        {
            if (tableFile.eof()) break;
            //reading one bit from file
            char ch;
            tableFile >> ch;
            if ( (ch == '0') or (ch == '1') ) 
            {
                index = index + (ch - '0') * ((int32_t)1 << i);
            }

        }
            
        if (tableFile.eof()) break;
        //std::cout<<" - "<<index<<std::endl;

        myTable[index].resize(myOutputsNumber,0);
        for (int i=0; i < myOutputsNumber; ++i)
        {
            //reading one bit from file
            if (tableFile.eof()) break;
            char ch;
            tableFile >> ch;
            if ( (ch == '0') or (ch == '1') ) 
            {
                myTable[index].set(i,((bool)(ch-'0')));
            }
            
        }
        counter++;
    }

    if (counter!=maxIndex)
    {
        std::cout << "Warning! BAD table file: " << validity_table.c_str() <<". Not enough entrance variables variants" << std::endl;
        system("pause");
    }
    
    tableFile.close();
}

BoolVector Function::evaluate(BoolVector const &in) const
{
    if (in.size() != myInputsNumber)
        throw std::logic_error("Wrong size of input data for evaluation!");
    
    int index=0;
    for (int32_t i=0; i < in.size();++i)
    {
        index=index+(in[i] * (1<<i));
    }

    return myTable[index];
}



