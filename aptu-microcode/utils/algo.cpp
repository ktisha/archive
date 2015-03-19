//class Utils

#include <iostream>

#include "algo.h"

bool algo::isEqual(IFunctionPtr const first, IFunctionPtr const second)
{
    if( (first->getCountInputs() == second->getCountInputs()) && (first->getCountOutputs() == second->getCountOutputs()))
    {
        BoolVector values;
        BoolVector outputFirst;
        BoolVector outputSecond;

        values.resize(first->getCountInputs());

        int out = 1 << first->getCountInputs();

    	for (int i = 0; i != first->getCountInputs(); ++i)
        {
            values.set(i,0);
        }
        for (int number = 0; number != out; ++number)
        {

            outputFirst = first->evaluate(values);
            outputSecond = second->evaluate(values);

            for (int i = 0; i != first->getCountOutputs(); ++i)
            {
                if( outputFirst[i] != outputSecond[i] )
                {
					std::cout << " FUNCTIONS IS NOT EQUAL!!!!!" << std::endl;
					std::cout << values << std::endl;
					return false;
				}
            }
            ++values;
        }
        std::cout << " FUNCTIONS IS EQUAL" << std::endl;
        return true;
    }
    
    std::cout << " FUNCTIONS IS NOT EQUAL" << std::endl;
    return false;
}


