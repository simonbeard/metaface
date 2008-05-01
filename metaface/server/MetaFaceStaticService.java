package metaface.server;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import metaface.interaction.*;
import metaface.dsp.*;
import metaface.client.*;

/**
 *  This class provides a static client service based on the prerendering of
 *  VHML. Prerendered FAPs and phonemes are retrieved when needed in response to
 *  a client's stimulus.
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        metaface.client.HTTPClient
 *@see        metaface.client.MetaFaceHTTPClient
 *@see        MetaFaceHTTPServer
 *@see        VHMLOutputReader
 */
public class MetaFaceStaticService extends MetaFaceService {

	/**
	 *  Constructor for the MetaFaceService object
	 *
	 *@param  interactionmanager      The interaction manager to handle user
	 *      (client machine) stimulus
	 *@param  s                       The client socket
	 *@param  httpserver              The server
	 *@param  digitalsignalprocessor  The DSP
	 *@exception  IOException         Input/Output Exception
	 */
	public MetaFaceStaticService(InteractionManager interactionmanager, Socket s, MetaFaceHTTPServer httpserver, DigitalSignalProcessor digitalsignalprocessor) throws IOException {
		super(interactionmanager, s, httpserver, digitalsignalprocessor);
	}


	/**
	 *  Process a response from the interaction manager (in accordance with client
	 *  stimulus).
	 *
	 *@param  response  The response string (a VHML file)
	 */
	protected void processResponse(String response) {
		VHMLOutputReader vhmloutputreader;

		vhmloutputreader = new VHMLOutputReader(response);
		setFrames(vhmloutputreader.getFrames());
		setPhonemes(vhmloutputreader.getPhonemes());
		setBrowserUpdates(vhmloutputreader.getBrowserUpdates());
		setText(vhmloutputreader.getText());
	}
}

