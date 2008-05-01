////////////////////////////////////////////////////////////////////////////////
//
// TTS_C_API Header file
// A C API for CTTS_Central
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __TTS_C_API_H__
#define __TTS_C_API_H__

#include "TTS_Central.h"
#include "TTS_EmotionIDs.h"

#include <jni.h>
#include "metaface_nlp_MetaFaceFestival.h"

////////////////////////////////
// Globals
////////////////////////////////
#ifdef __cplusplus
static CTTS_Central *TTS_SpeechSynthesizer;
#endif

////////////////////////////////
// Prototypes
////////////////////////////////
#ifdef __cplusplus
extern "C"
	{
#endif
	void TTS_Initialise ();
	void TTS_SpeakFromFile (const char *Filename);
	void TTS_SpeakText (const char *Text);
	void TTS_SpeakTextEx (const char *Text, int Emotion);
	void TTS_Destroy ();

#ifdef __cplusplus
	}
#endif

#endif