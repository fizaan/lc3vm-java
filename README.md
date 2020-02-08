# lc3vm-java
This is a Java Gui Based implementation of LC-3 VM

It is an EXACT copy of the C-code by J. Meiners and R. Pendleton taken from their site: Write your Own Virtual Machine 
https://justinmeiners.github.io/lc3-vm/

Except the c code was converted to Java to simulate a gui based version of it.

Other notes:
Test with Java 8 Windows 10 (64 bit).

Status: Not completely working as of Feb 07 2020.

COMPILE INSTRUCTIONS:
Goto the ant folder inside 'tiny-vm-C-version'. Run batch files steps 1-3.

To run: edit the run.bat file to point to your JAVA_HOME and then run the batch file. Also edit the string "IMAGE_FOLDER" inside tiny.vm.CPU.java class to point to the 2048.obj image

