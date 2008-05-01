////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Central CLASS Header file.
//
////////////////////////////////////////////////////////////////////////////////

#ifdef __cplusplus

#ifndef __CTTS_CENTRAL_H__
#define __CTTS_CENTRAL_H__

#include "TTS_EmotionIDs.h"
#include "TTS_SMLParser.h"
#include "TTS_SMLDocument.h"
#include "TTS_DigitalSignalProcessor.h"
#include "TTS_NaturalLanguageParser.h"
#include "Filter/TTS_SMLFilter.h"
#include "TTS_Visual.h"

#include "Tags/TTS_TextModifier.h"
#include "Tags/TTS_PhonemeModifier.h"
#include "Tags/TTS_Speaker.h"
#include "Tags/TTS_Embed.h"
#include "TTS_MbrolaOptions.h"

#ifdef FAITH
#include "../GST/filter/GST_API.h"
#endif

#include <iostream.h>
#include <stdio.h>
#include <assert.h>

static char *WaveFilename=NULL;
static CTTS_SMLDocument *smlDoc;
static CTTS_SMLNode *pSectionNode;
static CTTS_DigitalSignalProcessor *pDSP;
static CTTS_TextModifier *pTextModifier;
static CTTS_PhonemeModifier *pPhonemeModifier;

class CTTS_Central
{
private:
	const char *WRAPPER_START;
	const char *WRAPPER_END;
	string m_OpenPara;
	string m_ClosePara;
	const char *FILE_NOT_FOUND_FILENAME;
	string m_XMLFirstLine;
	CTTS_NaturalLanguageParser *m_pNLP;
	CTTS_SMLFilter *m_pFilter;
	string m_TagNamesFilename;
	string m_MSGDir;
	string m_RCDir;

	void WrapPlainFileInSML (string &Filename);
	bool IsXMLFile (string &Filename);
	void Synthesize (const char *Filename);
	void SynthesizeSection (CTTS_SMLNode *pSectionNode, CTTS_DigitalSignalProcessor *pDSP, CTTS_TextModifier *pTextModifier, CTTS_PhonemeModifier *pPhonemeModifier);
	void SendUtteranceOverMPEG4 (CTTS_SMLNode *pSection, char *WaveFilename, const char *MbrolaFilename);
	bool IsWhiteSpace (const char *Str);
	string *MakeTempFilename (const char *Basename);
	inline void SetDefaultEmotion (int Emotion);

public:
	CTTS_Central ()
		{
		WRAPPER_START = "<?xml version=\"1.0\"?>\n<!DOCTYPE sml SYSTEM \"./sml-v01.dtd\">\n\n<sml>\n";
		WRAPPER_END = "\n</sml>";
		m_XMLFirstLine = "<?xml version=\"1.0\"?>";
		SetDefaultEmotion (TTS_NEUTRAL);
		InitialiseFilenames ();
		Initialise ();
		};

	~CTTS_Central ()
		{
		Destroy ();		
		};

	int getFAPFile (string &filename);
	int getWavFile(const char *str,string &wavdata);
	int getPhonemeInfo (string &pi);
	void closeSMLFile ();
	int openSMLFile (const char *filename);
	void SpeakFromFile (const char *Filename);
	void SpeakText (const char *Message);
	inline void SpeakTextEx (const char *Message, int Emotion);

	void Initialise ();
	inline void Destroy ();

	inline void InitialiseFilenames ();
	inline CTTS_NaturalLanguageParser *NLP ();
	inline CTTS_SMLFilter *Filter ();	
};

///////////////////////////////////////////////////////////////
// INLINE functions
// 
///////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// SpeakTextEx
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_Central::SpeakTextEx (const char *Message, int Emotion)
{
	assert ((Message != NULL) && (Emotion >= 0));

	SetDefaultEmotion (Emotion);
	SpeakText (Message);
}

////////////////////////////////////////////////////////////////////////////////
// SetDefaultEmotion
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_Central::SetDefaultEmotion (int Emotion)
{
	string EmotionString;

	switch (Emotion)
		{
		case TTS_NEUTRAL:	EmotionString = "neutral";
							break;

		case TTS_ANGRY: 	EmotionString = "angry";
							break;

		case TTS_HAPPY:		EmotionString = "happy";
							break;

		case TTS_SAD:		EmotionString = "sad";
							break;

		default:			EmotionString = "neutral";
							break;
				
		}

	m_OpenPara = string ("<p><") + EmotionString + ">\n";
	m_ClosePara = string ("</") + EmotionString + "></p>\n";
}

////////////////////////////////////////////////////////////////////////////////
// NLP
//
////////////////////////////////////////////////////////////////////////////////
inline CTTS_NaturalLanguageParser *CTTS_Central::NLP ()
{
	return m_pNLP;
}

////////////////////////////////////////////////////////////////////////////////
// Filter
//
////////////////////////////////////////////////////////////////////////////////
inline CTTS_SMLFilter *CTTS_Central::Filter ()
{
	return m_pFilter;
}

////////////////////////////////////////////////////////////////////////////////
// Destroy
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_Central::Destroy ()
{
	delete m_pNLP;
	delete m_pFilter;
}

////////////////////////////////////////////////////////////////////////////////
// InitialiseFilenames
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_Central::InitialiseFilenames ()
{
#ifdef __TTS_WIN98__
	FILE_NOT_FOUND_FILENAME = ".\\SorryNoFileMsg.sml";
	m_TagNamesFilename = ".\\tag-names.xml";
#else
	#ifdef WIN32
		FILE_NOT_FOUND_FILENAME = "./SorryNoFileMsg.sml";
		m_TagNamesFilename = "./tag-names.xml";
	#else
		FILE_NOT_FOUND_FILENAME = "./SorryNoFileMsg.sml";
		m_TagNamesFilename = "./tag-names.xml";
	#endif
#endif
}

#endif
#endif
