////////////////////////////////////////////////////////////////////////////////
//
// CTTS_StringVector class HEADER file.
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __TTS_STRING_VECTOR_H__
#define __TTS_STRING_VECTOR_H__

#include <iostream.h>
#include <vector>
#include <string>
using std::string;

class CTTS_StringVector : public std::vector <char *>
{
public:
	void InsertOrder (const char *Str);
	bool Search (const char *Str);
	void Print ();
};

#endif