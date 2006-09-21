
Currently Supported:
     - Currently, only building from the free Borland C++ compiler is supported. 
        It can be downloaded from the net. However, it can also be compiled using
        MSVC compiler.

Prerequisites to compilation
     - You must have a JDK installed. JAVA_HOME must point to where it is installed.
     - You must have the free Borland C++ compiler. Download at

                http://www.borland.com/downloads/download_cbuilder.html

     - Create a bcc32.cfg file in <BORLAND INSTALLATION DIR>\bin with the following
        lines 
	-I"<BORLAND INSTALLATION DIR>\include"
                -L"<BORLAND INSTALLATION DIR>\lib;<INSTALLATION DIR>\Lib\PSDK"
     
     - Add <BORLAND INSTALLATION DIR>\bin  to your PATH environment variable.

To compile

(1) Go to 
         	<CHECK-OUT DIR>\livetribe-jmxcpu\src\main\cpp\win32

(2) Execute 

           	make -f Makefile.bcc

      This makefile will create the DLL and put it in
           	<CHECK-OUT DIR>\livetribe-jmxcpu\src\main\resources

To clean

(1) Execute

	make -f Makefile.bcc clean

TODO :
       	- Add support for building with tracers on.
	- Clean code.