package metaface.dsp;
import java.util.*;
import java.io.*;
import java.applet.*;
import java.security.*;
/**
 *  This class handles the phoneme to viseme mapping for Mbrola:<br>
 *  <A HREF="http://www.festvox.org/mbrola">See the website</A> <br>
 *  It also synthesises speech audio from phoneme information using the Java
 *  Native Interface (JNI). The MbrPlay Dynamic Link Library (DLL) is used to
 *  achieve this synthesis.
 *
 *@author     Simon Beard 
 *@version    1.0
 */
public class MetaFaceMbrola extends DigitalSignalProcessor {

	/**
	 *  Native method for synthesising speech audio.
	 *
	 *@param  input   The phoneme information
	 *@param  output  The file to contain speech audio
	 *@return         The file containing the speech audio (WAV format)
	 */
	public native String synthesise(String input, String output);


	/**
	 *  Native method for initialising Mbrola.
	 *
	 *@param  database    The path to the Mbrola voice database use
	 *@param  rename      The Mbrola phonemes to rename (replace)
	 *@param  clone       The Mbrola phonemes to clone
	 *@param  pitchratio  The pitch ratio to use when synthesising
	 *@param  freq        The frequency of the voice
	 *@return             An error message or null
	 */
	public native String initialise(String database, String rename, String clone, float pitchratio, long freq);


	/**
	 *  Clone phonemes for english database number one
	 */
	private final static String m_EN1_CLONE_PHONEMES = "_ #";
	/**
	 *  Rename phonemes for US database number one
	 */
	private final static String f_US1_RENAME_PHONEMES = "i i:  A A:  O O:  u u:  r= 3:  E e  EI eI  AI aI";
	/**
	 *  Clone phonemes for US database number one
	 */
	private final static String f_US1_CLONE_PHONEMES = "_ #  @ Q  e E@  O U@  I I  I I@";
	/**
	 *  Gender definition
	 */
	public final static int MALE = 0;
	/**
	 *  Gender definition
	 */
	public final static int FEMALE = 1;

	/**
	 *  A list of MetaFaceMbrola phonemes for mapping
	 */
	private static String phonemes[] =
			{"p", "b", "t", "d", "k", "m", "n", "l", "r", "f", "v", "s", "z", "h", "w", "g", "tS", "dZ", "N", "T", "D", "S", "Z", "j", "i:",
			"A:", "O:", "u:", "3:", "I", "e", "{", "V", "Q", "U", "@", "eI", "aI", "OI", "@U", "aU", "I@", "E@", "U@"};

	/**
	 *  The viseme mapping of the above phonemes
	 */
	private static int visemes[] =
			{1, 1, 4, 4, 5, 1, 8, 8, 9, 2, 2, 7, 7, 12, 12, 5, 6, 6, 8, 3, 3, 6, 6, 12, 12, 10, 13, 14, 9, 12, 11, 10, 9, 13, 14, 10, 11, 10, 13, 13, 10, 12, 11, 13};

	/**
	 *  The number of MetaFaceMbrola phonemes supported
	 */
	private static int MAX_PHONEMES = 44;


	/**
	 *  Creates new MetaFaceMbrola handling class
	 */
	public MetaFaceMbrola() {
		super();
	}


	/**
	 *  Constructor for the MetaFaceMbrola object
	 *
	 *@param  path  Path to DLL and Mbrola voice databases
	 */
	public MetaFaceMbrola(String path) {
		new MetaFaceMbrola(path, MALE);
	}


	/**
	 *  Constructor for the MetaFaceMbrola object
	 *
	 *@param  path    Path to DLL and Mbrola voice databases
	 *@param  gender  Gender of voice database to be used
	 */
	public MetaFaceMbrola(String path, int gender) {
		super();

		//Local variables
		String database = null;
		String rename = null;
		String clone = null;
		float pitchratio = 1.0f;
		int i;
		String temp1 = null;
		String temp2 = null;
		File dir;
		dir = new File(path);

		//Setup database paths to be used
		path = dir.getAbsolutePath();
		if (gender == FEMALE) {
			database = path + "/mbrola/us1/us1";
			rename = f_US1_RENAME_PHONEMES;
			clone = f_US1_CLONE_PHONEMES;
			pitchratio = 1.5f;
		} else {
			database = path + "/mbrola/en1/en1";
			rename = " ";
			clone = m_EN1_CLONE_PHONEMES;
			pitchratio = 1.0f;
		}

		//Load the Mbrola DLL
		try {
			System.load(path + "/MbrolaDLL.dll");
		} catch (Error e) {
			System.err.println("MetaFaceMbrola support library could not be loaded");
			e.printStackTrace();
		}

		//Make the path string Windows compliant for native method
		i = 0;
		while (i < database.length()) {
			if (database.charAt(i) == '/') {
				if (i == 0) {
					database = database.substring(1, database.length());
				} else {
					temp1 = database.substring(0, i);
					temp2 = database.substring(i + 1, database.length());
					database = temp1 + "\\" + temp2;
				}
				i = 0;
			} else {
				i++;
			}
		}

		//Call JNI initialise database
		initialise(database, rename, clone, pitchratio, 16000);
	}


	/**
	 *  Description of the Method
	 *
	 *@param  input  Description of the Parameter
	 *@return        Description of the Return Value
	 */
	public String synthesise(String input) {
		String output = null;
		String returnval = null;
		String temp1 = null;
		String temp2 = null;
		int i;
		int j;

		//Get temp file for WAV audio
		try {
			output = File.createTempFile("MFFW_speech", ".wav").toString();
		} catch (IOException e) {
			System.err.println("can't create temp wav file");
			e.printStackTrace();
		}

		//Reformat path string for windows
		i = 0;
		while (i < output.length()) {
			if (output.charAt(i) == '/') {
				if (i == 0) {
					output = output.substring(1, output.length());
				} else {
					temp1 = output.substring(0, i);
					temp2 = output.substring(i + 1, output.length());
					output = temp1 + "\\" + temp2;
				}
				i = 0;
			} else {
				i++;
			}
		}

		//Call JNI synthesise
		returnval = synthesise(input, output);

		output = null;
		return (returnval);
	}


	/**
	 *  Returns a viseme based on the phoneme string
	 *
	 *@param  ph  The phoneme string
	 *@return     The viseme equivalent
	 */
	public int getViseme(String ph) {
		int i;

		//Simple table look up for viseme based on phoneme
		for (i = 0; i < MAX_PHONEMES; i++) {
			if (phonemes[i].compareTo(ph) == 0) {
				return (visemes[i]);
			}
		}

		return 0;
	}


	/**
	 *  Returns a vector of visemes based on the phoneme information
	 *
	 *@param  phonemeinfo  The MetaFaceMbrola format phonemes and durations
	 *@return              A vector of visemes and durations
	 */
	public Vector phonemesToVisemes(String phonemeinfo) {

		//Local variables
		int i;
		int ph_start_ptr = 0;
		int du_start_ptr = 0;
		int ph_end_ptr = 0;
		int du_end_ptr = 0;
		String phoneme;
		int duration;
		int viseme;
		Vector tempvector;

		tempvector = new Vector();

		i = 0;

		//Segment input into lines of phonemes, get phoneme and duration
		while (i < phonemeinfo.length()) {
			ph_start_ptr = i;
			if (i < phonemeinfo.length()) {
				while ((phonemeinfo.charAt(i) != ' ') && (i < phonemeinfo.length())) {
					i++;
				}
			}
			ph_end_ptr = i;
			if (i < phonemeinfo.length()) {
				while ((phonemeinfo.charAt(i) == ' ') && (i < phonemeinfo.length())) {
					i++;
				}
			}

			du_start_ptr = i;
			if (i < phonemeinfo.length()) {
				while (((phonemeinfo.charAt(i) != ' ') && (phonemeinfo.charAt(i) != '\n')) &&
						(i < phonemeinfo.length())) {
					i++;
				}
			}
			du_end_ptr = i;

			if (i < phonemeinfo.length()) {
				while ((phonemeinfo.charAt(i) != '\n') && (i < phonemeinfo.length())) {
					i++;
				}
			}
			i++;

			//Store phoneme and duration
			phoneme = phonemeinfo.substring(ph_start_ptr, ph_end_ptr);
			duration = Integer.valueOf(phonemeinfo.substring(du_start_ptr, du_end_ptr)).intValue();

			//Look up viseme equivalent
			viseme = getViseme(phoneme);

			//Place visemes and durations into vector
			tempvector.add(new Integer(viseme));
			tempvector.add(new Integer(duration));
		}
		return tempvector;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  milliseconds  Description of the Parameter
	 *@return               Description of the Return Value
	 */
	public String silencePhonemeString(int milliseconds) {
		//String for silence
		return ("_ " + milliseconds);
	}
}

