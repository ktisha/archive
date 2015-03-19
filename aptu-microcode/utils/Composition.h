#ifndef _COMPOSITION__h
#define _COMPOSITION__h

#include <stdexcept>
#include "../IFunction.h"
#include "../utils/BoolVectorT.h"


class Composition : public IFunction
{
private:
    IFunctionPtr const myA;
    IFunctionPtr const myB;
public:
    int getCountInputs()  const { return myB->getCountInputs(); }
    int getCountOutputs() const { return myA->getCountOutputs(); }

    Composition(IFunctionPtr A, IFunctionPtr B);

    BoolVector evaluate(BoolVector const &in) const;
};
#endif

