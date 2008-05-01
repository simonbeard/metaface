/*
 * FPMs-TCTS SOFTWARE LIBRARY
 *
 * File    : play
 * Purpose : example file 4
 * Author  : Alain Ruelle
 * Email   : mbrola-tools@tcts.fpms.ac.be
 *
 * play a pho file or a phs file into a raw file
 *
 * Copyright (c) 1998 Faculte Polytechnique de Mons (TCTS lab)
 */
#include <stdio.h>
#include <windows.h>
#include "mbrplay.h"

char *initialise (char *database, char *rename, char *clone, float pitchratio, long freq)
{
	if (MBR_SetDatabaseEx(database,rename,clone)<0)
	{
		char msg[200];
		MBR_LastError(msg,200);
		return ("mbrola error: can't init database");				
	}	
	MBR_SetPitchRatio (pitchratio);	
	MBR_SetVoiceFreq (freq);
	MBR_SetVolumeRatio (1.0);
	MBR_SetNoError (1);

	return (NULL);
}

char *synthesise (char *input, char *output)
{
	MBR_Play(input,MBROUT_WAVE|MBR_WAIT,output,NULL);
	if (MBR_LastError(NULL,0)<0)
	{
		char msg[200];
		MBR_LastError(msg,200);
		return ("mbrola error: during synth");		
	}	
	return (NULL);
}

char *finalise ()
{
	MBR_UnregisterAll();
	MBR_MBRUnload();
	return (NULL);
}