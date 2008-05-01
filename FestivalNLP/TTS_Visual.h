////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Visual CLASS Header file
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_VISUAL_H__
#define __CTTS_VISUAL_H__

#include <stdio.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <malloc.h>
#include <string.h>

#ifdef FAITH
#include "../GST/filter/GST_API.h"
#include "speak.h"
#endif

class CTTS_Visual
{
private:
	int GetWaveform (const char *Filename, unsigned char **Data);

public:
	CTTS_Visual () {};
	~CTTS_Visual () {};

	void SendVisualData (const char *Filename, const char *MBROLA_Utterance, const char *UtteranceText, const char *GST_Filename, const char *EmbedFilename);
	void ConvertUtteranceToFaps (char *utterance, struct FAPSTRUCT *faps, int *fapnum, float *totaltime);
};

#endif