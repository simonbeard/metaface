package metaface.embodiment;
import java.awt.geom.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import metaface.mpeg4.*;
/**
 *  Abstract class that provides interface for classes to construct MPEG-4 faces
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        SpikyMPEG4Face
 *@see        GirlMPEG4Face
 */
public abstract class MPEG4Face {
	/**
	 *  Facing forwards
	 */
	protected static int FORWARDS = 0;
	/**
	 *  Facing backwards
	 */
	protected static int BACKWARDS = 1;
	/**
	 *  Facing left
	 */
	protected static int LEFT = 2;
	/**
	 *  Facing right
	 */
	protected static int RIGHT = 3;

	/**
	 *  Direction that the ECA is facing
	 */
	protected int facing = FORWARDS;


	/**
	 *  Creates new a MPEG4Face
	 */
	public MPEG4Face() { }

	/**
	 *  Animates the face according to the current FAP frame and FAPU information
	 *
	 *@param  frame  The current MPEG-4 FAP frame
	 */
	public abstract void animateFace(FAPData frame);


	/**
	 *  Draws the MPEG-4 face
	 *
	 *@param  g2d     The Graphics2D object to use in drawing
	 *@param  width   The width available to the face to draw
	 *@param  height  The height available to the face to draw
	 */
	public abstract void drawFace(Graphics2D g2d, int width, int height);
}

