@echo off

REM ***************************************
REM   BATCH SCRIPT TO STOP ADMIN CONSOLE
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

if defined JAVA_HOME goto use_java_home
if defined JRE_HOME goto use_jre_home
pushd ..\biserver-ce
set PENTAHO_PATH=%CD%\
popd
set PENTAHO_JAVA="%PENTAHO_PATH%jre\bin\java"
goto pac

:use_java_home
set PENTAHO_JAVA="%JAVA_HOME%\bin\java"
goto pac

:use_jre_home
set PENTAHO_JAVA="%JRE_HOME%\bin\java"
goto pac

:pac
java -Djava.io.tmpdir=temp -cp %CLASSPATH% org.pentaho.pac.server.StopJettyServer
