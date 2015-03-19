#ifndef _FUNCTION__h
#define _FUNCTION__h

#include <iostream>
#include <fstream>
#include <string>
#include <stdexcept>
#include <cstdlib>
#include "../IFunction.h"
#include "../utils/BoolVectorT.h"

class Function: public IFunction
{
    std::vector< BoolVector > myTable;
    size_t myOutputsNumber;
    size_t myInputsNumber;
public:
    int getCountOutputs() const { return myOutputsNumber; }
    int getCountInputs()  const { return myInputsNumber; }

    Function(std::string const &validity_table);

    BoolVector evaluate(BoolVector const &in) const;
};
#endif
