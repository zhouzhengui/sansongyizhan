#!/bin/bash


JARFILE=/home/App.jar
LOGPATH=/home/logs
LOG=/home/logs/suninggift.log
if [ ! -d $LOGPATH ];then
   mkdir -p $LOGPATH
fi

java -jar -Xms512M -Xmx1024M -Dskywalking.agent.namespace=prod -Dskywalking.agent.service_name=prod-suninggift -javaagent:/home/skywalking-agent-plugins/agent-prod-bj/skywalking-agent.jar  $JARFILE --spring.profiles.active=$ACTIVE  >> $LOG &

tail  -50f $LOG
