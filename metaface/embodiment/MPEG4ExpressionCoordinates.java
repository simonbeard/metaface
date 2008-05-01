package metaface.embodiment;

/**
 *  ExpressionCoordinates is a utility class that will quickly transform a 3D
 *  array of facial coordinates according to the current expression.
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        SpikyMPEG4Face
 *@see        GirlMPEG4Face
 */
public class MPEG4ExpressionCoordinates extends java.lang.Object {
	/**
	 *  A store of facial coordinates, ordered by emotion, facial feature, and
	 *  successive points:<BR>
	 *  <CODE><br/>
	 *  NEUTRAL<br/>
	 *  &nbsp;-- EYEBROW_LEFT<br/>
	 *  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-- 12.3,16.7,45.7, ...<br/>
	 *  &nbsp;-- EYEBROW_RIGHT<br/>
	 *  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-- 56.3,16.9,55.2, ...<br/>
	 *  &nbsp;-- ...<br/>
	 *  JOY<br/>
	 *  &nbsp;-- EYEBROW_LEFT<br/>
	 *  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-- ...<br/>
	 *  &nbsp;-- EYEBROW_RIGHT<br/>
	 *  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-- ...<br/>
	 *  &nbsp;-- ...<br/>
	 *  SADNESS<br/>
	 *  &nbsp;-- EYEBROW_LEFT<br/>
	 *  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-- ...<br/>
	 *  &nbsp;-- EYEBROW_RIGHT<br/>
	 *  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-- ...<br/>
	 *  &nbsp;-- ...<br/>
	 *  ANGER<br/>
	 *  ...<br/>
	 *  </CODE>
	 */
	private float coordinates[][][];
	/**
	 *  Displacements of visemes from the neutral position
	 */
	private float visemeDisplacements[][];
	/**
	 *  Stores the longest list of points for a facial feature (minimise searching)
	 */
	private int largestArraySize;
	/**
	 *  If set then the current facial positions of feature points will become the
	 *  neutral position
	 */
	private int initFace;

	/**
	 *  Array index representing the expression
	 */
	public static int NEUTRAL = 0;
	/**
	 *  Array index representing the expression
	 */
	public static int JOY = 1;
	/**
	 *  Array index representing the expression
	 */
	public static int SADNESS = 2;
	/**
	 *  Array index representing the expression
	 */
	public static int ANGER = 3;
	/**
	 *  Array index representing the expression
	 */
	public static int FEAR = 4;
	/**
	 *  Array index representing the expression
	 */
	public static int DISGUST = 5;
	/**
	 *  Array index representing the expression
	 */
	public static int SURPRISE = 6;
	/**
	 *  Number of expressions
	 */
	public static int NUM_EXPRESSIONS = 6;

	/**
	 *  Array index representing the facial feature
	 */
	public static int EYEBROW_LEFT = 0;
	/**
	 *  Array index representing the facial feature
	 */
	public static int EYEBROW_RIGHT = 1;
	/**
	 *  Array index representing the facial feature
	 */
	public static int MOUTH = 2;
	/**
	 *  Array index representing the facial feature
	 */
	public static int PUPIL_LEFT = 3;
	/**
	 *  Array index representing the facial feature
	 */
	public static int PUPIL_RIGHT = 4;
	/**
	 *  Array index representing the facial feature
	 */
	public static int EYELID_TOP_LEFT = 5;
	/**
	 *  Array index representing the facial feature
	 */
	public static int EYELID_TOP_RIGHT = 6;
	/**
	 *  Array index representing the facial feature
	 */
	public static int EYELID_BOTTOM_LEFT = 7;
	/**
	 *  Array index representing the facial feature
	 */
	public static int EYELID_BOTTOM_RIGHT = 8;
	/**
	 *  Array index representing the facial feature
	 */
	public static int BROW = 9;
	/**
	 *  Array index representing an undefined facial feature
	 */
	public static int UNDEFINED = 10;
	/**
	 *  Number of facial features
	 */
	public static int NUM_EXPRESSIVE_PARTS = 11;


	/**
	 *  Constructs the central storage
	 *
	 *@param  largestarraysize  The largest array size (normally the list of the
	 *      mouth positions)
	 */
	public MPEG4ExpressionCoordinates(int largestarraysize) {
		largestArraySize = largestarraysize;
		coordinates = new float[NUM_EXPRESSIONS + 2][NUM_EXPRESSIVE_PARTS][largestArraySize];
		visemeDisplacements = new float[15][16];
		initFace = 0;
	}


	/**
	 *  Sets the neutral position to the current expression, and updates facial
	 *  coordinates
	 *
	 *@param  expression1  MPEG-4 number for expression 1
	 *@param  expression2  MPEG-4 number for expression 2
	 *@param  intensity1   MPEG-4 number for the intensity of expression 1
	 *@param  intensity2   MPEG-4 number for the intensity of expression 2
	 */
	public void setInitFace(int expression1, int expression2, int intensity1, int intensity2) {
		int i;
		int j;

		for (i = 0; i < NUM_EXPRESSIVE_PARTS; i++) {
			for (j = 0; j < largestArraySize; j++) {
				coordinates[NUM_EXPRESSIONS + 1][i][j] = ((intensity1 / 63f) * (coordinates[expression1][i][j] - coordinates[initFace][i][j]))
						 + ((intensity2 / 63f) * (coordinates[expression2][i][j] - coordinates[initFace][i][j]));
				coordinates[NUM_EXPRESSIONS + 1][i][j] = coordinates[initFace][i][j] + coordinates[NUM_EXPRESSIONS + 1][i][j];
			}
		}
		initFace = NUM_EXPRESSIONS + 1;
	}


	/**
	 *  Returns the array index of the current neutral position (initFace)
	 *
	 *@return    Current neutral position
	 */
	public int getInitFace() {
		return initFace;
	}


	/**
	 *  Sets the coordinates for a particular expression and facial feature
	 *
	 *@param  expression  Expression array number
	 *@param  part        Facial feature array number
	 *@param  values      An array of coordinates
	 *@param  numValues   The length of the array of coordinates
	 */
	public void setCoords(int expression, int part, float values[], int numValues) {
		int i;

		for (i = 0; i < numValues; i++) {
			coordinates[expression][part][i] = values[i];
		}
	}


	/**
	 *  Sets the coordinates for a particular viseme
	 *
	 *@param  viseme     Viseme array number
	 *@param  values     An array of coordinates
	 *@param  numValues  The length of the array of coordinates
	 */
	public void setVisemeDisplacement(int viseme, float values[], int numValues) {
		int i;

		for (i = 0; i < numValues; i++) {
			visemeDisplacements[viseme][i] = values[i];
		}
	}


	/**
	 *  Tranforms and gets the coordinates for a particular mixed expression and
	 *  facial feature
	 *
	 *@param  expression1  MPEG-4 number for expression 1
	 *@param  expression2  MPEG-4 number for expression 2
	 *@param  intensity1   MPEG-4 number for the intensity of expression 1
	 *@param  intensity2   MPEG-4 number for the intensity of expression 2
	 *@param  part         Facial feature array number
	 *@param  numValues    The number of coordinates to return
	 *@return              An array of transformed facial feature coodinates
	 */
	public float[] getCoords(int expression1, int expression2, int intensity1, int intensity2, int part, int numValues) {
		int i;
		float temp[];

		temp = new float[numValues];
		for (i = 0; i < numValues; i++) {
			temp[i] = ((intensity1 / 63f) * (coordinates[initFace][part][i] - coordinates[expression1][part][i]))
					 + ((intensity2 / 63f) * (coordinates[initFace][part][i] - coordinates[expression2][part][i]));
			temp[i] = coordinates[initFace][part][i] - temp[i];
		}
		return temp;
	}


	/**
	 *  Tranforms and gets the coordinates for a particular mixed expression and
	 *  viseme
	 *
	 *@param  expression1  MPEG-4 number for expression 1
	 *@param  expression2  MPEG-4 number for expression 2
	 *@param  intensity1   MPEG-4 number for the intensity of expression 1
	 *@param  intensity2   MPEG-4 number for the intensity of expression 2
	 *@param  blend        MPEG-4 blend for visemes
	 *@param  numValues    The number of coordinates to return
	 *@param  viseme1      Description of the Parameter
	 *@param  viseme2      Description of the Parameter
	 *@return              An array of transformed facial feature coodinates
	 */
	public float[] getViseme(int expression1, int expression2, int intensity1, int intensity2, int viseme1, int viseme2, int blend, int numValues) {
		int i;
		float temp[];

		temp = new float[numValues];
		for (i = 0; i < numValues; i++) {
			temp[i] = ((intensity1 / 63f) * (coordinates[initFace][MOUTH][i] - coordinates[expression1][MOUTH][i]))
					 + ((intensity2 / 63f) * (coordinates[initFace][MOUTH][i] - coordinates[expression2][MOUTH][i]));
			temp[i] = coordinates[initFace][MOUTH][i] - temp[i];
			temp[i] = temp[i] + ((visemeDisplacements[viseme1][i] * (blend / 63f)) + (visemeDisplacements[viseme2][i] * (1f - (blend / 63f))));
		}
		return (temp);
	}
}
