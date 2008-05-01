package metaface.interaction;

import java.util.*;
import java.io.*;
/**
 *  This clas represents a subtopic DMTL element and is used to organsise the
 *  structure and searching strategy.<p>
 *
 *  The use of the “keywords” and “evaluate” attributes can be used to reduce
 *  the search time when the dialogue manager is attempting to find a response.
 *  For the dialogue manager to enter a subtopic, a user’s stimulus must contain
 *  a word that matches one of the keywords. <p>
 *
 *  The “evaluate” attribute string is a Boolean equation, and it must be true
 *  for the dialogue manager to proceed with the processing of a “subtopic”. The
 *  “evaluate” attribute string is not specified or validated within the DMTL
 *  design, as it contains application specific data. The application code must
 *  override the “evaluate” method in order to provide a customised handler
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        InteractionManager
 *@see        Topic
 *@see        State
 */
public class SubTopic {
	/**
	 *  DMTL name of the subtopic
	 */
	private String name;
	/**
	 *  DMTL keywords of the subtopic
	 */
	private String keywordstring;
	/**
	 *  DMTL evaluate string of the subtopic
	 */
	private String evaluatestring;
	/**
	 *  States within the subtopic
	 */
	private Vector states;
	/**
	 *  Subtopics within this subtopic
	 */
	private Vector subtopics;
	/**
	 *  Was this subtopic visited in the previous interaction
	 */
	private boolean visited;


	/**
	 *  Constructor for the SubTopic object
	 *
	 *@param  namestr      The DMTL name of the subtopic
	 *@param  keywordstr   The DMTL CDATA keyword string
	 *@param  evaluatestr  The DMTL CDATA evaluate string
	 */
	public SubTopic(String namestr, String keywordstr, String evaluatestr) {
		int i;

		keywordstring = keywordstr;
		evaluatestring = evaluatestr;
		name = namestr;
		subtopics = new Vector();
		states = new Vector();
		visited = false;
	}


	/**
	 *  Any buiding instructions that must be completed after the constructor is
	 *  called
	 */
	public void build() {
	}


	/**
	 *  Resets any internal state variables
	 */
	public void reset() {
		visited = false;
	}


	/**
	 *  Gets the name attribute of the SubTopic
	 *
	 *@return    The DMTL name
	 */
	public String getName() {
		return (name);
	}


	/**
	 *  Gets the keywords attribute of the SubTopic
	 *
	 *@return    The DMTL keywords string
	 */
	public String getKeywordString() {
		return (keywordstring);
	}


	/**
	 *  Gets the evaluate string attribute of the SubTopic
	 *
	 *@return    The evaluate string
	 */
	public String getEvaluateString() {
		return (evaluatestring);
	}


	/**
	 *  Gets the states within the SubTopic object
	 *
	 *@return    The states
	 */
	public Vector getStates() {
		return (states);
	}


	/**
	 *  Gets the subtopics within the SubTopic object
	 *
	 *@return    The subtopics
	 */
	public Vector getSubTopics() {
		return (subtopics);
	}


	/**
	 *  Adds a subtopic to the SubTopic object
	 *
	 *@param  element  The subtopic to add
	 */
	public void addSubTopic(SubTopic element) {
		subtopics.add(element);
	}


	/**
	 *  Adds a state to the SubTopic object
	 *
	 *@param  element  The state to add
	 */
	public void addState(State element) {
		states.add(element);
	}


	/**
	 *  The method that is called when processing the state for matching and based
	 *  upon the DMTL evaluate attribute
	 *
	 *@param  evualuatestr  the DMTL evalute string
	 *@return               whether the evaluate condition is met
	 */
	protected boolean evaluate(String evualuatestr) {
		return (true);
	}


	/**
	 *  The method that is called when processing the state for matching and based
	 *  upon the DMTL keywords attribute. The user's stimulus must match a keyword
	 *  of the subtopic.
	 *
	 *@param  keywordstr  the DMTL keywords string
	 *@param  stimulus    the user's stimulus
	 *@return             whether a keyword matches a word of the stimulus
	 */
	protected boolean keywordEvaluate(String keywordstr, String stimulus) {
		boolean process = false;
		WordChain tmpstimulus;
		int i;

		//Seperate words
		tmpstimulus = new WordChain(stimulus);

		//For each word, match to keywords
		for (i = 0; i < tmpstimulus.size(); i++) {
			if (keywordstring.indexOf(tmpstimulus.getWord(i)) > -1) {
				process = true;
				i = tmpstimulus.size();
			}
		}

		return (process);
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
		int i;
		State tmpstate;
		SubTopic tmpsubtopic;
		Evaluate tmpevaluate;
		boolean process;
		WordChain tmpstimulus;

		//Process keywords and evaluate string of subtopic
		process = true;
		if (evaluatestring != null) {
			process = evaluate(evaluatestring);
		}
		if (keywordstring != null) {
			process = keywordEvaluate(keywordstring, ic.stimulus);
		}

		path.push(name);
		if (process) {

			//For each state in subtopic -- perform matching
			for (i = 0; i < states.size(); i++) {
				tmpstate = (State) states.elementAt(i);
				bestlist = tmpstate.matchStimulus(ic, path, bestlist);
			}

			//For each subtopic in this subtopic -- perform matching
			for (i = 0; i < subtopics.size(); i++) {
				tmpsubtopic = (SubTopic) subtopics.elementAt(i);
				bestlist = tmpsubtopic.matchStimulus(ic, path, bestlist);
			}
		}
		path.pop();

		return (bestlist);
	}


	/**
	 *  Gets a response based on a state reference
	 *
	 *@param  path  The path to the response (state reference)
	 *@return       The response element
	 */
	public Response getResponse(Stack path) {
		int i;
		SubTopic tmpsubtopic;
		State tmpstate;
		String tmpname;
		String temp;

		//For each subtopic -- see if path name matches
		for (i = 0; i < subtopics.size(); i++) {
			tmpsubtopic = (SubTopic) subtopics.elementAt(i);
			tmpname = tmpsubtopic.getName();
			if (tmpname.equalsIgnoreCase((String) path.peek())) {
				temp = (String) path.pop();
				if (DMTLDebugger.debug) {
					System.out.println("Traversing: " + temp);
				}

				//Matches -- so process subtopic
				return (tmpsubtopic.getResponse(path));
			}
		}

		//For each state -- see if path name matches
		for (i = 0; i < states.size(); i++) {
			tmpstate = (State) states.elementAt(i);
			tmpname = tmpstate.getName();
			if (tmpname.equalsIgnoreCase((String) path.peek())) {
				temp = (String) path.pop();
				if (DMTLDebugger.debug) {
					System.out.println("Traversing: " + temp);
				}
				//Matches -- so process state
				return (tmpstate.getResponse());
			}
		}
		return (null);
	}
}
