////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Angry CLASS Header file.
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_ANGRY_H__
#define __CTTS_ANGRY_H__

#include "TTS_Emotion.h"

class CTTS_Angry : public CTTS_Emotion
{
public:
	CTTS_Angry () {};

	virtual void ProcessTag (CTTS_SMLNode *pNode);
};

#endif