////////////////////////////////////////////////////////////////////////////////
//
// CTTS_PhonemeModifier CLASS Header file.
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_PHONEME_MODIFIER_H__
#define __CTTS_PHONEME_MODIFIER_H__

#include "../TTS_SMLDocument.h"
#include "../TTS_SMLNode.h"

#include "TTS_SMLSimpleTag.h"
#include "TTS_Pause.h"
#include "TTS_Rate.h"
#include "TTS_Pitch.h"
#include "TTS_Emph.h"
#include "TTS_Sad.h"
#include "TTS_Happy.h"
#include "TTS_Angry.h"
#include "TTS_Stat.h"
#include "TTS_Volume.h"
#include "../TTS_MbrolaOptions.h"

class CTTS_PhonemeModifier
{
public:
	CTTS_PhonemeModifier () {};

	void ProcessNode (CTTS_SMLNode *pNode, CTTS_MbrolaOptions *pOptions);
};

#endif