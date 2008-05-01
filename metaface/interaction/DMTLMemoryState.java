package metaface.interaction;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;
import java.io.*;
/**
 *  DMTLMemoryState is a collection of state variables used in the
 *  DMTLMemoryParser
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        DMTLParser
 *@see        DMTLTagReader
 */
class DMTLMemoryState {
	/**
	 *  Currently not inside a state element
	 */
	public static int NOSTATE = 0;
	/**
	 *  Currently inside a dialogue element
	 */
	public static int DIALOGUE = 1;
	/**
	 *  Currently inside a macros element
	 */
	public static int MACROS = 2;
	/**
	 *  Currently inside a macro element
	 */
	public static int MACRO = 3;
	/**
	 *  Currently inside a defaulttopic element
	 */
	public static int DEFAULTTOPIC = 4;
	/**
	 *  Currently inside a topic element
	 */
	public static int TOPIC = 5;
	/**
	 *  Currently inside a subtopic element
	 */
	public static int SUBTOPIC = 6;
	/**
	 *  Currently inside a state element
	 */
	public static int STATE = 7;
	/**
	 *  Currently inside a stimulus element
	 */
	public static int STIMULUS = 8;
	/**
	 *  Currently inside a response element
	 */
	public static int RESPONSE = 9;
	/**
	 *  Currently inside a signal element
	 */
	public static int SIGNAL = 10;
	/**
	 *  Currently inside a prestate element
	 */
	public static int PRESTATE = 11;
	/**
	 *  Currently inside a nextstate element
	 */
	public static int NEXTSTATE = 12;
	/**
	 *  Currently inside an evaluate element
	 */
	public static int EVALUATE = 13;
	/**
	 *  The current element
	 */
	private int currentState = 0;
	/**
	 *  Is there stmimulus in this element
	 */
	private boolean stimulus = true;
	/**
	 *  Is this the first response of a state
	 */
	private boolean firstresponse = true;
	/**
	 *  Are macros being processed
	 */
	private boolean macro = false;
	/**
	 *  Is the defaulttopic bein processed
	 */
	private boolean defaulttopicflag = false;
	/**
	 *  Name of the current macro
	 */
	private String macroName = null;
	/**
	 *  The type of the current stimulus
	 */
	private int stimulustype = InteractionManager.TEXT;
	/**
	 *  The type of the current resopnse
	 */
	private int responsetype = Response.TEXT;
	/**
	 *  The current response reference string
	 */
	private String responsereference = null;


	/**
	 *  Create a new DMTL state object to aid in SAX parsing
	 */
	public DMTLMemoryState() {
		currentState = 0;
		stimulus = true;
		firstresponse = true;
		macro = false;
		macroName = null;
		stimulustype = InteractionManager.TEXT;
		responsetype = Response.TEXT;
		responsereference = null;
		defaulttopicflag = false;
	}


	/**
	 *  Set state to one of the definitions NOSTATE, DIALOG etc.
	 *
	 *@param  i  The current state: NOSTATE, DIALOG, MACROS etc.
	 */
	public void setState(int i) {
		currentState = i;
	}


	/**
	 *  Gets the current state NOSTATE, DIALOG etc.
	 *
	 *@return    The current state: NOSTATE, DIALOG, MACROS etc.
	 */
	public int getState() {
		return currentState;
	}


	/**
	 *  Set flag to read character data as stimulus
	 *
	 *@param  flag  Set true or false
	 */
	public void setIfReadStimulus(boolean flag) {
		stimulus = flag;
	}


	/**
	 *  Get flag to read character data as stimulus
	 *
	 *@return    true or false
	 */
	public boolean getIfReadStimulus() {
		return stimulus;
	}


	/**
	 *  See if this is first response (of multiple responses for a state)
	 *
	 *@return    true or false
	 */
	public boolean getIfFirstResponse() {
		return firstresponse;
	}


	/**
	 *  Set if this is first response (of multiple responses for a state)
	 *
	 *@param  flag  Set true or false
	 */
	public void setIfFirstResponse(boolean flag) {
		firstresponse = flag;
	}


	/**
	 *  Set if to read character data as a macro
	 *
	 *@param  flag  Set true or false
	 */
	public void setIfMacro(boolean flag) {
		macro = flag;
	}


	/**
	 *  Get if to read character data as a macro
	 *
	 *@return    true or false
	 */
	public boolean getIfMacro() {
		return (macro);
	}


	/**
	 *  Store name of macro
	 *
	 *@param  name  Name of the macro
	 */
	public void setMacroName(String name) {
		macroName = name.toUpperCase();
	}


	/**
	 *  Sets the stimulusType attribute of the DMTLMemoryState object
	 *
	 *@param  type  The new stimulusType value
	 */
	public void setStimulusType(int type) {
		stimulustype = type;
	}


	/**
	 *  Gets the stimulusType attribute of the DMTLMemoryState object
	 *
	 *@return    The stimulusType value
	 */
	public int getStimulusType() {
		return (stimulustype);
	}


	/**
	 *  Sets the responseType attribute of the DMTLMemoryState object
	 *
	 *@param  type  The new responseType value
	 */
	public void setResponseType(int type) {
		responsetype = type;
	}


	/**
	 *  Gets the responseType attribute of the DMTLMemoryState object
	 *
	 *@return    The responseType value
	 */
	public int getResponseType() {
		return (responsetype);
	}


	/**
	 *  Sets the responseReference attribute of the DMTLMemoryState object
	 *
	 *@param  ref  The new responseReference value
	 */
	public void setResponseReference(String ref) {
		if (ref == null) {
			responsereference = null;
		} else {
			responsereference = new String(ref);
		}
	}


	/**
	 *  Gets the ifDefaultTopic attribute of the DMTLMemoryState object
	 *
	 *@return    The ifDefaultTopic value
	 */
	public boolean getIfDefaultTopic() {
		return (defaulttopicflag);
	}


	/**
	 *  Sets the ifDefaultTopic attribute of the DMTLMemoryState object
	 *
	 *@param  flag  The new ifDefaultTopic value
	 */
	public void setIfDefaultTopic(boolean flag) {
		defaulttopicflag = flag;
	}


	/**
	 *  Gets the responseReference attribute of the DMTLMemoryState object
	 *
	 *@return    The responseReference value
	 */
	public String getResponseReference() {
		return (responsereference);
	}


	/**
	 *  Gets name of current macro
	 *
	 *@return    Name of the macro
	 */
	public String getMacroName() {
		return macroName;
	}
}

