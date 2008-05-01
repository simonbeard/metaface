////////////////////////////////////////////////////////////////////////////////
//
// CTTS_SMLException CLASS Header file.
//
////////////////////////////////////////////////////////////////////////////////
#ifndef __CTTS_SML_EXCEPTION_H__
#define __CTTS_SML_EXCEPTION_H__

#include <iostream.h>
#include <string>

using std::string;

/////////////////
// ERROR CODES
/////////////////
const int CSML_EXC_NUM_OF_ERRORS = 2;
const int CSML_EXC_NO_FILE = 0;
const int CSML_EXC_PARSER_ERROR = 1;


class CTTS_SMLException
{
private:
	int m_ExCode;
	string m_ErrorMsg [CSML_EXC_NUM_OF_ERRORS];

public:
	CTTS_SMLException (int ExCode)
		{
		m_ErrorMsg[0] = "No filename has been specified.";
		m_ErrorMsg[1] = "Parser error occurred.";

		m_ExCode = ExCode;
		};

	const char *GiveInfo ()
		{
		return m_ErrorMsg[m_ExCode].c_str();
		};
};

#endif