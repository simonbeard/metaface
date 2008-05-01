////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Rate Class IMPLEMENTATION file
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_Rate.h"

////////////////////////////////////////////////////////////////////////////////
// ProcessTag
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Rate::ProcessTag (CTTS_SMLNode *pNode)
{
	assert (pNode != NULL);

	char *Speed = GetAttribute (pNode, "speed");

	if (Speed != NULL)
		{
		string Temp = string (Speed);
		SetSpeed (Temp);
		if (pNode->Children())
			{
			ModifyPhonemeDurations (pNode->Children());
			}
		delete Speed;
		}
}

////////////////////////////////////////////////////////////////////////////////
// ModifyPhonemeDurations
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Rate::ModifyPhonemeDurations (CTTS_SMLNode *pCurrNode)
{
	// Modify phoneme durations if this is a text node
	if (pCurrNode->IsTextNode ())
		{
		int NumOfPhonemes;
		int NumOfWords = pCurrNode->UtteranceInfo()->GetNumOfWords ();
		for (int Word=0; Word < NumOfWords; Word++)
			{
			NumOfPhonemes = pCurrNode->UtteranceInfo()->GetNumOfPhonemes (Word);
			for (int Phoneme=0; Phoneme < NumOfPhonemes; Phoneme++)
				{
				int Duration = pCurrNode->UtteranceInfo()->GetPhonemeDuration (Word, Phoneme);
				Duration = (int) (Duration * m_SpeedFactor);

				// make sure new duration is a positive value.
				if (Duration > 0)
					pCurrNode->UtteranceInfo()->SetPhonemeDuration (Word, Phoneme, Duration);
				}
			}
		}

	// Visit next
	if (pCurrNode->Next())
		{
		ModifyPhonemeDurations (pCurrNode->Next());
		}

	// Visit children, but only if the current node isn't a
	// Rate node (don't want to interfere with other rate nodes).
	if ((!pCurrNode->IsNode("rate")) && (pCurrNode->Children()))
		{
		ModifyPhonemeDurations (pCurrNode->Children());
		}
}
