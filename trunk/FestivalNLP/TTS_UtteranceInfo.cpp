////////////////////////////////////////////////////////////////////////////////
//
// CTTS_UtteranceInfo IMPLEMENTATION File.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_UtteranceInfo.h"

////////////////////////////////////////////////////////////////////////////////
// AddWord
// 
////////////////////////////////////////////////////////////////////////////////
void CTTS_UtteranceInfo::AddWord (string &NewWord)
{
	AddWord (NewWord, 0);
}

////////////////////////////////////////////////////////////////////////////////
// AddWord
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_UtteranceInfo::AddWord (string &NewWord, int Start)
{
	CTTS_WordInfo *pWord;
	pWord = new CTTS_WordInfo (NewWord, Start);
	WordList.push_back (*pWord);
	delete pWord;
}

////////////////////////////////////////////////////////////////////////////////
// InsertWord
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_UtteranceInfo::InsertWord (string &NewWord, int Pos)
{
	WordListType::iterator Iter;
	
	Iter = WordList.begin();

	for (int i=0; i < WordList.size(); i++)
		{
		if (i == Pos)
			break;
		Iter++;
		}

	CTTS_WordInfo *pWord;
	pWord = new CTTS_WordInfo (NewWord, 0);
	WordList.insert (Iter, *pWord);
	delete pWord;
}

////////////////////////////////////////////////////////////////////////////////
// PrintUtteranceInfo
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_UtteranceInfo::PrintUtteranceInfo ()
{
	for (int i=0; i < WordList.size(); i++)
		{
		WordList[i].PrintWordInfo ();
		}
}

////////////////////////////////////////////////////////////////////////////////
// OutputPhonemesInMbrolaFormat
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_UtteranceInfo::OutputPhonemesInMbrolaFormat (ofstream &MBRFile)
{
	// output each WordList item to MBRFile
	for (int i=0; i < WordList.size(); i++)
		{
		WordList[i].OutputMbrolaWords(MBRFile);
		}
}

////////////////////////////////////////////////////////////////////////////////
// OutputPhonemesInMbrolaFormat
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_UtteranceInfo::OutputPhonemesInMbrolaFormat (string &MBROLA_Utterance)
{
	// output each WordList item to MBRFile
	for (int i=0; i < WordList.size(); i++)
		{
		WordList[i].OutputMbrolaWords(MBROLA_Utterance);
		}
}

////////////////////////////////////////////////////////////////////////////////
// OutputPhonemesInGSTFormat
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_UtteranceInfo::OutputPhonemesInGSTFormat (ofstream &GSTFile)
{
	// output each WordList item to GSTFile
	for (int i=0; i < WordList.size(); i++)
		{
		WordList[i].OutputGSTWords(GSTFile);
		}
}

////////////////////////////////////////////////////////////////////////////////
// GetWords
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_UtteranceInfo::GetWords (const char *Filename)
{	
	CTTS_SimpleInFile WordFile;				// open input word file for reading
	string Word;
	int StartTime;
	double temp;

	if (!WordFile.Open (Filename))
		{
		cerr << "File error in CTTS_UtteranceInfo::GetWords() : Could not open " << Filename << " for reading\n";
		exit (8);
		}

	// first line of file always contains '#' symbol.
	WordFile >> Word;

	// read in contents and fill WordInfo data structure
	int i = 0;
	while (!WordFile.eof())
		{
		// Read info
		if ((WordFile >> temp) < 0)
			continue;

		StartTime = (int) (temp * 1000);
		WordFile >> temp;
		WordFile >> Word;
	
		// Insert new word
		AddWord (Word, StartTime);

		// debug
		//cout << i++ << ": " << StartTime << " " << Word << "!" << endl;
		}

	// Output warning message if no words were inserted
	if (WordList.size() == 0)
		cout << "Warning in CTTS_UtteranceInfo::GetWords() : no words inserted\n";

	// close input file
	WordFile.Close ();
}

////////////////////////////////////////////////////////////////////////////////
// SetPhonemePitchInfo
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_UtteranceInfo::SetPhonemePitchInfo (char *Line, int wptr, int phptr)
{
	assert (Line != NULL);

	int Pitch;							// Phoneme's pitch
	int Position;						// Target position of pitch within Phoneme (in %)
	int i = 1;

	string str = Line;
	str += "!";

	while (str[i] != '!')
		{
		Position = NextNumber (str, i);
		Pitch = NextNumber (str, i);
		WordList[wptr].PhonemeList[phptr].InsertPitchPoint (Position, Pitch);
		}
}

////////////////////////////////////////////////////////////////////////////////
// GetPhonemes
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_UtteranceInfo::GetPhonemes (const char *Filename)
{
	const int TOLERANCE = 10;
	const int MAX_CHARS	= 200;			// max characters on a line;

	CTTS_SimpleInFile MBRFile;			// open input MBROLA format file for reading
	string Phoneme;						// Phoneme
	int PhonemeDuration;				// the Phoneme's duration (in ms)
	int WordDuration;
	char *pLine;						// temporary line read from file

	// open file for reading
	if (!MBRFile.Open (Filename))
		{
		cerr << "File Error in CTTS_UtteranceInfo::GetPhonemes() : Could not open " << Filename << " for reading\n";
		exit (8);
		}

	try 
		{
		// Initial silent phoneme (always present at beginning of file).
		// Additional error checking has been done so that nothing is done
		// if it is an empty file.
		if ((MBRFile >> Phoneme) < 0)
			throw "Warning in CTTS_UtteranceInfo::GetPhonemes() : empty file\n";

		// Will fall into this when wrd file output by Festival using function
		// (utt.save.words utt1 "filename") is empty, yet phonemes are output
		// in its mbr file.  This seems to be a bug in the Festival code.
		if (WordList.size() == 0)
			{
			throw "Warning in CTTS_UtteranceInfo::GetPhonemes() : no words in word file, therefore skipping phoneme file\n";
			}

		MBRFile >> PhonemeDuration;

		// allocate rest of phonemes to words in utterance
		WordDuration = (WordList[0].GetStartTime() - PhonemeDuration);
		int NumOfWords = WordList.size();
		int time = 0;
		int wptr = 0;
		bool LastPhoneme;

		while (!MBRFile.eof())
			{
			if (wptr >= NumOfWords)
				break;

			// Get Phoneme information for current word
			LastPhoneme = false;
			while (!LastPhoneme)
				{
				// Get Phoneme and its duration
				MBRFile >> Phoneme;
				MBRFile >> PhonemeDuration;
				WordList[wptr].AddPhoneme (Phoneme, PhonemeDuration);
				time += PhonemeDuration;
				if (abs(time-WordDuration) <= TOLERANCE)
					{
					LastPhoneme = true;
					time = 0;
					}
	
				// Get Phoneme pitch information
				pLine = MBRFile.GetLine (MAX_CHARS, '\n');
				SetPhonemePitchInfo (pLine, wptr, WordList[wptr].PhonemeList.size()-1);
				delete pLine;
				}
	
			wptr++;
			if (wptr < NumOfWords)
				WordDuration = WordList[wptr].GetStartTime() - WordList[wptr-1].GetStartTime();
			}
		}

	catch (char *Error)
		{
		cerr << Error;
		}
	catch (...)
		{
		cerr << "Error in CTTS_UtteranceInfo::GetPhonemes (): This is probably because an item was referenced in an empty vector object.\n";
		}

		MBRFile.Close ();
}

////////////////////////////////////////////////////////////////////////////////
// GetNumOfWords
//
////////////////////////////////////////////////////////////////////////////////
int CTTS_UtteranceInfo::GetNumOfWords ()
{	
	return (WordList.size ());	
}

////////////////////////////////////////////////////////////////////////////////
// GetNumOfPhonemes
//
////////////////////////////////////////////////////////////////////////////////
int CTTS_UtteranceInfo::GetNumOfPhonemes (int WordIndex)
{
	assert ((WordIndex >= 0) && (WordIndex < WordList.size()));

	return (WordList[WordIndex].PhonemeList.size());
}

////////////////////////////////////////////////////////////////////////////////
// GetPitchPointCount
//
////////////////////////////////////////////////////////////////////////////////
int CTTS_UtteranceInfo::GetPitchPointCount (int WordIndex, int PhonemeIndex)
{
	assert ((WordIndex >= 0) && (WordIndex < WordList.size()));
	assert ((PhonemeIndex >= 0) && (PhonemeIndex < WordList[WordIndex].PhonemeList.size()));

	return (WordList[WordIndex].PhonemeList[PhonemeIndex].PitchSeries.size());
}

////////////////////////////////////////////////////////////////////////////////
// GetPhonemeDuration
//
////////////////////////////////////////////////////////////////////////////////
int CTTS_UtteranceInfo::GetPhonemeDuration (int WordIndex, int PhonemeIndex)
{
	assert ((WordIndex >= 0)&& (WordIndex < WordList.size()));
	assert ((PhonemeIndex >= 0) && (PhonemeIndex < WordList[WordIndex].PhonemeList.size()));
		
	return (WordList[WordIndex].PhonemeList[PhonemeIndex].GetDuration ());
}

////////////////////////////////////////////////////////////////////////////////
// SetPhonemeDuration
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_UtteranceInfo::SetPhonemeDuration (int WordIndex, int PhonemeIndex, int Duration)
{
	assert ((WordIndex >= 0) && (WordIndex < WordList.size()));
	assert ((PhonemeIndex >= 0) && (PhonemeIndex < WordList.at (WordIndex).PhonemeList.size()));
	
	WordList[WordIndex].PhonemeList[PhonemeIndex].SetDuration (Duration);
} 

////////////////////////////////////////////////////////////////////////////////
// SetPitch
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_UtteranceInfo::SetPitch (int WordIndex, int PhonemeIndex, int PitchPoint, int Pitch)
{
	assert ((WordIndex >= 0) && (WordIndex < WordList.size()));
	assert ((PhonemeIndex >= 0) && (PhonemeIndex < WordList[WordIndex].PhonemeList.size()));
	assert ((PitchPoint >= 0) && (PitchPoint < WordList[WordIndex].PhonemeList[PhonemeIndex].PitchSeries.size()));

	WordList[WordIndex].PhonemeList[PhonemeIndex].PitchSeries[PitchPoint].SetPitch (Pitch);
}

////////////////////////////////////////////////////////////////////////////////
// GetPitch
//
////////////////////////////////////////////////////////////////////////////////
int CTTS_UtteranceInfo::GetPitch (int WordIndex, int PhonemeIndex, int PitchPoint)
{
	assert ((WordIndex >= 0) && (WordIndex < WordList.size()));
	assert ((PhonemeIndex >= 0) && (PhonemeIndex < WordList[WordIndex].PhonemeList.size()));
	assert ((PitchPoint >= 0) && (PitchPoint < WordList[WordIndex].PhonemeList[PhonemeIndex].PitchSeries.size()));

	return (WordList[WordIndex].PhonemeList[PhonemeIndex].PitchSeries[PitchPoint].GetPitch());
}
