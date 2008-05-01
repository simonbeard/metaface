package metaface.client;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import netscape.javascript.*;
import metaface.mpeg4.*;
import metaface.interaction.*;
import metaface.embodiment.*;
import metaface.dsp.*;
/**
 *  This class defines the base level client behaviour for the MetaFace
 *  framework. It controls the facial animation rendering and speech sysnthesis.
 *  It also controls the mailine execution of the client for the framework.
 *
 *@author     Simon Beard 
 *@version    1.0
 */
public abstract class MetaFaceClient extends Thread {

	/**
	 *  Description of the Field
	 */
	protected static int MAX_PHONEMES = 100;

	/**
	 *  Description of the Field
	 */
	protected FAPData frame;
	/**
	 *  Description of the Field
	 */
	protected FAPData lastframe;
	/**
	 *  Description of the Field
	 */
	protected SourceDataLine line;
	/**
	 *  The audio frame rate
	 */
	private float audioFrameRate;

	/**
	 *  How many audio frames per video frame
	 */
	private float afpvf;
	/**
	 *  Size of audio frames
	 */
	private int audioFrameSize;
	/**
	 *  The facial animation frame rate
	 */
	private int videoFrameRate;
	/**
	 *  Window for accessing web browser
	 */
	private JSObject window;
	/**
	 *  Default toolkit (for rendering)
	 */
	private Toolkit tk;
	/**
	 *  Audio output information structure
	 */
	private DataLine.Info info;
	/**
	 *  The current audio bytes being read
	 */
	private byte[] audioBytes;
	/**
	 *  The speech audio input stream (WAV file)
	 */
	private AudioInputStream audioInputStream;
	/**
	 *  The DMTL parser (for client interaction management)
	 */
	private DMTLParser dmtlparser;
	/**
	 *  The idle behaviour to be used
	 */
	private IdleBehaviour idlebehaviour;
	/**
	 *  Whether the current rendered FAP frame is too early
	 */
	private boolean tooearly;
	/**
	 *  The TTS DSP to use in synthesising speech audio from phonemes
	 */
	private DigitalSignalProcessor dsp;
	/**
	 *  A current store of target web browser frames and their contents
	 */
	private Hashtable webpages;
	/**
	 *  The FAP buffer used for rendering
	 */
	private Vector fapdatabuffer;
	/**
	 *  The phoneme buffer used for synthesising
	 */
	private Vector phonemedatabuffer;
	/**
	 *  The speech text (sometime displayed in a speech bubble)
	 */
	private String speechtext;
	/**
	 *  The number of FAP frames rendered
	 */
	private int numFrames;
	/**
	 *  Is a new WAV file needed (last one finished)
	 */
	private boolean newfile;
	/**
	 *  The current speech audio file being read (WAV file)
	 */
	private File audioFile;
	/**
	 *  The MPEG-4 face drawing region
	 */
	private DrawingArea appletDA;
	/**
	 *  Is the audio file the first one to be read (take initialisation steps)
	 */
	private boolean firstfile;


	/**
	 *  Creates new NewMetaFaceClient
	 *
	 *@param  digitalsignalprocessor  The TTS DSP to be used in synthesising
	 *      phonemes into speech audio
	 *@param  drawingarea             The drawing region for the MPEG-4 face
	 *@param  win                     The JSObject window to access the web browser
	 *@param  ib                      The idle behaviour to be employed by the
	 *      client
	 */
	public MetaFaceClient(DigitalSignalProcessor digitalsignalprocessor, DrawingArea drawingarea, JSObject win, IdleBehaviour ib) {

		//Clean up any temporary files from last time
		cleanFiles();

		//Set initialisation varaibles and copy incoming objects
		idlebehaviour = ib;
		audioFrameRate = 8000;
		audioFrameSize = 1;
		frame = new FAPData();
		audioBytes = new byte[100000];
		tk = Toolkit.getDefaultToolkit();
		lastframe = new FAPData();
		frame = new FAPData();
		numFrames = 0;
		videoFrameRate = 25;
		tooearly = false;
		afpvf = audioFrameRate / videoFrameRate;
		dsp = digitalsignalprocessor;
		appletDA = drawingarea;
		webpages = new Hashtable();
		numFrames = 0;
		fapdatabuffer = new Vector();
		phonemedatabuffer = new Vector();
		window = win;
		speechtext = "";
		newfile = true;
		audioFile = null;
		line = null;

		//Create empty audio line
		try {
			line = (SourceDataLine) AudioSystem.getLine(new Line.Info(SourceDataLine.class));
		} catch (LineUnavailableException e) {
			System.out.println("Source data line not available");
			e.printStackTrace();
		}
		firstfile = true;
	}


	/**
	 *  Process an MPEG-4 facial animation header (get the frame rate)
	 *
	 *@param  fapheader  The MPEG-4 header
	 */
	protected synchronized void processMPEG4Header(String fapheader) {
		int ptr;
		String temp;

		ptr = 0;

		//Skip to frame rate and set class variable
		ptr = fapheader.indexOf(" ", ptr);
		ptr = fapheader.indexOf(" ", ptr + 1);
		temp = fapheader.substring(ptr + 1, fapheader.indexOf(" ", ptr + 1));
		if (new Integer(temp).intValue() == 0) {
			temp = String.valueOf(25);
		}
		setVideoFrameRate(new Integer(temp).intValue());
	}


	/**
	 *  This method updates a table of target frames and their current URL content.
	 *  This method is called by the MetaFace Applet receiving JavaScript
	 *  notifications.
	 *
	 *@param  url     The URL displayed within the target frame
	 *@param  target  The target frame
	 */
	public synchronized void updateWebPage(String url, String target) {
		String oldurl;

		//If target not in table -- add
		if (!webpages.containsKey("target")) {
			webpages.put(target, url);
		} else {
			//Else see if URL has been updated
			oldurl = (String) webpages.get(target);
			if (oldurl.indexOf(url) != 0) {
				webpages.put(target, url);
			} else {
				webpages.remove(target);
			}
		}
	}


	/**
	 *  The client polls for changes to the web pages table and processes
	 */
	public synchronized void pollWebPages() {
		Enumeration keys;
		String target;
		String url;

		//Check entire table
		keys = webpages.keys();
		while (keys.hasMoreElements()) {

			//Use URLs as stimulus to interaction manager
			target = (String) (keys.nextElement());
			url = (String) (webpages.get(target));
			webpages.remove(target);			
			processUserStimulus(url);
		}
	}


	/**
	 *  Sets the videoFrameRate attribute of the MetaFaceClient object
	 *
	 *@param  fr  The new video frame rate
	 */
	public synchronized void setVideoFrameRate(int fr) {
		videoFrameRate = fr;
		afpvf = audioFrameRate / videoFrameRate;
	}
	
	/**
	 *  This method allows text to be appended to the speech text (normally
	 *  displayed within a speech bubble)
	 *
	 *@param  text  The speech text to append
	 */
	protected synchronized void appendSpeechText(String text) {
		speechtext = speechtext.concat(text);
	}


	/**
	 *  Sets the speech text attribute (for a speech bubble)
	 *
	 *@param  text  The new speech text
	 */
	protected synchronized void setSpeechText(String text) {
		speechtext = new String(text);
	}


	/**
	 *  Sets the digitalSignalProcessor (TTS DSP) attribute of the MetaFaceClient
	 *  object
	 *
	 *@param  newdsp  The new DSP
	 */
	protected synchronized void setDigitalSignalProcessor(DigitalSignalProcessor newdsp) {
		dsp = newdsp;
	}


	/**
	 *  Gets the digitalSignalProcessor attribute of the MetaFaceClient object
	 *
	 *@return    The current DSP
	 */
	protected synchronized DigitalSignalProcessor getDigitalSignalProcessor() {
		return (dsp);
	}


	/**
	 *  Gets the speechText attribute of the MetaFaceClient object
	 *
	 *@return    The current speech text (for a speech bubble)
	 */
	public synchronized String getSpeechText() {
		return (speechtext);
	}


	/**
	 *  This is caled when the client starts
	 */
	public abstract void startClient();


	/**
	 *  This is called when the client exits
	 */
	public abstract void exit();


	/**
	 *  The method called when processing stimulus such as hypertext or natural
	 *  language
	 *
	 *@param  usertext  The stimulus
	 */
	public abstract void processUserStimulus(String usertext);


	/**
	 *  Obtain phonemes from the buffer
	 *
	 *@return    Phonemes for synthesising speech audio
	 */
	protected synchronized Vector readPhonemesFromBuffer() {
		Vector output;
		int i;
		int j;
		output = new Vector();

		//check if buffer empty
		if (phonemedatabuffer.size() == 0) {
			return output;
		} else if (phonemedatabuffer.size() > MAX_PHONEMES) {

			//Get maximum amount of phonemes
			for (i = 0; i < MAX_PHONEMES; i++) {
				output.add(phonemedatabuffer.remove(0));
			}
		} else {

			//Get all phonemes
			j = phonemedatabuffer.size();
			for (i = 0; i < j; i++) {
				output.add(phonemedatabuffer.remove(0));
			}
		}
		return (output);
	}


	/**
	 *  Add phonemes to the buffer
	 *
	 *@param  phonemes  Phonemes to be synthesised
	 */
	protected synchronized void addPhonemesToBuffer(Vector phonemes) {
		int i;

		for (i = 0; i < phonemes.size(); i++) {
			phonemedatabuffer.add(((String) phonemes.elementAt(i)).trim());
		}
	}


	/**
	 *  Adds a phoneme to the buffer
	 *
	 *@param  phoneme  A phoneme to be synthesised
	 */
	protected synchronized void addPhonemeToBuffer(String phoneme) {
		phonemedatabuffer.add(phoneme.trim());
	}


	/**
	 *  Read a FAP frame from the buffer
	 *
	 *@return    A FAPData MPEG-4 FAP frame
	 */
	protected synchronized FAPData readFrameFromBuffer() {
		FAPData frame = new FAPData();
		if (fapdatabuffer.size() == 0) {
			frame.setNull(true);
		} else {
			frame.setNull(false);
			frame.adjustFAPData((FAPData) fapdatabuffer.remove(0));
		}
		return (frame);
	}


	/**
	 *  Add MPEG-4 FAPs to the buffer
	 *
	 *@param  faps  MPEG-4 FAPs
	 */
	protected synchronized void addFramesToBuffer(Vector faps) {
		int i;

		for (i = 0; i < faps.size(); i++) {
			fapdatabuffer.add(faps.elementAt(i));
		}
	}


	/**
	 *  Adds an MPEG-4 FAP to the buffer
	 *
	 *@param  fap  an MPEG-4 FAP
	 */
	protected synchronized void addFrameToBuffer(FAPData fap) {
		fapdatabuffer.add(new FAPData(fap));
	}


	/**
	 *  Clear all data from the FAP buffer
	 */
	protected void clearFAPBuffer() {
		fapdatabuffer.clear();
	}


	/**
	 *  Clear all data from the phoneme buffer
	 */
	protected void clearPhonemeBuffer() {
		phonemedatabuffer.clear();
	}


	/**
	 *  Display an URL in a target frame of the web browser
	 *
	 *@param  target  The target frame (e.g. frame1.frame2.final_frame)
	 *@param  url     The URL to be displayed (e.g. http://www.vhml.org)
	 */
	protected synchronized void displayHTML(String target, String url) {
		window.eval("parent." + target + ".location.href=\"" + url + "\"");
		System.gc();
	}


	/**
	 *  Stop (interrupt) the current action and idle
	 */
	public synchronized void stopClient() {
		newfile = true;

		//Get rid of unused FAPs and phonemes
		fapdatabuffer.clear();
		phonemedatabuffer.clear();

		//Stop and flush audio
		if (!firstfile)
		{
			line.stop();
			line.flush();
			line.start();
		}
		
		//Reset synchronisation of FAPs and audio
		numFrames = (int) ((float) line.getFramePosition() / afpvf);
		tooearly = false;
	}


	/**
	 *  Change the MPEG-4 face
	 *
	 *@param  mface  The new face
	 */
	public synchronized void changeModel(MPEG4Face mface) {
		appletDA.setMPEG4Face(mface);
	}


	/**
	 *  Repaint the drawing area for the MPEG-4 face
	 */
	public synchronized void repaintDrawingArea() {
		appletDA.repaint();
		tk.sync();
	}


	/**
	 *  Animate the MPEG-4 face drawing area by updating the face with the current
	 *  MPEG-4 FAP frame
	 *
	 *@param  fap  The current FAP frame
	 */
	public synchronized void animateDrawingArea(FAPData fap) {
		appletDA.animateMPEG4Face(fap);
		repaintDrawingArea();
		lastframe.adjustFAPData(fap);
	}


	/**
	 *  Gets the current FAP frame being rendered by the MPEG-4 face
	 *
	 *@return    The current FAP
	 */
	public synchronized FAPData getFAP() {
		return frame;
	}


	/**
	 *  Gets the audio frame rate of speech
	 *
	 *@return    The audio frame rate value
	 */
	public synchronized float getAudioFrameRate() {
		return audioFrameRate;
	}


	/**
	 *  Gets the audio frame size of speech
	 *
	 *@return    The audio frame size value
	 */
	public synchronized int getAudioFrameSize() {
		return audioFrameSize;
	}


	/**
	 *  Gets the audio line attribute of the MetaFaceClient object
	 *
	 *@return    The audio line value
	 */
	public synchronized SourceDataLine getAudioLine() {
		return line;
	}


	/**
	 *  Gets the window attribute of the MetaFaceClient object that accesses the
	 *  web browser
	 *
	 *@return    The JSObject window
	 */
	public synchronized JSObject getWindow() {
		return window;
	}


	/**
	 *  Gets the FAP buffer size of the MetaFaceClient
	 *
	 *@return    The FAP buffer size value
	 */
	protected synchronized int getFAPBufferSize() {
		return (fapdatabuffer.size());
	}


	/**
	 *  Gets the phoneme buffer size attribute of the MetaFaceClient
	 *
	 *@return    The phoneme buffer size value
	 */
	protected synchronized int getPhonemeBufferSize() {
		return (phonemedatabuffer.size());
	}


	/**
	 *  Called when transitioning to the neutral position (will autopopulate the
	 *  FAP and phoneme buffers)
	 *
	 *@param  frames  the number of frames to use in the transition
	 */
	protected synchronized void toNeutralPosition(int frames) {
		addFramesToBuffer(idlebehaviour.toNeutralFAPs(lastframe, frames));
	}


	/**
	 *  Adds audio silence to the phoneme buffer
	 *
	 *@param  milliseconds  The length of the silence
	 */
	protected synchronized void addAudioSilence(int milliseconds) {
		addPhonemeToBuffer(dsp.silencePhonemeString(milliseconds));
	}


	/**
	 *  Constructs the idle animation and autopopulates the FAP buffer
	 *
	 *@param  frames  the number of frames for idle behaviour
	 */
	protected synchronized void idleAnimation(int frames) {
		addFramesToBuffer(idlebehaviour.generateIdleFAPs(frames));
	}


	protected void resetSynchronisation () {
		//Get rid of unused FAPs and phonemes
		fapdatabuffer.clear();
		phonemedatabuffer.clear();		

		//Reset synchronisation of FAPs and audio
		numFrames = (int) ((float) line.getFramePosition() / afpvf);
		tooearly = false;		
	}
	
	/**
	 *  Tests for the client being idle and executes the idle buffering scheme
	 */
	protected synchronized void idleBufferingScheme() {

		//If no audio being played, and no phomemes in the buffer
		if ((!line.isActive()) && (phonemedatabuffer.size() == 0)&& (fapdatabuffer.size() <= 5)) {
			
			resetSynchronisation();
			
			//Idle behaviour
			idleAnimation(125);

			//Add silence to synchronise
			addAudioSilence(5000);			
			
			//Do clean up
			System.gc();
		}
	}


	/**
	 *  Deletes any temporary MetaFace framework files
	 *
	 *@param  filestr    A partial file string to match against potential file to
	 *      delete
	 *@param  extension  The extension of files to delete
	 */
	protected synchronized void cleanTemporaryFiles(String filestr, String extension) {
		String temp = null;
		File tempfile;
		File[] list;
		int i;

		//Create temporary file and get directory structure
		try {
			temp = File.createTempFile(filestr, extension).toString();
		} catch (IOException e) {
			e.printStackTrace();
			System.runFinalization();
		}

		//Get the files in this directory
		temp = temp.substring(0, temp.indexOf(filestr) - 1);
		tempfile = new File(temp);
		if (tempfile.isDirectory()) {
			list = tempfile.listFiles();
			for (i = 0; i < list.length; i++) {

				//If match criteria, then delete
				if (((list[i].toString()).indexOf(filestr) >= 0) && ((list[i].toString()).indexOf(extension) >= 0)) {
					list[i].delete();
				}
			}
			list = null;
		}
		tempfile = null;
	}


	/**
	 *  Clean MetaFace framework files of this particular client class
	 */
	private synchronized void cleanFiles() {
		String temp = null;
		File tempfile;
		File[] list;
		int i;

		//Create temporary file to get directory structure
		try {
			temp = File.createTempFile("MFFW_phonemes", ".wav").toString();
		} catch (IOException e) {
			e.printStackTrace();
			System.runFinalization();
		}

		temp = temp.substring(0, temp.indexOf("MFFW_phonemes") - 1);
		tempfile = new File(temp);

		//test for temporary files and delete as appropriate
		if (tempfile.isDirectory()) {
			list = tempfile.listFiles();
			for (i = 0; i < list.length; i++) {
				if (((list[i].toString()).indexOf("MFFW_") >= 0) && (((list[i].toString()).indexOf(".wav") >= 0) || ((list[i].toString()).indexOf(".xml") >= 0))) {
					list[i].delete();
				}
			}
			list = null;
		}
		tempfile = null;
	}


	/**
	 *  Synthesises audio using the DSP
	 *
	 *@param  phonemes  The phonemes to synthesise
	 *@return           The filename of the resulting WAV file (or null)
	 */
	protected synchronized String synthesiseWaveform(String phonemes) {
		String filename = null;
		String wavfilename;
		int i;
		FileWriter fw;

		if (phonemes == null) {
			return (null);
		}
		if (phonemes.length() <= 0) {
			return (null);
		}

		wavfilename = dsp.synthesise(phonemes);
		return (wavfilename);
	}


	/**
	 *  Plays ECA speech audio
	 */
	protected synchronized void playAudio() {
		String filename = null;
		String buffer = "";
		Vector ph;
		int lines = 0;
		int i;
		int toWrite;
		int bytesRead = 0;

		//If a new WAV file is needed
		if (newfile) {
			if ((audioFile != null)) {
				try {
					audioInputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				audioFile = null;
			}

			//Get phonemes and synthesise WAV file
			ph = readPhonemesFromBuffer();
			for (i = 0; i < ph.size(); i++) {
				buffer = buffer.concat(((String) ph.elementAt(i)) + "\n");
			}
			if (buffer == null) {
				return;
			}

			filename = synthesiseWaveform(buffer);

			if (filename == null) {
				return;
			}

			//Update state variables based on new WAV file
			newfile = false;
			audioFile = new File(filename);
			try {
				audioInputStream = AudioSystem.getAudioInputStream(audioFile);
				audioFrameRate = audioInputStream.getFormat().getFrameRate();
				audioFrameSize = audioInputStream.getFormat().getFrameSize();
				afpvf = audioFrameRate / videoFrameRate;

				//If first file being played -- initialise the audio line of the application
				if (firstfile) {
					firstfile = false;
					info = new DataLine.Info(SourceDataLine.class, audioInputStream.getFormat());
					line = (SourceDataLine) AudioSystem.getLine(info);
					line.open();
					line.start();
				}
			} catch (UnsupportedAudioFileException e) {
				System.out.println("Can't open audio input stream (Unsupported file)");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Can't open audio input stream (IO Exception)");
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				System.out.println("Source data line not available");
				e.printStackTrace();
			}
		}

		//Read from the WAV file and place bytes in audio buffer
		try {

			//Get how many bytes can be read and placed without blocking
			toWrite = line.available();
			if (toWrite > 0) {
				if (toWrite > 100000) {
					toWrite = 100000;
				}

				//Get and write bytes
				if ((bytesRead = audioInputStream.read(audioBytes, 0, toWrite)) != -1) {
					line.write(audioBytes, 0, bytesRead);
				} else {
					newfile = true;
					cleanFiles();
				}
			}
		} catch (IOException e) {
			System.out.println("Problem with audio line");
			e.printStackTrace();
		}
	}


	/**
	 *  Determines if the current FAP frame is early based on the current frame of
	 *  the audio line buffer
	 *
	 *@param  numFrames  The number of frames rendered
	 *@return            Whether FAP is early
	 */
	private synchronized boolean frameEarly(int numFrames) {
		if ((float) numFrames * afpvf > (float) line.getFramePosition() + afpvf) {
			return (true);
		}
		return (false);
	}


	/**
	 *  Determines if the current FAP late is early based on the current frame of
	 *  the audio line buffer
	 *
	 *@param  numFrames  The number of frames rendered
	 *@return            Whether FAP is late
	 */
	private synchronized boolean frameLate(int numFrames) {
		if (((float) numFrames * afpvf < (float) line.getFramePosition())) {
			return (true);
		}
		return (false);
	}


	/**
	 *  Renders the MPEG-4 FAPs in the buffer and synchronises with speech audio
	 */
	protected synchronized void renderAnimation() {
		if (!tooearly) {
			frame = readFrameFromBuffer();
			if (frame.isNull()) {
				return;
			}
		}

		// If frame is early -- return
		if (frameEarly(numFrames)) {
			tooearly = true;
			return;
		}

		// While audio not between prev and this frame -- get next frame
		while (frameLate(numFrames) && (!tooearly)) {
			numFrames++;
			frame = readFrameFromBuffer();
			tooearly = false;
			if (frame.isNull()) {
				return;
			}
		}

		animateDrawingArea(frame);
		numFrames++;
		tooearly = false;
	}
}

