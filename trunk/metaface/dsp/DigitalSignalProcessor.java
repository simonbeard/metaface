package metaface.dsp;
import java.util.*;
import java.io.*;
import java.applet.*;
/**
 *  This class defines the structure by which all inheriting Digital Signal
 *  Processors (DSPs) must adhere. A DSP takes textual phoneme information and
 *  synthesises speech audio
 *
 *@author     Simon Beard 
 *@version    1.0
 */
public abstract class DigitalSignalProcessor {

	/**
	 *  Constructor for the DSP
	 */
	public DigitalSignalProcessor() { }


	/**
	 *  Sythesise phoneme information into a speech audio file
	 *
	 *@param  input  A string of phoneme information
	 *@return        A speech audio file name string
	 */
	public abstract String synthesise(String input);


	/**
	 *  <p>
	 *
	 *  Get the visual equivalent of a phoneme</p> 
	 * <IMG SRC="../../resources/visemes.jpg"/>
	 *
	 *@param  ph  The phoneme to look up
	 *@return     An MPEG-4 numbered viseme representing the phoneme
	 */
	public abstract int getViseme(String ph);


	/**
	 *  <p>
	 *
	 *  Get the visual equivalents of phonemes</p>
	 *
	 *@param  phonemeinfo  The phoneme information (phonemes and duration)
	 *@return              The MPEG-4 numbered visemes representing the phoneme
	 *      information and durations (milliseconds):<br>
	 *      [viseme1][duration1][viseme2][duration2][viseme3] ...
	 */
	public abstract Vector phonemesToVisemes(String phonemeinfo);


	/**
	 *  Get a phoneme string representing a silent duration
	 *
	 *@param  milliseconds  The duration of the silence
	 *@return               A phoneme string representing the silence
	 */
	public abstract String silencePhonemeString(int milliseconds);
}

