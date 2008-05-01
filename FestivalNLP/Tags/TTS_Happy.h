////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Happy CLASS Header file.
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_HAPPY_H__
#define __CTTS_HAPPY_H__

#include "TTS_Emotion.h"

class CTTS_Happy : public CTTS_Emotion
{
public:
	CTTS_Happy () {};

	virtual void ProcessTag (CTTS_SMLNode *pNode);
};

#endif