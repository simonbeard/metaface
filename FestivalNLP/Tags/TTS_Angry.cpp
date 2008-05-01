////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Angry class IMPLEMENTATION file.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_Angry.h"

////////////////////////////////////////////////////////////////////////////////
// ProcessTag
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Angry::ProcessTag (CTTS_SMLNode *pNode)
{
	// TEMPORARY - values have been hardcoded, consult notebook 
	// for possibilities on specifying values.

	/*
	ModifySpeechRate (pNode, "+18%");					// Increase the speech rate.
	ModifyPitch (pNode, "+15%", "+175%");				// Increase average pitch (15%), and the pitch range (175%).

	ActivateProsodicEffect (pNode, EFFECT_1);			// Increase the pitch of stressed vowels.
	/**/

	ModifySpeechRate (pNode, "+18%");					// Increase the speech rate.
	ModifyPitch (pNode, "-15%", "-15%");				// Decrease average pitch (15%), and the pitch range (15%).

	ActivateProsodicEffect (pNode, EFFECT_1);			// Increase the pitch of stressed vowels.
}