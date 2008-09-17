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
$JAVA -DCONSOLE_HOME=. -jar lib/pentaho-open-admin-console.jar
