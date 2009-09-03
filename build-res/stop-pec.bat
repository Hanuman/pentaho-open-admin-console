@echo off

REM ***************************************
REM   BATCH SCRIPT TO STOP MGMG CONSOLE
REM ***************************************
set PATH=%path%
set CLASSPATH=.;./resource;./bin;./classes;./lib;

FOR %%F IN (lib\*.jar) DO call :updateClassPath %%F

goto :startjava

:updateClassPath
set CLASSPATH=%CLASSPATH%;%1
goto :eof

:startjava
echo %CLASSPATH%
java -Djava.io.tmpdir=temp -cp %CLASSPATH% org.pentaho.pac.server.StopJettyServer
