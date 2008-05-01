package metaface.interaction;

import java.util.*;
import java.io.*;
/**
 *  This is a utility class for testing and debugging DMTL files on the command
 *  line
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        InteractionManager
 */
public class DMTLDebugger {

	/**
	 *  Set this element to output debugging information of package to "System.out"
	 */
	public static boolean debug = false;
	/**
	 *  The interaction manager (for natural language and hypertext interaction)
	 */
	private InteractionManager im;
	/**
	 *  Parser for DMTL (XML based) documents
	 */
	private DMTLParser dmtlparser;
	/**
	 *  User input
	 */
	private String input;
	/**
	 *  Response output to user
	 */
	private String output;
	/**
	 *  Buffer for reading response files
	 */
	private byte[] buffer;
	/**
	 *  List of files (from command line)
	 */
	private Vector tempvec;


	/**
	 *  Constructor for the DMTLDebugger object
	 *
	 *@param  args  Command line arguments
	 */
	public DMTLDebugger(String[] args) {
		int i;
		FileReader fr;
		char[] buffer2;

		System.out.println("================================================================================");
		System.out.println("Welcome to the MetaFace Framework Dialogue Manager Tool Language (DMTL) Debugger");
		System.out.println("================================================================================");
		System.out.println("\n\n\n");
		System.out.println("Creating Interaction Manager...");
		im = new InteractionManager();
		System.out.println("...done");
		System.out.println("Creating DMTL Parser...");
		if (args.length == 2) {
			debug = true;
			tempvec = new Vector();
			tempvec.add(args[1]);
		} else {
			debug = false;
			tempvec = new Vector();
			tempvec.add(args[0]);
		}

		//Parse DMTL files
		dmtlparser = new DMTLParser(tempvec);
		System.out.println("...done");
		System.out.println("Handing Knowledge Base to Interaction Manager...");

		//Add knowledgebases to interaction manager
		im.addKnowledgeBase((KnowledgeBase) dmtlparser.getKnowledgeBases().elementAt(0));

		//Set the path for retrieving response files
		im.setBasePath("../Applications/VHMLFiles/");
		System.out.println("...done");
		System.out.println("\n\n\n");
		System.out.println("================================================================================");
		System.out.println("Running Interaction Manager, type exit to quit program");
		System.out.println("================================================================================");
		input = "";
		buffer = new byte[1000];
		System.out.println("Enter Text: ");

		//Get input from the user
		while (input.indexOf("exit") != 0) {
			try {
				i = System.in.read(buffer);
				input = new String(buffer, 0, i);
				input = input.trim();
				if (input.indexOf("exit") != 0) {

					//Process the input and get a response
					output = im.processUserStimulus(input);
					System.out.println(output);
					if ((output.indexOf(".xml") > 0) || (output.indexOf(".XML") > 0)) {
						try {
							fr = new FileReader(output);
							buffer2 = new char[1000];
							i = fr.read(buffer2, 0, 1000);
							while (i > 0) {
								System.out.print((new String(buffer, 0, i)));
								i = fr.read(buffer2, 0, 1000);
							}
						} catch (FileNotFoundException e) {
							System.err.println("Could not find file " + output);
							e.printStackTrace();
						} catch (IOException e) {
							System.err.println("Could read from file " + output);
							e.printStackTrace();
						}
					}
				}
				System.out.println("Enter Text: ");
			} catch (Exception e) {
				System.err.println("Error with dialogue management");
				e.printStackTrace();
			}
		}
		System.out.println("\n\n\n");
		System.out.println("================================================================================");
		System.out.println("Testing and debugging complete... Bye");
		System.out.println("================================================================================");
	}


	/**
	 *  The main program for the DMTLDebugger class
	 *
	 *@param  args  The command line arguments
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("USAGE: java DMTLDebugger [v - verbose] filename");
			System.out.println("EXAMPLE: c:\\j2sdk1.4.1\\bin\\java -classpath ./InteractionManager.jar;. DMTLDebugger v c:\\dmtl\\example.dmtl");
		} else {
			new DMTLDebugger(args);
		}
	}
}
