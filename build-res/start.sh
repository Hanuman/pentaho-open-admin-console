#!/bin/sh

S1="x$JAVA"
S2="x"
if [ $S1=$S2 ]; then
  S1="x$JAVA_HOME"
  if [ $S1=$S2 ]; then
    JAVA="java"
  else 
    JAVA="$JAVA_HOME/bin/java"
  fi
fi

CLASSPATH=resource/config

files=$(ls ./lib/*.jar)

for i in $files
do
  CLASSPATH="$CLASSPATH:$i"
done

$JAVA -Xmx512M -XX:PermSize=64M -XX:MaxPermSize=128M  -DCONSOLE_HOME=. -Dlog4j.configuration=resource/config/log4j.xml -cp $CLASSPATH  org.pentaho.pac.server.JettyServer
