#!/bin/sh

cd $(dirname $0)
DIR=$PWD
cd -

. "$DIR/set-pentaho-java.sh"

if [ -d "$DIR/../biserver-ce/jre" ]; then
  setPentahoJava "$DIR/../biserver-ce/jre"
else 
  setPentahoJava
fi

#---------------------------------#
# dynamically build the classpath #
#---------------------------------#

THE_CLASSPATH=".:./resource:./bin:./classes:./lib"
files=$(ls ./lib/*.jar)

for i in $files
do
  THE_CLASSPATH="$THE_CLASSPATH:$i"
done

"$_PENTAHO_JAVA" -Djava.io.tmpdir=temp -cp $THE_CLASSPATH org.pentaho.pac.server.StopJettyServer