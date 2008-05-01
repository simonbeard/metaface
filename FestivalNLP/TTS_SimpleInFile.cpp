////////////////////////////////////////////////////////////////////////////////
//
// CTTS_SimpleInFile CLASS Implementation file.
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_SimpleInFile.h"

////////////////////////////////////////////////////////////////////////////////
// Open
//
////////////////////////////////////////////////////////////////////////////////
int CTTS_SimpleInFile::Open ()
{
	if (m_Filename.length() == 0)
		{
		fprintf (stderr, "File error in CTTS_SimpleInFile::Open(): no filename specified\n");
		return 0;
		}

	m_FileHandle = fopen (m_Filename.c_str(), "r");

	if (m_FileHandle == NULL)
		{
		fprintf (stderr, "File error in CTTS_SimpleInFile::Open(): could not open %s for reading\n", m_Filename.c_str());
		return 0;
		}

	return 1;
}

////////////////////////////////////////////////////////////////////////////////
// GetLine
//
////////////////////////////////////////////////////////////////////////////////
char *CTTS_SimpleInFile::GetLine (int MaxLength, char Delimiter)
{
	char *TempLine = new char [MaxLength];
	int c;

	// Grab char per char.  Not efficient, but works.
	// Replace with a better algorithm at a later stage
	// e.g. fgets()?
	int i = 0;
	while (((c = getc (m_FileHandle)) != Delimiter))
		{
		assert (i < MaxLength);

		// bail out if hit EOF or TempLine buffer full
		if ((c == EOF) || (i >= MaxLength))
			break;
		
		// watch out for DOS carriage return
		else if (c != '\r')
			TempLine[i] = c;
		i++;
		}

	// null terminate the string
	if (i >= MaxLength)
		TempLine[MaxLength-1] = '\0';
	else
		TempLine[i] = '\0';

	char *Line = strdup(TempLine);

	delete [] TempLine;


	if ((i == 0) && (c == EOF))
		return (NULL);
	else
		return (Line);
}