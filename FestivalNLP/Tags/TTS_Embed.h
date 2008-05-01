////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Embed Class HEADER file
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_EMBED_H__
#define __CTTS_EMBED_H__

#include "TTS_SMLSimpleTag.h"
#include "../TTS_NaturalLanguageParser.h"
#include "MML_Singer.h"

class CTTS_Embed : public CTTS_SMLSimpleTag
{
private:

public:
	CTTS_Embed () 
		{
		};

	virtual void ProcessTag (CTTS_SMLNode *pNode, CTTS_NaturalLanguageParser *pNLP, CTTS_DigitalSignalProcessor *pDSP);
	void ProcessMMLDocument (CTTS_SMLNode *pNode, CTTS_NaturalLanguageParser *pNLP);
	void ProcessAudioFile (CTTS_SMLNode *pNode, CTTS_DigitalSignalProcessor *pDSP);
};

#endif