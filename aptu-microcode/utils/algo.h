//class algo

#ifndef _ALGO_H_
#define _ALGO_H_

#include "../IFunction.h"

class algo
{
    private:
        algo();

    public:
        static bool isEqual(IFunctionPtr const first, IFunctionPtr const second);
};

#endif
