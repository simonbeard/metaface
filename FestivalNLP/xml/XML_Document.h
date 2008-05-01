////////////////////////////////////////////////////////////////////
// CXML_Document template class HEADER file
//
////////////////////////////////////////////////////////////////////

#ifndef __CXML_DOCUMENT__
#define __CXML_DOCUMENT__

#include <string>
using std::string;

template <class NodeType>
class CXML_Document
{
private:
	NodeType *m_pRoot;
	string m_XMLVersion;

public:
	CXML_Document ()
		{
		m_pRoot = new NodeType (CXML_DOCUMENT_NODE, "root", NULL, NULL, NULL, NULL, NULL, NULL);
		};

	~CXML_Document ()
		{
		delete m_pRoot;
		};

	inline NodeType *Root ()
		{
		return m_pRoot;
		};

	inline void SetXMLVersion (const char *Version)
		{
		m_XMLVersion = Version;
		};

	inline const char *GetXMLVersion ()
		{
		return m_XMLVersion.c_str();
		};
};

#endif