////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Pron Class HEADER file
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_PRON_H__
#define __CTTS_PRON_H__

#include "TTS_SMLSimpleTag.h"

class CTTS_Pron : public CTTS_SMLSimpleTag
{
private:


public:
	CTTS_Pron () {};

	virtual void ProcessTag (CTTS_SMLNode *pNode);
};

#endif