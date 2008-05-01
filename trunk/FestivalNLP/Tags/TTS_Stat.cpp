////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Stat CLASS Implementation file.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_Stat.h"

////////////////////////////////////////////////////////////////////////////////
// ProcessTag
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Stat::ProcessTag (CTTS_SMLNode *pNode)
{
	assert (pNode != NULL);

	if (pNode->Children())
		GetInfo (pNode->Children());

	OutputStats ();
}

////////////////////////////////////////////////////////////////////////
// GetDurationInfo
//
// 
////////////////////////////////////////////////////////////////////////
void CTTS_Stat::GetDurationInfo (CTTS_SMLNode *pNode)
{
	if (pNode->IsTextNode ())
		{
		}

	if (pNode->Children ())
		{
		GetDurationInfo (pNode->Children());
		}

	if (pNode->Next ())
		{
		GetDurationInfo (pNode->Next());
		}
}

////////////////////////////////////////////////////////////////////////
// GetPitchInfo
//
// Gets Pitch information
////////////////////////////////////////////////////////////////////////
void CTTS_Stat::GetPitchInfo (CTTS_SMLNode *pNode)
{
	if (pNode->IsTextNode ())
		{
		int NumOfWords;
		int NumOfPhonemes;
		int NumOfPoints;
		int Pitch;

		NumOfWords = pNode->UtteranceInfo()->WordList.size();
		for (int w=0; w < NumOfWords; w++)
			{
			NumOfPhonemes = pNode->UtteranceInfo()->WordList[w].PhonemeList.size();
			for (int p=0; p < NumOfPhonemes; p++)
				{
				vector <CTTS_PhonemeInfo> &PhonemeList = pNode->UtteranceInfo()->WordList[w].PhonemeList;
				NumOfPoints = PhonemeList[p].PitchSeries.size();
				for (int i=0; i < NumOfPoints; i++)
					{
					Pitch = PhonemeList[p].PitchSeries[i].GetPitch ();
					//////////////////////////////////////
					// Update stats
					m_Count++;
					if (Pitch > m_Max)
						m_Max = Pitch;
					if (Pitch < m_Min)
						m_Min = Pitch;
					m_Sum += Pitch;
					}
				}
			}
		}

	if (pNode->Children ())
		{
		GetPitchInfo (pNode->Children());
		}

	if (pNode->Next ())
		{
		GetPitchInfo (pNode->Next());
		}
}

////////////////////////////////////////////////////////////////////////
// GetPitchInfo
//
// Gets Pitch information
////////////////////////////////////////////////////////////////////////
void CTTS_Stat::GetInfo (CTTS_SMLNode *pNode)
{
	assert (pNode != NULL);

	if (pNode->IsTextNode ())
		{
		int NumOfWords;
		int NumOfPhonemes;
		int NumOfPoints;
		int Pitch;
		int Duration;

		NumOfWords = pNode->UtteranceInfo()->WordList.size();
		for (int w=0; w < NumOfWords; w++)
			{
			NumOfPhonemes = pNode->UtteranceInfo()->WordList[w].PhonemeList.size();
			for (int p=0; p < NumOfPhonemes; p++)
				{
				vector <CTTS_PhonemeInfo> &PhonemeList = pNode->UtteranceInfo()->WordList[w].PhonemeList;
				NumOfPoints = PhonemeList[p].PitchSeries.size();
				for (int i=0; i < NumOfPoints; i++)
					{
					Pitch = PhonemeList[p].PitchSeries[i].GetPitch ();
					//////////////////////////////////////
					// Update Pitch stats
					m_Count++;
					if (Pitch > m_Max)
						m_Max = Pitch;
					if (Pitch < m_Min)
						m_Min = Pitch;
					m_Sum += Pitch;
					}

				//////////////////////////////////////
				// Update Duration stats
				Duration = PhonemeList[p].GetDuration ();

				m_DurCount++;
				if (Duration > m_DurMax)
					m_DurMax = Duration;
				if (Duration < m_DurMin)
					m_DurMin = Duration;
				m_DurSum += Duration;
				}
			}
		}

	if (pNode->Children ())
		{
		GetInfo (pNode->Children());
		}

	if (pNode->Next ())
		{
		GetInfo (pNode->Next());
		}
}

////////////////////////////////////////////////////////////////////////////////
// GetTimePitchInfo
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Stat::GetTimePitchInfo (const char *Filename)
{
	CTTS_SimpleInFile MBRFile;

	if (!MBRFile.Open (Filename))
		{
		cerr << "File error in CTTS_Stat::OutputTimePitchInfo () : Could not open file for reading\n";
		exit (1);
		}

	string Phoneme;
	int Duration;
	int Time = 0;
	char *pLine;

	while (!MBRFile.eof ())
		{
		if ((MBRFile >> Phoneme) < 0)
			continue;
		MBRFile >> Duration;
		pLine = MBRFile.GetLine (200, '\n');

		GetPitchPoint (pLine, Phoneme, Duration, Time);

		Time += Duration;
		delete pLine;
		}

	MBRFile.Close ();
}

////////////////////////////////////////////////////////////////////////////////
// GetPitchPoint
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Stat::GetPitchPoint (char *pLine, string &Phoneme, int Duration, int Time)
{
	int Pitch, Point;
	double temp;
	string str = pLine;
	int Length = str.size();

	if (Phoneme == "_")
		{
		Pitch = 0;
		Point = Time;
		//cout << Point << " " << Pitch << " \n";
		}
	else
		{
		int i = 1;
		while (i < str.size())
			{
			temp = (double)(NextNumber (str, i)) / 100 * Duration;
			Point = (int) temp + Time;
			Pitch = NextNumber (str, i);
			//cout << Point << " " << Pitch << " \n";

			//////////////////////////////////////
			// Update stats
			m_Count++;
			if (Pitch > m_Max)
				m_Max = Pitch;
			if (Pitch < m_Min)
				m_Min = Pitch;
			m_Sum += Pitch;
			}
		}
}

////////////////////////////////////////////////////////////////////////////////
// NextNumber
//
////////////////////////////////////////////////////////////////////////////////
int CTTS_Stat::NextNumber (string &str, int &i)
{
	int Value = 0;
	int Digit;

	while (!isspace (str[i]))
		{
		Digit = (int) (str[i++] - '0');
		Value = (Value * 10) + Digit;
		}

	i++;

	return Value;
}

////////////////////////////////////////////////////////////////////////////////
// OutputStats
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Stat::OutputStats ()
{
	cout << "----Pitch stats----\n";
	cout << "Count = " << m_Count << endl;
	cout << "Min = " << m_Min << endl;
	cout << "Max = " << m_Max << endl;
	cout << "Average = " << (m_Sum / m_Count) << endl;

	cout << "----Duration stats----\n";
	cout << "Count = " << m_DurCount << endl;
	cout << "Min = " << m_DurMin << endl;
	cout << "Max = " << m_DurMax << endl;
	cout << "Average = " << (m_DurSum / m_DurCount) << endl;
}