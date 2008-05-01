////////////////////////////////////////////////////////////////////////////////
//
// CTTS_MbrolaOptions Class HEADER file.
//
////////////////////////////////////////////////////////////////////////////////
#ifndef __CTTS_MBROLA_OPTIONS_H__
#define __CTTS_MBROLA_OPTIONS_H__

#include "Tags/TTS_Speaker.h"
#include "assert.h"
#include <string>
using std::string;

class CTTS_MbrolaOptions
{
private:
	CTTS_Speaker *m_pSpeaker;
	string m_TimeFactor;
	string m_VolumeRatio;

public:
	CTTS_MbrolaOptions ()
		{
		m_pSpeaker = new CTTS_Speaker;
		m_TimeFactor = "1.0";
		m_VolumeRatio = "1.0";
		};

	~CTTS_MbrolaOptions ()
		{
		delete m_pSpeaker;
		};

	inline void SetSpeaker (CTTS_Speaker &Speaker)
		{
		m_pSpeaker->SetAge (Speaker.GetAge());
		m_pSpeaker->SetGender (Speaker.GetGender ());
		m_pSpeaker->SetVoice (Speaker.GetVoice ());
		};

	inline void SetTimeFactor (const char *TimeFactor)
		{
		assert (TimeFactor != NULL);

		m_TimeFactor = TimeFactor;
		};

	inline const char *GetTimeFactor ()
		{
		return m_TimeFactor.c_str();
		};
	
	inline void SetVolumeRatio (const char *VolumeRatio)
		{
		assert (VolumeRatio != NULL);

		m_VolumeRatio = VolumeRatio;
		};

	inline const char *GetVolumeRatio ()
		{
		return m_VolumeRatio.c_str();
		};

	inline CTTS_Speaker *Speaker ()
		{
		return m_pSpeaker;
		};
};

#endif