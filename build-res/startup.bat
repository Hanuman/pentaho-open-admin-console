@echo off
call java -DCONSOLE_HOME=. -Dlog4j.configuration=resource/config/log4j.xml -jar lib/pentaho-open-admin-console.jar