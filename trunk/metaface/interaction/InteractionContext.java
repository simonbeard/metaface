package metaface.interaction;

import java.util.*;
import java.io.*;
/**
 *  A utility class for keeping the current interaction context
 *
 *@author     Simon Beard
 *@version    1.0
 *@see        InteractionManager
 */
public class InteractionContext {
	/**
	 *  The states that are linked via "nextstates" to the current state
	 */
	public static Vector linked = new Vector();
	/**
	 *  Previous paths in which a state response was found
	 */
	public static Stack previousstatepaths = new Stack();
	/**
	 * The current processing type (e.g. InteractionManager.ENTRY)
	 */
	public static int processingtype = 0;
	/**
	 * The current stimulus type (e.g. InteractionManager.HYPERTEXT)
	 */
	public static int stimulustype = 0;
	/**
	 * The current stimulus string
	 */
	public static String stimulus = "";


	/**
	 *  Constructor for the InteractionContext object
	 */
	public InteractionContext() { }
}
