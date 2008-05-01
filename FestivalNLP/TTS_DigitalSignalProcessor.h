////////////////////////////////////////////////////////////////////////////////
//
// CDigitalSignalProcessing CLASS Header file.
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_DIGITAL_SIGNAL_PROCESSOR__
#define __CTTS_DIGITAL_SIGNAL_PROCESSOR__

#include "Tags/TTS_Speaker.h"
#include "TTS_MbrolaOptions.h"
#include "TTS_stdPackage.h"
#include <stdio.h>
#include <assert.h>
//#include <process.h>

#ifdef WIN32
	#include "windows.h"
#endif

class CTTS_DigitalSignalProcessor
{
private:
	const char *m_MbrolaBinHome;
	const char *m_MbrolaVoiceHome;
	const char *m_EN1_RENAME_PHONEMES;
	const char *m_US1_RENAME_PHONEMES;
	const char *m_EN1_CLONE_PHONEMES;
	const char *m_US1_CLONE_PHONEMES;
	const char *m_EN1_FEMALE_FREQUENCY_RATIO;
	const char *m_EN1_MALE_FREQUENCY_RATIO;
	const char *m_EN1_FEMALE_VOICE_FREQUENCY;
	const char *m_EN1_MALE_VOICE_FREQUENCY;
	const char *m_US1_FEMALE_FREQUENCY_RATIO;
	const char *m_US1_MALE_FREQUENCY_RATIO;
	const char *m_US1_FEMALE_VOICE_FREQUENCY;
	const char *m_US1_MALE_VOICE_FREQUENCY;

	string m_FrequencyRatio;
	string m_VoiceFrequency;
	string m_RenamePhonemes;
	string m_ClonePhonemes;
	string m_Voice;

	inline void Initialize();

public:
	CTTS_DigitalSignalProcessor ()
		{
		Initialize ();
		};

	char *GenerateWave (const char *MbrolaFilename, CTTS_MbrolaOptions *pOptions);

	void PlayWave (const char *Filename);

	void SetSpeakerValues (CTTS_Speaker *pSpeaker);
};

///////////////////////////////////////////////////////////////
// INLINE functions
// 
///////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////
// Initialize
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_DigitalSignalProcessor::Initialize ()
{
#ifdef __TTS_WIN98__
	m_MbrolaBinHome = "C:\\users\\facial_animation\\mbrola\\";
	m_MbrolaVoiceHome = "C:\\users\\facial_animation\\mbrola\\";
	m_Voice = string (" ") + m_MbrolaVoiceHome + "en1\\en1";
#else
#	ifdef WIN32
	m_MbrolaBinHome = "C:/users/facial_animation/mbrola/";
	m_MbrolaVoiceHome = "C:/users/facial_animation/mbrola/";
#	else
	m_MbrolaBinHome = "/usr/data/lillee/raytrace/facial_animation/FAESHARE/MBROLA/";
	m_MbrolaVoiceHome = "/usr/data/lillee/raytrace/facial_animation/FAESHARE/MBROLA/";
#	endif
	m_Voice = string (" ") + m_MbrolaVoiceHome + "en1/en1";
#endif


	m_EN1_FEMALE_FREQUENCY_RATIO = "1.6 ";
	m_EN1_MALE_FREQUENCY_RATIO = "1.0 ";
	m_EN1_FEMALE_VOICE_FREQUENCY = "20000 ";
	m_EN1_MALE_VOICE_FREQUENCY = "16000 ";

	m_US1_FEMALE_FREQUENCY_RATIO = "1.5 ";
	m_US1_MALE_FREQUENCY_RATIO = "0.9 ";
	m_US1_FEMALE_VOICE_FREQUENCY = "16000 ";
	m_US1_MALE_VOICE_FREQUENCY = "16000 ";

	m_EN1_RENAME_PHONEMES = " -R \""
		"tS ch  "
		"dZ jh  "
		"N ng  "
		"T th  "
		"D dh  "
		"S sh  "
		"Z zh  "
		"j y  "
		"i: ii  "
		"A: aa  "
		"O: oo  "
		"u: uu  "
		"3: @@  "
		"I i  "
		"{ a  "
		"V uh  "
		"Q o  "
		"U u  "
		"eI ei  "
		"aI ai  "
		"OI oi  "
		"@U ou  "
		"aU au  "
		"I@ i@  "
		"E@ e@  "
		"U@ u@\" ";

	m_EN1_CLONE_PHONEMES = " ";

	m_US1_RENAME_PHONEMES = " -R \""
		"tS ch  "
		"dZ jh  "
		"N ng  "
		"T th  "
		"D dh  "
		"S sh  "
		"Z zh  "
		"j y  "
		"i ii  "
		"A aa  "
		"O oo  "
		"u uu  "
		"r= @@  "
		"E e  "
		"{ a  "
		"V uh  "
		"U u  "
		"EI ei  "
		"AI ai  "
		"OI oi  "
		"@U ou  "
		"aU au\" ";

	m_US1_CLONE_PHONEMES = " -C \""
		"@ o  "
		"e e@ "
		"O u@ "
		"I i  "
		"I i@\" ";

	m_RenamePhonemes = m_EN1_RENAME_PHONEMES;
	m_ClonePhonemes = m_EN1_CLONE_PHONEMES;
	m_FrequencyRatio = "1.0 ";
	m_VoiceFrequency = "16000 ";
}

#endif
