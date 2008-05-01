////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Volume Class IMPLEMENTATION file
//
////////////////////////////////////////////////////////////////////////////////

#include "TTS_Volume.h"

////////////////////////////////////////////////////////////////////////////////
// ProcessTag
//
////////////////////////////////////////////////////////////////////////////////
void CTTS_Volume::ProcessTag (CTTS_SMLNode *pNode)
{
	assert (pNode != NULL);

	char *Volume = GetAttribute (pNode, "level");

	if (Volume != NULL)
		{
		string *pTemp = new string (Volume);
		SetVolume (pTemp);
		delete pTemp;
		delete Volume;
		}
}

////////////////////////////////////////////////////////////////
// SetVolume
//
// Sets member variable m_VolumeRatio to level specified by
// input parameter pVolume.  pVolume can be expressed as a 
// preset description, or a percentage string:
//			"low", "normal", "high", ('+'|'-') (digit)+ '%'
////////////////////////////////////////////////////////////////
void CTTS_Volume::SetVolume (string *pVolume)
{
	if ((*pVolume) == "soft")
		{
		m_VolumeRatio = "0.3";
		}
	else if ((*pVolume) == "normal")
		{
		m_VolumeRatio = "0.5";
		}
	else if ((*pVolume) == "loud")
		{
		m_VolumeRatio = "1.5";
		}

	// level has been given as percentage
	else if (ValidPercentage (pVolume->c_str()))
		{
		(*pVolume) [pVolume->size()-1] = '\0';
		double VolumeLevel = (double) StrToInt (pVolume->c_str()) / 100;
		VolumeLevel += 1.0;
		if (VolumeLevel <= 0)
			VolumeLevel = 0.1;
		char Str[20];
//		sprintf (Str, "%lf", VolumeLevel);
		m_VolumeRatio = Str;
		}
}