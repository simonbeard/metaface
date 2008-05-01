////////////////////////////////////////////////////////////////////////////////
//
// CTTS_TextModifier CLASS Header file.
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_TEXT_MODIFIER_H__
#define __CTTS_TEXT_MODIFIER_H__

#include "../TTS_SMLDocument.h"
#include "../TTS_SMLNode.h"

#include "TTS_SMLSimpleTag.h"
#include "TTS_Pron.h"

class CTTS_TextModifier
{
private:
	void FilterText (CTTS_SMLNode *pNode);

public:
	CTTS_TextModifier () {};

	void ProcessNode (CTTS_SMLNode *pNode);
};

#endif