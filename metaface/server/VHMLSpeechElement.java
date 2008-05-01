package metaface.server;
import java.util.*;
import java.io.*;

/**
 *  This class stores a VHML Speech Element for recall when rendering speech
 *  audio
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        VHMLParser
 *@see        VHMLReader
 */
class VHMLSpeechElement {
	/**
	 *  The ID of the speech element
	 */
	int id;
	/**
	 *  The duration of the speech element
	 */
	int duration;
	/**
	 *  The number of attributes of the speech element
	 */
	int elements;
	/**
	 *  The attributes of the speech element
	 */
	String[] attrs;
	/**
	 *  The values of the speech element
	 */
	String[] values;
	/**
	 *  Whether the speech element is expressive
	 */
	boolean expression;
	/**
	 *  Whether the closing tag has been encountered
	 */
	boolean closedtag;


	/**
	 *  Constructor for the VHMLSpeechElement object
	 *
	 *@param  t_id        The VHMLReader ID of the VHML element
	 *@param  t_duration  The duration attribute of the VHML element
	 *@param  num_attr    Description of the Parameter
	 */
	public VHMLSpeechElement(int t_id, int t_duration, int num_attr) {
		id = t_id;
		duration = t_duration;
		expression = false;
		elements = num_attr;
		attrs = new String[num_attr];
		values = new String[num_attr];
		closedtag = false;
	}


	/**
	 *  Constructor for the VHMLSpeechElement object
	 *
	 *@param  t_id          The VHMLReader ID of the VHML elements
	 *@param  t_duration    The duration attribute of the VHML elements
	 *@param  isexpression  Whether the speech element is expressive (rather than
	 *      prosodic)
	 *@param  num_elements  The number of speech elements
	 */
	public VHMLSpeechElement(int t_id, int t_duration, boolean isexpression, int num_elements) {
		id = t_id;
		duration = t_duration;
		expression = isexpression;
		elements = num_elements;
		attrs = new String[num_elements];
		values = new String[num_elements];
		closedtag = false;
	}


	/**
	 *  Gets whether the end tag has been encountered for the VHML element
	 *
	 *@return    Whether the end tag has been encountered
	 */
	public boolean getIsClosedTag() {
		return closedtag;
	}


	/**
	 *  Sets whether the end tag has been encountered for the VHML element
	 *
	 *@param  ct  Whether the tag has been closed
	 */
	public void setClosedTag(boolean ct) {
		closedtag = ct;
	}


	/**
	 *  Gets whether the speech element is expressive (rather thatn prosodic)
	 *
	 *@return    The isExpression value
	 */
	public boolean getIsExpression() {
		return expression;
	}


	/**
	 *  Sets the duration attribute of the speech element
	 *
	 *@param  t_duration  The new duration value
	 */
	public void setDuration(int t_duration) {
		duration = t_duration;
	}


	/**
	 *  Sets an attribute of the speech element
	 *
	 *@param  i     The attribute to set
	 *@param  attr  The new attribute value
	 */
	public void setAttribute(int i, String attr) {
		attrs[i] = new String(attr);
	}


	/**
	 *  Sets a value of the speech element
	 *
	 *@param  i    The value to set
	 *@param  val  The new value
	 */
	public void setValue(int i, String val) {
		values[i] = new String(val);
	}


	/**
	 *  Gets the num of attributes for the speech element
	 *
	 *@return    The number of attributes
	 */
	public int getNumElements() {
		return elements;
	}


	/**
	 *  Gets the id of the speech element
	 *
	 *@return    The id value
	 */
	public int getId() {
		return id;
	}


	/**
	 *  Gets the duration of the speech element
	 *
	 *@return    The duration value
	 */
	public int getDuration() {
		return duration;
	}


	/**
	 *  Gets a value of the speech element
	 *
	 *@param  i  The value index
	 *@return    The value
	 */
	public String getValue(int i) {
		return values[i];
	}


	/**
	 *  Gets an attribute of the speech element
	 *
	 *@param  i  The attribute index
	 *@return    The attribute value
	 */
	public String getAttribute(int i) {
		return attrs[i];
	}
}

