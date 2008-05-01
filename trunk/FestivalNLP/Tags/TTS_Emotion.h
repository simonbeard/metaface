////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Emotion CLASS Header file.
//
// Base class for all emotion SML tags.  Is a specialisation of 
// CTTS_SimpleSMLTag.
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_EMOTION_H__
#define __CTTS_EMOTION_H__

#include "TTS_SMLSimpleTag.h"
#include "TTS_Speaker.h"
#include "TTS_Pause.h"
#include "TTS_Rate.h"
#include "TTS_Pitch.h"
#include "TTS_Emph.h"
#include "TTS_Stat.h"

class CTTS_Emotion : public CTTS_SMLSimpleTag
{
private:
	enum VowelType
		{
		NON_STRESSED, PRIMARY_VOWEL, SECONDARY_VOWEL, TERTIARY_VOWEL
		};

	const int PHONEMES_IN_LONG_WORD;
	const int LONG_WORD_PAUSE_LENGTH;
	const int MAX_DURATION;

	bool IsVowelSound (string *Phoneme);
	int GetNextPitch (CTTS_SMLNode *pNode, int *W, int *Ph, int *PP);
	void EliminateAbruptPitchChanges (CTTS_SMLNode *pNode, CTTS_Stat *pStat);
	void AddPauseAfterLongWords (CTTS_SMLNode *pNode);
	void LengthenPhonemesBeforePause (CTTS_SMLNode *pNode, int Word);
	void LowerWordBeforePauses (CTTS_SMLNode *pNode);
	void LowerWordInNode (CTTS_SMLNode *pNode, int Word);
	CTTS_SMLNode *SearchForLastTextNode (CTTS_SMLNode *pNode);
	void ChangePitchLastWord (CTTS_SMLNode *pNode, double PitchChange);
	void ChangeStressedVowelDurations (CTTS_SMLNode *pNode, CTTS_Stat *pStat);
	void ChangeStressedVowelPitch (CTTS_SMLNode *pNode, CTTS_Stat *pStat);
	enum VowelType StressedVowelType (CTTS_Stat *pStat, string *Phoneme, int Pitch, int Duration);
	int FindMaxPitch (CTTS_SMLNode *pNode, int W, int Ph);
	void ModifyForRegularStressing (CTTS_SMLNode *pNode, CTTS_Stat *pStat);

	void AddEffect1 (CTTS_SMLNode *pNode, CTTS_Stat *pStat);			// Increase the pitch of stressed vowels.
	void AddEffect2 (CTTS_SMLNode *pNode, CTTS_Stat *pStat);			// Increase the duration of stressed vowels.
	void AddEffect3 (CTTS_SMLNode *pNode);								// Increase the articulation precision.
	void AddEffect4 (CTTS_SMLNode *pNode);								// Decrease the articulation precision.
	void AddEffect5 (CTTS_SMLNode *pNode);								// Increase the rate of pitch declination.
	void AddEffect6 (CTTS_SMLNode *pNode, CTTS_Stat *pStat);			// Eliminate abrupt changes in pitch between phonemes.
	void AddEffect7 (CTTS_SMLNode *pNode);								// Add downward pitch inflections at word endings.
	void AddEffect8 (CTTS_SMLNode *pNode);								// Reduce the amount of pitch fall at end of utterance
	void AddEffect9 (CTTS_SMLNode *pNode);								// Replace upward inflections iwth downward inflections.
	void AddEffect10 (CTTS_SMLNode *pNode, CTTS_Stat *pStat);			// Modify phoneme durations for regular stressing.
	void AddEffect11 (CTTS_SMLNode *pNode);								// Add pauses after long words.
	void AddEffect12 (CTTS_SMLNode *pNode);								// Lower pitch of word before every pause.
	void AddEffect13 (CTTS_SMLNode *pNode);								// Incrase final lowering of utterance.

protected:
	enum ProsodicEffect
		{
		EFFECT_1, EFFECT_2, EFFECT_3, EFFECT_4, EFFECT_5, 
		EFFECT_6, EFFECT_7, EFFECT_8, EFFECT_9, EFFECT_10, EFFECT_11,
		EFFECT_12, EFFECT_13
		};

	void ModifySpeechRate (CTTS_SMLNode *pNode, const char *Amount);
	void ModifyPitch (CTTS_SMLNode *pNode, const char *Middle, const char *Range);
	void ModifyVolume (CTTS_SMLNode *pNode, double Amount);
	void ActivateProsodicEffect (CTTS_SMLNode *pNode, enum ProsodicEffect Effect);

public:
	CTTS_Emotion () 
		: PHONEMES_IN_LONG_WORD (6), LONG_WORD_PAUSE_LENGTH (80), MAX_DURATION (300)
		{
		};

	virtual void ProcessTag (CTTS_SMLNode *pNode);
};

#endif