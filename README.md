# lc3vm-java
This is a Java Gui Based implementation of LC-3 VM

It is an EXACT copy of the C-code by J. Meiners and R. Pendleton taken from their site: Write your Own Virtual Machine 
https://justinmeiners.github.io/lc3-vm/

Except the c code was converted to Java to simulate a gui based version of it.

Other notes:
Test with Java 8 on Windows 10 (64 bit). You will need Java 7 or 8, Ant 1.9+ or Exlipse for Java.

Status: See issues.

Edit tiny.vm.CPU.java file to change the <b>IMAGE_FOLDER</b> string so that it points to the folder containing the image 2048.obj </br>
e.g: public static final String IMAGE_FOLDER="c:/user/faiz/desktop/";

COMPILE instructions for windows if using ant: Open dos window and in the SAME window run the following commands:
[Please setup your java and ant home in 1.init.bat file FIRST!]

dos_prompt>cd c:\pathto\lc3vm-java\tiny-vm-C-version\ant </br>
dos_prompt>1.init </br>
dos_prompt>2.clean </br>
dos_prompt>3.compile </br></br>

RUN instructions for windows: </br>

dos_prompt>cd ../.. <br/>
dos_prompt>run

