CFLAGS=-ggdb

all: project

main.o: main.cpp ./circuit/classCircuit.h IFunction.h ./utils/algo.h ./ruletable/RuleTable.h ./truthtable/Function.h ./utils/Composition.h ./utils/BoolVectorT.h
	g++ -c ${CFLAGS} -c $<

BoolVectorT.o: ./utils/BoolVectorT.cpp ./utils/BoolVectorT.h
	g++ ${CFLAGS} -c ./utils/BoolVectorT.cpp

classCircuit.o: ./circuit/classCircuit.cpp ./circuit/classCircuit.h IFunction.h 
	g++ -c ${CFLAGS} -c $<

algo.o: ./utils/algo.cpp ./utils/algo.h IFunction.h
	g++ -c ${CFLAGS} -c $<

RuleTable.o: ./ruletable/RuleTable.cpp ./ruletable/RuleTable.h
	g++ -c ${CFLAGS} -c $<

Composition.o: ./utils/Composition.h ./utils/Composition.cpp IFunction.h
	g++ ${CFLAGS} -c ./utils/Composition.cpp 

Function.o: ./truthtable/Function.h ./truthtable/Function.cpp IFunction.h
	g++ ${CFLAGS} -c ./truthtable/Function.cpp

utils.o: ./utils/utils.h ./utils/utils.cpp
	g++ ${CFLAGS} -c ./utils/utils.cpp

clean: 
	rm -f *.o 

project: main.o Function.o Composition.o classCircuit.o algo.o RuleTable.o BoolVectorT.o utils.o
	g++ ${CFLAGS} $^ -o project
