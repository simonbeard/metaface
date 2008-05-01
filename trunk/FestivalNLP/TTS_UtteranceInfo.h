////////////////////////////////////////////////////////////////////////////////
//
// CTTS_UtteranceInfo CLASS Header file.
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_UTTERANCE_INFO_H__
#define __CTTS_UTTERANCE_INFO_H__

#include "TTS_WordInfo.h"
#include "TTS_SimpleInFile.h"
#include "TTS_stdPackage.h"

#include <iostream.h>
#include <vector>

using std::vector;
typedef vector <CTTS_WordInfo> WordListType;

class CTTS_UtteranceInfo
{
public:
	WordListType WordList;

	CTTS_UtteranceInfo ()
		{
		};

	void AddWord (string &NewWord);
	void AddWord (string &NewWord, int Start);
	void InsertWord (string &NewWord, int Pos);

	void PrintUtteranceInfo ();

	void GetData (const char *Basename);

	void OutputPhonemesInMbrolaFormat (ofstream &MBRFile);
	void OutputPhonemesInMbrolaFormat (string &MBROLA_Utterance);
	void OutputPhonemesInGSTFormat (ofstream &GSTFile);

	int GetNumOfWords ();
	int GetNumOfPhonemes (int WordIndex);
	int GetPitchPointCount (int WordIndex, int PhonemeIndex);
	int GetPhonemeDuration (int WordIndex, int PhonemeIndex);
	void SetPhonemeDuration (int WordIndex, int PhonemeIndex, int Duration);
	void SetPitch (int WordIndex, int PhonemeIndex, int PitchPoint, int Pitch);
	int GetPitch (int WordIndex, int PhonemeIndex, int PitchPoint);

private:
	void GetWords (const char *Filename);

	void GetPhonemes (const char *Filename);

	void SetPhonemePitchInfo (char*, int, int);

	inline int NextNumber (string &str, int &i);
};

///////////////////////////////////////////////////////////////
// INLINE functions
// 
///////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// GetData
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_UtteranceInfo::GetData (const char *Basename)
	{
	string Temp;

	// Get words and word boundary information from "wrd" file
	Temp = string (Basename) + ".wrd";
	GetWords (Temp.c_str ());

	// Get phoneme pitch and duration information from "mbr" file
	Temp = string (Basename) + ".mbr";
	GetPhonemes (Temp.c_str ());
	}

////////////////////////////////////////////////////////////////////////////////
// NextNumber
//
////////////////////////////////////////////////////////////////////////////////
inline int CTTS_UtteranceInfo::NextNumber (string &str, int &i)
	{
	int Value = 0;
	int Digit;

	while (str[i] != ' ')
		{
		Digit = (int) (str[i++] - '0');
		Value = (Value * 10) + Digit;
		}
	i++;

	return Value;
	}

#endif