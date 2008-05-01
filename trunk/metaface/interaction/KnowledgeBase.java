package metaface.interaction;

import java.util.*;
import java.io.*;
/**
 *  This class is an internal representation of a DMTL document. DMTL is defined
 *  by the following DMTL structure: 
 *  <IMG SRC="../../resources/DMTL_h.jpg"/><BR/>
 *  <IMG SRC="../../resources/DMTL_a.jpg"/><BR/>
 *  See the DMTL <A HREF="../../resources/DMTL.dtd">DTD</a> or and 
 *  <A HREF="../../resources/dmtl_example.xml">example file</a> .
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        InteractionManager
 *@see        State
 *@see        DefaultTopic
 *@see        Evaluate
 *@see        NextState
 *@see        PreState
 *@see        Signal
 *@see        SubTopic
 */
public class KnowledgeBase {
	private Vector topics;
	private DefaultTopic defaulttopic;
	private String filename;
	private boolean visited;


	/**
	 *  Constructor for the KnowledgeBase object
	 *
	 *@param  filenamestring  The DMTL file name, without path information, from
	 *      which this knowledge base was constructed
	 */
	public KnowledgeBase(String filenamestring) {
		filename = filenamestring;
		topics = new Vector();
		visited = false;
	}


	/**
	 *  Adds a Topic element to the KnowledgeBase object
	 *
	 *@param  element  The topic element
	 */
	public void addTopic(Topic element) {
		topics.add(element);
	}


	/**
	 *  Adds a DefaultTopic element to the KnowledgeBase object
	 *
	 *@param  element  The default topic element
	 */
	public void addDefaultTopic(DefaultTopic element) {
		defaulttopic = element;
	}


	/**
	 *  Gets the filename from which the KnowledgeBase object was constructed
	 *
	 *@return    The filename value
	 */
	public String getFilename() {
		return (filename);
	}


	/**
	 *  Gets the topics of the knowledge base
	 *
	 *@return    A list of topics
	 */
	public Vector getTopics() {
		return (topics);
	}


	/**
	 *  Gets the default topics of the knowledge base
	 *
	 *@return    The default topic
	 */
	public DefaultTopic getDefaultTopic() {
		return (defaulttopic);
	}


	/**
	 *  Building that must commence after construction and initialistation
	 */
	public void build() {
	}


	/**
	 *  Resets state variables of the knowledge base
	 */
	public void reset() {
		visited = false;
	}


	/**
	 *  Performs matching of the DMTL representation to a user's stimulus (e.g.
	 *  natural language or hypertext)
	 *
	 *@param  ic  The current interaction context
	 *@return     A lest of the best matching states
	 */
	public BestList matchStimulus(InteractionContext ic) {
		int i;
		boolean process;
		Topic tmptopic;
		Stack path;
		BestList bestlist;

		//Initialisation
		bestlist = new BestList();
		process = true;
		path = new Stack();
		path.push(filename);

		//If processing entry states, the knowledge base can only be processed if it yielded a response in the previous interation
		if ((ic.processingtype == InteractionManager.ENTRY) && (!visited)) {
			process = false;
		}
		if (process) {

			//Process each topic
			for (i = 0; i < topics.size(); i++) {
				tmptopic = (Topic) topics.elementAt(i);
				bestlist = tmptopic.matchStimulus(ic, path, bestlist);
			}
		}
		path.pop();
		return (bestlist);
	}


	/**
	 *  Gets a response based on the path
	 *
	 *@param  path  The path to the target response
	 *@return       A response string consisting of either an absolute path and
	 *      filename, or a textual response, or null if no response is found
	 */
	public Response getResponse(Stack path) {
		int i;
		Topic tmptopic;
		String topicname;

		visited = true;

		//Search each topic for a matching name in the path stack
		for (i = 0; i < topics.size(); i++) {
			tmptopic = (Topic) topics.elementAt(i);
			topicname = tmptopic.getName();

			//If found then process the subtopic
			if (topicname.equalsIgnoreCase((String) (path.peek()))) {
				path.pop();
				return (tmptopic.getResponse(path));
			}
		}
		return (null);
	}


	/**
	 *  Gets a default response
	 *
	 *@return    A response string consisting of either an absolute path and
	 *      filename, or a textual response
	 */
	public Response getDefaultResponse() {
		return (defaulttopic.getDefaultResponse());
	}
}
