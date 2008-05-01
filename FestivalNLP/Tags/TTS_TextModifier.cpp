////////////////////////////////////////////////////////////////////////////////
//
// CTTS_TextModifier CLASS Implementation file.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_TextModifier.h"

////////////////////////////////////////////////////////////////////////////////
// ProcessNode
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_TextModifier::ProcessNode (CTTS_SMLNode *pNode)
{
	assert (pNode != NULL);

	// visit children
	if (pNode->Children())
		{
		ProcessNode (pNode->Children());
		}

	// visit next
	if ((!pNode->IsSectionNode()) && (pNode->Next()))
		{
		ProcessNode (pNode->Next());
		}


	CTTS_SMLSimpleTag *pTag = NULL;

	/////////////////////////////////////////////
	// Find tag type of node

	//////////////////////////////
	// PRON tag
	if (pNode->IsNode ("pron"))
		{
		pTag = new CTTS_Pron;
		}

	//////////////////////////////
	// Filter text
	else if (pNode->IsTextNode())
		{
		FilterText (pNode);
		}


	//////////////////////////////////////////////
	// Process the node
	if (pTag)
		{
		pTag->ProcessTag (pNode);

		delete pTag;
		}
}

////////////////////////////////////////////////////////////////////////////////
// FilterText
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_TextModifier::FilterText (CTTS_SMLNode *pNode)
{
	assert (((pNode->IsTextNode()) && (pNode != NULL)));

	char *Content = strdup (pNode->GetContent ());
	int Length = strlen (Content);
	bool Modified = false;

	for (int i=0; i < Length; i++)
		{
		if (Content[i] == '\"')
			{
			Content[i] = '\'';
			Modified = true;
			}
		}

	if (Modified)
		pNode->SetContent (Content);
	delete Content;
}