////////////////////////////////////////////////////////////////////////////////
//
// CTTS_Volume Class HEADER file
//
////////////////////////////////////////////////////////////////////////////////

#ifndef __CTTS_VOLUME_H__
#define __CTTS_VOLUME_H__

#include "TTS_SMLSimpleTag.h"

class CTTS_Volume : public CTTS_SMLSimpleTag
{
private:
	string m_VolumeRatio;

public:
	CTTS_Volume () 
		{
		m_VolumeRatio = "1.0";
		};

	virtual void ProcessTag (CTTS_SMLNode *pNode);
	void SetVolume (string *Volume);

	inline const char *GetVolumeRatio ()
		{
		return m_VolumeRatio.c_str();
		};
};

#endif