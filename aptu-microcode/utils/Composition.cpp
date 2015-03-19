#include "Composition.h"


BoolVector Composition::evaluate(BoolVector const &in) const
{
    return myA->evaluate(myB->evaluate(in));
}

Composition::Composition(IFunctionPtr A, IFunctionPtr B)
    : myA(A)
    , myB(B) 
{
    if (B->getCountOutputs() != A->getCountInputs()) 
        throw std::logic_error("Error! Impossible to create composition! Function sizes not coordinated!");
}

