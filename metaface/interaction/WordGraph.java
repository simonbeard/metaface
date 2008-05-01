package metaface.interaction;

import java.util.*;
/**
 *  WordGraph is responsible for building word graphs from text stimulus and
 *  matching these graphs against text stimulus.<P>
 *
 *  For example, consider the following questions: <menu>
 *  <li> what is vhml?
 *  <li> what is the virtual human markup language?
 *  <li> tell me about vhml?
 *  <li> describe vhml for me?
 *  <li> tell me about the virtual human markup language? </menu> <p>
 *
 *  These questions should all result in the same answer, and therefore they can
 *  be combined into one word graph. The word graph nodes and arcs can also be
 *  weighed according to how often they occur in the text stimulus. Unimportant
 *  words such as a, an, and, the, of, is, are, was, etc. are give low weights.
 *  <img src="../../resources/wordgraph.jpg"> <p>
 *
 *  The word graph can now be compared to a user's text stimulus to examine how
 *  well it matches. A numerical score can be given due to the weights present
 *  in the word graph.
 *
 *@author     Simon Beard 
 *@version    1.0
 */
public class WordGraph extends java.lang.Object {
	/**
	 *  A list of subgraphs
	 */
	private Vector graphs;
	/**
	 *  A chain of words representing the current stimulus
	 */
	private WordChain wc;
	/**
	 *  The size of the graph
	 */
	private int graphsize;
	/**
	 *  Words to ignore when constructing a word graph
	 */
	private String unimportant = "a, an, and, the, of, is, are, was";


	/**
	 *  Creates a new WordGraph
	 *
	 *@param  stimulus  A list of textual stimulus representing the same concept
	 */
	public WordGraph(Vector stimulus) {
		int start;
		int finish;
		int i;
		int j;
		int k;
		int l;
		int index;
		int temp;
		boolean[] eliminatelist;
		boolean match;
		boolean matchchild;
		int lastgraphsize;
		String tempstr;

		if (DMTLDebugger.debug) {
			System.out.print("building word graph... ");
		}
		graphs = new Vector();

		//for chars in stimulus
		for (i = 0; i < stimulus.size(); i++) {
			tempstr = ((Stimulus) (stimulus.elementAt(i))).getStimulus();

			//construct chain from stimulus
			wc = new WordChain(tempstr);
			if (wc.size() > 2) {

				//if first stimulus (just add all to graph)
				if (graphs.size() == 0) {

					//add nodes
					for (j = 0; j < wc.size() - 1; j++) {
						graphs.add(new WordGraphNode(wc.getWord(j)));
						graphsize++;
						((WordGraphNode) graphs.elementAt(graphs.size() - 1)).
								addChild(wc.getWord(j + 1), 1.0f);
					}
				}
				//else not the first stimulus
				else {

					//construct elimination list for double words
					lastgraphsize = graphsize;
					eliminatelist = new boolean[lastgraphsize];
					for (j = 0; j < lastgraphsize; j++) {
						eliminatelist[j] = false;
					}

					//for each word in chain
					for (j = 0; j < wc.size() - 1; j++) {

						//search for match in wordgraph
						k = 0;
						match = false;

						while ((!match) && (k < lastgraphsize
						/*
						 *  graphs.size()
						 */
								)) {

							//if match
							if ((wc.getWord(j).compareTo(((WordGraphNode) graphs.elementAt(k)).getWord()) == 0)
									 && (eliminatelist[k] == false)) {

								//eliminate word and update match and weight
								eliminatelist[k] = true;
								match = true;
								((WordGraphNode) graphs.elementAt(k)).addWeight(1.0f);

								//search for match in children
								l = 0;
								matchchild = false;

								while ((matchchild == false) &&
										(l < ((WordGraphNode) graphs.elementAt(k)).numChildren())) {

									//if child match
									if (((WordGraphNode) graphs.elementAt(k)).getChildrenWords(l).
											compareTo(wc.getWord(j + 1)) == 0) {

										//update match and weight
										matchchild = true;
										((WordGraphNode) graphs.elementAt(k)).
												addToChildWeight(l, 1.0f);
									}
									l++;
								}

								//if there was no match
								if (matchchild == false) {

									//add child to end of children list
									((WordGraphNode) graphs.elementAt(k)).
											addChild(wc.getWord(j + 1), 1.0f);
								}
							}
							k++;
						}

						//if no match was found
						if (!match) {

							//add word to end of graph
							graphs.add(new WordGraphNode(wc.getWord(j)));
							graphsize++;
							((WordGraphNode) graphs.elementAt(graphs.size() - 1)).
									addChild(wc.getWord(j + 1), 1.0f);
						}
					}
				}
			}
		}
		if (DMTLDebugger.debug) {
			System.out.println("normalising ...");
		}
		normalise();
	}


	/**
	 *  Normalise the weighting of nodes and connections
	 */
	private void normalise() {
		int i;
		int j;
		float wordweights;
		float connectweights;

		wordweights = 0;
		connectweights = 0;

		//get totals of word weights and connectin weights for normalising
		if (graphs.size() >= 1) {
			((WordGraphNode) graphs.elementAt(0)).setWeight(0.0f);
		}
		for (i = 0; i < graphs.size(); i++) {
			if (unimportant.indexOf(((WordGraphNode) graphs.elementAt(i)).getWord()) == -1) {
				wordweights = wordweights + ((WordGraphNode) graphs.elementAt(i)).getWeight();
			}
			for (j = 0; j < ((WordGraphNode) graphs.elementAt(i)).numChildren(); j++) {
				connectweights = connectweights + ((WordGraphNode) graphs.elementAt(i)).getChildrenWeights(j);
			}
		}
		for (i = 0; i < graphs.size(); i++) {
			if (unimportant.indexOf(((WordGraphNode) graphs.elementAt(i)).getWord()) == -1) {
				//((WordGraphNode)graphs.elementAt(i)).setWeight ((100/wordweights) * ((WordGraphNode)graphs.elementAt(i)).getWeight());
				((WordGraphNode) graphs.elementAt(i)).setWeight(1);
				for (j = 0; j < ((WordGraphNode) graphs.elementAt(i)).numChildren(); j++) {
					//((WordGraphNode)graphs.elementAt(i)).setChildWeight (j, (100/connectweights) * ((WordGraphNode)graphs.elementAt(i)).getChildrenWeights(j));
					if (unimportant.indexOf(((WordGraphNode) graphs.elementAt(i)).getChildrenWords(j)) == -1) {
						((WordGraphNode) graphs.elementAt(i)).setChildWeight(j, 0.5f);
					} else {
						((WordGraphNode) graphs.elementAt(i)).setChildWeight(j, 0.25f);
					}
				}
			} else {
				((WordGraphNode) graphs.elementAt(i)).setWeight(0f);
				for (j = 0; j < ((WordGraphNode) graphs.elementAt(i)).numChildren(); j++) {
					((WordGraphNode) graphs.elementAt(i)).setChildWeight(j, 0.25f);
				}
			}
		}
	}


	/**
	 *  Perfom a match with this WordGraph and a user's stimulus
	 *
	 *@param  stimulus  The user's stimulus to match against
	 *@return           A score of how well the user's stimulus matches the
	 *      WordGraph
	 */
	public float match(String stimulus) {
		int i;
		int j;
		int k;
		float score = 0;
		WordGraphNode tmpwordgraphnode;
		WordChain input;
		boolean[] eliminated;
		boolean[] eliminated2;

		if (DMTLDebugger.debug) {
			System.out.print("->Matching");
		}

		//Initialise
		eliminated = new boolean[1000];
		eliminated2 = new boolean[1000];
		for (i = 0; i < 1000; i++) {
			eliminated[i] = false;
			eliminated2[i] = false;
		}

		//Break user's stimulus into words
		input = new WordChain(stimulus);

		//For each subgraph
		for (i = 0; i < graphs.size(); i++) {
			tmpwordgraphnode = (WordGraphNode) graphs.elementAt(i);

			//For each stimulus word
			for (k = 0; k < input.size() - 1; k++) {

				//Match with a subgraph word
				if ((tmpwordgraphnode.getWord().equalsIgnoreCase(input.getWord(k))) && (!eliminated[i]) && (!eliminated2[k])) {
					if (DMTLDebugger.debug) {
						System.out.print(" " + tmpwordgraphnode.getWord() + " " + tmpwordgraphnode.getWeight() + " ");
					}

					//If word not aready matched
					eliminated[i] = true;
					eliminated2[k] = true;
					if (k != 0) {

						//Add weight to score
						score = score + tmpwordgraphnode.getWeight();
					}

					//Match with children for connection match
					if (k < input.size() - 1) {
						for (j = 0; j < tmpwordgraphnode.numChildren(); j++) {
							if (tmpwordgraphnode.getChildrenWords(j).equalsIgnoreCase(input.getWord(k + 1))) {
								if (DMTLDebugger.debug) {
									System.out.print(" " + tmpwordgraphnode.getChildrenWords(j) + " " + tmpwordgraphnode.getChildrenWeights(j) + " ");
								}

								//Add connection weight to score
								score = score + tmpwordgraphnode.getChildrenWeights(j);
								j = tmpwordgraphnode.numChildren();
							}
						}
					}

					//Reset for next iteration of subgraph
					k = input.size();
				}
			}
		}
		if (DMTLDebugger.debug) {
			System.out.println("");
		}
		return (score);
	}


	/**
	 *  Utility method for constructing and XML representation of the WordGraph
	 *
	 *@return    The XML representation
	 */
	public String toXMLString() {
		String buffer;
		int i;
		int j;

		buffer = new String("");

		for (i = 0; i < graphs.size(); i++) {
			buffer = buffer.concat("\t\t<wordnode word=\"");
			buffer = buffer.concat(((WordGraphNode) graphs.elementAt(i)).getWord());
			buffer = buffer.concat("\" weight=\"");
			buffer = buffer.concat(Float.toString(((WordGraphNode) graphs.elementAt(i)).getWeight()));
			buffer = buffer.concat("\">\n");

			for (j = 0; j < ((WordGraphNode) graphs.elementAt(i)).numChildren(); j++) {
				buffer = buffer.concat("\t\t\t<connectedto word=\"");
				buffer = buffer.concat(((WordGraphNode) graphs.elementAt(i)).getChildrenWords(j));
				buffer = buffer.concat("\" weight=\"");
				buffer = buffer.concat(Float.toString(((WordGraphNode) graphs.elementAt(i)).getChildrenWeights(j)));
				buffer = buffer.concat("\"/>\n");
			}

			buffer = buffer.concat("\t\t</wordnode>\n");
		}

		return buffer;
	}
}

