package metaface.interaction;

import java.util.*;
import java.io.*;
/**
 *  This class store the DMTL default topic element and all of the subsequent
 *  states. The “defaulttopic” element can contain zero or more “states” and is
 *  used when the dialogue manager cannot find any matching responses to a
 *  user’s stimulus. In this case a response is chosen randomly from one of the
 *  states contained within the “defaulttopic” element. The “defaulttopic”
 *  element is the last resort for dialogue management, and is used to generate
 *  different “I don’t know” responses for a given DMTL document
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        InteractionManager
 */
public class DefaultTopic {
	/**
	 *  A list of the default topic states
	 */
	private Vector states;
	/**
	 *  The next default response to give
	 */
	private int nextdefaultresponse = 0;


	/**
	 *  Constructor for the DefaultTopic object
	 */
	public DefaultTopic() {
		states = new Vector();
		nextdefaultresponse = 0;
	}


	/**
	 *  Adds a state to the default topic
	 *
	 *@param  element  The state to be added
	 */
	public void addState(State element) {
		states.add(element);
	}


	/**
	 *  Gets the states within the default topic
	 *
	 *@return    The DMTL states
	 */
	public Vector getStates() {
		return (states);
	}


	/**
	 *  Description of the Method
	 */
	public void build() {
	}


	/**
	 *  Description of the Method
	 */
	public void reset() {
		nextdefaultresponse = 0;
	}


	/**
	 *  Gets a default response when there are no clear matches to a user's
	 *  stimulus
	 *
	 *@return    A default response chosen
	 */
	public Response getDefaultResponse() {
		int i;
		State tmpstate;

		//Get the next response (try not to be predicable)
		if (nextdefaultresponse >= states.size()) {
			nextdefaultresponse = 0;
		}

		//Get the state
		tmpstate = (State) states.elementAt(nextdefaultresponse);

		//Increment next response
		nextdefaultresponse++;

		return (tmpstate.getResponse());
	}
}
