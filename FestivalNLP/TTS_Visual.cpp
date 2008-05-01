////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Visual CLASS Implementation file
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_Visual.h"

#ifdef FAITH
#ifdef __cplusplus
extern "C"
	{
#endif
	#define SRV_AUDIO	1
	#define SRV_FAP		2
	#define SRV_TEXT	3
	#define SRV_APPDATA	4
	#define SRV_CMDIN	5
	#define SRV_CMDOUT	6
	
	extern void convert_utterance_to_faps (char *, FAPSTRUCT *, int *, float *);
	extern void add_faps_to_stream (FAPSTRUCT *, int, int);
	extern unsigned long getChannelHandle(int );
	extern int MPEG4_Send_Data(unsigned long , char *, int);

#ifdef __cplusplus
	}
#endif
#endif

/////////////////////////////////////////////////////////////////////
// SendVisualData
//
// This function mimicks old module's synthesize_speech () function
/////////////////////////////////////////////////////////////////////
void CTTS_Visual::SendVisualData (const char *WaveFilename, const char *MBROLA_Utterance, const char *UtteranceText, const char *GST_Filename, const char *EmbedFilename)
{
#ifdef FAITH
	unsigned char *Waveform;
	struct FAPSTRUCT faps[MAX_PHONEME_NUM];
	int FapNum;
	float TotalTime;

	ConvertUtteranceToFaps ((char *)MBROLA_Utterance, faps, &FapNum, &TotalTime);

	int TempSize = GetWaveform (WaveFilename, &Waveform);		// this is simulating part of MBROLA_DSP ()

	fprintf (stdout, "\tSending Waveform of size %d...\n", TempSize);
	MPEG4_Send_Data (getChannelHandle (SRV_AUDIO), (char *)Waveform, TempSize);
	fprintf (stdout,"\tSending utterance text...\n");
	MPEG4_Send_Data (getChannelHandle (SRV_TEXT), (char *)UtteranceText, strlen (UtteranceText)+1);

	fprintf (stdout, "\tadding faps to stream...\n");
	GST_add_faps_to_stream (faps, FapNum, 0, GST_Filename, EmbedFilename);

	free (Waveform);
#endif
}

//////////////////////////////////////////////////////////////////////
// GetWaveform
//
// This function simulates Speak's MBROLA_DSP in that it allocates
// a storage variable to hold the waveform generated by 
// CTTS_DigitalSignalProcessor. (The WaveFilename parameter of this 
// function points to the audio file that CTTS_DigitalSignalProcessor
// created.)
//////////////////////////////////////////////////////////////////////
int CTTS_Visual::GetWaveform (const char *WaveFilename, unsigned char **Data)
{
	int AmountStored = 0;
#ifdef FAITH
	struct stat FileBuf;
	int Length = 0;
	unsigned char *Array;
	FILE *fp;

	stat (WaveFilename, &FileBuf);
	Length = FileBuf.st_size;
	AmountStored = Length;
	*Data = (unsigned char *) malloc (AmountStored);

	Array = *Data;
	fp = fopen (WaveFilename, "rb");
	if (fp == NULL)
		{
		fprintf (stderr, "Error in CTTS_Visual::GetWaveform(): Could not open MBROLA output file. Call a FAITH healer.\n");
		exit (0);
		}

	for (int count=0; count < AmountStored; count++)
		{
		Array[count] = getc (fp);
		}

	fclose (fp);
#endif

	return AmountStored;
}

//////////////////////////////////////////////////////////////////////
// ConvertUtteranceToFaps
//
// This is a function that has been copied straight from the Speak
// module.  It has been modified slightly for readability and efficiency.
//////////////////////////////////////////////////////////////////////
void CTTS_Visual::ConvertUtteranceToFaps (char *Utterance, struct FAPSTRUCT *Faps, int *FapNum, float *TotalTime)
{
#ifdef FAITH
	int Count,FapNo,Fap,Duration,Phonemes,LineNo,Length;
	float Time = 0.0;
	char Phoneme[3],Line[MAX_PHONEME_DESCRIPTION];
	char *ptr = Utterance;
	struct 
		{
		int FapNo;
		int Duration;
		} MappedPhonemes[MAX_PHONEME_NUM];

	struct 
		{
		char Phoneme[3];
		int Fap;
		} Phoneme2Viseme[MAXPHONEMES] = 
			{
			{"p",1},{"b",1},{"t",4},{"d",4},{"k",5},{"m",1},{"n",8},{"l",8},{"r",9},{"f",2},
			{"v",2},{"s",7},{"z",7},{"h",12},{"w",10},{"g",5},{"tS",6},{"dZ",6},{"N",12},
			{"T",3},{"D",3},{"S",6},{"Z",6},{"j",10},{"i:",0},{"A:",10},{"O:",10},{"u:",10},
			{"3:",14},{"I",12},{"e",11},{"{",11},{"V",11},{"Q",13},{"U",14},{"@",10},{"eI",11},
			{"aI",11},{"OI",10},{"@U",10},{"aU",11},{"I@",0},{"e@",0},{"U@",0}			
			};

	Phonemes = 0;
	*TotalTime = 0.0;
	while((Length = strcspn(ptr,"\n")) != 0)
		{
		memset (Line, ' ', MAX_PHONEME_DESCRIPTION);
		strncpy (Line, ptr, Length);
		ptr += Length + 1;
		sscanf (Line, "%s %d\n", Phoneme, &Duration);
		FapNo = 0;
		for(Fap = 0; Fap < MAXPHONEMES; Fap++)
			{
			if (strcmp (Phoneme, Phoneme2Viseme[Fap]. Phoneme) == 0)
		  		{
		  		FapNo=Phoneme2Viseme[Fap].Fap;
				break;
			  	}
			}

		MappedPhonemes[Phonemes].FapNo = FapNo;
		MappedPhonemes[Phonemes].Duration = Duration;
		*TotalTime += (float)(Duration/1000.0);
		Phonemes++;
		}

	MappedPhonemes[Phonemes].FapNo = 0;

	LineNo = 0;
	Time = 0.0;
	for(Count = 0; Count < Phonemes; Count++)
		{
		Time += (float) MappedPhonemes[Count].Duration;
		while(Time > 0.0)
			{
			Faps[LineNo].fapno1 = MappedPhonemes[Count].FapNo;
			Faps[LineNo].fapno2 = MappedPhonemes[Count+1].FapNo;
			Faps[LineNo].blend = (int) (63*(Time/MappedPhonemes[Count].Duration));
			Time -= (1000.0 / (float) FRAMESASEC);
			LineNo++;
			}
		}

	*TotalTime -= (float) (Time / 1000.0);
	*FapNum = LineNo;
#endif
}