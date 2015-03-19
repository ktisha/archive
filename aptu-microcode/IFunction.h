#ifndef _IFUNCTION__h
#define _IFUNCTION__h

#include "boost/shared_ptr.hpp"
#include "./utils/BoolVectorT.h"

class IFunction
{
	public:
		virtual BoolVector evaluate (BoolVector const &in) const = 0;
		virtual int getCountOutputs () const = 0;
		virtual int getCountInputs () const = 0;

		virtual ~IFunction(){};
};

typedef boost::shared_ptr<IFunction> IFunctionPtr;

#endif


