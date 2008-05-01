package metaface.interaction;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;
import java.io.*;
/**
 *  DMTLParser takes DMTL documents and transforms them into an internal
 *  storage format (KnowledgeBase). This format is then used by the
 *  InteractionManager. DMTL is defined by the following DMTL structure: 
 *  <IMG SRC="../../resources/DMTL_h.jpg"/><BR/>
 *  <IMG SRC="../../resources/DMTL_a.jpg"/><BR/>
 *  See the DMTL <A HREF="../../resources/DMTL.dtd">DTD</a> or and 
 *  <A HREF="../../resources/dmtl_example.xml">example file</a> .
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        InteractionManager
 *@see        DMTLTagReader
 *@see        KnowledgeBase
 */
public class DMTLParser {

	/**
	 *  The DMTL files in memory
	 */
	private Vector knowledgebases;
	/**
	 *  The SAX tag parser
	 */
	private DMTLTagReader dmtltagreader = null;
	/**
	 *  The macros within the DMTL files
	 */
	private Macros macros;


	/**
	 *  Creates new DMTLMemoryParser
	 *
	 *@param  filenames  The DMTL files to parse
	 */
	public DMTLParser(Vector filenames) {
		int i;
		boolean validation = true;
		int numFiles = filenames.size();

		knowledgebases = new Vector();

		// Set up the parser
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setValidating(validation);
		XMLReader xmlReader = null;

		try {

			// Create a JAXP SAXParser
			SAXParser saxParser = spf.newSAXParser();

			// Get the encapsulated SAX XMLReader
			xmlReader = saxParser.getXMLReader();
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}

		// Set an ErrorHandler before parsing
		xmlReader.setErrorHandler(new DMTLErrorHandler(System.err));
		try {

			// Tell the XMLReader to parse the XML document
			for (i = 0; i < numFiles; i++) {
				if (DMTLDebugger.debug) {
					System.out.println("parsing file " + (String) filenames.elementAt(i));
				}

				// Set the ContentHandler of the XMLReader
				dmtltagreader = new DMTLTagReader((String) filenames.elementAt(i));
				xmlReader.setContentHandler(dmtltagreader);
				xmlReader.parse(convertToFileURL((String) filenames.elementAt(i)));
				knowledgebases.add(dmtltagreader.getKnowledgeBase());
			}
		} catch (SAXException e) {
			System.err.println("XML error parsing DMTL file");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IO error parsing DMTL file");
			e.printStackTrace();
		}
	}


	/**
	 *  Constructor for the DMTLParser object
	 */
	public DMTLParser() { }


	/**
	 *  Gets the knowledge bases parsed from the DMTL documents
	 *
	 *@return    The knowledge bases
	 */
	public Vector getKnowledgeBases() {
		return (knowledgebases);
	}


	/**
	 *  Convert from a filename to a file URL.
	 *
	 *@param  filename  The filename to turn to an URL
	 *@return           The URL
	 */
	public static String convertToFileURL(String filename) {
		String path = new File(filename).getAbsolutePath();
		if (File.separatorChar != '/') {
			path = path.replace(File.separatorChar, '/');
		}
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		return "file:" + path;
	}
}
