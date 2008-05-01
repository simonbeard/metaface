package metaface.client;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import netscape.javascript.*;
import java.net.*;
import metaface.mpeg4.*;
import metaface.interaction.*;
import metaface.embodiment.*;
import metaface.dsp.*;

/**
 *  This class implements the client-side (server free) framework design.
 *  Although the MetaFace framework was designed for use within Internet
 *  websites, it is not uncommon for websites to be viewed offline, on a client
 *  machine. For example, consider the Java API documentation available from the
 *  Sun website . Since the information is static and Java developers are
 *  constantly consulting the documentation web pages, it is possible to
 *  download what is essentially a website. This website can then be viewed
 *  using a web browser, by accessing the file system rather than an Internet
 *  web server.
 *
 *@author     Simon Beard 
 *@version    1.0
 */
public class MetaFaceIndependentClient extends MetaFaceClient {
	private Thread thread;
	private boolean exit;
	private VHMLOutputReader vhmloutputreader;
	private InteractionManager im;
	private DMTLParser dmtlparser;


	/**
	 *  Creates new client that does not use a server connection
	 *
	 *@param  dsp          The TTS DSP to be used by the client when producing
	 *      speech audio
	 *@param  drawingarea  The MPEG-4 face drawing area
	 *@param  window       The JSObject window to access the web browser
	 *@param  ib           The idle behaviour class for animating the character
	 */
	public MetaFaceIndependentClient(DigitalSignalProcessor dsp, DrawingArea drawingarea, JSObject window, IdleBehaviour ib) {
		super(dsp, drawingarea, window, ib);
		exit = false;
		thread = new Thread(this);
		this.start();
	}


	/**
	 *  Initialises the interaction manager with DMTL files
	 *
	 *@param  files         The DMTL files to be used as a knowledge base
	 *@param  vhmlfilepath  The path to VHML files refered to in the DMTL files
	 */
	public synchronized void initialiseInteractionManager(Vector files, String vhmlfilepath) {
		int i;
		Vector kb;
		im = new InteractionManager();
		dmtlparser = new DMTLParser(files);
		kb = dmtlparser.getKnowledgeBases();
		for (i = 0; i < kb.size(); i++) {
			im.addKnowledgeBase((KnowledgeBase) kb.elementAt(i));
		}
		im.setBasePath(vhmlfilepath);
	}


	/**
	 *  Initialises the interaction manager with DMTL files
	 *
	 *@param  parser        The DMTL parser to be used in the knowledge base
	 *@param  vhmlfilepath  The path to VHML files refered to in the DMTL files
	 */
	public synchronized void initialiseInteractionManager(DMTLParser parser, String vhmlfilepath) {
		int i;
		Vector kb;
		im = new InteractionManager();
		kb = parser.getKnowledgeBases();
		for (i = 0; i < kb.size(); i++) {
			im.addKnowledgeBase((KnowledgeBase) kb.elementAt(i));
		}
		im.setBasePath(vhmlfilepath);
	}


	/**
	 *  User stimulus has been recieved and this method is called. Usually to
	 *  process natural language or hypertext interaction stimulus
	 *
	 *@param  usertext  The stimulus
	 */
	public synchronized void processUserStimulus(String usertext) {
		String response;
		Vector faps;
		Vector phonemes;
		String target;
		String url;
		String text;
		Hashtable browserupdate;
		Enumeration keys;
		response = im.processUserStimulus(usertext);

		if (response != null) {

			//Process the response file (a VHML Ouput file)
			vhmloutputreader = new VHMLOutputReader(response);

			//Get the FAPs, phonemes, URLs, and text
			faps = vhmloutputreader.getFrames();
			phonemes = vhmloutputreader.getPhonemes();
			browserupdate = vhmloutputreader.getBrowserUpdates();
			text = vhmloutputreader.getText();
			setSpeechText(text);

			//Reset ECA
			stopClient();
			toNeutralPosition(25);
			addAudioSilence(1000);

			//Add data to buffers
			addFramesToBuffer(faps);
			addPhonemesToBuffer(phonemes);
			playAudio();
			renderAnimation();

			//Display URLs
			keys = browserupdate.keys();
			while (keys.hasMoreElements()) {
				target = (String) (keys.nextElement());
				url = (String) (browserupdate.get(target));
				displayHTML(target, url);
			}

			//Transition to neutral position
			toNeutralPosition(25);
			addAudioSilence(1000);
		}
	}


	/**
	 *  This method is called when the client exits
	 */
	public synchronized void exit() {
		exit = true;
	}


	/**
	 *  Main processing method for the MetaFaceIndependentClient object. Rendering
	 *  and polling is conducted by a thread.
	 */
	public void run() {
		while (!exit) {

			//Check for browser changes
			pollWebPages();
			
			//Play speech audio from phoneme buffer
			playAudio();
			
			//Render FAPs in buffer
			renderAnimation();
			
			//Check if idle, and do idle behaviour
			idleBufferingScheme();

			//Sleep to allow other threads to run
			try {
				thread.yield();
				thread.sleep(10);
			} catch (Exception e) {
				System.out.println("Error while sleeping");
				e.printStackTrace();
			}
		}
	}


	/**
	 *  This method is called when the client starts
	 */
	public synchronized void startClient() {
	}
}

