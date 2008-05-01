////////////////////////////////////////////////////////////////////////////////
//
// CTTS_NaturalLanguageParser CLASS Implementation file.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_NaturalLanguageParser.h"
#include <fstream>

////////////////////////////////////////////////////////////////////////////////
// CharsToWords
// 
// Parameters:
//		Text - string of characters to tokenize
//
// Returns:
//		A character string containing words separated by spaces.  The importance
//	  of this function is that if the input text contains abbreviations, or
//	  dates, numbers, currency etc., these are all expanded into their proper
//	  words.
//
// Algorithm:
//		Makes use of festival's natural language parser to do the dirty work.
//	  Therefore, the proper scheme commands are generated.  The words are stored
//    in an Utterance structure which is output to a temporary file.  The file
//	  is read in and the output character string is generated.
//
// Festival scheme commands:
//		1. (set! utt1 (Utterance Text "This is an example"))	; create utterance utt1
//		2. (Initialize utt)										; initialise
//		3. (Text utt)											; Tokenize text
//		4. (Token_POS utt)
//		5. (Token utt)
//		6. (utt.save.words UTT filename)						; save words to file
//		
////////////////////////////////////////////////////////////////////////////////
char *CTTS_NaturalLanguageParser::CharsToWords (const char *Text)
{
#ifdef __TTS_WIN98__
	char TempFilenameTemplate[] = "C:/temp/TTS_NLP_XXXXXX";
#else
#	ifdef WIN32
		char TempFilenameTemplate[] = "C:/temp/TTS_NLP_XXXXXX";
#	else
		char TempFilenameTemplate[] = "/tmp/TTS_NLP_XXXXXX";
#	endif
#endif
	char *TempFilename = mktemp (TempFilenameTemplate);

	//////////////////////////////////////////
	// Generate scheme commands
	////////////////////////////////////////
	string SET_UTTERANCE_VARIABLE = "(set! utt1 (Utterance Text \"";
	SET_UTTERANCE_VARIABLE.append (Text);
	SET_UTTERANCE_VARIABLE.append ("\"))");
	const char *INITIALIZE_utt = "(Initialize utt1)";
	const char *TEXT_utt = "(Text utt1)";
	const char *TOKEN_POS_utt = "(Token_POS utt1)";
	const char *TOKEN_utt = "(Token utt1)";

	// might think about removing these commands, and just filtering
	// out all characters that are pauses (e.g. '.', ',', '!')
	const char *POS_utt = "(POS utt1)";
	const char *PHRASIFY_utt = "(Phrasify utt1)";
	const char *WORD_utt = "(Word utt1)";
	const char *PAUSES_utt = "(Pauses utt1)";
	string SAVE_WORDS = string ("(utt.save.words utt1 \"") + TempFilename + "\")";

#ifdef FAITH
	//*******************************************************************************
	// QUOC - MODIFICATION 5th October 2000											*
	//																				*
	   const char *GST_run = "(GST_process_scheme utt1)";						  //*
	//																				*
	//*******************************************************************************
#endif

	/////////////////////////////////////////
	// Run the scheme commands
	// festival_eval_command () returns FALSE
	// if scheme command was unsuccessful.
	////////////////////////////////////////
	char *Words;

	try
		{
		if (!festival_eval_command (SET_UTTERANCE_VARIABLE.c_str()))
			throw SET_UTTERANCE_VARIABLE.c_str();
		if (!festival_eval_command (INITIALIZE_utt))
			throw INITIALIZE_utt;
		if (!festival_eval_command (TEXT_utt))
			throw TEXT_utt;
		if (!festival_eval_command (TOKEN_POS_utt))
			throw TOKEN_POS_utt;
		if (!festival_eval_command (TOKEN_utt))
			throw TOKEN_utt;
		if (!festival_eval_command (POS_utt))
			throw POS_utt;
		if (!festival_eval_command (PHRASIFY_utt))
			throw PHRASIFY_utt;
		if (!festival_eval_command (WORD_utt))
			throw WORD_utt;
		if (!festival_eval_command (PAUSES_utt))
			throw PAUSES_utt;
		if (!festival_eval_command (SAVE_WORDS.c_str()))
			throw SAVE_WORDS.c_str();


#ifdef FAITH
		//***************************************************
		// QUOC - TEMP MODIFICATION 5th October 2000		*
		//													*
		if (!festival_eval_command (GST_run))			  //*
			throw GST_run;								  //*
		//													*
		//***************************************************
#endif


		////////////////////////////////////////
		// Read in the words from the temp file,
		// then delete temporary file.
		Words = GetWords (TempFilename);
		unlink (TempFilename);
		}

	////////////////////////////////////////
	// Error Handler
	// Report failed scheme command and 
	// return string as "error"
	////////////////////////////////////////
	catch (const char *FailedSchemeCommand)
		{
		cerr << "CTTS_NaturalLanguageParser::CharsToWords(): failed near Scheme command: " << FailedSchemeCommand << endl;
		Words = strdup ("error");
		}

	catch (...)
		{
		cerr << "CTTS_NaturalLanguageParser::CharsToWords(): Unknown error occured" << endl;
		Words = strdup ("error");
		}

	return Words;
}

////////////////////////////////////////////////////////////////////////////////
// Get Words
//
////////////////////////////////////////////////////////////////////////////////
char *CTTS_NaturalLanguageParser::GetWords (const char *FileName)
{
	using std::ifstream;

	ifstream WordFile (FileName);		// open input word file for reading
	string SingleWord;
	string Words;
	float temp;

	// check if file opened successfully
	if (!WordFile)
		{
		cerr << "Error: Could not open " << FileName << endl;
		exit (8);
		}

	// first line of file always contains '#' symbol.
	WordFile >> SingleWord;

	// read in contents and keeping appending to Words character string
	int i = 0;
	while (!WordFile.eof())
		{
		// Read info
		SingleWord="";
		WordFile >> temp >> temp >> SingleWord;

		// skip line if it was empty
		if (SingleWord.length() == 0)
			continue;

		// Append to Words character string
		Words += SingleWord + " ";
		}

	// close input file
	WordFile.close ();

	char *pWords = strdup (Words.c_str());

	// return Words list
	return pWords;
}

////////////////////////////////////////////////////////////////////////////////
// GeneratePhonemes
//
// Synthesizes an character string to produce phoneme and timing information.
// At the moment, a "Text" type is synthesized.  For efficiency, this could be
// upgraded to "Words" type.  Also, the actual synthesis steps are being called
// as functions instead of one "(utt.synth UTT)" because the latter produces a
// waveform, which is not what we require, and therefore is doing more work than
// is required.
//
// Returns 
//		A pointer to a character string giving the base of the two files that
//	are output: a "wrd" file, containing word and word boundary information,
//  and a "mbr" file, which contains phoneme information in MBROLA's format.
//
// Festival scheme commands:
//		1. (set! utt1 (Utterance Text "This is an example"))    ; create utterance utt1
//		2. Synthesize utterance:
//				(Initialize utt)
//				(Text utt)
//				(Token_POS utt)
//				(Token utt)
//				(POS utt)
//				(Phrasify utt)
//				(Word utt)
//				(Pauses utt)
//				(Intonation utt)
//				(PostLex utt)
//				(Duration utt)
//				(Int_Targets utt)
//		4. (save_segments_mbrola utt1 "test.mbr")				; save utterance in mbrola format
//		5. (utt.save.words UTT filename)						; save words in mbrola format
//
////////////////////////////////////////////////////////////////////////////////
char *CTTS_NaturalLanguageParser::GeneratePhonemes (const char *Text)
{
#ifdef __TTS_WIN98__
	char TempBasename[] = "C:/temp/TTS_NLP_XXXXXX";
#else
#	ifdef WIN32
		char TempBasename[] = "C:/temp/TTS_NLP_XXXXXX";
#	else
		char TempBasename[] = "/tmp/TTS_NLP_XXXXXX";
#	endif
#endif
	mktemp (TempBasename);

	//////////////////////////////////////////
	// Generate scheme commands
	////////////////////////////////////////
	string SET_UTTERANCE_VARIABLE = "(set! utt1 (Utterance Text \"";
	SET_UTTERANCE_VARIABLE.append (Text);
	SET_UTTERANCE_VARIABLE.append ("\"))");

	const char *INITIALIZE_utt	= "(Initialize utt1)";
	const char *TEXT_utt		= "(Text utt1)";
	const char *TOKEN_POS_utt	= "(Token_POS utt1)";
	const char *TOKEN_utt		= "(Token utt1)";
	const char *POS_utt			= "(POS utt1)";
	const char *PHRASIFY_utt	= "(Phrasify utt1)";
	const char *WORD_utt		= "(Word utt1)";
	const char *PAUSES_utt		= "(Pauses utt1)";
	const char *INTONATION_utt	= "(Intonation utt1)";
	const char *POSTLEX_utt		= "(PostLex utt1)";
	const char *DURATION_utt	= "(Duration utt1)";
	const char *INT_TARGETS_utt	= "(Int_Targets utt1)";


#ifdef FAITH
	//*******************************************************************************
	// QUOC - MODIFICATION 5th October 2000											*
	//																				*
	   const char *GST_run = "(GST_process_scheme utt1)";						  //*
	//																				*
	//*******************************************************************************
#endif

	string SAVE_WORDS = string ("(utt.save.words utt1 \"") + TempBasename + ".wrd\")";
	string SAVE_MBROLA_FORMAT = string ("(save_segments_mbrola utt1 \"") + TempBasename + ".mbr\")";


	/////////////////////////////////////////
	// Run the scheme commands
	// festival_eval_command () returns FALSE
	// if scheme command was unsuccessful.
	////////////////////////////////////////
	char *Filename;

	try
		{
		// Set utterance
		if (!festival_eval_command (SET_UTTERANCE_VARIABLE.c_str()))
			throw SET_UTTERANCE_VARIABLE.c_str();

		// Synthesize utterance (except don't generate waveform)
		if (!festival_eval_command (INITIALIZE_utt))
			throw INITIALIZE_utt;
		if (!festival_eval_command (TEXT_utt))
			throw TEXT_utt;
		if (!festival_eval_command (TOKEN_POS_utt))
			throw TOKEN_POS_utt;
		if (!festival_eval_command (TOKEN_utt))
			throw TOKEN_utt;
		if (!festival_eval_command (POS_utt))
			throw POS_utt;
		if (!festival_eval_command (PHRASIFY_utt))
			throw PHRASIFY_utt;
		if (!festival_eval_command (WORD_utt))
			throw WORD_utt;
		if (!festival_eval_command (PAUSES_utt))
			throw PAUSES_utt;
		if (!festival_eval_command (INTONATION_utt))
			throw INTONATION_utt;
		if (!festival_eval_command (POSTLEX_utt))
			throw POSTLEX_utt;
		if (!festival_eval_command (DURATION_utt))
			throw DURATION_utt;
		if (!festival_eval_command (INT_TARGETS_utt))
			throw INT_TARGETS_utt;

		// Save information to "mbr" and "wrd" files.
		if (!festival_eval_command (SAVE_MBROLA_FORMAT.c_str()))
			throw SAVE_MBROLA_FORMAT.c_str();
		if (!festival_eval_command (SAVE_WORDS.c_str()))
			throw SAVE_WORDS.c_str();


#ifdef FAITH
		//***************************************************
		// QUOC - TEMP MODIFICATION 5th October 2000		*
		//													*
		if (!festival_eval_command (GST_run))			  //*
			throw GST_run;								  //*
		//													*
		//***************************************************
#endif


		Filename = strdup (TempBasename);
		}

	////////////////////////////////////////
	// Error Handler
	// Report failed scheme command and 
	// return NULL;
	////////////////////////////////////////
	catch (const char *FailedSchemeCommand)
		{
		cerr << "CTTS_NaturalLanguageParser::GeneratePhonemes(): failed near Scheme command: " << FailedSchemeCommand << endl;
		Filename = 0;
		}

	catch (...)
		{
		cerr << "CTTS_NaturalLanguageParser::GeneratePhonemes(): Unknown error occured" << endl;
		Filename = 0;
		}

	return Filename;
}
