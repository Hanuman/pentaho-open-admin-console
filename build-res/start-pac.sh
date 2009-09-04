#!/bin/sh

cd $(dirname $0)
DIR=$PWD
cd -

. $DIR/set-pentaho-java.sh

if [ -d $DIR/../biserver-ce/jre ]; then
  setPentahoJava $DIR/../biserver-ce/jre
else 
  setPentahoJava
fi

CLASSPATH=.:resource/config:
files=$(ls ./jdbc/*.jar ./lib/*.jar)

for i in $files
do
  CLASSPATH="$CLASSPATH:$i"
done

$_PENTAHO_JAVA -Xmx512M -XX:PermSize=64M -XX:MaxPermSize=128M  -DCONSOLE_HOME=. -Dlog4j.configuration=resource/config/log4j.xml -cp $CLASSPATH  org.pentaho.pac.server.JettyServer
