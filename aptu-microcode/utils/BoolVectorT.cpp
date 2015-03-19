#include "BoolVectorT.h"

std::ostream& operator<<(std::ostream &stream, const BoolVector &b)
{
    for (int i=0; i < b.size(); ++i)
    {
        stream << b[i];
    }
    stream <<std::endl;
}
