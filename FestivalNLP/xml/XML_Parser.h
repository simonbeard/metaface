////////////////////////////////////////////////////////////////////////////////
//
// CXML_Parser CLASS Header file.
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CXML_PARSER_H__
#define __CXML_PARSER_H__

#include <iostream.h>
#include <string>
#include "../TTS_libxml_includes.h"
#include "XML_Exception.h"
#include "XML_Document.h"

using std::string;

template <class NodeType>
class CXML_Parser
{
private:
	xmlDocPtr m_Document;

	////////////////////////////////////////////////////////////////////////////////
	// BuildXMLDocument
	//
	////////////////////////////////////////////////////////////////////////////////
	void BuildXMLDocument (xmlNodePtr CurrNode, NodeType *ParentNode, NodeType *PrevNode)
	{
		// If CurrNode is a comment node, ignore it completely (don't put
		// it in the XML document tree.
		if (CurrNode->type == XML_COMMENT_NODE)
			{
			if (CurrNode->next != NULL)
				BuildXMLDocument (CurrNode->next, ParentNode, PrevNode);
			}
	
		// Check also that if the node is a text node, that it contains some text.
		// If it doesn't then don't put it in the XML document tree.
		else if ((IsTextNode (CurrNode)) && (IsEmptyNode (CurrNode)))
			{
			if (CurrNode->next != NULL)
				BuildXMLDocument (CurrNode->next, ParentNode, PrevNode);
			}
	
		else
			{
			// create current node
			NodeType *pNewNode = new NodeType (ConvertNodeType (CurrNode->type),
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
				BuildXMLDocument (CurrNode->properties, pNewNode, NULL);
		

			// go to Children
			if (CurrNode->children != NULL)
				BuildXMLDocument (CurrNode->children, pNewNode, NULL);
	
			// go to next in list
			if (CurrNode->next != NULL)
				BuildXMLDocument (CurrNode->next, (NodeType *) pNewNode->Parent(), pNewNode);
			}
	};

	////////////////////////////////////////////////////////////////////////////////
	// BuildXMLDocument
	//
	////////////////////////////////////////////////////////////////////////////////
	void BuildXMLDocument (xmlAttrPtr CurrNode, NodeType *ParentNode, NodeType *PrevNode)
	{
		// create current node
		NodeType *pNewNode = new NodeType (ConvertNodeType (CurrNode->type),
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
			BuildXMLDocument (CurrNode->children, pNewNode, NULL);
	
		// go to next in list
		if (CurrNode->next != NULL)
			BuildXMLDocument (CurrNode->next, (NodeType *) pNewNode->Parent(), pNewNode);
	};

	////////////////////////////////////////////////////////////////////////////////
	// IsTextNode
	//
	////////////////////////////////////////////////////////////////////////////////
	bool IsTextNode (xmlNodePtr CurrNode)
	{
		if ((CurrNode->type == XML_TEXT_NODE) && (strcmp ((const char *)CurrNode->name, "text") == 0))
			return true;
		else
			return false;
	};

	////////////////////////////////////////////////////////////////////////////////
	// IsEmptyNode
	//
	////////////////////////////////////////////////////////////////////////////////
	bool IsEmptyNode (xmlNodePtr CurrNode)
	{
		string Content = (const char *) CurrNode->content;
		bool HasOnlyWhiteSpace = true;
	
		for (int i=0; i < Content.length(); i++)
			{
			if (!isspace (Content[i]))
				{
				return false;
				}
			}

		return true;
	};

	enum XMLClassType ConvertNodeType (int LibXMLNodeType)
	{
		switch (LibXMLNodeType)
			{
			case XML_DOCUMENT_NODE		: return CXML_DOCUMENT_NODE;
	
			case XML_ELEMENT_NODE		: return CXML_ELEMENT_NODE;
	
			case XML_ATTRIBUTE_NODE		: return CXML_ATTRIBUTE_NODE;
	
			case XML_TEXT_NODE			: return CXML_TEXT_NODE;

			case XML_CDATA_SECTION_NODE	: return CXML_CDATA_SECTION_NODE;
	
			default						: return CXML_TEXT_NODE;
			}
	};

protected:
	string m_Filename;

public:
	CXML_Parser ()	
		{
		m_Document = NULL;
		};

	CXML_Parser (const char *Filename)
		{
		m_Document = NULL;
		m_Filename = Filename;
		};

	inline void SetFilename (const char *Filename)
		{
		m_Filename = Filename;
		};

	inline const char *GetFilename ()
		{
		return m_Filename.c_str();
		};

	/////////////////////////////////////////////////////////////////
	// ParseFile
	//
	// Wrapper for libxml's xmlParseFile() function.
	/////////////////////////////////////////////////////////////////
	CXML_Document <NodeType>* ParseFile ()
	{
		///////////////////////////////////////
		// Use libxml Parser to parse sml file
		///////////////////////////////////////
		if (m_Filename.length() == 0)
			throw CXML_Exception (CXML_EXC_NO_FILE);
	
		m_Document = xmlParseFile (m_Filename.c_str());
		if (!m_Document)
			throw CXML_Exception (CXML_EXC_PARSER_ERROR);
	
		///////////////////////////////////////
		// Make our own tree from info in 
		// m_Document
		///////////////////////////////////////
		CXML_Document <NodeType> *pXMLDoc = new CXML_Document <NodeType> ();
		if (!pXMLDoc)
			{
			cerr << "error unable to allocate memory for CXML_Document object" << endl;
			exit (8);
			}
	
		pXMLDoc->SetXMLVersion ((const char *) m_Document->version);
	
		if (m_Document->children->type == XML_DTD_NODE)
			BuildXMLDocument (m_Document->children->next, pXMLDoc->Root(), NULL);
		else
			BuildXMLDocument (m_Document->children, pXMLDoc->Root(), NULL);

		////////////////////////////////////////
		// Clean up, delete libxml's document
		////////////////////////////////////////
		xmlFreeDoc (m_Document);

		return pXMLDoc;
	};
};

#endif