package metaface.server;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import metaface.interaction.*;
import metaface.dsp.*;
import metaface.nlp.*;

/**
 *  This class provides a dynamic client service based on rendering of VHML
 *  files as they are needed for a response to a client's stimulus.
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        metaface.client.HTTPClient
 *@see        metaface.client.MetaFaceHTTPClient
 *@see        MetaFaceHTTPServer
 */
public class MetaFaceDynamicService extends MetaFaceService {
	/**
	 *  The NLP
	 */
	NaturalLanguageProcessor nlp;


	/**
	 *  Constructor for the MetaFaceService object
	 *
	 *@param  interactionmanager        The interaction manager to handle user
	 *      (client machine) stimulus
	 *@param  s                         The client socket
	 *@param  httpserver                The server
	 *@param  digitalsignalprocessor    The DSP
	 *@param  naturallanguageprocessor  The NLP
	 *@exception  IOException           Input/Output Exception
	 */
	public MetaFaceDynamicService(InteractionManager interactionmanager, Socket s, MetaFaceHTTPServer httpserver, DigitalSignalProcessor digitalsignalprocessor, NaturalLanguageProcessor naturallanguageprocessor) throws IOException {
		super(interactionmanager, s, httpserver, digitalsignalprocessor);
		nlp = naturallanguageprocessor;
	}


	/**
	 *  Process a response from the interaction manager (in accordance with client
	 *  stimulus).
	 *
	 *@param  response  The response string (a VHML file)
	 */
	protected void processResponse(String response) {
		VHMLParser vhmlparser;

		//Parse VHML file
		vhmlparser = new VHMLParser(response, getFrameRate(), getDSP(), nlp, ".");

		//Set data for sending to client
		setFrames(vhmlparser.getFrames());
		setPhonemes(vhmlparser.getPhonemes());
		setBrowserUpdates(vhmlparser.getBrowserUpdates());
		setText(vhmlparser.getText());
	}
}

