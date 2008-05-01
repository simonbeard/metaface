////////////////////////////////////////////////////////////////////////////////
//
// CTTS_PhonemeModifier CLASS Implementation file.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_PhonemeModifier.h"

////////////////////////////////////////////////////////////////////////////////
// ProcessNode
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_PhonemeModifier::ProcessNode (CTTS_SMLNode *pNode, CTTS_MbrolaOptions *pOptions)
{
	// visit children
	if (pNode->Children())
		{
		ProcessNode (pNode->Children(), pOptions);
		}

	// visit next
	if ((!pNode->IsSectionNode()) && (pNode->Next()))
		{
		ProcessNode (pNode->Next(), pOptions);
		}


	CTTS_SMLSimpleTag *pTag = NULL;

	/////////////////////////////////////////////
	// Find tag type of node
	// TEMPORARY - I don't like the long if-then-else
	// list.  Tag lookup table in the works.
	if (pNode->IsNode ("speaker"))
		{
		CTTS_Speaker *pSpeaker = new CTTS_Speaker;
		pSpeaker->ProcessTag (pNode);
		pOptions->SetSpeaker (*pSpeaker);
		delete pSpeaker;
		}
	else if (pNode->IsNode ("pause"))
		{
		pTag = new CTTS_Pause;
		}
	else if (pNode->IsNode ("rate"))
		{
		pTag = new CTTS_Rate;
		}
	else if (pNode->IsNode ("pitch"))
		{
		pTag = new CTTS_Pitch;
		}
	else if (pNode->IsNode ("emph"))
		{
		pTag = new CTTS_Emph;
		}
	else if (pNode->IsNode ("sad"))
		{
		pTag = new CTTS_Sad;
		pOptions->SetVolumeRatio ("0.6");
		}
	else if (pNode->IsNode ("happy"))
		{
		pTag = new CTTS_Happy;
		}
	else if (pNode->IsNode ("angry"))
		{
		pTag = new CTTS_Angry;
		pOptions->SetVolumeRatio ("1.7");
		}
	else if (pNode->IsNode ("stat"))
		{
		pTag = new CTTS_Stat;
		}
	else if (pNode->IsNode ("volume"))
		{
		CTTS_Volume *pVolume = new CTTS_Volume;
		pVolume->ProcessTag (pNode);
		pOptions->SetVolumeRatio (pVolume->GetVolumeRatio ());
		delete pVolume;
		}

	//////////////////////////////////////////////
	// Process the node
	if (pTag)
		{
		pTag->ProcessTag (pNode);
		delete pTag;
		}
}