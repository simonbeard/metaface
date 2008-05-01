package metaface.embodiment;
import java.lang.*;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.text.*;
import java.awt.font.*;
import java.util.*;

/**
 *  This class is used to render speech text within a cartoon speech bubble. The
 *  result is a scrollable text area within a speech bubble outline.<p>
 *
 *  <img src="../../resources/speechbubble.jpg"/>
 *
 *@author     Simon Beard
 *@version    1.0
 *@see        MPEG4Face
 *@see        SpikyMPEG4Face
 *@see        GirlMPEG4Face
 */
public class SpeechBubble extends javax.swing.JPanel {

	/**
	 *  Rendering hints that are set (i.e. antialias etc.)
	 */
	private RenderingHints rh;
	/**
	 *  Graphics object used to paint this panel
	 */
	private Graphics2D g2d;
	/**
	 *  The default toolkit for rendering
	 */
	private Toolkit tk;
	/**
	 *  The pane in which to display text
	 */
	private JTextPane textpane;
	/**
	 *  The panel in which to draw the bubble outline
	 */
	private JPanel textpanel;
	/**
	 *  Scroll pane for the speech text
	 */
	private JScrollPane scrollpane;
	/**
	 *  Outline for the speech bubble
	 */
	private RoundRectangle2D.Float sb;
	/**
	 *  Background colour
	 */
	private Color bg;
	/**
	 *  Foreground colour
	 */
	private Color fg;
	/**
	 *  Object to erase outline for peak of speech bubble outline
	 */
	private Rectangle2D.Float erase;
	/**
	 *  The peak of the speech bubble
	 */
	private GeneralPath speechtri;
	/**
	 *  Measure of text line for wrapping
	 */
	private LineBreakMeasurer lineMeasurer;
	/**
	 *  Location of paragraph start
	 */
	private int paragraphStart;
	/**
	 *  Location of paragraph end
	 */
	private int paragraphEnd;
	/**
	 *  The speech text with rendering atributes
	 */
	private AttributedString speechtext;
	/**
	 *  The paragraph text
	 */
	private AttributedCharacterIterator paragraph;
	/**
	 *  The plain text speech to display
	 */
	private String plaintext;
	/**
	 *  Margin of speech bubble on the left
	 */
	float marginleft;
	/**
	 *  Inset of speech bubble on the left
	 */
	float insetleft;
	/**
	 *  Inset of speech bubble on the right
	 */
	float insetright;
	/**
	 *  Inset of speech bubble on the top
	 */
	float insettop;
	/**
	 *  Inset of speech bubble on the bottom
	 */
	float insetbottom;
	/**
	 *  Formating width taking into account insets and margin
	 */
	float formatWidth;
	/**
	 *  Formating height taking into account insets
	 */
	float formatHeight;
	/**
	 *  Size of the speech text font
	 */
	float fontsize = 14.0f;
	/**
	 *  Speech peak is on the left
	 */
	public static int LEFT = 0;
	/**
	 *  Speech peak is on the bottom
	 */
	public static int BOTTOM = 1;
	/**
	 *  Map for text attributes
	 */
	private final static Hashtable map = new Hashtable();
	static {
		map.put(TextAttribute.SIZE, new Float(14.0f));
	}


	/**
	 *  Constructor for the SpeechBubble object
	 *
	 *@param  width            width of speech bubble
	 *@param  height           height of speech bubble
	 *@param  direction        direction of speech bubble peak (LEFT or DOWN)
	 *@param  text             text to display in the speech bubble
	 *@param  foregroundcolor  foreground colour to use
	 *@param  backgroundcolor  background colour to use
	 */
	public SpeechBubble(int width, int height, int direction, String text, Color foregroundcolor, Color backgroundcolor) {
		super();

		bg = backgroundcolor;
		fg = foregroundcolor;

		JScrollBar scrollbar;

		//Create rendering area
		textpanel = new JPanel();
		textpanel.setBackground(backgroundcolor);
		textpanel.setForeground(backgroundcolor);
		setBackground(backgroundcolor);
		setForeground(foregroundcolor);
		rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		tk = getToolkit();

		//If speech bubble peak is on the left
		if (direction == LEFT) {

			//Construct text area
			textpanel.setLayout(new BoxLayout(textpanel, BoxLayout.Y_AXIS));
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.add(Box.createRigidArea(new Dimension((int) (width / 7f), 0)));
			textpanel.add(Box.createRigidArea(new Dimension(0, (int) (height / 25f))));
			sb = new RoundRectangle2D.Float(0f + 0.1f, 0f + 0.02f, (1f - 0.0025f) - 0.1f, (0.95f), 0.1f, 0.1f);
			this.setPreferredSize(new Dimension(width, height));

			//Construct speech bubble outline
			speechtri = new GeneralPath();
			speechtri.moveTo(0.1f, 0.77f);
			speechtri.lineTo(0f + 0.02f, 0.88f);
			speechtri.lineTo(0.1f, 0.86f);
			erase = new Rectangle2D.Float(0.1f, 0.77f, 0.5f, 0.09f);
			marginleft = ((float) width / 10f);
			insetleft = ((float) width / 20f);
			insetright = insetleft;
			insettop = ((float) height / 40f);
			insetbottom = insettop;
			formatWidth = (float) width - (marginleft + insetleft + insetright);
			formatHeight = ((float) height) - (insettop + insetbottom);
			textpane = new JTextPane();
			textpane.setEditable(false);
			textpane.setForeground(foregroundcolor);
			textpane.setBackground(backgroundcolor);

			//Construct scroll pane
			scrollpane = new JScrollPane(textpane);
			scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollpane.setBackground(backgroundcolor);
			scrollpane.setForeground(backgroundcolor);
			scrollbar = scrollpane.getVerticalScrollBar();
			scrollbar.setBorder(BorderFactory.createLineBorder(Color.darkGray));
			scrollpane.setVerticalScrollBar(scrollbar);
			scrollpane.setViewportBorder(BorderFactory.createEmptyBorder());
			scrollpane.setBorder(BorderFactory.createEmptyBorder());

			//Put it all together
			textpanel.add(scrollpane);
			textpanel.add(Box.createRigidArea(new Dimension(0, (int) (2f * insetbottom))));
			this.add(textpanel);
			this.add(Box.createRigidArea(new Dimension((int) (width / 35f), 0)));
		} else if (direction == BOTTOM) {

			//If speech bubble peak is on the bottom
			textpanel.setLayout(new BoxLayout(textpanel, BoxLayout.Y_AXIS));
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

			//Construct speech bubble outline
			sb = new RoundRectangle2D.Float(0f + 0.04f, 0f + 0.04f, 1f - 0.08f, 0.83f, 0.1f, 0.1f);
			this.setPreferredSize(new Dimension(width, height));
			speechtri = new GeneralPath();
			speechtri.moveTo(0.72f, 0.87f);
			speechtri.lineTo(0.7f, 0.98f);
			speechtri.lineTo(0.79f, 0.87f);
			erase = new Rectangle2D.Float(0.72f, 0.86f, 0.07f, 0.01f);
			insetleft = ((float) width / 18f);
			insetright = ((float) width / 16f);
			insettop = ((float) height / 15f);
			insetbottom = ((float) height / 6.5f);

			//Construct text area
			textpane = new JTextPane();
			textpane.setEditable(false);
			textpane.setForeground(foregroundcolor);
			textpane.setBackground(backgroundcolor);

			//Construct scroll pane
			scrollpane = new JScrollPane(textpane);
			scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollpane.setBackground(backgroundcolor);
			scrollpane.setForeground(backgroundcolor);
			scrollbar = scrollpane.getVerticalScrollBar();
			scrollbar.setBorder(BorderFactory.createLineBorder(backgroundcolor));
			scrollpane.setVerticalScrollBar(scrollbar);
			scrollpane.setViewportBorder(BorderFactory.createEmptyBorder());
			scrollpane.setBorder(BorderFactory.createEmptyBorder());

			//Put it all together
			textpanel.add(Box.createRigidArea(new Dimension(0, (int) (insettop))));
			textpanel.add(scrollpane);
			textpanel.add(Box.createRigidArea(new Dimension(0, (int) (insetbottom))));
			this.add(Box.createRigidArea(new Dimension((int) (insetleft), 0)));
			this.add(textpanel);
			this.add(Box.createRigidArea(new Dimension((int) (insetright), 0)));
		}
		setText(text);
		super.repaint();
	}


	/**
	 *  Constructor for the SpeechBubble object
	 *
	 *@param  width      width of speech bubble
	 *@param  height     height of speech bubble
	 *@param  direction  direction of speech bubble peak (LEFT or DOWN)
	 *@param  text       text to display in the speech bubble
	 */
	public SpeechBubble(int width, int height, int direction, String text) {
		super();

		bg = Color.black;
		fg = Color.white;

		JScrollBar scrollbar;

		//Create rendering area
		textpanel = new JPanel();
		textpanel.setBackground(Color.black);
		textpanel.setForeground(Color.black);
		setBackground(Color.black);
		setForeground(Color.black);
		rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		tk = getToolkit();

		//If speech bubble peak is on the left
		if (direction == LEFT) {

			//Construct text area
			textpanel.setLayout(new BoxLayout(textpanel, BoxLayout.Y_AXIS));
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.add(Box.createRigidArea(new Dimension((int) (width / 7f), 0)));
			textpanel.add(Box.createRigidArea(new Dimension(0, (int) (height / 25f))));
			sb = new RoundRectangle2D.Float(0f + 0.1f, 0f + 0.02f, (1f - 0.0025f) - 0.1f, (0.95f), 0.1f, 0.1f);
			this.setPreferredSize(new Dimension(width, height));

			//Construct speech bubble outline
			speechtri = new GeneralPath();
			speechtri.moveTo(0.1f, 0.77f);
			speechtri.lineTo(0f + 0.02f, 0.88f);
			speechtri.lineTo(0.1f, 0.86f);
			erase = new Rectangle2D.Float(0.1f, 0.77f, 0.5f, 0.09f);
			marginleft = ((float) width / 10f);
			insetleft = ((float) width / 20f);
			insetright = insetleft;
			insettop = ((float) height / 40f);
			insetbottom = insettop;
			formatWidth = (float) width - (marginleft + insetleft + insetright);
			formatHeight = ((float) height) - (insettop + insetbottom);
			textpane = new JTextPane();
			textpane.setEditable(false);

			//Construct scroll pane
			scrollpane = new JScrollPane(textpane);
			scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollpane.setBackground(Color.white);
			scrollpane.setForeground(Color.white);
			scrollbar = scrollpane.getVerticalScrollBar();
			scrollbar.setBorder(BorderFactory.createLineBorder(Color.darkGray));
			scrollpane.setVerticalScrollBar(scrollbar);
			scrollpane.setViewportBorder(BorderFactory.createEmptyBorder());
			scrollpane.setBorder(BorderFactory.createEmptyBorder());

			//Put it all together
			textpanel.add(scrollpane);
			textpanel.add(Box.createRigidArea(new Dimension(0, (int) (2f * insetbottom))));
			this.add(textpanel);
			this.add(Box.createRigidArea(new Dimension((int) (width / 35f), 0)));
		} else if (direction == BOTTOM) {

			//If speech bubble peak is on the left
			textpanel.setLayout(new BoxLayout(textpanel, BoxLayout.Y_AXIS));
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

			//Construct speech bubble outline
			sb = new RoundRectangle2D.Float(0f + 0.04f, 0f + 0.04f, 1f - 0.08f, 0.83f, 0.1f, 0.1f);
			this.setPreferredSize(new Dimension(width, height));
			speechtri = new GeneralPath();
			speechtri.moveTo(0.72f, 0.87f);
			speechtri.lineTo(0.7f, 0.98f);
			speechtri.lineTo(0.79f, 0.87f);
			erase = new Rectangle2D.Float(0.72f, 0.86f, 0.07f, 0.01f);
			insetleft = ((float) width / 18f);
			insetright = ((float) width / 16f);
			insettop = ((float) height / 15f);
			insetbottom = ((float) height / 6.5f);

			//Construct text area
			textpane = new JTextPane();
			textpane.setEditable(false);
			textpane.setForeground(Color.white);
			textpane.setBackground(Color.black);

			//Construct scroll pane
			scrollpane = new JScrollPane(textpane);
			scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollpane.setBackground(Color.black);
			scrollpane.setForeground(Color.black);
			scrollbar = scrollpane.getVerticalScrollBar();
			scrollbar.setBorder(BorderFactory.createLineBorder(Color.black));
			scrollpane.setVerticalScrollBar(scrollbar);
			scrollpane.setViewportBorder(BorderFactory.createEmptyBorder());
			scrollpane.setBorder(BorderFactory.createEmptyBorder());

			//Put it all together
			textpanel.add(Box.createRigidArea(new Dimension(0, (int) (insettop))));
			textpanel.add(scrollpane);
			textpanel.add(Box.createRigidArea(new Dimension(0, (int) (insetbottom))));
			this.add(Box.createRigidArea(new Dimension((int) (insetleft), 0)));
			this.add(textpanel);
			this.add(Box.createRigidArea(new Dimension((int) (insetright), 0)));
		}
		setText(text);
		super.repaint();
	}


	/**
	 *  Sets the text of the SpeechBubble object to be displayed
	 *
	 *@param  text  The new speech text
	 */
	public void setText(String text) {
		plaintext = new String(text);
		if (plaintext.length() > 0) {
			textpane.setText(plaintext);
			this.repaint();
		}
	}


	/**
	 *  Appends to the text of the SpeechBubble object
	 *
	 *@param  text  The speech text to append
	 */
	public void appendText(String text) {
		plaintext = plaintext.concat(text);
		if (plaintext.length() > 0) {
			textpane.setText(plaintext);
			this.repaint();
		}
	}


	/**
	 *  Paints the speech bubble component
	 *
	 *@param  graphics  The Graphics2d object for this panel
	 */
	public void paintComponent(java.awt.Graphics graphics) {
		super.paintComponent(graphics);
		paintOverComponent(graphics);
	}


	/**
	 *  Paints over what has already been drawn in this panel. This method does not
	 *  "wipe" the speech bubble panel before drawing.
	 *
	 *@param  graphics  The Graphics2d object for this panel
	 */
	public void paintOverComponent(java.awt.Graphics graphics) {
		int width;
		int height;
		boolean fontfit;

		//Set rendering variables
		g2d = (Graphics2D) graphics;
		g2d.setRenderingHints(rh);
		width = getWidth();
		height = getHeight();
		g2d.setStroke(new BasicStroke(0.01f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));

		//Paint the speech bubble
		g2d.scale(width, height);
		g2d.setPaint(bg);
		g2d.fill(sb);
		g2d.setPaint(fg);
		g2d.draw(sb);
		g2d.setPaint(bg);
		g2d.fill(speechtri);
		g2d.draw(speechtri);
		g2d.fill(erase);
		g2d.draw(erase);
		g2d.setPaint(fg);
		g2d.draw(speechtri);

		//Draw text
		scrollpane.scrollRectToVisible(new Rectangle(0, 0, 0, 0));
		scrollpane.repaint();
		tk.sync();
	}
}

