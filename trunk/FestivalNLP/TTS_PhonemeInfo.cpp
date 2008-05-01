////////////////////////////////////////////////////////////////////////////////
//
// CTTS_PhonemeInfo IMPLEMENTATION File.
//
////////////////////////////////////////////////////////////////////////////////

#include <iostream.h>
#include <assert.h>
#include "TTS_PhonemeInfo.h"

////////////////////////////////////////////////////////////////////////////////
// GetPhoneme
//
////////////////////////////////////////////////////////////////////////////////
string *CTTS_PhonemeInfo::GetPhoneme ()
{
	char pTemp[3];

	pTemp[0] = Phoneme[0];
	if (Phoneme[1] != '-')
		{
		pTemp[1] = Phoneme[1];
		pTemp[2] = '\0';
		}
	else
		{
		pTemp[1] = '\0';
		}

	string *pStr = new string (pTemp);

	return pStr;
}

////////////////////////////////////////////////////////////////////////////////
// InsertPitchPoint
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_PhonemeInfo::InsertPitchPoint (int pos, int pitch)
{
	CPitchPatternPoint *pNewPoint;

	pNewPoint = new CPitchPatternPoint (pos, pitch);
	PitchSeries.push_back (*pNewPoint);
	delete pNewPoint;
}

////////////////////////////////////////////////////////////////////////////////
// PrintPitchPoints
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_PhonemeInfo::PrintPitchPoints ()
{
	for (int i=0; i < PitchSeries.size(); i++)
		{
		cout << PitchSeries[i].GetPos() << " " << PitchSeries[i].GetPitch() << " ";
		}
	cout << endl;
}

////////////////////////////////////////////////////////////////////////////////
// OutputPitchPoints
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_PhonemeInfo::OutputPitchPoints (ofstream &MBRFile)
{
	for (int i=0; i < PitchSeries.size(); i++)
		{
		MBRFile << PitchSeries[i].GetPos() << " " << PitchSeries[i].GetPitch() << " ";
		}
	MBRFile << endl;
}

////////////////////////////////////////////////////////////////////////////////
// OutputPitchPoints
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_PhonemeInfo::OutputPitchPoints (string &MBROLA_Utterance)
	{
	for (int i=0; i < PitchSeries.size(); i++)
		{
		char *temp;
		temp = ConvertIntegerToString (PitchSeries[i].GetPos());
		MBROLA_Utterance += temp;
		delete temp;

		MBROLA_Utterance += " ";

		temp = ConvertIntegerToString (PitchSeries[i].GetPitch());
		MBROLA_Utterance += temp;
		delete temp;

		MBROLA_Utterance += " ";
		}
	MBROLA_Utterance += "\n";
}

////////////////////////////////////////////////////////////////////////////////
// PrintPhonemeInfo
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_PhonemeInfo::PrintPhonemeInfo ()
{
	cout << Phoneme[0] << Phoneme[1] << " " << Duration << " ";
	PrintPitchPoints ();
}

////////////////////////////////////////////////////////////////////////////////
// OutputMbrolaPhonemes
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_PhonemeInfo::OutputMbrolaPhonemes (ofstream &MBRFile)
{
	// Phoneme[0] filter
	if (Phoneme[0] == '#')
		MBRFile << "_";
	else
		MBRFile << Phoneme[0];

	// Phoneme[1] filter
	if (Phoneme[1] != '-')
		MBRFile << Phoneme[1] << " ";
	else
		MBRFile << " ";

	MBRFile << Duration << " ";

	OutputPitchPoints (MBRFile);
}

///////////////////////////////////////////////////////////////////////////
// OutputGSTPhonemes
//
// The only difference between this function and OutputMbrolaPhonemes is
// that the latter outputs each phoneme's MBROLA pitch points also.  This
// function does not.
///////////////////////////////////////////////////////////////////////////
void CTTS_PhonemeInfo::OutputGSTPhonemes (ofstream &GSTFile)
{
	// Phoneme[0] filter
	if (Phoneme[0] == '#')
		GSTFile << "_";
	else
		GSTFile << Phoneme[0];

	// Phoneme[1] filter
	if (Phoneme[1] != '-')
		GSTFile << Phoneme[1] << " ";
	else
		GSTFile << " ";

	GSTFile << Duration << endl;
}


////////////////////////////////////////////////////////////////////////////////
// OutputMbrolaPhonemes
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_PhonemeInfo::OutputMbrolaPhonemes (string &MBROLA_Utterance)
{
	string TempPhoneme;

	TempPhoneme = Phoneme[0];
	TempPhoneme += Phoneme[1];
	char *NewPhoneme = FilterPhoneme (TempPhoneme);

	MBROLA_Utterance += NewPhoneme[0];
	MBROLA_Utterance += NewPhoneme[1];
	MBROLA_Utterance += " ";

	char *TempString = ConvertIntegerToString (Duration);
	MBROLA_Utterance += TempString;
	MBROLA_Utterance += " ";

	OutputPitchPoints (MBROLA_Utterance);
	delete TempString;
	delete NewPhoneme;
}

////////////////////////////////////////////////////////////////////////////////
// FilterPhoneme
//
////////////////////////////////////////////////////////////////////////////////
char *CTTS_PhonemeInfo::FilterPhoneme (string &Phoneme)
{
	char *NewPhoneme;

	if (Phoneme == "ch")
		{
		NewPhoneme = strdup ("tS");
		}
	else if (Phoneme == "jh")
		{
		NewPhoneme = strdup ("dZ");
		}
	else if (Phoneme == "ng")
		{
		NewPhoneme = strdup ("N ");
		}
	else if (Phoneme == "th")
		{
		NewPhoneme = strdup ("T ");
		}
	else if (Phoneme == "dh")
		{
		NewPhoneme = strdup ("D ");
		}
	else if (Phoneme == "sh")
		{
		NewPhoneme = strdup ("S ");
		}
	else if (Phoneme == "zh")
		{
		NewPhoneme = strdup ("Z ");
		}
	else if (Phoneme == "y-")
		{
		NewPhoneme = strdup ("j ");
		}
	else if (Phoneme == "ii")
		{
		NewPhoneme = strdup ("i:");
		}
	else if (Phoneme == "aa")
		{
		NewPhoneme = strdup ("A:");
		}
	else if (Phoneme == "oo")
		{
		NewPhoneme = strdup ("O:");
		}
	else if (Phoneme == "uu")
		{
		NewPhoneme = strdup ("u:");
		}
	else if (Phoneme == "@@")
		{
		NewPhoneme = strdup ("3:");
		}
	else if (Phoneme == "i-")
		{
		NewPhoneme = strdup ("I ");
		}
	else if (Phoneme == "a-")
		{
		NewPhoneme = strdup ("{ ");
		}
	else if (Phoneme == "uh")
		{
		NewPhoneme = strdup ("V ");
		}
	else if (Phoneme == "o-")
		{
		NewPhoneme = strdup ("Q ");
		}
	else if (Phoneme == "u-")
		{
		NewPhoneme = strdup ("U ");
		}
	else if (Phoneme == "ei")
		{
		NewPhoneme = strdup ("eI");
		}
	else if (Phoneme == "ai")
		{
		NewPhoneme = strdup ("aI");
		}
	else if (Phoneme == "oi")
		{
		NewPhoneme = strdup ("OI");
		}
	else if (Phoneme == "ou")
		{
		NewPhoneme = strdup ("@U");
		}
	else if (Phoneme == "au")
		{
		NewPhoneme = strdup ("aU");
		}
	else if (Phoneme == "i@")
		{
		NewPhoneme = strdup ("I@");
		}
	/* NO CONVERSION REQUIRED
	else if (Phoneme == "e@")
		{
		NewPhoneme = strdup ("E@");
		}
	/**/
	else if (Phoneme == "u@")
		{
		NewPhoneme = strdup ("U@");
		}
	else if (Phoneme == "# ")
		{
		NewPhoneme = strdup ("_ ");
		}
	else if (Phoneme[1] == '-')
		{
		NewPhoneme = (char *) malloc (2 * sizeof (char));
		NewPhoneme[0] = Phoneme[0];
		NewPhoneme[1] = ' ';
		}
	else
		{
		NewPhoneme = (char *) malloc (2 * sizeof (char));
		NewPhoneme[0] = Phoneme[0];
		NewPhoneme[1] = Phoneme[1];
		}

	return NewPhoneme;
}

////////////////////////////////////////////////////////////////////////////////
// ConvertIntegerToString
//
////////////////////////////////////////////////////////////////////////////////
char *CTTS_PhonemeInfo::ConvertIntegerToString (int Number)
{
	char *pString;
	string TempString;

	if (Number < 0)
		{
		TempString = "-";
		Number = -Number;
		}

	do
		{
		TempString += Number % 10 + '0';
		Number = Number / 10;
		} while (Number > 0);

	int First = 0;
	int Last = TempString.length()-1;
	char temp;
	while (First < Last)
		{
		temp = TempString[First];
		TempString[First] = TempString[Last];
		TempString[Last] = temp;
		First++;
		Last--;
		}
	
	pString = strdup (TempString.c_str());
	return pString;
}