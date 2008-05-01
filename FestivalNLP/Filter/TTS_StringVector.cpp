////////////////////////////////////////////////////////////////////////////////
//
// CTTS_StringVector CLASS IMPLEMENTATION file.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_StringVector.h"

////////////////////////////////////////////////////////////////////////////////
// InsertOrder
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_StringVector::InsertOrder (const char *Str)
{
	if (size() == 0)
		{
		push_back ((char *)Str);
		}
	else
		{
		CTTS_StringVector::iterator i = begin();
	
		bool Found = false;
		while ((!Found) && (i < end()))
			{
			string Item = *i;
			if (Str < Item)
				Found = true;
			else
				i++;
			}
	
		insert (i, (char *) Str);
		}
}

////////////////////////////////////////////////////////////
// Search
//
// Looks up table for occurence of string Str.  Returns true
// if found, otherwise returns false.  TEMPORARY algorithm:
// Since vector is ordered, should implement binary search.
////////////////////////////////////////////////////////////
bool CTTS_StringVector::Search (const char *Str)
{
	CTTS_StringVector::iterator i;

	bool Found = false;

	for (i = begin(); i != end(); i++)
		{
		string Item = *i;
		if (Item == Str)
			{
			Found = true;
			break;
			}
		}

	if (Found)
		{
		//cout << "FOUND " << Str << " in string vector\n";
		return true;
		}
	else
		{
		//cout << "Could NOT find " << Str << " in string vector\n";
		return false;
		}
}

////////////////////////////////////////////////////////////////////////////////
// Print
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_StringVector::Print ()
{
	int c = 0;
	CTTS_StringVector::iterator i;

	cout << "***********************************\n";
	for (i = begin(); i != end(); i++)
		{
		cout << "Item [" << c << "]: " << *i << endl;
		c++;
		}
}