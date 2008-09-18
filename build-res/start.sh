#!/bin/sh

echo "JAVA_HOME set to $JAVA_HOME"

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

echo "JAVA is $JAVA"
$JAVA -Xmx512M -XX:PermSize=64M -XX:MaxPermSize=128M  -DCONSOLE_HOME=. -Dlog4j.configuration=resource/config/log4j.xml -jar lib/pentaho-open-admin-console.jar
