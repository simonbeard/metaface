////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Pitch Class HEADER file
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_PITCH_H__
#define __CTTS_PITCH_H__

#include "TTS_SMLSimpleTag.h"
#include "TTS_Stat.h"

class CTTS_Pitch : public CTTS_SMLSimpleTag
{
private:
	const double HIGHEST;
	const double HIGH;
	const double MEDIUM;
	const double LOW;
	const double LOWEST;
	double m_PitchFactor;
	double m_RangeFactor;

	void SetPitchFactor (string &Pitch);
	void SetRangeFactor (string &Range);
	void ModifyUtterancePitchAvg (CTTS_SMLNode *pCurrNode);
	void ModifyUtterancePitchRange (CTTS_SMLNode *pCurrNode, double Average);

public:
	CTTS_Pitch () 
		: HIGHEST (2.5), HIGH (1.6), MEDIUM (1.0), LOW (0.7), LOWEST (0.4)
		{
		m_PitchFactor = MEDIUM;
		m_RangeFactor = 0.0;
		};

	virtual void ProcessTag (CTTS_SMLNode *pNode);
};

#endif