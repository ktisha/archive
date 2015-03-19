#ifndef _RULETABLE__H
#define _RULETABLE__H

#include <algorithm>
#include <iostream>
#include <sstream>
#include <fstream>
#include <string>
#include <vector>
#include <stack>
#include "../IFunction.h"
#include "../utils/BoolVectorT.h"


struct Token
{
    enum Type {LEFT_BRACKET, RIGHT_BRACKET, VARIABLE, OPERATION, NUMBER};
    enum Operation {PLUS, MULTIPLY, NOT};

    Type type;
    Operation op;
    int value;
};

class RuleTable:public IFunction
{
    typedef std::string string;
    public:
        RuleTable(string const &ruleTable);
        BoolVector evaluate(BoolVector const &in)const;
        int getCountOutputs() const {return myOutputsNumber;};
        int getCountInputs() const {return myInputsNumber;};

    private:
        int myInputsNumber;
        int myOutputsNumber;
        std::vector< std::vector<Token> > myRules;
        int getPriority (const Token &t) const;
  
        bool isVar          (const char &ch) const {return ( tolower(ch) == 'x');}
        bool isOperation    (const char &ch) const {return (ch == '+') or (ch == '*') or (ch == '!');}
        bool isLeftBracket  (const char &ch) const {return ( ch == '(' );}
        bool isRightBracket (const char &ch) const {return ( ch == ')' );}
        
        void correctString(string &str) const;
        void error(const string &method,const string &what) const;
        void error(const string &method,const string &what, const string &line) const;

    
        int makeInvertPolishExpr(string &str_in, std::vector<Token> &result) const;
        int evalSingleRule(const std::vector<Token> &in_exp, const BoolVector &in_bv) const;    
};

#endif
