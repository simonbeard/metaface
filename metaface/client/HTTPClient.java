package metaface.client;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 *  Implementation of a basic HTTP client for Firewall tunneling. The client
 *  supports both Client->Server and Server->Client communication.
 *
 *@author     Simon Beard 
 *@version    1.0
 */
public class HTTPClient implements Runnable {
	/**
	 *  Description of the Field
	 */
	public final static int PENDING = 1;
	/**
	 *  Description of the Field
	 */
	public final static int DATA = 2;
	/**
	 *  The server URL
	 */
	private String _url;
	/**
	 *  Pending connection thread
	 */
	private Thread _pendingTID;
	/**
	 *  Notify to stop client
	 */
	private boolean _stop;
	/**
	 *  Listeners for incoming messages
	 */
	private HTTPListener _listener;
	/**
	 *  Input stream for messages
	 */
	private DataInputStream input;
	/**
	 *  The connection to the MetaFace HTTP server
	 */
	private HttpURLConnection connection;
	/**
	 *  Length of the message
	 */
	private int length;

	/**
	 *  Whether data is pending
	 */
	public boolean pending = false;


	/**
	 *  Creates a new HTTPClient instance configured to connect to the server
	 *  specified in the url parameter.
	 *
	 *@param  url  URL of the server, example http://bogus:9000
	 */
	public HTTPClient(String url) {
		File[] list;
		int i;
		File f;

		HttpURLConnection connection;

		_url = url;
		_pendingTID = new Thread(this);
		_pendingTID.setPriority(Thread.MAX_PRIORITY);
		_listener = null;
	}


	/**
	 *  Removes the HTTPListener. The pending connection is stopped, so the client
	 *  won't receive any more messages from the server
	 *
	 *@param  l  HTTPListener to remove
	 */
	public void removeHTTPListener(HTTPListener l) {
		if (_listener != null) {
			synchronized (this) {
				_stop = true;
			}
			_pendingTID.destroy();
			_listener = null;
		}
	}


	/**
	 *  Adds the HTTPListener. The pending connection is started, so the client
	 *  will receive messages from the server.
	 *
	 *@param  l                           HTTPListener to remove
	 *@throws  TooManyListenersException  Thrown if more then one HTTPListener is
	 *      added.
	 */
	public void addHTTPListener(HTTPListener l) throws TooManyListenersException {
		if (_listener == null) {
			_listener = l;
			synchronized (this) {
				_stop = false;
			}
			_pendingTID.start();
		} else {
			throw new TooManyListenersException();
		}
	}


	/**
	 *  Convience method to send the PENDING connection to the server
	 *
	 *@return               void
	 *@throws  IOException  Thrown if a problem occurs while setting up the pending
	 *      message.
	 */
	private HttpURLConnection _sendPending() throws IOException {
		URL url = new URL(_url);
		connection = (HttpURLConnection) url.openConnection();
		connection.setUseCaches(false);
		connection.setDoOutput(true);
		connection.setRequestProperty("Keep-Alive", "timeout=300, max=300");
		return connection;
	}


	/**
	 *  Thread used to maintain the pending connection
	 */
	public void run() {
		int id;
		try {
			connection = _sendPending();
			DataOutputStream output = new DataOutputStream(connection.getOutputStream());
			output.writeInt(PENDING);
			output.flush();
			output.close();
			output = null;

			int code = ((HttpURLConnection) connection).getResponseCode();

			switch (code) {
							case 200:
							{
								// HTTP_OK
								input = new DataInputStream(connection.getInputStream());
								length = input.readInt();
								byte buffer[] = new byte[length];

								//Read data
								if (length >= 0) {
									input.readFully(buffer);
									if (buffer.length == length) {
										if (_listener != null) {
											_listener.service(buffer, MetaFaceHTTPClient.START);
											pending = true;
										}
									} else {
										System.err.println("Short read");
									}
									buffer = null;
								} else {
									System.err.println("Invalid length: " + length);
								}
								break;
							}
							case 504:
							{
								//HTTP_GATEWAY_TIMEOUT
								System.out.println("error response");
								break;
							}
							default:
							{
								//OTHER HTTP_SERVER ERRORS
								System.out.println("Invalid code:" + code);
								break;
							}
			}

			//Main loop for receiving messages
			while (true) {
				if (input.available() > 0) {
					id = (int) input.readByte();
					length = input.readInt();
					byte buffer[] = new byte[length];
					if (length >= 0) {
						input.readFully(buffer);
						if (buffer.length == length) {
							if (_listener != null) {
								_listener.service(buffer, id);
							}
						} else {
							System.err.println("Short read");
						}
						buffer = null;
					}
				} else {

					//Sleep to allow other threads to execute
					Thread.yield();
					Thread.sleep(5);
				}
			}
		} catch (Exception e) {
			System.err.println("Exiting " + e);
			e.printStackTrace();
		}
	}


	/**
	 *  Sends a message to the server
	 *
	 *@param  data          Array of bytes to send
	 *@throws  IOException  Thrown if a problem occurs while sending the message
	 */
	public synchronized void send(byte data[]) throws IOException {
		byte buffer[];

		// Establish a connection
		URL url = new URL(_url);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setUseCaches(false);
		connection.setDoOutput(true);

		// Write out the data
		DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));
		dataOut.writeInt(HTTPClient.DATA);
		dataOut.writeInt(data.length);
		dataOut.write(data);
		dataOut.flush();
		dataOut.close();

		DataInputStream input = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
		int type = input.readInt();
		if (type == HTTPClient.DATA) {
			length = input.readInt();
			buffer = new byte[length];
			input.readFully(buffer);
		} else {
			buffer = null;
			throw new IOException("Unknown Response Type");
		}
		input.close();
	}
}


