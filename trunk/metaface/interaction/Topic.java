package metaface.interaction;

import java.util.*;
import java.io.*;
/**
 *  This clas represents a subtopic DMTL element and is used to organsise the
 *  structure and searching strategy.<p>
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        InteractionManager 
 *@see        SubTopic
 */
public class Topic {
	/**
	 *  DMTL name of the topic
	 */	
	private String name;
	/**
	 *  Subtopics within this topic
	 */	
	private Vector subtopics;
	/**
	 *  Was this topic visited in the previous interaction
	 */	
	private boolean visited = false;


	/**
	 *  Constructor for the Topic object
	 *
	 *@param  namestring      The DMTL name of the topic
	 */
	public Topic(String namestring) {
		name = new String(namestring);
		subtopics = new Vector();
		visited = false;
	}


	/**
	 *  Adds a subtopic to the Topic object
	 *
	 *@param  element  The subtopic to add
	 */
	public void addSubTopic(SubTopic element) {
		subtopics.add(element);
	}


	/**
	 *  Gets the name attribute of the Topic
	 *
	 *@return    The DMTL name
	 */
	public String getName() {
		return (name);
	}


	/**
	 *  Gets the subtopics within the Topic object
	 *
	 *@return    The subtopics
	 */
	public Vector getSubTopics() {
		return (subtopics);
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
		boolean process;
		SubTopic tmpsubtopic;

		process = true;
		path.push(name);
		
		//Only process if visisted in the previous interaction (and processing ENTRY states)
		if ((ic.processingtype == InteractionManager.ENTRY) && (!visited)) {
			process = false;
		}
		if (process) {
			
			//Process all subtopics
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
		String subtopicname;
		String temp;

		visited = true;
		
		//Search all subtopics
		for (i = 0; i < subtopics.size(); i++) {
			
			//If path name matches subtopic name -- process
			tmpsubtopic = (SubTopic) subtopics.elementAt(i);
			subtopicname = tmpsubtopic.getName();
			if (subtopicname.equalsIgnoreCase((String) (path.peek()))) {
				temp = (String) path.pop();
				if (DMTLDebugger.debug) {
					System.out.println("Traversing: " + temp);
				}
				
				//Get the response from the subtopic
				return (tmpsubtopic.getResponse(path));
			}
		}
		return (null);
	}
}
