////////////////////////////////////////////////////////////////////////////////
//
// CTTS_SMLNode IMPLEMENTATION File.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_SMLNode.h"

////////////////////////////////////////////////////////////////////////////////
// PrintNode
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLNode::PrintNode ()
{
	// Print this node
	PrintCurrentNode ();

	// Print next in line
	if (Next() != NULL)
		Next()->PrintNode();

	// Print children
	if (Children() != NULL)
		Children()->PrintNode();
}

////////////////////////////////////////////////////////////////////////////////
// OutputPlainText
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLNode::OutputPlainText ()
{
	if ((m_Type == SML_DOCUMENT_NODE) || (m_Type == SML_ELEMENT_NODE))
		{
		// go to children
		if (Children() != NULL)
			Children()->OutputPlainText ();

		// go to next in list
		if (Next() != NULL)
			Next()->OutputPlainText ();
		}

	else if (m_Type == SML_TEXT_NODE)
		{
		cout << m_Content.c_str() << " ";
		//m_pUtteranceInfo->PrintUtteranceInfo ();
		if (Next() != NULL)
			Next()->OutputPlainText ();
		}
}

////////////////////////////////////////////////////////////////////////////////
// GetPlainText
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLNode::GetPlainText (string &UtteranceText)
{
	if (IsTextNode ())
		{
		string Content = GetContent ();
		char LastChar = Content[Content.length()-1];
		if ((LastChar != ' ') && (LastChar != '\n') && (LastChar != '\t'))
			Content += " ";

		UtteranceText.append (Content);
		}

	// go to children
	if (Children() != NULL)
		Children()->GetPlainText (UtteranceText);

	// go to next in list
	if (Next() != NULL)
		Next()->GetPlainText (UtteranceText);
}

////////////////////////////////////////////////////////////////////////////////
// OutputTags
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLNode::OutputTags (const char *TagName)
{
	if (m_Name == TagName)
		{
		PrintCurrentNode();
		}

	if (Children () != NULL)
		Children()->OutputTags (TagName);

	if (Next() != NULL)
		Next()->OutputTags (TagName);
}

////////////////////////////////////////////////////////////////////////////////
// OutputMbrolaPhonemeFile
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLNode::OutputMbrolaPhonemeFile (const char *Filename)
{
	ofstream MBRFile (Filename);

	if (!MBRFile)
		{
		cerr << "Error in CTTS_SMLNode::OutputMbrolaPhonemeFile (): could not open file for writing" << endl;
		exit (8);
		}

	////////////////////////////////////////////
	// Output phoneme information contained in
	// section in MBROLA format.
	MBRFile << "_ 100 " << endl;
	OutputMbrolaUtteranceInfo (MBRFile);
	MBRFile << "_ 100 " << endl;

	MBRFile.close ();
}

////////////////////////////////////////////////////////////////////////////////
// OutputGSTPhonemeFile
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLNode::OutputGSTPhonemeFile (const char *Filename)
{
	ofstream GSTFile (Filename);

	if (!GSTFile)
		{
		cerr << "Error in CTTS_SMLNode::OutputGSTPhonemeFile (): could not open file for writing" << endl;
		exit (8);
		}

	////////////////////////////////////////////
	// Output phoneme information contained in
	// section in MBROLA format.
	GSTFile << "_ 100 " << endl;
	OutputGSTUtteranceInfo (GSTFile);
	GSTFile << "_ 100 " << endl;

	GSTFile.close ();
}

////////////////////////////////////////////////////////////////////////////////
// OutputMbrolaPhonemeString
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLNode::OutputMbrolaPhonemeString (string &MBROLA_Utterance)
{
	////////////////////////////////////////////
	// Output phoneme information contained in
	// section in MBROLA format.
	MBROLA_Utterance = "_ 100 \n";
	OutputMbrolaUtteranceInfo (MBROLA_Utterance);
	MBROLA_Utterance += "_ 100 \n";	
}

////////////////////////////////////////////////////////////////////////////////
// GetSectionPhonemes
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLNode::GetSectionPhonemes (CTTS_NaturalLanguageParser *pNLP)
{
	TokenizeContent (pNLP);
	PhonemizeUtterance (pNLP);
}

////////////////////////////////////////////////////////////////////////////////
// TokenizeContent
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLNode::TokenizeContent (CTTS_NaturalLanguageParser *pNLP)
{
	// Tokenize the node's content if it's a Text node
	if (IsTextNode ())
		{
		char *Words = pNLP->CharsToWords (GetContent ());
		//SetContent (Words);

		// put separate words in utterance's word list
		string Temp = string (Words);
		InsertWordsInUtterance (Temp);

		// free memory
		delete Words;
		}

	// visit Children nodes
	if (Children() != NULL)
		Children()->TokenizeContent (pNLP);

	// visit Next nodes
	if ((!IsSectionNode()) && (Next()))
		Next()->TokenizeContent (pNLP);
}

////////////////////////////////////////////////////////////////////////////////
// DispersePhonemeInformation
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLNode::DispersePhonemeInformation (CTTS_UtteranceInfo *pMainUtteranceTable, int *WPtr)
{
	if (IsTextNode ())
		{
		int NumOfWords = m_pUtteranceInfo->WordList.size();

		for (int i=0; i < NumOfWords; i++)
			{
			m_pUtteranceInfo->WordList[i] = pMainUtteranceTable->WordList[(*WPtr)];
			(*WPtr)++;
			}
		}

	// visit children nodes
	if (Children())
		{
		Children()->DispersePhonemeInformation (pMainUtteranceTable, WPtr);
		}

	// visit next nodes
	if (Next ())
		{
		Next()->DispersePhonemeInformation (pMainUtteranceTable, WPtr);
		}
}

////////////////////////////////////////////////////////////////////////////////
// PhonemizeUtterance
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLNode::PhonemizeUtterance (CTTS_NaturalLanguageParser *pNLP)
{
	// If the current node is top level utterance parent,
	// then get all text content of all children nodes.
	if (this->IsTopLevelUtteranceNode())
		{
		if (Children())
			{
			string *pText = Children()->GetUtteranceString ();
			char *Basename = pNLP->GeneratePhonemes (pText->c_str());

			// Check if Phonemes were able to be generated.
			if (!Basename)
				{
				cerr << "In CTTS_SMLNode::PhonemizeUtterance (): Could not generate phonemes" << endl;
				delete m_pUtteranceInfo;
				m_pUtteranceInfo = NULL;
				}
			else
				{
				// Get phoneme pitch and duration information from mbr file
				m_pUtteranceInfo->GetData (Basename);

				// debug
				//m_pUtteranceInfo->PrintUtteranceInfo ();

				// Disperse phoneme pitch and duration information among children.
				int WPtr = 0;
				Children()->DispersePhonemeInformation (m_pUtteranceInfo, &WPtr);

				// TopLevelUtteranceNode's m_pUtteranceInfo information is now contained within
				// it's child nodes, so data is redundant.  Takes up quite a bit of memory, so
				// should delete it now.
				delete m_pUtteranceInfo;
				m_pUtteranceInfo = NULL;

				// Clean up temporary files - .wrd and .mbr files
				string *pTempFilename = new string;
				*pTempFilename = string (Basename) + ".wrd";
				unlink (pTempFilename->c_str());
				*pTempFilename = string (Basename) + ".mbr";
				unlink (pTempFilename->c_str());

				delete pTempFilename;
				delete Basename;
				}
			delete pText;
			}
		}

	else
		{
		// visit children
		if (Children())
			Children()->PhonemizeUtterance (pNLP);
		}

	
	// visit next in tree
	if ((!IsSectionNode()) && (Next()))
		Next()->PhonemizeUtterance (pNLP);
}

////////////////////////////////////////////////////////////////////////////////
// GetUtteranceString
//
////////////////////////////////////////////////////////////////////////////////
string *CTTS_SMLNode::GetUtteranceString ()
{
	string *pText = new string ();

	if (this->IsTextNode())
		{
		string *Temp = new string (GetContent());
		assert (Temp->size() > 0);

		if (!isspace((*Temp)[0]))
			{
			pText->assign (" ");
			pText->append (Temp->c_str());
			}
		else
			{
			pText->assign (Temp->c_str());
			}
		delete Temp;
		}

	else
		{
		if (Children())
			{
			string *Temp = Children()->GetUtteranceString();
			pText->append (Temp->c_str());
			delete Temp;
			}
		}

	if (Next ())
		{
		string *Temp = Next()->GetUtteranceString();
		pText->append (Temp->c_str());
		delete Temp;
		}

	return pText;
}

////////////////////////////////////////////////////////////////////////////////
// InsertWordsInUtterance
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLNode::InsertWordsInUtterance (string &Words)
{
	int i = 0;
	while (i < Words.length ())
		{
		// dont do anything if white space
		if (Words[i] == ' ')
			{
			i++;
			continue;
			}

		// get the next string of characters not separated by whitespace
		int first = i;
		while ((Words[i] != ' ') && (i < Words.length()))
			{
			i++;
			}
		int last = i - first;

		//string Word = m_Content.substr (first, last);
		string Word  = "something";

		m_pUtteranceInfo->AddWord (Word);
		}
}

////////////////////////////////////////////////////////////////////////////////
// OutputMbrolaUtteranceInfo
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLNode::OutputMbrolaUtteranceInfo (ofstream &MBRFile)
{
	// Reader should pause before every utterance.
	if (IsEmotionNode ())
		{
		MBRFile << "_ 420 " << endl;
		}

	// Output Utterance Information if this is a text node
	if (IsTextNode ())
		{
		m_pUtteranceInfo->OutputPhonemesInMbrolaFormat (MBRFile);
		}

	// visit children
	if (Children())
		{
		Children()->OutputMbrolaUtteranceInfo (MBRFile);
		}

	// always visit next (but only if we're not at top level node
	if ((!IsSectionNode()) && (Next ()))
		{
		Next()->OutputMbrolaUtteranceInfo (MBRFile);
		}
}

////////////////////////////////////////////////////////////////////////////////
// OutputMbrolaUtteranceInfo
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLNode::OutputMbrolaUtteranceInfo (string &MBROLA_Utterance)
{
	// Reader should pause before every utterance.
	if (IsEmotionNode ())
		{
		MBROLA_Utterance += "_ 420 \n";
		}

	// Output Utterance Information if this is a text node
	if (IsTextNode ())
		{
		m_pUtteranceInfo->OutputPhonemesInMbrolaFormat (MBROLA_Utterance);
		}

	// visit children
	if (Children())
		{
		Children()->OutputMbrolaUtteranceInfo (MBROLA_Utterance);
		}

	// always visit next
	if ((Next ()) && (!IsSectionNode ()))
		{
		Next()->OutputMbrolaUtteranceInfo (MBROLA_Utterance);
		}
}

////////////////////////////////////////////////////////////////////////////////
// OutputGSTUtteranceInfo
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLNode::OutputGSTUtteranceInfo (ofstream &GSTFile)
{
	// Reader should pause before every utterance.
	if (IsEmotionNode ())
		{
		GSTFile << "_ 420 " << endl;
		}

	// Output Utterance Information if this is a text node
	if (IsTextNode ())
		{
		m_pUtteranceInfo->OutputPhonemesInGSTFormat (GSTFile);
		}

	// visit children
	if (Children())
		{
		Children()->OutputGSTUtteranceInfo (GSTFile);
		}

	// always visit next (but only if we're not at top level node
	if ((!IsSectionNode()) && (Next ()))
		{
		Next()->OutputGSTUtteranceInfo (GSTFile);
		}
}

////////////////////////////////////////////////////////////////////////
// InsertTag
//
// Tag should not be inserted if parent tag has no children.
////////////////////////////////////////////////////////////////////////
CTTS_SMLNode *CTTS_SMLNode::InsertTag (const char *TagName)
{
	assert (TagName != NULL);

	CTTS_SMLNode *pOldChild = Children ();
	if (pOldChild == NULL)
		return NULL;

	CTTS_SMLNode *pNewTag = new CTTS_SMLNode (SML_ELEMENT_NODE, TagName, NULL, this, pOldChild, NULL, NULL, NULL);
	pOldChild->SetParent (pNewTag);
	this->SetChildren (pNewTag);

	return pNewTag;
}

////////////////////////////////////////////////////////////////////////////////
// InsertAttribute
//
////////////////////////////////////////////////////////////////////////////////
CTTS_SMLNode *CTTS_SMLNode::InsertAttribute (const char *AttribName, const char *AttribValue)
{
	assert ((AttribName != NULL) && (AttribValue != NULL));

	CTTS_SMLNode *pAttribElement = new CTTS_SMLNode (SML_ATTRIBUTE_NODE, AttribName, NULL, this, NULL, NULL, NULL, NULL);
	CTTS_SMLNode *pAttribValue = new CTTS_SMLNode (SML_TEXT_NODE, "text", AttribValue, pAttribElement, NULL, NULL, NULL, NULL);

	pAttribElement->SetChildren (pAttribValue);

	// Insert new Attribute node pair at beginning of attribute list.
	if (this->Attributes())
		{
		CTTS_SMLNode *pOldAttribElement = this->Attributes ();
		this->SetAttributes (pAttribElement);
		pAttribElement->SetNext (pOldAttribElement);
		pOldAttribElement->SetPrev (pAttribElement);
		}
	else
		{
		this->SetAttributes (pAttribElement);
		}

	return pAttribElement;
}

////////////////////////////////////////////////////////////////////////
// DeleteTag
//
// Assertion will fail if tag node does not have children, because tag
// should not have been inserted in the first place in InsertTag()
// function.
////////////////////////////////////////////////////////////////////////
void CTTS_SMLNode::DeleteTag (CTTS_SMLNode *pTag)
{
	assert ((pTag != NULL) && (pTag->Children() != NULL));

	CTTS_SMLNode *pChild = pTag->Children();

	// Disassociate tag node from rest of DOM Tree.
	this->SetChildren (pChild);
	pChild->SetParent (this);
	pTag->SetParent (NULL);
	pTag->SetChildren (NULL);
	pTag->SetNext (NULL);

	delete pTag;
}