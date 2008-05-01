# Microsoft Developer Studio Project File - Name="SpemoSupport" - Package Owner=<4>
# Microsoft Developer Studio Generated Build File, Format Version 6.00
# ** DO NOT EDIT **

# TARGTYPE "Win32 (x86) Dynamic-Link Library" 0x0102

CFG=SPEMOSUPPORT - WIN32 FESTIVAL
!MESSAGE This is not a valid makefile. To build this project using NMAKE,
!MESSAGE use the Export Makefile command and run
!MESSAGE 
!MESSAGE NMAKE /f "SpemoSupport.mak".
!MESSAGE 
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "SpemoSupport.mak" CFG="SPEMOSUPPORT - WIN32 FESTIVAL"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "SpemoSupport - Win32 Festival" (based on "Win32 (x86) Dynamic-Link Library")
!MESSAGE 

# Begin Project
# PROP AllowPerConfigDependencies 0
# PROP Scc_ProjName ""
# PROP Scc_LocalPath ""
CPP=cl.exe
MTL=midl.exe
RSC=rc.exe
# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 0
# PROP BASE Output_Dir "SpemoSupport___Win32_Festival"
# PROP BASE Intermediate_Dir "SpemoSupport___Win32_Festival"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 0
# PROP Output_Dir "SpemoSupport___Win32_Festival"
# PROP Intermediate_Dir "SpemoSupport___Win32_Festival"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /MT /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MBCS" /D "_USRDLL" /D "SPEMOSUPPORT_EXPORTS" /YX /FD /c
# ADD CPP /nologo /MLd /W3 /GX /O2 /I "." /I "..\\" /I ".\Tags\\" /I ".\xml\\" /I "c:\j2sdk1.4.2_01\include" /I "c:\j2sdk1.4.2_01\include\win32" /I "..\..\Libraries\LibXML\include" /I "..\..\Libraries\Festival\include" /I "..\..\Libraries\EST\include" /I "..\..\Libraries\Canto\include" /D "JNI" /D "FESTIVAL_SUPPORT" /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_MBCS" /D "_USRDLL" /D "SPEMOSUPPORT_EXPORTS" /D "SYSTEM_IS_WIN32" /Fp"SpemoSupport___Win32_MBrola/SpemoSupport.pch" /YX /Fo"SpemoSupport___Win32_MBrola/" /Fd"SpemoSupport___Win32_MBrola/" /FD /I /GZ /c
# ADD BASE MTL /nologo /D "NDEBUG" /mktyplib203 /win32
# ADD MTL /nologo /D "NDEBUG" /mktyplib203 /win32
# ADD BASE RSC /l 0x409 /d "NDEBUG"
# ADD RSC /l 0x409 /d "NDEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /dll /machine:I386
# ADD LINK32 libxmld2.lib libFestival.lib libestools.lib libestbase.lib libeststring.lib wsock32.lib winmm.lib libcmtd.lib /nologo /dll /incremental:yes /pdb:"SpemoSupport___Win32_MBrola/MbrolaSupport.pdb" /debug /machine:I386 /nodefaultlib:"libcd" /nodefaultlib:"libc" /nodefaultlib:"libmsvcrtd" /out:"..\..\Libraries\FestivalDLL.dll" /implib:"SpemoSupport___Win32_MBrola/MbrolaSupport.lib" /pdbtype:sept /libpath:"..\..\Libraries\Festival\\" /libpath:"..\..\Libraries\LibXML\\" /libpath:"..\..\Libraries\EST\\"
# SUBTRACT LINK32 /pdb:none
# Begin Target

# Name "SpemoSupport - Win32 Festival"
# Begin Group "Source Files"

# PROP Default_Filter "cpp;c;cxx;rc;def;r;odl;idl;hpj;bat"
# Begin Source File

SOURCE=.\Tags\TTS_Angry.cpp
# End Source File
# Begin Source File

SOURCE=.\TTS_C_API.cpp
# End Source File
# Begin Source File

SOURCE=.\TTS_Central.cpp
# End Source File
# Begin Source File

SOURCE=.\TTS_DigitalSignalProcessor.cpp
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Embed.cpp
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Emotion.cpp
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Emph.cpp
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Happy.cpp
# End Source File
# Begin Source File

SOURCE=.\TTS_NaturalLanguageParser.cpp
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Pause.cpp
# End Source File
# Begin Source File

SOURCE=.\TTS_PhonemeInfo.cpp
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_PhonemeModifier.cpp
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Pitch.cpp
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Pron.cpp
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Rate.cpp
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Sad.cpp
# End Source File
# Begin Source File

SOURCE=.\TTS_SimpleInFile.cpp
# End Source File
# Begin Source File

SOURCE=.\TTS_SMLDocument.cpp
# End Source File
# Begin Source File

SOURCE=.\Filter\TTS_SMLFilter.cpp
# End Source File
# Begin Source File

SOURCE=.\TTS_SMLNode.cpp
# End Source File
# Begin Source File

SOURCE=.\TTS_SMLParser.cpp
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_SMLSimpleTag.cpp
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Speaker.cpp
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Stat.cpp
# End Source File
# Begin Source File

SOURCE=.\Filter\TTS_StringVector.cpp
# End Source File
# Begin Source File

SOURCE=.\Filter\TTS_TagsNode.cpp
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_TextModifier.cpp
# End Source File
# Begin Source File

SOURCE=.\TTS_UtteranceInfo.cpp
# End Source File
# Begin Source File

SOURCE=.\TTS_Visual.cpp
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Volume.cpp
# End Source File
# Begin Source File

SOURCE=.\TTS_WordInfo.cpp
# End Source File
# End Group
# Begin Group "Header Files"

# PROP Default_Filter "h;hpp;hxx;hm;inl"
# Begin Source File

SOURCE=.\MetaFaceApplet.h
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Angry.h
# End Source File
# Begin Source File

SOURCE=.\TTS_C_API.h
# End Source File
# Begin Source File

SOURCE=.\TTS_Central.h
# End Source File
# Begin Source File

SOURCE=.\TTS_DigitalSignalProcessor.h
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Embed.h
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Emotion.h
# End Source File
# Begin Source File

SOURCE=.\TTS_EmotionIDs.h
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Emph.h
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Happy.h
# End Source File
# Begin Source File

SOURCE=.\TTS_libxml_includes.h
# End Source File
# Begin Source File

SOURCE=.\TTS_MbrolaOptions.h
# End Source File
# Begin Source File

SOURCE=.\TTS_NaturalLanguageParser.h
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Pause.h
# End Source File
# Begin Source File

SOURCE=.\TTS_PhonemeInfo.h
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_PhonemeModifier.h
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Pitch.h
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Pron.h
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Rate.h
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Sad.h
# End Source File
# Begin Source File

SOURCE=.\TTS_SimpleInFile.h
# End Source File
# Begin Source File

SOURCE=.\TTS_SMLDocument.h
# End Source File
# Begin Source File

SOURCE=.\TTS_SMLException.h
# End Source File
# Begin Source File

SOURCE=.\Filter\TTS_SMLFilter.h
# End Source File
# Begin Source File

SOURCE=.\TTS_SMLNode.h
# End Source File
# Begin Source File

SOURCE=.\TTS_SMLParser.h
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_SMLSimpleTag.h
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Speaker.h
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Stat.h
# End Source File
# Begin Source File

SOURCE=.\TTS_stdPackage.h
# End Source File
# Begin Source File

SOURCE=.\Filter\TTS_StringVector.h
# End Source File
# Begin Source File

SOURCE=.\Filter\TTS_TagsNode.h
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_TextModifier.h
# End Source File
# Begin Source File

SOURCE=.\TTS_UtteranceInfo.h
# End Source File
# Begin Source File

SOURCE=.\TTS_Visual.h
# End Source File
# Begin Source File

SOURCE=.\Tags\TTS_Volume.h
# End Source File
# Begin Source File

SOURCE=.\TTS_WordInfo.h
# End Source File
# Begin Source File

SOURCE=.\xml\XML_Document.h
# End Source File
# Begin Source File

SOURCE=.\xml\XML_DocumentIterator.h
# End Source File
# Begin Source File

SOURCE=.\xml\XML_Exception.h
# End Source File
# Begin Source File

SOURCE=.\xml\XML_Node.h
# End Source File
# Begin Source File

SOURCE=.\xml\XML_Parser.h
# End Source File
# End Group
# Begin Group "Resource Files"

# PROP Default_Filter "ico;cur;bmp;dlg;rc2;rct;bin;rgs;gif;jpg;jpeg;jpe"
# End Group
# End Target
# End Project
