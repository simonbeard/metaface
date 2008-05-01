package metaface.interaction;

import java.util.*;
import java.io.*;

/**
 *  This class is based on the DMTL nextstate element and represents a state that
 *  must be searched in the following interaction
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        InteractionManager
 */
public class NextState {
	private String link;


	/**
	 *  Constructor for the NextState object
	 *
	 *@param  linkstring  The state reference of the state to be processed in the
	 *      following interaction
	 */
	public NextState(String linkstring) {
		link = linkstring;
	}


	/**
	 *  Does this NextState object link to a state
	 *
	 *@param  paths  The path to a state
	 *@return        Whether this NextState object links to the path
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
	 *  Gets the link attribute of the NextState object
	 *
	 *@return    The link state reference
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
