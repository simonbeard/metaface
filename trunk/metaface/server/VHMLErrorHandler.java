package metaface.server;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;

/**
 *  Error handler to report XML errors and warnings during VHML parsing
 *
 *@author     Simon Beard
 *@version    1.0
 *@see        VHMLParser
 *@see        VHMLReader
 */
class VHMLErrorHandler implements ErrorHandler {

	/**
	 *  Error handler output goes here
	 */
	private PrintStream out;


	/**
	 *  Construct the error handler
	 *
	 *@param  out  Description of the Parameter
	 */
	VHMLErrorHandler(PrintStream out) {
		this.out = out;
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
	 *  Prints warning
	 *
	 *@param  spe               The SAX Parser Exception thrown
	 *@exception  SAXException  Description of the Exception
	 */
	public void warning(SAXParseException spe) throws SAXException {
		out.println("Warning: " + getParseExceptionInfo(spe));
	}


	/**
	 *  Prints error
	 *
	 *@param  spe               The SAX Parser Exception thrown
	 *@exception  SAXException  Description of the Exception
	 */
	public void error(SAXParseException spe) throws SAXException {
		String message = "Error: " + getParseExceptionInfo(spe);
		throw new SAXException(message);
	}


	/**
	 *  Prints fatal error
	 *
	 *@param  spe               The SAX Parser Exception thrown
	 *@exception  SAXException  Description of the Exception
	 */
	public void fatalError(SAXParseException spe) throws SAXException {
		String message = "Fatal Error: " + getParseExceptionInfo(spe);
		throw new SAXException(message);
	}
}

