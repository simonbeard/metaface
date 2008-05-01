package metaface.interaction;

import java.util.*;
import java.io.*;
/**
 * This class provides a different type of DMTL State to cover proactive actions
 *@author     Simon Beard 
 *@version    1.0
 *@see	InteractionManager
 */
public class ActiveState extends State {

	/**
	 *  Constructor for the ActiveState object
	 *
	 *@param  statename  The name of the DMTL state
	 */
	public ActiveState(String statename) {
		super(statename);
	}


	/**
	 *  Performs a match on user stimulus such as natural language or a hypertext link
	 *
	 *@param  ic        The current context of the interaction
	 *@param  path      A stack denoting the path (within the DMTL file) to this state
	 *@param  bestlist  A list of the best matching states
	 *@return           The modified list of best matching states
	 */
	public BestList matchStimulus(InteractionContext ic, Stack path, BestList bestlist) {
		boolean process;

		//By default -- do nothing (designed to be overridden)
		process = false;

		if (process) {
			return (super.matchStimulus(ic, path, bestlist));
		}

		return (bestlist);
	}
}
