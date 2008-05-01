////////////////////////////////////////////////////////////////////////////////
//
// CTTS_SMLSimpleTag class IMPLEMENTATION File.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_SMLSimpleTag.h"

////////////////////////////////////////////////////////////////////////////////
// ProcessTag
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_SMLSimpleTag::ProcessTag (CTTS_SMLNode *pNode)
{
}

////////////////////////////////////////////////////////////////////////////////
// GetAttribute
//
////////////////////////////////////////////////////////////////////////////////
char *CTTS_SMLSimpleTag::GetAttribute (CTTS_SMLNode *pNode, const char *AttributeName)
{
	assert ((pNode != NULL) && (AttributeName != NULL));

	CTTS_SMLNode *pAttributeNode = pNode->Attributes();
	CTTS_SMLNode *pValueNode;
	
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


///////////////////////////////////////////////////////////////////////////////
// ValidPercentage
// 
// Checks if input string Str follows the following format:
//		(whitespace)* ('+'|'-') (Digit)+ ('%')
//
// Returns boolean true if it is, false if it isn't.
///////////////////////////////////////////////////////////////////////////////
bool CTTS_SMLSimpleTag::ValidPercentage (const char *Str)
{
	assert (Str != NULL);

	int Length = strlen (Str);

	// Make 's' point to first non-whitespace character
	int s = 0;
	while ((isspace (Str[s])) && (s < Length))
		{
		s++;
		}

	// Must be at least 3 or more characters long (excluding whitespace)
	if ((Length - s) <= 2)
		return false;

	// Must have '+' or '-' character at beginning
	if ((Str[s] != '+') && (Str[s] != '-'))
		return false;

	// Each character between sign and % must be a digit
	s++;
	while (s < Length-1)
		{
		if ((Str[s] >= '0') && (Str[s] <= '9'))
			{
			s++;
			}
		else
			{
			return false;
			}
		}

	// Passed all tests, therefore it's a valid percentage number
	return true;
}

///////////////////////////////////////////////////////////////////////////////
// StrToInt
//
// Converts an ascii string to an integer value.
// Assumes Str has format: (whitespace)* {'+'|'-"} (digit)*
///////////////////////////////////////////////////////////////////////////////
int CTTS_SMLSimpleTag::StrToInt (const char *Str)
{
	assert (Str != NULL);

	int Num=0;
	bool Pos;

	int s = 0;
	while (isspace (Str[s]))
		s++;

	if (Str[s] == '-')
		{
		Pos = false;
		s++;
		}
	else if (Str[s] == '+')
		{
		Pos = true;
		s++;
		}

	do
		{
		Num = (Num * 10) + (Str[s] - '0');
		s++;
		} while (Str[s] != '\0');

	if (Pos)
		return Num;
	else
		return -Num;
}
