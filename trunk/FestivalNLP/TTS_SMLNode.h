////////////////////////////////////////////////////////////////////////////////
//
// CTTS_SMLNode CLASS Header file.
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_SML_NODE_H__
#define __CTTS_SML_NODE_H__

#include <iostream.h>
#include <string>
#include "TTS_NaturalLanguageParser.h"
#include "TTS_UtteranceInfo.h"

using std::string;

enum SMLNodeType
		{
		SML_DOCUMENT_NODE,
		SML_ELEMENT_NODE,
		SML_ATTRIBUTE_NODE,
		SML_TEXT_NODE,
		SML_CDATA_SECTION_NODE
		};

class CTTS_SMLNode
{
private:
	enum SMLNodeType m_Type;
	string m_Name;
	string m_Content;
	CTTS_SMLNode *m_Parent;
	CTTS_SMLNode *m_Children;
	CTTS_SMLNode *m_Next;
	CTTS_SMLNode *m_Prev;
	CTTS_SMLNode *m_Attributes;
	CTTS_UtteranceInfo *m_pUtteranceInfo;

	inline void PrintNodeType ();
	string *GetUtteranceString ();
	void InsertWordsInUtterance (string &Words);
	void DispersePhonemeInformation (CTTS_UtteranceInfo *, int *);

public:
	CTTS_SMLNode () { };

	CTTS_SMLNode (enum SMLNodeType Type, const char *Name, const char *Content,
		CTTS_SMLNode *Parent, CTTS_SMLNode *Children, CTTS_SMLNode *Prev, CTTS_SMLNode *Next,
		CTTS_SMLNode *Attributes)
		{
		m_Type = Type;

		// Following if statement is required because of bug in libxml v2 - XML_CDATA_SECTION_NODE nodes aren't
		// given a name.  If future versions do, then take out the if statement.
		if (m_Type == SML_CDATA_SECTION_NODE)
			m_Name = "cdata";

		if (Name != NULL)
			m_Name = Name;

		if (Type == SML_TEXT_NODE)
			m_Content = Content;
		m_Parent = Parent;
		m_Children = Children;
		m_Prev = Prev;
		m_Next = Next;
		m_Attributes = Attributes;

		if ((IsTextNode ()) || (IsTopLevelUtteranceNode ()))
			{
			m_pUtteranceInfo = new CTTS_UtteranceInfo;
			}
		else
			{
			m_pUtteranceInfo = NULL;
			}
		};

	~CTTS_SMLNode ()
		{
		// delete children
		if (Children() != NULL)
			delete Children();

		// delete next in line
		if (Next() != NULL)
			delete Next();

		// delete attribute list
		if (Attributes () != NULL)
			delete Attributes ();

		// delete UtteranceInfo
		if (m_pUtteranceInfo)
			{
			delete m_pUtteranceInfo;
			}
		};

	inline CTTS_SMLNode *Parent();
	inline CTTS_SMLNode *Children();
	inline CTTS_SMLNode *Prev();
	inline CTTS_SMLNode *Next();
	inline CTTS_SMLNode *Attributes();
	inline CTTS_UtteranceInfo *UtteranceInfo ();

	inline const char *GetContent ();
	inline void SetContent (const char *Content);
	inline void SetParent (CTTS_SMLNode *Parent);
	inline void SetChildren (CTTS_SMLNode *FirstChild);
	inline void SetNext (CTTS_SMLNode *NextNode);
	inline void SetPrev (CTTS_SMLNode *PrevNode);
	inline void SetAttributes (CTTS_SMLNode *FirstAttribute);

	inline bool IsNode (const char *Name);
	inline bool IsTextNode ();
	inline bool IsCDataSectionNode ();
	inline bool IsTopLevelUtteranceNode ();
	inline bool IsSectionNode ();
	inline bool IsEmotionNode ();

	void OutputPlainText ();
	void GetPlainText (string &UtteranceText);
	void OutputTags (const char *TagName);
	void PrintNode ();
	inline void PrintCurrentNode ();

	void GetSectionPhonemes (CTTS_NaturalLanguageParser *pNLP);
	void TokenizeContent (CTTS_NaturalLanguageParser *pNLP);
	void PhonemizeUtterance (CTTS_NaturalLanguageParser *pNLP);

	inline void PushPhoneme (const char *Phoneme, int Duration);
	inline void PushPhoneme (int Word, const char *Phoneme, int Duration);

	/////////////////////////////////////////
	// File/String Output functions
	void OutputMbrolaPhonemeFile (const char *Filename);
	void OutputMbrolaPhonemeString (string &MBROLA_Utterance);
	void OutputGSTPhonemeFile (const char *Filename);
	void OutputMbrolaUtteranceInfo (ofstream &MBRFile);
	void OutputMbrolaUtteranceInfo (string &MBROLA_Utterance);
	void OutputGSTUtteranceInfo (ofstream &GSTFile);

	inline bool UtteranceInfoValid ();

	CTTS_SMLNode *InsertTag (const char *TagName);
	CTTS_SMLNode *InsertAttribute (const char *AttribName, const char *AttribValue);
	void DeleteTag (CTTS_SMLNode *pTag);
};

///////////////////////////////////////////////////////////////
// INLINE functions
// 
///////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// PrintNodeType
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_SMLNode::PrintNodeType ()
{
	switch (m_Type)
		{
		case SML_DOCUMENT_NODE	:	cout << "SML_DOCUMENT_NODE";
									break;

		case SML_ELEMENT_NODE	:	cout << "SML_ELEMENT_NODE";
									break;

		case SML_TEXT_NODE		:	cout << "SML_TEXT_NODE";
									break;

		case SML_ATTRIBUTE_NODE	:	cout << "SML_ATTRIBUTE_NODE";
									break;

		default					:	cout << "unknown type";
									break;
		}

	cout << endl;
}

////////////////////////////////////////////////////////////////////////////////
// Parent
//
////////////////////////////////////////////////////////////////////////////////
inline CTTS_SMLNode *CTTS_SMLNode::Parent()
{
	return m_Parent;
}

////////////////////////////////////////////////////////////////////////////////
// Children
//
////////////////////////////////////////////////////////////////////////////////
inline CTTS_SMLNode *CTTS_SMLNode::Children()
{
	return m_Children;
}

////////////////////////////////////////////////////////////////////////////////
// Prev
//
////////////////////////////////////////////////////////////////////////////////
inline CTTS_SMLNode *CTTS_SMLNode::Prev()
{
	return m_Prev;
}

////////////////////////////////////////////////////////////////////////////////
// Next
//
////////////////////////////////////////////////////////////////////////////////
inline CTTS_SMLNode *CTTS_SMLNode::Next()
{
	return m_Next;
}

////////////////////////////////////////////////////////////////////////////////
// Attributes
//
////////////////////////////////////////////////////////////////////////////////
inline CTTS_SMLNode *CTTS_SMLNode::Attributes()
{
	return m_Attributes;
}

////////////////////////////////////////////////////////////////////////////////
// UtteranceInfo
//
////////////////////////////////////////////////////////////////////////////////
inline CTTS_UtteranceInfo *CTTS_SMLNode::UtteranceInfo ()
{
	return m_pUtteranceInfo;
}

////////////////////////////////////////////////////////////////////////////////
// SetParent
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_SMLNode::SetParent (CTTS_SMLNode *Parent)
{
	m_Parent = Parent;
}

////////////////////////////////////////////////////////////////////////////////
// SetChildren
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_SMLNode::SetChildren (CTTS_SMLNode *FirstChild)
{
	m_Children = FirstChild;
}

////////////////////////////////////////////////////////////////////////////////
// SetNext
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_SMLNode::SetNext (CTTS_SMLNode *NextNode)
{
	m_Next = NextNode;
}

////////////////////////////////////////////////////////////////////////////////
// SetPrev
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_SMLNode::SetPrev (CTTS_SMLNode *PrevNode)
{
	m_Prev = PrevNode;
}

////////////////////////////////////////////////////////////////////////////////
// SetAttributes
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_SMLNode::SetAttributes (CTTS_SMLNode *FirstAttribute)
{
	m_Attributes = FirstAttribute;
}

////////////////////////////////////////////////////////////////////////////////
// PrintCurrentNode
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_SMLNode::PrintCurrentNode ()
{
	// Print this node
	PrintNodeType ();
	cout << "Name: " << m_Name.c_str() << endl;
	cout << "Content: " << m_Content.c_str() << endl;

	// Print attributes of this node
	if (Attributes() != NULL)
		{
		cout << "Attributes: \n----------------------------------" << endl;
		Attributes()->PrintNode();
		cout << "----------------------------------" << endl;
		}
}

////////////////////////////////////////////////////////////////////////////////
// IsTextNode
//
////////////////////////////////////////////////////////////////////////////////
inline bool CTTS_SMLNode::IsTextNode ()
{
	if ((m_Type == SML_TEXT_NODE) && (IsNode ("text")))
		return true;
	else
		return false;
}

////////////////////////////////////////////////////////////////////////////////
// IsCDataSectionNode
//
////////////////////////////////////////////////////////////////////////////////
inline bool CTTS_SMLNode::IsCDataSectionNode ()
{
	if ((m_Type == SML_CDATA_SECTION_NODE) && (IsNode ("cdata")))
		return true;
	else
		return false;
}

////////////////////////////////////////////////////////////////////////////////
// IsTopLevelUtteranceNode
//
////////////////////////////////////////////////////////////////////////////////
inline bool CTTS_SMLNode::IsTopLevelUtteranceNode ()
{
	if (IsEmotionNode () || IsNode ("embed"))
		return true;
	else
		return false;
}

////////////////////////////////////////////////////////////////////////////////
// IsSectionNode
//
////////////////////////////////////////////////////////////////////////////////
inline bool CTTS_SMLNode::IsSectionNode ()
{
	if (IsEmotionNode() || IsNode ("embed"))
		return true;
	else
		return false;
}

////////////////////////////////////////////////////////////////////////////////
// IsNode
//
////////////////////////////////////////////////////////////////////////////////
inline bool CTTS_SMLNode::IsNode (const char *Name)
{
	if (m_Name == Name)
		return true;
	else
		return false;
}

////////////////////////////////////////////////////////////////////////////////
// IsEmotionNode
//
////////////////////////////////////////////////////////////////////////////////
inline bool CTTS_SMLNode::IsEmotionNode ()
{
	if ((m_Type == SML_ELEMENT_NODE) &&
		(IsNode ("neutral") || IsNode ("happy") || IsNode ("angry") || IsNode ("sad") || IsNode ("fear")))
		return true;
	else
		return false;
}

////////////////////////////////////////////////////////////////////////////////
// GetContent
//
////////////////////////////////////////////////////////////////////////////////
inline const char *CTTS_SMLNode::GetContent ()
{
	return m_Content.c_str ();
}

////////////////////////////////////////////////////////////////////////////////
// SetContent
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_SMLNode::SetContent (const char *Content)
{
	m_Content = Content;
}

////////////////////////////////////////////////////////////////////////////////
// PushPhoneme
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_SMLNode::PushPhoneme (const char *Phoneme, int Duration)
{
	int LastWord = UtteranceInfo()->WordList.size() - 1;

	assert (LastWord >= 0);
	string Temp = string (Phoneme);
	UtteranceInfo()->WordList[LastWord].AddPhoneme (Temp, Duration);
}

////////////////////////////////////////////////////////////////////////////////
// PushPhoneme
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_SMLNode::PushPhoneme (int Word, const char *Phoneme, int Duration)
{
	assert ((Word >= 0) && (Word < UtteranceInfo()->WordList.size()));

	string Temp = string (Phoneme);
	UtteranceInfo()->WordList[Word].AddPhoneme (Temp, Duration);
}

////////////////////////////////////////////////////////////
// UtteranceInfoValid
//
// UtteranceInfo is valid if it contains at least one word
////////////////////////////////////////////////////////////
inline bool CTTS_SMLNode::UtteranceInfoValid ()
{
	if (UtteranceInfo()->WordList.size() > 0)
		return true;
	else
		return false;
}

#endif