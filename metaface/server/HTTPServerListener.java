package metaface.server;
/**
 *  Used by the server to listen for messages from the HTTPClient<p>
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
 *@version    1.0
 *@see        metaface.client.HTTPClient
 *@see        metaface.client.MetaFaceHTTPClient
 *@see        MetaFaceHTTPServer
 */
public interface HTTPServerListener extends java.util.EventListener {
	/**
	 *  Method invoked when a message is received by the HttpServer
	 *
	 *@param  data     Message data
	 *@param  results  Stream to write response too
	 */
	public void service(java.io.InputStream data, java.io.OutputStream results);
}

