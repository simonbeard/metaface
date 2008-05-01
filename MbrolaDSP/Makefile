# Define tools
CC=c:\Program Files\Microsoft Visual Studio\VC98\bin\cl
DLL=c:\Program Files\Microsoft Visual Studio\VC98\bin\lib

# Define path for tools (MS Visual C)
MSVCPATH=.;c:\Program Files\Microsoft Visual Studio\Common\MSDev98\Bin;c:\Program Files\Microsoft Visual Studio\VC98\bin

# Define include path
INCLUDE=../../
MSINCLUDE=/Progra~1/Micros~3/vc98/include
JNIINCLUDE=/j2sdk1.4.2_01/include
JNIWIN32INCLUDE=/j2sdk1.4.2_01/include/win32

mbrolajni :
	set path=%path%;$(MSVCPATH)
	$(CC) mbrolajni.cpp /I$(INCLUDE) /I$(MSINCLUDE) /I$(JNIINCLUDE) /I$(JNIWIN32INCLUDE)
	$(CC) mbrolawavjni.cpp /I$(INCLUDE) /I$(MSINCLUDE) /I$(JNIINCLUDE) /I$(JNIWIN32INCLUDE)
	echo make complete 

clean :
	del *.o
	echo clean complete 

# End of makefile

