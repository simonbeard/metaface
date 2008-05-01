package metaface.interaction;

import java.util.*;
import java.io.*;
/**
 *  This class provides a response to a user's interaction with natural language
 *  or hypertext
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        InteractionManager
 */
public class Response {
	/**
	 *  This reponse is a file
	 */
	public final static int FILE = 0;
	/**
	 *  This reponse is textual
	 */
	public final static int TEXT = 1;
	/**
	 *  The DMTL string response
	 */
	private String response;
	/**
	 *  The DMTL reference to the response
	 */
	private String reference;
	/**
	 *  Wether the repsone is TEXT or a FILE
	 */
	private int type;
	/**
	 *  The reference path to the response
	 */
	private Stack referencepath;


	/**
	 *  Constructor for the Response object
	 *
	 *@param  responsestring   The DMTL PCDATA response
	 *@param  referencestring  A reference string to another response state (may be
	 *      null)
	 *@param  responsetype     The type of response (TEXT by default)
	 */
	public Response(String responsestring, String referencestring, int responsetype) {
		int i;
		int j;
		String temp;

		//Set internat variables
		response = responsestring;
		reference = referencestring;
		type = responsetype;

		//Create the reference path to this response
		referencepath = new Stack();
		if (reference != null) {
			if (DMTLDebugger.debug) {
				System.out.println("Creating state reference: " + referencestring);
			}
			i = 0;
			j = 0;
			i = reference.indexOf('.');
			while (i >= 0) {
				temp = reference.substring(j, i);
				referencepath.push(temp);
				i++;
				j = i;
				i = reference.indexOf('.', i);
			}
			i = reference.length();
			temp = reference.substring(j, i);
			referencepath.push(temp);
		}
	}


	/**
	 *  Constructor for the Response object
	 *
	 *@param  responsestring   The DMTL PCDATA response
	 *@param  responsetype     The type of response (TEXT by default)
	 *@param  referencestack   Description of the Parameter
	 */
	public Response(String responsestring, Stack referencestack, int responsetype) {
		response = responsestring;
		reference = "";
		type = responsetype;
		referencepath = (Stack) referencestack.clone();
	}


	/**
	 *  Gets the type attribute of response (TEXT or FILE)
	 *
	 *@return    The type
	 */
	public int getType() {
		return (type);
	}


	/**
	 *  Gets the DMTL response string
	 *
	 *@return    The response
	 */
	public String getResponse() {
		return (response);
	}


	/**
	 *  Gets the reference to another response
	 *
	 *@return    The reference path
	 */
	public Stack getReference() {
		return (referencepath);
	}
}
