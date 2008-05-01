////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Rate Class HEADER file
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_RATE_H__
#define __CTTS_RATE_H__

#include "TTS_SMLSimpleTag.h"

class CTTS_Rate : public CTTS_SMLSimpleTag
{
private:
	const double FASTEST;
	const double FAST;
	const double NORMAL;
	const double SLOW;
	const double SLOWEST;
	double m_SpeedFactor;

public:
	CTTS_Rate () 
		: FASTEST (0.5), FAST (0.8), NORMAL (1), SLOW (1.5), SLOWEST (2)
		{
		m_SpeedFactor = NORMAL;
		};

	virtual void ProcessTag (CTTS_SMLNode *pNode);

	inline void SetSpeed (string &Speed);

	void ModifyPhonemeDurations (CTTS_SMLNode *pCurrNode);
};

///////////////////////////////////////////////////////////
// INLINE Functions
//
///////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// SetSpeed
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_Rate::SetSpeed (string &Speed)
{
	if (Speed == "fastest")
		{
		m_SpeedFactor = FASTEST;
		}
	else if (Speed == "fast")
		{
		m_SpeedFactor = FAST;
		}
	else if (Speed == "normal")
		{
		m_SpeedFactor = NORMAL;
		}
	else if (Speed == "slow")
		{
		m_SpeedFactor = SLOW;
		}
	else if (Speed == "slowest")
		{
		m_SpeedFactor = SLOWEST;
		}
	else if (ValidPercentage (Speed.c_str()))
		{
		Speed [Speed.size()-1] = '\0';
		double temp = (double) StrToInt (Speed.c_str()) / 100;

		// if -ve number specified, a decrease in speed is wanted (and vice-versa)
		m_SpeedFactor += (m_SpeedFactor * -temp);
		}
}

#endif