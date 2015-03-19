#include "RuleTable.h"

RuleTable::RuleTable(const string &ruleTable)
{
    //opening file stream
    std::ifstream ruleFile(ruleTable.c_str());
    if (!ruleFile)
    {
        error("RuleTable()", string("Impossible to open file:")+ ruleTable);
    }
    
    //reading number of variables
    ruleFile >> myInputsNumber >> myOutputsNumber;
    if (ruleFile.fail()) {
        error("RuleTable()","no inputs/outputs numbers");
    }
    myRules.resize(myOutputsNumber);
    
    //creating contoller to check if all outputs will be defined
    BoolVector outCheck;
    outCheck.resize(myOutputsNumber, false);
    
    //reading rule lines from file
    while (!ruleFile.eof()){
        string line;
        getline(ruleFile, line, ';');
        correctString(line);//make no spaces and small caps
    
        std::vector<Token> result;
        int y_index = makeInvertPolishExpr(line, result);
        //if output index = -1 then rules are ended
        if (y_index == -1) break;
        myRules[y_index] = result;
        outCheck.set(y_index ,true);
    }
    
    //checking that all outputs defined
    for (int i=0; i < myOutputsNumber; ++i){
        if (outCheck[i] == false){
            error("RuleTable()","not enouph definitions in rule file");
        }
    }

}

void RuleTable::error(const string &method,const string &what, const string &line) const
{
    string temp = "Error in RuleTable::"+ method + " : " + what + ", in line : " + line + " !";
    throw std::logic_error(temp);
}

void RuleTable::error(const string &method,const string &what) const
{
    string temp = "Error in RuleTable::"+ method + " : " + what + " !";
    throw std::logic_error(temp);
}
    
void RuleTable::correctString(string &str) const 
{
    //remove spaces
    str.erase( std::remove_if( str.begin(), str.end(), isspace ), str.end());

    //tolower string
    transform(str.begin(), str.end(), str.begin(), tolower);    
}

BoolVector RuleTable::evaluate(BoolVector const &in) const
{
    if (in.size() != myInputsNumber){
        error("evaluate()" ,"input BoolVector size and RuleTable input sizes are not coordinated");
    }
    BoolVector tempBV;
    for (int i=0; i < myOutputsNumber; ++i){
        //uncomment next line to look at integer type result
        //std::cout<< evalSingleRule(myRules[i], in) << std::endl;
        tempBV.push_back(evalSingleRule(myRules[i], in));
    }
    return tempBV;
}

//returns token priority
int RuleTable::getPriority(const Token &t) const
{
    if (t.type == Token::OPERATION) {
        if (t.op == Token::NOT) return 4;
        if (t.op == Token::MULTIPLY) return 3;
        if (t.op == Token::PLUS    ) return 2;
    } else if (t.type == Token::LEFT_BRACKET) {
        return 1;
    } else
        return 0;
}

// returns pair from index of output and corresponding invert polish expression from standart arithmetical expression
int RuleTable::makeInvertPolishExpr(string &str_in, std::vector<Token> &result) const
{

    string::iterator p = str_in.begin();
    int y_index = -1;//this will help to understand if index changed
    //reading output variable number
    if ( *p == 'y') {
        ++p;
        string y_index_string(p, str_in.end());
        const char *c_str = y_index_string.c_str();
        char *ptr;
        y_index = strtol(c_str, &ptr, 0);

        if (ptr == c_str) {
            error("makeInvertPolishExpr()", string("unexpected symbol: ") + *p, str_in);
        }

        if (y_index >= myOutputsNumber) {
            error("makeInvertPolishExpr()","output variable index > outputs number" ,str_in);
        }

        p += (ptr - c_str);

        if ( *p != '=' ) {
            error("makeInvertPolishExpr()", string("unexpected symbol: ") + *p, str_in);
        }
        ++p;
    } else {
        //if output index is not set and we are in the end of line then rules are ended
        if ( (p == str_in.end()) and (y_index == -1)) {
            return y_index;             
        } 
        error("makeInvertPolishExpr()", string("unexpected symbol: ") + *p, str_in);
    }

    //checking that there is right brackets order in str_in
    int leftBrackets = 0;
    int rightBrackets = 0;
    for(string::const_iterator p = str_in.begin(); p != str_in.end(); ++p){
        if ( (*p == '(') or (*p == ')')  ){
            if (*p == '(' ){
                ++leftBrackets;
            } else {
                ++rightBrackets;
            }
            
            if (rightBrackets > leftBrackets) {
                error("makeInvertPolishExpr()","wrong brackets order", str_in);
            }
        }
    }
    
    if (rightBrackets != leftBrackets) {
        error("makeInvertPolishExpr()","wrong brackets order", str_in);
    }
            
    //main parser
    std::stack<Token> op_stack;
    Token prevToken;
    
    while (p != str_in.end()){

        if ( isVar(*p) ) 
        {
            ++p;
            string y_index_string(p, str_in.end());
            const char *c_str = y_index_string.c_str();
            char *ptr;
            int tempIndex = strtol(c_str, &ptr, 0);

            if (ptr == c_str) {
                error("makeInvertPolishExpr()","variable with no index", str_in);
            }

            if (tempIndex >= myInputsNumber) {
                error("makeInvertPolishExpr()","wrong rule: input variable index > inputs number", str_in);
            }

            p += (ptr - c_str);

            Token tempToken;
            tempToken.type = Token::VARIABLE;
            tempToken.value = tempIndex;

            //checking errors in rule
            if ( !(result.empty() and op_stack.empty())) {
                if (prevToken.type == Token::VARIABLE or prevToken.type == Token::NUMBER ) {
                    error("makeInvertPolishExpr()","wrong rule defenition", str_in);
                }
            }    
            prevToken = tempToken;
            result.push_back(tempToken);
        } 
        else if ( isdigit(*p) ) 
        {
            int tempValue = *p - '0';
            ++p;
            if ((tempValue > 1) or isdigit(*p)) error("makeInvertPolishExpr()", "bad digit(only 0 or 1 is allowed):", str_in);
            Token tempToken;
            tempToken.type = Token::NUMBER;
            tempToken.value = tempValue;

            //checking errors in rule
            if ( !(result.empty() and op_stack.empty())) {
                if (prevToken.type == Token::VARIABLE or prevToken.type == Token::NUMBER ) {
                    error("makeInvertPolishExpr()","wrong rule defenition", str_in);
                }
            }    
            prevToken = tempToken;
            result.push_back(tempToken);
            
        }
        else if (isOperation(*p))
        {
            Token tempToken;
            tempToken.type = Token::OPERATION;
            switch (*p) {
                case '+':
                    tempToken.op = Token::PLUS;
                    break;
                case '*':
                    tempToken.op = Token::MULTIPLY;
                    break;
                case '!':
                    tempToken.op = Token::NOT;
                    break;                   
            }

            //checking errors in rule
            if ( !(result.empty() and op_stack.empty())) {
                //this is for such errors as: y0 = x0! or 4! or )! or !!
                if (tempToken.op == Token::NOT) {
                    if (prevToken.type == Token::VARIABLE or
                        prevToken.type == Token::NUMBER or
                        prevToken.type == Token::RIGHT_BRACKET or
                        (prevToken.type == Token::OPERATION and prevToken.op == Token::NOT)) 
                    {                    
                        error("makeInvertPolishExpr()","wrong rule defenition", str_in);
                    }                   
                } else {
                    //this is for such errors as: y0 = !+ or ++ or ** or +* or (* or (+ and so on...
                    if (prevToken.type == Token::OPERATION or prevToken.type == Token::LEFT_BRACKET ) {
                        error("makeInvertPolishExpr()","wrong rule defenition", str_in);
                    }
                }
            } else {
                //this is for errors like this : y0 = *x0; or  y0 = +3; and so on ...  
                //when there is binary operator in the beginning of rule;
                if (tempToken.op != Token::NOT) {
                        error("makeInvertPolishExpr()","wrong rule defenition", str_in);
                }
            }    

            prevToken = tempToken;
            
            int curPriority = getPriority(tempToken);


            if ( op_stack.empty() or ( curPriority > getPriority(op_stack.top()) )) {
                op_stack.push(tempToken);
                ++p;
            } else {
                do {
                    result.push_back(op_stack.top());
                    op_stack.pop();
                } while (!op_stack.empty() and (curPriority <= getPriority(op_stack.top())) );
                op_stack.push(tempToken);
                ++p;
            }
        } 
        else if (isLeftBracket(*p))
        {
            Token tempToken;
            tempToken.type = Token::LEFT_BRACKET;

            //checking errors in rule
            if ( !(result.empty() and op_stack.empty())) {
                if (prevToken.type == Token::VARIABLE or prevToken.type == Token::NUMBER ) {
                    error("makeInvertPolishExpr()","wrong rule defenition", str_in);
                }
            }    
            prevToken = tempToken;
            op_stack.push(tempToken);
            ++p;
        } 
        else if (isRightBracket(*p)) 
        {
            //checking errors in rule
            if ( !(result.empty() and op_stack.empty())) {
                if (prevToken.type == Token::OPERATION or prevToken.type == Token::LEFT_BRACKET ) {
                    error("makeInvertPolishExpr()","wrong rule defenition", str_in);
                }
            }    
            prevToken.type = Token::RIGHT_BRACKET;
            
            while (op_stack.top().type != Token::LEFT_BRACKET ) {
                result.push_back(op_stack.top());
                op_stack.pop();
            };
            op_stack.pop();
            ++p;

        } else {
            error("makeInvertPolishExpr()",string("wrong symbol :") + *p , str_in);
        }
    }

    if (result.empty()) {
        error("makeInvertPolishExpr()","rule is empty");        
    }
    
    //this is for errors like this: y0 = x1+; and so on...
    //when operation is in the end of rule
    if (prevToken.type == Token::OPERATION) {
        error("makeInvertPolishExpr()", "wrong rule defenition", str_in);
    }
    
    //transfer tokens from op_stack to result vector
    while ( !op_stack.empty()) {
        result.push_back( op_stack.top() );
        op_stack.pop();
    }


    return y_index;
}

//this method evaluate result of invert polish expression.
int RuleTable::evalSingleRule(const std::vector<Token> &in_exp, const BoolVector &in_bv) const
{
    std::stack<int> result;
        
    for (int i=0; i < in_exp.size(); ++i){
        if ( in_exp[i].type == Token::VARIABLE) 
        {
            result.push( in_bv[ in_exp[i].value ] );
        }   
        else if ( in_exp[i].type == Token::NUMBER)
        {
            result.push( in_exp[i].value );
        } 
        else if ( in_exp[i].type == Token::OPERATION)
        {
            if (result.empty()){
                error("evalSingleRule()","no arguments for operation");
            }
            int a = result.top();
            result.pop();
                 
            if (in_exp[i].op == Token::NOT) {
                result.push( !a );                
            } else {
                
                if (result.empty()){
                    error("evalSingleRule()","no arguments for operation");
                }
                int b = result.top();
                result.pop();
    
                switch( in_exp[i].op ){
                    case Token::PLUS:
                        result.push(a + b);
                        break;
                    case Token::MULTIPLY:
                        result.push(a * b);
                        break;
                }
            }
        } else {
            error("evalSingleRule()","wrong token");
        }
    }
    
    return result.top();
}
