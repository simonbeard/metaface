////////////////////////////////////////////////////////////////////////////////
//
// CTTS_SMLFilter CLASS Header file.
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_SML_FITLER_H__
#define __CTTS_SML_FILTER_H__

#include "../XML/XML_Parser.h"
#include "TTS_TagsNode.h"
#include "TTS_StringVector.h"

class CTTS_SMLFilter
{
private:
	CXML_Document <CTTS_TagsNode> *m_pTagsDoc;
	CTTS_StringVector *m_pTable;
	string m_TagsDefinitionFilename;
	char OPEN_TAG_CHAR;
	char CLOSE_TAG_CHAR;

	bool ParseInputFiles (const char *TagsDefinitionFile, CXML_Document <CTTS_TagsNode> **pm_pTagsDoc);
	void FillTable (CTTS_TagsNode *pNode);
	void StripUnknownTags (const char *InFilename, const char *OutFilename);
	char *GetTagName (FILE *InFile, char *LastChar, bool *Ignore, bool *HitEndTag);

public:
	CTTS_SMLFilter ()
		{
		InitialiseFilenames ();
		Initialise ();
		};

	CTTS_SMLFilter (const char *TagsDefinitionFile)
		{
		m_TagsDefinitionFilename = TagsDefinitionFile;
		Initialise ();
		};

	~CTTS_SMLFilter ()
		{
		delete m_pTable;
		if (m_pTagsDoc)
			delete m_pTagsDoc;
		};

	inline void InitialiseFilenames ();
	void Initialise();
	void FilterFile (const char *InputFile, const char *OutputFile, const char *TagsDefinitionFile);
	void FilterFile (const char *InputFile, const char *OutputFile);
	inline CTTS_StringVector *TagTable ();
};

///////////////////////////////////////////////////////////
// INLINE functions
///////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// TagTable
//
////////////////////////////////////////////////////////////////////////////////
inline CTTS_StringVector *CTTS_SMLFilter::TagTable ()
{
	return m_pTable;
}

////////////////////////////////////////////////////////////////////////////////
// InitialiseFilenames
//
////////////////////////////////////////////////////////////////////////////////
inline void CTTS_SMLFilter::InitialiseFilenames ()
{
#ifdef __TTS_WIN98__
		m_TagsDefinitionFilename = "c:\\users\\damh\\ww\\tag-names.xml";
#else
	#ifdef WIN32
		m_TagsDefinitionFilename = "c:/users/damh/ww/tag-names.xml";
	#else
		m_TagsDefinitionFilename = "/usr/local/share/fae/TTS_rc/tag-names.xml";
	#endif
#endif
}

#endif
