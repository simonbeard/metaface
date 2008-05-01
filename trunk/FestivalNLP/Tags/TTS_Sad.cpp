////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Sad class IMPLEMENTATION file.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_Sad.h"

////////////////////////////////////////////////////////////////////////////////
// ProcessTag
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Sad::ProcessTag (CTTS_SMLNode *pNode)
{
	// TEMPORARY - values have been hardcoded, consult notebook 
	// for possibilities on specifying values.
	ModifySpeechRate (pNode, "-15%");					// Decrease the speech rate.
	ModifyPitch (pNode, "-5%", "-25%");					// Decrease average pitch (5%), and the pitch range (25%).

	ActivateProsodicEffect (pNode, EFFECT_6);			// Eliminate abrupt changes in pitch between phonemes
	ActivateProsodicEffect (pNode, EFFECT_11);			// Add pauses after long words.
	ActivateProsodicEffect (pNode, EFFECT_12);			// Lower pitch of every word that occurs before a pause
	ActivateProsodicEffect (pNode, EFFECT_13);			// Final lowering of utterance.
}