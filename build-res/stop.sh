#!/bin/sh

#---------------------------------#
# dynamically build the classpath #
#---------------------------------#

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
THE_CLASSPATH=".:./resource:./bin:./classes:./lib"
files=$(ls ./lib/*.jar)

for i in $files
do
  THE_CLASSPATH="$THE_CLASSPATH:$i"
done

$JAVA -Djava.io.tmpdir=temp -cp $THE_CLASSPATH org.pentaho.pac.server.StopJettyServer