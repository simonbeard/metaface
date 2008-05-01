////////////////////////////////////////////////////////////////////////////////
//
// CTTS_SMLParser IMPLEMENTATION file.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_SMLParser.h"

/////////////////////////////////////////////////////////////////
// ParseFile
//
// Wrapper for libxml's xmlParseFile() function.
/////////////////////////////////////////////////////////////////
CTTS_SMLDocument* CTTS_SMLParser::ParseFile ()
{
	///////////////////////////////////////
	// Use libxml Parser to parse sml file
	///////////////////////////////////////
	if (m_Filename.length() == 0)
		throw CTTS_SMLException (CSML_EXC_NO_FILE);

	m_Document = xmlParseFile (m_Filename.c_str());
	if (!m_Document)
		throw CTTS_SMLException (CSML_EXC_PARSER_ERROR);

	///////////////////////////////////////
	// Make our own tree from info in 
	// m_Document
	///////////////////////////////////////
	CTTS_SMLDocument *pSMLDoc = new CTTS_SMLDocument ();
	if (!pSMLDoc)
		{
		cerr << "error unable to allocate memory for CTTS_SMLDocument object" << endl;
		exit (8);
		}

	pSMLDoc->SetXMLVersion ((const char *) m_Document->version);

	if (m_Document->children->type == XML_DTD_NODE)
		BuildSMLDocument (m_Document->children->next, pSMLDoc->Root(), NULL);
	else
		BuildSMLDocument (m_Document->children, pSMLDoc->Root(), NULL);

	////////////////////////////////////////
	// Clean up, delete libxml's document
	////////////////////////////////////////
	xmlFreeDoc (m_Document);

	return pSMLDoc;
}

////////////////////////////////////////////////////////////////////////////////
// BuildSMLDocument
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLParser::BuildSMLDocument (xmlNodePtr CurrNode, CTTS_SMLNode *ParentNode, CTTS_SMLNode *PrevNode)
{
	// If CurrNode is a comment node, ignore it completely (don't put
	// it in the SML document tree.
	if (CurrNode->type == XML_COMMENT_NODE)
		{
		if (CurrNode->next != NULL)
			BuildSMLDocument (CurrNode->next, ParentNode, PrevNode);
		}

	// Check also that if the node is a text node, that it contains some text.
	// If it doesn't then don't put it in the SML document tree.
	else if ((IsTextNode (CurrNode)) && (IsEmptyNode (CurrNode)))
		{
		if (CurrNode->next != NULL)
			BuildSMLDocument (CurrNode->next, ParentNode, PrevNode);
		}

	else
		{
		// create current node
		CTTS_SMLNode *pNewNode = new CTTS_SMLNode (ConvertNodeType (CurrNode->type),
											(const char *) CurrNode->name,
											(const char *) CurrNode->content,
											ParentNode, NULL,
											PrevNode, NULL, NULL);

		// make parent point to new node, only if parent hasn't been set yet.
		if (pNewNode->Parent()->Children() == NULL)
			pNewNode->Parent()->SetChildren (pNewNode);

		// set next pointer of previous node to point to this node
		if (pNewNode->Prev() != NULL)
			pNewNode->Prev()->SetNext (pNewNode);

		// set attributes here
		if (CurrNode->properties != NULL)
			BuildSMLDocument (CurrNode->properties, pNewNode, NULL);
		

		// go to Children
		if (CurrNode->children != NULL)
			BuildSMLDocument (CurrNode->children, pNewNode, NULL);

		// go to next in list
		if (CurrNode->next != NULL)
			BuildSMLDocument (CurrNode->next, pNewNode->Parent(), pNewNode);
		}
}

////////////////////////////////////////////////////////////////////////////////
// BuildSMLDocument
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLParser::BuildSMLDocument (xmlAttrPtr CurrNode, CTTS_SMLNode *ParentNode, CTTS_SMLNode *PrevNode)
{
	// create current node
	CTTS_SMLNode *pNewNode = new CTTS_SMLNode (ConvertNodeType (CurrNode->type),
										(const char *) CurrNode->name, NULL,
										ParentNode, NULL,
										PrevNode, NULL, NULL);

	// set parent's attribute pointer to point to this node, only if
	// it hasn't been set yet.
	if (pNewNode->Parent()->Attributes() == NULL)
		pNewNode->Parent()->SetAttributes (pNewNode);

	// set next pointer of previous node to point to this node
	if (pNewNode->Prev() != NULL)
		pNewNode->Prev()->SetNext (pNewNode);

	// go to Children
	if (CurrNode->children != NULL)
		BuildSMLDocument (CurrNode->children, pNewNode, NULL);

	// go to next in list
	if (CurrNode->next != NULL)
		BuildSMLDocument (CurrNode->next, pNewNode->Parent(), pNewNode);
}

////////////////////////////////////////////////////////////////////////////////
// IsTextNode
//
////////////////////////////////////////////////////////////////////////////////
bool CTTS_SMLParser::IsTextNode (xmlNodePtr CurrNode)
{
	if ((CurrNode->type == XML_TEXT_NODE) && (strcmp ((const char *)CurrNode->name, "text") == 0))
		return true;
	else
		return false;
}

////////////////////////////////////////////////////////////////////////////////
// IsEmptyNode
//
////////////////////////////////////////////////////////////////////////////////
bool CTTS_SMLParser::IsEmptyNode (xmlNodePtr CurrNode)
{
	string Content = (const char *) CurrNode->content;
	bool HasOnlyWhiteSpace = true;
	char Ch;

	for (int i=0; i < Content.length(); i++)
		{
		Ch = Content[i];
		if ((Ch != ' ') && (Ch != '\n') && (Ch != '\t'))
			{
			HasOnlyWhiteSpace = false;
			break;
			}
		}

	return HasOnlyWhiteSpace;
}

////////////////////////////////////////////////////////////////////////////////
// CTTS_SMLParser
//
////////////////////////////////////////////////////////////////////////////////
enum SMLNodeType CTTS_SMLParser::ConvertNodeType (int NodeType)
{
	switch (NodeType)
		{
		case XML_DOCUMENT_NODE		: return SML_DOCUMENT_NODE;

		case XML_ELEMENT_NODE		: return SML_ELEMENT_NODE;

		case XML_ATTRIBUTE_NODE		: return SML_ATTRIBUTE_NODE;

		case XML_TEXT_NODE			: return SML_TEXT_NODE;

		case XML_CDATA_SECTION_NODE	: return SML_CDATA_SECTION_NODE;

		default						: return SML_TEXT_NODE;
		}
}
