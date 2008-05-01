////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Sad CLASS Header file.
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_SAD_H__
#define __CTTS_SAD_H__

#include "TTS_Emotion.h"

class CTTS_Sad : public CTTS_Emotion
{
public:
	CTTS_Sad () {};

	virtual void ProcessTag (CTTS_SMLNode *pNode);
};

#endif