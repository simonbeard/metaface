////////////////////////////////////////////////////////////////////////////////
//
// MAIN function
//
////////////////////////////////////////////////////////////////////////////////
 
#include "TTS_C_API.h"
#include <stdio.h>

void main ()
{
	TTS_Initialise ();
	//TTS_SpeakFromFile ("data/PartA.sml");

	//TTS_SpeakFromFile ("data/th-example3-plain.sml");
	//TTS_SpeakFromFile ("data/th-example3.sml");

	//TTS_SpeakFromFile ("input/sing.sml");
	
	//TTS_SpeakFromFile ("input/speak-english.sml");
	//TTS_SpeakFromFile ("input/tweedledee.sml");
	
	//TTS_SpeakFromFile ("input/allstories.sml");

	TTS_SpeakFromFile ("D:/simon/SpemoSupport/input/test.sml");
	TTS_Destroy ();
}
