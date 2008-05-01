////////////////////////////////////////////////////////////////////////////////
//
// CTTS_SMLDocument IMPLEMENTATION File.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_SMLDocument.h"

///////////////////////////////////////////////////////////////////////////
// OutputMbrolaPhonemeFile
//
///////////////////////////////////////////////////////////////////////////
void CTTS_SMLDocument::OutputMbrolaPhonemeFile (const char *Filename)
{
	//string Filename = string (Basename) + ".mbr";
	ofstream MBRFile (Filename);

	if (!MBRFile)
		{
		cerr << "Error in CTTS_SMLDocument::OutputMbrolaPhonemeFile(): could not open file for writing" << endl;
		exit (8);
		}
	
	////////////////////////////////////////////
	// Output phoneme information contained in 
	// document in MBROLA format
	MBRFile << "_ 100 " << endl;
	Root()->OutputMbrolaUtteranceInfo (MBRFile);
	MBRFile << "_ 100 " << endl;

	MBRFile.close ();
}

///////////////////////////////////////////////////////////////////////////
// OutputMbrolaPhonemeString
//
///////////////////////////////////////////////////////////////////////////
void CTTS_SMLDocument::OutputMbrolaPhonemeString (string &MBROLA_Utterance)
{
	////////////////////////////////////////////
	// Output phoneme information contained in 
	// document in MBROLA format
	MBROLA_Utterance = "_ 100 \n";
	Root()->OutputMbrolaUtteranceInfo (MBROLA_Utterance);
	MBROLA_Utterance += string ("_ 100 \n");
}

///////////////////////////////////////////////////////////////////////////
// NextSection
//
// Returns a pointer to the root node of the next section to be synthesized.
// It is assumed that if currently the document isn't pointing to a 
// top-level utterance node (defined in the function IsTopLevelUtteranceNode()),
// then we're at the beginning of the document.  Therefore it is only a matter
// of traversing down some levels until a top-level node is found.
// If we're currently at a top-level utterance node, then the way to get to
// the next top-level utterance node is by either going to current node's
// Next() node (if it exists), otherwise the next node can be found at the
// current node's parent's next's child node (!) (i.e. Go up to parent, 
// go to next node, go down a level - you're there).
// If there are no more section to visit (i.e. the whole document has been
// traversed), then the function will return NULL.
///////////////////////////////////////////////////////////////////////////
CTTS_SMLNode *CTTS_SMLDocument::NextSection (CTTS_SMLNode *pCurr)
{
	assert (pCurr != NULL);

	CTTS_SMLNode *pNextSectionNode = NULL;

	// Are we currently at a top level utterance node?
	if (pCurr->IsSectionNode ())
		{
		// Is the current node have another node next to it (on the same level)?
		if (pCurr->Next())
			{
			pNextSectionNode = pCurr->Next();
			}
		else
			{
			// Go up a level, next, then down a level.
			if (pCurr->Parent()->Next())
				{
				if (pCurr->Parent()->Next()->Children())
					pNextSectionNode = pCurr->Parent()->Next()->Children();
				}
			
			}
		}
	else
		{
		// We're currently at top of document.  Go down a few levels until we  hit
		// a top-level utterance node.
		pCurr = pCurr->Children();
		while (pCurr != NULL)
			{
			if (pCurr->IsSectionNode ())
				{
				pNextSectionNode = pCurr;
				break;
				}
			else
				{
				pCurr = pCurr->Children();
				}
			}
		}

	// Make sure that the function will return either a NULL pointer, or a pointer to a
	// node that is a top-level utterance node.
	assert ((pNextSectionNode == NULL) || (pNextSectionNode->IsSectionNode()));

	return pNextSectionNode;
}
