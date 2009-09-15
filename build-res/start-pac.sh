#!/bin/sh

DIR_REL=`dirname $0`
cd $DIR_REL
DIR=`pwd`
cd -

. "$DIR/set-pentaho-java.sh"

if [ -d "$DIR/../biserver-ce/jre" ]; then
  setPentahoJava "$DIR/../biserver-ce/jre"
else 
  setPentahoJava
fi

CLASSPATH=`$DIR_REL:resource/config:`
files=`ls $DIR_REL/jdbc/*.jar $DIR_REL/lib/*.jar`

for i in $files
do
  CLASSPATH="$CLASSPATH:$i"
done

"$_PENTAHO_JAVA" -Xmx512M -XX:PermSize=64M -XX:MaxPermSize=128M  -DCONSOLE_HOME=$DIR_REL -Dfile.encoding="UTF-8" -Dlog4j.configuration=resource/config/log4j.xml -cp $CLASSPATH  org.pentaho.pac.server.JettyServer
