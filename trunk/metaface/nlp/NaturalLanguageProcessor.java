package metaface.nlp;

import metaface.dsp.*;
/**
 *  This class defines the structure by which all inheriting Natural Language
 *  Processors (NLPs) must adhere. An NLP takes text and produces speech phoneme
 *  information
 *
 *@author     Simon Beard 
 *@version    1.0
 */
public abstract class NaturalLanguageProcessor {

	/**
	 *  Constructor for the NaturalLanguageProcessor object
	 */
	public NaturalLanguageProcessor() { }


	/**
	 *  Uses the NLP to get the next section of phoneme information from a file
	 *
	 *@param  filename  The file
	 *@return           The phoneme information string
	 */
	public abstract String getNextPhonemes(String filename);
}

