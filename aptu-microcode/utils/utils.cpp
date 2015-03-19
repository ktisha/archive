#include <iostream>
#include <stdexcept>
#include <string>

#include "../IFunction.h"
#include "utils.h"

#include "../truthtable/Function.h"
#include "../ruletable/RuleTable.h"
#include "../circuit/classCircuit.h"

#include "Composition.h"
#include "BoolVectorT.h"
#include "algo.h"

IFunctionPtr createIFunction(const std::string &str_in){
    int i = str_in.rfind(".");
    if (i == std::string::npos){
        throw std::logic_error("Error in createIFunction(): input file without extension!");
    }
    std::string ext(str_in.begin() + i + 1, str_in.end());

    if (ext == "ttbl"){
        return IFunctionPtr(new Function(str_in));
    } else if (ext == "ctbl"){
        return IFunctionPtr(new Circuit(str_in));
    }else if (ext == "rtbl"){
        return IFunctionPtr(new RuleTable(str_in));
    }
    throw std::logic_error("Error in createIFunction(): bad extension of input file: " + ext);

}

void equal_functions(std::string const& firstFile, std::string const& secondFile)
{
	IFunctionPtr first = createIFunction(firstFile);
	IFunctionPtr second = createIFunction(secondFile);
	algo::isEqual(first, second);
}

void print_truth_table(const IFunctionPtr p){
    int size = p->getCountInputs();
    if (size > 63) throw std::logic_error("Error in print_truth_table()! Too big truth table!");
    BoolVector bv;
    BoolVectorT<int64_t> helpbv;
    bv.resize(size, false);
    helpbv.resize(size + 1, false);

    do {
        BoolVector res = p->evaluate(bv);
        std::cout << bv << " : " << res << std::endl;
        ++bv;
        ++helpbv;
    } while (helpbv[ bv.size()] != true);
}


void truth_table(const std::string &str_in){
    IFunctionPtr p = createIFunction(str_in);
    print_truth_table(p);
}

void compose_functions(const std::string &str_inA, const std::string &str_inB){
    IFunctionPtr p1 = createIFunction(str_inA);
    IFunctionPtr p2 = createIFunction(str_inB);
    IFunctionPtr comp(new Composition(p1,p2));
    print_truth_table(comp);
}

//example of usage
/*/
int main(){
    try {
        IFunctionPtr a = createIFunction("rules.rtbl");
        IFunctionPtr b = createIFunction("rules.rtbl");
        
        truth_table("rules.rtbl");
        std::cout << std::endl;
        compose_functions("circuit.ctbl","rules.rtbl");
        char ch;
        std::cin >> ch;
    }catch (std::exception &e) {
        std::cout << e.what();
        char ch;
        std::cin >> ch;
    }
};
/*/
