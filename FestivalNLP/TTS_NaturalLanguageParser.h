////////////////////////////////////////////////////////////////////////////////
//
// CTTS_NaturalLanguageParser CLASS Header file.
//
// TO DO:
//		Allow only one instance of this class to be created at one time.
// 
// NOTE:
//		File "speech_tools/includes/EST_Val.h" has been modified.  Inline
// function called "string" has been put in a namespace called "EST_String_Namespace
// so that it doesn't conflict with the string type that is being used in this
// program. 
////////////////////////////////////////////////////////////////////////////////
#ifndef __CTTS_NATURAL_LANGUAGE_PARSER_H__
#define __CTTS_NATURAL_LANGUAGE_PARSER_H__

#include <iostream.h>
#include <stdio.h>
#include "TTS_stdPackage.h"

#include <festival.h>

class CTTS_NaturalLanguageParser
{
private:
	const int FESTIVAL_HEAP_SIZE;	     		// Default scheme heap size
	const int FESTIVAL_LOAD_INIT_FILES;			// We want the festival init files loaded

protected:
	char *GetWords (const char *Filename);
	inline void Initialise ();

public:
	CTTS_NaturalLanguageParser () 
		: FESTIVAL_HEAP_SIZE (210000), FESTIVAL_LOAD_INIT_FILES (1)		// initializer list
		{
		Initialise ();
		};

	~CTTS_NaturalLanguageParser ()
		{
		};

	inline void SayText (const char *);

	char *CharsToWords (const char *Text);

	char *GeneratePhonemes (const char *Text);
};

///////////////////////////////////////////////////////////////
// INLINE functions
// 
///////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// Initialise
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_NaturalLanguageParser::Initialise ()
{
	try 
		{
		// Initialise Festival
		festival_initialize (FESTIVAL_LOAD_INIT_FILES, FESTIVAL_HEAP_SIZE);
		// Load appropriate scheme library files
		const char *LOAD_MBROLA_SCM = "(load (string-append libdir \"/mbrola.scm\"))";
		if (!festival_eval_command (LOAD_MBROLA_SCM))
			throw LOAD_MBROLA_SCM;

	#ifdef FAITH
		//*****************************************************************************
		// QUOC - MODIFICATION 5th October 2000										  *
		//																			  *
		const char *GST_load = "(load (string-append libdir \"GST_scheme.scm\"))";	//*
		if (!festival_eval_command (GST_load))										//*
			throw GST_load;															//*
		//																			  *
		//*****************************************************************************
	#endif

		}

	////////////////////////////////////////
	// Error Handler
	// Report failed scheme command 
	////////////////////////////////////////
	catch (const char *FailedSchemeCommand)
		{
		cerr << "CTTS_NaturalLanguageParser::Initialise(): failed near Scheme command: " << FailedSchemeCommand << endl;
		exit (8);
		}

	catch (...)
		{
		cerr << "CTTS_NaturalLanguageParser::Initialise(): Unknown error occured." << endl;
		exit (8);
		}
}

////////////////////////////////////////////////////////////////////////////////
// SayText
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_NaturalLanguageParser::SayText (const char *Text)
{
	cout << "Saying text: " << Text << endl;

	// Festival function
	festival_say_text ("Please work");
}


#endif
