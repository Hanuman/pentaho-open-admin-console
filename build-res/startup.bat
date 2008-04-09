@echo off

REM ***************************************
REM   BATCH SCRIPT TO START MGMG CONSOLE
REM ***************************************

if ""%1"" == ""un"" (set UN=%2)
if ""%1"" == ""pwd"" (SET PWD=%2)
if ""%3"" == ""un"" (set UN=%4)
if ""%3"" == ""pwd"" (SET PWD=%4)

if "%PWD%" == "" goto ret
if "%UN%" == "" goto ret

SET JAVA_PROPS= -Dpentaho.platform.username=%UN% -Dpentaho.platform.password=%PWD%


call java -jar %JAVA_PROPS% C:\EclipseWorkspace\pentaho-administration-console\dist\pentaho-admin-console\pentaho-admin-console\pentaho-adminstration-console.jar

goto end


:ret
echo "Invalid arguments! Usage: startup un=<USER_NAME> pwd=<PASSWORD>"

:end
