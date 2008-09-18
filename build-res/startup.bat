@echo off
call java -Xmx512M -XX:PermSize=64M -XX:MaxPermSize=128M -DCONSOLE_HOME=. -Dlog4j.configuration=resource/config/log4j.xml -jar lib/pentaho-open-admin-console.jar
