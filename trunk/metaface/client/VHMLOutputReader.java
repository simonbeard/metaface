package metaface.client;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;
import java.io.*;
import java.security.*;
/**
 *  This class is used to construct an eXtensible Markup Language (XML) parser
 *  for a Virtual Human Markup Language (VHML) output document.<p>
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
 *@version    1.0 
 *@see        metaface.presynthesise.PreSynthesise
 */
public class VHMLOutputReader {

	/**
	 *  The parser for reading the XML tags
	 */
	private VHMLOutputTagReader vhmlreader;


	/**
	 *  Construct a parser for a VHML output document
	 *
	 *@param  eca_synthesis_file  the name of VHML output document to be parsed
	 */
	public VHMLOutputReader(String eca_synthesis_file) {

		//Whether to use a DTD to validate
		boolean validation = true;

		//Setup the parser
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setValidating(validation);
		XMLReader xmlReader = null;

		try {

			//Create a JAXP SAXParser
			SAXParser saxParser = spf.newSAXParser();

			//Get the encapsulated SAX XMLReader
			xmlReader = saxParser.getXMLReader();
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}

		// Set an ErrorHandler before parsing
		xmlReader.setErrorHandler(new VHMLOutputErrorHandler(System.err));
		vhmlreader = new VHMLOutputTagReader();
		xmlReader.setContentHandler(vhmlreader);

		//Do this to get descriptive stack traces if parsing fails
		VHMLOutputAccess voa = new VHMLOutputAccess(xmlReader, convertToFileURL(eca_synthesis_file));
		AccessController.doPrivileged(voa);
	}


	/**
	 *  Allows access to the MPEG-4 FAPs retrieved
	 *
	 *@return    A list of FAPData MPEG-4 FAP frames
	 */
	public Vector getFrames() {
		return (vhmlreader.getFrames());
	}


	/**
	 *  Allows access to the phonemes retrieved
	 *
	 *@return    A list of phonemes
	 */
	public Vector getPhonemes() {
		return (vhmlreader.getPhonemes());
	}


	/**
	 *  Allows access to the plain text retrieved
	 *
	 *@return    A String of characters
	 */
	public String getText() {
		return (vhmlreader.getText());
	}


	/**
	 *  Allows access to the URLs and targets retrieved
	 *
	 *@return    A table of URLs referenced by their targets as keys
	 */
	public Hashtable getBrowserUpdates() {
		return (vhmlreader.getBrowserUpdates());
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
}

