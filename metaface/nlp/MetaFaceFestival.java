package metaface.nlp;

import java.io.*;
import java.util.*;
import metaface.mpeg4.*;
import metaface.dsp.*;
/**
 *  This class is a wrapper for MetaFaceFestival and the SPEMO dll. It gets
 *  around the thread problems by controlling instanciation and access
 *
 *@author     Simon Beard 
 *@version    1.0
 */
public class MetaFaceFestival extends NaturalLanguageProcessor {

	/**
	 *  The JNI class for calling the SPEMO dll
	 */
	public native void initialise();


	/**
	 *  Native method for opening an SML file with the SPEMO library
	 *
	 *@param  filename  The SML file to open
	 *@return           Error return code
	 */
	public native int openSMLFile(String filename);


	/**
	 *  Native method for closing an SML file with the SPEMO library
	 */
	public native void closeSMLFile();


	/**
	 *  Native method for getting the next section of phoneme information with the
	 *  SPEMO library
	 *
	 *@return    A section of phoneme information
	 */
	public native String getNextPhonemeInfo();


	/**
	 *  Whether an SML file is open
	 */
	private boolean fileopen;


	/**
	 *  Constructor for the MetaFaceFestival object
	 *
	 *@param  path  The path to the MetaFace Festival JNI DLL
	 */
	public MetaFaceFestival(String path) {
		super();
		File dir;

		fileopen = false;
		dir = new File(path);
		path = dir.getAbsolutePath();
		try {
			System.load(path + "/FestivalDLL.dll");
		} catch (Exception e) {
			System.err.println("MetaFaceFestival support library could not be loaded");
			e.printStackTrace();
		}
		initialise();
	}


	/**
	 *  Uses the NLP to get the next section of phoneme information from an SML
	 *  file
	 *
	 *@param  filename  The SML file to open
	 *@return           The phoneme information string
	 */
	public String getNextPhonemes(String filename) {
		String section;

		if (!fileopen) {
			openSMLFile(filename);
		}
		section = getNextPhonemeInfo();
		if (section == null) {
			closeSMLFile();
		} else if (section.length() == 0) {
			closeSMLFile();
			return (null);
		}
		section = section.replace('#', '_');
		return (section);
	}
}
