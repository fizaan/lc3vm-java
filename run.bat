@echo off

set JAVA_HOME=C:\Users\Alifa\Desktop\mystuff\faiz.dev.root\java8
set path=%JAVA_HOME%\bin;

java -cp %cd%\tiny-vm-C-version\bin; tiny.vm.CPU

set /p=Press any key to exit.