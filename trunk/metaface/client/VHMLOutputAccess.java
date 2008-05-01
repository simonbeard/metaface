package metaface.client;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;
import java.io.*;
import java.security.*;

/**
 *  This class allows XML parsing errors to propogate due to privileged actions.
 *  Otherwise a security exception will be thrown for XML errors.
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        VHMLOutputTagReader
 *@see        VHMLOutputReader
 */
class VHMLOutputAccess implements PrivilegedAction {
	private XMLReader xmlreader;
	private String eca_synthesis_file;


	/**
	 *  Constructor for the VHMLOutputAccess object
	 *
	 *@param  xmlr  The XML parser needing privileged action
	 *@param  file  The VHML output file to be parsed
	 */
	public VHMLOutputAccess(XMLReader xmlr, String file) {
		xmlreader = xmlr;
		eca_synthesis_file = file;
	}


	/**
	 *  Main processing method for the VHMLOutputAccess object in order to execute
	 *  a privileged action
	 *
	 *@return    null
	 */
	public Object run() {
		try {
			xmlreader.parse(eca_synthesis_file);
		} catch (SAXException e) {
			System.err.println("XML error parsing VHML output file");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IO error parsing VHML output file");
			e.printStackTrace();
		}
		return null;
	}
}

