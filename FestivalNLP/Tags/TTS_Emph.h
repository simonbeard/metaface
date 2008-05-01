////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Emph CLASS Header file.
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_EMPH_H__
#define __CTTS_EMPH_H__

#include "TTS_SMLSimpleTag.h"

class CTTS_Emph : public CTTS_SMLSimpleTag
{
private:
	double m_Strength;
	int m_Affect;
	const int PITCH_AND_DURATION;
	const int PITCH_ONLY;
	const int DURATION_ONLY;
	const double WEAKEST;
	const double WEAK;
	const double MODERATE;
	const double STRONG;
	const int MAX_PITCH;
	const int MAX_DURATION;
	const double PRIMARY_PITCH;
	const double PRIMARY_DURATION;
	const double SECONDARY_PITCH;
	const double SECONDARY_DURATION;

	void FindTarget (CTTS_SMLNode *pNode, const char *pTarget, int *Word, int *Phoneme);
	void FindFirstVowelSound (CTTS_SMLNode *pNode, int *Word, int *Phoneme);
	void EmphasizePhonemes (CTTS_SMLNode *pNode, int Word, int Phoneme);
	void Emphasize (CTTS_SMLNode *pNode, int Word, int Phoneme, double PitchFactor, double DurationFactor);
	int GetLastPitchValue (CTTS_SMLNode *pNode, int Word, int Phoneme);

public:
	CTTS_Emph ()
		: WEAKEST (0.5), WEAK (0.9), MODERATE (1.0), STRONG (1.2), MAX_PITCH (120), MAX_DURATION (300), 
		PRIMARY_PITCH (1.3), PRIMARY_DURATION (2.0), SECONDARY_PITCH (1.2), SECONDARY_DURATION (1.8),
		PITCH_AND_DURATION (1), PITCH_ONLY (2), DURATION_ONLY (3)
		{
		m_Strength = WEAK;
		m_Affect = PITCH_ONLY;
		}

	virtual void ProcessTag (CTTS_SMLNode *pNode);

	inline double GetStrength ();
	inline void SetStrength (const char *Strength);

	inline int GetAffect ();
	inline void SetAffect (const char *Affect);

	inline bool AffectPitch ();
	inline bool AffectDuration ();
};


////////////////////////////////////////////////////////////////
// Inline Functions
////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// GetStrength
//
////////////////////////////////////////////////////////////////////////////////
inline double CTTS_Emph::GetStrength ()
		{
		return m_Strength;
		};

////////////////////////////////////////////////////////////////////////////////
// SetStrength
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_Emph::SetStrength (const char *Strength)
{
	assert (Strength != NULL);

	string Str = Strength;

	if (Str == "weakest")
		{
		m_Strength = WEAKEST;
		}
	else if (Str == "weak")
		{
		m_Strength = WEAK;
		}
	else if (Str == "moderate")
		{
		m_Strength = MODERATE;
		}
	else if (Str == "strong")
		{
		m_Strength = STRONG;
		}
}

////////////////////////////////////////////////////////////////////////////////
// GetAffect
//
////////////////////////////////////////////////////////////////////////////////
inline int CTTS_Emph::GetAffect ()
		{
		return m_Affect;
		}

////////////////////////////////////////////////////////////////////////////////
// SetAffect
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_Emph::SetAffect (const char *Affect)
{
	assert (Affect != NULL);

	string Str = Affect;

	// look at first character to determine what type of emphasis is needed
	if (Str[0] == 'p')
		{
		m_Affect = PITCH_ONLY;
		}
	else if (Str[0] == 'd')
		{
		m_Affect = DURATION_ONLY;
		}
	else
		{
		m_Affect = PITCH_AND_DURATION;
		}
}

////////////////////////////////////////////////////////////////////////////////
// AffectPitch
//
////////////////////////////////////////////////////////////////////////////////
inline bool CTTS_Emph::AffectPitch ()
{
	if (m_Affect == DURATION_ONLY)
		return false;
	else
		return true;
}

////////////////////////////////////////////////////////////////////////////////
// AffectDuration
//
////////////////////////////////////////////////////////////////////////////////
inline bool CTTS_Emph::AffectDuration ()
{
	if (m_Affect == PITCH_ONLY)
		return false;
	else
		return true;
}

#endif