package metaface.interaction;

import java.util.*;
import java.io.*;
/**
 *  This class provides an abstract basis for DMTL State elements. There are
 *  three basic “types”: <menu>
 *  <li> Active: is a proactive state that does not need to be matched to a
 *  stimulus. These states are implementation dependant and a response may be
 *  given to the user for many reasons. The MetaFace framework does not have a
 *  default implementation for this type of state, and the developer would need
 *  to extend the ActiveState class in order to provide the relevant processing
 *  for these types of states.</li>
 *  <li> Entry: is the most common type of state and “entry” denotes that this
 *  state can be processed by the interaction manager without it having given a
 *  certain response in the previous interaction.</li>
 *  <li> Linked: is the opposite of an “entry” state, and specifies that the
 *  previous response must be linked to this state by the use of the “prestate”
 *  and “nextstate” elements. These “linked” states are used to deal with
 *  anaphora or very closely contextually linked states.</li> </menu> The
 *  “state” element is the building block of interaction management.
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        InteractionManager
 *@see        SubTopic
 */
public abstract class State {
	/**
	 *  The DMTL name of the state
	 */
	private String name;
	/**
	 *  Whether this state was visited in the previous interaction
	 */
	private boolean visited;
	/**
	 *  The last response given by this state
	 */
	private int lastresponse;
	/**
	 *  A list of DMTL stimulus
	 */
	private Vector stimuli;
	/**
	 *  A list of the state's responses
	 */
	private Vector responses;
	/**
	 *  A list of the state's pre states
	 */
	private Vector prestates;
	/**
	 *  A list of the state's next states
	 */
	private Vector nextstates;
	/**
	 *  A list of the state's evaluation elements
	 */
	private Vector evaluates;
	/**
	 *  A list of the state's signals
	 */
	private Vector signals;

	/**
	 *  A word graph representing and natural language (TEXT) stimuli, and used in
	 *  matching
	 */
	protected WordGraph template;


	/**
	 *  Constructor for the State object
	 *
	 *@param  statename  The DMTL name of the state
	 */
	public State(String statename) {
		name = new String(statename);
		responses = new Vector();
		stimuli = new Vector();
		prestates = new Vector();
		nextstates = new Vector();
		evaluates = new Vector();
		signals = new Vector();
		visited = false;
		lastresponse = 0;
		template = null;
	}


	/**
	 *  Gets the name attribute of the State
	 *
	 *@return    The DMTL name
	 */
	public String getName() {
		return (name);
	}


	/**
	 *  Resets any internal state variables
	 */
	public void reset() {
		visited = false;
		lastresponse = 0;
	}


	/**
	 *  Has this state been visted in the previous interaction
	 *
	 *@return    Whether visited
	 */
	public boolean visited() {
		return (visited);
	}


	/**
	 *  Adds a stimulus element to the State
	 *
	 *@param  element  The DMTL stimulus element
	 */
	public void addStimulus(Stimulus element) {
		stimuli.add(element);
	}


	/**
	 *  Adds a response element to the State
	 *
	 *@param  element  The DMTL response element
	 */
	public void addResponse(Response element) {
		responses.add(element);
	}


	/**
	 *  Adds a next state element to the State
	 *
	 *@param  element  The DMTL next state element
	 */
	public void addNextState(NextState element) {
		nextstates.add(element);
	}


	/**
	 *  Adds a pre state element to the State
	 *
	 *@param  element  The DMTL pre state element
	 */
	public void addPreState(PreState element) {
		prestates.add(element);
	}


	/**
	 *  Adds a signal element to the State
	 *
	 *@param  element  The DMTL signal element
	 */

	public void addSignal(Signal element) {
		signals.add(element);
	}


	/**
	 *  Adds an evaluate element to the State
	 *
	 *@param  element  The DMTL evaluate element
	 */
	public void addEvaluate(Evaluate element) {
		evaluates.add(element);
	}


	/**
	 *  Gets the stimuli of the State
	 *
	 *@return    A list of the DMTL stimuli
	 */
	public Vector getStimuli() {
		return (stimuli);
	}


	/**
	 *  Gets the responses of the State
	 *
	 *@return    A list of the DMTL responses
	 */
	public Vector getResponses() {
		return (responses);
	}


	/**
	 *  Gets the next states of the State
	 *
	 *@return    A list of the DMTL next states
	 */
	public Vector getNextStates() {
		return (nextstates);
	}


	/**
	 *  Gets the pre states of the State
	 *
	 *@return    A list of the DMTL pre states
	 */
	public Vector getPreStates() {
		return (prestates);
	}


	/**
	 *  Gets the signals of the State
	 *
	 *@return    A list of the DMTL signals
	 */
	public Vector getSignals() {
		return (signals);
	}


	/**
	 *  Gets the evaluate elements of the State
	 *
	 *@return    A list of the DMTL evalaute elements
	 */
	public Vector getEvaluates() {
		return (evaluates);
	}


	/**
	 *  Gets the word graph template of the State to be used in matching a user's
	 *  stimulus
	 *
	 *@return    The word graph template
	 */
	public WordGraph getTemplate() {
		return (template);
	}


	/**
	 *  Any buiding instructions that must be completed after the constructor is
	 *  called
	 */
	public void build() {
		Stimulus tmpstimulus;
		Vector tmpvec;
		int i;

		//Construct a word graph from any TEXT stimuli
		tmpvec = new Vector();
		for (i = 0; i < stimuli.size(); i++) {
			tmpstimulus = (Stimulus) stimuli.elementAt(i);
			if (tmpstimulus.getType() == InteractionManager.TEXT) {
				tmpvec.add(tmpstimulus);
			}
		}
		if (tmpvec.size() > 0) {
			template = new WordGraph(tmpvec);
		}
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
		int i;
		float score;
		PreState tmpprestate;
		NextState tmpnextstate;
		Stimulus tmpstimulus;
		Evaluate tmpevaluate;
		String tmpstring;

		process = true;
		path.push(getName());

		if (ic.processingtype == InteractionManager.LINKED) {
			process = false;

			//If searching linked states then see if this state is refered to by the pre states
			for (i = 0; i < prestates.size(); i++) {
				tmpprestate = (PreState) prestates.elementAt(i);
				if (tmpprestate.linksTo(ic.previousstatepaths)) {
					process = true;
				}
			}
			if (DMTLDebugger.debug) {
				if (process == true) {
					System.out.println(name + ": PreStates match");
				}
			}
			//If searching linked states then see if this state is refered to by the next states
			for (i = 0; i < ic.linked.size(); i++) {
				tmpnextstate = (NextState) ic.linked.elementAt(i);
				if (tmpnextstate.linksTo(path)) {
					process = true;
				}
			}
			if (DMTLDebugger.debug) {
				if (process == true) {
					System.out.println(name + ": NextStates match");
				}
			}

			//If no pre or next states then process anyway
			if ((prestates.size() == 0) && (ic.linked.size() == 0)) {
				process = true;
			}
			if (DMTLDebugger.debug) {
				if (process == true) {
					System.out.println(name + ": No PreStates or NextStates");
				}
			}
		}

		if (process) {
			score = 0f;

			//Do any evaluations of the state, if any are false then don't match
			for (i = 0; i < evaluates.size(); i++) {
				tmpevaluate = (Evaluate) evaluates.elementAt(i);
				if (tmpevaluate.execute() == false) {
					process = false;
				}
			}

			if (process) {

				//If text stimulus then match against the word graph template
				if (ic.stimulustype == InteractionManager.TEXT) {
					if (template != null) {
						score = template.match(ic.stimulus);
					}
				} else if (ic.stimulustype == InteractionManager.HYPERTEXT) {

					//If hypertext stimulus then match against the hypertext stimulus elements of this state
					for (i = 0; i < stimuli.size(); i++) {
						tmpstimulus = (Stimulus) stimuli.elementAt(i);
						if (tmpstimulus.getType() == InteractionManager.HYPERTEXT) {
							tmpstring = tmpstimulus.getStimulus();
							tmpstring = tmpstring.substring(tmpstring.indexOf("://") + 3, tmpstring.length());
							if (ic.stimulus.indexOf(tmpstring) >= 0) {
								score = 1.0f;
							}
						}
					}
				}

				//If there is a score assocaited with matching then place the state into the "best list"
				if (score > 0f) {
					bestlist.add(score, (Stack) path.clone(), (Vector) nextstates.clone());
				}
			}
		}
		path.pop();
		return (bestlist);
	}


	/**
	 *  Gets a response from the State object
	 *
	 *@return    The response element
	 */
	public Response getResponse() {
		int i;

		visited = true;

		//Get a different response to last time
		lastresponse++;
		if (lastresponse >= responses.size()) {
			lastresponse = 0;
		}

		if (DMTLDebugger.debug) {
			System.out.println("Found state, getting response number: " + lastresponse);
		}

		//Execute all of the signals now that the response has been retrieved
		for (i = 0; i < signals.size(); i++) {
			((Signal) signals.elementAt(i)).execute();
		}

		return ((Response) responses.elementAt(lastresponse));
	}
}
