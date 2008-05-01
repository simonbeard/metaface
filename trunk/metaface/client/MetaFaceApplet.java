package metaface.client;
import java.awt.*;
import java.applet.*;
import javax.swing.*;
import java.awt.event.*;
import netscape.javascript.*;
/**
 *  This class handles the instanciation of MetaFace in the browser by providing an Applet
 *
 *@author     Simon Beard 
 *@version    1.0
 */
public abstract class MetaFaceApplet extends JApplet implements ActionListener {
	/**
	 *  The window of the browser
	 */
	private JSObject window;
	/**
	 *  Applet base path
	 */
	private String basePath;
	/**
	 *  Current web page
	 */
	private String currentWebPage;


	/**
	 *  Initializes the applet MetaFaceApplet
	 */
	public void init() {
	}


	/**
	 *  Sets the client attribute of the MetaFaceApplet object
	 *
	 *@param  mfc  The new client
	 */
	public abstract void setClient(MetaFaceClient mfc);


	/**
	 *  Start the applet
	 */
	public void start() {
	}


	/**
	 *  Initialise components
	 */
	private void initComponents() {
		setLayout(new java.awt.BorderLayout());
		setBackground(java.awt.Color.white);
	}


	/**
	 *  Web browser JavaScript methods call this to notify of updates to target frames
	 *
	 *@param  url     The URL currently displayed
	 *@param  target  The target frame
	 */
	public abstract void updateWebPage(String url, String target);
}

