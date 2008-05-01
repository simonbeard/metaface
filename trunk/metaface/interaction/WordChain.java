package metaface.interaction;

import java.util.*;

/**
 *  A utility class for representing strings as individual words.
 *
 *@author     Simon Beard 
 *@version    1.0
 */
public class WordChain extends java.lang.Object {
	/**
	 *  A list of words
	 */
	private Vector wc;


	/**
	 *  Creates a new WordChain
	 *
	 *@param  input_a  the string to represent as a word chain
	 */
	public WordChain(String input_a) {
		int i;
		int start;
		int finish;
		String input;

		//Initialise
		wc = new Vector();
		start = 0;
		finish = 0;
		i = 0;

		//Get rid of non-word characters
		input = removePunctuation(input_a, false);

		//Each word chain has a "_start_node_" and an "_end_node_"
		wc.add("_start_node_");

		//Search for white space and grab characters from in between
		while ((start < input.length()) && (finish < input.length())) {
			while (((input.charAt(start) == ' ') || (input.charAt(start) == '\t') || (input.charAt(start) == '\n'))
					 && (start < input.length() - 1)) {
				start++;
			}

			finish = start;
			while (((input.charAt(finish) != ' ') && (input.charAt(finish) != '\t') && (input.charAt(finish) != '\n'))
					 && (finish < input.length() - 1)) {
				finish++;
			}

			//Add new word to chain
			if (start < finish) {
				wc.add(input.substring(start, finish));
			}
			start = finish + 1;
		}

		//add end node
		wc.add("_end_node_");
	}


	/**
	 *  Creates a new WordChain
	 *
	 *@param  input_a        the string to represent as a word chain
	 *@param  caseSensitive  whether case sensitivity should be preserved
	 */
	public WordChain(String input_a, boolean caseSensitive) {
		int i;
		int start;
		int finish;
		String input;

		//Initialise
		wc = new Vector();
		start = 0;
		finish = 0;
		i = 0;

		//Get rid of non-word characters
		input = removePunctuation(input_a, caseSensitive);

		//Each word chain has a "_start_node_" and an "_end_node_"
		wc.add("_start_node_");

		//Search for white space and grab characters from in between
		while ((start < input.length()) && (finish < input.length())) {
			while (((input.charAt(start) == ' ') || (input.charAt(start) == '\t') || (input.charAt(start) == '\n'))
					 && (start < input.length() - 1)) {
				start++;
			}

			finish = start;
			while (((input.charAt(finish) != ' ') && (input.charAt(finish) != '\t') && (input.charAt(finish) != '\n'))
					 && (finish < input.length() - 1)) {
				finish++;
			}

			//Add new word to chain
			if (start < finish) {
				wc.add(input.substring(start, finish));
			}
			start = finish + 1;
		}

		//Add end node
		wc.add("_end_node_");
	}


	/**
	 *  Remove punctuation from a string
	 *
	 *@param  input          The string from which to take the punctuation
	 *@param  caseSensitive  Whether to preserve case sensitivity
	 *@return                The modified input string
	 */
	private String removePunctuation(String input, boolean caseSensitive) {
		int i;
		int x;
		char[] temp;

		temp = input.toCharArray();
		x = 0;

		//Iterate through string and replace punctuation with white space
		for (i = 0; i < input.length(); i++) {
			if ((input.charAt(i) == '.') || (input.charAt(i) == ',') || (input.charAt(i) == '?')
					 || (input.charAt(i) == ':') || (input.charAt(i) == ';') || (input.charAt(i) == '"')
					 || (input.charAt(i) == '\'') || (input.charAt(i) == '`') || (input.charAt(i) == '\n')
					 || (input.charAt(i) == '\t')) {
				if (input.charAt(i) == '\'') {
					x--;
				} else {
					temp[x] = ' ';
				}
			} else {
				if (!caseSensitive) {
					temp[x] = Character.toLowerCase(input.charAt(i));
				}
			}
			x++;
		}

		return ((new String(temp, 0, x)).concat("\n"));
	}


	/**
	 *  Gets a word from a postiion in the WordChain
	 *
	 *@param  i  The index of the word wanted
	 *@return    The word
	 */
	public String getWord(int i) {
		return ((String) wc.elementAt(i));
	}


	/**
	 *  Size of the word chain
	 *
	 *@return    The size of the word chain
	 */
	public int size() {
		return wc.size();
	}


	/**
	 *  Prints the word chain to a string
	 *
	 *@return    A string representing the word chain
	 */
	public String toString() {
		String temp;
		int i;

		temp = new String("");

		//Seperate each word by a space (leave off "_start_node_" and "_end_node_")
		for (i = 1; i < wc.size() - 1; i++) {
			temp = temp.concat(((String) wc.elementAt(i)));
			if (i < wc.size() - 2) {
				temp = temp.concat(" ");
			}
		}
		if (temp.length() <= 0) {
			return null;
		}
		return temp;
	}
}

