package metaface.client;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import javax.swing.*;
import netscape.javascript.*;
import java.net.*;
import java.io.*;

import metaface.embodiment.*;

/**
 *  This default class handles the instantiation of a MetaFace applet within the
 *  browser
 *
 *@author     Simon Beard 
 *@version    1.0
 */
public abstract class DefaultMetaFaceApplet extends MetaFaceApplet {

	/**
	 *  The speech bubble for the talking head
	 */
	private SpeechBubble speechBubble;
	/**
	 *  The dialogue input object
	 */
	private TextControl tc;
	/**
	 *  The MPEG-4 face drawing region
	 */
	private DrawingArea appletDA;
	/**
	 *  The MetaFace client to use
	 */
	private MetaFaceClient dmfc = null;


	/**
	 *  Initializes the applet MetaFaceApplet
	 */
	public DefaultMetaFaceApplet() {
		super.init();
	}


	/**
	 *  Sets the client attribute of the DefaultMetaFaceApplet object
	 *
	 *@param  mfc  The new client
	 */
	public void setClient(MetaFaceClient mfc) {
		dmfc = mfc;
	}


	/**
	 *  Initialise the MetaFace applet
	 */
	public void init() {
		JPanel one;
		JPanel two;
		JPanel three;
		JPanel four;
		Rectangle bounds;
		int height;

		super.init();

		// Create GUI and setup
		setBackground(java.awt.Color.white);
		bounds = this.getBounds();

		// Create dialogue input object
		tc = new TextControl((int) (getBounds().getWidth()));
		tc.setBackground(java.awt.Color.black);
		tc.setForeground(java.awt.Color.black);
		tc.setBackground(new Color(255, 255, 255));
		height = tc.getHeight();
		appletDA = new DrawingArea((int) (bounds.getWidth()), (int) ((bounds.getHeight() / 2f) - height));
		height = 30;

		//Speech bubble for displaying speech text
		speechBubble = new SpeechBubble((int) (bounds.getWidth()), (int) ((bounds.getHeight() / 2f)), SpeechBubble.BOTTOM, "Welcome to Simon Beard's Resume");

		//Set layou of components
		getContentPane().setBackground(java.awt.Color.black);
		getContentPane().setLayout(new BorderLayout(0, 0));
		one = new JPanel();
		two = new JPanel();
		three = new JPanel();
		four = new JPanel();
		one.setBackground(Color.black);
		two.setBackground(Color.black);
		three.setBackground(Color.black);
		four.setBackground(Color.black);
		three.setLayout(new BoxLayout(three, BoxLayout.Y_AXIS));
		four.setLayout(new FlowLayout(FlowLayout.CENTER));
		two.setLayout(new BoxLayout(two, BoxLayout.Y_AXIS));
		two.add(appletDA);
		two.add(four);
		three.add(speechBubble);
		three.add(two);
		getContentPane().add(three, BorderLayout.CENTER);
	}


	/**
	 *  Start the applet
	 */
	public void start() {
		try {
			dmfc = new DefaultMetaFaceClient(appletDA, JSObject.getWindow(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 *  Initialise some components
	 */
	private void initComponents() {
		setLayout(new java.awt.BorderLayout());
		setBackground(java.awt.Color.black);
	}


	/**
	 *  This method is called by web browser JavaScript methods to notify of URL
	 *  changes to target frames
	 *
	 *@param  url     The new URL displayed
	 *@param  target  The target frame of the web browser
	 */
	public void updateWebPage(String url, String target) {

		//Notify client of changes
		if ((url != null) && (target != null)) {
			if (dmfc != null) {
				dmfc.updateWebPage(url, target);
			}
		}
	}
}

