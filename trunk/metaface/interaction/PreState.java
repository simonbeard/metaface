package metaface.interaction;

import java.util.*;
import java.io.*;

/**
 *  This class is based on the DMTL prestate element and represents a state that
 *  must have been given as a response in order to access this state during
 *  interaction
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        InteractionManager
 */
public class PreState {
	private String link;


	/**
	 *  Constructor for the PreState object
	 *
	 *@param  linkstring  The state reference of the state that must have been
	 *      given as a response
	 */
	public PreState(String linkstring) {
		link = linkstring;
	}


	/**
	 *  Does this PreState object link to a state
	 *
	 *@param  paths  The path to a state
	 *@return        Whether this PreState object links to the path
	 */
	public boolean linksTo(Stack paths) {
		String tmppath = "";
		int i;

		if (paths == null) {
			return (true);
		}
		if (paths.size() == 0) {
			return (true);
		}

		//Get the path of the previous state
		tmppath = tmppath.concat((String) paths.elementAt(0));
		if (tmppath.indexOf(link) >= 0) {
			return (true);
		}
		return (false);
	}


	/**
	 *  Gets the link attribute of the PreState object
	 *
	 *@return    The link value
	 */
	public String getLink() {
		return (link);
	}


	/**
	 *  Perform any building after construction of the object
	 */
	public void build() {
	}


	/**
	 *  Reset internal state variables
	 */
	public void reset() {
	}
}
