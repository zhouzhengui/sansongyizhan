#!/bin/bash


JARFILE=/home/App.jar
LOGPATH=/home/logs
LOG=/home/logs/suninggift.log
if [ ! -d $LOGPATH ];then
   mkdir -p $LOGPATH
fi

java -jar -Xms512M -Xmx1024M -Xmn128M -Dskywalking.agent.namespace=tdev -Dskywalking.agent.service_name=tdev-suninggift -javaagent:/home/skywalking-agent-plugins/agent-prod-bj/skywalking-agent.jar -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/home/suninggift.dump  $JARFILE --spring.profiles.active=$ACTIVE  >> $LOG &

tail  -50f $LOG
