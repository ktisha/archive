//classCircuit

#ifndef _CLASSCIRCUIT_H_
#define _CLASSCIRCUIT_H_

#include "../IFunction.h"

#include <string>
#include <iostream>
#include <map>           
#include <iterator>           
#include <set>
#include <vector>
           
struct Gate
{
    enum Type { NOT, IMP, AND, OR, EQ };

    Type type;        

    int leftParent;	
    int rightParent;
};

class Circuit : public IFunction
{
    private:
        int countLeaves;
        int countGates;
        int countOutputs;
       
        std::vector < Gate > gateArray;

    public:
        Circuit(std::string const& input);
        ~Circuit() {}
        
    private:
        Circuit(Circuit const& main, std::map <int, int> const& input);
        Circuit()	{}
        
    public:
        bool isSimplify(int gateNumber);
        Circuit subCircuit(int gateNumber);
        BoolVector evaluate( BoolVector const& values) const;		
        int getCountInputs()        const { return countLeaves;}
        int getCountGates()         const { return countGates;}
        int getCountOutputs()       const { return countOutputs;}
        
	private:
		void gateDependencies(int gateNumber, std::set <int>  & s);
		void dependencies(int gateNumber, std::map <int, int> & m);
		
	private:
		bool simplify(int gateNumber, bool value);
		std::vector <int> findDescendants(int gateNumber, int Gate::* parent)	const;
		void changeThisToParent(int gateNumber, int Gate::* parent);
		bool partSimplify(int gateNumber, bool value, int Gate::* parent);
		
		Circuit defaultCircuit(bool value)	const;
		
	public:
		Circuit fullSimplify(int gateNumber, bool value)	const;
		size_t size()	const {return gateArray.size();}
		
	public:
//        Circuit &operator = (Circuit const& Other);
//        Circuit(Circuit const& Other);   
};

#endif
