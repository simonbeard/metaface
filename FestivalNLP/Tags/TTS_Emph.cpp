////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Emph CLASS Implementation file.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_Emph.h"
#include "../TTS_WordInfo.h"
#include "../TTS_PhonemeInfo.h"

////////////////////////////////////////////////////////////////////////////////
// ProcessTag
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Emph::ProcessTag (CTTS_SMLNode *pNode)
{
	assert ((pNode != NULL) && (pNode->Children() != NULL));
	int Word=0, Phoneme=0;

	char *pLevel = GetAttribute (pNode, "level");
	if (pLevel)
		{
		SetStrength (pLevel);
		delete pLevel;
		}

	char *pAffect = GetAttribute (pNode, "affect");
	if (pAffect)
		{
		SetAffect (pAffect);
		delete pAffect;
		}

	char *pTarget = GetAttribute (pNode, "target");
	if (pTarget)
		{
		FindTarget (pNode->Children(), pTarget, &Word, &Phoneme);
		delete pTarget;
		}
	else
		{
		FindFirstVowelSound (pNode->Children(), &Word, &Phoneme);
		}

	EmphasizePhonemes (pNode->Children(), Word, Phoneme);
}

//////////////////////////////////////////////////////////////////////////////////
// Finds the first vowel sounding phoneme.  For each phoneme, a linear search is
// done through the table of known vowel sounds.  THIS IS COULD BE MADE MUCH MORE
// INEFFICIENT, and will be modified when I get round to it.
//
//////////////////////////////////////////////////////////////////////////////////
void CTTS_Emph::FindFirstVowelSound (CTTS_SMLNode *pNode, int *Word, int *Phoneme)
{
	char *VowelTable[] = {"@", "@@", "a", "aa", "ai", "au", "e", "e@", "ei", "i", "i@", "ii",
						"o", "oi", "oo", "ou", "u", "u@", "uh", "uu","\0"};

	assert (pNode != NULL);
	int NumOfPhonemes;
	int NumOfWords = pNode->UtteranceInfo()->WordList.size();
	int W=0;
	bool Found = false;

	while ((W < NumOfWords) && (!Found))
		{
		int P = 0;
		NumOfPhonemes = pNode->UtteranceInfo()->WordList[W].PhonemeList.size();
		while ((P < NumOfPhonemes) && (!Found))
			{
			string *pPh = pNode->UtteranceInfo()->WordList[W].PhonemeList[P].GetPhoneme();

			char *pCh;
			int i=0;
			while (*(pCh = VowelTable[i]) != '\0')
				{
				if (pPh->compare ((const char *)pCh) == 0)
					{
					*Word = W;
					*Phoneme = P;
					Found = true;
					break;
					}
				else if (pPh->compare ((const char *)pCh) < 0)
					{
					break;
					}
				i++;
				}

			delete pPh;
			P++;
			}
		W++;
		}

	assert ((*Word < NumOfWords) && (*Phoneme < NumOfPhonemes));
}

////////////////////////////////////////////////////////////////////////////////
// FindTarget
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Emph::FindTarget (CTTS_SMLNode *pNode, const char *pTarget, int *Word, int *Phoneme)
{
	assert ((pNode != NULL) && (pTarget != NULL));
	int NumOfPhonemes;
	int NumOfWords = pNode->UtteranceInfo()->WordList.size();
	int W=0;
	bool Found = false;

	while ((W < NumOfWords) && (!Found))
		{
		int P = 0;
		NumOfPhonemes = pNode->UtteranceInfo()->WordList[W].PhonemeList.size();
		while ((P < NumOfPhonemes) && (!Found))
			{
			string *pPh = pNode->UtteranceInfo()->WordList[W].PhonemeList[P].GetPhoneme ();
			if (pPh->compare ((const char *) pTarget) == 0)
				{
				*Word = W;
				*Phoneme = P;
				Found = true;
				}

			delete pPh;
			P++;
			}
		W++;
		}

	assert ((*Word < NumOfWords) && (*Phoneme < NumOfPhonemes));
}

////////////////////////////////////////////////////////////////////////////////
// EmphasizePhonemes
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Emph::EmphasizePhonemes (CTTS_SMLNode *pNode, int Word, int Phoneme)
{
	assert (pNode != NULL);
	assert ((Word >= 0) && (Word < pNode->UtteranceInfo()->WordList.size()));
	assert ((Phoneme >= 0) && (Phoneme < pNode->UtteranceInfo()->WordList[Word].PhonemeList.size()));

	double PitchFactor = PRIMARY_PITCH;
	double DurationFactor = PRIMARY_DURATION;

	string *pPh = pNode->UtteranceInfo()->WordList[Word].PhonemeList[Phoneme+1].GetPhoneme ();
	if (((Phoneme + 1) < pNode->UtteranceInfo()->WordList[Word].PhonemeList.size()) && (pPh->compare ("_") != 0))
		Emphasize (pNode, Word, Phoneme+1, SECONDARY_PITCH, SECONDARY_DURATION);
	delete pPh;

	Emphasize (pNode, Word, Phoneme, PRIMARY_PITCH, PRIMARY_DURATION);

	pPh = pNode->UtteranceInfo()->WordList[Word].PhonemeList[Phoneme-1].GetPhoneme ();
	if ((Phoneme > 0) && (pPh->compare ("_") != 0))
		Emphasize (pNode, Word, Phoneme-1, SECONDARY_PITCH, SECONDARY_DURATION);
	delete pPh;
}

////////////////////////////////////////////////////////////////////////////////
// Emphasize
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Emph::Emphasize (CTTS_SMLNode *pNode, int Word, int Phoneme, double PitchFactor, double DurationFactor)
{
	assert (pNode != NULL);
	assert ((Word >= 0) && (Word < pNode->UtteranceInfo()->WordList.size()));
	assert ((Phoneme >= 0) && (Phoneme < pNode->UtteranceInfo()->WordList[Word].PhonemeList.size()));

	int NewPitch;
	int NewDuration;
	int NumOfPitchPoints = pNode->UtteranceInfo()->WordList[Word].PhonemeList[Phoneme].PitchSeries.size();

	if (NumOfPitchPoints > 0)
		{
		// Calculate and set new duration for phoneme
		if (AffectDuration())
			{
			NewDuration = pNode->UtteranceInfo()->WordList[Word].PhonemeList[Phoneme].GetDuration ();
			NewDuration = (int) NewDuration * DurationFactor * GetStrength ();
			pNode->UtteranceInfo()->WordList[Word].PhonemeList[Phoneme].SetDuration (NewDuration);
			}

		// Calculate and set new pitch for all phoneme's pitch points 
		if (AffectPitch ())
			{
			for (int i=0; i < NumOfPitchPoints; i++)
				{
				NewPitch = pNode->UtteranceInfo()->WordList[Word].PhonemeList[Phoneme].PitchSeries[i].GetPitch();
				NewPitch = (int) NewPitch * PitchFactor * GetStrength();
				pNode->UtteranceInfo()->WordList[Word].PhonemeList[Phoneme].PitchSeries[i].SetPitch (NewPitch);
				}
			}
		}
	else
		{
		if (AffectDuration ())
			{
			NewDuration = pNode->UtteranceInfo()->WordList[Word].PhonemeList[Phoneme].GetDuration ();
			NewDuration = (int) NewDuration * DurationFactor * GetStrength ();
			pNode->UtteranceInfo()->WordList[Word].PhonemeList[Phoneme].SetDuration (NewDuration);
			}

		if (AffectPitch ())
			{
			NewPitch = GetLastPitchValue (pNode, Word, Phoneme-1);
			NewPitch = (int) NewPitch * PitchFactor * GetStrength ();
			pNode->UtteranceInfo()->WordList[Word].PhonemeList[Phoneme].InsertPitchPoint (0, NewPitch);
			}
		}
}

////////////////////////////////////////////////////////////////////////////////
// GetLastPitchValue
//
////////////////////////////////////////////////////////////////////////////////
int CTTS_Emph::GetLastPitchValue (CTTS_SMLNode *pNode, int Word, int Phoneme)
{
	int Pitch = 0;

	while (Phoneme >= 0)
		{
		int Size = pNode->UtteranceInfo()->WordList[Word].PhonemeList[Phoneme].PitchSeries.size();
		if (Size > 0)
			{
			Pitch = pNode->UtteranceInfo()->WordList[Word].PhonemeList[Phoneme].PitchSeries[Size-1].GetPitch();
			break;
			}
		Phoneme--;
		}

	return Pitch;
}