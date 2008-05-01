////////////////////////////////////////////////////////////////////////////////
//
// CTTS_SMLFilter CLASS IMPLEMENTATION file.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_SMLFilter.h"

////////////////////////////////////////////////////////////////////////////////
// 
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLFilter::Initialise ()
{
	m_pTagsDoc = NULL;
	m_pTable = new CTTS_StringVector;
	m_pTable->InsertOrder ("?xml");
	m_pTable->InsertOrder ("!DOCTYPE");
	m_pTable->InsertOrder ("!--");
	m_pTable->InsertOrder ("![CDATA[");
	OPEN_TAG_CHAR = '<';
	CLOSE_TAG_CHAR = '>';

	if (ParseInputFiles (m_TagsDefinitionFilename.c_str(), &m_pTagsDoc))
		{
		FillTable (m_pTagsDoc->Root());
		}
}

////////////////////////////////////////////////////////////////////////////////
// 
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLFilter::FilterFile (const char *InputFile, const char *OutputFile, const char *TagsDefinitionFile)
{
	StripUnknownTags (InputFile, OutputFile);

	if (ParseInputFiles (TagsDefinitionFile, &m_pTagsDoc))
		{
		FillTable (m_pTagsDoc->Root());
		StripUnknownTags (InputFile, OutputFile);
		}
}

////////////////////////////////////////////////////////////////////////////////
// 
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLFilter::FilterFile (const char *InputFile, const char *OutputFile)
{
	StripUnknownTags (InputFile, OutputFile);
}

////////////////////////////////////////////////////////////////////////////////
// 
//
////////////////////////////////////////////////////////////////////////////////
bool CTTS_SMLFilter::ParseInputFiles (const char *TagsDefinitionFile, CXML_Document <CTTS_TagsNode> **ppTagsDoc)
{
	bool ParsedOk = true;
	CXML_Parser <CTTS_TagsNode> *pTagsParser = new CXML_Parser <CTTS_TagsNode> (TagsDefinitionFile);

	*ppTagsDoc = NULL;

	try
		{
		*ppTagsDoc = pTagsParser->ParseFile ();
		delete pTagsParser;
		}

	catch (CXML_Exception e)
		{
		cerr << "Error in CTTS_SMLFilter::ParseInputFiles() : " << e.GiveInfo () << endl;
		ParsedOk = false;
		}

	catch (...)
		{
		cerr << "Unknown error occurred\n";
		ParsedOk = false;
		}

	return ParsedOk;
}

////////////////////////////////////////////////////////////////////////////////
// 
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLFilter::FillTable (CTTS_TagsNode *pNode)
{
	if (pNode->IsTextNode())
		{
		m_pTable->InsertOrder (pNode->GetContent());
		}

	if (pNode->Children())
		{
		FillTable (pNode->Children());
		}

	if (pNode->Next())
		{
		FillTable (pNode->Next());
		}
}

////////////////////////////////////////////////////////////////////////////////
// 
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLFilter::StripUnknownTags (const char *InFilename, const char *OutFilename)
{
	FILE *InFile;
	FILE *OutFile;

	if ((InFile = fopen (InFilename, "r")) == NULL)
		{
		cerr << "Error in CTTS_SMLFilter::StripUnknownTags (): Cannot open file " << InFilename << " for reading.\n";
		exit (0);
		}

	if ((OutFile = fopen (OutFilename, "w")) == NULL)
		{
		fclose (InFile);
		cerr << "Error in CTTS_SMLFilter::StripUnknownTags (): Cannot open file " << OutFilename << " for writing.\n";
		exit (0);
		}


	int c;
	char LastChar;
	char *pTagName;
	bool HitEndTag = false;
	bool Ignore = false;
	bool InCDataSection = false;
	int SquareRBraces = 0;

	while ((c = fgetc (InFile)) != EOF)
		{
		if (!InCDataSection)
			{
			if (c == '<')
				{
				pTagName = GetTagName (InFile, &LastChar, &Ignore, &HitEndTag);
	
				if (TagTable()->Search (pTagName))
					{
					if (HitEndTag)
						{
						fprintf (OutFile, "</");
						}
					else
						{
						fprintf (OutFile, "<");
						}
	
					fprintf (OutFile, "%s%c", pTagName, LastChar);
					if (strcmp (pTagName, "![CDATA[") == 0)
						{
						InCDataSection = true;
						continue;
						}
					Ignore = false;
					}
				else
					{
					// Debug
					cout << "Ignoring tag: " << pTagName << endl;
					if (LastChar == '>')
						Ignore = false;
					else
						Ignore = true;
					}

				delete pTagName;
				}
			else if (c == '>')
				{
				if (!Ignore)
					{
					fprintf (OutFile, "%c", c);
					}
				Ignore = false;
				}
			else if (!Ignore)
				{
				fprintf (OutFile, "%c", c);
				}
			}
		else
			{
			fprintf (OutFile, "%c", c);

			if ((c == '>') && (SquareRBraces >= 2))
				InCDataSection = false;
			else if (c == ']')
				SquareRBraces++;
			else
				SquareRBraces = 0;
			}
		}

	///////////////////////
	// Clean up
	fclose (InFile);
	fclose (OutFile);
}

////////////////////////////////////////////////////////////////////////////////
// 
//
////////////////////////////////////////////////////////////////////////////////
char *CTTS_SMLFilter::GetTagName (FILE *InFile, char *LastChar, bool *Ignore, bool *HitEndTag)
{
	string Str;

	// First char can be a '/' (for end tag)
	int c = fgetc (InFile);

	if (c != '/')
		{
		*HitEndTag = false;
		Str += c;
		}
	else
		{
		*HitEndTag = true;
		}

	// Get rest of name
	while (c = fgetc (InFile))
		{
		if (isspace (c))
			{
			break;
			}
		else if (c == '>')
			{
			*Ignore = false;
			break;
			}
		// hit an end of empty tag "/>"
		else if (c == '/')
			{
			*Ignore = false;
			break;
			}
		else
			{
			Str += c;
			}
		}

	char *TagName = strdup (Str.c_str());
	*LastChar = (char) c;
	return TagName;
}
