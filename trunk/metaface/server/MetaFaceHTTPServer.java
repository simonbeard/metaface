package metaface.server;

import java.io.*;
import java.net.*;
import java.util.*;
import metaface.interaction.*;
import metaface.dsp.*;

/**
 *  Implementation of a basic HTTP server for Firewall tunneling. The server
 *  supports both Client->Server and Server->Client communication.<p>
 *
 *  <IMG SRC="../../resources/server.jpg"/><p>
 *
 *  If an initiate message is sent then a new process or thread in this case, is
 *  spawned and subsequently identified by the initiating client’s socket
 *  address. Obviously, if there are any existing processes with this address
 *  then they should be killed. If a close message is received, then the process
 *  with an identification matching the client’s socket address is killed. When
 *  stimulus is sent, the primary process must match the client’s socket address
 *  with the identification of one of the spawned processes. The identified
 *  process is then sent the stimulus (text or hypertext links) and given
 *  execution time; the process interprets the stimulus and formulates a
 *  response. The persistent spawned process finishes in an altered state which
 *  allows any subsequent stimulus to be processed in accordance with previous
 *  interaction. This allows for anaphora and keeps track of the dialogue
 *  context. Once the FAPs, phonemes, and hypertext links are ready to be
 *  transported to the client, it is a simple matter of multiplexing the data
 *  over a new downstream socket connection using the client’s socket address.
 *  <p>
 *
 *  <IMG SRC="../../resources/multiplexing.jpg"/><p>
 *  <IMG SRC="../../resources/clientserver.jpg"/><p>
 *
 *  <small>Copyright (c) 1996,1997,1998 Sun Microsystems, Inc. All Rights
 *  Reserved. Permission to use, copy, modify, and distribute this software and
 *  its documentation for NON-COMMERCIAL purposes and without fee is hereby
 *  granted provided that this copyright notice appears in all copies. Please
 *  refer to the file "copyright.html" for further important copyright and
 *  licensing information. The Java source code is the confidential and
 *  proprietary information of Sun Microsystems, Inc. ("Confidential
 *  Information"). You shall not disclose such Confidential Information and
 *  shall use it only in accordance with the terms of the license agreement you
 *  entered into with Sun. SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
 *  SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT
 *  LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 *  PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY
 *  DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 *  THIS SOFTWARE OR ITS DERIVATIVES.</small> <p>
 *
 *
 *
 *@author     Simon Beard
 *@author     beardsw 
 *@version    1.0
 *@see        metaface.client.HTTPClient
 *@see        metaface.client.MetaFaceHTTPClient
 */
public abstract class MetaFaceHTTPServer implements Runnable {
	/**
	 *  Accept socket
	 */
	private ServerSocket _serverSocket;
	/**
	 *  Server Listener
	 */
	private HTTPServerListener _listener;
	/**
	 *  Thread accepting connections
	 */
	private Thread _acceptTID;
	/**
	 *  Handler threads
	 */
	private Pool _pool;
	/**
	 *  Client sockets
	 */
	private Vector _clients;
	/**
	 *  Default HTTP Response
	 */
	private String _httpResponse;
	/**
	 *  The DMTL parser to construct knowledge bases
	 */
	private DMTLParser dmtlparser;
	/**
	 *  The knowledge base for incoming stimulus
	 */
	private KnowledgeBase knowledgebase;
	/**
	 *  The interaction manager to handle incoming stimulus
	 */
	private InteractionManager im;
	/**
	 *  Server output to clients
	 */
	private DataOutputStream output;
	/**
	 *  The DSP
	 */
	private DigitalSignalProcessor dsp;
	/**
	 *  Whether to debug
	 */
	private boolean debug = false;
	/**
	 *  The path to VHML files
	 */
	private String vhmlfilepath = "";


	/**
	 *  Creates a new HttpServer instance
	 *
	 *@param  port                    Port to listen on
	 *@param  poolSize                Number of handler threads
	 *@param  dmtlpath                Description of the Parameter
	 *@param  vhmlpath                Description of the Parameter
	 *@param  digitalsignalprocessor  Description of the Parameter
	 *@exception  IOException         Description of the Exception
	 *@throws  IOException            Thrown if the accept socket cannot be opended
	 */
	public MetaFaceHTTPServer(int port, int poolSize, String dmtlpath, String vhmlpath, DigitalSignalProcessor digitalsignalprocessor) throws IOException {
		File tempfile;
		File[] list;
		Vector filenames;
		Vector kbs;
		int i;

		//Initialise
		dsp = digitalsignalprocessor;
		vhmlfilepath = vhmlpath;
		filenames = new Vector();
		tempfile = new File(dmtlpath);
		System.out.println("Parsing DMTL files from: " + dmtlpath);

		//Parse DMTL files
		if (tempfile.isDirectory()) {
			list = tempfile.listFiles();
			for (i = 0; i < list.length; i++) {
				if (list[i].getName().indexOf(".dmtl") > 0) {
					System.out.println("Obtaining file: " + list[i].getPath());
					filenames.add(list[i].getPath());
				}
			}
		}

		//Create interaction manager
		im = new InteractionManager();
		dmtlparser = new DMTLParser(filenames);

		//Add knowledgebases
		kbs = dmtlparser.getKnowledgeBases();
		for (i = 0; i < kbs.size(); i++) {
			im.addKnowledgeBase((KnowledgeBase) kbs.elementAt(0));
		}

		//Create accept socket
		_serverSocket = new ServerSocket(port);
		_httpResponse = "HTTP/1.1 200 MetaFace \nCache-Control: no-cache\nPragma: no-cache \r\n\r\n";
		try {
			_pool = new Pool(poolSize, HTTPServerWorker.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalError(e.getMessage());
		}
		_clients = new Vector();
		_acceptTID = new Thread(this);
		_acceptTID.start();
	}


	/**
	 *  Adds a new client
	 *
	 *@param  s  Socket
	 */
	public abstract void addClient(Socket s);


	/**
	 *  Gets the Server DSP
	 *
	 *@return    The DSP
	 */
	protected DigitalSignalProcessor getDSP() {
		return (dsp);
	}


	/**
	 *  Gets the path to VHML files
	 *
	 *@return    The VHML file path
	 */
	public String getVHMLFilePath() {
		return (vhmlfilepath);
	}


	/**
	 *  Sets whether to print debug statements
	 *
	 *@param  flag  The new debug value
	 */
	public void setDebug(boolean flag) {
		debug = flag;
	}


	/**
	 *  Gets the interaction manager of the MetaFaceHTTPServer
	 *
	 *@return    The interaction manager
	 */
	protected InteractionManager getInteractionManager() {
		return (im);
	}


	/**
	 *  Adds a client with an associated service
	 *
	 *@param  s    The client socket
	 *@param  mfs  The client service
	 */
	public synchronized void addClient(Socket s, MetaFaceService mfs) {
		if (debug) {
			System.out.println("Adding client");
		}
		try {
			_clients.addElement(mfs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 *  Removes a client
	 *
	 *@param  mfs  The associated service of the client
	 */
	public synchronized void removeClient(MetaFaceService mfs) {
		if (debug) {
			System.out.println("Removing client");
		}
		try {
			_clients.removeElement(mfs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 *  Adds a new HttpServerListener. Only one listener can be added
	 *
	 *@param  l                           HttpServerListener
	 *@throws  TooManyListenersException  Thrown if more then one listener is added
	 *      (HTTP server)
	 */
	public void addHttpServerListener(HTTPServerListener l) throws TooManyListenersException {
		if (_listener == null) {
			_listener = l;
		} else {
			throw new TooManyListenersException();
		}
	}


	/**
	 *  Removes a HttpServerListener
	 *
	 *@param  l  HttpServerListener
	 */
	public void removeHttpServerListener(HTTPServerListener l) {
		_listener = null;
	}


	/**
	 *  Notifies the listener when a message arrives
	 *
	 *@param  data  Message data
	 *@param  out   Stream to write results too
	 *@param  s     Description of the Parameter
	 */
	public synchronized void notifyListener(InputStream data, OutputStream out, Socket s) {
		int i;
		MetaFaceService mfs;
		boolean match = false;
		boolean nomatch = true;

		if (debug) {
			System.out.println("notifying server thread of incoming data");
		}

		//For each client
		for (i = 0; i < _clients.size(); i++) {

			//Get their service
			mfs = (MetaFaceService) _clients.elementAt(i);

			//See if intiating socket matches service socket
			if (mfs.matchClient(s)) {
				if (debug) {
					System.out.println("Found client: running data service");
				}

				//Notify of data and run client service
				i = _clients.size();
				match = true;
				nomatch = false;
				mfs.setInput(data);
				new Thread(mfs).start();
			}
		}
		if (nomatch) {
			//A dropped client makes a request
			if (debug) {
				System.out.println("Client was dropped: make a new thread");
			}

			//Add the client
			addClient(s);
			mfs = (MetaFaceService) _clients.elementAt(_clients.size() - 1);

			//Notify of data and run
			mfs.setInput(data);
			if (debug) {
				System.out.println("Running data service");
			}
			new Thread(mfs).start();
		}
		try {

			//Acknowledge client data
			DataOutputStream dataOut = new DataOutputStream(out);
			dataOut.writeBytes("ACK");
			dataOut.flush();
			dataOut.close();
		} catch (Exception e) {
			System.out.println("problem with ack");
		}
	}


	/**
	 *  Thread to accept connections
	 */
	public void run() {

		while (true) {
			try {
				Socket s = _serverSocket.accept();
				Hashtable data = new Hashtable();
				data.put("Socket", s);
				data.put("MetaFaceHTTPServer", this);
				_pool.performWork(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 *  Convience method to write the HTTP Response header
	 *
	 *@param  out           Stream to write the response too
	 *@throws  IOException  Thrown if response can't be written
	 */
	public void writeResponse(DataOutputStream out) throws IOException {
		out.writeBytes(_httpResponse);
	}
}

