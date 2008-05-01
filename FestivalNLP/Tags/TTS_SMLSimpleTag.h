////////////////////////////////////////////////////////////////////////////////
//
// CTTS_SMLSimpleTag CLASS Header file.
//
// Base class for all SML tags.
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_SML_SIMPLE_TAG_H__
#define __CTTS_SML_SIMPLE_TAG_H__

#include "../TTS_SMLNode.h"
#include "../TTS_stdPackage.h"
#include <assert.h>

class CTTS_SMLSimpleTag
{
protected:
	char *GetAttribute (CTTS_SMLNode *pNode, const char *AttributeName);

	int StrToInt (const char *Str);
	bool ValidPercentage (const char *Str);

public:
	CTTS_SMLSimpleTag () {};

	virtual void ProcessTag (CTTS_SMLNode *pNode);
};

#endif