//classCircuit

#include "classCircuit.h"
#include <fstream>

Circuit::Circuit(std::string const& input)
{
    std::ifstream file(input.c_str());

    if ( file )
    {
        file >> countLeaves >> countOutputs >> countGates;	

        gateArray.resize(countGates + countLeaves);
                            
        for ( int i = 0; i != countGates; ++i )
        {
            int m = 0;
            int r = 0;
            int l = 0;
            std::string  n;
            file >> m >> l >> r >> n; 	
                
            if ( (m >= countLeaves) && (m < countLeaves + countGates))
            {
                if ((l >= 0) && (l < countLeaves + countGates))
                {
                    gateArray[m].leftParent = l;
                }
                else return;
                if ((r >= 0) && (r < countLeaves + countGates))		
                {
                    gateArray[m].rightParent = r;
                    if (n == "&")
                        gateArray[m].type = Gate::AND;
                    else if(n == "!")
                        gateArray[m].type = Gate::NOT;
                    else if(n == "|")
                        gateArray[m].type = Gate::OR;
                    else if(n == "~")
                        gateArray[m].type = Gate::EQ;
                    else if(n == "=>")
                        gateArray[m].type = Gate::IMP;
                    else 
                    {
                        gateArray[m].type = Gate::AND;
                        std::cerr << "AAAA! Wrong value!!! (" << n << ")\n";
                    }
                }
                else 
                    std::cerr << " Wrong input file\n";
            }
            else 
                std::cerr << " Wrong input file\n";
        }
    }
    
    for ( int i = 0; i != countLeaves; ++i )
    {
        gateArray[i].leftParent = 0;
        gateArray[i].rightParent = 0;
//        gateArray[i].type = Gate::OR;
    }
}

BoolVector Circuit::evaluate( BoolVector const& inValues ) const
{
    BoolVector values = inValues;
    values.resize(gateArray.size(), false);

    BoolVector outVector;

    outVector.resize(getCountOutputs());
  
    for (int i = countLeaves; i != gateArray.size(); ++i)
    {
        switch (gateArray[i].type)
        {
            case Gate::NOT:
                values.set(i, !values[gateArray[i].rightParent]);
                break;
            
            case Gate::AND:
                values.set(i, values[gateArray[i].leftParent] && values[gateArray[i].rightParent]);
                break;
            
            case Gate::IMP:
                values.set(i, !values[gateArray[i].leftParent] || values[gateArray[i].rightParent]);
            
            case Gate::OR:
                values.set(i, values[gateArray[i].leftParent] || values[gateArray[i].rightParent]);
                break;

            case Gate::EQ:
                values.set(i, values[gateArray[i].leftParent] == values[gateArray[i].rightParent]);
                break;
            
    	    default:
        		std::cout << "wrong value of logic operation" << std::endl;
                break;
        }
    }
    
    for (int i = gateArray.size() - 1; i >= gateArray.size() - getCountOutputs(); --i) 
    {
        outVector.set(gateArray.size() - 1 - i, values[i]);
    }

    return outVector;
}

Circuit::Circuit(Circuit const& main, std::map <int, int> const& input)
{
	countLeaves = 0;
    gateArray.resize(input.size());

    for (std::map <int, int>::const_iterator i = input.begin(); i != input.end(); ++i)
    {
    	Gate & gate = gateArray[i->second];
    	Gate const & mainGate = main.gateArray[i->first];
        gate.type = mainGate.type;
        std::map <int, int>::const_iterator right = input.find(mainGate.rightParent);
        if(right != input.end())
	        gate.rightParent = right->second;
	    else
	    	gate.rightParent = 0;
	    
	    std::map <int, int>::const_iterator left = input.find(mainGate.leftParent);
	    if( left != input.end())
		    gate.leftParent = left->second;
		else
			gate.leftParent = 0;
    }
    
	for (int i = 0; i != gateArray.size(); ++i)
	{
		if ( gateArray[i].rightParent == gateArray[i].leftParent)
			++ countLeaves;
	}
	
    countOutputs = 1;
    countGates = input.size() - countLeaves;
}

Circuit Circuit::subCircuit(int gateNumber)
{
	std::map <int, int> dependenciesGates;
    dependencies(gateNumber, dependenciesGates);
    
    Circuit subcircuit(*this, dependenciesGates);
    return subcircuit; 
}

void Circuit::dependencies(int gateNumber, std::map<int, int> & m)
{
	std::set <int> subArr;
	gateDependencies(gateNumber, subArr);
    int j = 0;
    for(std::set<int>::iterator it = subArr.begin(); it != subArr.end(); ++it, ++j)
    {
    	m.insert(std::make_pair(*it, j));
    }
}

void Circuit::gateDependencies(int gateNumber, std::set<int> & s )
{
    if ( gateNumber >= getCountInputs() )
    {
		s.insert(gateNumber);
	
        if ( gateArray[gateNumber].leftParent >= getCountInputs() && gateArray[gateNumber].type != Gate::NOT )
        {
            gateDependencies(gateArray[gateNumber].leftParent, s);
        }    
        else if (gateArray[gateNumber].type != Gate::NOT )
        {
            s.insert(gateArray[gateNumber].leftParent);
        }
		  
        if ( gateArray[gateNumber].rightParent >= getCountInputs() )
        {
            gateDependencies(gateArray[gateNumber].rightParent, s);
        }
        else
        {
            s.insert(gateArray[gateNumber].rightParent);
        }
    }    
}

bool Circuit::isSimplify(int gateNumber)
{
	BoolVector output;
	BoolVector values;
	int count = 0;

	Circuit b = subCircuit(gateNumber);

	values.resize(b.getCountInputs()); 
 
    int out = 1 << b.getCountInputs();

	int tmp = 0;

	for (int i = b.getCountInputs() - 1; i >= 0; --i)
    {
        values.set(i,0);
    }
    for (int number = 0; number != out; ++number)
    {
        output = b.evaluate(values);        

        std::cout << output[0] << " ";

        if( tmp != output[0] && number != 0)
			++count;                    
		tmp = output[0];
		++values;
    }
    std::cout <<  std::endl;    

    if( count != 0)
    	return false;
    else return true;
}

bool Circuit::simplify(int gateNumber, bool value)
{
	bool a = partSimplify(gateNumber, value, &Gate::leftParent);
	bool b = partSimplify(gateNumber, value, &Gate::rightParent);
	return a || b;
}

bool Circuit::partSimplify(int gateNumber, bool value, int Gate::*parent)
{	
	std::vector <int> descendants = findDescendants(gateNumber, parent);
	for (int i = 0; i != descendants.size(); ++i)
	{
		Gate & g = gateArray[descendants[i]];
		switch (g.type)
		{
			case Gate::NOT:
				if(g.leftParent == g.*parent)
					simplify(descendants[i], !value); 
				break;
			
			case Gate::AND:
			    if(0 == value)
			    {
			    	simplify(descendants[i], value);
			    }
			    else
			    {
			    	changeThisToParent(descendants[i], parent);
			    }
			    break;
			
			case Gate::IMP:
			    if(0 == value)
				{
					if (g.leftParent == g.*parent)
						g.type = Gate::NOT;
					else simplify(descendants[i], !value);
				}
				else
				{
					if (g.leftParent == g.*parent)
						simplify(descendants[i], value);
					else changeThisToParent(descendants[i], &Gate::rightParent);
				}

			case Gate::OR:
			    if(0 == value)
			    {
			    	changeThisToParent(descendants[i], parent);
			    }
			    else
			    {
			    	simplify(descendants[i], value);
			    }
			    break;

			case Gate::EQ:
				if(0 == value)
				{
					g.type = Gate::NOT;
					g.leftParent = 0;
					if (g.leftParent == g.*parent)
						g.rightParent = g.leftParent;
				}
				else
				{
					changeThisToParent(descendants[i], parent);
				}
			    break;

			default:
				std::cout << "wrong gate's number" << std::endl;
			    break;
		}
	}
	return !descendants.empty();
}

void Circuit::changeThisToParent(int gateNumber, int Gate::*parent)
{
	std::vector <int> leftSubDescendants = findDescendants(gateNumber, &Gate::leftParent);
	Gate & g = gateArray[gateNumber];
	for(std::vector <int>::iterator it = leftSubDescendants.begin(); it != leftSubDescendants.end(); ++it)
	{	
		if(g.leftParent == g.*parent)
			gateArray[*it].rightParent = g.leftParent;
		else gateArray[*it].rightParent = g.rightParent;
	}
	
	std::vector <int> rightSubDescendants = findDescendants(gateNumber, &Gate::rightParent);
	for(std::vector <int>::iterator it = rightSubDescendants.begin(); it != rightSubDescendants.end(); ++it)
	{
		if(g.leftParent == g.*parent)	
			gateArray[*it].leftParent = g.leftParent;
		else gateArray[*it].leftParent = g.rightParent;
	}
	if(0 == !(leftSubDescendants.empty() || rightSubDescendants.empty()))
	{
		if(g.leftParent == g.*parent)	
		g = gateArray[g.leftParent];
		else g = gateArray[g.rightParent];
	}
}

std::vector <int> Circuit::findDescendants(int gateNumber, int Gate::*parent)	const
{
	std::vector <int> descendants;

	for (int i = countLeaves; i != gateArray.size(); ++i)
	{
		Gate const & g = gateArray[i];
		if(g.leftParent == g.*parent)
		{
			if(g.rightParent == gateNumber)
			{
				descendants.push_back(i);
			}
		}
		else 
		{
			if(g.leftParent == gateNumber)
			{
				descendants.push_back(i);
			}
		}
	}
	return descendants;
}

Circuit Circuit::fullSimplify(int gateNumber, bool value)	const
{
	Circuit copy(*this);
	bool a = copy.simplify(gateNumber, value);
	if(0 == a)
	{
		std::cout << "THIS CIRCUIT HAS ONLY ONE VALUE = " << value << std::endl;
		if(0 == value)
			return defaultCircuit(false);
		if(1 == value)
			return defaultCircuit(true);
	}
	return copy.subCircuit(gateArray.size() - 1);
}

Circuit Circuit::defaultCircuit(bool value)	const
{
	Circuit subcircuit;
	subcircuit.countOutputs = 1;
	subcircuit.countLeaves = 1;

	subcircuit.gateArray.resize(3);
	subcircuit.gateArray[0].leftParent = 0;
	subcircuit.gateArray[0].rightParent = 0;
	subcircuit.gateArray[1].leftParent = 0;
	subcircuit.gateArray[1].rightParent = 0;
	subcircuit.gateArray[1].type = Gate::NOT;
	subcircuit.gateArray[2].leftParent = 0;
	subcircuit.gateArray[2].rightParent = 1;
	if(0 == value)
		subcircuit.gateArray[2].type = Gate::AND;
	else
		subcircuit.gateArray[2].type = Gate::OR;
	return subcircuit;
}
