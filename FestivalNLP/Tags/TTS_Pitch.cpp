////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Pitch Class IMPLEMENTATION file
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_Pitch.h"

////////////////////////////////////////////////////////////////////////////////
// ProcessTag
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Pitch::ProcessTag (CTTS_SMLNode *pNode)
{
	assert ((pNode != NULL) && (pNode->Children() != NULL));

	///////////////////////////////////////////////////////
	// Process "middle" pitch attribute
	///////////////////////////////////////////////////////
	char *Pitch = GetAttribute (pNode, "middle");
	if (Pitch != NULL)
		{
		string Temp = string (Pitch);
		SetPitchFactor (Temp);
		if (pNode->Children())
			{
			ModifyUtterancePitchAvg (pNode->Children());
			}
		delete Pitch;
		}

	///////////////////////////////////////////////////////
	// Process "range" pitch attribute
	///////////////////////////////////////////////////////
	char *Range = GetAttribute (pNode, "range");
	if (Range != NULL)
		{
		string Temp = string (Range);
		SetRangeFactor (Temp);
		if (pNode->Children())
			{
			CTTS_Stat *pStat = new CTTS_Stat;
			pStat->GetPitchInfo (pNode->Children());
			double Average = pStat->GetAverage ();
			//pStat->OutputStats();

			ModifyUtterancePitchRange (pNode->Children(), Average);

			delete pStat;
			}
		delete Range;
		}

}

////////////////////////////////////////////////////////////////////////////////
// ModifyUtterancePitchRange
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Pitch::ModifyUtterancePitchRange (CTTS_SMLNode *pNode, double Average)
{
	if (pNode->IsTextNode ())
		{
		int NumOfWords;
		int NumOfPhonemes;
		int NumOfPoints;
		int Pitch;
		int Diff;

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
					Diff = Pitch - Average;

					//cout << PhonemeList[p].GetPhoneme()->c_str() << ", Pitch = " << Pitch << ", Diff = " << Diff;
					Pitch += (m_RangeFactor * Diff);

					//////////////////////////////////////////////////////////////////
					// This code is stopping intonation from going too low or too high.
					// This is TEMPORARY, and should be changed to follow an appropriate
					// function.
					int LOWER_BOUND = 80;
					int UPPER_BOUND = 155;
					if (Pitch < LOWER_BOUND)
						{
						Pitch = LOWER_BOUND;
						PhonemeList[p].PitchSeries[i].SetPitch (LOWER_BOUND);
						//cout << ", New Pitch = " << Pitch << " *" << endl;
						}
					else if (Pitch > UPPER_BOUND)
						{
						Pitch = UPPER_BOUND;
						PhonemeList[p].PitchSeries[i].SetPitch (Pitch);
						//cout << ", New Pitch = " << Pitch << " *" << endl;
						}
					else
						{
						PhonemeList[p].PitchSeries[i].SetPitch (Pitch);
						//cout << ", New Pitch = " << Pitch << endl;
						}
					//
					//////////////////////////////////////////////////////////////////
					}
				}
			}
		}

	if (pNode->Children ())
		{
		ModifyUtterancePitchRange (pNode->Children(), Average);
		}

	if (pNode->Next ())
		{
		ModifyUtterancePitchRange (pNode->Next(), Average);
		}
}

////////////////////////////////////////////////////////////////////////////////
// ModifyUtterancePitchAvg
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Pitch::ModifyUtterancePitchAvg (CTTS_SMLNode *pCurrNode)
{
	// Modify phoneme durations if this is a text node
	if (pCurrNode->IsTextNode ())
		{
		int NumOfPhonemes;
		int NumOfPitchPoints;
		int NumOfWords = pCurrNode->UtteranceInfo()->GetNumOfWords ();
		for (int Word=0; Word < NumOfWords; Word++)
			{
			NumOfPhonemes = pCurrNode->UtteranceInfo()->GetNumOfPhonemes (Word);
			for (int Phoneme=0; Phoneme < NumOfPhonemes; Phoneme++)
				{
				NumOfPitchPoints = pCurrNode->UtteranceInfo()->GetPitchPointCount (Word, Phoneme);
				int Pitch;
				for (int PP=0; PP < NumOfPitchPoints; PP++)
					{
					Pitch = pCurrNode->UtteranceInfo()->GetPitch (Word, Phoneme, PP);
					Pitch = (int) (Pitch * m_PitchFactor);
					pCurrNode->UtteranceInfo()->SetPitch (Word, Phoneme, PP, Pitch);
					}
				}
			}
		}

	// Visit next
	if (pCurrNode->Next())
		{
		ModifyUtterancePitchAvg (pCurrNode->Next());
		}

	// Visit children, but only if the current node isn't a
	// Pitch node (don't want to interfere with other Pitch nodes).
	if ((!pCurrNode->IsNode("pitch")) && (pCurrNode->Children()))
		{
		ModifyUtterancePitchAvg (pCurrNode->Children());
		}
}

////////////////////////////////////////////////////////////////////////////////
// SetPitchFactor
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Pitch::SetPitchFactor (string &Pitch)
{
	if (Pitch == "highest")
		{
		m_PitchFactor = HIGHEST;
		}
	else if (Pitch == "high")
		{
		m_PitchFactor = HIGH;
		}
	else if (Pitch == "medium")
		{
		m_PitchFactor = MEDIUM;
		}
	else if (Pitch == "low")
		{
		m_PitchFactor = LOW;
		}
	else if (Pitch == "lowest")
		{
		m_PitchFactor = LOWEST;
		}
	else if (ValidPercentage (Pitch.c_str()))
		{
		// delete '%' char
		Pitch [Pitch.size()-1] = '\0';
		double temp = (double) StrToInt (Pitch.c_str()) / 100;
		m_PitchFactor += (m_PitchFactor * temp);
		}
}

////////////////////////////////////////////////////////////////////////////////
// SetRangeFactor
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Pitch::SetRangeFactor (string &Range)
{
	if (ValidPercentage (Range.c_str()))
		{
		Range [Range.size()-1] = '\0';
		m_RangeFactor = (double) StrToInt (Range.c_str()) / 100;
		}
}