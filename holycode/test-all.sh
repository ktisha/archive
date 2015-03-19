#!/bin/bash

echo 'Starting test...'

rm test.log > /dev/null 2>&1 
rm dfs-client/Makefile >> /dev/null 2>&1


echo 'Testing serverlibrary...' &&
# Test serverlibrary
pushd dfs-serverlibrary >> test.log 2>&1 &&
ant >> ../test.log 2>&1 &&
ant jar >> ../test.log 2>&1 &&
popd >> test.log 2>&1 &&

echo 'Testing agent...' &&
# Test agent
pushd dfs-agent >> test.log 2>&1 &&
ant >> ../test.log 2>&1 &&
ant jar >> ../test.log 2>&1 &&
popd >> test.log 2>&1 &&

echo 'Testing server...' &&
# Test server
pushd dfs-server >> test.log 2>&1 &&
ant >> ../test.log 2>&1 &&
ant jar >> ../test.log 2>&1 &&
popd >> test.log 2>&1 &&

echo 'Integrate testing...' &&
# Integarate testing server
pushd dfs-server/testrequest >> test.log 2>&1 &&
./serverRequestTester.py >> ../../test.log 2>&1 &&
popd >> test.log 2>&1 &&

echo 'Testing client...' &&
# Test client
pushd dfs-client >> test.log 2>&1 &&
qmake "DEFINES += TESTS" >> ../test.log 2>&1 &&
make clean >> ../test.log 2>&1 &&
make >> ../test.log 2>&1 &&
./dfs-client >> ../test.log 2>&1 &&
popd >> test.log 2>&1 &&

echo &&
echo "---          TEST SUCCESSFUL          ---" &&
echo "---   for more details see test.log   ---" ||

echo "---   TEST FAIL, see 'test.log' for more details   ---"
