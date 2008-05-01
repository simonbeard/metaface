package metaface.server;
import java.io.*;
import java.net.*;
import java.util.*;
import metaface.dsp.*;

/**
 *  Implementation of a basic HTTP server for Firewall tunneling. The server
 *  supports both Client->Server and Server->Client communication.<p>
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
 *@see        MetaFaceHTTPServer
 */
public class MetaFaceDefaultServer extends MetaFaceHTTPServer {
	/**
	 *  The server mainline
	 *
	 *@param  argv           Command line arguments
	 *@exception  Exception  Exit exception
	 */
	public static void main(String argv[]) throws Exception {
		if (argv.length == 4) {
			new MetaFaceDefaultServer(Integer.parseInt(argv[0]), Integer.parseInt(argv[1]), argv[2], argv[3], new MetaFaceMbrola());
		} else {
			System.out.println("Usage: java HttpServer <port> <pool size> <path of DMTL files> <path of VHML files>");
			System.exit(1);
		}
	}


	/**
	 *  Creates a new HTTP server instance
	 *
	 *@param  port                    Port to listen on
	 *@param  poolSize                Number of handler threads
	 *@param  dmtlpath                Path to DMTL knowledge base files
	 *@param  vhmlpath                Path to VHML files
	 *@param  digitalsignalprocessor  The DSP
	 *@exception  IOException         File IO Exception
	 *@throws  IOException            Thrown if the accept socket cannot be opended
	 */
	public MetaFaceDefaultServer(int port, int poolSize, String dmtlpath, String vhmlpath, DigitalSignalProcessor digitalsignalprocessor) throws IOException {
		super(port, poolSize, dmtlpath, vhmlpath, digitalsignalprocessor);
	}


	/**
	 *  Adds a new client to server
	 *
	 *@param  s  Socket of client
	 */
	public synchronized void addClient(Socket s) {
		try {
			addClient(s, new MetaFaceStaticService(getInteractionManager(), s, this, getDSP()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

