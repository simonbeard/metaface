////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Pause CLASS Implementation file.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_Pause.h"

////////////////////////////////////////////////////////////////////////////////
// ProcessTag
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Pause::ProcessTag (CTTS_SMLNode *pNode)
{
	CTTS_SMLNode *pLastTextNode = FindLastTextNode (pNode);
	if (pLastTextNode)
		{
		// Test to see if either "length" or "msec" attributes
		// has been specified within tag.
		char *Length = GetAttribute (pNode, "length");
		if (!Length)
			{
			Length = GetAttribute (pNode, "msec");
			}

		if (Length)
			{
			string Temp = string (Length);
			SetLength (Temp);
			delete Length;
			}

		InsertPause (pLastTextNode);

		char *Smooth = GetAttribute (pNode, "smooth");
		if ((Smooth) && (Smooth[0] == 'y'))
			{
			LengthenPhonemesBeforePause (pLastTextNode);
			}
		delete Smooth;
		}
}

//////////////////////////////////////////////////////////////////
// FindLastTextNode
//
// This function is needed to find the last text node that contains
// at least one word so that a pause phoneme can be inserted at the
// end of it.  Traversal is:
//		1) Current node
//		2) Children nodes	**children nodes are traversed differently
//		3) Prev nodes	
//
//////////////////////////////////////////////////////////////////
CTTS_SMLNode *CTTS_Pause::FindLastTextNode (CTTS_SMLNode *pNode)
{
	assert (pNode != NULL);

	if ((pNode->IsTextNode ()) && (pNode->UtteranceInfoValid ()))
		{
		return (pNode);
		}

	if (pNode->Children())
		{
		CTTS_SMLNode *pTemp = FindNextTextNode (pNode->Children());
		if (pTemp != NULL)
			return pTemp;
		}

	if (pNode->Prev())
		{
		return FindLastTextNode (pNode->Prev());
		}

	return NULL;
}

//////////////////////////////////////////////////////////////////
// This function is needed for the traversal of children nodes.
// Traversal is:
//		1) Next node
//		2) Children nodes
//		3) Current node
//
//////////////////////////////////////////////////////////////////
CTTS_SMLNode *CTTS_Pause::FindNextTextNode (CTTS_SMLNode *pNode)
{
	assert (pNode != NULL);

	if (pNode->Next())
		{
		CTTS_SMLNode *pTemp = FindNextTextNode (pNode->Next());
		if (pTemp != NULL)
			return pTemp;
		}

	if (pNode->Children())
		{
		CTTS_SMLNode *pTemp = FindNextTextNode (pNode->Children());
		if (pTemp != NULL)
			return pTemp;
		}

	if ((pNode->IsTextNode ()) && (pNode->UtteranceInfoValid ()))
		{
		return (pNode);
		}

	return NULL;
}

////////////////////////////////////////////////////////////////////////////////
// LengthenPhonemesBeforePause
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Pause::LengthenPhonemesBeforePause (CTTS_SMLNode *pNode)
{
	assert ((pNode != NULL) && (pNode->UtteranceInfo() != NULL));

	int LastWord = pNode->UtteranceInfo()->WordList.size() - 1;
	assert (LastWord >= 0);

	// Make LastPhoneme index into the phoneme before the inserted pause.
	int LastPhoneme = pNode->UtteranceInfo()->WordList[LastWord].PhonemeList.size() - 2;
	assert (LastPhoneme >= 0);

	int Curr;
	if (LastPhoneme > 1)
		Curr = LastPhoneme - 1;
	else
		Curr = 0;

	// Lengthen the last two phonemes previous to the inserted pause.
	while (Curr <= LastPhoneme)
		{
		int Duration = (int) (pNode->UtteranceInfo()->WordList[LastWord].PhonemeList[Curr].GetDuration () * 1.9);
		if (Duration > MAX_DURATION)
			Duration = MAX_DURATION;
		pNode->UtteranceInfo()->WordList[LastWord].PhonemeList[Curr].SetDuration (Duration);
		Curr++;
		}
}
