package metaface.interaction;

import java.util.*;
import java.io.*;
/**
 *  This class handles all interaction within a MetaFace application.
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        KnowledgeBase
 *@see        InteractionContext
 *@see        BestList
 *@see        State
 *@see        DefaultTopic
 *@see        Evaluate
 *@see        NextState
 *@see        PreState
 *@see        Signal
 *@see        SubTopic
 */
public class InteractionManager {
	/**
	 *  The current stimulus type is natural language
	 */
	public final static int TEXT = 0;
	/**
	 *  The current stimulus type is hypertext
	 */
	public final static int HYPERTEXT = 1;

	/**
	 *  The current processing type is all entry states
	 */
	public final static int ALLENTRY = 0;
	/**
	 *  The current processing type is entry within the current subtopic
	 */
	public final static int ENTRY = 1;
	/**
	 *  The current processing type is all linked states
	 */
	public final static int LINKED = 2;
	/**
	 *  The current processing type is the default topic states
	 */
	public final static int DEFAULT = 3;
	/**
	 *  The DMTL knowledgebases used in parsing
	 */
	private Vector knowledgebases;
	/**
	 *  The current interaction context
	 */
	private InteractionContext context;
	/**
	 *  The list of best matching states to a user's stimulus
	 */
	private BestList bestlist;
	/**
	 *  The path to response files
	 */
	private String basepath;


	/**
	 *  Constructor for the InteractionManager object
	 *
	 *@param  kbs  A list of knowledge bases
	 */
	public InteractionManager(Vector kbs) {
		knowledgebases = kbs;
		context = new InteractionContext();
		context.processingtype = ALLENTRY;
		bestlist = new BestList();
		basepath = "";
	}


	/**
	 *  Constructor for the InteractionManager object
	 */
	public InteractionManager() {
		knowledgebases = new Vector();
		context = new InteractionContext();
		context.processingtype = ALLENTRY;
		bestlist = new BestList();
		basepath = "";
	}


	/**
	 *  Sets the base path for response files
	 *
	 *@param  path  The new base path value
	 */
	public void setBasePath(String path) {
		if ((path.charAt(path.length() - 1) != '\\') && (path.charAt(path.length() - 1) != '/')) {
			basepath = path + "/";
		} else {
			basepath = path;
		}
	}


	/**
	 *  Gets the base path for response files
	 *
	 *@return    The base path value
	 */
	public String getBasePath() {
		return (basepath);
	}


	/**
	 *  Adds a knowledge base to the InteractionManager object
	 *
	 *@param  kb  The knowledgebase to be added
	 */
	public void addKnowledgeBase(KnowledgeBase kb) {
		knowledgebases.add(kb);
	}


	/**
	 *  Gets the knowledge bases of the InteractionManager object
	 *
	 *@return    A list of knowledge bases
	 */
	public Vector getKnowledgeBases() {
		return (knowledgebases);
	}


	/**
	 *  Process a user's stimulus to find a response. The follwing searching
	 *  strategy is used: <menu>
	 *  <li> 1. Search the current subtopic (the subtopic from which the last
	 *  response came).</li>
	 *  <li> 2. If no relevant responses are found then search the subtopics and
	 *  topics at the same hierarchical level. If no valid response is found, then
	 *  the next highest level is searched, and so on.</li>
	 *  <li> 3. If no relevant responses are found after searching the whole
	 *  document then a response is retrieved from “defaulttopic”.</li> </menu>
	 *
	 *@param  stimulus  The user's stimulus (e.g. hypertext or natural language)
	 *@return           The response as text, or the absolute path of a file, or
	 *      null if none is found
	 */
	public String processUserStimulus(String stimulus) {
		Response response = null;
		Response responsetemp = null;
		FileReader fr;
		char[] buffer;
		String responsetext;
		int i;
		int j;
		int k;
		Stack path;
		Stack path2;
		String temp;
		Vector temppath;
		Vector temppath2;

		//Set context
		context.stimulus = stimulus;

		//Check the type of user stimulus and set context
		if ((stimulus.indexOf("http://") == 0) || (stimulus.indexOf("file://") == 0)) {
			context.stimulustype = HYPERTEXT;
		} else {
			context.stimulustype = TEXT;
		}

		if (DMTLDebugger.debug) {
			if (context.processingtype == LINKED) {
				System.out.println("\n\nProccessing LINKED states");
			} else {
				System.out.println("\n\nProccessing ALL ENTRY states");
			}
		}

		//For each knowledge base -- perform matches
		for (i = 0; i < knowledgebases.size(); i++) {
			bestlist = ((KnowledgeBase) knowledgebases.elementAt(i)).matchStimulus(context);
		}

		//Proceed with matching process until a match is found or a default response has to be retrieved
		while ((bestlist.size() == 0) && (context.processingtype != DEFAULT)) {
			if (context.processingtype == ENTRY) {
				if (DMTLDebugger.debug) {
					System.out.println("\n\nProccessing ALL ENTRY states");
				}
				context.processingtype = ALLENTRY;
				for (i = 0; i < knowledgebases.size(); i++) {
					bestlist = ((KnowledgeBase) knowledgebases.elementAt(i)).matchStimulus(context);
				}
			} else if (context.processingtype == ALLENTRY) {
				if (DMTLDebugger.debug) {
					System.out.println("\n\nProccessing DEFAULT states");
				}
				context.processingtype = DEFAULT;
			} else if (context.processingtype == LINKED) {
				if (DMTLDebugger.debug) {
					System.out.println("\n\nProccessing previous ENTRY states");
				}
				context.processingtype = ENTRY;
				for (i = 0; i < knowledgebases.size(); i++) {
					bestlist = ((KnowledgeBase) knowledgebases.elementAt(i)).matchStimulus(context);
				}
			}
		}

		response = null;

		//If stimulus is not hypertext then get a default response
		if ((context.processingtype == DEFAULT) && (context.stimulustype != HYPERTEXT)) {
			if (DMTLDebugger.debug) {
				System.out.println("Getting default response");
			}

			//Get default response
			for (i = 0; i < knowledgebases.size(); i++) {
				response = ((KnowledgeBase) knowledgebases.elementAt(i)).getDefaultResponse();
			}
			context.processingtype = ALLENTRY;
			context.linked.clear();
		} else if (bestlist.size() > 0) {
			//If there is a valid response

			if (DMTLDebugger.debug) {
				System.out.println("\n\n");
				bestlist.print();
			}

			//Reverse the path order to retrieve response
			if (DMTLDebugger.debug) {
				System.out.println("\n\nRetrieving response");
			}
			path = bestlist.getReferenceAt(0);
			temppath = new Vector();
			j = path.size();
			for (i = 0; i < j; i++) {
				temppath.add(path.pop());
			}
			temp = "";
			for (i = 0; i < j; i++) {
				path.push((String) temppath.elementAt(i));
			}
			for (i = j - 1; i >= 0; i--) {
				if (DMTLDebugger.debug) {
					System.out.print((String) temppath.elementAt(i) + "->");
				}
				if (i != j - 1) {
					temp = temp.concat((String) temppath.elementAt(i) + ".");
				}
			}
			if (DMTLDebugger.debug) {
				System.out.println("");
			}
			temp = temp.substring(0, temp.length() - 1);
			context.previousstatepaths.push(temp);

			//Get the response
			for (i = 0; i < knowledgebases.size(); i++) {
				if ((((KnowledgeBase) knowledgebases.elementAt(i)).getFilename()).indexOf((String) path.peek()) == 0) {
					temp = (String) path.pop();
					if (DMTLDebugger.debug) {
						System.out.println("Traversing: " + temp);
					}
					response = ((KnowledgeBase) knowledgebases.elementAt(i)).getResponse(path);
				}
			}

			//Set context for next interaction (search linked first)
			context.processingtype = LINKED;
			context.linked = bestlist.getNextStatesAt(0);
		} else {

			//Set context for next interaction (no valid response found)
			context.processingtype = ALLENTRY;
			context.linked.clear();
		}

		if (response == null) {
			return (null);
		}

		//If the response has a response reference in another state -- then it must be retieved
		if (response.getReference() != null) {
			if ((response.getReference()).size() > 0) {
				if (DMTLDebugger.debug) {
					System.out.println("Response has state reference: Must get new response");
				}
				if (DMTLDebugger.debug) {
					System.out.println("Using absolute path reference");
				}

				path = bestlist.getReferenceAt(0);
				path2 = response.getReference();

				//reverse stack order for path
				temppath = new Vector();
				j = path2.size();
				for (i = 0; i < j; i++) {
					temppath.add(path2.pop());
				}
				for (i = 0; i < j; i++) {
					path2.push(temppath.elementAt(i));
				}
				for (i = j - 1; i >= 0; i--) {
					if (DMTLDebugger.debug) {
						System.out.print("->" + (String) temppath.elementAt(i));
					}
				}
				if (DMTLDebugger.debug) {
					System.out.println("");
				}

				//Search path for a reponse
				responsetemp = new Response(response.getResponse(), response.getReference(), response.getType());
				for (i = 0; i < knowledgebases.size(); i++) {
					response = ((KnowledgeBase) knowledgebases.elementAt(i)).getResponse(path2);

					//if found -- exit
					if (response != null) {
						i = knowledgebases.size();
					}
				}

				//If a response was not found (maybe it was a relative path)
				if (response == null) {
					response = responsetemp;
					if (DMTLDebugger.debug) {
						System.out.println("No response found");
					}
					if (DMTLDebugger.debug) {
						System.out.println("Using relative path reference");
					}

					//Make path into absolute
					path = bestlist.getReferenceAt(0);
					path.pop();
					temppath = new Vector();
					j = path.size();
					for (i = 0; i < j; i++) {
						temppath.add(path.pop());
					}
					for (i = j - 1; i >= 0; i--) {
						if (DMTLDebugger.debug) {
							System.out.print("->" + (String) temppath.elementAt(i));
						}
					}

					path2 = response.getReference();
					temppath2 = new Vector();
					k = path2.size();
					for (i = 0; i < k; i++) {
						temppath2.add(path2.pop());
					}
					for (i = 0; i < k; i++) {
						path2.push(temppath2.elementAt(i));
					}
					for (i = k - 1; i >= 0; i--) {
						if (DMTLDebugger.debug) {
							System.out.print("->" + (String) temppath2.elementAt(i));
						}
					}
					if (DMTLDebugger.debug) {
						System.out.println("");
					}
					for (i = 0; i < j; i++) {
						path2.push(temppath.elementAt(i));
					}

					//Retrieve the response
					for (i = 0; i < knowledgebases.size(); i++) {
						if ((((KnowledgeBase) knowledgebases.elementAt(i)).getFilename()).indexOf((String) path2.peek()) == 0) {
							temp = (String) path2.pop();
							if (DMTLDebugger.debug) {
								System.out.println("Traversing: " + temp);
							}
							response = ((KnowledgeBase) knowledgebases.elementAt(i)).getResponse(path2);
						}
					}
				}
			}
		}

		//If response is text -- return text
		if (response.getType() == Response.TEXT) {
			if (DMTLDebugger.debug) {
				System.out.println("Retrieving response TEXT: ");
			}
			return (response.getResponse());
		} else if (response.getType() == Response.FILE) {

			//If the response is a file -- return the absolute path of the file
			if (DMTLDebugger.debug) {
				System.out.println("Retrieving response FILE: ");
			}
			responsetext = basepath + (response.getResponse());
			return (responsetext);
		}

		return (null);
	}
}
