#pragma once

//creates IFunction using  input file extension
IFunctionPtr createIFunction(const std::string &str_in);

//print truth table of input file definition
void truth_table(const std::string &str_in);

//print truth table of two input file difenition
void compose_functions(const std::string &str_inA, const std::string &str_inB);

//print truth table of IFunctionPtr
void print_truth_table(const IFunctionPtr p);

//print equal function or notvoid equal_functions(std::string const& firstFile, std::string const& secondFile);

