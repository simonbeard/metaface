////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Happy class IMPLEMENTATION file.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_Happy.h"

////////////////////////////////////////////////////////////////////////////////
// ProcessTag
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Happy::ProcessTag (CTTS_SMLNode *pNode)
{
	// TEMPORARY - values have been hardcoded, consult notebook 
	// for possibilities on specifying values.

	ModifySpeechRate (pNode, "+10%");					// Increase the speech rate.
	ModifyPitch (pNode, "+20%", "+175%");				// Increase average pitch (10%), and the pitch range (100%).

	ActivateProsodicEffect (pNode, EFFECT_2);			// Increase the duration of stressed vowels.
	ActivateProsodicEffect (pNode, EFFECT_6);			// Eliminate abrupt changes in pitch between phonemes.
	ActivateProsodicEffect (pNode, EFFECT_8);			// Reduce the amount of pitch fall at the end of the utterance
	//ActivateProsodicEffect (pNode, EFFECT_10);			// Modify phoneme durations for regular stressing.
}