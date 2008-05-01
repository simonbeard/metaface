package metaface.interaction;

import java.util.*;
/**
 *  BestListItem is a structure of information for elements of a BestList vector
 *
 *@author     Simon Beard
 *@version    1.0
 *@see        BestList
 */
class BestListItem {
	/**
	 *  The score
	 */
	private float score;
	/**
	 *  The state reference (full path)
	 */
	private Stack statereference;
	/**
	 *  A list of DMTL "next states" to this one
	 */
	private Vector nextstates;


	/**
	 *  Creates a new BestListItem
	 *
	 *@param  in_score           The matching score
	 *@param  in_statereference  The refernce (full path) to the state
	 *@param  in_nextstates      Description of the Parameter
	 */
	public BestListItem(float in_score, Stack in_statereference, Vector in_nextstates) {
		score = in_score;
		statereference = in_statereference;
		nextstates = in_nextstates;
	}


	/**
	 *  Gets the score of this item
	 *
	 *@return    The score
	 */
	public float getScore() {
		return score;
	}


	/**
	 *  Gets the reference (full path) of the state
	 *
	 *@return    The state reference
	 */
	public Stack getStateReference() {
		return (Stack) statereference.clone();
	}


	/**
	 *  Gets the DMTL "next states" of this state
	 *
	 *@return    The DMTL next states
	 */
	public Vector getNextStates() {
		return (Vector) nextstates.clone();
	}
}

