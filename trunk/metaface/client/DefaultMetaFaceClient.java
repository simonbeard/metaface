package metaface.client;
import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import javax.swing.*;
import netscape.javascript.*;
import java.net.*;
import java.io.*;
import metaface.embodiment.*;
import metaface.dsp.*;

/**
 *  This class a default MetaFace client based on the client side framework
 *  design (server-free)
 *
 *@author     Simon Beard 
 *@version    1.0
 */

public class DefaultMetaFaceClient extends MetaFaceIndependentClient {

	/**
	 *  Constructor for the DefaultMetaFaceClient object
	 *
	 *@param  drawingarea  Drawing region for the MPEG-4 face
	 *@param  win          The JSObject window for accessing the browser
	 */
	public DefaultMetaFaceClient(DrawingArea drawingarea, JSObject win) {
		
		//Create default client with Mbrola DSP and default idle behaviour
		super(new MetaFaceMbrola("../Libraries"), drawingarea, win, new IdleBehaviour());
	}
}
