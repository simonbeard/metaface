////////////////////////////////////////////////////////////////////
// CXML_DocumentIterator template class HEADER file
//
////////////////////////////////////////////////////////////////////

#ifndef __CXML_DOCUMENT_ITERATOR_H__
#define __CXML_DOCUMENT_ITERATOR_H__

#include "XML_Document.h"
#include "XML_Node.h"

template <class NodeType>
class CXML_DocumentIterator
{
private:
	CXML_Node <NodeType> *m_pCurrNode;
	CXML_Document <NodeType> *m_pDoc;

public:
	CXML_DocumentIterator ()
		{
		m_pCurrNode = NULL;
		m_pDoc = NULL;
		};

	CXML_DocumentIterator (CXML_Document <NodeType> *pDocument)
		{
		assert (pDocument != NULL);

		m_pCurrNode = NULL;
		m_pDoc = pDocument;
		};

	inline void SetDocument (CXML_Document <NodeType> *pDocument)
		{
		assert (pDocument != NULL);
		m_pDoc = pDocument;
		};

	inline CXML_Document <NodeType> *Document ()
		{
		return m_pDoc;
		};

	inline NodeType *CurrentNode ()
		{
		return (NodeType *) m_pCurrNode;
		};

	inline void SetCurrentNode (CXML_Node <NodeType> *pNode)
		{
		m_pCurrNode = pNode;
		};
};

#endif