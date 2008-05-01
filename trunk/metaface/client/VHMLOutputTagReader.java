package metaface.client;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;
import java.io.*;
import metaface.mpeg4.*;

/**
 *  This class is used to read pre-rendered Facial Animation Parameters (FAPs),
 *  phonemes, Uniform Resource Locators (URLs) and text from an eXtensible
 *  Markup Language (XML) file. <p>
 *
 *  These files are formed during presynthesis of Virtual Human Markup Language
 *  (VHML) XML documents<p>
 *
 *  These files are in the form of: <BR/>
 *  <code>
 * &lt;eca_synthesis&gt;<br/>
 *  &nbsp;&nbsp;&lt;text&gt;the text spoken by the ECA&lt;/text&gt;<br/>
 *  &nbsp;&nbsp;&lt;url target="name.of.browser.frame"&gt;http://some.url.here&lt;/url&gt;
 *  <br/>
 *  &nbsp;&nbsp;&lt;url target="another.browser.frame"&gt;http://another.url.here&lt;/url&gt;
 *  <br/>
 *  &nbsp;&nbsp;....<br/>
 *  &nbsp;&nbsp;&lt;phonemes&gt;<br/>
 *  &nbsp;&nbsp;&nbsp;&nbsp;A list of the ECA phonemes for speech<br/>
 *  &nbsp;&nbsp;&lt;phonemes&gt;<br/>
 *  &nbsp;&nbsp;&lt;faps&gt;<br/>
 *  &nbsp;&nbsp;&nbsp;&nbsp;A list of the MPEG-4 FAPs for facial animation<br/>
 *  &nbsp;&nbsp;&lt;faps&gt;<br/>
 *  &lt;/eca_synthesis&gt;<br/>
 *  </code><p>
 *
 *  Follow this link to retrieve the Document Type Definition 
 *  <A HREF="../../resources/vhml_output.dtd">vhml_output.dtd</A> Follow this link
 *  to retrieve an example XML file <A HREF="../../resources/vhml_output.xml">
 *  vhml_example.xml</A>
 *
 *@author     Simon Beard
 *
 *@version    1.0
 *@see        VHMLOutputReader
 *@see        metaface.presynthesise.PreSynthesise
 */
class VHMLOutputTagReader extends DefaultHandler {

	/**
	 *  hold characters read within the XML document
	 */
	private String tempchar = "";
	/**
	 *  remember the last target frame for the URL
	 */
	private String target = "";
	/**
	 *  hold the ECA phonemes
	 */
	private Vector phonemes;
	/**
	 *  hold the ECA MPEG-4 FAPs
	 */
	private Vector faps;
	/**
	 *  hold the plain text spoken by the ECA
	 */
	private String text;
	/**
	 *  hold a table of URLs by their target keys
	 */
	private Hashtable browserupdates;


	/**
	 *  The constructor that intitialises the output reader
	 */
	public VHMLOutputTagReader() {
		super();
		phonemes = new Vector();
		faps = new Vector();
		text = "";
		browserupdates = new Hashtable();
	}


	/**
	 *  Allows access to the MPEG-4 FAPs retrieved
	 *
	 *@return    A list of FAPData MPEG-4 FAP frames
	 */
	public Vector getFrames() {
		return (faps);
	}


	/**
	 *  Allows access to the phonemes retrieved
	 *
	 *@return    A list of phonemes
	 */
	public Vector getPhonemes() {
		return (phonemes);
	}


	/**
	 *  Allows access to the plain text retrieved
	 *
	 *@return    A String of characters
	 */
	public String getText() {
		return (text);
	}


	/**
	 *  Allows access to the URLs and targets retrieved
	 *
	 *@return    A table of URLs referenced by their targets as keys
	 */
	public Hashtable getBrowserUpdates() {
		return (browserupdates);
	}


	/**
	 *  The parser calls this once at the beginning of a document
	 *
	 *@exception  SAXException  XML parsing Exception	 
	 */
	public void startDocument() throws SAXException {
	}


	/**
	 *  The parser calls this once after parsing a document
	 *
	 *@exception  SAXException  XML parsing Exception	 
	 */
	public void endDocument() throws SAXException {
	}


	/**
	 *  The parser calls this when encountering character data
	 *
	 *@param  ch                the characers encountered between tags
	 *@param  start             the starting point in the character array
	 *@param  length            the length of character data occupying the array
	 *@exception  SAXException  XML parsing Exception	 
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		//Store the character data as encountered between tags
		tempchar = tempchar.concat(new String(ch, start, length));
	}


	/**
	 *  The parser calls this for each element in a document
	 *
	 *@param  namespaceURI      used to resolve the namespace of the element
	 *@param  localName         the local name of the element
	 *@param  rawName           the resolved name of the element
	 *@param  atts              the attributes of the element
	 *@exception  SAXException  XML parsing Exception	 
	 */
	public void startElement(String namespaceURI, String localName, String rawName, Attributes atts) throws SAXException {
		//Get the key and determine what tag has been encountered
		String key = rawName;
		if (key.compareTo("eca_synthesis") == 0) {
		} else if (key.compareTo("text") == 0) {
			//Reset character string
			tempchar = "";
		} else if (key.compareTo("url") == 0) {
			//Store the target
			target = atts.getValue(0);
			tempchar = "";
		} else if (key.compareTo("phonemes") == 0) {
			tempchar = "";
		} else if (key.compareTo("faps") == 0) {
			tempchar = "";
		}
	}


	/**
	 *  The parser calls this when encountering a closing tag.
	 *
	 *@param  namespaceURI  used to resolve the namespace of the element
	 *@param  localName     the local name of the element
	 *@param  rawName       the resolved name of the element	 
	 */
	public void endElement(String namespaceURI, String localName, String rawName) {
		//Get the key and determine what tag has been encountered
		String key = rawName;
		if (key.compareTo("eca_synthesis") == 0) {
		} else if (key.compareTo("text") == 0) {
			//Get rid of white space
			text = tempchar.trim();
		} else if (key.compareTo("url") == 0) {
			//Store the URL using its target as the key
			browserupdates.put(target, tempchar.trim());
		} else if (key.compareTo("phonemes") == 0) {
			int i;
			int j;
			String line;

			//Parse the phonemes line by line to store in a list rather than as a string
			tempchar = tempchar.trim();
			i = tempchar.indexOf('\n');
			j = 0;
			while (i > 0) {
				line = tempchar.substring(j, i);
				phonemes.add(line);
				i++;
				j = i;
				i = tempchar.indexOf('\n', j);
			}
			line = tempchar.substring(j, tempchar.length());
			phonemes.add(line);
		} else if (key.compareTo("faps") == 0) {
			int i;
			int j;
			String mask;
			String values;

			//Parse FAP bitmasks and values string to store as FAPData objects in a list
			tempchar = tempchar.trim();
			i = tempchar.indexOf('\n');
			j = 0;
			while (i > 0) {
				mask = tempchar.substring(j, i);
				i++;
				j = i;
				i = tempchar.indexOf('\n', j);
				if (i < 0) {
					i = tempchar.length();
				}
				values = tempchar.substring(j, i);
				faps.add(new FAPData(mask, values));
				i++;
				j = i;
				i = tempchar.indexOf('\n', j);
			}
		}
	}
}
