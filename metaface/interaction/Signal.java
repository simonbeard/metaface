package metaface.interaction;

import java.util.*;
import java.io.*;
/**
 *  This class provides a signal to application code once a state is given as a
 *  response
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        InteractionManager
 */
public class Signal {
	/**
	 *  The name attribute of the Signal element
	 */
	private String name;
	/**
	 *  The data type attribute of the Signal element
	 */
	private String datatype;
	/**
	 *  The data attribute of the Signal element
	 */
	private String data;


	/**
	 *  Constructor for the Signal object
	 *
	 *@param  namestring      The DMTL name component
	 *@param  datatypestring  The DMTL data type component
	 *@param  datastring      The DMTL data component
	 */
	public Signal(String namestring, String datatypestring, String datastring) {
		name = namestring;
		datatype = datatypestring;
		data = datastring;
	}


	/**
	 *  Gets the name attribute of the Signal element
	 *
	 *@return    The name
	 */
	protected String getName() {
		return (name);
	}


	/**
	 *  Gets the data type attribute of the Signal element
	 *
	 *@return    The data type
	 */
	protected String getDataType() {
		return (datatype);
	}


	/**
	 *  Gets the data attribute of the Signal element
	 *
	 *@return    The data
	 */
	protected String getData() {
		return (data);
	}


	/**
	 *  Sets the name attribute of the Signal element
	 *
	 *@param  namestring  The new name
	 */
	protected void setName(String namestring) {
		name = namestring;
	}


	/**
	 *  Sets the dataType attribute of the Signal element
	 *
	 *@param  datatypestring  The new data type
	 */
	protected void setDataType(String datatypestring) {
		data = datatypestring;
	}


	/**
	 *  Sets the data attribute of the Signal element
	 *
	 *@param  datastring  The new data
	 */
	protected void setData(String datastring) {
		data = datastring;
	}


	/**
	 *  This method is called when a state is given as a response and this signal
	 *  element is encountered
	 */
	public void execute() { }


	/**
	 *  Any buiding instructions that must be completed after the constructor is
	 *  called
	 */
	public void build() { }
}
