#!/bin/bash

if [ $# != 1 ]; then
    echo "Enter version"    
    exit 1
fi

rm build.log > /dev/null 2>&1 
rm -r dfs.db > /dev/null 2>&1 
rm -r dfs-client-$1 > /dev/null 2>&1 


echo 'Starting build...' &&

echo 'Building serverlibrary...' &&
# Build serverlibrary
pushd dfs-serverlibrary >> build.log 2>&1 &&
ant jar >> ../build.log 2>&1 &&
popd >> build.log 2>&1 &&

echo 'Building agent...' &&
# Build agent
pushd dfs-agent >> build.log 2>&1 &&
ant build -Dversion=$1 >> ../build.log 2>&1 &&
popd >> build.log 2>&1 &&

echo 'Building server...' &&
# Build server
pushd dfs-server >> build.log 2>&1 &&
svn export dfs.db/ ../dfs.db >> ../build.log 2>&1 &&
ant build -Dversion=$1 >> ../build.log 2>&1 &&
popd >> build.log 2>&1 &&

echo 'Building client...' &&
# Build client
svn export dfs-client dfs-client-$1 >> build.log 2>&1 &&
rm dfs-client-$1/docs/TODO >> build.log 2>&1 &&
cp dfs-client-$1/docs/README dfs-client-$1/README.TXT >> build.log 2>&1 &&
tar -czf dfs-client-$1.tar.gz dfs-client-$1 >> build.log 2>&1 &&
rm dfs-client-$1 -r >> build.log 2>&1 &&

echo &&
echo "---          BUILD SUCCESSFUL          ---" &&
echo "---   for more details see build.log   ---" ||
echo "---   BUILD FAIL, see 'build.log' for more details   ---"
