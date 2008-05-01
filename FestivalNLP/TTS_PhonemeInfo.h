////////////////////////////////////////////////////////////////////////////////
//
// CTTS_PhonemeInfo CLASS Header file.
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_PHONEME_INFO_H__
#define __CTTS_PHONEME_INFO_H__

#include "TTS_stdPackage.h"
#include <fstream.h>
#include <vector>

using std::vector;

class CPitchPatternPoint
{
private:
	int m_Position;		// position of pitch pattern point within phoneme (in % of duration)
	int m_Pitch;			// pitch value (in Hz) at the position

public:
	///////////////////////////////////////
	// Constructor
	///////////////////////////////////////
	CPitchPatternPoint ()
		{
		m_Position = 0;
		m_Pitch = 0;
		};

	CPitchPatternPoint (int InitPosition, int InitPitch)
		{
		m_Position = 0;
		m_Pitch = 0;
		if (InitPosition >= 0)
			m_Position = InitPosition;

		if (InitPitch >= 0)
			m_Pitch = InitPitch;
		};

	inline int GetPos ()
		{
		return m_Position;
		};

	inline int GetPitch ()
		{
		return m_Pitch;
		};

	inline void SetPos (int Position)
		{
		m_Position = Position;
		}

	inline void SetPitch (int Pitch)
		{
		m_Pitch = Pitch;
		}
};

////////////////////////////////////////////////////////////////////////
// class CTTS_PhonemeInfo
////////////////////////////////////////////////////////////////////////
typedef vector<CPitchPatternPoint> PitchSeriesType;

class CTTS_PhonemeInfo
{
private:
	void OutputPitchPoints (ofstream &MBRFile);
	void OutputPitchPoints (string &MBROLA_Utterance);

protected:
	char Phoneme[2];
	int Duration;

	char *FilterPhoneme (string &Phoneme);
	char *ConvertIntegerToString (int Number);

public:
	PitchSeriesType PitchSeries;

	CTTS_PhonemeInfo ()
		{
		Phoneme[0] = '-';
		Phoneme[1] = '-';
		Duration = 0;
		};

	CTTS_PhonemeInfo (string &ph, int dur)
		{
		Phoneme[0] = ph.at (0);
		if (ph.size() > 1)
			Phoneme[1] = ph.at (1);
		else
			Phoneme[1] = '-';

		if (dur > 0)
			Duration = dur;
		else
			Duration = 0;
		};

	void InsertPitchPoint (int Pos, int Pitch);

	void PrintPitchPoints ();

	inline void SetPhoneme (string &Phoneme);
	string *GetPhoneme ();

	inline void SetDuration (int);
	inline int GetDuration ();

	void PrintPhonemeInfo ();

	void OutputMbrolaPhonemes (ofstream &MBRFile);
	void OutputMbrolaPhonemes (string &MBROLA_Utterance);
	void OutputGSTPhonemes (ofstream &GSTFile);
};

///////////////////////////////////////////////////////
// Inline functions
///////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// 
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_PhonemeInfo::SetPhoneme (string &ph)
	{
	Phoneme[0] = ph.at (0);
	if (ph.size() > 1)
		Phoneme[1] = ph.at (1);
	}

////////////////////////////////////////////////////////////////////////////////
// 
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_PhonemeInfo::SetDuration (int dur)
	{
	Duration = dur;
	}

////////////////////////////////////////////////////////////////////////////////
// 
//
////////////////////////////////////////////////////////////////////////////////
inline int CTTS_PhonemeInfo::GetDuration ()
	{
	return Duration;
	}

#endif