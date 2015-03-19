#! /bin/bash

if [ $# != 2 ]; then 
	echo "Usage: $0 [Number of agents] [Port for the first agent]" 
	exit 1 
fi 

AGENT_NUMBER=$1
START_AGENT_PORT=$2

rm forkill.pids

pushd ../pseudoClient/ > /dev/null
	ant jar > /dev/null 2>&1
	echo build pseudoClient
popd > /dev/null

pushd ../../dfs-agent/ > /dev/null
	for (( c=1; c<=$AGENT_NUMBER; c++ )) 
	do
		let num=$START_AGENT_PORT+$c-1
		SER=lib/myState$c.ser
		LOG=log/main$c.log
		echo "port = $num" > temp$c 
		echo "logPath = $LOG" >> temp$c
		echo "pathToSerialize = $SER" >> temp$c
		echo "freeSpace = 1073741824" >> temp$c
		if [ -f $SER ]; then
			rm $SER
		fi
		if [ -f $LOG ]; then
			rm $LOG
		fi
		java -jar dist/dfs-agent.jar temp$c > /dev/null 2>&1 &
		PID[$c]=$!
		echo ${PID[$c]} >> ../dfs-server/testrequest/forkill.pids
		echo launch agent number $c in the background process PID = ${PID[$c]}
	done
popd > /dev/null

sleep 1

pushd ../ > /dev/null
	ant run > /dev/null 2>&1 &
	PID_SERVER=$!
	echo $PID_SERVER >> ./testrequest/forkill.pids
	echo launch server in the background process PID = $PID_SERVER
popd > /dev/null

sleep 10

exit 0 
