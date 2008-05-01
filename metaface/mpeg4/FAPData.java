package metaface.mpeg4;
import java.util.*;

/**
 *  <p>
 *
 *  This class stores an MPEG-4 Facial Animation Parameter (FAP) frame. There
 *  are several methods for accessing animation values of the frame and
 *  manipulating them. An MPEG-4 fap is used to animate an MPEG-4 compliant face
 *  model, such as the two-dimensional models of the MetaFace framework.</p> <p>
 *
 *  Several successful facial animation systems have been developed in preceding
 *  years, but most suffer from the same limitation. Each of them uses a
 *  proprietary architecture and syntax for animating a synthetic face. This
 *  makes it hard to use these systems outside the application area for which
 *  they were constructed. The ISO/IEC international standard of MPEG-4, tries
 *  to overcome proprietary differences in the world of facial animation by
 *  defining a standard for the efficient representation and transportation of
 *  multimedia. This standard deals with many types of media, but the facial
 *  animation component defines a standard and efficient way of animating
 *  synthetic faces. MPEG-4 facial animation can utilise an Internet connection
 *  as slow as 1kbs for a talking head.</p> <p>
 *
 *  The facial animation component of MPEG-4 uses three inter-working concepts
 *  that make it possible to standardise synthetic faces and their animation.
 *  </p> <menu>
 *  <li> Feature points</li>
 *  <li> The neutral face</li>
 *  <li> Normalisation of values</li> </menu> <p>
 *
 *  MPEG-4 can control faces using high-level parameters for expression (for
 *  example: happy and sad) and visemes (the shape of the mouth when talking).
 *  There are also low-level parameters for animating standard "feature points"
 *  of a face.</p> <p>
 *
 *  Due to the efficient transportation of facial animation, MPEG-4 could be
 *  used within an ECA framework to minimise user delay. Basing facial animation
 *  on a well known standard should also allow different frameworks to work
 *  cohesively together. Having discussed low-level facial animation, the
 *  following section will now review ECA scripting technology used to control
 *  the lower level.</p> <IMG SRC="../../resources/mpeg4_fp.jpg"/>
 *
 *@author     Simon Beard 
 *@version    1.0
 */
public class FAPData {

	/**
	 *  The number of MPEG-4 FAP's
	 */
	public static int NUMOFFAP = 68;
	/**
	 *  The current viseme data
	 */
	private VisemeData viseme;
	/**
	 *  The current expression data
	 */
	private ExpressionData expression;
	/**
	 *  The current low level data
	 */
	private LowLevelFAPData[] lowLevelFAPs;
	/**
	 *  Flag identifying whether the FAP is null
	 */
	private boolean nullvar = false;


	/**
	 *  Creates an empty FAP data structure (values set to zero)
	 */
	public FAPData() {
		int i;
		lowLevelFAPs = new LowLevelFAPData[NUMOFFAP + 1];
		for (i = 0; i < NUMOFFAP + 1; i++) {
			lowLevelFAPs[i] = new LowLevelFAPData();
		}
		viseme = new VisemeData();
		expression = new ExpressionData();
		reset();
	}


	/**
	 *  Creates a FAP data structure from fap and mask strings
	 *
	 *@param  fapmask    A string representing the fap mask "0110...."
	 *@param  fapvalues  A string representing the fap vales "23 42 0...."
	 */
	public FAPData(String fapmask, String fapvalues) {

		//Temporary variables for holding the FAP values and masks
		int vis_mask;
		int exp_mask;
		int vis1;
		int vis2;
		int vis_blend;
		int vis_def;
		int exp1;
		int exp2;
		int in1;
		int in2;
		int exp_init = 0;
		int exp_def = 0;

		//Temporary holding variables
		String temp;
		int ptr;
		int blend;
		int[] ll_mask = new int[NUMOFFAP + 1];
		int[] ll_val = new int[NUMOFFAP + 1];

		//Counters
		int i;

		//Counters
		int j;

		//Counters
		int k;

		//Create empty FAP structure
		lowLevelFAPs = new LowLevelFAPData[NUMOFFAP + 1];
		for (i = 0; i < NUMOFFAP + 1; i++) {
			lowLevelFAPs[i] = new LowLevelFAPData();
		}
		viseme = new VisemeData();
		expression = new ExpressionData();
		reset();

		//Get masks from input string
		ptr = 0;
		temp = fapmask.substring(ptr, ptr + 1);
		ptr++;
		ptr++;
		vis_mask = new Integer(temp).intValue();
		temp = fapmask.substring(ptr, ptr + 1);
		ptr++;
		ptr++;
		exp_mask = new Integer(temp).intValue();
		for (j = 3; j < NUMOFFAP + 1; j++) {
			temp = fapmask.substring(ptr, ptr + 1);
			ptr++;
			ptr++;
			ll_mask[j] = new Integer(temp).intValue();
		}

		//Get rid of frame number
		ptr = 0;
		i = fapvalues.indexOf(" ", ptr);
		if (i < 0) {
			i = fapvalues.indexOf("\n", ptr);
		}
		if (i < 0) {
			i = fapvalues.length();
		}
		temp = fapvalues.substring(ptr, i);
		ptr = i + 1;

		//If the viseme mask is set -- process the viseme values from the input string
		if (vis_mask == 1) {
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			vis1 = new Integer(temp).intValue();
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			vis2 = new Integer(temp).intValue();
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			blend = new Integer(temp).intValue();
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			vis_def = new Integer(temp).intValue();

			//Set viseme values
			setVisemeValues(vis1, vis2, blend);
		}

		//If the expression mask is set -- process the viseme values from the input string
		if (exp_mask == 1) {
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			exp1 = new Integer(temp).intValue();
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			in1 = new Integer(temp).intValue();
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			exp2 = new Integer(temp).intValue();
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			in2 = new Integer(temp).intValue();
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			exp_init = new Integer(temp).intValue();
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			exp_def = new Integer(temp).intValue();

			//Set expression values
			setExpressionValues(exp1, in1, exp2, in2);
		}

		//If low level masks are set -- process from input string
		for (k = 3; k < NUMOFFAP + 1; k++) {
			if (ll_mask[k] == 1) {
				i = fapvalues.indexOf(" ", ptr);
				if (i < 0) {
					i = fapvalues.indexOf("\n", ptr);
				}
				if (i < 0) {
					i = fapvalues.length();
				}
				temp = fapvalues.substring(ptr, i);
				ptr = i + 1;
				ll_val[k] = new Integer(temp).intValue();

				//Set low level values
				setLowLevelValues(k, ll_val[k]);
			}
		}

		//Set the viseme, expression, and low level fap masks
		setVisemeVariables(vis_mask);
		setExpressionVariables(exp_mask, exp_init, exp_def);
		for (k = 3; k < NUMOFFAP + 1; k++) {
			setLowLevelVariables(k, ll_mask[k]);
		}
	}


	/**
	 *  Creates a FAP data structure from fap and mask byte arrays
	 *
	 *@param  fapmask    A byte array representing the fap mask (a group of 8 bit
	 *      masks is represented by a byte):<br>
	 *      01000000 01000011 01000000 01000011 ... is represented by bytes 64 67
	 *      64 67 ... and the byte array [@][C][@][C] ...
	 *@param  fapvalues  A byte array representing the fap values (values are
	 *      represented as byte characters):<br>
	 *      1 3 4 12 32 0 ... is represented by the byte array [1][ ][3][ ][4][
	 *      ][1][2][ ][3][2][ ][0] ...
	 */
	public FAPData(byte[] fapmask, byte[] fapvalues) {

		//Temporary holding variables
		String mask;
		String vals;
		boolean[] temp;
		int temp2;
		int[] ll_mask;
		int[] ll_val;
		int vis_mask;
		int exp_mask;
		int vis1;
		int vis2;
		int vis_blend;
		int vis_def;
		int exp1;
		int exp2;
		int in1;
		int in2;
		int exp_init = 0;
		int exp_def = 0;
		String temps;
		int blend;

		//Counters
		int ptr;
		int i;
		int j;
		int k;

		//Process the bit mask (represented as bytes)
		temp = new boolean[8];
		mask = new String("");

		//Process next byte
		for (i = 0; i < 9; i++) {

			//initialise mask array
			for (j = 7; j >= 0; j--) {
				temp[j] = false;
			}
			temp2 = (int) fapmask[i];
			if (temp2 < 0) {
				temp2 = 256 + temp2;
			}
			k = 128;
			for (j = 7; j >= 0; j--) {
				if (k > 0) {
					if (temp2 / k > 0) {
						temp[j] = true;
						temp2 = temp2 - k;
					}
					k = k / 2;
				}
			}

			//If a standard length bit mask
			if (i < 8) {
				for (j = 0; j <= 7; j++) {
					if (temp[j]) {
						mask = mask.concat("1 ");
					} else {
						mask = mask.concat("0 ");
					}
				}
			}
			//Else the last bits don't make a whole byte
			else {
				for (j = 4; j <= 7; j++) {
					if (temp[j]) {
						mask = mask.concat("1 ");
					} else {
						mask = mask.concat("0 ");
					}
				}
			}
		}
		mask = mask.substring(0, mask.length() - 1);
		vals = new String(fapvalues);

		//Create new empty FAP structure
		lowLevelFAPs = new LowLevelFAPData[NUMOFFAP + 1];
		for (i = 0; i < NUMOFFAP + 1; i++) {
			lowLevelFAPs[i] = new LowLevelFAPData();
		}
		viseme = new VisemeData();
		expression = new ExpressionData();
		reset();

		//Initialise temporary holding variables
		ptr = 0;
		ll_mask = new int[NUMOFFAP + 1];
		ll_val = new int[NUMOFFAP + 1];

		//Process fap values
		temps = mask.substring(ptr, ptr + 1);
		ptr++;
		ptr++;
		vis_mask = new Integer(temps).intValue();
		temps = mask.substring(ptr, ptr + 1);
		ptr++;
		ptr++;
		exp_mask = new Integer(temps).intValue();
		for (j = 3; j < NUMOFFAP + 1; j++) {
			temps = mask.substring(ptr, ptr + 1);
			ptr++;
			ptr++;
			ll_mask[j] = new Integer(temps).intValue();
		}

		//Get rid of frame number
		ptr = 0;
		i = vals.indexOf(" ", ptr);
		if (i < 0) {
			i = vals.indexOf("\n", ptr);
		}
		if (i < 0) {
			i = vals.length();
		}
		temps = vals.substring(ptr, i);
		ptr = i + 1;

		//If viseme mask is set, then get viseme values
		if (vis_mask == 1) {
			i = vals.indexOf(" ", ptr);
			if (i < 0) {
				i = vals.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = vals.length();
			}
			temps = vals.substring(ptr, i);
			ptr = i + 1;
			vis1 = new Integer(temps).intValue();
			i = vals.indexOf(" ", ptr);
			if (i < 0) {
				i = vals.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = vals.length();
			}
			temps = vals.substring(ptr, i);
			ptr = i + 1;
			vis2 = new Integer(temps).intValue();
			i = vals.indexOf(" ", ptr);
			if (i < 0) {
				i = vals.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = vals.length();
			}
			temps = vals.substring(ptr, i);
			ptr = i + 1;
			blend = new Integer(temps).intValue();
			i = vals.indexOf(" ", ptr);
			if (i < 0) {
				i = vals.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = vals.length();
			}
			temps = vals.substring(ptr, i);
			ptr = i + 1;
			vis_def = new Integer(temps).intValue();
			setVisemeValues(vis1, vis2, blend);
		}

		//If expression mask is set, then get expression values
		if (exp_mask == 1) {
			i = vals.indexOf(" ", ptr);
			if (i < 0) {
				i = vals.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = vals.length();
			}
			temps = vals.substring(ptr, i);
			ptr = i + 1;
			exp1 = new Integer(temps).intValue();
			i = vals.indexOf(" ", ptr);
			if (i < 0) {
				i = vals.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = vals.length();
			}
			temps = vals.substring(ptr, i);
			ptr = i + 1;
			in1 = new Integer(temps).intValue();
			i = vals.indexOf(" ", ptr);
			if (i < 0) {
				i = vals.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = vals.length();
			}
			temps = vals.substring(ptr, i);
			ptr = i + 1;
			exp2 = new Integer(temps).intValue();
			i = vals.indexOf(" ", ptr);
			if (i < 0) {
				i = vals.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = vals.length();
			}
			temps = vals.substring(ptr, i);
			ptr = i + 1;
			in2 = new Integer(temps).intValue();
			i = vals.indexOf(" ", ptr);
			if (i < 0) {
				i = vals.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = vals.length();
			}
			temps = vals.substring(ptr, i);
			ptr = i + 1;
			exp_init = new Integer(temps).intValue();
			i = vals.indexOf(" ", ptr);
			if (i < 0) {
				i = vals.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = vals.length();
			}
			temps = vals.substring(ptr, i);
			ptr = i + 1;
			exp_def = new Integer(temps).intValue();
			setExpressionValues(exp1, in1, exp2, in2);
		}

		//Process low level fap values if mask set
		for (k = 3; k < NUMOFFAP + 1; k++) {
			if (ll_mask[k] == 1) {
				i = vals.indexOf(" ", ptr);
				if (i < 0) {
					i = vals.indexOf("\n", ptr);
				}
				if (i < 0) {
					i = vals.length();
				}
				temps = vals.substring(ptr, i);
				ptr = i + 1;
				ll_val[k] = new Integer(temps).intValue();
				setLowLevelValues(k, ll_val[k]);
			}
		}

		//Update FAP structure with new values
		setVisemeVariables(vis_mask);
		setExpressionVariables(exp_mask, exp_init, exp_def);
		for (k = 3; k < NUMOFFAP + 1; k++) {
			setLowLevelVariables(k, ll_mask[k]);
		}
	}


	/**
	 *  Creates a duplicate FAP data structure from an existing FAPData object
	 *
	 *@param  f  The FAPData object to dublicate
	 */
	public FAPData(FAPData f) {
		int i;

		//Duplicate structure in input FAP

		//Create empty FAP structure
		lowLevelFAPs = new LowLevelFAPData[NUMOFFAP + 1];
		for (i = 0; i < NUMOFFAP + 1; i++) {
			lowLevelFAPs[i] = new LowLevelFAPData();
		}
		viseme = new VisemeData();
		expression = new ExpressionData();
		reset();

		//Set masks and values
		if (f.visemeMaskSet()) {
			setVisemeValues(f.getViseme1(), f.getViseme2(), f.getVisemeBlend());
			setVisemeVariables(1);
		}
		if (f.expressionMaskSet()) {
			setExpressionValues(f.getExpression1(), f.getExpressionIntensity1(), f.getExpression2(), f.getExpressionIntensity2());
			setExpressionVariables(1, f.getExpressionInitFace(), 0);
		}
		for (i = 0; i < NUMOFFAP + 1; i++) {
			if (f.lowLevelMaskSet(i)) {
				setLowLevelValues(i, f.getLowLevelValue(i));
				setLowLevelVariables(i, 1);
			}
		}
	}


	/**
	 *  Adjusts the existing FAP data structure according to the fap mask and value
	 *  strings
	 *
	 *@param  fapmask    A string representing the fap mask "0110...."
	 *@param  fapvalues  A string representing the fap vales "23 42 0...."
	 */
	public void adjustFAPData(String fapmask, String fapvalues) {

		//Temporary holding variables
		int vis_mask;
		int exp_mask;
		int vis1;
		int vis2;
		int vis_blend;
		int vis_def;
		int exp1;
		int exp2;
		int in1;
		int in2;
		int exp_init = 0;
		int exp_def = 0;
		String temp;
		int blend;
		char[] tempchar;
		int[] ll_mask;
		int[] ll_val;
		
		ll_mask = new int[NUMOFFAP + 1];
		ll_val = new int[NUMOFFAP + 1];

		//Counters
		int ptr;
		int i;
		int j;
		int k;

		//Reset the FAP structure
		reset();

		//Put strings into arrays for processing
		tempchar = new char[1];
		tempchar[0] = fapmask.charAt(0);
		vis_mask = Integer.parseInt(new String(tempchar));
		tempchar[0] = fapmask.charAt(2);
		exp_mask = Integer.parseInt(new String(tempchar));
		ptr = 3;
		for (j = 3; j < NUMOFFAP + 1; j++) {
			ptr++;
			ptr++;
			tempchar[0] = fapmask.charAt(ptr);
			ll_mask[j] = Integer.parseInt(new String(tempchar));
		}

		//Get rid of frame number
		ptr = 0;
		i = fapvalues.indexOf(" ", ptr);
		if (i < 0) {
			i = fapvalues.indexOf("\n", ptr);
		}
		if (i < 0) {
			i = fapvalues.length();
		}
		temp = fapvalues.substring(ptr, i);
		ptr = i + 1;

		//If viseme mask set then get viseme values
		if (vis_mask == 1) {
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			vis1 = new Integer(temp).intValue();
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			vis2 = new Integer(temp).intValue();
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			blend = new Integer(temp).intValue();
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			vis_def = new Integer(temp).intValue();

			//Set values
			setVisemeValues(vis1, vis2, blend);
		}

		//If expression mask set then get expression values
		if (exp_mask == 1) {
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			exp1 = new Integer(temp).intValue();
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			in1 = new Integer(temp).intValue();
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			exp2 = new Integer(temp).intValue();
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			in2 = new Integer(temp).intValue();
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			exp_init = new Integer(temp).intValue();
			i = fapvalues.indexOf(" ", ptr);
			if (i < 0) {
				i = fapvalues.indexOf("\n", ptr);
			}
			if (i < 0) {
				i = fapvalues.length();
			}
			temp = fapvalues.substring(ptr, i);
			ptr = i + 1;
			exp_def = new Integer(temp).intValue();

			//Set values
			setExpressionValues(exp1, in1, exp2, in2);
		}

		//Process low level masks and values
		for (k = 3; k < NUMOFFAP + 1; k++) {
			if (ll_mask[k] == 1) {
				i = fapvalues.indexOf(" ", ptr);
				if (i < 0) {
					i = fapvalues.indexOf("\n", ptr);
				}
				if (i < 0) {
					i = fapvalues.length();
				}
				temp = fapvalues.substring(ptr, i);
				ptr = i + 1;
				ll_val[k] = new Integer(temp).intValue();

				//Set values
				setLowLevelValues(k, ll_val[k]);
			}
		}

		//Set masks
		setVisemeVariables(vis_mask);
		setExpressionVariables(exp_mask, exp_init, exp_def);
		for (k = 3; k < NUMOFFAP + 1; k++) {
			setLowLevelVariables(k, ll_mask[k]);
		}
	}


	/**
	 *  Adjusts the existing FAP data structure according to the fap mask and value
	 *  strings
	 *
	 *@param  f  FAPData structure to use to adjust this FAP
	 */
	public void adjustFAPData(FAPData f) {
		int i;
		
		reset();
		if (f.visemeMaskSet()) {
			setVisemeValues(f.getViseme1(), f.getViseme2(), f.getVisemeBlend());
			setVisemeVariables(1);
		}
		if (f.expressionMaskSet()) {
			setExpressionValues(f.getExpression1(), f.getExpressionIntensity1(), f.getExpression2(), f.getExpressionIntensity2());
			setExpressionVariables(1, f.getExpressionInitFace(), 0);
		}
		for (i = 0; i < NUMOFFAP + 1; i++) {
			if (f.lowLevelMaskSet(i)) {
				setLowLevelValues(i, f.getLowLevelValue(i));
				setLowLevelVariables(i, 1);
			}
		}
	}


	/**
	 *  Resets the FAP data structure to zero values
	 */
	public void reset() {
		int i;

		nullvar = false;

		setVisemeValues(0, 0, 0);
		setVisemeVariables(0);
		setExpressionValues(0, 0, 0, 0);
		setExpressionVariables(0, 0, 0);
		for (i = 0; i < NUMOFFAP + 1; i++) {
			setLowLevelValues(i, 0);
			setLowLevelVariables(i, 0);
		}
	}


	/**
	 *  Get the bit mask of the FAP as a byte array
	 *
	 *@return    A byte array representing the bit mask of this FAP (a group of 8
	 *      bit masks is represented by a byte):<br>
	 *      01000000 01000011 01000000 01000011 ... is represented by bytes 64 67
	 *      64 67 ... and the byte array [@][C][@][C] ...
	 */
	public byte[] getBitMaskBytes() {
		byte[] list;
		int temp;
		int temp2;
		int i;
		int j;
		int x;
		int y;
		boolean[] byterep;
		byte abyte;

		//Initialise
		list = new byte[9];
		byterep = new boolean[72];

		//Set masks according to visemes, expressions and low level variables
		byterep[0] = visemeMaskSet();
		byterep[1] = expressionMaskSet();
		for (i = 3; i < 69; i++) {
			byterep[i - 1] = lowLevelMaskSet(i);
		}
		for (i = 68; i < 72; i++) {
			byterep[i] = false;
		}

		//Transform mask array into bytes (group of 8 bits)
		for (j = 0; j < 9; j++) {
			temp = 0;
			y = 1;
			for (i = j * 8; i < (j * 8) + 8; i++) {
				if (byterep[i]) {
					temp2 = 1;
				} else {
					temp2 = 0;
				}
				temp = temp + (temp2 * y);
				y = y + y;
			}
			list[j] = (byte) temp;
		}

		return (list);
	}


	/**
	 *  Get the values of the FAP as a byte array
	 *
	 *@param  frame  Description of the Parameter
	 *@return        A byte array representing the values of this FAP (values are
	 *      represented as byte characters):<br>
	 *      1 3 4 12 32 0 ... is represented by the byte array [1][ ][3][ ][4][
	 *      ][1][2][ ][3][2][ ][0] ...
	 */
	public byte[] getValueBytes(int frame) {
		int j;
		String tempstring2;
		Vector tempvec;

		//Initialise
		tempvec = new Vector();
		tempstring2 = new String("");

		//If masks are set then add values to temporary string
		if (visemeMaskSet()) {
			tempstring2 = tempstring2.concat(frame + " ");
			tempstring2 = tempstring2.concat(getViseme1() + " ");
			tempstring2 = tempstring2.concat(getViseme2() + " ");
			tempstring2 = tempstring2.concat(getVisemeBlend() + " 0 ");
		} else {
			tempstring2 = tempstring2.concat(frame + " ");
		}
		if (expressionMaskSet()) {
			tempstring2 = tempstring2.concat(getExpression1() + " ");
			tempstring2 = tempstring2.concat(getExpressionIntensity1() + " ");
			tempstring2 = tempstring2.concat(getExpression2() + " ");
			tempstring2 = tempstring2.concat(getExpressionIntensity2() + " 0 0 ");
		}
		for (j = 3; j < NUMOFFAP + 1; j++) {
			if (lowLevelMaskSet(j)) {
				tempstring2 = tempstring2.concat(getLowLevelValue(j) + " ");
			}
		}

		//return string as a byte array
		return (tempstring2.getBytes());
	}


	/**
	 *  Returns if the FAP is null
	 *
	 *@return     A boolean representing if the FAPData structure is null
	 */
	public boolean isNull() {
		return (nullvar);
	}


	/**
	 *  Set the FAP to null
	 *
	 *@param  flag  A variable representing whether the FAPData structure is set to
	 *      null
	 */
	public void setNull(boolean flag) {
		nullvar = flag;
	}


	/**
	 *  Utility method for parsing strings and geting text between whitespace
	 *
	 *@param  data  The string to parse
	 *@param  ptr   The current position in the string
	 *@return       The string found from the current position to the next
	 *      whitespace
	 */
	private String getNextString(String data, int ptr) {
		int j;
		int start;
		String temp;
		int end;

		//Initialise
		end = data.length();
		j = ptr;
		if (j >= end) {
			ptr = j;
			return ("0");
		}

		//Get next white space
		while ((data.charAt(j) == ' ') || (data.charAt(j) == '\t') || (data.charAt(j) == '\n') || (data.charAt(j) == '\0')) {
			if (j >= end - 1) {
				if ((data.charAt(end - 1) != ' ') && (data.charAt(end - 1) != '\t') && (data.charAt(end - 1) != '\n') && (data.charAt(end - 1) != '\0')) {
					ptr = j + 1;
					return (String.valueOf(data.charAt(end - 1)));
				} else {
					ptr = j + 1;
					return ("0");
				}
			}
			j++;
		}

		//Get following white space
		start = j;
		while ((data.charAt(j) != ' ') && (data.charAt(j) != '\t') && (data.charAt(j) != '\n') && (data.charAt(j) != '\0')) {
			if (j >= end - 1) {
				if (j >= start - 1) {
					temp = data.substring(start, j + 1);
					ptr = j + 1;
					return (temp);
				} else {
					ptr = j + 1;
					return ("0");
				}
			}
			j++;
		}

		//Grab the string in between the white space
		temp = data.substring(start, j);
		ptr = j;
		return (temp);
	}


	/**
	 *  A utility funtion to calculate the duration of a vector of visemes
	 *
	 *@param  visemeinfo  A vector of MPEG-4 numbered visemes and durations
	 *      (milliseconds):<br>
	 *      [viseme1][duration1][viseme2][duration2][viseme3] ...
	 *@return             The total duration of the visemes in milliseconds
	 */
	public static int totalDuration(Vector visemeinfo) {
		int i;
		int duration;

		duration = 0;
		for (i = 0; i < visemeinfo.size(); i++) {
			i++;
			duration = duration + ((Integer) (visemeinfo.elementAt(i))).intValue();
		}

		return (duration);
	}


	/**
	 *  A utility funtion that translates between visemes and FAPS
	 *
	 *@param  fapstring  Description of the Parameter
	 *@return            A vector containing MPEG-4 FAPData objects
	 */
	public static Vector FAPStringToFAPVector(String fapstring) {
		Vector tmp;
		int i;
		int j;
		String mask;
		String vals;

		//Initialise
		tmp = new Vector();

		//Create header
		i = fapstring.indexOf("2.1 Spiky_b0y");
		i = fapstring.indexOf("\n", i);
		i++;
		j = i;

		//Process fap string
		while (i >= 0) {
			i = fapstring.indexOf("\n", i);
			mask = fapstring.substring(j, i);
			i++;
			j = i;
			i = fapstring.indexOf("\n", i);
			if (i == -1) {
				i = fapstring.length();
			}
			vals = fapstring.substring(j, i);
			if (i < fapstring.length() - 1) {
				i++;
				j = i;
			} else {
				i = -1;
			}

			//Add new FAPData to output vector
			tmp.add(new FAPData(mask, vals));
		}

		return (tmp);
	}


	/**
	 *  A utility funtion that translates between visemes and FAPS
	 *
	 *@param  visemes    A vector of MPEG-4 numbered visemes (milliseconds):<br>
	 *      [viseme1][duration1][viseme2][duration2][viseme3] ...
	 *@param  preverror  The previous rounding area of constructing FAPs
	 *@param  fps        The frames per second of the animation
	 *@param  header     A flag signifying whether a header is needed for the FAPs
	 *@return            A vector containing MPEG-4 FAP strings
	 */
	public static Vector visemesToFAPString(Vector visemes, float preverror, int fps, boolean header) {
		String temp = null;
		StringBuffer tempbuff = null;
		Vector tempvector = null;
		int prevvis = 0;
		int nextvis = 0;
		int i;
		int j;
		Integer tempint;
		float totaltime;
		int step;
		int blend;
		int frameno = 0;
		int t1 = 0;
		float numframes;

		//Initialise
		totaltime = preverror;
		tempvector = new Vector();

		//Process visemes
		for (i = 0; i < visemes.size(); i++) {
			//Get visemes
			i++;
			if (i + 1 >= visemes.size()) {
				nextvis = ((Integer) visemes.elementAt(i - 1)).intValue();
			} else {
				nextvis = ((Integer) visemes.elementAt(i + 1)).intValue();
			}

			//Calculate frames
			tempint = (Integer) visemes.elementAt(i);
			totaltime = totaltime + ((float) (tempint.intValue()) / 1000f);
			tempint = (Integer) visemes.elementAt(i - 1);
			numframes = totaltime * ((float) fps);
			step = (int) (Math.floor((double) (numframes)));
			step = step - frameno;

			//Blend with previous and next viseme
			blend = 0;
			if ((prevvis == 0) && (nextvis == 0) && (tempint.intValue() == 0)) {
				for (j = 0; j < step; j++) {
					temp = new String("0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ");
					tempvector.add(new String(temp));
					tempbuff = new StringBuffer((String.valueOf(frameno)));
					tempvector.add(new String(tempbuff.toString()));
					frameno++;
				}
				tempint = (Integer) visemes.elementAt(i - 1);
				prevvis = tempint.intValue();
			}
			//No blending required
			else {
				for (j = 0; j < (int) ((float) step * 0.75f); j++) {
					temp = new String("1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ");
					tempvector.add(new String(temp));
					tempbuff = new StringBuffer((String.valueOf(frameno)));
					tempbuff.append(" ");
					tempbuff.append(String.valueOf(tempint.intValue()));
					tempbuff.append(" ");
					tempbuff.append(String.valueOf(prevvis));
					tempbuff.append(" ");
					tempbuff.append(String.valueOf((int) ((31f / ((float) step * 0.75f)) * ((float) j + 1f) + 31f))
					/*
					 *  "63"
					 */
							);
					tempbuff.append(" 0");
					tempvector.add(new String(tempbuff.toString()));
					frameno++;
				}
				for (j = 0; j < (int) (step - (((float) step * 0.75f))); j++) {
					temp = new String("1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ");
					tempvector.add(new String(temp));
					tempbuff = new StringBuffer((String.valueOf(frameno)));
					tempbuff.append(" ");
					tempbuff.append(String.valueOf(tempint.intValue()));
					tempbuff.append(" ");
					tempbuff.append(String.valueOf(tempint.intValue()));
					tempbuff.append(" ");
					tempbuff.append(
					/*
					 *  String.valueOf ((int)((63f/step)*(j+1)))
					 */
							"63");
					tempbuff.append(" 0");
					tempvector.add(new String(tempbuff.toString()));
					frameno++;
				}
				tempint = (Integer) visemes.elementAt(i - 1);
				prevvis = tempint.intValue();
			}
		}

		//Construct header if needed
		if (header) {
			tempbuff = new StringBuffer("#2D Talking Head FAP file\n2.1 Spiky_b0y ");
			tempbuff.append(String.valueOf((int) (fps)));
			tempbuff.append(" ");
			tempbuff.append(String.valueOf((int) (tempvector.size() / 2)));
			tempvector.add(0, new String(tempbuff.toString()));
		}

		return tempvector;
	}


	/**
	 *  Translate a list of FAPData structures to a vector of FAP strings
	 *
	 *@param  faps  A list of FAPData structures to translate
	 *@return       A vector of FAP strings
	 */
	public static Vector FAPDataToFAPString(Vector faps) {
		int i;
		int j;
		String tempstring1;
		String tempstring2;
		FAPData tempfap;
		Vector tempvec;

		tempvec = new Vector();

		//For each FAP
		for (i = 0; i < faps.size(); i++) {

			//Create a string bitmask string and value string depending on the FAP mask and values
			tempstring1 = new String("");
			tempstring2 = new String("");
			tempfap = (FAPData) faps.elementAt(i);
			if (tempfap.visemeMaskSet()) {
				tempstring1 = tempstring1.concat("1 ");
				tempstring2 = tempstring2.concat(i + " ");
				tempstring2 = tempstring2.concat(tempfap.getViseme1() + " ");
				tempstring2 = tempstring2.concat(tempfap.getViseme2() + " ");
				tempstring2 = tempstring2.concat(tempfap.getVisemeBlend() + " 0 ");
			} else {
				tempstring1 = tempstring1.concat("0 ");
				tempstring2 = tempstring2.concat(i + " ");
			}
			if (tempfap.expressionMaskSet()) {
				tempstring1 = tempstring1.concat("1 ");
				tempstring2 = tempstring2.concat(tempfap.getExpression1() + " ");
				tempstring2 = tempstring2.concat(tempfap.getExpressionIntensity1() + " ");
				tempstring2 = tempstring2.concat(tempfap.getExpression2() + " ");
				tempstring2 = tempstring2.concat(tempfap.getExpressionIntensity2() + " 0 0 ");
			} else {
				tempstring1 = tempstring1.concat("0 ");
			}
			for (j = 3; j < NUMOFFAP + 1; j++) {
				if (tempfap.lowLevelMaskSet(j)) {
					tempstring1 = tempstring1.concat("1 ");
					tempstring2 = tempstring2.concat(tempfap.getLowLevelValue(j) + " ");
				} else {
					tempstring1 = tempstring1.concat("0 ");
				}
			}

			//Add strings to vector
			tempvec.add(tempstring1);
			tempvec.add(tempstring2);
		}
		return (tempvec);
	}


	/**
	 *  Translate a list of visemes to a vector of FAPData structures
	 *
	 *@param  visemes    A list of visemes and duration (milliseconds):<br>
	 *      [viseme1][duration1][viseme2][duration2][viseme3] ...
	 *@param  preverror  The previous error (milliseconds) calculated when doing
	 *      this translation
	 *@param  fps        The frames per second for FAP animation
	 *@return            A vector of FAPData structures
	 */
	public static Vector visemesToFAPData(Vector visemes, float preverror, int fps) {
		String temp = null;
		int prevvis = 0;
		int nextvis = 0;
		int i;
		int j;
		Integer tempint;
		float totaltime;
		int step;
		int blend;
		int t1 = 0;
		float numframes;
		int[] temparray;
		Vector tempvector;
		FAPData tempfap;
		int frameno = 0;

		//Initialise
		totaltime = preverror;
		tempvector = new Vector();

		//For each visemes
		for (i = 0; i < visemes.size(); i++) {
			i++;
			if (i + 1 >= visemes.size()) {
				nextvis = ((Integer) visemes.elementAt(i - 1)).intValue();
			} else {
				nextvis = ((Integer) visemes.elementAt(i + 1)).intValue();
			}

			//Calculate frames
			tempint = (Integer) visemes.elementAt(i);
			totaltime = totaltime + ((float) (tempint.intValue()) / 1000f);
			tempint = (Integer) visemes.elementAt(i - 1);
			numframes = totaltime * ((float) fps);
			step = (int) (Math.floor((double) (numframes)));
			step = step - frameno;
			blend = 0;

			//Blend with previous and next viseme
			if ((prevvis == 0) && (nextvis == 0) && (tempint.intValue() == 0)) {
				for (j = 0; j < step; j++) {
					tempfap = new FAPData();
					tempvector.add(new FAPData(tempfap));
					frameno++;
				}
				tempint = (Integer) visemes.elementAt(i - 1);
				prevvis = tempint.intValue();
			}
			//No blending needed
			else {
				for (j = 0; j < (int) ((float) step * 0.75f); j++) {
					tempfap = new FAPData();
					tempfap.setVisemeVariables(1);
					tempfap.setVisemeValues(tempint.intValue(), prevvis, (int) ((31f / ((float) step * 0.75f)) * ((float) j + 1f) + 31f));
					tempvector.add(new FAPData(tempfap));
					frameno++;
				}
				for (j = 0; j < (int) (step - (((float) step * 0.75f))); j++) {
					tempfap = new FAPData();
					tempfap.setVisemeVariables(1);
					tempfap.setVisemeValues(tempint.intValue(), tempint.intValue(), 63);
					tempvector.add(new FAPData(tempfap));
					frameno++;
				}
				tempint = (Integer) visemes.elementAt(i - 1);
				prevvis = tempint.intValue();
			}
		}
		return tempvector;
	}


	/**
	 *  Returns the MPEG-4 FAP string representation of the current frame
	 *
	 *@param  framenum  The sequential order of the FAP
	 *@return           A string representing the MPEG-4 FAP
	 */
	public String toString(int framenum) {
		int i;
		String temp;

		//Check internal variables and construct a string
		temp = new String("");
		if (visemeMaskSet()) {
			temp = temp.concat("1");
		} else {
			temp = temp.concat("0");
		}
		if (expressionMaskSet()) {
			temp = temp.concat(" 1");
		} else {
			temp = temp.concat(" 0");
		}
		for (i = 3; i < NUMOFFAP + 1; i++) {
			if (lowLevelMaskSet(i)) {
				temp = temp.concat(" 1");
			} else {
				temp = temp.concat(" 0");
			}
		}
		temp = temp.concat("\n" + framenum + " ");
		if (visemeMaskSet()) {
			temp = temp.concat(String.valueOf(getViseme1()) + " ");
			temp = temp.concat(String.valueOf(getViseme2()) + " ");
			temp = temp.concat(String.valueOf(getVisemeBlend()) + " 0 ");
		}
		if (expressionMaskSet()) {
			temp = temp.concat(String.valueOf(getExpression1()) + " ");
			temp = temp.concat(String.valueOf(getExpressionIntensity1()) + " ");
			temp = temp.concat(String.valueOf(getExpression2()) + " ");
			temp = temp.concat(String.valueOf(getExpressionIntensity2()) + " 0 0 ");
		}
		for (i = 3; i < NUMOFFAP + 1; i++) {
			if (lowLevelMaskSet(i)) {
				temp = temp.concat(String.valueOf(getLowLevelValue(i)) + " ");
			}
		}
		temp = temp.substring(0, temp.length() - 1);
		temp = temp.concat("\n");
		return temp;
	}


	/**
	 *  Checks to see if the mask of a particular low-level MPEG-4 feature point is
	 *  set
	 *
	 *@param  i  The MPEG-4 number for the feature point
	 *@return    true if the mask is set (i.e. there is a value set), or false if
	 *      not
	 */
	public boolean lowLevelMaskSet(int i) {
		if (lowLevelFAPs[i].groupMask == 1) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 *  Checks to see if the viseme mask is set
	 *
	 *@return    True if the mask is set (i.e. there is a value set), or false if
	 *      not
	 */
	public boolean visemeMaskSet() {
		if (viseme.groupMask == 1) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 *  Checks to see if the expression mask is set
	 *
	 *@return    true if the mask is set (i.e. there is a value set), or false if
	 *      not
	 */
	public boolean expressionMaskSet() {
		if (expression.groupMask == 1) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 *  Gets a low-level MPEG-4 feature point value
	 *
	 *@param  i  The MPEG-4 number for the feature point
	 *@return    The MPEG-4 value of the low-level feature point
	 */
	public int getLowLevelValue(int i) {
		return (lowLevelFAPs[i].value);
	}


	/**
	 *  Gets the viseme1 MPEG-4 value
	 *
	 *@return    The MPEG-4 value
	 */
	public int getViseme1() {
		return (viseme.viseme1);
	}


	/**
	 *  Gets the viseme2 MPEG-4 value
	 *
	 *@return    The MPEG-4 value
	 */
	public int getViseme2() {
		return (viseme.viseme2);
	}


	/**
	 *  Gets the viseme blend MPEG-4 value
	 *
	 *@return    The MPEG-4 value
	 */
	public int getVisemeBlend() {
		return (viseme.blend);
	}


	/**
	 *  Gets the expression initFace MPEG-4 value
	 *
	 *@return    The MPEG-4 value
	 */
	public int getExpressionInitFace() {
		return (expression.initBit);
	}


	/**
	 *  Gets the expression1 MPEG-4 value
	 *
	 *@return    The MPEG-4 value
	 */
	public int getExpression1() {
		return (expression.expression1);
	}


	/**
	 *  Gets the expression2 MPEG-4 value
	 *
	 *@return    The MPEG-4 value
	 */
	public int getExpression2() {
		return (expression.expression2);
	}


	/**
	 *  Gets the expression intensity1 MPEG-4 value
	 *
	 *@return    The MPEG-4 value
	 */
	public int getExpressionIntensity1() {
		return (expression.intensity1);
	}


	/**
	 *  Gets the expression intensity2 MPEG-4 value
	 *
	 *@return    The MPEG-4 value
	 */
	public int getExpressionIntensity2() {
		return (expression.intensity2);
	}


	/**
	 *  Sets the viseme values
	 *
	 *@param  vis1   The value of MPEG-4 viseme1
	 *@param  vis2   The value of MPEG-4 viseme2
	 *@param  blend  The new visemeValues value
	 */
	public void setVisemeValues(int vis1, int vis2, int blend) {
		viseme.viseme1 = vis1;
		viseme.viseme2 = vis2;
		viseme.blend = blend;
	}


	/**
	 *  Sets the viseme mask
	 *
	 *@param  gm  The viseme group mask (if value is set)
	 */
	public void setVisemeVariables(int gm) {
		viseme.groupMask = gm;
	}


	/**
	 *  Sets the expression values
	 *
	 *@param  ex1  The value of MPEG-4 expression1
	 *@param  in1  The value of MPEG-4 expression intensity1
	 *@param  ex2  The value of MPEG-4 expression2
	 *@param  in2  The value of MPEG-4 expression intensity2
	 */
	public void setExpressionValues(int ex1, int in1, int ex2, int in2) {
		expression.expression1 = ex1;
		expression.expression2 = ex2;
		expression.intensity1 = in1;
		expression.intensity2 = in2;
	}


	/**
	 *  Sets the expression mask
	 *
	 *@param  gm     The expression group mask (if value is set)
	 *@param  initf  Initialise face (if current face to become neutral position)
	 *@param  exdef  Define expression (put current expression in facial animation
	 *      table)
	 */
	public void setExpressionVariables(int gm, int initf, int exdef) {
		expression.groupMask = gm;
		expression.initBit = initf;
		expression.defBit = exdef;
	}


	/**
	 *  Sets the low-level feature point values
	 *
	 *@param  index  The MPEG-4 feature point to set
	 *@param  value  The value to use
	 */
	public void setLowLevelValues(int index, int value) {
		while (value > (2d * Math.PI * 100000d)) {
			value = value - (int) (2d * Math.PI * 100000d);
		}
		while (value < (-2d * Math.PI * 100000d)) {
			value = value + (int) (2d * Math.PI * 100000d);
		}
		lowLevelFAPs[index].value = value;
	}


	/**
	 *  Sets the low-level group mask
	 *
	 *@param  index  The MPEG-4 feature point to set
	 *@param  gm     The low-level group mask
	 */
	public void setLowLevelVariables(int index, int gm) {
		lowLevelFAPs[index].groupMask = gm;
	}
}
