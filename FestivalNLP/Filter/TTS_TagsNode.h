////////////////////////////////////////////////////////////////////
// CTTS_TagsNode template class HEADER file
//
////////////////////////////////////////////////////////////////////

#ifndef __CTTS_TAGS_NODE__
#define __CTTS_TAGS_NODE__

#include "../XML/XML_Node.h"
#include <iostream.h>

class CTTS_TagsNode : public CXML_Node <CTTS_TagsNode>
{
private:

public:
	CTTS_TagsNode (enum XMLClassType Type, const char *Name, const char *Content, 
			CTTS_TagsNode *pParent, CTTS_TagsNode *pChildren, CTTS_TagsNode *pPrev, CTTS_TagsNode *pNext, CTTS_TagsNode *pAttributes)

			: CXML_Node <CTTS_TagsNode> (Type, Name, Content, pParent, pChildren, pPrev, pNext, pAttributes)
		{
		};

	~CTTS_TagsNode ()
		{
		};

	void OutputTagNames ();
};

#endif