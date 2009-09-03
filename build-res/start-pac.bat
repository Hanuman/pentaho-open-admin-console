@echo off
REM ***************************************
REM   BATCH SCRIPT TO START ADMIN CONSOLE
REM ***************************************
set CLASSPATH=.;resource\config
FOR %%F IN (lib\*.jar) DO call :updateClassPath %%F
FOR %%F IN (jdbc\*.jar) DO call :updateClassPath %%F

goto :startjava

:updateClassPath
set CLASSPATH=%CLASSPATH%;%1
goto :eof

:startjava

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
call %PENTAHO_JAVA% -Xmx512M -XX:PermSize=64M -XX:MaxPermSize=128M -DCONSOLE_HOME=. -Dlog4j.configuration=resource/config/log4j.xml -cp %CLASSPATH%  org.pentaho.pac.server.JettyServer
