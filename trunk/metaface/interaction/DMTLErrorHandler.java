package metaface.interaction;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;
import java.io.*;

/**
 *  Error handler to report errors and warnings from parsing DMTL file
 *
 *@author     Simon Beard
 *@version    1.0
 *@see        InteractionManager
 */
public class DMTLErrorHandler implements ErrorHandler {

	/**
	 *  Error handler output goes here
	 */
	private PrintStream out;


	/**
	 *  Constructor for the DMTLErrorHandler object
	 *
	 *@param  output  the error output destination
	 */
	public DMTLErrorHandler(PrintStream output) {
		this.out = output;
	}


	/**
	 *  Returns a string describing parse exception details
	 *
	 *@param  spe  The SAX Parser Exception thrown
	 *@return      A string that identifies the error
	 */
	private String getParseExceptionInfo(SAXParseException spe) {
		String systemId = spe.getSystemId();
		if (systemId == null) {
			systemId = "null";
		}
		String info = "URI=" + systemId +
				" Line=" + spe.getLineNumber() +
				": " + spe.getMessage();
		return info;
	}


	// The following methods are standard SAX ErrorHandler methods.
	// See SAX documentation for more info.

	/**
	 *  Description of the Method
	 *
	 *@param  spe               The SAX Parser Exception thrown
	 *@exception  SAXException  Description of the Exception
	 */
	public void warning(SAXParseException spe) throws SAXException {
		out.println("Warning: " + getParseExceptionInfo(spe));
	}


	/**
	 *  Description of the Method
	 *
	 *@param  spe               The SAX Parser Exception thrown
	 *@exception  SAXException  Description of the Exception
	 */
	public void error(SAXParseException spe) throws SAXException {
		String message = "Error: " + getParseExceptionInfo(spe);
		throw new SAXException(message);
	}


	/**
	 *  Description of the Method
	 *
	 *@param  spe               The SAX Parser Exception thrown
	 *@exception  SAXException  Description of the Exception
	 */
	public void fatalError(SAXParseException spe) throws SAXException {
		String message = "Fatal Error: " + getParseExceptionInfo(spe);
		throw new SAXException(message);
	}
}

