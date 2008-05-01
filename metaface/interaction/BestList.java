package metaface.interaction;

import java.util.*;
/**
 *  This class keeps a list of the best matching DMTL states to a user's
 *  stimulus
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        InteractionManager
 */
public class BestList extends java.lang.Object {

	/**
	 *  The actual list of states and associated information
	 */
	private Vector bestlist;
	/**
	 *  The minimum score for a relevant match
	 */
	public static float MinimumScore = 0f;


	/**
	 *  Creates new empty BestList
	 */
	public BestList() {
		bestlist = new Vector();
	}


	/**
	 *  Adds a new entry to the BestList vector
	 *
	 *@param  score           The score associated with matching the user's
	 *      stimulus
	 *@param  statereference  The full path to the DMTL state
	 *@param  nextstates      Description of the Parameter
	 */
	public void add(float score, Stack statereference, Vector nextstates) {
		int i;
		boolean inserted;

		// Insert into BestList vector according to score
		if (bestlist.size() == 0) {
			bestlist.add(new BestListItem(score, statereference, nextstates));
		} else if (score >= ((BestListItem) (bestlist.elementAt(0))).getScore()) {
			bestlist.insertElementAt(new BestListItem(score, statereference, nextstates), 0);
		} else {
			inserted = false;
			for (i = 0; i < bestlist.size() - 1; i++) {
				if ((score <= ((BestListItem) (bestlist.elementAt(i))).getScore()) &&
						(score >= ((BestListItem) (bestlist.elementAt(i + 1))).getScore())) {
					bestlist.insertElementAt(new BestListItem(score, statereference, nextstates), i + 1);
					inserted = true;
					i = bestlist.size();
				}
			}
			if (!inserted) {
				bestlist.add(new BestListItem(score, statereference, nextstates));
			}
		}
	}


	/**
	 *  The size of the BestList vector
	 *
	 *@return    The size of the BestList vector
	 */
	public int size() {
		return (bestlist.size());
	}


	/**
	 *  Description of the Method
	 */
	public void reset() {
		bestlist.clear();
	}


	/**
	 *  Get the state reference (full path) of an element of the BestList vector
	 *
	 *@param  i  Description of the Parameter
	 *@return    The state reference (full path) of an element of the BestList
	 *      vector
	 */
	public Stack getReferenceAt(int i) {
		if (i > bestlist.size() - 1) {
			return (null);
		}
		return (((BestListItem) (bestlist.elementAt(i))).getStateReference());
	}


	/**
	 *  Gets the nextStatesAt attribute of the BestList object
	 *
	 *@param  i  Description of the Parameter
	 *@return    The nextStatesAt value
	 */
	public Vector getNextStatesAt(int i) {
		if (i > bestlist.size() - 1) {
			return (new Vector());
		}
		return (((BestListItem) (bestlist.elementAt(i))).getNextStates());
	}


	/**
	 *  Debugging method for printing the list of best matches to "System.out"
	 */
	public void print() {
		int i;
		Stack path;

		System.out.println("Printing list of matched states");
		for (i = 0; i < bestlist.size(); i++) {
			System.out.print(((BestListItem) bestlist.elementAt(i)).getScore() + " ");
			path = ((BestListItem) bestlist.elementAt(i)).getStateReference();
			System.out.println(path.peek());
		}
	}
}

