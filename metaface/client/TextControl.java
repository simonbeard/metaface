package metaface.client;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;

/**
 *  This class is allows text field input for an applet
 *
 *@author     Simon Beard 
 *@version    1.0
 */
public class TextControl extends javax.swing.JPanel {
	/**
	 *  The text field used for dialogue input
	 */
	private JTextField userText;
	/**
	 *  The dimension of the object
	 */
	private Dimension dim;
	/**
	 *  Whether the text field is hidden
	 */
	private boolean hidden = false;


	/**
	 *  Creates new TextControl for dialogue management input
	 *
	 *@param  width  width of the TextControl object
	 */
	public TextControl(int width) {
		super();
		this.setBackground(Color.white);
		userText = new JTextField();
		userText.setBorder(
				BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Type Here:"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)),
				userText.getBorder()));

		userText.setBackground(new Color(255, 255, 255));
		dim = userText.getPreferredSize();
		userText.setPreferredSize(new Dimension(width, (int) (dim.getHeight())));
		userText.setMinimumSize(new Dimension(width, (int) (dim.getHeight())));
		this.setLayout(new BorderLayout(0, 0));
		this.add(userText, BorderLayout.CENTER);
	}


	/**
	 *  Hide the text field
	 */
	public void hide() {
		userText.setVisible(false);
	}


	/**
	 *  Show the text field
	 */
	public void show() {
		userText.setVisible(true);
	}


	/**
	 *  Sets the textColor attribute of the TextControl object
	 *
	 *@param  c  The new textColor value
	 */
	public void setTextColor(Color c) {
		userText.setBackground(c);
	}


	/**
	 *  Gets the height attribute of the TextControl object
	 *
	 *@return    The height value
	 */
	public int getHeight() {
		return ((int) dim.getHeight());
	}


	/**
	 *  Gets the textField attribute of the TextControl object
	 *
	 *@return    The textField value
	 */
	public JTextField getTextField() {
		return (userText);
	}
}

