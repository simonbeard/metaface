////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Emotion class IMPLEMENTATION file.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_Emotion.h"

////////////////////////////////////////////////////////////////////////
// ProcessTag
//
////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::ProcessTag (CTTS_SMLNode *pNode)
{
	cout << "Processing Emotion tag\n";
}

////////////////////////////////////////////////////////////////////////
// ModifySpeechRate
// 
// Modifies speech rate of all children nodes of pNode, as specified by
// Amount parameter.  Amount parameter should be in the format:
// {whitespace} ('+'|'-') (digit)+ '%'
// If not in correct format, it will be ignored when tag node is processed.
////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::ModifySpeechRate (CTTS_SMLNode *pNode, const char *Amount)
{
	assert (pNode != NULL);

	CTTS_SMLNode *pRateNode = pNode->InsertTag ("rate");

	if (pRateNode)
		{
		pRateNode->InsertAttribute ("speed", Amount);

		CTTS_Rate *pTag = new CTTS_Rate;
		pTag->ProcessTag (pRateNode);
		delete pTag;

		pNode->DeleteTag (pRateNode);
		}
}

////////////////////////////////////////////////////////////////////////
// ModifyPitch
//
////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::ModifyPitch (CTTS_SMLNode *pNode, const char *Middle, const char *Range)
{
	assert (pNode != NULL);
	
	// To create temporary pitch tag node, at least one parameter must have been set.
	if (Middle || Range)
		{
		CTTS_SMLNode *pPitchNode = pNode->InsertTag ("pitch");
		if (pPitchNode)
			{
			if (Middle)
				{
				pPitchNode->InsertAttribute ("middle", Middle);
				}
			if (Range)
				{
				pPitchNode->InsertAttribute ("range", Range);
				}

			CTTS_Pitch *pTag = new CTTS_Pitch;
			pTag->ProcessTag (pPitchNode);
			delete pTag;

			pNode->DeleteTag (pPitchNode);
			}
		}
}

////////////////////////////////////////////////////////////////////////
// ModifyVolume
//
////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::ModifyVolume (CTTS_SMLNode *pNode, double Amount)
{
}

////////////////////////////////////////////////////////////////////////
// ActivateProsodicEffect
//
// Activates one of the following effects:
//		EFFECT_1: Increase the pitch of stressed vowels.		
//		EFFECT_2: Increase the duration of stressed vowels.
//		EFFECT_3: Increase the articulation precision.
//		EFFECT_4: Decrease the articulation precision.
//		EFFECT_5: Increase the rate of pitch declination.
//		EFFECT_6: Eliminate abrupt changes in pitch between phonemes.
//		EFFECT_7: Add downward pitch inflections at word endings.
//		EFFECT_8: Reduce the amount of pitch fall at end of utterance
//		EFFECT_9: Replace upward inflections iwth downward inflections.
//		EFFECT_10: Modify phoneme durations for regular stressing.
//		EFFECT_11: Add pauses after long words.
//		EFFECT_12: Lower pitch of word before every pause.
//		EFFECT_13: Increase final lowering of utterance.
////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::ActivateProsodicEffect (CTTS_SMLNode *pNode, enum ProsodicEffect Effect)
{
	// Process desired prosodic effect.
	switch (Effect)
		{
		// Increase the pitch of stressed vowels.
		case EFFECT_1:		{
							CTTS_Stat *pStat = new CTTS_Stat;
							pStat->GetInfo (pNode->Children());
							AddEffect1 (pNode->Children(), pStat);
							delete pStat;
							break;
							}

		// Increase the duration of stressed vowels.
		case EFFECT_2:		{
							CTTS_Stat *pStat = new CTTS_Stat;
							pStat->GetInfo (pNode->Children());
							AddEffect2 (pNode->Children(), pStat);
							delete pStat;
							break;
							}

		// Increase the articulation precision.
		case EFFECT_3:		AddEffect3 (pNode->Children());
							break;

		// Decrease the articulation precision.
		case EFFECT_4:		AddEffect4 (pNode->Children());
							break;

		// Increase the rate of pitch declination.
		case EFFECT_5:		AddEffect5 (pNode->Children());
							break;

		// Eliminate abrupt changes in pitch between phonemes.
		case EFFECT_6:		{
							// Obtain utterance stats.
							CTTS_Stat *pStat = new CTTS_Stat;
							pStat->GetPitchInfo (pNode->Children());
							AddEffect6 (pNode->Children(), pStat);
							delete pStat;
							break;
							}

		// Add downward pitch inflections at word endings.
		case EFFECT_7:		AddEffect7 (pNode->Children());
							break;

		// Reduce the amount of pitch fall at end of utterance
		case EFFECT_8:		AddEffect8 (pNode->Children());
							break;

		// Replace upward inflections iwth downward inflections.
		case EFFECT_9:		AddEffect9 (pNode->Children());
							break;

		// Modify phoneme durations for regular stressing.
		case EFFECT_10:		{
							CTTS_Stat *pStat = new CTTS_Stat;
							pStat->GetInfo (pNode->Children());
							AddEffect10 (pNode->Children(), pStat);
							delete pStat;
							break;
							}

		// Add pauses after long words.
		case EFFECT_11:		AddEffect11 (pNode->Children());
							break;

		// Lower pitch of word before every pause.
		case EFFECT_12:		AddEffect12 (pNode->Children());
							break;

		// Increase final lowering of utterance.
		case EFFECT_13:		AddEffect13 (pNode->Children());
							break;
		}
}

////////////////////////////////////////////////////////////////////////
// AddEffect1
//
////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::AddEffect1 (CTTS_SMLNode *pNode, CTTS_Stat *pStat)
{
	assert ((pNode != NULL) && (pStat != NULL));
	
	if (pNode->IsTextNode ())
		{
		ChangeStressedVowelPitch (pNode, pStat);
		}

	if (pNode->Next())
		{
		AddEffect1 (pNode->Next(), pStat);
		}

	if (pNode->Children())
		{
		AddEffect1 (pNode->Children(), pStat);
		}
}

////////////////////////////////////////////////////////////////////////
// AddEffect2
//
// Increase the duration of stressed vowels.
// The phoneme data is scanned, and the duration of all stressed vowels
// is increased by 50%.
////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::AddEffect2 (CTTS_SMLNode *pNode, CTTS_Stat *pStat)
{
	assert ((pNode != NULL) && (pStat != NULL));
	
	if (pNode->IsTextNode ())
		{
		ChangeStressedVowelDurations (pNode, pStat);
		}

	if (pNode->Next())
		{
		AddEffect2 (pNode->Next(), pStat);
		}

	if (pNode->Children())
		{
		AddEffect2 (pNode->Children(), pStat);
		}
}

////////////////////////////////////////////////////////////////////////////////
// AddEffect3
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::AddEffect3 (CTTS_SMLNode *pNode)
{
}

////////////////////////////////////////////////////////////////////////////////
// AddEffect4
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::AddEffect4 (CTTS_SMLNode *pNode)
{
}

////////////////////////////////////////////////////////////////////////////////
// AddEffect5
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::AddEffect5 (CTTS_SMLNode *pNode)
{
}

////////////////////////////////////////////////////////////////////////
// AddEffect6
//
// Eliminate abrupt changes in pitch between phonemes.
////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::AddEffect6 (CTTS_SMLNode *pNode, CTTS_Stat *pStat)
{
	assert (pNode != NULL);

	if (pNode->IsTextNode ())
		{
		EliminateAbruptPitchChanges (pNode, pStat);
		}

	if (pNode->Next())
		{
		AddEffect6 (pNode->Next(), pStat);
		}

	if (pNode->Children())
		{
		AddEffect6 (pNode->Children(), pStat);
		}
}

////////////////////////////////////////////////////////////////////////////////
// AddEffect7
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::AddEffect7 (CTTS_SMLNode *pNode)
{
}

////////////////////////////////////////////////////////////////////////
// AddEffect8
//
// Reduces the amount of pitch fall at the end of the utterance.
// Utterances usually have a pitch drop in the final vowel and any
// following consonants.  This routine increases the pitch values of
// these phonemes by 15%, hence reducing the size of the terminal
// pitch fall.
////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::AddEffect8 (CTTS_SMLNode *pNode)
{
	assert (pNode != NULL);

	CTTS_SMLNode *pLastTextNode = SearchForLastTextNode (pNode);
	if (pLastTextNode)
		{
		ChangePitchLastWord (pLastTextNode, 0.15);
		}
}

////////////////////////////////////////////////////////////////////////////////
// AddEffect9
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::AddEffect9 (CTTS_SMLNode *pNode)
{
}

////////////////////////////////////////////////////////////////////////////////
// AddEffect10
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::AddEffect10 (CTTS_SMLNode *pNode, CTTS_Stat *pStat)
{
	assert (pNode != NULL);

	if (pNode->IsTextNode ())
		{
		ModifyForRegularStressing (pNode, pStat);
		}

	if (pNode->Next())
		{
		AddEffect10 (pNode->Next(), pStat);
		}

	if (pNode->Children())
		{
		AddEffect10 (pNode->Children(), pStat);
		}
}

////////////////////////////////////////////////////////////////////////
// AddEffect11
//
// Add pauses after long words.
////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::AddEffect11 (CTTS_SMLNode *pNode)
{
	assert (pNode != NULL);

	if (pNode->IsTextNode ())
		{
		AddPauseAfterLongWords (pNode);
		}

	if (pNode->Next())
		{
		AddEffect11 (pNode->Next());
		}

	if (pNode->Children())
		{
		AddEffect11 (pNode->Children());
		}
}

////////////////////////////////////////////////////////////////////////
// AddEffect12
//
// Scans the phoneme data in the utterance and lowers every word's pitch
// that occurs before a pause.
////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::AddEffect12 (CTTS_SMLNode *pNode)
{
	assert (pNode != NULL);

	if (pNode->IsTextNode ())
		{
		LowerWordBeforePauses (pNode);
		}

	if (pNode->Next())
		{
		AddEffect12 (pNode->Next());
		}

	if (pNode->Children())
		{
		AddEffect12 (pNode->Children());
		}
}

////////////////////////////////////////////////////////////////////////
// AddEffect13
//
// Lowers the pitch of the last word in the utterance.  To do this, the
// last text node in the sub-tree structure is found using the function
// SearchForLastTextNode ().
////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::AddEffect13 (CTTS_SMLNode *pNode)
{
	assert (pNode != NULL);

	CTTS_SMLNode *pLastTextNode = SearchForLastTextNode (pNode);
	if (pLastTextNode)
		{
		ChangePitchLastWord (pLastTextNode, -0.15);
		}
}

////////////////////////////////////////////////////////////////////////
// SearchForLastTextNode
//
// Returns the last text node in the sub-tree structure.  Basic approach
// is to recursively call this function in breadth-first fashion.  If
// this search proves unfruitful (dead-end or no text nodes exist there),
// then search is taken down a level, and breadth-first search is done
// there as well.
////////////////////////////////////////////////////////////////////////
CTTS_SMLNode *CTTS_Emotion::SearchForLastTextNode (CTTS_SMLNode *pNode)
{
	assert (pNode != NULL);

	if (pNode->Next() && pNode->Children())
		{
		CTTS_SMLNode *pTemp = SearchForLastTextNode (pNode->Next());
		if (pTemp)
			return pTemp;
		else
			return SearchForLastTextNode (pNode->Children());
		}
	else if (pNode->Next())
		{
		return SearchForLastTextNode (pNode->Next());
		}
	else if (pNode->Children())
		{
		return SearchForLastTextNode (pNode->Children());
		}
	else
		{
		if (pNode->IsTextNode())
			{
			return pNode;
			}
		else
			{
			return NULL;
			}
		}
}

/////////////////////////////////////////////////////////////////
// ChangePitchLastWord
//
// Changes the pitch of the last vowel-sounding phoneme (and any
// following consonant phonemes) of the lat word in the utterace
// by the amount specified by input parameter PitchChange.  The
// amount is given as a percentage.  So, the following changes
// apply given the value of PitchChange:
//		a) PitchChange = 0.3	- Pitch increases by 30%
//		b) PitchChange = -0.15	- Pitch decreases by 15%
/////////////////////////////////////////////////////////////////
void CTTS_Emotion::ChangePitchLastWord (CTTS_SMLNode *pNode, double PitchChange)
{
	assert ((pNode != NULL) && (pNode->IsTextNode()));

	const int LOWEST_PITCH = 70;

	int LastWord = pNode->UtteranceInfo()->WordList.size() - 1;

	int Ph;
	int NumOfPhonemes = pNode->UtteranceInfo()->WordList[LastWord].PhonemeList.size();
	Ph = NumOfPhonemes - 1;
	bool HitLastVowel = false;

	while (!HitLastVowel)
		{
		// Change pitch in phoneme by amount specified by PitchChange
		int NumOfPitchPoints = pNode->UtteranceInfo()->GetPitchPointCount (LastWord, Ph);
		int Pitch;
		for (int PP=0; PP < NumOfPitchPoints; PP++)
			{
			Pitch = pNode->UtteranceInfo()->GetPitch (LastWord, Ph, PP);
			Pitch = (int) Pitch + (Pitch * PitchChange);
			if (Pitch < LOWEST_PITCH)
				Pitch = LOWEST_PITCH;
			pNode->UtteranceInfo()->SetPitch (LastWord, Ph, PP, Pitch);
			}

		// Check if phoneme is a vowel sounding phoneme.  If it is, time to quit loop.
		string *pPhoneme = pNode->UtteranceInfo()->WordList[LastWord].PhonemeList[Ph].GetPhoneme();
		if (IsVowelSound (pPhoneme))
			{
			HitLastVowel = true;
			}
		delete pPhoneme;

		assert (Ph >= 0);
		Ph--;
		}
}

////////////////////////////////////////////////////////////////////////////////
// EliminateAbruptPitchChanges
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::EliminateAbruptPitchChanges (CTTS_SMLNode *pNode, CTTS_Stat *pStat)
{
	assert ((pNode != NULL) && (pNode->IsTextNode()) && (pStat != NULL));

	int NumOfWords = pNode->UtteranceInfo()->WordList.size();
	int W=0;		// Word index
	int Ph=0;		// Phoneme index
	int PP=-1;		// Pitch point index
	int Diff;

	// Set threshold to 10% of the Pitch Range
	int Range = pStat->GetRange();
	double PitchDifferenceThreshold = (double) Range * 0.1;
	int P1 = GetNextPitch (pNode, &W, &Ph, &PP);
	int P2;

	do
		{
		P2 = GetNextPitch (pNode, &W, &Ph, &PP);
		if (P2 >= 0)
			{
			Diff = abs (P1 - P2);
			if (Diff > PitchDifferenceThreshold)
				{
				// If difference between points is great, then increase the lower of the two pitch points
				// by 5% of the Pitch Range.  Note: Adding 0.5 for rounding.
				// Debug
				//cout << "P1 = " << P1 << ", P2 = " << P2 << ", Diff = " << Diff << endl;
				if (P1 < P2)
					{
					P1 += (Range * 0.05) + 0.5;
					pNode->UtteranceInfo()->WordList[W].PhonemeList[Ph].PitchSeries[PP].SetPitch (P1);
					// Debug
					//cout << "Set P1 to " << P1 << endl;
					}
				else
					{
					P2 += (Range * 0.05) + 0.5;
					pNode->UtteranceInfo()->WordList[W].PhonemeList[Ph].PitchSeries[PP].SetPitch (P2);
					// Debug
					//cout << "Set P2 to " << P2 << endl;
					}

				}

			P1 = P2;
			}
		} while (W < NumOfWords);
}

////////////////////////////////////////////////////////////////////////////////
// GetNextPitch
//
////////////////////////////////////////////////////////////////////////////////
int CTTS_Emotion::GetNextPitch (CTTS_SMLNode *pNode, int *Word, int *Phoneme, int *PitchPoint)
{
	int W = *Word;
	int Ph = *Phoneme;
	int PP = *PitchPoint;
	int Pitch = 0;


	// First check if can get another pitch point for current phoneme
	PP++;
	if (PP < pNode->UtteranceInfo()->WordList[W].PhonemeList[Ph].PitchSeries.size())
		{
		Pitch = pNode->UtteranceInfo()->WordList[W].PhonemeList[Ph].PitchSeries[PP].GetPitch();
		}

	// Can't get it, so need to move to next phoneme which has at least one pitch point.
	else
		{
		PP = 0;
		for ( ; ; )
			{

			// Check if current word has more phonemes
			Ph++;
			if (Ph < pNode->UtteranceInfo()->WordList[W].PhonemeList.size())
				{

				// It does, so check if current phoneme has a pitch point.
				if (pNode->UtteranceInfo()->WordList[W].PhonemeList[Ph].PitchSeries.size() > 0)
					{
					Pitch = pNode->UtteranceInfo()->WordList[W].PhonemeList[Ph].PitchSeries[PP].GetPitch();
					break;
					}
				}

			// Current word didn't have any more phonemes, so move to next word.
			else
				{
				Ph = -1;		// set to -1 because incremented at beginning of next iteration!
				W++;

				// Ran out of words, so quit.
				if (W >= pNode->UtteranceInfo()->WordList.size())
					{
					Pitch = -1;
					break;
					}
				}
			}
		}
	
	*Word = W;
	*Phoneme = Ph;
	*PitchPoint = PP;
	return Pitch;
}

////////////////////////////////////////////////////////////////////////////////
// AddPauseAfterLongWords
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::AddPauseAfterLongWords (CTTS_SMLNode *pNode)
{
	assert ((pNode != NULL) && (pNode->IsTextNode()));

	int NumOfWords = pNode->UtteranceInfo()->WordList.size();
	int W=0;

	while (W < NumOfWords)
		{
		int NumOfPhonemes = pNode->UtteranceInfo()->WordList[W].PhonemeList.size();
		if (NumOfPhonemes >= PHONEMES_IN_LONG_WORD)
			{
			// Debug
			//cout << "Found a long word - " << pNode->UtteranceInfo()->WordList[W].GetWord().c_str() << "\n";
			pNode->PushPhoneme (W, "_", LONG_WORD_PAUSE_LENGTH);
			LengthenPhonemesBeforePause (pNode, W);
			}
		W++;
		}
}

////////////////////////////////////////////////////////////////////////////////
// LengthenPhonemesBeforePause
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::LengthenPhonemesBeforePause (CTTS_SMLNode *pNode, int Word)
{
	assert ((pNode != NULL) && (pNode->IsTextNode()));
	assert ((Word >= 0) && (Word < pNode->UtteranceInfo()->WordList.size()));
	assert (pNode->UtteranceInfo()->WordList[Word].PhonemeList.size() >= PHONEMES_IN_LONG_WORD);

	// make LastPhoneme index into the last phoneme (should be pause phoneme)
	int LastPhoneme = pNode->UtteranceInfo()->WordList[Word].PhonemeList.size() - 1;
	int Curr = LastPhoneme - 2;

	while (Curr < LastPhoneme)
		{
		int Duration = (int) (pNode->UtteranceInfo()->WordList[Word].PhonemeList[Curr].GetDuration () * 1.5);
		if (Duration > MAX_DURATION)
			Duration = MAX_DURATION;
		pNode->UtteranceInfo()->WordList[Word].PhonemeList[Curr].SetDuration (Duration);
		Curr++;
		}
}

////////////////////////////////////////////////////////////////////////////////
// LowerWordBeforePauses
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::LowerWordBeforePauses (CTTS_SMLNode *pNode)
{
	assert ((pNode != NULL) && (pNode->IsTextNode()));

	int W;
	int Ph;
	
	for (W=0; W < pNode->UtteranceInfo()->WordList.size(); W++)
		{
		for (Ph=0; Ph < pNode->UtteranceInfo()->WordList[W].PhonemeList.size(); Ph++)
			{
			string *pPhoneme = pNode->UtteranceInfo()->WordList[W].PhonemeList[Ph].GetPhoneme();
			if ((*pPhoneme == "_") || (*pPhoneme == "#"))
				{
				// Lower pitch of word previous to pause.
				if (Ph > 0)
					{
					// Word is at W
					LowerWordInNode (pNode, W);
					}
				else
					{
					// Word is at W - 1
					if (W > 0)
						{
						// Word is at W-1
						LowerWordInNode (pNode, W-1);
						}
					else
						{
						// Can't do anything for now.  Need to find previous node (more tricky).
						//cout << "Want to lower word, but word is in node different to pause.  Needs to be implemented still.\n";
						//cout << "Word is previous to: " << pNode->UtteranceInfo()->WordList[W].GetWord().c_str() << endl;
						}
					}
				}
			delete pPhoneme;
			}
		}
}

///////////////////////////////////////////////////////////////////
// LowerWordInNode
//
// Lowers the word indexed by Word, in pNode's Utterance structure.
// This is done by lowering the pitch of the last syllable (last
// vowel phoneme and any following consonant phonemes) by 15%.
///////////////////////////////////////////////////////////////////
void CTTS_Emotion::LowerWordInNode (CTTS_SMLNode *pNode, int Word)
{
	double PITCH_LOWERING = 0.15;
	int LOWEST_PITCH = 70;

	int NumOfPhonemes = pNode->UtteranceInfo()->WordList[Word].PhonemeList.size();
	for (int Ph = NumOfPhonemes-1; Ph >= 0; Ph--)
		{
		int NumOfPitchPoints = pNode->UtteranceInfo()->GetPitchPointCount (Word, Ph);
		int Pitch;
		for (int PP=0; PP < NumOfPitchPoints; PP++)
			{
			Pitch = pNode->UtteranceInfo()->GetPitch (Word, Ph, PP);
			Pitch = (int) Pitch - (Pitch * PITCH_LOWERING);
			if (Pitch < LOWEST_PITCH)
				Pitch = LOWEST_PITCH;
			pNode->UtteranceInfo()->SetPitch (Word, Ph, PP, Pitch);
			}

		string *pPhoneme = pNode->UtteranceInfo()->WordList[Word].PhonemeList[Ph].GetPhoneme ();
		if (IsVowelSound (pPhoneme))
			{
			delete pPhoneme;
			break;
			}

		delete pPhoneme;
		}
}

///////////////////////////////////////////////////////////////////
// IsVowelSound
//
// Returns true if Phoneme is a vowel sounding phoneme.  For now,
// this is being determined via a big if-then-else tree.  A better
// algorithm should be implemented.  TEMPORARY.
///////////////////////////////////////////////////////////////////
bool CTTS_Emotion::IsVowelSound (string *Phoneme)
{
	if ((*Phoneme == "ii") || (*Phoneme == "aa") || (*Phoneme == "oo") || (*Phoneme == "uu") ||
		(*Phoneme == "@@") || (*Phoneme == "i") || (*Phoneme == "e") || (*Phoneme == "a") ||
		(*Phoneme == "uh") || (*Phoneme == "o") || (*Phoneme == "u") || (*Phoneme == "@") ||
		(*Phoneme == "ei") || (*Phoneme == "ai") || (*Phoneme == "oi") || (*Phoneme == "ou") ||
		(*Phoneme == "au") || (*Phoneme == "i@") || (*Phoneme == "e@") || (*Phoneme == "u@"))
			{
			return true;
			}
	else
			{
			return false;
			}
}

////////////////////////////////////////////////////////////////////////////////
// ChangeStressedVowelPitch
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::ChangeStressedVowelPitch (CTTS_SMLNode *pNode, CTTS_Stat *pStat)
{
	assert ((pNode != NULL) && (pStat != NULL));
	
	enum VowelType Type;
	int NumOfWords = pNode->UtteranceInfo()->WordList.size();
	int NumOfPhonemes;
	double PitchFactor;

	for (int W=0; W < NumOfWords; W++)
		{
		NumOfPhonemes = pNode->UtteranceInfo()->WordList[W].PhonemeList.size();
		for (int Ph=0; Ph < NumOfPhonemes; Ph++)
			{
			string *pPhoneme = pNode->UtteranceInfo()->WordList[W].PhonemeList[Ph].GetPhoneme ();
			int Duration = pNode->UtteranceInfo()->WordList[W].PhonemeList[Ph].GetDuration ();
			int MaxPitch = FindMaxPitch (pNode, W, Ph);

			// Find type of phoneme
			Type = StressedVowelType (pStat, pPhoneme, MaxPitch, Duration);
			switch (Type)
				{
				case PRIMARY_VOWEL:			PitchFactor = 0.2;
											break;
				
				case SECONDARY_VOWEL:		PitchFactor = 0.1;
											break;

				case TERTIARY_VOWEL:		PitchFactor = 0.0;
											break;

				case NON_STRESSED:			PitchFactor = 0.0;
											break;

				default:					cerr << "WARNING: could not decide what type " << pPhoneme->c_str() << "was\n";
											break;
				}

			// Increase pitch by PitchFactor if phoneme was a Primary or Secondary vowel.
			if (PitchFactor > 0)
				{
				int NumOfPitchPoints = pNode->UtteranceInfo()->WordList[W].PhonemeList[Ph].PitchSeries.size();
				for (int PP=0; PP < NumOfPitchPoints; PP++)
					{
					int Pitch = pNode->UtteranceInfo()->WordList[W].PhonemeList[Ph].PitchSeries[PP].GetPitch ();
					Pitch = Pitch + (int) (PitchFactor * Pitch);
					pNode->UtteranceInfo()->WordList[W].PhonemeList[Ph].PitchSeries[PP].SetPitch (Pitch);
					}
				}

			// Cleanup
			delete pPhoneme;
			}
		}
}

////////////////////////////////////////////////////////////////////////////////
// ChangeStressedVowelDurations
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::ChangeStressedVowelDurations (CTTS_SMLNode *pNode, CTTS_Stat *pStat)
{
	assert ((pNode != NULL) && (pStat != NULL));
	
	enum VowelType Type;
	int NumOfWords = pNode->UtteranceInfo()->WordList.size();
	int NumOfPhonemes;
	double DurationFactor;

	for (int W=0; W < NumOfWords; W++)
		{
		NumOfPhonemes = pNode->UtteranceInfo()->WordList[W].PhonemeList.size();
		for (int Ph=0; Ph < NumOfPhonemes; Ph++)
			{
			string *pPhoneme = pNode->UtteranceInfo()->WordList[W].PhonemeList[Ph].GetPhoneme ();
			int Duration = pNode->UtteranceInfo()->WordList[W].PhonemeList[Ph].GetDuration ();
			int MaxPitch = FindMaxPitch (pNode, W, Ph);

			// Find type of phoneme
			Type = StressedVowelType (pStat, pPhoneme, MaxPitch, Duration);
			switch (Type)
				{
				case PRIMARY_VOWEL:			DurationFactor = 0.2;
											break;
				
				case SECONDARY_VOWEL:		DurationFactor = 0.0;
											break;

				case TERTIARY_VOWEL:		DurationFactor = 0.0;
											break;

				case NON_STRESSED:			DurationFactor = 0.0;
											break;

				default:					cerr << "WARNING: could not decide what type " << pPhoneme->c_str() << "was\n";
											break;
				}

			// Increase duration by 50% if phoneme was a Primary or Secondary vowel.
			if ((DurationFactor > 0) && (MaxPitch > 0))
				{
				// Debug
				//cout << pPhoneme->c_str() << ": modified duration from " << Duration;
				Duration = Duration + (int) (DurationFactor * Duration);
				pNode->UtteranceInfo()->WordList[W].PhonemeList[Ph].SetDuration (Duration);
				//cout << " to " << Duration << endl;
				}

			// Cleanup
			delete pPhoneme;
			}
		}
}

///////////////////////////////////////////////////////////////////
// StressedVowelType
//
// Returns the type of phoneme it is in the utterance according to 
// the actual phoneme symbol, and it's pitch and duration.  It is
// assumed that pStat already holds the utterance's statistics. The 
// following criteria is used to determine the type:
//		a) PRIMARY_VOWEL	- Phoneme is vowel, both duration and pitch >= average.
//		b) SECONDARY_VOWEL - Phoneme is vowel, duration >= average, pitch isn't.
//		c) TERTIARY_VOWEL - Phoneme is vowel, pitch >= average, duration isn't.
//		d) NON_STRESSED - Phoneme is either a non-vowel sounding phoneme, or is
//		   a vowel, but doesn't not fit any of the above categories.
//
// For efficiency reasons, the Phoneme's pitch and duration are tested
// against the utterance's averages (held by pStat).  This is so that
// int comparisons are done to see whether a lengthy string compare
// is required or not.
///////////////////////////////////////////////////////////////////
enum CTTS_Emotion::VowelType CTTS_Emotion::StressedVowelType (CTTS_Stat *pStat, string *pPhoneme, int Pitch, int Duration)
{
	assert ((pStat != NULL) && (pPhoneme != NULL));

	enum VowelType Vowel;

	if (Duration >= ((int) pStat->GetDurAverage ()))
		{
		if (Pitch >= ((int) pStat->GetAverage ()))
			{
			// Possibly a PRIMARY_VOWEL type vowel.
			if (IsVowelSound (pPhoneme))
				{
				Vowel = PRIMARY_VOWEL;
				}
			else
				{
				Vowel = NON_STRESSED;
				}
			}
		else
			{
			// Possibly a SECONDARY_VOWEL type vowel.
			if (IsVowelSound (pPhoneme))
				{
				Vowel = SECONDARY_VOWEL;
				}
			else
				{
				Vowel = NON_STRESSED;
				}
			}
		}
	else
		{
		if (Pitch >= ((int) pStat->GetAverage()))
			{
			// Possibly a TERTIARY_VOWEL type vowel
			if (IsVowelSound (pPhoneme))
				{
				Vowel = TERTIARY_VOWEL;
				}
			else
				{
				Vowel = NON_STRESSED;
				}
			}
		else
			{
			Vowel = NON_STRESSED;
			}
		}

	return Vowel;
}

////////////////////////////////////////////////////////////////////////////////
// FindMaxPitch
//
////////////////////////////////////////////////////////////////////////////////
int CTTS_Emotion::FindMaxPitch (CTTS_SMLNode *pNode, int W, int Ph)
{
	int NumOfPitchPoints = pNode->UtteranceInfo()->WordList[W].PhonemeList[Ph].PitchSeries.size();
	int MaxPitch = 0;

	if (NumOfPitchPoints > 0)
		{
		for (int PP=0; PP < NumOfPitchPoints; PP++)
			{
			int Pitch = pNode->UtteranceInfo()->WordList[W].PhonemeList[Ph].PitchSeries[PP].GetPitch ();
			if (Pitch > MaxPitch)
				MaxPitch = Pitch;
			}
		}
	else
		{
		// Debug
		//cout << "Warning in CTTS_Emotion::FindMaxPitch (): Can't modify pitch point because there is none.\n";
		}

	return MaxPitch;
}

////////////////////////////////////////////////////////////////////////////////
// ModifyForRegularStressing
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Emotion::ModifyForRegularStressing (CTTS_SMLNode *pNode, CTTS_Stat *pStat)
{
}