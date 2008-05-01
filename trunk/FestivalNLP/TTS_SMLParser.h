////////////////////////////////////////////////////////////////////////////////
//
// CTTS_SMLParser CLASS Header file.
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_SML_PARSER_H__
#define __CTTS_SML_PARSER_H__

#include <iostream.h>
#include <string>
#include "TTS_libxml_includes.h"
#include "TTS_SMLException.h"
#include "TTS_SMLDocument.h"

//using std::cout;
//using std::cerr;
//using std::string;

class CTTS_SMLParser
{
private:
	xmlDocPtr m_Document;
	void BuildSMLDocument (xmlNodePtr CurrNode, CTTS_SMLNode *ParentNode, CTTS_SMLNode *PrevNode);
	void BuildSMLDocument (xmlAttrPtr CurrNode, CTTS_SMLNode *ParentNode, CTTS_SMLNode *PrevNode);
	enum SMLNodeType ConvertNodeType (int);
	bool IsTextNode (xmlNodePtr CurrNode);
	bool IsEmptyNode (xmlNodePtr CurrNode);

protected:
	string m_Filename;

public:
	CTTS_SMLParser ()	
		{
		m_Document = NULL;
		};

	CTTS_SMLParser (const char *Filename)
		{
		m_Document = NULL;
		m_Filename = Filename;
		};

	inline void SetFilename (const char *Filename);

	inline const char *GetFilename ();

	inline void PrintXMLDocInfo ();

	CTTS_SMLDocument* ParseFile ();
};

///////////////////////////////////////////////////////////////
// INLINE functions
// 
///////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// SetFilename
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_SMLParser::SetFilename (const char *Filename)
	{
	m_Filename = Filename;
	}

////////////////////////////////////////////////////////////////////////////////
// GetFilename
//
////////////////////////////////////////////////////////////////////////////////
inline const char *CTTS_SMLParser::GetFilename ()
	{
	return m_Filename.c_str ();
	}

////////////////////////////////////////////////////////////////////////////////
// PrintXMLDocInfo
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_SMLParser::PrintXMLDocInfo ()
{
	cout << "m_Filename: " << m_Filename.c_str() << endl;
}

#endif