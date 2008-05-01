package metaface.client;

import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.JTextArea;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.*;
import netscape.javascript.*;
import com.sun.image.codec.jpeg.*;
import metaface.mpeg4.*;
import metaface.interaction.*;
import metaface.embodiment.*;
import metaface.dsp.*;

/**
 *  This class allows communication with a MetaFace framework server via HTTP.<p>
 *
 *  Traditional HTTP communication follows these steps: <br/>
 *  <menu>
 *  <li> 1. Client sends a request to the HTTP Server</li>
 *  <li> 2. The HTTP server retrieves the HTML data</li>
 *  <li> 3. The HTTP server then sends the data in response</li> </menu> <p>
 *
 *  <IMG SRC="../../resources/http.jpg"/><p>
 *
 *  User stimulus is sent to the HTTP MetaFace server via multplexing and MetaFace
 *  framework headers<p>
 *
 *  <IMG SRC="../../resources/multiplexing.jpg"/>
 *
 *@author     Simon Beard
 *@version    1.0
 */
public class MetaFaceHTTPClient extends MetaFaceClient implements HTTPListener, Runnable {
	/**
	 *  Incoming data is a FAP
	 */
	public final static int FAP = 1;
	/**
	 *  Incoming data is a phoneme
	 */
	public final static int PHONEMES = 2;
	/**
	 *  Incoming data is text
	 */
	public final static int TEXT = 3;
	/**
	 *  Incoming data is an URL
	 */
	public final static int HTMLREF = 4;
	/**
	 *  Incoming data is of an active type
	 */
	public final static int ACTIVE = 5;
	/**
	 *  Incoming data is an MPEG-4 header
	 */
	public final static int HEADER = 6;
	/**
	 *  Outgoing data is a starting sequence
	 */
	public final static int START = 7;
	/**
	 *  Incoming data is unknown
	 */
	public final static int UNKNOWN = -1;
	/**
	 *  The HTTP client
	 */
	private HTTPClient _client;
	/**
	 *  Dialogue input field
	 */
	private JTextField fromClient;
	/**
	 *  Incoming data type
	 */
	private final static int datatype = UNKNOWN;
	/**
	 *  Are there incoming FAPs ?
	 */
	private boolean fapincoming;
	/**
	 *  Are there incoming phonemes ?
	 */
	private boolean phonemeincoming;
	/**
	 *  Is the phoneme buffer empty
	 */
	private boolean phonemebufferempty;
	/**
	 *  Are incoming FAPs to be ignored ?
	 */
	private boolean ignoreIncomingFAPData;
	/**
	 *  Are incoming phonemes to be ignored ?
	 */
	private boolean ignoreIncomingPhData;
	/**
	 *  Set when client exits
	 */
	private boolean exit;
	/**
	 *  Incoming faps
	 */
	private Vector catchfaps = new Vector();
	/**
	 *  Incoming phonemes
	 */
	private Vector catchaudio = new Vector();
	/**
	 *  The last fap (use for transitioning)
	 */
	private FAPData lastcaughtfap = new FAPData();
	/**
	 *  The MPEG-4 header
	 */
	private boolean header;
	/**
	 *  Output debug information to console
	 */
	private boolean debug = false;


	/**
	 *  Constructor for the MetaFaceHTTPClient object
	 *
	 *@param  dsp          The TTS DSP to be used by the client in synthesising speech audio
	 *@param  drawingarea  The MPEG-4 face rendering panel
	 *@param  win          The JSObject window used to access the web browser
	 *@param  url          The URL of the HTTP MetaFace server
	 *@param  ib           The idle time behaviour to be used
	 */
	public MetaFaceHTTPClient(DigitalSignalProcessor dsp, DrawingArea drawingarea, JSObject win, String url, IdleBehaviour ib) {
		super(dsp, drawingarea, win, ib);
		
		//Set state variables
		ignoreIncomingFAPData = false;
		ignoreIncomingPhData = false;
		fapincoming = true;
		phonemeincoming = true;
		phonemebufferempty = false;
		exit = false;
		
		//Construct HTTP client
		_client = new HTTPClient(url);
		try {
			_client.addHTTPListener(this);
		} catch (TooManyListenersException e) {
			System.err.println("Error adding http listener");
			e.printStackTrace();
		}
		header = false;
		new Thread(this).start();
	}


	/**
	 *  Sets the debug attribute of the MetaFaceHTTPClient object
	 *
	 *@param  flag  The new debug value
	 */
	public void setDebug(boolean flag) {
		debug = flag;
	}


	/**
	 *  User stimulus has been recieved and this method is called. Usually to
	 *  process natural language or hypertext interaction stimulus
	 *
	 *@param  usertext  The stimulus
	 */
	public synchronized void processUserStimulus(String usertext) {
		if (debug) {
			System.out.println("sending " + usertext);
		}
		sendData(usertext.trim());
	}


	/**
	 *  This method is called when the client exits
	 */
	public synchronized void exit() {
		if (debug) {
			System.out.println("client exiting");
		}
		exit = true;
		sendData("_exit_");
	}


	/**
	 *  This method is called when the client starts
	 */
	public void startClient() {
		if (debug) {
			System.out.println("starting client");
		}
		ignoreIncomingFAPData = false;
		ignoreIncomingPhData = false;
	}


	/**
	 *  Main processing method for the MetaFaceIndependentClient object. Rendering
	 *  and polling (of browser and HTTP connection) is conducted by a thread.
	 */
	public void run() {

		//Wait until pending socket connected
		while (!_client.pending) {
			try {
				Thread.yield();
				Thread.sleep(20);
			} catch (Exception e) {
				System.out.println("error " + e);
				e.printStackTrace();
			}
		}

		//Send starting sequence to server
		sendData("_start_");
		while (!header) {
			try {
				Thread.yield();
				Thread.sleep(20);
			} catch (Exception e) {
				System.out.println("error " + e);
				e.printStackTrace();
			}
		}

		while (!exit) {
			
			//Check if idle and do behaviour
			idleBufferingScheme();
			
			//Render ECA
			renderAnimation();
			
			//Play phonemes
			playAudio();
			
			//Poll for changes to web browser
			pollWebPages();
			
			//Sleep to allow other threads to execute
			try {
				Thread.yield();
				Thread.sleep(10);
			} catch (Exception e) {
				System.out.println("Error during sleep");
				e.printStackTrace();
			}
		}
	}


	/**
	 *  Check if idle (no incoming data) and execute the idle behaviour scheme. Adds FAPs and phonemes to
	 * to the relevent buffers.
	 */
	protected synchronized void idleBufferingScheme() {
		if ((!fapincoming) && (!phonemeincoming) && (getFAPBufferSize() == 0) && (getPhonemeBufferSize() == 0)) {
			idleAnimation(125);
			addAudioSilence(5000);
			System.gc();
		}
	}


	/**
	 *  Called when the client is stopped whilst rendering and talking
	 */
	public synchronized void stopClient() {
		if (debug) {
			System.out.println("stopping client");
		}
		super.stopClient();
		sendData("_stop_");
		ignoreIncomingFAPData = true;
		ignoreIncomingPhData = true;
	}


	/**
	 *  A string is sent as byte data to the HTTP MetaFace server
	 *
	 *@param  data  The data to send
	 */
	public synchronized void sendData(String data) {
		if (debug) {
			System.out.println("sending data bytes");
		}
		try {
			_client.send(data.getBytes());
		} catch (Exception e) {
			System.out.println("error sending to server");
		}
	}


	/**
	 *  Provide a service for incoming HTTP data
	 *
	 *@param  data  The incoming data
	 *@param  id    Description of the data
	 */
	public synchronized void service(byte[] data, int id) {
		String s;
		FAPData currentfap;
		byte[] fapmask = new byte[9];
		byte[] fapvalues;
		boolean append = false;
		int i;
		int j;
		i = 0;

		//Determine what has been received
		switch (id) {
			case HEADER:
			{
				if (debug) {
					System.out.println("got header");
				}
				
				//Grab header and process (frame rate)
				s = new String(data);
				processMPEG4Header(s);
				header = true;
				break;
			}
			case FAP:
			{
				if (debug) {
					System.out.println("got fap");
				}
				
				//Get FAP and determine if EOF
				if (data.length < 9) {
					ignoreIncomingFAPData = false;
					fapincoming = false;
				} else {
										
					fapincoming = true;
					
					//Get bitmask
					for (i = 0; i < 9; i++) {
						fapmask[i] = data[i];
					}
					if (!ignoreIncomingFAPData) {
						
						//Get values
						j = 0;
						fapvalues = new byte[data.length - 9];
						for (i = 9; i < data.length; i++) {
							fapvalues[j] = data[i];
							j++;
						}
						
						//Decode bytes and add to buffer
						addFrameToBuffer(new FAPData(fapmask, fapvalues));
					}
				}
				break;
			}
			case PHONEMES:
			{
				if (debug) {
					System.out.println("got phoneme");
				}
				s = new String(data);
				
				//Determine of EOF
				if (s.indexOf("\n") == 1) {
					ignoreIncomingPhData = false;
					phonemeincoming = false;
				} else {
					
					//Add to buffer
					if (!ignoreIncomingPhData) {
						addPhonemeToBuffer(s);
					}
					phonemeincoming = true;
				}
				break;
			}
			case TEXT:
			{
				//Get text
				if (debug) {
					System.out.println("got text");
				}
				s = new String(data);
				if (append) {
					appendSpeechText(s);
				} else {
					setSpeechText(s);
				}
				break;
			}
			case HTMLREF:
			{
				//Get URLs
				if (debug) {
					System.out.println("got URL");
				}
				String target;
				String url;
				
				//Seperate URL from target
				s = new String(data);
				i = s.indexOf("->");
				url = s.substring(0, i);
				target = s.substring(i + 2, s.length());
				displayHTML(target, url);
				break;
			}
		}
	}
}

