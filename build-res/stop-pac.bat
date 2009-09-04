@echo off

REM ***************************************
REM   BATCH SCRIPT TO STOP ADMIN CONSOLE
REM ***************************************

setlocal

set PATH=%path%
set CLASSPATH=.;./resource;./bin;./classes;./lib;

FOR %%F IN (lib\*.jar) DO call :updateClassPath %%F

goto :startjava

:updateClassPath
set CLASSPATH=%CLASSPATH%;%1
goto :eof

:startjava

if exist "%~dp0..\biserver-ce\jre" call "%~dp0set-pentaho-java.bat" "%~dp0..\biserver-ce\jre"
if not exist "%~dp0..\biserver-ce\jre" call "%~dp0set-pentaho-java.bat"

"%_PENTAHO_JAVA%" -Djava.io.tmpdir=temp -cp %CLASSPATH% org.pentaho.pac.server.StopJettyServer
