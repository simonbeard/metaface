////////////////////////////////////////////////////////////////////////////////
//
// CXML_Node template class HEADER file
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CXML_NODE__
#define __CXML_NODE__

#include <assert.h>
#include <string>

using std::string;

enum XMLClassType
		{
		CXML_DOCUMENT_NODE,
		CXML_ELEMENT_NODE,
		CXML_ATTRIBUTE_NODE,
		CXML_TEXT_NODE,
		CXML_CDATA_SECTION_NODE
		};

template <class ClassType>
class CXML_Node
{
private:
	enum XMLClassType m_Type;
	string m_Name;
	string m_Content;
	ClassType *m_pParent;
	ClassType *m_pChildren;
	ClassType *m_pNext;
	ClassType *m_pPrev;
	ClassType *m_pAttributes;

public:
	CXML_Node (enum XMLClassType Type, const char *Name, const char *Content, 
			ClassType *pParent, ClassType *pChildren, ClassType *pPrev, ClassType *pNext, 
			ClassType *pAttributes)
		{
		m_Type = Type;
		if (Name != NULL)
			m_Name = Name;
		if (Type == CXML_TEXT_NODE)
			m_Content = Content;

		// Following if statement is required because of bug in libxml v2 - XML_CDATA_SECTION_NODE nodes aren't
		// given a name.  If future versions do, then take out the if statement.
		else if (Type == CXML_CDATA_SECTION_NODE)
			m_Name = "cdata";
		m_pParent = pParent;
		m_pChildren = pChildren;
		m_pPrev = pPrev;
		m_pNext = pNext;
		m_pAttributes = pAttributes;
		};

	~CXML_Node ()
		{
		// delete children
		if (Children())
			{
			delete Children ();
			}

		// delete next node in tree
		if (Next())
			{
			delete Next ();
			}

		// delete attribute list
		if (Attributes())
			{
			delete Attributes ();
			}

		};

	//////////////////////////////////////////////////////////////
	// ACCESSORS
	//////////////////////////////////////////////////////////////
	inline ClassType *Parent ()
		{
		return m_pParent;
		};

	inline ClassType *Children ()
		{
		return m_pChildren;
		};

	inline ClassType *Prev ()
		{
		return m_pPrev;
		};

	inline ClassType *Next ()
		{
		return m_pNext;
		};

	inline ClassType *Attributes ()
		{
		return m_pAttributes;
		};

	inline const char *GetName ()
		{
		return m_Name.c_str();
		};

	inline const char *GetContent ()
		{
		return m_Content.c_str();
		};


	//////////////////////////////////////////////////////////////
	// MUTATORS
	//////////////////////////////////////////////////////////////
	inline void SetChild (ClassType *pChild)
		{
		m_pChildren = pChild;
		};

	inline void SetName (const char *Name)
		{
		m_Name = Name;
		};

	inline void SetContent (const char *Content)
		{
		m_Content = Content;
		};

	inline void SetChildren (ClassType *pFirstChild)
		{
		m_pChildren = pFirstChild;
		};

	inline void SetNext (ClassType *pNextNode)
		{
		m_pNext = pNextNode;
		};

	inline void SetAttributes (ClassType *pFirstAttribute)
		{
		m_pAttributes = pFirstAttribute;
		};


	//////////////////////////////////////////////////////////////
	// PROBES
	//////////////////////////////////////////////////////////////
	inline bool IsNode (const char *Name)
		{
		if (m_Name == Name)
			return true;
		else
			return false;
		};

	inline bool IsTextNode ()
		{
		if ((m_Type == CXML_TEXT_NODE) && (IsNode ("text")))
			return true;
		else
			return false;
		};

	inline bool IsCDataSectionNode ()
		{
		if ((m_Type == CXML_CDATA_SECTION_NODE) && (IsNode ("cdata")))
			return true;
		else
			return false;
		};

	inline bool IsType (enum XMLClassType Type)
		{
		if (m_Type == Type)
			return true;
		else
			return false;
		};

	char *GetAttribute (const char *AttributeName)
		{
		assert (AttributeName != NULL);

		CXML_Node <ClassType> *pAttributeNode = this->Attributes();
		CXML_Node <ClassType> *pValueNode;
	
		if (pAttributeNode != NULL)
			{
			pValueNode = pAttributeNode->Children();
	
			while (pAttributeNode != NULL)
				{
				if (pAttributeNode->IsNode (AttributeName))
					{
					char *AttributeValue = strdup (pValueNode->GetContent ());
					return (AttributeValue);
					}
				else
					{
					// go to next attribute
					pAttributeNode = pAttributeNode->Next();
					if (pAttributeNode)
						pValueNode = pAttributeNode->Children();
					}
				}
			}

		return NULL;
		}
};

#endif