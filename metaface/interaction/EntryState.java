package metaface.interaction;

import java.util.*;
import java.io.*;
/**
 *  This class provides a different type of DMTL State to cover normal entry
 *  actions
 *
 *@author     Simon Beard
 *@version    1.0
 *@see        InteractionManager
 */
public class EntryState extends State {

	/**
	 *  Constructor for the EntryState object
	 *
	 *@param  statename  The name of the DMTL state
	 */
	public EntryState(String statename) {
		super(statename);
	}


	/**
	 *  Performs a match on user stimulus such as natural language or a hypertext
	 *  link
	 *
	 *@param  ic        The current context of the interaction
	 *@param  path      A stack denoting the path (within the DMTL file) to this
	 *      state
	 *@param  bestlist  A list of the best matching states
	 *@return           The modified list of best matching states
	 */
	public BestList matchStimulus(InteractionContext ic, Stack path, BestList bestlist) {
		boolean process;

		process = false;

		//Check that this state can be processed (correct context)
		if (ic.processingtype == InteractionManager.ALLENTRY) {
			process = true;
		} else if (ic.processingtype == InteractionManager.ENTRY) {
			process = true;
		} else if (ic.processingtype == InteractionManager.LINKED) {
			process = true;
		}

		//Go ahead and score the match
		if (process) {
			return (super.matchStimulus(ic, path, bestlist));
		}
		return (bestlist);
	}
}
