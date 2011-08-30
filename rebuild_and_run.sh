#!/bin/bash
CONFIG_FILE="config.xml"
LIB_DIR="lib"
BUILD_DIR="build"

CS_RUN_CLASS="org.cluenet.clueservices.core.ClueServices"

# Actual paths
cd "$(dirname $0)"
BASE_DIR="$(pwd)"
CS_CONFIG_PATH="$BASE_DIR/$CONFIG_FILE"

JAVA_CPATH=$CLASSPATH""$BASE_DIR"/"$BUILD_DIR"/:"$BASE_DIR"/"$LIB_DIR"/*"
[ ! -z "$CLASSPATH" ] && JAVA_CPATH=$CLASSPATH":"$JAVA_CPATH

# Commands
CS_BUILD_COMMAND="ant"
CS_RUN_COMMAND="java -cp '$JAVA_CPATH' $CS_RUN_CLASS '$CS_CONFIG_PATH'"

# Run stuff
eval $CS_BUILD_COMMAND && eval $CS_RUN_COMMAND
