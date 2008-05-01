package metaface.interaction;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;
import java.io.*;

/**
 *  This class contains the callback routines neccessary to read DMTL into
 *  memory (KnowledgeBase). This format is then used by the InteractionManager.
 *  DMTL is defined by the following DMTL structure: 
 *  <IMG SRC="../../resources/DMTL_h.jpg"/><BR/>
 *  <IMG SRC="../../resources/DMTL_a.jpg"/><BR/>
 *  See the DMTL <A HREF="../../resources/DMTL.dtd">DTD</a> or and 
 *  <A HREF="../../resources/dmtl_example.xml">example file</a>.
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        InteractionManager
 *@see        DMTLParser
 *@see        KnowledgeBase
 */
public class DMTLTagReader extends DefaultHandler {
	/**
	 *  Store the current topic
	 */
	private Topic currenttopic;
	/**
	 *  Store the default topic
	 */
	private DefaultTopic currentdefaulttopic;
	/**
	 *  Store the current subtopic
	 */
	private SubTopic currentsubtopic = null;
	/**
	 *  Store the current state
	 */
	private State currentstate;
	/**
	 *  Store the current stimulus
	 */
	private Stimulus currentstimulus;
	/**
	 *  Store the stimulus macros
	 */
	private Macros macros;
	/**
	 *  The knowledgebase constructed from DMTL
	 */
	private KnowledgeBase knowledgebase;
	/**
	 *  The current processing state of the DMTL document
	 */
	DMTLMemoryState stateInfo;
	/**
	 *  Temporary storage string
	 */
	String tempchar = new String("");
	/**
	 *  The subtopics of the current subtopic
	 */
	Stack subtopicstack;


	/**
	 *  Constructor for the DMTLTagReader object
	 *
	 *@param  filename  The file to construct a knowledge base from
	 */
	public DMTLTagReader(String filename) {
		super();
		knowledgebase = new KnowledgeBase(filename);
		if (DMTLDebugger.debug) {
			System.out.println("constructing...");
		}
		stateInfo = new DMTLMemoryState();
		macros = new Macros();
		subtopicstack = new Stack();
	}


	/**
	 *  Gets the knowledge base of the DMTLTagReader object
	 *
	 *@return    The knowledge base
	 */
	public KnowledgeBase getKnowledgeBase() {
		return (knowledgebase);
	}


	/**
	 *  Parser calls this once at the beginning of a document
	 *
	 *@exception  SAXException  XML parsing Exception
	 */
	public void startDocument() throws SAXException {
		if (DMTLDebugger.debug) {
			System.out.println("starting document...");
		}
	}


	/**
	 *  Parser calls this once after parsing a document
	 *
	 *@exception  SAXException  XML parsing Exception
	 */
	public void endDocument() throws SAXException {
		if (DMTLDebugger.debug) {
			System.out.println("ending document...");
		}
	}


	/**
	 *  The parser calls this when encountering character data
	 *
	 *@param  ch                The characers encountered between tags
	 *@param  start             The starting point in the character array
	 *@param  length            The length of character data occupying the array
	 *@exception  SAXException  XML parsing Exception
	 */
	public void characters(char[] ch, int start, int length)
			 throws SAXException {
		if (DMTLDebugger.debug) {
			System.out.println("reading character data...");
		}
		tempchar = tempchar.concat(new String(ch, start, length));
	}


	/**
	 *  Parser calls this for each element in a document
	 *
	 *@param  namespaceURI      Used to resolve the namespace of the element
	 *@param  localName         The local name of the element
	 *@param  rawName           The resolved name of the element
	 *@param  atts              The attributes of the element
	 *@exception  SAXException  XML parsing Exception
	 */
	public void startElement(String namespaceURI, String localName,
			String rawName, Attributes atts)
			 throws SAXException {
		String key = rawName;
		String name;
		String datatype;
		String data;
		String type;
		String weight;
		String ref;
		String keywords;
		String evaluate;
		String statereference;
		String variable;
		String operator;
		String value;

		if (DMTLDebugger.debug) {
			System.out.print("starting element... ");
		}

		//Work out which element has been encountered
		if (key.compareTo("dialogue") == 0) {
			if (DMTLDebugger.debug) {
				System.out.println("dialogue...");
			}

			//Set the current processing state
			stateInfo.setState(DMTLMemoryState.DIALOGUE);
		} else if (key.compareTo("macros") == 0) {
			if (DMTLDebugger.debug) {
				System.out.println("  macros...");
			}

			//Set the current processing state
			stateInfo.setState(DMTLMemoryState.MACROS);
		} else if (key.compareTo("macro") == 0) {
			if (DMTLDebugger.debug) {
				System.out.print("    macro... ");
			}

			//Set the current processing state
			stateInfo.setState(DMTLMemoryState.MACRO);

			//Get element attributes
			name = atts.getValue(0);

			//Set the current processing state
			stateInfo.setMacroName(name);
			if (DMTLDebugger.debug) {
				System.out.println(name);
			}
		} else if (key.compareTo("defaulttopic") == 0) {
			if (DMTLDebugger.debug) {
				System.out.println("  defaulttopic...");
			}

			//Set the current processing state
			stateInfo.setState(DMTLMemoryState.DEFAULTTOPIC);
			stateInfo.setIfDefaultTopic(true);

			//Construct element
			currentdefaulttopic = new DefaultTopic();
		} else if (key.compareTo("topic") == 0) {
			if (DMTLDebugger.debug) {
				System.out.print("  topic... ");
			}
			//Set the current processing state
			stateInfo.setState(DMTLMemoryState.TOPIC);

			//Get attributes
			name = atts.getValue(0);

			//Construct the element
			currenttopic = new Topic(name);
			if (DMTLDebugger.debug) {
				System.out.println(name);
			}
		} else if (key.compareTo("subtopic") == 0) {
			if (DMTLDebugger.debug) {
				System.out.print("    subtopic... ");
			}
			//Set the current processing state
			stateInfo.setState(DMTLMemoryState.SUBTOPIC);

			//Get attributes
			name = atts.getValue(0);
			keywords = atts.getValue("keywords");
			evaluate = atts.getValue("evaluate");

			//Construct the element
			currentsubtopic = new SubTopic(name, keywords, evaluate);

			//Keep trach of the previos subtopics
			subtopicstack.push(currentsubtopic);
			if (DMTLDebugger.debug) {
				System.out.println(name);
			}
		} else if (key.compareTo("state") == 0) {
			if (DMTLDebugger.debug) {
				System.out.print("      state... ");
			}

			//Set the current processing state
			stateInfo.setState(DMTLMemoryState.STATE);

			//Get attributes
			name = atts.getValue("name");
			type = atts.getValue("type");

			//Work out the type of State encountered and construct
			if (type.indexOf("linked") == 0) {
				currentstate = new LinkedState(name);
			} else if (type.indexOf("active") == 0) {
				currentstate = new ActiveState(name);
			} else {
				currentstate = new EntryState(name);
			}
			if (DMTLDebugger.debug) {
				System.out.println(name);
			}
		} else if (key.compareTo("stimulus") == 0) {
			if (DMTLDebugger.debug) {
				System.out.println("        stimulus...");
			}
			tempchar = "";

			//If the stimulus is inside a macro
			if (stateInfo.getState() == stateInfo.MACRO) {
				stateInfo.setIfMacro(true);
			} else {

				//Else stimulus is inside a state
				stateInfo.setIfMacro(false);
				stateInfo.setIfFirstResponse(true);
				stateInfo.setState(DMTLMemoryState.STIMULUS);
			}

			//Get attributes
			type = atts.getValue(0);
			if (type == null) {
				type = "text";
			}

			//Set the current processing state
			stateInfo.setStimulusType(InteractionManager.TEXT);
			if (type.indexOf("hypertext") == 0) {
				stateInfo.setStimulusType(InteractionManager.HYPERTEXT);
			}
		} else if (key.compareTo("response") == 0) {
			if (DMTLDebugger.debug) {
				System.out.println("        response...");
			}
			tempchar = "";

			//Set the current processing state
			stateInfo.setState(DMTLMemoryState.RESPONSE);

			//Get attributes
			statereference = atts.getValue("statereference");
			type = atts.getValue("type");
			if (type == null) {
				type = "text";
			}

			//Set the current processing state
			stateInfo.setResponseType(Response.TEXT);
			if (type.indexOf("file") == 0) {
				stateInfo.setResponseType(Response.FILE);
			}
			stateInfo.setResponseReference(statereference);
		} else if (key.compareTo("signal") == 0) {
			if (DMTLDebugger.debug) {
				System.out.println("        signal...");
			}

			//Set the current processing state
			stateInfo.setState(DMTLMemoryState.SIGNAL);

			//Get attributes
			name = atts.getValue("name");
			datatype = atts.getValue("datatype");
			data = atts.getValue("data");

			//Construct element within current state
			currentstate.addSignal(new Signal(name, datatype, data));
		} else if (key.compareTo("prestate") == 0) {
			if (DMTLDebugger.debug) {
				System.out.println("        prestate...");
			}

			//Set the current processing state
			stateInfo.setState(DMTLMemoryState.PRESTATE);

			//Get attributes
			name = atts.getValue(0);

			//Construct element within current state
			currentstate.addPreState(new PreState(name));
		} else if (key.compareTo("nextstate") == 0) {
			if (DMTLDebugger.debug) {
				System.out.println("        nextstate...");
			}

			//Set the current processing state
			stateInfo.setState(DMTLMemoryState.NEXTSTATE);

			//Get attributes
			name = atts.getValue(0);

			//Construct element within current state
			currentstate.addNextState(new NextState(name));
		} else if (key.compareTo("evaluate") == 0) {
			if (DMTLDebugger.debug) {
				System.out.println("        evaluate...");
			}

			//Set the current processing state
			stateInfo.setState(DMTLMemoryState.EVALUATE);

			//Get attributes
			variable = atts.getValue(0);
			operator = atts.getValue(1);
			value = atts.getValue(2);

			//Construct element within current state
			currentstate.addEvaluate(new Evaluate(variable, operator, value));
		}
	}


	/**
	 *  Adds a Signal element to the knowledge base
	 *
	 *@param  s  The Signal element
	 */
	public void addSignal(Signal s) {
		currentstate.addSignal(s);
	}


	/**
	 *  Adds an Evaluate element to the knowledge base
	 *
	 *@param  e  The Evaluate element
	 */
	public void addEvaluate(Evaluate e) {
		currentstate.addEvaluate(e);
	}


	/**
	 *  Parser calls this when encountering a closing tag
	 *
	 *@param  namespaceURI  Used to resolve the namespace of the element
	 *@param  localName     The local name of the element
	 *@param  rawName       The resolved name of the element
	 */
	public void endElement(String namespaceURI, String localName,
			String rawName) {
		String key = rawName;
		WordChain tempwc;
		Vector tempvec;
		int i;

		//Work out the name of the element encountered
		if (key.compareTo("dialogue") == 0) {

			//Build the knowledge base
			knowledgebase.build();
		} else if (key.compareTo("macro") == 0) {
		} else if (key.compareTo("macros") == 0) {
			if (DMTLDebugger.debug) {
				System.out.println("building macros");
			}

			//Build the macros
			macros.build();
		} else if (key.compareTo("defaulttopic") == 0) {
			if (DMTLDebugger.debug) {
				System.out.println("adding default topic to knowledge base");
			}

			//Build and add to the knowledge base
			currentdefaulttopic.build();
			knowledgebase.addDefaultTopic(currentdefaulttopic);
			stateInfo.setIfDefaultTopic(false);
		} else if (key.compareTo("topic") == 0) {
			if (DMTLDebugger.debug) {
				System.out.println("adding topic to knowledge base");
			}

			//Build and add to the knowledge base
			currenttopic.build();
			knowledgebase.addTopic(currenttopic);
		} else if (key.compareTo("subtopic") == 0) {
			if (DMTLDebugger.debug) {
				System.out.print("adding subtopic ");
			}
			SubTopic tmpsubtopic;

			//Build
			currentsubtopic.build();

			//If subtopic within another subtopic
			if (subtopicstack.size() >= 2) {

				//Take out last subtopic
				subtopicstack.pop();
				tmpsubtopic = (SubTopic) subtopicstack.pop();

				//Nest sub topic
				tmpsubtopic.addSubTopic(currentsubtopic);
				currentsubtopic = tmpsubtopic;

				//Put back into stack
				subtopicstack.push(currentsubtopic);
				if (DMTLDebugger.debug) {
					System.out.println("to subtopic");
				}
			} else {
				if (DMTLDebugger.debug) {
					System.out.println("to topic");
				}

				//Is a root subtopic
				for (i = 0; i < subtopicstack.size(); i++) {
					subtopicstack.pop();
				}

				//Add to current topic
				currenttopic.addSubTopic(currentsubtopic);
			}
		} else if (key.compareTo("state") == 0) {
			if (DMTLDebugger.debug) {
				System.out.println("adding state ");
			}

			//Build
			currentstate.build();

			//If state of a default topic
			if (stateInfo.getIfDefaultTopic()) {
				if (DMTLDebugger.debug) {
					System.out.println("  to default topic");
				}

				//Add to default topic
				currentdefaulttopic.addState(currentstate);
			} else {
				if (DMTLDebugger.debug) {
					System.out.println("  to subtopic");
				}

				//Else add to current subtopic
				currentsubtopic.addState(currentstate);
			}
		} else if (key.compareTo("stimulus") == 0) {
			if (DMTLDebugger.debug) {
				System.out.print("adding stimulus " + tempchar + " ");
			}

			//If stimulus of macro
			if (stateInfo.getIfMacro()) {
				if (DMTLDebugger.debug) {
					System.out.println("to macro");
				}

				//Add to macro object
				macros.addMacro(stateInfo.getMacroName(), tempchar.trim());
			} else {
				if (DMTLDebugger.debug) {
					System.out.println("to state");
				}

				//Else is part of a state and is TEXT (natural language)
				if (stateInfo.getStimulusType() == InteractionManager.TEXT) {
					tempvec = new Vector();

					//Perform macro substitution on stimulus
					tempvec = macros.substitute(tempchar.trim());

					//Add expanded stimuli to current state
					for (i = 0; i < tempvec.size(); i++) {
						currentstimulus = new Stimulus((String) tempvec.elementAt(i), stateInfo.getStimulusType());
						currentstate.addStimulus(currentstimulus);
					}
				} else {
					//Else is part of a state and is hypertext stimulus (or something different)
					currentstimulus = new Stimulus(tempchar.trim(), stateInfo.getStimulusType());
					currentstate.addStimulus(currentstimulus);
				}
			}
		} else if (key.compareTo("response") == 0) {

			//Add to the current state
			currentstate.addResponse(new Response(tempchar.trim(), stateInfo.getResponseReference(), stateInfo.getResponseType()));
		} else if (key.compareTo("evaluate") == 0) {
		} else if (key.compareTo("prestate") == 0) {
		} else if (key.compareTo("nextstate") == 0) {
		}
	}
}

