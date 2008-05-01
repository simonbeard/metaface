# Microsoft Developer Studio Project File - Name="MbrolaDLL" - Package Owner=<4>
# Microsoft Developer Studio Generated Build File, Format Version 6.00
# ** DO NOT EDIT **

# TARGTYPE "Win32 (x86) Dynamic-Link Library" 0x0102

CFG=MbrolaDLL - Win32 Debug
!MESSAGE This is not a valid makefile. To build this project using NMAKE,
!MESSAGE use the Export Makefile command and run
!MESSAGE 
!MESSAGE NMAKE /f "MbrolaDLL.mak".
!MESSAGE 
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "MbrolaDLL.mak" CFG="MbrolaDLL - Win32 Debug"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "MbrolaDLL - Win32 Release" (based on "Win32 (x86) Dynamic-Link Library")
!MESSAGE "MbrolaDLL - Win32 Debug" (based on "Win32 (x86) Dynamic-Link Library")
!MESSAGE 

# Begin Project
# PROP AllowPerConfigDependencies 0
# PROP Scc_ProjName ""
# PROP Scc_LocalPath ""
CPP=cl.exe
MTL=midl.exe
RSC=rc.exe

!IF  "$(CFG)" == "MbrolaDLL - Win32 Release"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 0
# PROP BASE Output_Dir "Release"
# PROP BASE Intermediate_Dir "Release"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 0
# PROP Output_Dir "Release"
# PROP Intermediate_Dir "Release"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /MT /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MBCS" /D "_USRDLL" /D "MBROLADLL_EXPORTS" /YX /FD /c
# ADD CPP /nologo /MLd /W3 /GX /O2 /X /I "." /I "..\\" /I "..\..\Libraries\Mbrola" /I "c:\progra~1\micros~3\vc98\include" /I "c:\j2sdk1.4.2_01\include" /I "c:\j2sdk1.4.2_01\include\win32" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MBCS" /D "_USRDLL" /D "MBROLADLL_EXPORTS" /D "JNI" /D "MBROLA_SUPPORT" /D "_DEBUG" /D "SYSTEM_IS_WIN32" /YX /FD /I /GZ /c
# ADD BASE MTL /nologo /D "NDEBUG" /mktyplib203 /win32
# ADD MTL /nologo /D "NDEBUG" /mktyplib203 /win32
# ADD BASE RSC /l 0x409 /d "NDEBUG"
# ADD RSC /l 0x409 /d "NDEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /dll /machine:I386
# ADD LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib  mbrplay.lib /nologo /dll /machine:I386 /out:"..\..\Libraries\MbrolaDLL.dll" /libpath:"..\..\Libraries\Mbrola"
# SUBTRACT LINK32 /pdb:none

!ELSEIF  "$(CFG)" == "MbrolaDLL - Win32 Debug"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "Debug"
# PROP BASE Intermediate_Dir "Debug"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "Debug"
# PROP Intermediate_Dir "Debug"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /MTd /W3 /Gm /GX /ZI /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_MBCS" /D "_USRDLL" /D "MBROLADLL_EXPORTS" /YX /FD /GZ /c
# ADD CPP /nologo /MTd /W3 /Gm /GX /ZI /Od /I "c:\progra~1\mbrola\mbrola~1\source\c\include" /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_MBCS" /D "_USRDLL" /D "MBROLADLL_EXPORTS" /YX /FD /GZ /c
# ADD BASE MTL /nologo /D "_DEBUG" /mktyplib203 /win32
# ADD MTL /nologo /D "_DEBUG" /mktyplib203 /win32
# ADD BASE RSC /l 0x409 /d "_DEBUG"
# ADD RSC /l 0x409 /d "_DEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /dll /debug /machine:I386 /pdbtype:sept
# ADD LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /dll /debug /machine:I386 /out:"c:\bcard\MbrolaDLL.dll" /pdbtype:sept

!ENDIF 

# Begin Target

# Name "MbrolaDLL - Win32 Release"
# Name "MbrolaDLL - Win32 Debug"
# Begin Group "Source Files"

# PROP Default_Filter "cpp;c;cxx;rc;def;r;odl;idl;hpj;bat"
# Begin Source File

SOURCE=.\mbrolajni.cpp

!IF  "$(CFG)" == "MbrolaDLL - Win32 Release"

# ADD CPP /I "c:\users\beardsw\source" /I "c:\j2sdk1.4.1_02\include" /I "c:\j2sdk1.4.1_02\include\win32"

!ELSEIF  "$(CFG)" == "MbrolaDLL - Win32 Debug"

# ADD CPP /I "c:\users\beardsw\metafacelatest" /I "c:\j2Sdk1.4.1_01\include" /I "c:\j2sdk1.4.1_01\include\win32" /I "c:\program files\mbrola tools\source\c\include"
# SUBTRACT CPP /I "c:\progra~1\mbrola\mbrola~1\source\c\include"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\mbrolawavjni.cpp

!IF  "$(CFG)" == "MbrolaDLL - Win32 Release"

!ELSEIF  "$(CFG)" == "MbrolaDLL - Win32 Debug"

# ADD CPP /I "c:\progra~1\mbrola~1\source\c\include"
# SUBTRACT CPP /I "c:\progra~1\mbrola\mbrola~1\source\c\include"

!ENDIF 

# End Source File
# End Group
# Begin Group "Header Files"

# PROP Default_Filter "h;hpp;hxx;hm;inl"
# End Group
# Begin Group "Resource Files"

# PROP Default_Filter "ico;cur;bmp;dlg;rc2;rct;bin;rgs;gif;jpg;jpeg;jpe"
# End Group
# End Target
# End Project
