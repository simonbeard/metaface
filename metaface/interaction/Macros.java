package metaface.interaction;

import java.util.*;
import java.io.*;
/**
 *  DMTL makes use of “stimulus” elements to provide a match to user stimuli,
 *  such as a typed question. Using editorial brackets [ ] within the XML data
 *  section specifies that text can be substituted, and parentheses ( ) are used
 *  to identify the text to be substituted. These “macro” elements are used
 *  further in the DMTL document by “state” elements which are the actual
 *  mechanism used to match a user’s query. The use of macros allows quick
 *  construction of a knowledge base through reuse, and the ability to hide
 *  complexity.<p>
 *
 *  Care has to be taken when naming macros, as their recursive nature leads to
 *  implementation issues and errors; DMTL uses just straight text substitution
 *  to expand macros. Constructing a macro with a name that is a sub string of
 *  another macro will cause erroneous substitution.
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        InteractionManager
 */
public class Macros {
	/**
	 *  A table of macros
	 */
	Hashtable macros;


	/**
	 *  Constructor for the Macros object
	 */
	public Macros() {
		macros = new Hashtable();
	}


	/**
	 *  Adds a macro to the Macros object
	 *
	 *@param  name         Name of the macro to be added
	 *@param  macrostring  The macro string
	 */
	public void addMacro(String name, String macrostring) {
		Vector macrovector;

		//If in table then add new macro string to list
		if (macros.containsKey(name)) {
			macrovector = (Vector) (macros.get(name));
			macrovector.add(macrostring);
			macros.put(name, macrovector);
		} else {

			//Make a new macro and add to table
			macrovector = new Vector();
			macrovector.add(macrostring);
			macros.put(name, macrovector);
		}
	}


	/**
	 *  Utility method for debugging. This method displays a macro's strings on
	 *  "System.out"
	 *
	 *@param  name  Name of the macro to display
	 */
	public void printMacro(String name) {
		Vector macroarray;
		int i;

		macroarray = new Vector();
		macroarray = (Vector) (macros.get(name));
		if (macroarray == null) {
			System.out.println("macro not found: " + name);
		} else {
			for (i = 0; i < macroarray.size(); i++) {
				System.out.println((String) macroarray.elementAt(i));
			}
		}
	}


	/**
	 *  Utility method for debugging. This method displays the entire macro table
	 *  on "System.out"
	 */
	public void printMacroTable() {
		Enumeration keys;
		String macroname;
		Vector macroarray;
		int i;

		keys = macros.keys();
		while (keys.hasMoreElements()) {
			macroname = (String) (keys.nextElement());
			macroarray = (Vector) (macros.get(macroname));
			System.out.println(macroname);
			for (i = 0; i < macroarray.size(); i++) {
				System.out.println("    " + (String) (macroarray.elementAt(i)));
			}
		}
	}


	/**
	 *  Perform substiution using the stored macros of this oject on a text string
	 *
	 *@param  stimulus  The text string to perform substitution of macros
	 *@return           The modified and expanded stimulus strings
	 */
	public Vector substitute(String stimulus) {
		Enumeration keys;
		Enumeration keys2;
		String macroname;
		String macroname2;
		String subst_string = "[]";
		String tempstring;
		String tempstring2;
		String tempstring3;
		String contents;
		Vector macroarray;
		Vector macroarray2;
		boolean match;
		boolean bracketfound;
		int i;
		int start;
		int finish;
		int j;
		int k;
		int l;
		int lastindex;
		int index;
		int index2;
		int bracketcount;
		int bracketat;
		Vector stimulusvector;

		// Perform macro substitution on stimulus
		stimulusvector = new Vector();
		stimulusvector.add(stimulus);

		// For each stimulus
		for (i = 0; i < stimulusvector.size(); i++) {

			// Check if this stimulus contains this macro
			keys = macros.keys();
			while (keys.hasMoreElements()) {

				macroname = (String) (keys.nextElement());

				// Do string check
				j = 0;
				index = ((String) (stimulusvector.elementAt(i))).indexOf(macroname, j);
				j = index + 1;
				while (index != -1) {

					// There is a macro match, get macro array
					macroarray = (Vector) (macros.get(macroname));

					// If there are brackets -- do different substitution
					bracketfound = false;
					if (((String) (stimulusvector.elementAt(i))).length() > j + macroname.length()) {
						if (((String) (stimulusvector.elementAt(i))).charAt(j + macroname.length() - 1) == '(') {
							bracketfound = true;
						}
					}
					if (bracketfound) {

						// Search for closing bracket
						bracketcount = 0;
						bracketfound = false;
						bracketat = 0;
						for (l = j + macroname.length(); l < ((String) (stimulusvector.elementAt(i))).length(); l++) {
							if ((((String) (stimulusvector.elementAt(i))).charAt(l) == ')')
									 && (bracketcount == 0)) {
								bracketat = l;
								l = ((String) (stimulusvector.elementAt(i))).length();
								bracketfound = true;
							} else if (((String) (stimulusvector.elementAt(i))).charAt(l) == '(') {
								bracketcount++;
							} else if (((String) (stimulusvector.elementAt(i))).charAt(l) == ')') {
								bracketcount--;
							}
						}
						if (bracketfound) {
							tempstring = (String) (stimulusvector.remove(i));
							if (j + macroname.length() < bracketat) {
								contents = new String(tempstring.substring(j + macroname.length(), bracketat));
							} else {
								contents = new String("");
							}
							for (k = 0; k < macroarray.size(); k++) {
								tempstring2 = new String(((String) macroarray.elementAt(k)));
								l = 0;
								tempstring3 = new String(tempstring.substring(0, index));
								index2 = tempstring2.indexOf(subst_string, l);
								l = index2 + 1;
								lastindex = 0;
								while (index2 != -1) {
									tempstring3 = tempstring3.concat(new String(tempstring2.substring(lastindex, index2)));
									tempstring3 = tempstring3.concat(contents);

									lastindex = index2 + subst_string.length();
									index2 = tempstring2.indexOf(subst_string, l);
									l = index2 + 1;
								}
								if (lastindex < tempstring2.length()) {
									tempstring3 = tempstring3.concat(new String(tempstring2.substring(lastindex)));
								}
								if (index + macroname.length() + contents.length() + 2 < tempstring.length()) {
									tempstring3 = tempstring3.concat(tempstring.substring(index + macroname.length() + contents.length() + 2));
								}
								stimulusvector.insertElementAt(tempstring3, i);
							}
						} else {

							// Substitute macro
							tempstring = (String) (stimulusvector.remove(i));
							for (k = 0; k < macroarray.size(); k++) {
								tempstring2 = new String(tempstring.substring(0, index));
								tempstring2 = tempstring2.concat((String) (macroarray.elementAt(k)));
								if (index + macroname.length() < tempstring.length()) {
									tempstring2 = tempstring2.concat(tempstring.substring(index + macroname.length()));
								}
								stimulusvector.insertElementAt(tempstring2, i);
							}
						}
					} else {

						// Substitute macro
						tempstring = (String) (stimulusvector.remove(i));
						for (k = 0; k < macroarray.size(); k++) {
							tempstring2 = new String(tempstring.substring(0, index));
							tempstring2 = tempstring2.concat((String) (macroarray.elementAt(k)));
							if (index + macroname.length() < tempstring.length()) {
								tempstring2 = tempstring2.concat(tempstring.substring(index + macroname.length()));
							}
							stimulusvector.insertElementAt(tempstring2, i);
						}
					}

					// Keep checking
					index = ((String) (stimulusvector.elementAt(i))).indexOf(macroname, j);
					j = index + 1;
				}
			}
		}
		return (stimulusvector);
	}


	/**
	 *  Build the macro table after all macros have been added
	 */
	public void build() {
		Enumeration keys;
		Enumeration keys2;
		String macroname;
		String macroname2;
		String subst_string = "[]";
		String tempstring;
		String tempstring2;
		String tempstring3;
		String contents;
		Vector macroarray;
		Vector macroarray2;
		boolean match;
		boolean bracketfound;
		int i;
		int start;
		int finish;
		int j;
		int k;
		int l;
		int lastindex;
		int index;
		int index2;
		int bracketcount;
		int bracketat;

		subst_string = new String("[]");
		keys = macros.keys();
		while (keys.hasMoreElements()) {
			macroname = (String) (keys.nextElement());
			macroarray = (Vector) (macros.get(macroname));

			// For each stimulus in macro
			for (i = 0; i < macroarray.size(); i++) {
				match = false;

				// Check if this macro contains other macros
				keys2 = macros.keys();
				while (keys2.hasMoreElements()) {
					macroname2 = (String) (keys2.nextElement());

					// Do string check
					j = 0;
					index = ((String) (macroarray.elementAt(i))).indexOf(macroname2, j);
					j = index + 1;
					while (index != -1) {

						// There is a macro match, get macro array
						match = true;
						macroarray2 = (Vector) (macros.get(macroname2));

						// If there are brackets -- do different substitution
						bracketfound = false;
						if (((String) (macroarray.elementAt(i))).length() > j + macroname2.length()) {
							if (((String) (macroarray.elementAt(i))).charAt(j + macroname2.length() - 1) == '(') {
								bracketfound = true;
							}
						}
						if (bracketfound) {

							//search for closing bracket
							bracketcount = 0;
							bracketfound = false;
							bracketat = 0;
							for (l = j + macroname2.length(); l < ((String) (macroarray.elementAt(i))).length(); l++) {
								if ((((String) (macroarray.elementAt(i))).charAt(l) == ')')
										 && (bracketcount == 0)) {
									bracketat = l;
									l = ((String) (macroarray.elementAt(i))).length();
									bracketfound = true;
								} else if (((String) (macroarray.elementAt(i))).charAt(l) == '(') {
									bracketcount++;
								} else if (((String) (macroarray.elementAt(i))).charAt(l) == ')') {
									bracketcount--;
								}
							}
							if (bracketfound) {
								tempstring = (String) (macroarray.remove(i));
								if (j + macroname2.length() < bracketat) {
									contents = new String(tempstring.substring(j + macroname2.length(), bracketat));
								} else {
									contents = new String("");
								}
								for (k = 0; k < macroarray2.size(); k++) {
									tempstring2 = new String(((String) macroarray2.elementAt(k)));
									l = 0;
									tempstring3 = new String(tempstring.substring(0, index));
									index2 = tempstring2.indexOf(subst_string, l);
									l = index2 + 1;
									lastindex = 0;
									while (index2 != -1) {
										tempstring3 = tempstring3.concat(new String(tempstring2.substring(lastindex, index2)));
										tempstring3 = tempstring3.concat(contents);

										lastindex = index2 + subst_string.length();
										index2 = tempstring2.indexOf(subst_string, l);
										l = index2 + 1;
									}
									if (lastindex < tempstring2.length()) {
										tempstring3 = tempstring3.concat(new String(tempstring2.substring(lastindex)));
									}
									if (index + macroname2.length() + contents.length() + 2 < tempstring.length()) {
										tempstring3 = tempstring3.concat(tempstring.substring(index + macroname2.length() + contents.length() + 2));
									}
									macroarray.insertElementAt(tempstring3, i);
								}
							} else {

								// For original macro -- substitute other macro
								tempstring = (String) (macroarray.remove(i));
								for (k = 0; k < macroarray2.size(); k++) {
									tempstring2 = new String(tempstring.substring(0, index));
									tempstring2 = tempstring2.concat((String) (macroarray2.elementAt(k)));
									if (index + macroname2.length() < tempstring.length()) {
										tempstring2 = tempstring2.concat(tempstring.substring(index + macroname2.length()));
									}
									macroarray.insertElementAt(tempstring2, i);
								}
							}
						} else {

							// For original macro -- substitute other macro
							tempstring = (String) (macroarray.remove(i));
							for (k = 0; k < macroarray2.size(); k++) {
								tempstring2 = new String(tempstring.substring(0, index));
								tempstring2 = tempstring2.concat((String) (macroarray2.elementAt(k)));
								if (index + macroname2.length() < tempstring.length()) {
									tempstring2 = tempstring2.concat(tempstring.substring(index + macroname2.length()));
								}
								macroarray.insertElementAt(tempstring2, i);
							}
						}

						index = ((String) (macroarray.elementAt(i))).indexOf(macroname2, j);
						j = index + 1;
					}
				}

				// If there was a match, decrement to process same element again (do this until all macros resolved)
				if (match) {
					i--;
					match = false;
				}
			}
		}
	}
}
