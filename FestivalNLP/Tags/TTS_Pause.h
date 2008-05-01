////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Pause CLASS Header file.
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_PAUSE_H__
#define __CTTS_PAUSE_H__

#include "TTS_SMLSimpleTag.h"

class CTTS_Pause : public CTTS_SMLSimpleTag
{
private:
	int m_Length;
	const int SHORT;
	const int MEDIUM;
	const int LONG;
	const int MAX_DURATION;

	CTTS_SMLNode *FindLastTextNode (CTTS_SMLNode *pNode);
	CTTS_SMLNode *FindNextTextNode (CTTS_SMLNode *pNode);
	inline void InsertPause (CTTS_SMLNode *pTextNode);
	inline void GetPauseLength (CTTS_SMLNode *pNode);
	inline void SetLength (string &Length);
	void LengthenPhonemesBeforePause (CTTS_SMLNode *pCurrNode);

public:
	CTTS_Pause ()
		: SHORT (150), MEDIUM (500), LONG (1000), MAX_DURATION (300)
		{
		m_Length = MEDIUM;
		}

	virtual void ProcessTag (CTTS_SMLNode *pNode);
};

//////////////////////////////////////////////////////////////////
// INLINE Functions
//////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// InsertPause
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_Pause::InsertPause (CTTS_SMLNode *pTextNode)
{
	pTextNode->PushPhoneme ("_", m_Length);
}

////////////////////////////////////////////////////////////////////////////////
// SetLength
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_Pause::SetLength (string &Length)
{
	if (Length == "short")
		{
		m_Length = SHORT;
		}
	else if (Length == "medium")
		{
		m_Length = MEDIUM;
		}
	else if (Length == "long")
		{
		m_Length = LONG;
		}
	else
		{
		int msec;
		sscanf (Length.c_str(), "%d", &msec);
		assert (msec >= 0);
		m_Length = msec;
		}
}

#endif