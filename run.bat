@echo off

set JAVA_HOME=C:\<YOUR_JAVA_HOME_PATH>
set path=%JAVA_HOME%\bin;

java -cp %cd%\bin; tiny.vm.CPU

set /p=Press any key to exit.