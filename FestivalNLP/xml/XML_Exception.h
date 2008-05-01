////////////////////////////////////////////////////////////////////////////////
//
// CXML_Exception CLASS Header file.
//
////////////////////////////////////////////////////////////////////////////////
#ifndef __CXML_EXCEPTION_H__
#define __CXML_EXCEPTION_H__

#include <iostream.h>
#include <assert.h>
#include <string>

using std::string;

const int CXML_EXC_NUM_OF_ERRORS = 2;

/////////////////
// ERROR CODES
/////////////////
const int CXML_EXC_NO_FILE = 0;
const int CXML_EXC_PARSER_ERROR = 1;


class CXML_Exception
{
private:
	int m_ExCode;
	string m_ErrorMsg [CXML_EXC_NUM_OF_ERRORS];

public:
	CXML_Exception (int ExCode)
		{
		m_ErrorMsg[0] = "No filename has been specified.";
		m_ErrorMsg[1] = "Parser error occurred.";

		m_ExCode = ExCode;
		};

	const char *GiveInfo ()
		{
		assert ((m_ExCode >= 0) && (m_ExCode < CXML_EXC_NUM_OF_ERRORS));
		return m_ErrorMsg[m_ExCode].c_str();
		};
};

#endif