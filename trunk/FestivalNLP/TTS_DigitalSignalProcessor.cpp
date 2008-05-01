////////////////////////////////////////////////////////////////////////////////
//
// CTTS_DigitalSignalProcessor CLASS Implementation file.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_DigitalSignalProcessor.h"

////////////////////////////////////////////////////////////////////////////////
// GenerateWave
//
////////////////////////////////////////////////////////////////////////////////
char *CTTS_DigitalSignalProcessor::GenerateWave (const char *PhonemeFilename, CTTS_MbrolaOptions *pOptions)
{
	assert ((PhonemeFilename != NULL) && (pOptions != NULL));

	////////////////////////////////////////////////
	// Speaker contains speaker parameters such as
	// gender, age, and language.  Set appropriate
	// MBROLA command line arguments to produce a
	// waveform with these settings.
	if (pOptions->Speaker())
	{		
		SetSpeakerValues (pOptions->Speaker());		
	}

	////////////////////////////////////////////////
	// Prepare MBROLA command
	string WaveFilename = string (PhonemeFilename) + ".wav";		// this should be changed to a .wav extension (instead of .raw)

	string MBROLA_COMMAND = string (m_MbrolaBinHome) + "mbrola " 		
		+ "-v " + pOptions->GetVolumeRatio() + " "
		+ "-t " + pOptions->GetTimeFactor() + " "
		+ "-f " + m_FrequencyRatio + " -l " + m_VoiceFrequency 
		+ m_RenamePhonemes + m_ClonePhonemes // Altered by spiky -- substitutions not working
		+ m_Voice + " " + PhonemeFilename + " " + WaveFilename.c_str();

	string MBTMP = string (m_MbrolaBinHome) + "mbrola.exe";
	string MBARG = string() + "-v " + pOptions->GetVolumeRatio() + " "
		+ "-t " + pOptions->GetTimeFactor() + " "
		+ "-f " + m_FrequencyRatio + " -l " + m_VoiceFrequency 
		+ m_RenamePhonemes + m_ClonePhonemes
		+ m_Voice + " " + PhonemeFilename + " " + WaveFilename.c_str();

	cout << MBROLA_COMMAND.c_str(); cout.flush();

	////////////////////////////////////////////////
	// Execute MBROLA
	int ReturnStatus = system (MBROLA_COMMAND.c_str());
      
          //_spawnl(_P_WAIT,MBTMP.c_str(),"mbrola.exe",MBARG.c_str(),NULL);
          //fprintf(stderr,"Cannot execute mbrola\n");


	if (ReturnStatus < 0)
		{
		cerr << "Error in CTTS_DigitalSignalProcessor::GenerateWave (): could not execute mbrola.exe successfully\n";
		}

	////////////////////////////////////////////////
	// Return name of wave file that Mbrola created
 	char *Filename = strdup (WaveFilename.c_str());

	assert (Filename != NULL);

	return (Filename);
}

////////////////////////////////////////////////////////////////////////////////
// PlayWave
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_DigitalSignalProcessor::PlayWave (const char *Filename)
{
	assert (Filename != NULL);
//	cout << "Playing waveform...\n"; cout.flush();

#ifdef WIN32
	PlaySound (Filename, NULL, SND_FILENAME);
#endif
}

////////////////////////////////////////////////////////////////////////////////
// SetSpeakerValues
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_DigitalSignalProcessor::SetSpeakerValues (CTTS_Speaker *pSpeaker)
{
	string SpeakerGender = pSpeaker->GetGender ();
	string SpeakerVoice = pSpeaker->GetVoice ();

	if (SpeakerVoice == "en1")
		{
		if (SpeakerGender == "male")
			{
			m_FrequencyRatio = m_EN1_MALE_FREQUENCY_RATIO;
			m_VoiceFrequency = m_EN1_MALE_VOICE_FREQUENCY;
			}
		else if (SpeakerGender == "female")
			{
			m_FrequencyRatio = m_EN1_FEMALE_FREQUENCY_RATIO;
			m_VoiceFrequency = m_EN1_FEMALE_VOICE_FREQUENCY;
			}

#ifdef __TTS_WIN98__
		m_Voice = string (" ") + m_MbrolaVoiceHome + "en1\\en1";
#else
		m_Voice = string (" ") + m_MbrolaVoiceHome + "en1/en1";
#endif

		m_RenamePhonemes = m_EN1_RENAME_PHONEMES;
		m_ClonePhonemes = m_EN1_CLONE_PHONEMES;
		}
	else if (SpeakerVoice == "us1")
		{
		if (SpeakerGender == "male")
			{
			m_FrequencyRatio = m_US1_MALE_FREQUENCY_RATIO;
			m_VoiceFrequency = m_US1_MALE_VOICE_FREQUENCY;
			}
		else if (SpeakerGender == "female")
			{
			m_FrequencyRatio = m_US1_FEMALE_FREQUENCY_RATIO;
			m_VoiceFrequency = m_US1_FEMALE_VOICE_FREQUENCY;
			}

#ifdef __TTS_WIN98__
		m_Voice = string (" ") + m_MbrolaVoiceHome + "us1\\us1";
#else
		m_Voice = string (" ") + m_MbrolaVoiceHome + "us1/us1";
#endif
		m_RenamePhonemes = m_US1_RENAME_PHONEMES;
		m_ClonePhonemes = m_US1_CLONE_PHONEMES;
		}

#if 0
	//Added by saila to get voice working.
m_VoiceFrequency = "8000 ";
#endif
}