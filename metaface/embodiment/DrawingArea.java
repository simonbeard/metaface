package metaface.embodiment;
import java.lang.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import metaface.mpeg4.*;
/**
 *  DrawingArea is a double buffered panel that captures paint calls and
 *  executes the MPEG-4Face.drawFace method
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        MPEG4Face
 *@see        SpikyMPEG4Face
 *@see        GirlMPEG4Face
 */
public class DrawingArea extends javax.swing.JPanel {

	/**
	 *  Rendering hints that are set (i.e.&nbsp;antialias etc.)
	 */
	private RenderingHints rh;
	/**
	 *  Graphics object used to paint this panel
	 */
	private Graphics2D g2d = null;
	/**
	 *  Graphics tookit for synching
	 */
	private Toolkit tk;
	/**
	 *  MPEG-4 Character
	 */
	private MPEG4Face character;



	/**
	 *  Creates new DrawingArea based on rendering hints
	 *
	 *@param  width   The width of the drawing area
	 *@param  height  The height of the drawing area
	 */
	public DrawingArea(int width, int height) {
		super();
		character = new SpikyMPEG4Face();
		setBackground(java.awt.Color.black);
		rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		tk = getToolkit();
		this.setPreferredSize(new Dimension(width, height));
		super.repaint();
	}


	/**
	 *  Sets a new MPEG4 Face of the DrawingArea object
	 *
	 *@param  face  The new MPEG4 face to use
	 */
	public void setMPEG4Face(MPEG4Face face) {
		character = face;
	}
	
	
	/**
	 *  Animates the MPEG-4 character face of this drawing area
	 *
	 *@param  frame  The MPEG-4 FAP frame to animate the face with
	 */
	public void animateMPEG4Face(FAPData frame) {
		character.animateFace(frame);
	}


	/**
	 *  Paints the component
	 *
	 *@param  graphics  The Graphics2d object for this panel
	 */
	public void paintComponent(java.awt.Graphics graphics) {
		super.paintComponent(graphics);
		paintOverComponent(graphics);
	}


	/**
	 *  Paints over what has already been drawn in this panel. This method does not
	 *  "wipe" the panel before drawing.
	 *
	 *@param  graphics  The Graphics2d object for this panel
	 */
	public void paintOverComponent(java.awt.Graphics graphics) {
		int width;
		int height;

		g2d = (Graphics2D) graphics;
		g2d.setRenderingHints(rh);
		width = getWidth();
		height = getHeight();
		character.drawFace(g2d, width, height);
		tk.sync();
	}
}

