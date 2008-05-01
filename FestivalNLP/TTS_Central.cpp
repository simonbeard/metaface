////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Central CLASS Implementation file
//
////////////////////////////////////////////////////////////////////////////////


#include "TTS_Central.h"

#define STRINGLENGTH (256)
#define NUMOFFAP (68)
#define EOF (-1)
#define ERROR (-2)
#define SET (2)
#define LOCK (3)
#define INTERP (4)
#define TRUE (1)
#define FALSE (0)
#define SUCCESS (1)

int CTTS_Central::getFAPFile (string &filename)
{
	return (SUCCESS);
}

int CTTS_Central::getWavFile(const char *pi,string &wavfile)
{
	FILE *mbfpr;
	pDSP = new CTTS_DigitalSignalProcessor;
	CTTS_MbrolaOptions *pOptions = new CTTS_MbrolaOptions;	

///	if (WaveFilename != NULL) //Put back in later
///	{
///		unlink (WaveFilename);
///		delete WaveFilename;
///	}

	/////////////////////////////////
	// Create temporary file
	string *pMbrolaFilename = MakeTempFilename ("TTS_Cntr_XXXXXX");
	mbfpr = fopen (pMbrolaFilename->c_str(),"w");
	fwrite (pi,sizeof(char),strlen(pi),mbfpr);
	fclose (mbfpr);

	/////////////////////////////////
	// Generate wave based on mbrola file
	WaveFilename = pDSP->GenerateWave (pMbrolaFilename->c_str(), pOptions);
	if (WaveFilename == NULL)
		return ERROR;
	wavfile = WaveFilename;
	delete pOptions;

	/////////////////////////////////
	// Cleanup temp files
	unlink (pMbrolaFilename->c_str());	
	delete pMbrolaFilename;	
	return (SUCCESS);
}

int CTTS_Central::getPhonemeInfo (string &pi)
{
	string *temppi;
	char line[1024];
	int i;

	if (smlDoc)
		{		
		CTTS_MbrolaOptions *pOptions = new CTTS_MbrolaOptions;
		pSectionNode = smlDoc->NextSection(pSectionNode);
		if (pSectionNode == NULL)
			return (EOF);
		if (pSectionNode != NULL)
			{
//			if (pSectionNode->IsNode ("embed"))
//				{			
//				CTTS_Embed *pTag = new CTTS_Embed;
//				pTag->ProcessTag (pSectionNode, NLP(), pDSP);
//				delete pTag;
//				}
//			else
//				{									

				/////////////////////////////////////////////////////
				// Modify TEXT according to embedded tags in the SML doc
				if (pTextModifier != NULL)
					pTextModifier->ProcessNode (pSectionNode);		

				/////////////////////////////////////////////////////
				// Translate text to phonemes using NLP
				pSectionNode->GetSectionPhonemes (m_pNLP);

				/////////////////////////////////////////////////////
				// Modify PHONEMES according to embedded tags in the SML doc
				if (pPhonemeModifier != NULL)
					pPhonemeModifier->ProcessNode (pSectionNode, pOptions);

//////////////////////////
	/////////////////////////////////
	// Create temporary filename
	//string *pMbrolaFilename = MakeTempFilename ("TTS_Cntr_XXXXXX");

	/////////////////////////////////
	// Output MBROLA phoneme file
 	//pSectionNode->OutputMbrolaPhonemeFile (pMbrolaFilename->c_str());    
	pSectionNode->OutputMbrolaPhonemeString (pi);
//////////////////////////

				//line = NULL;
/*				FILE *fp = fopen (pMbrolaFilename->c_str(),"r");
				
				temppi = new string ();				
				i = 1;
				while (i > 0)
				{	
					i = fread (line,sizeof(char),1000,fp);					
					if (i < 1000)
						line[i] = '\0';		
					else
						line[1000] = '\0';
//if (i>0) {fprintf (stdout,"line: %s",line);}
//fprintf (stdout,"\n");					
					temppi->append(line);
				}				
				fclose (fp);
				pi = temppi->c_str();
//fprintf (stdout,"phonemes:\n%s",pi);

///				unlink (pMbrolaFilename->c_str()); //put back later
///				delete pMbrolaFilename;
*/
				/////////////////////////////////
				// Output MBROLA phoneme file
				//pSectionNode->OutputMbrolaPhonemeString (pi);								

				delete pOptions;
//				}
			}
		}
	return (SUCCESS);
}

void CTTS_Central::closeSMLFile ()
{
	if (WaveFilename != NULL)
	{
//		unlink (WaveFilename);
//		delete WaveFilename;
	}
	if (pDSP)
		delete pDSP;
	if (pTextModifier)
		delete pTextModifier;
	if (pPhonemeModifier)
		delete pPhonemeModifier;
	if (smlDoc)
		delete smlDoc;
}

int CTTS_Central::openSMLFile (const char *filename)
{
	string SMLFilename = filename;
	string InputFilename = filename;	
	bool IsPlainFile = false;
	CTTS_SMLParser *pSMLParser;	
	pDSP = new CTTS_DigitalSignalProcessor;
	pTextModifier = new CTTS_TextModifier;
	pPhonemeModifier = new CTTS_PhonemeModifier;		

/*	if (!IsXMLFile (SMLFilename))
		{
		// File is a plain text file.  Therefore, need to put
		// plain text inside basic SML body.
		WrapPlainFileInSML (SMLFilename);		// SMLFilename is changed.
		IsPlainFile = true;
		InputFilename = SMLFilename;
		}
		
	// Need to filter all unknown tags from file.  A temporary
	// file is created, input file is left intact.
	string *pTempFilename = MakeTempFilename ("TTS_Cntl_FltXXXXXX");
	Filter()->FilterFile (SMLFilename.c_str(), pTempFilename->c_str());
	SMLFilename = pTempFilename->c_str();
	delete pTempFilename;*/
	
	try
		{
		/////////////////////////////////
		// Create Parser object
		pSMLParser = new CTTS_SMLParser (SMLFilename.c_str());
		if (pSMLParser == 0)
			throw "new () failed for CTTS_SMLParser object";

		/////////////////////////////////
		// Parse SML document
		if (pSMLParser)
		{
			smlDoc = pSMLParser->ParseFile ();
			delete pSMLParser;
		}
		unlink (SMLFilename.c_str());
		if (IsPlainFile)		
			unlink (InputFilename.c_str());
		}

	/////////////////////////////////////////
	// Exception Handling
	/////////////////////////////////////////
	catch (CTTS_SMLException e)
		{
//		cerr << e.GiveInfo () << endl;

		// If parser error occurred, then have to execute
		// recovery procedures:
		//	 a) Debug level - SML DOM tree is constructed
		//		containing an error message.
		//	 b)	User level - All tags are stripped, try to 
		//		re-parse simplified text?  If error still
		//		occurs, report "Sorry, I do not know the 
		//		answer to that question" (give debug
		//		warning!)
		smlDoc = NULL;
		// If SML document doesn't exist, then report
		//	a)	File not found at debug level
		//	b)	"Sorry, don't know answer to that question."
		return (ERROR);
		}

	catch (char *ErrorMsg)
		{
//		cerr << ErrorMsg << endl;
		return (ERROR);
		}

	catch (...)
		{
//		cerr << "Unknown error occurred." << endl;
		return (ERROR);
		}

	if (smlDoc != NULL)
	{
		pSectionNode = smlDoc->Root();
		return (SUCCESS);
	}
	return (ERROR);
}




/////////////////////////////////////////////////////////////////////////////////
// Initialise
//
// Performs initialisation of the member variable m_pNLP, which is the Natural
// Language Parser.  This function is called directly by the C API function
// TTS_Initialise (), and indirectly whenever a C++ CTTS_Central object is 
// instantiated.
/////////////////////////////////////////////////////////////////////////////////
void CTTS_Central::Initialise ()
{
	/////////////////////////////////
	// Create NaturalLanguageParser object
	try
		{
		cout << "TTS: Initializing module...\n";
		m_pNLP = new CTTS_NaturalLanguageParser;
		if (!m_pNLP)
			throw "Error in CTTSCentral::Initialise (): new() failed for CTTS_NaturalLanguageParser object.";
		m_pFilter = new CTTS_SMLFilter (m_TagNamesFilename.c_str());
		if (!m_pFilter)
			throw "Error in CTTSCentral::Initialise (): new() failed for CTTS_SMLFilter object";
		cout << "done\n"; cout.flush();
		}

	/////////////////////////////////
	// Error handling
	catch (char *ErrorMsg)
		{
		cerr << ErrorMsg << endl;
		}

	catch (...)
		{
		cerr << "Unknown error occurred." << endl;
		}
}

////////////////////////////////////////////////////////////////////////////////////
// SpeakFromFile
//
// A TTS API function.  Used to synthesize an input file (specified by input parameter
// Filename).  The input file does not need to be marked up in SML since the input file
// is inspected to see if it conforms to the structure of an SML document.  If it isn't,
// then appropriate steps are taken to wrap the contents of the file within the structure
// of an SML document.
////////////////////////////////////////////////////////////////////////////////////
void CTTS_Central::SpeakFromFile (const char *Filename)
{
	assert (Filename != NULL);

	string SMLFilename = Filename;
	string InputFilename = Filename;
	bool IsPlainFile = false;

	if (!IsXMLFile (SMLFilename))
		{
		// File is a plain text file.  Therefore, need to put
		// plain text inside basic SML body.
		WrapPlainFileInSML (SMLFilename);		// SMLFilename is changed.
		IsPlainFile = true;
		InputFilename = SMLFilename;
		}
	
	
	// Need to filter all unknown tags from file.  A temporary
	// file is created, input file is left intact.
	string *pTempFilename = MakeTempFilename ("TTS_Cntl_FltXXXXXX");
	Filter()->FilterFile (SMLFilename.c_str(), pTempFilename->c_str());
	SMLFilename = pTempFilename->c_str();
	delete pTempFilename;

#ifdef FAITH
	/////////////////////////////////////////////////
	// Initialise GST module
	GST_global_initialise (InputFilename.c_str());
#endif

	// Do main synthesis
	Synthesize (SMLFilename.c_str());

	unlink (SMLFilename.c_str());
	if (IsPlainFile)
		unlink (InputFilename.c_str());


#ifdef FAITH
	/////////////////////////////////////////////////
	// Destroy GST module
	GST_global_destroy ();
#endif
}

////////////////////////////////////////////////////////////////////////////////////
// IsXMLFile
//
// This function checks to see if the file specified by Filename is an XML file
// or not.  A simple check is done to see if the first line contains the xml
// declaration required for an xml file (it does not parse the whole file!).  The
// first line of the file should have something similar to:
//		<?xml version="1.0"?>
// If the first line of the file holds this string, then IsXMLFile returns true,
// otherwise false is returned.
////////////////////////////////////////////////////////////////////////////////////
bool CTTS_Central::IsXMLFile (string &Filename)
{
	CTTS_SimpleInFile InFile;

	if (!InFile.Open (Filename.c_str()))
		{
		Filename = FILE_NOT_FOUND_FILENAME;
		if (!InFile.Open (Filename.c_str()))
			{
			cerr << "Error in CTTS_Central::IsXMLFile (): Could not open specified filename, and could not open SorryNoFileMsg.xml either\n";
			exit (1);
			}
		}

	string FirstLine;
	char *temp = InFile.GetLine (500, '\n');
	if (temp != NULL)
		{
		FirstLine = temp;
		delete temp;
		}

	InFile.Close ();

	if (FirstLine != m_XMLFirstLine)
		{
		return false;
		}
	else
		{
		return true;
		}
}

////////////////////////////////////////////////////////////////////////////////////
// SpeakText
//
// An API function for TTS module.  Accepts and synthesizes a string of characters
// held in input parameter Message.  In order not to replicate code, another TTS API
// function, SpeakFromFile (), is called.  Therefore, the contents of Message is
// output to a temporary file.  The filename of the temporary file is then given to
// SpeakFromFile () and synthesis is done.  Message does not necessarily be plain
// text, and can include tags.
////////////////////////////////////////////////////////////////////////////////////
void CTTS_Central::SpeakText (const char *Message)
{	
	assert (Message != NULL);

	// Create a temporary file.
	ofstream OutFile;
	string *pFilename = MakeTempFilename ("TTS_Cntl_TmpXXXXXX");

	OutFile.open (pFilename->c_str());

	if (!OutFile)
		{
		cerr << "Error in CTTS_Central::SpeakText (): could not open file for writing" << endl;
		exit (1);
		}

	// Output contents of Message to OutFile.
	OutFile << Message;
	OutFile.close ();
	
	// Call main TTS API function to synthesize contents of Message.
	SpeakFromFile (pFilename->c_str());

	// Cleanup
	unlink (pFilename->c_str());
	delete pFilename;
}

////////////////////////////////////////////////////////////////////////////////////
// WrapPlainFileInSML
//
// Once a file is detected to not contain the appropriate struture of an SML 
// document, this function is called to wrap the filename's contents within
// the structure of an SML document.
// The input file is not touched, but rather a temporary file is created.
// The temporary file will have the following contents:
//		<?xml version="1.0"?>
//		<!DOCTYPE sml SYSTEM "./sml-v01.dtd">
//		<sml>
//			<p>
//				<neutral>
//
//					--- Contents of original input file ---
//
//				</neutral>
//			</p>
//		</sml>
//
// For efficiency purposes, the contents of the original input file will be broken
// up into smaller paragraph sections, so that multiple <p>...</p> tags will exist
// within the SML document.  A new paragraph section is created whenever two
// newline characters ("\n\n") are encountered in the original input file.
////////////////////////////////////////////////////////////////////////////////////
void CTTS_Central::WrapPlainFileInSML (string &Filename)
{
	CTTS_SimpleInFile InFile;
	ofstream OutFile;

	string *pWrapperFilename = MakeTempFilename ("TTS_Cntl_WrXXXXXX");
	OutFile.open (pWrapperFilename->c_str());

	/////////////////////////////////
	// Open input and output files
	if (!InFile.Open (Filename.c_str()))
		{
		cerr << "Error in CTTS_Central::WrapPlainFileInSML (): Cannot open file \"" << Filename.c_str() <<"\" for reading" << endl;
		exit (1);
		}

	if (!OutFile)
		{
		cerr << "Error in CTTS_Central::WrapPlainFileInSML (): could not open file for writing" << endl;
		exit (1);
		}

	//////////////////////////////////////////////////////////////////////
	// Write SML file contents.
	// Want to start a new sml section when paragraphs are
	// separated by blank lines.  However, a new section is only created if
	// the previous section contains at least 4 lines.

	OutFile << WRAPPER_START;
	OutFile << m_OpenPara.c_str();

	char *pLine;
	bool CanStartNewSection = false;
	int LinesInLastPara = 0;
	while ((pLine = InFile.GetLine (2000, '\n')) != NULL)
		{
		if (IsWhiteSpace (pLine))
			{
			CanStartNewSection = true;
			}
		else
			{
			if (CanStartNewSection)
				{
				if (LinesInLastPara > 4)
					{
					OutFile << m_ClosePara.c_str() << m_OpenPara.c_str();
					LinesInLastPara = 0;
					}
				CanStartNewSection = false;
				}

			OutFile << pLine << "\n";
			LinesInLastPara++;
			}

		delete pLine;
		}

	OutFile << m_ClosePara.c_str();
	OutFile << WRAPPER_END;

	/////////////////////////////////
	// Cleanup
	OutFile.close ();
	InFile.Close ();

	// Make TTS now point to the newly made wrapper file
	Filename = pWrapperFilename->c_str();
	delete (pWrapperFilename);
}

////////////////////////////////////////////////////////////////////////////////////
// Synthesize
//
// Main function that synthesizes an input file.  It is assumed that the input file
// conforms to the tight structure of an SML document.
////////////////////////////////////////////////////////////////////////////////////
void CTTS_Central::Synthesize (const char *Filename)
{
	assert (Filename != NULL);

	CTTS_SMLParser *pSMLParser;
	CTTS_SMLDocument *pDoc;

	try
		{
		/////////////////////////////////
		// Create Parser object
		pSMLParser = new CTTS_SMLParser (Filename);
		if (pSMLParser == 0)
			throw "new () failed for CTTS_SMLParser object";

		/////////////////////////////////
		// Parse SML document
		cout << "Parsing...\n";	cout.flush ();
		pDoc = pSMLParser->ParseFile ();
		}

	/////////////////////////////////////////
	// Exception Handling
	/////////////////////////////////////////
	catch (CTTS_SMLException e)
		{
		cerr << e.GiveInfo () << endl;

		// If parser error occurred, then have to execute
		// recovery procedures:
		//	 a) Debug level - SML DOM tree is constructed
		//		containing an error message.
		//	 b)	User level - All tags are stripped, try to 
		//		re-parse simplified text?  If error still
		//		occurs, report "Sorry, I do not know the 
		//		answer to that question" (give debug
		//		warning!)
		pDoc = NULL;
		// If SML document doesn't exist, then report
		//	a)	File not found at debug level
		//	b)	"Sorry, don't know answer to that question."
		}

	catch (char *ErrorMsg)
		{
		cerr << ErrorMsg << endl;
		}

	catch (...)
		{
		cerr << "Unknown error occurred." << endl;
		}

	
	///////////////////////////////////////////
	// Synthesize document, section by section
	if (pDoc)
		{
		cout << "Synthesizing document...\n";
		CTTS_DigitalSignalProcessor *pDSP = new CTTS_DigitalSignalProcessor;
		CTTS_TextModifier *pTextModifier = new CTTS_TextModifier;
		CTTS_PhonemeModifier *pPhonemeModifier = new CTTS_PhonemeModifier;
		CTTS_SMLNode *pSectionNode = pDoc->Root();

		while ((pSectionNode = pDoc->NextSection (pSectionNode)) != NULL)
			{
			if (pSectionNode->IsNode ("embed"))
				{
				cout << "Processing embedded file...\n"; cout.flush ();
				CTTS_Embed *pTag = new CTTS_Embed;
				pTag->ProcessTag (pSectionNode, NLP(), pDSP);
				delete pTag;
				}
			else
				{
				cout << "Synthesizing section...\n"; cout.flush ();
				SynthesizeSection (pSectionNode, pDSP, pTextModifier, pPhonemeModifier);
				}
			}

		//////////////////////////////
		// cleanup
		delete pDSP;
		delete pTextModifier;
		delete pPhonemeModifier;
		}

	/////////////////////////////////
	// clean up
	if (pDoc)
		delete pDoc;

	if (pSMLParser)
		delete pSMLParser;
}

////////////////////////////////////////////////////////////////////////////////////
// SynthesizeSection
//
////////////////////////////////////////////////////////////////////////////////////
void CTTS_Central::SynthesizeSection (CTTS_SMLNode *pSectionNode, CTTS_DigitalSignalProcessor *pDSP, CTTS_TextModifier *pTextModifier, CTTS_PhonemeModifier *pPhonemeModifier)
{
	assert ((pSectionNode != NULL) && (pDSP != NULL));

	CTTS_MbrolaOptions *pOptions = new CTTS_MbrolaOptions;
	CTTS_Speaker Speaker;

	/////////////////////////////////////////////////////
	// Modify TEXT according to embedded tags in the SML doc
	if (pTextModifier != NULL)
		pTextModifier->ProcessNode (pSectionNode);

	/////////////////////////////////////////////////////
	// Translate text to phonemes using NLP
	pSectionNode->GetSectionPhonemes (m_pNLP);

	/////////////////////////////////////////////////////
	// Modify PHONEMES according to embedded tags in the SML doc
	if (pPhonemeModifier != NULL)
		pPhonemeModifier->ProcessNode (pSectionNode, pOptions);

	/////////////////////////////////
	// Create temporary filename
	string *pMbrolaFilename = MakeTempFilename ("TTS_Cntr_XXXXXX");

	/////////////////////////////////
	// Output MBROLA phoneme file
	pSectionNode->OutputMbrolaPhonemeFile (pMbrolaFilename->c_str());

	/////////////////////////////////
	// Generate wave based on mbrola file
	char *WaveFilename = pDSP->GenerateWave (pMbrolaFilename->c_str(), pOptions);
	delete pOptions;

#ifdef FAITH
	/////////////////////////////////////////////////////
	// Generate faps and send them plus wave over MPEG4
	SendUtteranceOverMPEG4 (pSectionNode, WaveFilename, pMbrolaFilename->c_str());

#else
	/////////////////////////////////
	// Play the wave file
	pDSP->PlayWave (WaveFilename);
#endif

	/////////////////////////////////
	// Cleanup temp files
	unlink (pMbrolaFilename->c_str());
//	unlink (WaveFilename);
	delete pMbrolaFilename;
//	delete WaveFilename;
}


/////////////////////////////////////////////////////////////////////////////////
// SendUtteranceOverMPEG4
// 
// This function prepares some files that are necessary for the TTS module to
// be used with the Talking Head.
//
//		MBROLA_Utterance	- a string containing the utterance's phoneme data in
//							MBROLA format.  The string is used later by other functions
//							to determine the equivalent visemes so that the Talking
//							Head can move its mouth accordingly.
//		UtteranceText		- plain text of what is actually being spoken.  (Contains
//							no markup whatsoever.)
//		GSTFilename			- A file is created for the GST module.  It contains word
//							and phoneme informaton.
/////////////////////////////////////////////////////////////////////////////////
void CTTS_Central::SendUtteranceOverMPEG4 (CTTS_SMLNode *pSection, char *WaveFilename, const char *MbrolaFilename)
{
	assert ((pSection != NULL) && (WaveFilename != NULL));

	string MBROLA_Utterance;
	string UtteranceText;

	pSection->OutputMbrolaPhonemeString (MBROLA_Utterance);

	if (pSection->Children())
		pSection->Children()->GetPlainText (UtteranceText);

	/////////////////////////////////
	// Output GST word/phoneme file
	string GSTFilename = string(MbrolaFilename) + ".gst";
	pSection->OutputGSTPhonemeFile (GSTFilename.c_str());

	CTTS_Visual Visual;
	Visual.SendVisualData (WaveFilename, MBROLA_Utterance.c_str(), UtteranceText.c_str(), GSTFilename.c_str(), NULL);

	// Cleanup

	//***********************************************
	//** QUOC TEMP MODIFICATION 5th October 2000	*
	//*												*
	//   unlink (GSTFilename.c_str());				*
	//*												*
	//***********************************************
}

/////////////////////////////////////////////////////////////////////////////////
// IsWhiteSpace
//
// Returns true if character string Str contains only whitespace.  Whitespace
// is defined as ' ', '\n', and '\t', and any other character that the standard
// C function isspace () defines as being whitespace.  If Str contains at least
// one non-whitespace character, then the function returns false.
/////////////////////////////////////////////////////////////////////////////////
bool CTTS_Central::IsWhiteSpace (const char *Str)
{
	assert (Str!= NULL);

	bool WS = true;
	int Length = strlen (Str);

	if (Length > 0)
		{
		for (int i=0; i < Length; i++)
			{
			if (!isspace (Str[Length]))
				{
				WS = false;
				break;
				}
			}
		}

	return WS;
}

/////////////////////////////////////////////////////////////////////////
// MakeTempFilename
//
// Returns a pointer to a string object containing a temporary filename
// based on the input Basename.  Basename should NOT include a path since
// MakeTempFilename provides a path already (depending on the system).
// Also, it is assumed that Basename gives a character string that ends with 
// six X's.  
//		e.g. Basename = "MyTempFile_XXXXXX"
/////////////////////////////////////////////////////////////////////////
string *CTTS_Central::MakeTempFilename (const char *Basename)
{
#ifdef __TTS_WIN98__
	string *pFilename = new string ("C:\\temp\\");
#else
#	ifdef WIN32
		string *pFilename = new string ("C:/temp/");
#	else
		string *pFilename = new string ("/tmp/");
#	endif
#endif

	pFilename->append (Basename);
	char Temp[200];

	int i;
	for (i=0; i < pFilename->size(); i++)
		{
		Temp[i] = pFilename->at (i);
		}

	Temp[i] = '\0';

	mktemp (Temp);

	i = pFilename->size() - 1;
	for (int j=0; j < 6; j++)
		{
		pFilename->at(i) = Temp[i];
		i--;
		}

	return pFilename;
}
