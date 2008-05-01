////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Embed Class IMPLEMENTATION file
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_Embed.h"

////////////////////////////////////////////////////////////////////////////////
// ProcessTag
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Embed::ProcessTag (CTTS_SMLNode *pNode, CTTS_NaturalLanguageParser *pNLP, CTTS_DigitalSignalProcessor *pDSP)
{
	assert ((pNode->IsNode ("embed")) && (pNode != NULL));
	assert ((pNLP != NULL) && (pDSP != NULL));

	char *EmbedType = GetAttribute (pNode, "type");
	if (EmbedType != NULL)
		{
		string Str = string (EmbedType);
		if (Str == "mml")
			{
			ProcessMMLDocument (pNode, pNLP);
			}
		else if (Str == "audio")
			{
			ProcessAudioFile (pNode, pDSP);
			}

		delete EmbedType;
		}
}

////////////////////////////////////////////////////////////////////////////////
// ProcessMMLDocument
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Embed::ProcessMMLDocument (CTTS_SMLNode *pNode, CTTS_NaturalLanguageParser *pNLP)
{
/*	assert ((pNode->IsNode ("embed")) && (pNode != NULL));

	char *MusicFilename = GetAttribute (pNode, "music_file");
	if (MusicFilename != NULL)
		{
		char *LyricsFilename = GetAttribute (pNode, "lyr_file");
		if (LyricsFilename != NULL)
			{
			CMML_Singer Singer;
			Singer.Sing (pNLP, MusicFilename, LyricsFilename);

			delete LyricsFilename;
			}
		delete MusicFilename;
		}*/
}

////////////////////////////////////////////////////////////////////////////////
// ProcessAudioFile
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Embed::ProcessAudioFile (CTTS_SMLNode *pNode, CTTS_DigitalSignalProcessor *pDSP)
{
	assert ((pNode->IsNode ("embed")) && (pNode != NULL));

	char *WaveFilename = GetAttribute (pNode, "src");
	if (WaveFilename != NULL)
		{
		pDSP->PlayWave (WaveFilename);
		delete WaveFilename;
		}
}