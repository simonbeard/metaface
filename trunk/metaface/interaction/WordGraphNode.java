package metaface.interaction;

import java.util.*;

/**
 *  An individual word node of a WordGraph
 *
 *@author     Simon Beard 
 *@version    1.0  
 *@see        WordGraph
 */
class WordGraphNode {
	/** The word string of the node*/
	private String word;
	/** The weight of the word*/
	private float weight;
	/** The children word strings attached to this node*/	
	private Vector childrenwords;
	/** The weights of the children words attached to this node*/
	private Vector childrenweights;


	/**
	 *  Constructor for the WordGraphNode object
	 *
	 *@param  w  The word string of the node
	 */
	public WordGraphNode(String w) {
		word = new String(w);
		weight = 1.0f;
		childrenwords = new Vector();
		childrenweights = new Vector();
	}


	/**
	 *  Adds a weight to the weight of the WordGraphNode
	 *
	 *@param  w  The weight to add
	 */
	public void addWeight(float w) {
		weight = weight + w;
	}


	/**
	 *  Sets the weight of the WordGraphNode
	 *
	 *@param  w  The new weight value
	 */
	public void setWeight(float w) {
		weight = w;
	}


	/**
	 *  Adds a child to the WordGraphNode
	 *
	 *@param  childword    The word string of the child
	 *@param  childweight  The word weight of the child
	 */
	public void addChild(String childword, float childweight) {
		childrenwords.addElement(childword);
		childrenweights.addElement(new Float(childweight));
	}


	/**
	 *  Gets the word attribute of the WordGraphNode
	 *
	 *@return    The word string
	 */
	public String getWord() {
		return word;
	}


	/**
	 *  Gets the weight attribute of the WordGraphNode
	 *
	 *@return    The weight value
	 */
	public float getWeight() {
		return weight;
	}


	/**
	 *  Gets a child's word of the WordGraphNode
	 *
	 *@param  i  The index of the child
	 *@return    The child's word
	 */
	public String getChildrenWords(int i) {
		return (String) (childrenwords.elementAt(i));
	}


	/**
	 *  Adds a weight to a child's weight of the WordGraphNode
	 *
	 *@param  i  The index of the child
	 *@param  w  The weight to add
	 */
	public void addToChildWeight(int i, float w) {
		float f = ((Float) childrenweights.remove(i)).floatValue();
		childrenweights.add(i, new Float(f + w));
	}


	/**
	 *  Sets the weight of a child's of the WordGraphNode
	 *
	 *@param  i  The index of the child
	 *@param  w  The weight
	 */
	public void setChildWeight(int i, float w) {
		childrenweights.remove(i);
		childrenweights.insertElementAt(new Float(w), i);
	}


	/**
	 *  Gets a child's weight attribute of the WordGraphNode object
	 *
	 *@param  i  The index of the child
	 *@return    The child's weight
	 */
	public float getChildrenWeights(int i) {
		return ((Float) (childrenweights.elementAt(i))).floatValue();
	}


	/**
	 *  The number of children of the WordGraphNode
	 *
	 *@return    The number of children
	 */
	public int numChildren() {
		return childrenwords.size();
	}
}

