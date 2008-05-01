package metaface.server;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.*;
import java.io.*;
import metaface.dsp.*;
import metaface.nlp.*;
/**
 *  This class is used to construct an eXtensible Markup Language (XML) parser
 *  for a Virtual Human Markup Language (VHML)document.<p>
 *
 *  Follow this link to retrieve the Document Type Definition 
 *  <A HREF="../../resources/vhml.dtd">vhml.dtd</A> Follow this link to retrieve an
 *  example XML file <A HREF="../../resources/vhml_example.xml">
 *  vhml_example.xml</A>
 *
 *@author     Simon Beard 
 *@version    1.0 
 */
public class VHMLParser {
	private VHMLReader vhmlparser;
	private SAXParserFactory spf;
	private XMLReader xmlReader;
	private SAXParser saxParser;
	private boolean validation = true;


	/**
	 *  Constructor for the VHMLParser object
	 *
	 *@param  response_file             The VHML file to parse
	 *@param  framespersec              The frame rate for animation
	 *@param  digitalsignalprocessor    The DSP
	 *@param  naturallanguageprocessor  The NLP
	 *@param  smldtdpath                The path to the SML DTD (for SPEMO parsing)
	 */
	public VHMLParser(String response_file, int framespersec, DigitalSignalProcessor digitalsignalprocessor, NaturalLanguageProcessor naturallanguageprocessor, String smldtdpath) {

		spf = SAXParserFactory.newInstance();
		spf.setValidating(validation);
		xmlReader = null;
		try {

			// Create a JAXP SAXParser
			saxParser = spf.newSAXParser();

			// Get the encapsulated SAX XMLReader
			xmlReader = saxParser.getXMLReader();
			xmlReader.setErrorHandler(new VHMLErrorHandler(System.err));

		} catch (Exception e) {
			System.err.println("Error creating SAX parser");
			e.printStackTrace();
		}

		// Tell the XMLReader to parse the XML document
		try {
			vhmlparser = new VHMLReader(framespersec, digitalsignalprocessor, naturallanguageprocessor, smldtdpath);
			xmlReader.setContentHandler(vhmlparser);
			xmlReader.parse(convertToFileURL(response_file));
		} catch (IOException e) {
			System.err.println("IO Error whilst parsing VHML");
			e.printStackTrace();
		} catch (SAXException e) {
			System.err.println("SAX Error whilst parsing VHML");
			e.printStackTrace();
		}
	}


	/**
	 *  Convert from a filename to a file URL.
	 *
	 *@param  filename  The filename to turn to an URL
	 *@return           The URL
	 */
	private static String convertToFileURL(String filename) {
		String path = new File(filename).getAbsolutePath();
		if (File.separatorChar != '/') {
			path = path.replace(File.separatorChar, '/');
		}
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		return "file:" + path;
	}


	/**
	 *  Allows access to the MPEG-4 FAPs retrieved
	 *
	 *@return    A list of FAPData MPEG-4 FAP frames
	 */
	public Vector getFrames() {
		return (vhmlparser.getFrames());
	}


	/**
	 *  Allows access to the phonemes retrieved
	 *
	 *@return    A list of phonemes
	 */
	public Vector getPhonemes() {
		return (vhmlparser.getPhonemes());
	}


	/**
	 *  Allows access to the plain text retrieved
	 *
	 *@return    A String of characters
	 */
	public String getText() {
		return (vhmlparser.getText());
	}


	/**
	 *  Allows access to the URLs and targets retrieved
	 *
	 *@return    A table of URLs referenced by their targets as keys
	 */
	public Hashtable getBrowserUpdates() {
		return (vhmlparser.getBrowserUpdates());
	}
}

