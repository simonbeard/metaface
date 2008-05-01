package metaface.server;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import metaface.interaction.*;
import metaface.dsp.*;
import metaface.client.*;
import metaface.mpeg4.*;

/**
 *  This class provides the basis for client services. It may be extended to
 *  offer a particular service to clients.
 *
 *@author     Simon Beard
 *@version    1.0
 *@see        metaface.client.HTTPClient
 *@see        metaface.client.MetaFaceHTTPClient
 *@see        MetaFaceHTTPServer
 */
public abstract class MetaFaceService extends Thread {

	/**
	 *  Data is a start sequence
	 */
	private static int START = 0;
	/**
	 *  Data is VHML
	 */
	private static int VHML = 1;
	/**
	 *  Data is a flush sequence
	 */
	private static int FLUSH = 2;
	/**
	 *  Data for sending to the client
	 */
	private String senddata = new String("");
	/**
	 *  The client machine string
	 */
	private String clientmachine;
	/**
	 *  Data output to the client
	 */
	private DataOutputStream output;
	/**
	 *  Client stimulus
	 */
	private String input;
	/**
	 *  The response to send to the client
	 */
	private String _httpResponse;
	/**
	 *  Acknowledgement of receipt
	 */
	private byte[] ack;
	/**
	 *  The HTTP server of this service
	 */
	private MetaFaceHTTPServer hs;
	/**
	 *  The interaction manager for handling client stimulus
	 */
	private InteractionManager im;
	/**
	 *  The DSP
	 */
	private DigitalSignalProcessor dsp;
	/**
	 *  The frame rate for animation
	 */
	private int framerate;
	/**
	 *  The MPEG-4 FAPs the send
	 */
	private Vector faps;
	/**
	 *  The phoneme information to send
	 */
	private Vector phonemes;
	/**
	 *  The speech text to send
	 */
	private String speechtext;
	/**
	 *  The URLs and targets to send
	 */
	private Hashtable browserupdates;
	/**
	 *  Whether to debug
	 */
	private boolean debug = false;


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
	public MetaFaceService(InteractionManager interactionmanager, Socket s, MetaFaceHTTPServer httpserver, DigitalSignalProcessor digitalsignalprocessor) throws IOException {
		_httpResponse = "HTTP/1.1 200 MetaFace \nCache-Control: no-cache\nPragma: no-cache \r\n\r\n";
		ack = new String("ACK").getBytes();
		im = interactionmanager;
		clientmachine = s.getInetAddress().getHostAddress();
		output = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
		output.writeBytes(_httpResponse);
		output.writeInt(ack.length);
		output.write(ack);
		output.flush();
		hs = httpserver;
		dsp = digitalsignalprocessor;
		framerate = 25;
	}


	/**
	 *  Whether to print debug information
	 *
	 *@param  flag  The new debug value
	 */
	public void setDebug(boolean flag) {
		debug = flag;
	}


	/**
	 *  Process a response for sending to the client
	 *
	 *@param  response  The response string (a file, text, or null)
	 */
	protected abstract void processResponse(String response);


	/**
	 *  Sets the MPEG-4 FAP frames of the MetaFaceService
	 *
	 *@param  frames  The MPEG-4 FAPs
	 */
	protected void setFrames(Vector frames) {
		faps = frames;
	}


	/**
	 *  Sets the phoneme information of the MetaFaceService
	 *
	 *@param  phs  The new phonemes value
	 */
	protected void setPhonemes(Vector phs) {
		phonemes = phs;
	}


	/**
	 *  Sets the speech text of the MetaFaceService
	 *
	 *@param  txt  The new text value
	 */
	protected void setText(String txt) {
		speechtext = txt;
	}


	/**
	 *  Sets the browser updates (URLs and targets) of the MetaFaceService
	 *
	 *@param  updates  The new browserUpdates value
	 */
	protected void setBrowserUpdates(Hashtable updates) {
		browserupdates = updates;
	}


	/**
	 *  Sets the frame rate attribute of the MetaFaceService
	 *
	 *@param  framespersecond  The new frame rate
	 */
	public void setFrameRate(int framespersecond) {
		framerate = framespersecond;
	}


	/**
	 *  Gets the frame rate attribute of the MetaFaceService
	 *
	 *@return    The frame rate
	 */
	public int getFrameRate() {
		return (framerate);
	}


	/**
	 *  Gets the DSP attribute of the MetaFaceService
	 *
	 *@return    The DSP
	 */
	protected DigitalSignalProcessor getDSP() {
		return (dsp);
	}


	/**
	 *  Determines whether the client's socket matches the argument
	 *
	 *@param  s  A client socket to match against
	 *@return    Whether the sockets match
	 */
	public boolean matchClient(Socket s) {
		if (clientmachine.equalsIgnoreCase(s.getInetAddress().getHostAddress())) {
			return (true);
		}
		return (false);
	}


	/**
	 *  Sets the data input for the MetaFaceService
	 *
	 *@param  data  The new input data
	 */
	public void setInput(InputStream data) {
		try {
			String inputtemp2;
			input = new String("");
			BufferedReader inputtemp = new BufferedReader(new InputStreamReader(data));
			inputtemp2 = inputtemp.readLine();
			while (inputtemp2.length() > 0) {
				input = input.concat(inputtemp2);
				inputtemp2 = inputtemp.readLine();
			}
			data.close();
		} catch (Exception e) {}
	}


	/**
	 *  Main processing method for the MetaFaceService object. Executed when
	 *  incoming data arrives for this service.
	 */
	public void run() {
		String response;
		try {
			String s = input;

			//Starting sequesnce
			if (s.indexOf("_start_") == 0) {
				if (debug) {
					System.out.println("starting client " + clientmachine);
				}
				sendData(START, null);
			} else if (s.indexOf("_stop_") == 0) {

				//Stopping sequence
				if (debug) {
					System.out.println("stopping client " + clientmachine);
				}
				sendData(FLUSH, null);
			} else if (s.indexOf("_exit_") == 0) {

				//Exit sequence
				if (debug) {
					System.out.println("client exiting " + clientmachine);
				}
				hs.removeClient(this);
			} else {
				//Stimulus has arrived

				if (debug) {
					System.out.println("processing stimulus " + input + " " + clientmachine);
				}

				//Get a response to the stimulus
				response = im.processUserStimulus(s.trim());
				if (debug) {
					System.out.println("retrieve " + response);
				}
				sendData(VHML, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 *  Sends data to the client
	 *
	 *@param  flag  The type of data to send (START, VHML, FLUSH)
	 *@param  data  The data string to send
	 */
	private synchronized void sendData(int flag, String data) {
		Vector fapvec;
		String pi;
		String header;
		Vector visemes;
		int i;
		int j;
		String temp;
		byte[] bytes;
		byte[] bytes2;
		FAPData tempfap;
		char[] buf;
		String grouping;
		String fap;
		String ph;
		String html;
		String speechtext;
		String fapdata;
		Enumeration keys;
		String target;
		String url;
		VHMLOutputReader vhmloutputreader;
		VHMLParser vhmlparser;

		//Start sequence
		if (flag == START) {
			try {

				//Send silence
				pi = dsp.silencePhonemeString(1000);

				//Send FAPs of same length
				fapvec = new Vector();
				visemes = dsp.phonemesToVisemes(pi + "\n");
				fapvec = FAPData.visemesToFAPString(visemes, 0f, framerate, true);

				//Get a FAP header
				header = (String) fapvec.elementAt(0);
				header = header.substring(header.indexOf("\n") + 1);
				fapvec = new Vector();

				//Send the header
				try {
					bytes = header.getBytes();
					output.writeByte((byte) MetaFaceHTTPClient.HEADER);
					output.writeInt(bytes.length);
					output.write(bytes);
				} catch (IOException e) {
					e.printStackTrace();
				}

				//Send the FAPs
				for (j = 0; j < framerate; j++) {
					try {
						tempfap = new FAPData();
						bytes = tempfap.getBitMaskBytes();
						bytes2 = tempfap.getValueBytes(j);
						output.writeByte((byte) MetaFaceHTTPClient.FAP);
						output.writeInt(bytes.length + bytes2.length);
						output.write(bytes);
						output.write(bytes2);
						output.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				//Flush FAPs
				try {
					bytes = "\0\n".getBytes();
					output.writeByte((byte) MetaFaceHTTPClient.FAP);
					output.writeInt(bytes.length);
					output.write(bytes);
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//Send phoneme information
				try {
					bytes = pi.getBytes();
					output.writeByte((byte) MetaFaceHTTPClient.PHONEMES);
					output.writeInt(bytes.length);
					output.write(bytes);
					bytes = "\0\n".getBytes();
					output.writeByte((byte) MetaFaceHTTPClient.PHONEMES);
					output.writeInt(bytes.length);
					output.write(bytes);
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				System.err.println("Server sending error");
				e.printStackTrace();
			}
		} else if (flag == VHML) {
			//Sending VHML data

			if (data != null) {
				faps = new Vector();
				phonemes = new Vector();
				speechtext = "";
				browserupdates = new Hashtable();

				//Get the FAPs, phonemes, text, URLs, and targets
				processResponse(hs.getVHMLFilePath() + "/" + data);

				//Send URLs and targets
				try {
					keys = browserupdates.elements();
					while (keys.hasMoreElements()) {
						target = (String) keys.nextElement();
						url = (String) browserupdates.get(target);
						bytes = (url + "->" + target).getBytes();
						output.writeByte((byte) MetaFaceHTTPClient.HTMLREF);
						output.writeInt(bytes.length);
						output.write(bytes);
						output.flush();
					}

					//Send text
					bytes = speechtext.getBytes();
					output.writeByte((byte) MetaFaceHTTPClient.TEXT);
					output.writeInt(bytes.length);
					output.write(bytes);
					output.flush();

					//Send FAPs
					for (i = 0; i < faps.size(); i++) {
						tempfap = (FAPData) faps.elementAt(i);
						bytes = tempfap.getBitMaskBytes();
						bytes2 = tempfap.getValueBytes(i);
						output.writeByte((byte) MetaFaceHTTPClient.FAP);
						output.writeInt(bytes.length + bytes2.length);
						output.write(bytes);
						output.write(bytes2);
						output.flush();
					}
					bytes = "\0\n".getBytes();
					output.writeByte((byte) MetaFaceHTTPClient.FAP);
					output.writeInt(bytes.length);
					output.write(bytes);
					output.flush();

					//Send phoneme information
					ph = "";
					for (i = 0; i < phonemes.size(); i++) {
						ph = ph.concat(((String) phonemes.elementAt(i)) + "\n");
					}
					bytes = ph.getBytes();
					output.writeByte((byte) MetaFaceHTTPClient.PHONEMES);
					output.writeInt(bytes.length);
					output.write(bytes);
					output.flush();
					bytes = "\0\n".getBytes();
					output.writeByte((byte) MetaFaceHTTPClient.PHONEMES);
					output.writeInt(bytes.length);
					output.write(bytes);
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (flag == FLUSH) {

			//Flush sequence (send /0 for streaming FAPs and phoneme information)
			try {
				bytes = "\0\n".getBytes();
				output.writeByte((byte) MetaFaceHTTPClient.PHONEMES);
				output.writeInt(bytes.length);
				output.write(bytes);
				output.flush();
				output.writeByte((byte) MetaFaceHTTPClient.FAP);
				output.writeInt(bytes.length);
				output.write(bytes);
				output.flush();
			} catch (Exception e) {
				System.out.println(e + " server sending error");
			}
		}
	}
}

