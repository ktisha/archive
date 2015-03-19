// main

#include <iostream>
#include <cstdlib>

#include "./truthtable/Function.h"
#include "./utils/Composition.h"
#include "./utils/BoolVectorT.h"
#include "./utils/utils.h"

#include "./circuit/classCircuit.h"
#include "./utils/algo.h"
#include "./ruletable/RuleTable.h"

int main()
{

//	from CIRCUIT

    BoolVector values;
    BoolVector output;

	Circuit a ("./circuit/input/input1.txt");
	Circuit b = a.fullSimplify(0, 0);
    values.resize(a.getCountInputs()); 

	int out = 1 << b.getCountInputs();
    for (int i = b.getCountInputs() - 1; i >= 0; --i)
    {
        values.set(i,0);
    }
    for (int number = 0; number != out; ++number)
    {
        output = b.evaluate(values);        

        std::cout << output;
        ++values;
    }
    
    std::cout << std::endl;
    
    equal_functions("./input/trial.ctbl", "./input/trial2.ctbl");
    
//	from RULETABLE

        try {
    IFunctionPtr r(new RuleTable("./ruletable/input/Rules.txt"));
    BoolVector bv;
    bv.push_back(1);
    bv.push_back(0);
    bv.push_back(1);
    
    BoolVector b = r->evaluate(bv);
    std::cout << b << std::endl;
    char ch;
    std::cin >> ch;
    } catch (std::exception &e) {
        std::cerr << "Something goes wrong: " <<  e.what() << std::endl;    
        std::cerr << "Program will be closed." << std::endl;    
        char ch;
        std::cin >> ch;
        return -1;
    }

//	from TRUTHTABLE
    
    try {
        IFunctionPtr A(new Function("./truthtable/input/table.txt"));
        IFunctionPtr B(new Function("./truthtable/input/table2.txt"));
        IFunctionPtr D(new Composition(B,A));
        
        
        BoolVector my;
        my.resize(3,0);
        my.set(0,1);
        my.set(1,1);
        my.set(2,1);
        
        std::cout << B->getCountInputs() << std::endl;
        std::cout << B->getCountOutputs() << std::endl;
        
        std::cout << D->getCountInputs() << std::endl;
        std::cout << D->getCountOutputs() << std::endl;
        
        std::cout <<  A->evaluate(my);

        std::cout << B->evaluate(my);

        std::cout << D->evaluate(my);

        BoolVector bv;
//        bv.resize(32, 1);
        bv.resize(31, 1);
        
        //bv.resize(3,1);
        //bv.resize(6,0);
                
        std::cout << bv;
        
    } catch (std::exception & e) {
        std::cerr << "Something goes wrong: " <<  e.what() << std::endl;    
        std::cerr << "Program will be closed." << std::endl;    

        return -1;
    }

}


