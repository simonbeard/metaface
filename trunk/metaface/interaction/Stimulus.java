package metaface.interaction;

import java.util.*;
import java.io.*;
/**
 *  This state reoresents a DMTL stimulus element for matching to a user's
 *  stimulus.
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        InteractionManager
 */
public class Stimulus {
	/**
	 *  The DMTL stimulus string
	 */
	private String stimulus;
	/**
	 *  The stimulus type (e.g. InteractionManager.TEXT)
	 */
	private int type;


	/**
	 *  Constructor for the Stimulus object
	 *
	 *@param  stimulusstring  The DMTL PCDATA stimulus string
	 *@param  stimulustype    The stimulus type (e.g. InteractionManager.TEXT)
	 */
	public Stimulus(String stimulusstring, int stimulustype) {
		stimulus = stimulusstring;
		type = stimulustype;
	}


	/**
	 *  Gets the DMLT stimulus attribute of the Stimulus object
	 *
	 *@return    The DMTL stimulus string
	 */
	public String getStimulus() {
		return (stimulus);
	}


	/**
	 *  Gets the type attribute of the Stimulus object
	 *
	 *@return    The stimulus type (e.g. InteractionManager.TEXT)
	 */
	public int getType() {
		return (type);
	}


	/**
	 *  Any buiding instructions that must be completed after the constructor is
	 *  called
	 */
	public void build() {
	}


	/**
	 *  Resets any internal state variables
	 */
	public void reset() {
	}
}
