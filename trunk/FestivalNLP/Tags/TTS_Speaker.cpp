////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Speaker class IMPLEMENTATION File.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_Speaker.h"

////////////////////////////////////////////////////////////////////////////////
// ProcessTag
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Speaker::ProcessTag (CTTS_SMLNode *pNode)
{
	if (pNode->Attributes ())
		{
		CTTS_SMLNode *pAttributeNode = pNode->Attributes();
		CTTS_SMLNode *pValueNode = pNode->Attributes()->Children();

		while (pAttributeNode != NULL)
			{
			if (pAttributeNode->IsNode ("gender"))
				{
				SetGender (pValueNode->GetContent());
				}
			else if (pAttributeNode->IsNode ("name"))
				{
				SetVoice (pValueNode->GetContent());
				}

			// go to next attribute
			pAttributeNode = pAttributeNode->Next();
			if (pAttributeNode)
				pValueNode = pAttributeNode->Children();
			}
		}
}