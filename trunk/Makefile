# MetaFace Framework Windows Makefile (for gmake)
# Type "gmake all" to make all

# Define tool locations (these may need to be changed)
JAVABASE=c:\j2sdk1.4.2_01
JAVATOOLS=$(JAVABASE)\bin

# Define tools
CC=$(JAVATOOLS)\javac
JAVA=$(JAVATOOLS)\java
JAR=$(JAVATOOLS)\jar
JAVADOC=$(JAVATOOLS)\javadoc
KEYTOOL=$(JAVATOOLS)\keytool
JARSIGN=$(JAVATOOLS)\jarsigner
JAVAH=$(JAVATOOLS)\javah

# Define flags for tools
JAVACFLAGS=-O
JARFLAGS=cvf
JAVADOCFLAGS=-private -use -version -author

# Define jar locations (these may need to be changed)
JAWS=c:\progra~1\java\j2re1.4.1_01\lib\jaws.jar

# Define details for signing JAR files
PASSWORD=metaface
ALIAS=Simon_Beard
DETAILS="cn=Simon Beard www.vhml.org, ou=School of Computing, o=Curtin University of Technology, c=aus"
VALIDDAYS=360
KEYSTORE=keystore.$$

# Output MetaFace Framework as JAR packages
IM=InteractionManager.jar
EMBOD=Embodiment.jar
CLIENT=Client.jar
SERVER=Server.jar
MPEG4=MPEG4.jar
DSP=DSP.jar
NLP=NLP.jar
PRESYNTH=PreSynthesise.jar

JARLIB=./$(DSP);./$(IM);./$(EMBOD)./$(NLP);./$(MPEG4);./$(CLIENT);./$(SERVER)

# Output documentation to directory
DOCUMENTATION=javadoc
DEVDOCUMENTATION=javadoc_dev

# Objects
IMOBJ = metaface/interaction/ActiveState.class\
metaface/interaction/BestListItem.class\
metaface/interaction/BestList.class\
metaface/interaction/DefaultTopic.class\
metaface/interaction/DMTLDebugger.class\
metaface/interaction/DMTLMemoryState.class\
metaface/interaction/DMTLErrorHandler.class\
metaface/interaction/DMTLTagReader.class\
metaface/interaction/DMTLParser.class\
metaface/interaction/EntryState.class\
metaface/interaction/Evaluate.class\
metaface/interaction/InteractionContext.class\
metaface/interaction/InteractionManager.class\
metaface/interaction/KnowledgeBase.class\
metaface/interaction/LinkedState.class\
metaface/interaction/Macros.class\
metaface/interaction/NextState.class\
metaface/interaction/PreState.class\
metaface/interaction/Response.class\
metaface/interaction/Signal.class\
metaface/interaction/State.class\
metaface/interaction/Stimulus.class\
metaface/interaction/SubTopic.class\
metaface/interaction/Topic.class\
metaface/interaction/WordChain.class\
metaface/interaction/WordGraphNode.class\
metaface/interaction/WordGraph.class

MPEG4OBJ = metaface/mpeg4/ExpressionData.class\
metaface/mpeg4/FAPMembers.class\
metaface/mpeg4/LowLevelFAPData.class\
metaface/mpeg4/VisemeData.class\
metaface/mpeg4/FAPData.class\
metaface/mpeg4/FAPUData.class

DSPOBJ = metaface/dsp/DigitalSignalProcessor.class\
metaface/dsp/MetaFaceMbrola.class

PRESYNTHOBJ = metaface/presynthesise/PreSynthesise.class

NLPOBJ = metaface/nlp/NaturalLanguageProcessor.class\
metaface/nlp/MetaFaceFestival.class

CLIENTOBJ = metaface/client/DefaultMetaFaceApplet.class\
metaface/client/DefaultMetaFaceClient.class\
metaface/client/HTTPClient.class\
metaface/client/HTTPListener.class\
metaface/client/MetaFaceApplet.class\
metaface/client/MetaFaceClient.class\
metaface/client/MetaFaceHTTPClient.class\
metaface/client/MetaFaceIndependentClient.class\
metaface/client/TextControl.class\
metaface/client/VHMLOutputTagReader.class\
metaface/client/VHMLOutputErrorHandler.class\
metaface/client/VHMLOutputAccess.class\
metaface/client/VHMLOutputReader.class

SERVEROBJ = metaface/server/HTTPServerListener.class\
metaface/server/HTTPServerWorker.class\
metaface/server/MetaFaceHTTPServer.class\
metaface/server/MetaFaceDefaultServer.class\
metaface/server/MetaFaceService.class\
metaface/server/MetaFaceStaticService.class\
metaface/server/MetaFaceDynamicService.class\
metaface/server/Worker.class\
metaface/server/WorkerThread.class\
metaface/server/Pool.class\
metaface/server/VHMLSpeechElement.class\
metaface/server/VHMLFAElement.class\
metaface/server/VHMLReader.class\
metaface/server/VHMLErrorHandler.class\
metaface/server/VHMLParser.class\
metaface/server/VHMLSemantics.class\
metaface/server/Worker.class

EMBODOBJ = metaface/embodiment/DrawingArea.class\
metaface/embodiment/MPEG4Face.class\
metaface/embodiment/MPEG4ExpressionCoordinates.class\
metaface/embodiment/GirlMPEG4Face.class\
metaface/embodiment/SpikyMPEG4Face.class\
metaface/embodiment/SpeechBubble.class\
metaface/embodiment/IdleBehaviour.class

# Put all class files into JAR
# Generate any JNI header files
interaction : $(IMOBJ)	
	$(JAR) cvmf metaface/interaction/main.txt $(IM) $+
	echo make interaction manager complete

embodiment : $(EMBODOBJ)
	$(JAR) cvmf metaface/embodiment/main.txt $(EMBOD) $+
	echo make embodiment complete

mpeg4 : $(MPEG4OBJ)
	$(JAR) $(JARFLAGS) $(MPEG4) $+
	echo make mpeg4 complete

dsp : $(DSPOBJ)
	$(JAR) $(JARFLAGS) $(DSP) $+
	$(JAVAH) -jni -verbose -classpath ./$(DSP) metaface.dsp.MetaFaceMbrola
	echo make dsp complete	
	
nlp : $(NLPOBJ)
	$(JAR) $(JARFLAGS) $(NLP) $+
	$(JAVAH) -jni -verbose -classpath ./$(NLP) metaface.nlp.MetaFaceFestival	
	echo make nlp complete
	
client : $(CLIENTOBJ)
	$(JAR) cvmf metaface/client/main.txt $(CLIENT) $+		
	echo make client complete
	
server : $(SERVEROBJ)
	$(JAR) cvmf metaface/server/main.txt $(SERVER) $+			
	echo make server complete

presynth : $(PRESYNTHOBJ)
	$(JAR) cvmf metaface/presynthesise/main.txt $(PRESYNTH) $+			
	echo make presynthesise complete		

jars : interaction\
mpeg4\
dsp\
nlp\
embodiment\
client\
server\
presynth
	
documentation :	
	$(JAVADOC) -d $(DOCUMENTATION) -classpath $(EMBOD);$(IM);$(CLIENT);$(MPEG4);$(DSP);$(JAWS);$(PRESYNTH);$(SERVER);$(NLP) -sourcepath $(SOURCE) metaface/interaction/*.java metaface/server/*.java metaface/client/*.java metaface/dsp/*.java metaface/nlp/*.java metaface/mpeg4/*.java metaface/embodiment/*.java metaface/presynthesise/*.java -private -use -version -author -linkoffline http://java.sun.com/j2se/1.4.2/docs/api . -linksource -breakiterator
	$(JAVADOC) -d $(DEVDOCUMENTATION) -classpath $(EMBOD);$(IM);$(CLIENT);$(MPEG4);$(DSP);$(JAWS);$(PRESYNTH);$(SERVER);$(NLP) -sourcepath $(SOURCE) metaface/interaction/*.java metaface/server/*.java metaface/client/*.java metaface/dsp/*.java metaface/nlp/*.java metaface/mpeg4/*.java metaface/embodiment/*.java metaface/presynthesise/*.java -use -version -author -protected -linkoffline http://java.sun.com/j2se/1.4.2/docs/api . -linksource -breakiterator
	echo documentation complete

export :
	copy *.jar ..\libraries	

all : jars\
export\
documentation

# Compile all java files
%.class:%.java
	$(CC) -classpath .;$(JAWS);$(JARLIB) $(JAVACFLAGS) $+	

# Clean
# Delete all class files
clean :
	del *.jar
	del *.h
	del metaface\\interaction\\*.class
	del metaface\\embodiment\\*.class
	del metaface\\server\\*.class
	del metaface\\mpeg4\\*.class
	del metaface\\dsp\\*.class
	del metaface\\nlp\\*.class
	del metaface\\client\\*.class
	del $(KEYSTORE)
	echo clean complete 

# End of makefile