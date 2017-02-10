#!/bin/bash

# . set_environment.sh

#export CLASSPATH=${PROJECT_HOME}/gemfire-server/target/gemfire-server-0.0.1-SNAPSHOT.jar:${PROJECT_HOME}/domain/target/domain-0.0.1-SNAPSHOT.jar

# copy to the cluster configuration service for deployment
#cp ../server/gemfire-functions/target/gemfire-functions-0.0.1-SNAPSHOT.jar locator1/cluster_config/cluster/
#cp ../lib/gemfire-functions/target/gemfire-functions-0.0.1-SNAPSHOT.jar locator1/cluster_config/cluster/

#DIR=$(PWD)
#APP_JARS=$DIR/lib/log4j-core-2.7.jar

# Issue commands to gfsh to start locator and launch a server
echo "Starting locator and server..."
gfsh <<!
connect

start locator --name=locator1 --port=10334 --properties-file=config/locator.properties --load-cluster-configuration-from-dir=true --initial-heap=256m --max-heap=256m

start server --name=server1 --redis-port=11211 --redis-bind-address=localhost --initial-heap=2g --max-heap=2g --J=-XX:+UseParNewGC --J=-XX:+UseConcMarkSweepGC --J=-XX:+UnlockDiagnosticVMOptions --J=-XX:ParGCCardsPerStrideChunk=32768 --properties-file=config/gemfire.properties

list members;
list regions;
!
