//////////////////////////////////////////////////////////////////////////////
// CTTS_Stat Header file
//
//////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_STAT__
#define __CTTS_STAT__

#include <iostream.h>
#include "TTS_SMLSimpleTag.h"
#include "../TTS_SMLNode.h"
#include "../TTS_SimpleInFile.h"

#include <string>
using std::string;

#include <vector>
using std::vector;

class CTTS_Stat : public CTTS_SMLSimpleTag
{
private:
	int m_Max;					// Maximum Pitch value
	int m_Min;					// Minimum Pitch value
	int m_Count;				// Number of pitch points
	int m_Sum;					// Sum of Pitch values

	int m_DurSum;				// Sum of Duration values
	int m_DurCount;				// Number of Duration points
	int m_DurMax;				// Maximum Duration values
	int m_DurMin;				// Minimum Duration values

	void GetPitchPoint (char *pLine, string &Phoneme, int Duration, int Time);
	int NextNumber (string &Str, int &i);

public:
	const int INITIAL_MIN;

	CTTS_Stat ()
		: INITIAL_MIN (999999)
		{
		m_Max = 0;
		m_Min = INITIAL_MIN;
		m_Count = 0;
		m_Sum = 0;

		m_DurSum = 0;
		m_DurCount = 0;
		m_DurMax = 0;
		m_DurMin = INITIAL_MIN;
		};

	virtual void ProcessTag (CTTS_SMLNode *pNode);

	void GetInfo (CTTS_SMLNode *pNode);
	void GetPitchInfo (CTTS_SMLNode *pNode);
	void GetDurationInfo (CTTS_SMLNode *pNode);
	void GetTimePitchInfo (const char *Filename);

	inline double GetAverage ();
	inline int GetMin ();
	inline int GetMax ();
	inline int GetRange ();

	inline double GetDurAverage ();
	inline int GetDurMin ();
	inline int GetDurMax ();
	inline int GetDurRange ();

	void OutputStats ();
};

////////////////////////////////////////////////////////////////
// Inline functions
//
////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// GetAverage
//
////////////////////////////////////////////////////////////////////////////////
inline double CTTS_Stat::GetAverage ()
{
	assert (m_Count != 0);

	return (m_Sum / m_Count);
}

////////////////////////////////////////////////////////////////////////////////
// GetMin
//
////////////////////////////////////////////////////////////////////////////////
inline int CTTS_Stat::GetMin ()
{
	return m_Min;
}

////////////////////////////////////////////////////////////////////////////////
// GetMax
//
////////////////////////////////////////////////////////////////////////////////
inline int CTTS_Stat::GetMax ()
{
	return m_Max;
}

////////////////////////////////////////////////////////////////////////////////
// GetRange
//
////////////////////////////////////////////////////////////////////////////////
inline int CTTS_Stat::GetRange ()
{
	int Range = m_Max - m_Min;
	if (Range < 0)
		Range = 0;

	return Range;
}

////////////////////////////////////////////////////////////////////////////////
// GetDurAverage
//
////////////////////////////////////////////////////////////////////////////////
inline double CTTS_Stat::GetDurAverage ()
{
	assert (m_DurCount != 0);

	return (m_DurSum / m_DurCount);
}

////////////////////////////////////////////////////////////////////////////////
// GetDurMin
//
////////////////////////////////////////////////////////////////////////////////
inline int CTTS_Stat::GetDurMin ()
{
	return m_DurMin;
}

////////////////////////////////////////////////////////////////////////////////
// GetDurMax
//
////////////////////////////////////////////////////////////////////////////////
inline int CTTS_Stat::GetDurMax ()
{
	return m_DurMax;
}

////////////////////////////////////////////////////////////////////////////////
// GetDurRange
//
////////////////////////////////////////////////////////////////////////////////
inline int CTTS_Stat::GetDurRange ()
{
	int Range = m_DurMax - m_DurMin;
	if (Range < 0)
		Range = 0;

	return Range;
}

#endif