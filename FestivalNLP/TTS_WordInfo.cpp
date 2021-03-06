////////////////////////////////////////////////////////////////////////////////
//
// CTTS_WordInfo IMPLEMENTATION File.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_WordInfo.h"

////////////////////////////////////////////////////////////////////////////////
// PrintWordInfo
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_WordInfo::PrintWordInfo ()
{
	cout << Word.c_str() << ": " << StartTime << " ms" << endl;
	for (int i=0; i < PhonemeList.size(); i++)
		{
		cout << "\t";
		PhonemeList[i].PrintPhonemeInfo ();
		}
}

////////////////////////////////////////////////////////////////////////////////
// OutputMbrolaWords
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_WordInfo::OutputMbrolaWords (ofstream &MBRFile)
{
	for (int i=0; i < PhonemeList.size(); i++)
		{
		PhonemeList[i].OutputMbrolaPhonemes (MBRFile);
		}
}

////////////////////////////////////////////////////////////////////////////////
// OutputGSTWords
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_WordInfo::OutputGSTWords (ofstream &GSTFile)
{
	if (Word != "'s")
		GSTFile << ">" << Word.c_str() << endl;

	for (int i=0; i < PhonemeList.size(); i++)
		{
		PhonemeList[i].OutputGSTPhonemes (GSTFile);
		}
}

////////////////////////////////////////////////////////////////////////////////
// OutputMbrolaWords
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_WordInfo::OutputMbrolaWords (string &MBROLA_Utterance)
{
	for (int i=0; i < PhonemeList.size(); i++)
		{
		PhonemeList[i].OutputMbrolaPhonemes (MBROLA_Utterance);
		}
}

////////////////////////////////////////////////////////////////////////////////
// AddPhoneme
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_WordInfo::AddPhoneme (string &phoneme, int duration)
{
	CTTS_PhonemeInfo *pPhoneme;

	pPhoneme = new CTTS_PhonemeInfo (phoneme, duration);
	PhonemeList.push_back (*pPhoneme);
	delete pPhoneme;
}

////////////////////////////////////////////////////////////////////////////////
// Copy
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_WordInfo::Copy (CTTS_WordInfo &Src)
{
	Word = Src.GetWord ();
	StartTime = Src.GetStartTime ();

	int NumOfPhonemes = Src.PhonemeList.size ();
	for (int i=0; i < NumOfPhonemes; i++)
		{
		PhonemeList.push_back (Src.PhonemeList[i]);
		}
}
