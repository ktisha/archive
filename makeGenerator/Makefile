CFLAGS=-Wall -ggdb 

all: project

classdir.o: classdir.cpp classdir.h 
	g++ -c ${CFLAGS} -c $<

dependencies.o: dependencies.cpp dependencies.h classfile.h classdir.h classUtils.h 
	g++ -c ${CFLAGS} -c $<

classUtils.o: classUtils.cpp classUtils.h classfile.h classdir.h 
	g++ -c ${CFLAGS} -c $<

main.o: main.cpp classfile.h classdir.h dependencies.h classUtils.h 
	g++ -c ${CFLAGS} -c $<

classfile.o: classfile.cpp classfile.h classdir.h 
	g++ -c ${CFLAGS} -c $<

clean: 
	rm -f *.o 

project: classdir.o dependencies.o classUtils.o main.o classfile.o
	g++ ${CFLAGS} $^ -o project