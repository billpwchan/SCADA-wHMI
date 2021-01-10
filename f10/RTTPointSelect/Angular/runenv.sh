#!/bin/sh

CUR_PATH=`pwd -P`
export COTS_PATH=$CUR_PATH/../../../cots
export SOFTS_PATH=$COTS_PATH/softs.x86_64
export NODEJS=node-v6.11.0-win-x64
#export NODEJS=node-v8.1.3-win-x64
export NODEJS_HOME=$SOFTS_PATH/$NODEJS
export PATH=.:$NODEJS_HOME:$PATH
