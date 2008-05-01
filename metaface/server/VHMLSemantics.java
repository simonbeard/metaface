package metaface.server;
import java.util.*;
import metaface.mpeg4.*;
/**
 *  This class is designed to interpret VHML facial elements and render using
 *  MPEG-4 FAPs. This class can be extended to allow different interpretations
 *  of VHML documents.
 *
 *@author     Simon Beard
 *@version    1.0
 *@see        VHMLParser
 *@see        VHMLReader
 */
class VHMLSemantics {
	private int currentFrame = 0;
	private Vector newfaps;
	private int fps;
	private String headerstr;
	private int currentexpressionblend;
	private String[] expressionmap;
	private FAPData template;

	/**
	 *  Index of VHML element
	 */
	private final static int AFRAID = 0;
	/**
	 *  Index of VHML element
	 */
	private final static int ANGRY = 1;
	/**
	 *  Index of VHML element
	 */
	private final static int CONFUSED = 2;
	/**
	 *  Index of VHML element
	 */
	private final static int DAZED = 3;
	/**
	 *  Index of VHML element
	 */
	private final static int DISGUSTED = 4;
	/**
	 *  Index of VHML element
	 */
	private final static int HAPPY = 5;
	/**
	 *  Index of VHML element
	 */
	private final static int NEUTRAL = 6;
	/**
	 *  Index of VHML element
	 */
	private final static int SURPRISED = 7;
	/**
	 *  Index of VHML element
	 */
	private final static int SAD = 8;
	/**
	 *  Index of VHML element
	 */
	private final static int DEFAULT = 9;
	/**
	 *  Index of VHML element
	 */
	private final static int AGREE = 0;
	/**
	 *  Index of VHML element
	 */
	private final static int DISAGREE = 1;
	/**
	 *  Index of VHML element
	 */
	private final static int CONCENTRATE = 2;
	/**
	 *  Index of VHML element
	 */
	private final static int EMPHASIS = 3;
	/**
	 *  Index of VHML element
	 */
	private final static int SIGH = 4;
	/**
	 *  Index of VHML element
	 */
	private final static int SMILE = 5;
	/**
	 *  Index of VHML element
	 */
	private final static int SHRUG = 6;
	/**
	 *  Index of VHML element
	 */
	private final static int EMPHASIZESYLLABLE = 0;
	/**
	 *  Index of VHML element
	 */
	private final static int EMPHASISESYLLABLE = 1;
	/**
	 *  Index of VHML element
	 */
	private final static int PHONEME = 2;
	/**
	 *  Index of VHML element
	 */
	private final static int PROSODY = 3;
	/**
	 *  Index of VHML element
	 */
	private final static int SAYAS = 4;
	/**
	 *  Index of VHML element
	 */
	private final static int VOICE = 5;
	/**
	 *  Index of VHML element
	 */
	private final static int EMPH = 0;
	/**
	 *  Index of VHML element
	 */
	private final static int PRON = 1;
	/**
	 *  Index of VHML element
	 */
	private final static int PITCH = 2;
	/**
	 *  Index of VHML element
	 */
	private final static int RANGE = 3;
	/**
	 *  Index of VHML element
	 */
	private final static int RATE = 4;
	/**
	 *  Index of VHML element
	 */
	private final static int PROS = 5;
	/**
	 *  Index of VHML element
	 */
	private final static int LOOKLEFT = 0;
	/**
	 *  Index of VHML element
	 */
	private final static int LOOKRIGHT = 1;
	/**
	 *  Index of VHML element
	 */
	private final static int LOOKUP = 2;
	/**
	 *  Index of VHML element
	 */
	private final static int LOOKDOWN = 3;
	/**
	 *  Index of VHML element
	 */
	private final static int EYESLEFT = 4;
	/**
	 *  Index of VHML element
	 */
	private final static int EYESRIGHT = 5;
	/**
	 *  Index of VHML element
	 */
	private final static int EYESUP = 6;
	/**
	 *  Index of VHML element
	 */
	private final static int EYESDOWN = 7;
	/**
	 *  Index of VHML element
	 */
	private final static int HEADLEFT = 8;
	/**
	 *  Index of VHML element
	 */
	private final static int HEADRIGHT = 9;
	/**
	 *  Index of VHML element
	 */
	private final static int HEADUP = 10;
	/**
	 *  Index of VHML element
	 */
	private final static int HEADDOWN = 11;
	/**
	 *  Index of VHML element
	 */
	private final static int HEADROLLLEFT = 12;
	/**
	 *  Index of VHML element
	 */
	private final static int HEADROLLRIGHT = 13;
	/**
	 *  Index of VHML element
	 */
	private final static int EYEBROWUP = 14;
	/**
	 *  Index of VHML element
	 */
	private final static int EYEBROWDOWN = 15;
	/**
	 *  Index of VHML element
	 */
	private final static int EYEBLINK = 16;
	/**
	 *  Index of VHML element
	 */
	private final static int WINK = 17;
	/**
	 *  Index of VHML element
	 */
	private final static int JAWOPEN = 18;
	/**
	 *  Index of VHML element
	 */
	private final static int JAWCLOSE = 19;
	/**
	 *  Which facial feature
	 */	
	private final static int LEFT = 0;
	/**
	 *  Which facial feature
	 */		
	private final static int RIGHT = 1;
	/**
	 *  Which facial feature
	 */		
	private final static int BOTH = 2;
	/**
	 *  The number of expression transition frames to use
	 */
	private int expressiontransitionframes;
	/**
	 *  An expression bit mask
	 */
	private static String EXPMASK = "1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ";
	/**
	 *  The head yaw template for initialisinng MPEG-4 FAPs
	 */
	private int head_yaw;
	/**
	 *  The eye yaw template for initialisinng MPEG-4 FAPs
	 */
	private int eyes_yaw;
	/**
	 *  The head pitch template for initialisinng MPEG-4 FAPs
	 */
	private int head_pitch;
	/**
	 *  The eye pitch template for initialisinng MPEG-4 FAPs
	 */
	private int eyes_pitch;
	/**
	 *  The head roll template for initialisinng MPEG-4 FAPs
	 */
	private int head_roll;
	/**
	 *  The eyebrow displacement template (left) for initialisinng MPEG-4 FAPs
	 */
	private int eyebrow_vertical_left;
	/**
	 *  The eyebrow displacement template (right) for initialisinng MPEG-4 FAPs
	 */
	private int eyebrow_vertical_right;
	/**
	 *  The jaw displacement template for initialisinng MPEG-4 FAPs
	 */
	private int jaw_vertical;
	/**
	 *  The look up table for expressions
	 */
	private int[] expressionlookup;

	/**
	 *  Description of the Field
	 */
	protected int eye_blink_interval;
	/**
	 *  Description of the Field
	 */
	protected int eye_blink_interval_margin;
	/**
	 *  Description of the Field
	 */
	protected int eye_blink_duration_down;
	/**
	 *  Description of the Field
	 */
	protected int eye_blink_duration_hold;
	/**
	 *  Description of the Field
	 */
	protected int eye_blink_duration_up;
	/**
	 *  Description of the Field
	 */
	protected int eye_blink_duration_margin;

	private Random random_numbers;


	/**
	 *  Creates new Semantics
	 *
	 *@param  framespsec  The frame rate to use in animation
	 */
	public VHMLSemantics(int framespsec) {
		fps = framespsec;

		expressiontransitionframes = fps + (int) (fps * 0.5f);

		expressionlookup = new int[10];
		expressionlookup[0] = 4;
		expressionlookup[1] = 3;
		expressionlookup[2] = 0;
		expressionlookup[3] = 0;
		expressionlookup[4] = 5;
		expressionlookup[5] = 1;
		expressionlookup[6] = 0;
		expressionlookup[7] = 6;
		expressionlookup[8] = 2;
		expressionlookup[9] = 1;

		head_yaw = 0;
		eyes_yaw = 0;
		head_pitch = 0;
		eyes_pitch = 0;
		head_roll = 0;
		eyebrow_vertical_left = 0;
		eyebrow_vertical_right = 0;
		jaw_vertical = 0;

		template = new FAPData();

		setEyeBlinkParameters(fps * 3, fps, Math.round((float) fps / 6f), 1, Math.round((float) fps / 4f), 1);
		random_numbers = new Random();
	}


	/**
	 *  Sets the eyeBlinkParameters attribute of the VHMLSemantics object
	 *
	 *@param  interval         Interval of eye blinks
	 *@param  interval_margin  Margin of change for interval
	 *@param  duration_down    Duration of blink down action
	 *@param  duration_hold    Duration of holding blink
	 *@param  duration_up      Duration of blink up action
	 *@param  duration_margin  Margin of change for blinking duration
	 */
	public void setEyeBlinkParameters(int interval, int interval_margin, int duration_down, int duration_hold, int duration_up, int duration_margin) {
		eye_blink_interval = interval;
		eye_blink_interval_margin = interval_margin;
		eye_blink_duration_down = duration_down;
		eye_blink_duration_hold = duration_hold;
		eye_blink_duration_up = duration_up;
		eye_blink_duration_margin = duration_margin;
	}


	/**
	 *  Reset internal state variables
	 */
	public void reset() {
		head_yaw = 0;
		eyes_yaw = 0;
		head_pitch = 0;
		eyes_pitch = 0;
		head_roll = 0;
		eyebrow_vertical_left = 0;
		eyebrow_vertical_right = 0;
		jaw_vertical = 0;

		template = new FAPData();
	}


	/**
	 *  Gets whether the bit mask is set for a FAP index
	 *
	 *@param  faps    The MPEG-4 FAPs to test
	 *@param  fapnum  The FAP from the list to test
	 *@param  bitord  The bit of the FAP to test
	 *@return         Whether the bit is set
	 */
	private boolean isBitSet(Vector faps, int fapnum, int bitord) {
		String fapmask;
		char[] fapmarray;

		fapmask = (String) faps.elementAt(fapnum);
		fapmarray = fapmask.toCharArray();
		if (fapmarray[(2 * bitord) - 2] == '1') {
			return true;
		}
		return false;
	}


	/**
	 *  Gets a low level value for an MPEG-4 FAP
	 *
	 *@param  faps    The MPEG-4 FAPs to use
	 *@param  fapnum  The FAP from the list to use
	 *@param  bitord  The bit of the FAP to use
	 *@return         The low level value value corresonding to the bit
	 */
	private int getLowLevelValue(Vector faps, int fapnum, int bitord) {
		int skip = 0;
		int x;
		int i;
		String fapvalue;
		String temp;

		if (!isBitSet(faps, fapnum, bitord)) {
			return (0);
		}
		if (isBitSet(faps, fapnum, 1)) {
			skip = skip + 4;
		}
		if (isBitSet(faps, fapnum, 2)) {
			skip = skip + 6;
		}
		skip = skip + (bitord - 2);
		fapvalue = (String) faps.elementAt(fapnum + 1);
		x = 0;
		i = 0;
		while ((i < skip) && (x < fapvalue.length())) {
			if (fapvalue.charAt(x) == ' ') {
				i++;
			}
			x++;
		}
		i = x;
		while ((fapvalue.charAt(i) != ' ') || (fapvalue.charAt(i) != ' ')) {
			i++;
		}
		temp = fapvalue.substring(x, i);
		return (new Integer(temp).intValue());
	}


	/**
	 *  Seats a low level value for an MPEG-4 FAP
	 *
	 *@param  faps    The MPEG-4 FAPs to set
	 *@param  fapnum  The FAP from the list to set
	 *@param  bitord  The bit of the FAP to set
	 *@param  vals    The new lowLevelFAPs value
	 *@return         The modified FAPs
	 */
	private Vector setLowLevelFAPs(Vector faps, int fapnum, int bitord, String vals) {
		String fapmask;
		String fapvalue;
		char[] fapmarray;
		int skip = 0;
		int i;
		int x;
		boolean remove = true;
		String temp;

		fapmask = (String) faps.elementAt(fapnum);
		fapvalue = (String) faps.elementAt(fapnum + 1);
		faps.removeElementAt(fapnum);
		faps.removeElementAt(fapnum);
		fapmarray = fapmask.toCharArray();

		if (!isBitSet(faps, fapnum, bitord)) {
			remove = false;
		}
		if (isBitSet(faps, fapnum, 1)) {
			skip = skip + 4;
		}
		if (isBitSet(faps, fapnum, 2)) {
			skip = skip + 6;
		}
		skip = skip + (bitord - 2);

		fapmarray[(2 * bitord) - 2] = '1';
		fapmask = new String(fapmarray);

		x = 0;
		i = 0;
		while ((i < skip) && (x < fapvalue.length())) {
			if (fapvalue.charAt(x) == ' ') {
				i++;
			}
			x++;
		}
		i = x;

		if (remove) {
			while ((fapvalue.charAt(i) != ' ') || (fapvalue.charAt(i) != ' ')) {
				i++;
			}
		}

		temp = fapvalue.substring(0, x);
		temp = temp.concat(" " + vals);
		temp = temp.concat(fapvalue.substring(i, fapvalue.length()));

		faps.insertElementAt(fapmask, fapnum);
		faps.insertElementAt(temp, fapnum + 1);
		return (faps);
	}


	/**
	 *  Initialise MPEG-4 FAPs based on the current template
	 *
	 *@param  faps  The FAPs to initialise
	 *@return       The modified FAPs
	 */
	public Vector initFAPs(Vector faps) {
		int size;
		int i;
		FAPData tempfap;

		size = faps.size();
		for (i = 0; i < size; i++) {
			tempfap = (FAPData) faps.elementAt(i);
			faps.remove(i);
			if (head_yaw != 0) {
				tempfap.setLowLevelVariables(49, 1);
				tempfap.setLowLevelValues(49, head_yaw);
			}
			if (eyes_yaw != 0) {
				tempfap.setLowLevelVariables(23, 1);
				tempfap.setLowLevelValues(23, eyes_yaw);
				tempfap.setLowLevelVariables(24, 1);
				tempfap.setLowLevelValues(24, eyes_yaw);
			}
			if (head_pitch != 0) {
				tempfap.setLowLevelVariables(48, 1);
				tempfap.setLowLevelValues(48, head_pitch);
			}
			if (eyes_pitch != 0) {
				tempfap.setLowLevelVariables(25, 1);
				tempfap.setLowLevelValues(25, eyes_pitch);
				tempfap.setLowLevelVariables(26, 1);
				tempfap.setLowLevelValues(26, eyes_pitch);
			}
			if (head_roll != 0) {
				tempfap.setLowLevelVariables(50, 1);
				tempfap.setLowLevelValues(50, head_roll);
			}
			if (eyebrow_vertical_left != 0) {
				tempfap.setLowLevelVariables(31, 1);
				tempfap.setLowLevelValues(31, eyebrow_vertical_left);
				tempfap.setLowLevelVariables(33, 1);
				tempfap.setLowLevelValues(33, eyebrow_vertical_left);
				tempfap.setLowLevelVariables(35, 1);
				tempfap.setLowLevelValues(35, eyebrow_vertical_left);
			}
			if (eyebrow_vertical_right != 0) {
				tempfap.setLowLevelVariables(32, 1);
				tempfap.setLowLevelValues(32, eyebrow_vertical_right);
				tempfap.setLowLevelVariables(34, 1);
				tempfap.setLowLevelValues(34, eyebrow_vertical_right);
				tempfap.setLowLevelVariables(36, 1);
				tempfap.setLowLevelValues(36, eyebrow_vertical_right);
			}
			if (jaw_vertical != 0) {
				tempfap.setLowLevelVariables(10, 1);
				tempfap.setLowLevelValues(10, jaw_vertical);
				tempfap.setLowLevelVariables(11, 1);
				tempfap.setLowLevelValues(11, jaw_vertical);
				tempfap.setLowLevelVariables(57, 1);
				tempfap.setLowLevelValues(57, jaw_vertical);
				tempfap.setLowLevelVariables(58, 1);
				tempfap.setLowLevelValues(58, jaw_vertical);
				tempfap.setLowLevelVariables(5, 1);
				tempfap.setLowLevelValues(5, jaw_vertical);
				tempfap.setLowLevelVariables(52, 1);
				tempfap.setLowLevelValues(52, jaw_vertical);
				tempfap.setLowLevelVariables(8, 1);
				tempfap.setLowLevelValues(8, jaw_vertical);
				tempfap.setLowLevelVariables(9, 1);
				tempfap.setLowLevelValues(9, jaw_vertical);
				tempfap.setLowLevelVariables(55, 1);
				tempfap.setLowLevelValues(55, jaw_vertical);
				tempfap.setLowLevelVariables(56, 1);
				tempfap.setLowLevelValues(56, jaw_vertical);
				tempfap.setLowLevelVariables(4, 1);
				tempfap.setLowLevelValues(4, jaw_vertical);
				tempfap.setLowLevelVariables(51, 1);
				tempfap.setLowLevelValues(51, jaw_vertical);
			}
			faps.insertElementAt(tempfap, i);
		}
		return (faps);
	}


	/**
	 *  Apply eye blinks to MPEG-4 FAPs
	 *
	 *@param  faps       The FAPs to apply blinks
	 *@param  fromframe  The index to start blinks from
	 *@param  toframe    The index to end blinks at
	 *@return            The modified FAPs
	 */
	public Vector applyBlinking(Vector faps, int fromframe, int toframe) {
		int interval;
		int down;
		int hold;
		int up;
		int i;
		int x;
		int neg_pos;
		boolean fit;
		int position;
		FAPData frame;
		int frames = toframe - fromframe;

		fit = true;

		position = fromframe;
		while (fit) {
			neg_pos = random_numbers.nextInt(2);
			if (neg_pos == 0) {
				neg_pos = -1;
			}
			interval = (eye_blink_interval + ((int) ((float) eye_blink_interval_margin * random_numbers.nextFloat()) * neg_pos));
			neg_pos = random_numbers.nextInt(2);
			if (neg_pos == 0) {
				neg_pos = -1;
			}
			down = (eye_blink_duration_down + ((int) ((float) eye_blink_duration_margin * random_numbers.nextFloat()) * neg_pos));
			neg_pos = random_numbers.nextInt(2);
			if (neg_pos == 0) {
				neg_pos = -1;
			}
			hold = (eye_blink_duration_hold + ((int) ((float) eye_blink_duration_margin * random_numbers.nextFloat()) * neg_pos));
			neg_pos = random_numbers.nextInt(2);
			if (neg_pos == 0) {
				neg_pos = -1;
			}
			up = (eye_blink_duration_up + ((int) ((float) eye_blink_duration_margin * random_numbers.nextFloat()) * neg_pos));
			if (up == 0) {
				up = 1;
			}
			if (interval + down + hold + up < faps.size() - position) {
				position = position + interval;
				x = 1;
				for (i = position; i < down + position; i++) {
					frame = (FAPData) faps.elementAt(i);
					frame.setLowLevelVariables(19, 1);
					frame.setLowLevelVariables(20, 1);
					frame.setLowLevelVariables(21, 1);
					frame.setLowLevelVariables(22, 1);
					frame.setLowLevelValues(19, x * (int) (1024f / (float) down));
					frame.setLowLevelValues(20, x * (int) (1024f / (float) down));
					frame.setLowLevelValues(21, x * (int) (124f / (float) down));
					frame.setLowLevelValues(22, x * (int) (124f / (float) down));
					faps.remove(i);
					faps.insertElementAt(frame, i);
					x++;
				}
				position = position + down;
				x = 1;
				for (i = position; i < position + hold; i++) {
					frame = (FAPData) faps.elementAt(i);
					frame.setLowLevelVariables(19, 1);
					frame.setLowLevelVariables(20, 1);
					frame.setLowLevelVariables(21, 1);
					frame.setLowLevelVariables(22, 1);
					frame.setLowLevelValues(19, 1024);
					frame.setLowLevelValues(20, 1024);
					frame.setLowLevelValues(21, 124);
					frame.setLowLevelValues(22, 124);
					faps.remove(i);
					faps.insertElementAt(frame, i);
					x++;
				}
				position = position + hold;
				x = 1;
				for (i = position; i < position + up; i++) {
					frame = (FAPData) faps.elementAt(i);
					frame.setLowLevelVariables(19, 1);
					frame.setLowLevelVariables(20, 1);
					frame.setLowLevelVariables(21, 1);
					frame.setLowLevelVariables(22, 1);
					frame.setLowLevelValues(19, 1024 - (x * (int) (1024f / (float) up)));
					frame.setLowLevelValues(20, 1024 - (x * (int) (1024f / (float) up)));
					frame.setLowLevelValues(21, 124 - (x * (int) (124f / (float) up)));
					frame.setLowLevelValues(22, 124 - (x * (int) (124f / (float) up)));
					faps.remove(i);
					faps.insertElementAt(frame, i);
					x++;
				}
				position = position + up;
			} else {
				fit = false;
			}
		}

		return (faps);
	}


	/**
	 *  Apply facial animation to MPEG-4 FAPs
	 *
	 *@param  faps       The FAPs to apply facial animation
	 *@param  fromframe  The index to start facial animation from
	 *@param  toframe    The index to end facial animation at
	 *@param  id         The ID of the element
	 *@param  intensity  The intensity of the element
	 *@param  repeat     How many times to repeat the facial animation
	 *@param  which      Which facial feature to apply the animation to
	 *@return            The modified FAPs
	 */
	public Vector applyFacialAnimation(Vector faps, int fromframe, int toframe, int id, int intensity, int repeat, String which) {
		int i;
		int x;
		int j;
		int currentpos;
		float fraction;
		int from;
		int to;
		FAPData tempfap;
		int tempfromframe;
		int temptoframe;
		int size;
		int temp_head_yaw;
		int temp_head_pitch;
		int temp_eyes_yaw;
		int temp_eyes_pitch;
		int temp_head_roll;
		int temp_eyebrow_vertical_left;
		int temp_eyebrow_vertical_right;
		int temp_jaw_vertical;
		float intensity2;
		int which_side = BOTH;

		if (which == null) {
			which_side = BOTH;
		} else if (which.equalsIgnoreCase("left")) {
			which_side = LEFT;
		} else if (which.equalsIgnoreCase("right")) {
			which_side = RIGHT;
		} else if (which.equalsIgnoreCase("both")) {
			which_side = BOTH;
		} else {
			which_side = BOTH;
		}

		if (fromframe < 0) {
			fromframe = 0;
		}
		if (toframe < 0) {
			toframe = 0;
		}
		if (fromframe >= faps.size()) {
			return (faps);
		} else if (toframe >= faps.size()) {
			size = faps.size();
			for (i = 0; i <= toframe - size; i++) {
				tempfap = new FAPData();
				if (head_yaw != 0) {
					tempfap.setLowLevelVariables(49, 1);
					tempfap.setLowLevelValues(49, head_yaw);
				}
				if (eyes_yaw != 0) {
					tempfap.setLowLevelVariables(23, 1);
					tempfap.setLowLevelValues(23, eyes_yaw);
					tempfap.setLowLevelVariables(24, 1);
					tempfap.setLowLevelValues(24, eyes_yaw);
				}
				if (head_pitch != 0) {
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, head_pitch);
				}
				if (eyes_pitch != 0) {
					tempfap.setLowLevelVariables(25, 1);
					tempfap.setLowLevelValues(25, eyes_pitch);
					tempfap.setLowLevelVariables(26, 1);
					tempfap.setLowLevelValues(26, eyes_pitch);
				}
				if (head_roll != 0) {
					tempfap.setLowLevelVariables(50, 1);
					tempfap.setLowLevelValues(50, head_roll);
				}
				if (eyebrow_vertical_left != 0) {
					tempfap.setLowLevelVariables(31, 1);
					tempfap.setLowLevelValues(31, eyebrow_vertical_left);
					tempfap.setLowLevelVariables(33, 1);
					tempfap.setLowLevelValues(33, eyebrow_vertical_left);
					tempfap.setLowLevelVariables(35, 1);
					tempfap.setLowLevelValues(35, eyebrow_vertical_left);
				}
				if (eyebrow_vertical_right != 0) {
					tempfap.setLowLevelVariables(32, 1);
					tempfap.setLowLevelValues(32, eyebrow_vertical_right);
					tempfap.setLowLevelVariables(34, 1);
					tempfap.setLowLevelValues(34, eyebrow_vertical_right);
					tempfap.setLowLevelVariables(36, 1);
					tempfap.setLowLevelValues(36, eyebrow_vertical_right);
				}
				if (jaw_vertical != 0) {
					tempfap.setLowLevelVariables(10, 1);
					tempfap.setLowLevelValues(10, jaw_vertical);
					tempfap.setLowLevelVariables(11, 1);
					tempfap.setLowLevelValues(11, jaw_vertical);
					tempfap.setLowLevelVariables(57, 1);
					tempfap.setLowLevelValues(57, jaw_vertical);
					tempfap.setLowLevelVariables(58, 1);
					tempfap.setLowLevelValues(58, jaw_vertical);
					tempfap.setLowLevelVariables(5, 1);
					tempfap.setLowLevelValues(5, jaw_vertical);
					tempfap.setLowLevelVariables(52, 1);
					tempfap.setLowLevelValues(52, jaw_vertical);
					tempfap.setLowLevelVariables(8, 1);
					tempfap.setLowLevelValues(8, jaw_vertical);
					tempfap.setLowLevelVariables(9, 1);
					tempfap.setLowLevelValues(9, jaw_vertical);
					tempfap.setLowLevelVariables(55, 1);
					tempfap.setLowLevelValues(55, jaw_vertical);
					tempfap.setLowLevelVariables(56, 1);
					tempfap.setLowLevelValues(56, jaw_vertical);
					tempfap.setLowLevelVariables(4, 1);
					tempfap.setLowLevelValues(4, jaw_vertical);
					tempfap.setLowLevelVariables(51, 1);
					tempfap.setLowLevelValues(51, jaw_vertical);
				}
				faps.addElement(tempfap);
			}
		}

		intensity2 = (float) intensity / 100f;
		tempfromframe = fromframe;
		temptoframe = toframe;
		toframe = tempfromframe;
		for (j = 0; j < repeat; j++) {
			fromframe = toframe;
			toframe = ((temptoframe - tempfromframe) / repeat) + fromframe;

			if (id == LOOKLEFT) {
				x = 0;
				from = fromframe;
				to = toframe;
				temp_head_yaw = head_yaw;
				head_yaw = 0;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(49);
					tempfap.setLowLevelVariables(49, 1);
					tempfap.setLowLevelValues(49, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 2f)) * 100000f)) * intensity2)));
					if (fraction < 0.5f) {
						currentpos = tempfap.getLowLevelValue(23);
						tempfap.setLowLevelVariables(23, 1);
						tempfap.setLowLevelValues(23, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 2f)) * 100000f)) * intensity2)));
						currentpos = tempfap.getLowLevelValue(24);
						tempfap.setLowLevelVariables(24, 1);
						tempfap.setLowLevelValues(24, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 2f)) * 100000f)) * intensity2)));
					} else {
						currentpos = tempfap.getLowLevelValue(23);
						tempfap.setLowLevelVariables(23, 1);
						tempfap.setLowLevelValues(23, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / 2f)) * 100000f)) * intensity2)));
						currentpos = tempfap.getLowLevelValue(24);
						tempfap.setLowLevelVariables(24, 1);
						tempfap.setLowLevelValues(24, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / 2f)) * 100000f)) * intensity2)));
					}
					faps.insertElementAt(tempfap, i);
					x++;
				}
				head_yaw = (int) ((((((float) Math.PI / 2f))) * 100000f) * intensity2);
				head_yaw = temp_head_yaw + head_yaw;
				for (i = to; i < faps.size(); i++) {
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(49);
					tempfap.setLowLevelVariables(49, 1);
					tempfap.setLowLevelValues(49, currentpos + (head_yaw - temp_head_yaw));
					faps.insertElementAt(new FAPData(tempfap), i);
				}
			} else if (id == LOOKRIGHT) {
				x = 0;
				from = fromframe;
				to = toframe;
				temp_head_yaw = head_yaw;
				head_yaw = 0;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(49);
					tempfap.setLowLevelVariables(49, 1);
					tempfap.setLowLevelValues(49, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -2f)) * 100000f)) * intensity2)));
					if (fraction < 0.5f) {
						currentpos = tempfap.getLowLevelValue(23);
						tempfap.setLowLevelVariables(23, 1);
						tempfap.setLowLevelValues(23, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -2f)) * 100000f)) * intensity2)));
						currentpos = tempfap.getLowLevelValue(24);
						tempfap.setLowLevelVariables(24, 1);
						tempfap.setLowLevelValues(24, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -2f)) * 100000f)) * intensity2)));
					} else {
						currentpos = tempfap.getLowLevelValue(23);
						tempfap.setLowLevelVariables(23, 1);
						tempfap.setLowLevelValues(23, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / -2f)) * 100000f)) * intensity2)));
						currentpos = tempfap.getLowLevelValue(24);
						tempfap.setLowLevelVariables(24, 1);
						tempfap.setLowLevelValues(24, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / -2f)) * 100000f)) * intensity2)));
					}
					faps.insertElementAt(tempfap, i);
					x++;
				}
				head_yaw = (int) ((((((float) Math.PI / -2f))) * 100000f) * intensity2);
				head_yaw = temp_head_yaw + head_yaw;
				for (i = to; i < faps.size(); i++) {
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(49);
					tempfap.setLowLevelVariables(49, 1);
					tempfap.setLowLevelValues(49, currentpos + (head_yaw - temp_head_yaw));
					faps.insertElementAt(new FAPData(tempfap), i);
				}
			} else if (id == LOOKUP) {
				x = 0;
				from = fromframe;
				to = toframe;
				temp_head_pitch = head_pitch;
				head_pitch = 0;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(48);
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -2f)) * 100000f)) * intensity2)));
					if (fraction < 0.5f) {
						currentpos = tempfap.getLowLevelValue(25);
						tempfap.setLowLevelVariables(25, 1);
						tempfap.setLowLevelValues(25, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -2f)) * 100000f)) * intensity2)));
						currentpos = tempfap.getLowLevelValue(26);
						tempfap.setLowLevelVariables(26, 1);
						tempfap.setLowLevelValues(26, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -2f)) * 100000f)) * intensity2)));
					} else {
						currentpos = tempfap.getLowLevelValue(25);
						tempfap.setLowLevelVariables(25, 1);
						tempfap.setLowLevelValues(25, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / -2f)) * 100000f)) * intensity2)));
						currentpos = tempfap.getLowLevelValue(26);
						tempfap.setLowLevelVariables(26, 1);
						tempfap.setLowLevelValues(26, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / -2f)) * 100000f)) * intensity2)));
					}
					faps.insertElementAt(tempfap, i);
					x++;
				}
				head_pitch = (int) ((((((float) Math.PI / -2f))) * 100000f) * intensity2);
				head_pitch = temp_head_pitch + head_pitch;
				for (i = to; i < faps.size(); i++) {
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(48);
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, currentpos + (head_pitch - temp_head_pitch));
					faps.insertElementAt(new FAPData(tempfap), i);
				}
			} else if (id == LOOKDOWN) {
				x = 0;
				from = fromframe;
				to = toframe;
				temp_head_pitch = head_pitch;
				head_pitch = 0;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(48);
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 2f)) * 100000f)) * intensity2)));
					if (fraction < 0.5f) {
						currentpos = tempfap.getLowLevelValue(25);
						tempfap.setLowLevelVariables(25, 1);
						tempfap.setLowLevelValues(25, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 2f)) * 100000f)) * intensity2)));
						currentpos = tempfap.getLowLevelValue(26);
						tempfap.setLowLevelVariables(26, 1);
						tempfap.setLowLevelValues(26, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 2f)) * 100000f)) * intensity2)));
					} else {
						currentpos = tempfap.getLowLevelValue(25);
						tempfap.setLowLevelVariables(25, 1);
						tempfap.setLowLevelValues(25, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / 2f)) * 100000f)) * intensity2)));
						currentpos = tempfap.getLowLevelValue(26);
						tempfap.setLowLevelVariables(26, 1);
						tempfap.setLowLevelValues(26, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / 2f)) * 100000f)) * intensity2)));
					}
					faps.insertElementAt(tempfap, i);
					x++;
				}
				head_pitch = (int) ((((((float) Math.PI / 2f))) * 100000f) * intensity2);
				head_pitch = temp_head_pitch + head_pitch;
				for (i = to; i < faps.size(); i++) {
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(48);
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, currentpos + (head_pitch - temp_head_pitch));
					faps.insertElementAt(new FAPData(tempfap), i);
				}
			} else if (id == EYESLEFT) {
				x = 0;
				from = fromframe;
				to = toframe;
				temp_eyes_yaw = eyes_yaw;
				eyes_yaw = 0;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(23);
					tempfap.setLowLevelVariables(23, 1);
					tempfap.setLowLevelValues(23, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 2f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(24);
					tempfap.setLowLevelVariables(24, 1);
					tempfap.setLowLevelValues(24, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 2f)) * 100000f)) * intensity2)));
					faps.insertElementAt(tempfap, i);
					x++;
				}
				eyes_yaw = (int) ((((((float) Math.PI / 2f))) * 100000f) * intensity2);
				eyes_yaw = temp_eyes_yaw + eyes_yaw;
				for (i = to; i < faps.size(); i++) {
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(23);
					tempfap.setLowLevelVariables(23, 1);
					tempfap.setLowLevelValues(23, currentpos + (eyes_yaw - temp_eyes_yaw));
					currentpos = tempfap.getLowLevelValue(24);
					tempfap.setLowLevelVariables(24, 1);
					tempfap.setLowLevelValues(24, currentpos + (eyes_yaw - temp_eyes_yaw));
					faps.insertElementAt(new FAPData(tempfap), i);
				}
			} else if (id == EYESRIGHT) {
				x = 0;
				from = fromframe;
				to = toframe;
				temp_eyes_yaw = eyes_yaw;
				eyes_yaw = 0;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(23);
					tempfap.setLowLevelVariables(23, 1);
					tempfap.setLowLevelValues(23, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -2f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(24);
					tempfap.setLowLevelVariables(24, 1);
					tempfap.setLowLevelValues(24, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -2f)) * 100000f)) * intensity2)));
					faps.insertElementAt(tempfap, i);
					x++;
				}
				eyes_yaw = (int) ((((((float) Math.PI / -2f))) * 100000f) * intensity2);
				eyes_yaw = temp_eyes_yaw + eyes_yaw;
				for (i = to; i < faps.size(); i++) {
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(23);
					tempfap.setLowLevelVariables(23, 1);
					tempfap.setLowLevelValues(23, currentpos + (eyes_yaw - temp_eyes_yaw));
					currentpos = tempfap.getLowLevelValue(24);
					tempfap.setLowLevelVariables(24, 1);
					tempfap.setLowLevelValues(24, currentpos + (eyes_yaw - temp_eyes_yaw));
					faps.insertElementAt(new FAPData(tempfap), i);
				}
			} else if (id == EYESUP) {
				x = 0;
				from = fromframe;
				to = toframe;
				temp_eyes_pitch = eyes_pitch;
				eyes_pitch = 0;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(25);
					tempfap.setLowLevelVariables(25, 1);
					tempfap.setLowLevelValues(25, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -2f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(26);
					tempfap.setLowLevelVariables(26, 1);
					tempfap.setLowLevelValues(26, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -2f)) * 100000f)) * intensity2)));
					faps.insertElementAt(tempfap, i);
					x++;
				}
				eyes_pitch = (int) ((((((float) Math.PI / -2f))) * 100000f) * intensity2);
				eyes_pitch = temp_eyes_pitch + eyes_pitch;
				for (i = to; i < faps.size(); i++) {
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(25);
					tempfap.setLowLevelVariables(25, 1);
					tempfap.setLowLevelValues(25, currentpos + (eyes_pitch - temp_eyes_pitch));
					currentpos = tempfap.getLowLevelValue(26);
					tempfap.setLowLevelVariables(26, 1);
					tempfap.setLowLevelValues(26, currentpos + (eyes_pitch - temp_eyes_pitch));
					faps.insertElementAt(new FAPData(tempfap), i);
				}
			} else if (id == EYESDOWN) {
				x = 0;
				from = fromframe;
				to = toframe;
				temp_eyes_pitch = eyes_pitch;
				eyes_pitch = 0;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(25);
					tempfap.setLowLevelVariables(25, 1);
					tempfap.setLowLevelValues(25, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 2f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(26);
					tempfap.setLowLevelVariables(26, 1);
					tempfap.setLowLevelValues(26, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 2f)) * 100000f)) * intensity2)));
					faps.insertElementAt(tempfap, i);
					x++;
				}
				eyes_pitch = (int) ((((((float) Math.PI / 2f))) * 100000f) * intensity2);
				eyes_pitch = temp_eyes_pitch + eyes_pitch;
				for (i = to; i < faps.size(); i++) {
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(25);
					tempfap.setLowLevelVariables(25, 1);
					tempfap.setLowLevelValues(25, currentpos + (eyes_pitch - temp_eyes_pitch));
					currentpos = tempfap.getLowLevelValue(26);
					tempfap.setLowLevelVariables(26, 1);
					tempfap.setLowLevelValues(26, currentpos + (eyes_pitch - temp_eyes_pitch));
					faps.insertElementAt(new FAPData(tempfap), i);
				}
			} else if (id == HEADLEFT) {
				x = 0;
				from = fromframe;
				to = toframe;
				temp_head_yaw = head_yaw;
				head_yaw = 0;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(49);
					tempfap.setLowLevelVariables(49, 1);
					tempfap.setLowLevelValues(49, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 2f)) * 100000f)) * intensity2)));
					faps.insertElementAt(tempfap, i);
					x++;
				}
				head_yaw = (int) ((((((float) Math.PI / 2f))) * 100000f) * intensity2);
				head_yaw = temp_head_yaw + head_yaw;
				for (i = to; i < faps.size(); i++) {
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(49);
					tempfap.setLowLevelVariables(49, 1);
					tempfap.setLowLevelValues(49, currentpos + (head_yaw - temp_head_yaw));
					faps.insertElementAt(new FAPData(tempfap), i);
				}
			} else if (id == HEADRIGHT) {
				x = 0;
				from = fromframe;
				to = toframe;
				temp_head_yaw = head_yaw;
				head_yaw = 0;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(49);
					tempfap.setLowLevelVariables(49, 1);
					tempfap.setLowLevelValues(49, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -2f)) * 100000f)) * intensity2)));
					faps.insertElementAt(tempfap, i);
					x++;
				}
				head_yaw = (int) ((((((float) Math.PI / -2f))) * 100000f) * intensity2);
				head_yaw = temp_head_yaw + head_yaw;
				for (i = to; i < faps.size(); i++) {
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(49);
					tempfap.setLowLevelVariables(49, 1);
					tempfap.setLowLevelValues(49, currentpos + (head_yaw - temp_head_yaw));
					faps.insertElementAt(new FAPData(tempfap), i);
				}
			} else if (id == HEADUP) {
				x = 0;
				from = fromframe;
				to = toframe;
				temp_head_pitch = head_pitch;
				head_pitch = 0;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(48);
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -2f)) * 100000f)) * intensity2)));
					faps.insertElementAt(tempfap, i);
					x++;
				}
				head_pitch = (int) ((((((float) Math.PI / -2f))) * 100000f) * intensity2);
				head_pitch = temp_head_pitch + head_pitch;
				for (i = to; i < faps.size(); i++) {
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(48);
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, currentpos + (head_pitch - temp_head_pitch));
					faps.insertElementAt(new FAPData(tempfap), i);
				}
			} else if (id == HEADDOWN) {
				x = 0;
				from = fromframe;
				to = toframe;
				temp_head_pitch = head_pitch;
				head_pitch = 0;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(48);
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 2f)) * 100000f)) * intensity2)));
					faps.insertElementAt(tempfap, i);
					x++;
				}
				head_pitch = (int) ((((((float) Math.PI / 2f))) * 100000f) * intensity2);
				head_pitch = temp_head_pitch + head_pitch;
				for (i = to; i < faps.size(); i++) {
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(48);
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, currentpos + (head_pitch - temp_head_pitch));
					faps.insertElementAt(new FAPData(tempfap), i);
				}
			} else if (id == HEADROLLLEFT) {
				x = 0;
				from = fromframe;
				to = toframe;
				temp_head_roll = head_roll;
				head_roll = 0;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(50);
					tempfap.setLowLevelVariables(50, 1);
					tempfap.setLowLevelValues(50, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -5f)) * 100000f)) * intensity2)));
					faps.insertElementAt(tempfap, i);
					x++;
				}
				head_roll = (int) ((((((float) Math.PI / -5f))) * 100000f) * intensity2);
				head_roll = temp_head_roll + head_roll;
				for (i = to; i < faps.size(); i++) {
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(50);
					tempfap.setLowLevelVariables(50, 1);
					tempfap.setLowLevelValues(50, currentpos + (head_roll - temp_head_roll));
					faps.insertElementAt(new FAPData(tempfap), i);
				}
			} else if (id == HEADROLLRIGHT) {
				x = 0;
				from = fromframe;
				to = toframe;
				temp_head_roll = head_roll;
				head_roll = 0;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(50);
					tempfap.setLowLevelVariables(50, 1);
					tempfap.setLowLevelValues(50, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 5f)) * 100000f)) * intensity2)));
					faps.insertElementAt(tempfap, i);
					x++;
				}
				head_roll = (int) ((((((float) Math.PI / 5f))) * 100000f) * intensity2);
				head_roll = temp_head_roll + head_roll;
				for (i = to; i < faps.size(); i++) {
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(50);
					tempfap.setLowLevelVariables(50, 1);
					tempfap.setLowLevelValues(50, currentpos + (head_roll - temp_head_roll));
					faps.insertElementAt(new FAPData(tempfap), i);
				}
			} else if (id == EYEBROWUP) {
				x = 0;
				from = fromframe;
				to = toframe;
				temp_eyebrow_vertical_left = eyebrow_vertical_left;
				temp_eyebrow_vertical_right = eyebrow_vertical_right;
				eyebrow_vertical_left = 0;
				eyebrow_vertical_right = 0;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					if ((which_side == LEFT) || (which_side == BOTH)) {
						currentpos = tempfap.getLowLevelValue(31);
						tempfap.setLowLevelVariables(31, 1);
						tempfap.setLowLevelValues(31, (int) ((float) currentpos + ((fraction * 200f) * intensity2)));
						currentpos = tempfap.getLowLevelValue(33);
						tempfap.setLowLevelVariables(33, 1);
						tempfap.setLowLevelValues(33, (int) ((float) currentpos + ((fraction * 200f) * intensity2)));
						currentpos = tempfap.getLowLevelValue(35);
						tempfap.setLowLevelVariables(35, 1);
						tempfap.setLowLevelValues(35, (int) ((float) currentpos + ((fraction * 200f) * intensity2)));
					}
					if ((which_side == RIGHT) || (which_side == BOTH)) {
						currentpos = tempfap.getLowLevelValue(32);
						tempfap.setLowLevelVariables(32, 1);
						tempfap.setLowLevelValues(32, (int) ((float) currentpos + ((fraction * 200f) * intensity2)));
						currentpos = tempfap.getLowLevelValue(34);
						tempfap.setLowLevelVariables(34, 1);
						tempfap.setLowLevelValues(34, (int) ((float) currentpos + ((fraction * 200f) * intensity2)));
						currentpos = tempfap.getLowLevelValue(36);
						tempfap.setLowLevelVariables(36, 1);
						tempfap.setLowLevelValues(36, (int) ((float) currentpos + ((fraction * 200f) * intensity2)));
					}
					faps.insertElementAt(tempfap, i);
					x++;
				}
				if ((which_side == LEFT) || (which_side == BOTH)) {
					eyebrow_vertical_left = (int) ((200f) * intensity2);
				}
				if ((which_side == RIGHT) || (which_side == BOTH)) {
					eyebrow_vertical_right = (int) ((200f) * intensity2);
				}
				eyebrow_vertical_left = temp_eyebrow_vertical_left + eyebrow_vertical_left;
				eyebrow_vertical_right = temp_eyebrow_vertical_right + eyebrow_vertical_right;
				for (i = to; i < faps.size(); i++) {
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(31);
					tempfap.setLowLevelVariables(31, 1);
					tempfap.setLowLevelValues(31, currentpos + (eyebrow_vertical_left - temp_eyebrow_vertical_left));
					currentpos = tempfap.getLowLevelValue(33);
					tempfap.setLowLevelVariables(33, 1);
					tempfap.setLowLevelValues(33, currentpos + (eyebrow_vertical_left - temp_eyebrow_vertical_left));
					currentpos = tempfap.getLowLevelValue(35);
					tempfap.setLowLevelVariables(35, 1);
					tempfap.setLowLevelValues(35, currentpos + (eyebrow_vertical_left - temp_eyebrow_vertical_left));
					currentpos = tempfap.getLowLevelValue(32);
					tempfap.setLowLevelVariables(32, 1);
					tempfap.setLowLevelValues(32, currentpos + (eyebrow_vertical_right - temp_eyebrow_vertical_right));
					currentpos = tempfap.getLowLevelValue(34);
					tempfap.setLowLevelVariables(34, 1);
					tempfap.setLowLevelValues(34, currentpos + (eyebrow_vertical_right - temp_eyebrow_vertical_right));
					currentpos = tempfap.getLowLevelValue(36);
					tempfap.setLowLevelVariables(36, 1);
					tempfap.setLowLevelValues(36, currentpos + (eyebrow_vertical_right - temp_eyebrow_vertical_right));
					faps.insertElementAt(new FAPData(tempfap), i);
				}
			} else if (id == EYEBROWDOWN) {
				x = 0;
				from = fromframe;
				to = toframe;
				temp_eyebrow_vertical_left = eyebrow_vertical_left;
				temp_eyebrow_vertical_right = eyebrow_vertical_right;
				eyebrow_vertical_left = 0;
				eyebrow_vertical_right = 0;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					if ((which_side == LEFT) || (which_side == BOTH)) {
						currentpos = tempfap.getLowLevelValue(31);
						tempfap.setLowLevelVariables(31, 1);
						tempfap.setLowLevelValues(31, (int) ((float) currentpos + ((fraction * -200f) * intensity2)));
						currentpos = tempfap.getLowLevelValue(33);
						tempfap.setLowLevelVariables(33, 1);
						tempfap.setLowLevelValues(33, (int) ((float) currentpos + ((fraction * -200f) * intensity2)));
						currentpos = tempfap.getLowLevelValue(35);
						tempfap.setLowLevelVariables(35, 1);
						tempfap.setLowLevelValues(35, (int) ((float) currentpos + ((fraction * -200f) * intensity2)));
					}
					if ((which_side == RIGHT) || (which_side == BOTH)) {
						currentpos = tempfap.getLowLevelValue(32);
						tempfap.setLowLevelVariables(32, 1);
						tempfap.setLowLevelValues(32, (int) ((float) currentpos + ((fraction * -200f) * intensity2)));
						currentpos = tempfap.getLowLevelValue(34);
						tempfap.setLowLevelVariables(34, 1);
						tempfap.setLowLevelValues(34, (int) ((float) currentpos + ((fraction * -200f) * intensity2)));
						currentpos = tempfap.getLowLevelValue(36);
						tempfap.setLowLevelVariables(36, 1);
						tempfap.setLowLevelValues(36, (int) ((float) currentpos + ((fraction * -200f) * intensity2)));
					}
					faps.insertElementAt(tempfap, i);
					x++;
				}
				if ((which_side == LEFT) || (which_side == BOTH)) {
					eyebrow_vertical_left = (int) ((-200f) * intensity2);
				}
				if ((which_side == RIGHT) || (which_side == BOTH)) {
					eyebrow_vertical_right = (int) ((-200f) * intensity2);
				}
				eyebrow_vertical_left = temp_eyebrow_vertical_left + eyebrow_vertical_left;
				eyebrow_vertical_right = temp_eyebrow_vertical_right + eyebrow_vertical_right;
				for (i = to; i < faps.size(); i++) {
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(31);
					tempfap.setLowLevelVariables(31, 1);
					tempfap.setLowLevelValues(31, currentpos + (eyebrow_vertical_left - temp_eyebrow_vertical_left));
					currentpos = tempfap.getLowLevelValue(33);
					tempfap.setLowLevelVariables(33, 1);
					tempfap.setLowLevelValues(33, currentpos + (eyebrow_vertical_left - temp_eyebrow_vertical_left));
					currentpos = tempfap.getLowLevelValue(35);
					tempfap.setLowLevelVariables(35, 1);
					tempfap.setLowLevelValues(35, currentpos + (eyebrow_vertical_left - temp_eyebrow_vertical_left));
					currentpos = tempfap.getLowLevelValue(32);
					tempfap.setLowLevelVariables(32, 1);
					tempfap.setLowLevelValues(32, currentpos + (eyebrow_vertical_right - temp_eyebrow_vertical_right));
					currentpos = tempfap.getLowLevelValue(34);
					tempfap.setLowLevelVariables(34, 1);
					tempfap.setLowLevelValues(34, currentpos + (eyebrow_vertical_right - temp_eyebrow_vertical_right));
					currentpos = tempfap.getLowLevelValue(36);
					tempfap.setLowLevelVariables(36, 1);
					tempfap.setLowLevelValues(36, currentpos + (eyebrow_vertical_right - temp_eyebrow_vertical_right));
					faps.insertElementAt(new FAPData(tempfap), i);
				}
			} else if (id == EYEBLINK) {
				x = 0;
				from = fromframe;
				to = toframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					if (fraction < 0.5f) {
						currentpos = tempfap.getLowLevelValue(19);
						tempfap.setLowLevelVariables(19, 1);
						tempfap.setLowLevelValues(19, (int) ((float) currentpos + ((fraction * 2048f) * intensity2)));
						currentpos = tempfap.getLowLevelValue(20);
						tempfap.setLowLevelVariables(20, 1);
						tempfap.setLowLevelValues(20, (int) ((float) currentpos + ((fraction * 2048f) * intensity2)));
						currentpos = tempfap.getLowLevelValue(21);
						tempfap.setLowLevelVariables(21, 1);
						tempfap.setLowLevelValues(21, (int) ((float) currentpos + ((fraction * 248f) * intensity2)));
						currentpos = tempfap.getLowLevelValue(22);
						tempfap.setLowLevelVariables(22, 1);
						tempfap.setLowLevelValues(22, (int) ((float) currentpos + ((fraction * 248f) * intensity2)));
					} else {
						currentpos = tempfap.getLowLevelValue(19);
						tempfap.setLowLevelVariables(19, 1);
						tempfap.setLowLevelValues(19, (int) ((float) currentpos + (((1f - fraction) * 2048f) * intensity2)));
						currentpos = tempfap.getLowLevelValue(20);
						tempfap.setLowLevelVariables(20, 1);
						tempfap.setLowLevelValues(20, (int) ((float) currentpos + (((1f - fraction) * 2048f) * intensity2)));
						currentpos = tempfap.getLowLevelValue(21);
						tempfap.setLowLevelVariables(21, 1);
						tempfap.setLowLevelValues(21, (int) ((float) currentpos + (((1f - fraction) * 248f) * intensity2)));
						currentpos = tempfap.getLowLevelValue(22);
						tempfap.setLowLevelVariables(22, 1);
						tempfap.setLowLevelValues(22, (int) ((float) currentpos + (((1f - fraction) * 248f) * intensity2)));
					}
					faps.insertElementAt(tempfap, i);
					x++;
				}
			} else if (id == WINK) {
				x = 0;
				from = fromframe;
				to = toframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					if (fraction < 0.5f) {
						if (which_side == RIGHT) {
							currentpos = tempfap.getLowLevelValue(20);
							tempfap.setLowLevelVariables(20, 1);
							tempfap.setLowLevelValues(20, (int) ((float) currentpos + ((fraction * 2048f))));
							currentpos = tempfap.getLowLevelValue(22);
							tempfap.setLowLevelVariables(22, 1);
							tempfap.setLowLevelValues(22, (int) ((float) currentpos + ((fraction * 248f))));
							currentpos = tempfap.getLowLevelValue(50);
							tempfap.setLowLevelVariables(50, 1);
							tempfap.setLowLevelValues(50, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -5f)) * 100000f)) * intensity2)));
							currentpos = tempfap.getLowLevelValue(32);
							tempfap.setLowLevelVariables(32, 1);
							tempfap.setLowLevelValues(32, (int) ((float) currentpos + ((fraction * -200f) * intensity2)));
							currentpos = tempfap.getLowLevelValue(34);
							tempfap.setLowLevelVariables(34, 1);
							tempfap.setLowLevelValues(34, (int) ((float) currentpos + ((fraction * -200f) * intensity2)));
							currentpos = tempfap.getLowLevelValue(36);
							tempfap.setLowLevelVariables(36, 1);
							tempfap.setLowLevelValues(36, (int) ((float) currentpos + ((fraction * -200f) * intensity2)));
						} else {
							currentpos = tempfap.getLowLevelValue(19);
							tempfap.setLowLevelVariables(19, 1);
							tempfap.setLowLevelValues(19, (int) ((float) currentpos + ((fraction * 2048f))));
							currentpos = tempfap.getLowLevelValue(21);
							tempfap.setLowLevelVariables(21, 1);
							tempfap.setLowLevelValues(21, (int) ((float) currentpos + ((fraction * 248f))));
							currentpos = tempfap.getLowLevelValue(50);
							tempfap.setLowLevelVariables(50, 1);
							tempfap.setLowLevelValues(50, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 5f)) * 100000f)) * intensity2)));
							currentpos = tempfap.getLowLevelValue(31);
							tempfap.setLowLevelVariables(31, 1);
							tempfap.setLowLevelValues(31, (int) ((float) currentpos + ((fraction * -200f) * intensity2)));
							currentpos = tempfap.getLowLevelValue(33);
							tempfap.setLowLevelVariables(33, 1);
							tempfap.setLowLevelValues(33, (int) ((float) currentpos + ((fraction * -200f) * intensity2)));
							currentpos = tempfap.getLowLevelValue(35);
							tempfap.setLowLevelVariables(35, 1);
							tempfap.setLowLevelValues(35, (int) ((float) currentpos + ((fraction * -200f) * intensity2)));
						}
					} else {
						if (which_side == RIGHT) {
							currentpos = tempfap.getLowLevelValue(20);
							tempfap.setLowLevelVariables(20, 1);
							tempfap.setLowLevelValues(20, (int) ((float) currentpos + (((1f - fraction) * 1024f) * intensity2)));
							currentpos = tempfap.getLowLevelValue(22);
							tempfap.setLowLevelVariables(22, 1);
							tempfap.setLowLevelValues(22, (int) ((float) currentpos + (((1f - fraction) * 124f) * intensity2)));
							currentpos = tempfap.getLowLevelValue(50);
							tempfap.setLowLevelVariables(50, 1);
							tempfap.setLowLevelValues(50, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / -5f)) * 100000f)) * intensity2)));
							currentpos = tempfap.getLowLevelValue(32);
							tempfap.setLowLevelVariables(32, 1);
							tempfap.setLowLevelValues(32, (int) ((float) currentpos + (((1f - fraction) * -200f) * intensity2)));
							currentpos = tempfap.getLowLevelValue(34);
							tempfap.setLowLevelVariables(34, 1);
							tempfap.setLowLevelValues(34, (int) ((float) currentpos + (((1f - fraction) * -200f) * intensity2)));
							currentpos = tempfap.getLowLevelValue(36);
							tempfap.setLowLevelVariables(36, 1);
							tempfap.setLowLevelValues(36, (int) ((float) currentpos + (((1f - fraction) * -200f) * intensity2)));
						} else {
							currentpos = tempfap.getLowLevelValue(19);
							tempfap.setLowLevelVariables(19, 1);
							tempfap.setLowLevelValues(19, (int) ((float) currentpos + (((1f - fraction) * 1024f) * intensity2)));
							currentpos = tempfap.getLowLevelValue(21);
							tempfap.setLowLevelVariables(21, 1);
							tempfap.setLowLevelValues(21, (int) ((float) currentpos + (((1f - fraction) * 124f) * intensity2)));
							currentpos = tempfap.getLowLevelValue(50);
							tempfap.setLowLevelVariables(50, 1);
							tempfap.setLowLevelValues(50, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / 5f)) * 100000f)) * intensity2)));
							currentpos = tempfap.getLowLevelValue(31);
							tempfap.setLowLevelVariables(31, 1);
							tempfap.setLowLevelValues(31, (int) ((float) currentpos + (((1f - fraction) * -200f) * intensity2)));
							currentpos = tempfap.getLowLevelValue(33);
							tempfap.setLowLevelVariables(33, 1);
							tempfap.setLowLevelValues(33, (int) ((float) currentpos + (((1f - fraction) * -200f) * intensity2)));
							currentpos = tempfap.getLowLevelValue(35);
							tempfap.setLowLevelVariables(35, 1);
							tempfap.setLowLevelValues(35, (int) ((float) currentpos + (((1f - fraction) * -200f) * intensity2)));
						}
					}
					faps.insertElementAt(tempfap, i);
					x++;
				}
			} else if (id == JAWOPEN) {
				x = 0;
				from = fromframe;
				to = toframe;
				temp_jaw_vertical = jaw_vertical;
				jaw_vertical = 0;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(10);
					tempfap.setLowLevelVariables(10, 1);
					tempfap.setLowLevelValues(10, (int) ((float) currentpos + ((fraction * -400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(11);
					tempfap.setLowLevelVariables(11, 1);
					tempfap.setLowLevelValues(11, (int) ((float) currentpos + ((fraction * -400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(57);
					tempfap.setLowLevelVariables(57, 1);
					tempfap.setLowLevelValues(57, (int) ((float) currentpos + ((fraction * -400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(58);
					tempfap.setLowLevelVariables(58, 1);
					tempfap.setLowLevelValues(58, (int) ((float) currentpos + ((fraction * -400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(5);
					tempfap.setLowLevelVariables(5, 1);
					tempfap.setLowLevelValues(5, (int) ((float) currentpos + ((fraction * -400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(52);
					tempfap.setLowLevelVariables(52, 1);
					tempfap.setLowLevelValues(52, (int) ((float) currentpos + ((fraction * -400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(8);
					tempfap.setLowLevelVariables(8, 1);
					tempfap.setLowLevelValues(8, (int) ((float) currentpos + ((fraction * -400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(9);
					tempfap.setLowLevelVariables(9, 1);
					tempfap.setLowLevelValues(9, (int) ((float) currentpos + ((fraction * -400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(55);
					tempfap.setLowLevelVariables(55, 1);
					tempfap.setLowLevelValues(55, (int) ((float) currentpos + ((fraction * -400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(56);
					tempfap.setLowLevelVariables(56, 1);
					tempfap.setLowLevelValues(56, (int) ((float) currentpos + ((fraction * -400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(4);
					tempfap.setLowLevelVariables(4, 1);
					tempfap.setLowLevelValues(4, (int) ((float) currentpos + ((fraction * -400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(51);
					tempfap.setLowLevelVariables(51, 1);
					tempfap.setLowLevelValues(51, (int) ((float) currentpos + ((fraction * -400f) * intensity2)));
					faps.insertElementAt(tempfap, i);
					x++;
				}
				jaw_vertical = (int) ((-400f) * intensity2);
				jaw_vertical = temp_jaw_vertical + jaw_vertical;
				if (jaw_vertical - temp_jaw_vertical < -5) {
					for (i = to; i < faps.size(); i++) {
						tempfap = (FAPData) faps.elementAt(i);
						faps.remove(i);
						currentpos = tempfap.getLowLevelValue(10);
						tempfap.setLowLevelVariables(10, 1);
						tempfap.setLowLevelValues(10, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(11);
						tempfap.setLowLevelVariables(11, 1);
						tempfap.setLowLevelValues(11, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(57);
						tempfap.setLowLevelVariables(57, 1);
						tempfap.setLowLevelValues(57, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(58);
						tempfap.setLowLevelVariables(58, 1);
						tempfap.setLowLevelValues(58, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(5);
						tempfap.setLowLevelVariables(5, 1);
						tempfap.setLowLevelValues(5, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(52);
						tempfap.setLowLevelVariables(52, 1);
						tempfap.setLowLevelValues(52, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(8);
						tempfap.setLowLevelVariables(8, 1);
						tempfap.setLowLevelValues(8, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(9);
						tempfap.setLowLevelVariables(9, 1);
						tempfap.setLowLevelValues(9, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(55);
						tempfap.setLowLevelVariables(55, 1);
						tempfap.setLowLevelValues(55, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(56);
						tempfap.setLowLevelVariables(56, 1);
						tempfap.setLowLevelValues(56, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(4);
						tempfap.setLowLevelVariables(4, 1);
						tempfap.setLowLevelValues(4, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(51);
						tempfap.setLowLevelVariables(51, 1);
						tempfap.setLowLevelValues(51, currentpos + (jaw_vertical - temp_jaw_vertical));
						faps.insertElementAt(new FAPData(tempfap), i);
					}
				} else {
					jaw_vertical = 0;
					for (i = to; i < faps.size(); i++) {
						tempfap = (FAPData) faps.elementAt(i);
						faps.remove(i);
						tempfap.setLowLevelVariables(10, 0);
						tempfap.setLowLevelValues(10, 0);
						tempfap.setLowLevelVariables(11, 0);
						tempfap.setLowLevelValues(11, 0);
						tempfap.setLowLevelVariables(57, 0);
						tempfap.setLowLevelValues(57, 0);
						tempfap.setLowLevelVariables(58, 0);
						tempfap.setLowLevelValues(58, 0);
						tempfap.setLowLevelVariables(5, 0);
						tempfap.setLowLevelValues(5, 0);
						tempfap.setLowLevelVariables(52, 0);
						tempfap.setLowLevelValues(52, 0);
						tempfap.setLowLevelVariables(8, 0);
						tempfap.setLowLevelValues(8, 0);
						tempfap.setLowLevelVariables(9, 0);
						tempfap.setLowLevelValues(9, 0);
						tempfap.setLowLevelVariables(55, 0);
						tempfap.setLowLevelValues(55, 0);
						tempfap.setLowLevelVariables(56, 0);
						tempfap.setLowLevelValues(56, 0);
						tempfap.setLowLevelVariables(4, 0);
						tempfap.setLowLevelValues(4, 0);
						tempfap.setLowLevelVariables(51, 0);
						tempfap.setLowLevelValues(51, 0);
						faps.insertElementAt(new FAPData(tempfap), i);
					}
				}
			} else if (id == JAWCLOSE) {
				x = 0;
				from = fromframe;
				to = toframe;
				temp_jaw_vertical = jaw_vertical;
				jaw_vertical = 0;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(10);
					tempfap.setLowLevelVariables(10, 1);
					tempfap.setLowLevelValues(10, (int) ((float) currentpos + ((fraction * 400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(11);
					tempfap.setLowLevelVariables(11, 1);
					tempfap.setLowLevelValues(11, (int) ((float) currentpos + ((fraction * 400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(57);
					tempfap.setLowLevelVariables(57, 1);
					tempfap.setLowLevelValues(57, (int) ((float) currentpos + ((fraction * 400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(58);
					tempfap.setLowLevelVariables(58, 1);
					tempfap.setLowLevelValues(58, (int) ((float) currentpos + ((fraction * 400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(5);
					tempfap.setLowLevelVariables(5, 1);
					tempfap.setLowLevelValues(5, (int) ((float) currentpos + ((fraction * 400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(52);
					tempfap.setLowLevelVariables(52, 1);
					tempfap.setLowLevelValues(52, (int) ((float) currentpos + ((fraction * 400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(8);
					tempfap.setLowLevelVariables(8, 1);
					tempfap.setLowLevelValues(8, (int) ((float) currentpos + ((fraction * 400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(9);
					tempfap.setLowLevelVariables(9, 1);
					tempfap.setLowLevelValues(9, (int) ((float) currentpos + ((fraction * 400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(55);
					tempfap.setLowLevelVariables(55, 1);
					tempfap.setLowLevelValues(55, (int) ((float) currentpos + ((fraction * 400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(56);
					tempfap.setLowLevelVariables(56, 1);
					tempfap.setLowLevelValues(56, (int) ((float) currentpos + ((fraction * 400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(4);
					tempfap.setLowLevelVariables(4, 1);
					tempfap.setLowLevelValues(4, (int) ((float) currentpos + ((fraction * 400f) * intensity2)));
					currentpos = tempfap.getLowLevelValue(51);
					tempfap.setLowLevelVariables(51, 1);
					tempfap.setLowLevelValues(51, (int) ((float) currentpos + ((fraction * 400f) * intensity2)));
					faps.insertElementAt(tempfap, i);
					x++;
				}
				jaw_vertical = (int) ((400f) * intensity2);
				jaw_vertical = temp_jaw_vertical + jaw_vertical;
				if (jaw_vertical - temp_jaw_vertical < -5) {
					for (i = to; i < faps.size(); i++) {
						tempfap = (FAPData) faps.elementAt(i);
						faps.remove(i);
						currentpos = tempfap.getLowLevelValue(10);
						tempfap.setLowLevelVariables(10, 1);
						tempfap.setLowLevelValues(10, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(11);
						tempfap.setLowLevelVariables(11, 1);
						tempfap.setLowLevelValues(11, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(57);
						tempfap.setLowLevelVariables(57, 1);
						tempfap.setLowLevelValues(57, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(58);
						tempfap.setLowLevelVariables(58, 1);
						tempfap.setLowLevelValues(58, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(5);
						tempfap.setLowLevelVariables(5, 1);
						tempfap.setLowLevelValues(5, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(52);
						tempfap.setLowLevelVariables(52, 1);
						tempfap.setLowLevelValues(52, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(8);
						tempfap.setLowLevelVariables(8, 1);
						tempfap.setLowLevelValues(8, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(9);
						tempfap.setLowLevelVariables(9, 1);
						tempfap.setLowLevelValues(9, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(55);
						tempfap.setLowLevelVariables(55, 1);
						tempfap.setLowLevelValues(55, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(56);
						tempfap.setLowLevelVariables(56, 1);
						tempfap.setLowLevelValues(56, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(4);
						tempfap.setLowLevelVariables(4, 1);
						tempfap.setLowLevelValues(4, currentpos + (jaw_vertical - temp_jaw_vertical));
						currentpos = tempfap.getLowLevelValue(51);
						tempfap.setLowLevelVariables(51, 1);
						tempfap.setLowLevelValues(51, currentpos + (jaw_vertical - temp_jaw_vertical));
						faps.insertElementAt(tempfap, i);
					}
				} else {
					jaw_vertical = 0;
					for (i = to; i < faps.size(); i++) {
						tempfap = (FAPData) faps.elementAt(i);
						faps.remove(i);
						tempfap.setLowLevelVariables(10, 0);
						tempfap.setLowLevelValues(10, 0);
						tempfap.setLowLevelVariables(11, 0);
						tempfap.setLowLevelValues(11, 0);
						tempfap.setLowLevelVariables(57, 0);
						tempfap.setLowLevelValues(57, 0);
						tempfap.setLowLevelVariables(58, 0);
						tempfap.setLowLevelValues(58, 0);
						tempfap.setLowLevelVariables(5, 0);
						tempfap.setLowLevelValues(5, 0);
						tempfap.setLowLevelVariables(52, 0);
						tempfap.setLowLevelValues(52, 0);
						tempfap.setLowLevelVariables(8, 0);
						tempfap.setLowLevelValues(8, 0);
						tempfap.setLowLevelVariables(9, 0);
						tempfap.setLowLevelValues(9, 0);
						tempfap.setLowLevelVariables(55, 0);
						tempfap.setLowLevelValues(55, 0);
						tempfap.setLowLevelVariables(56, 0);
						tempfap.setLowLevelValues(56, 0);
						tempfap.setLowLevelVariables(4, 0);
						tempfap.setLowLevelValues(4, 0);
						tempfap.setLowLevelVariables(51, 0);
						tempfap.setLowLevelValues(51, 0);
						faps.insertElementAt(tempfap, i);
					}
				}
			}
		}
		return (faps);
	}


	/**
	 *  Apply facial gestures to MPEG-4 FAPs
	 *
	 *@param  faps       The FAPs to apply gestures
	 *@param  fromframe  The index to start gestures from
	 *@param  toframe    The index to end gestures at
	 *@param  id         The ID of the element
	 *@param  intensity  The intensity of the element
	 *@param  repeat     How many times to repeat the facial animation
	 *@return            The modified FAPs
	 */
	public Vector applyFacialGesture(Vector faps, int fromframe, int toframe, int id, int intensity, int repeat) {
		int i;
		int x;
		int j;
		int currentpos;
		float fraction;
		int from;
		int to;
		FAPData tempfap;
		int tempfromframe;
		int temptoframe;
		int size;
		float intensity2;

		intensity2 = (float) intensity / 100f;

		if (fromframe < 0) {
			fromframe = 0;
		}
		if (toframe < 0) {
			toframe = 0;
		}
		if (fromframe >= faps.size()) {
			return (faps);
		} else if (toframe >= faps.size()) {
			size = faps.size();
			for (i = 0; i <= toframe - size; i++) {
				tempfap = new FAPData();
				if (head_yaw != 0) {
					tempfap.setLowLevelVariables(49, 1);
					tempfap.setLowLevelValues(49, head_yaw);
				}
				if (eyes_yaw != 0) {
					tempfap.setLowLevelVariables(23, 1);
					tempfap.setLowLevelValues(23, eyes_yaw);
					tempfap.setLowLevelVariables(24, 1);
					tempfap.setLowLevelValues(24, eyes_yaw);
				}
				if (head_pitch != 0) {
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, head_pitch);
				}
				if (eyes_pitch != 0) {
					tempfap.setLowLevelVariables(25, 1);
					tempfap.setLowLevelValues(25, eyes_pitch);
					tempfap.setLowLevelVariables(26, 1);
					tempfap.setLowLevelValues(26, eyes_pitch);
				}
				if (head_roll != 0) {
					tempfap.setLowLevelVariables(50, 1);
					tempfap.setLowLevelValues(50, head_roll);
				}
				if (eyebrow_vertical_left != 0) {
					tempfap.setLowLevelVariables(31, 1);
					tempfap.setLowLevelValues(31, eyebrow_vertical_left);
					tempfap.setLowLevelVariables(33, 1);
					tempfap.setLowLevelValues(33, eyebrow_vertical_left);
					tempfap.setLowLevelVariables(35, 1);
					tempfap.setLowLevelValues(35, eyebrow_vertical_left);
				}
				if (eyebrow_vertical_right != 0) {
					tempfap.setLowLevelVariables(32, 1);
					tempfap.setLowLevelValues(32, eyebrow_vertical_right);
					tempfap.setLowLevelVariables(34, 1);
					tempfap.setLowLevelValues(34, eyebrow_vertical_right);
					tempfap.setLowLevelVariables(36, 1);
					tempfap.setLowLevelValues(36, eyebrow_vertical_right);
				}
				if (jaw_vertical != 0) {
					tempfap.setLowLevelVariables(10, 1);
					tempfap.setLowLevelValues(10, jaw_vertical);
					tempfap.setLowLevelVariables(11, 1);
					tempfap.setLowLevelValues(11, jaw_vertical);
					tempfap.setLowLevelVariables(57, 1);
					tempfap.setLowLevelValues(57, jaw_vertical);
					tempfap.setLowLevelVariables(58, 1);
					tempfap.setLowLevelValues(58, jaw_vertical);
					tempfap.setLowLevelVariables(5, 1);
					tempfap.setLowLevelValues(5, jaw_vertical);
					tempfap.setLowLevelVariables(52, 1);
					tempfap.setLowLevelValues(52, jaw_vertical);
					tempfap.setLowLevelVariables(8, 1);
					tempfap.setLowLevelValues(8, jaw_vertical);
					tempfap.setLowLevelVariables(9, 1);
					tempfap.setLowLevelValues(9, jaw_vertical);
					tempfap.setLowLevelVariables(55, 1);
					tempfap.setLowLevelValues(55, jaw_vertical);
					tempfap.setLowLevelVariables(56, 1);
					tempfap.setLowLevelValues(56, jaw_vertical);
					tempfap.setLowLevelVariables(4, 1);
					tempfap.setLowLevelValues(4, jaw_vertical);
					tempfap.setLowLevelVariables(51, 1);
					tempfap.setLowLevelValues(51, jaw_vertical);
				}
				faps.addElement(tempfap);
			}
		}

		tempfromframe = fromframe;
		temptoframe = toframe;
		toframe = tempfromframe;
		for (j = 0; j < repeat; j++) {
			fromframe = toframe;
			toframe = ((temptoframe - tempfromframe) / repeat) + fromframe;

			if (id == AGREE) {
				x = 0;
				from = fromframe;
				to = ((toframe - fromframe) / 4) + fromframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(48);
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 5f)) * 100000f)) * intensity2)));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
				x = 0;
				from = to;
				to = ((toframe - fromframe) / 2) + fromframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					currentpos = tempfap.getLowLevelValue(48);
					faps.remove(i);
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / 5f)) * 100000f)) * intensity2)));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
				x = 0;
				from = to;
				to = ((3 * (toframe - fromframe)) / 4) + fromframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					currentpos = tempfap.getLowLevelValue(48);
					faps.remove(i);
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -5f)) * 100000f)) * intensity2)));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
				x = 0;
				from = to;
				to = toframe;
				for (i = from; i <= to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					currentpos = tempfap.getLowLevelValue(48);
					faps.remove(i);
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / -5f)) * 100000f)) * intensity2)));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
			} else if (id == DISAGREE) {
				x = 0;
				from = fromframe;
				to = ((toframe - fromframe) / 4) + fromframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(49);
					tempfap.setLowLevelVariables(49, 1);
					tempfap.setLowLevelValues(49, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 2f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(23);
					tempfap.setLowLevelVariables(23, 1);
					tempfap.setLowLevelValues(23, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -6f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(24);
					tempfap.setLowLevelVariables(24, 1);
					tempfap.setLowLevelValues(24, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -6f)) * 100000f)) * intensity2)));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
				x = 0;
				from = to;
				to = ((toframe - fromframe) / 2) + fromframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(49);
					tempfap.setLowLevelVariables(49, 1);
					tempfap.setLowLevelValues(49, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / 2f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(23);
					tempfap.setLowLevelVariables(23, 1);
					tempfap.setLowLevelValues(23, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / -6f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(24);
					tempfap.setLowLevelVariables(24, 1);
					tempfap.setLowLevelValues(24, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / -6f)) * 100000f)) * intensity2)));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
				x = 0;
				from = to;
				to = ((3 * (toframe - fromframe)) / 4) + fromframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(49);
					tempfap.setLowLevelVariables(49, 1);
					tempfap.setLowLevelValues(49, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -2f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(23);
					tempfap.setLowLevelVariables(23, 1);
					tempfap.setLowLevelValues(23, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 6f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(24);
					tempfap.setLowLevelVariables(24, 1);
					tempfap.setLowLevelValues(24, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 6f)) * 100000f)) * intensity2)));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
				x = 0;
				from = to;
				to = toframe;
				for (i = from; i <= to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(49);
					tempfap.setLowLevelVariables(49, 1);
					tempfap.setLowLevelValues(49, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / -2f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(23);
					tempfap.setLowLevelVariables(23, 1);
					tempfap.setLowLevelValues(23, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / 6f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(24);
					tempfap.setLowLevelVariables(24, 1);
					tempfap.setLowLevelValues(24, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / 6f)) * 100000f)) * intensity2)));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
			} else if (id == CONCENTRATE) {
				x = 0;
				from = fromframe;
				to = ((toframe - fromframe) / 5) + fromframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(31);
					tempfap.setLowLevelVariables(31, 1);
					tempfap.setLowLevelValues(31, (int) ((float) currentpos + (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(32);
					tempfap.setLowLevelVariables(32, 1);
					tempfap.setLowLevelValues(32, (int) ((float) currentpos + (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(33);
					tempfap.setLowLevelVariables(33, 1);
					tempfap.setLowLevelValues(33, (int) ((float) currentpos + (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(34);
					tempfap.setLowLevelVariables(34, 1);
					tempfap.setLowLevelValues(34, (int) ((float) currentpos + (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(35);
					tempfap.setLowLevelVariables(35, 1);
					tempfap.setLowLevelValues(35, (int) ((float) currentpos + (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(36);
					tempfap.setLowLevelVariables(36, 1);
					tempfap.setLowLevelValues(36, (int) ((float) currentpos + (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(19);
					tempfap.setLowLevelVariables(19, 1);
					tempfap.setLowLevelValues(19, (int) ((float) currentpos + (fraction * (224f * intensity2))));
					currentpos = tempfap.getLowLevelValue(20);
					tempfap.setLowLevelVariables(20, 1);
					tempfap.setLowLevelValues(20, (int) ((float) currentpos + (fraction * (224f * intensity2))));
					currentpos = tempfap.getLowLevelValue(21);
					tempfap.setLowLevelVariables(21, 1);
					tempfap.setLowLevelValues(21, (int) ((float) currentpos + (fraction * (124f * intensity2))));
					currentpos = tempfap.getLowLevelValue(22);
					tempfap.setLowLevelVariables(22, 1);
					tempfap.setLowLevelValues(22, (int) ((float) currentpos + (fraction * (124f * intensity2))));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
				x = 0;
				from = to;
				to = ((4 * (toframe - fromframe)) / 5) + fromframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(31);
					tempfap.setLowLevelVariables(31, 1);
					tempfap.setLowLevelValues(31, (int) ((float) currentpos + (-200f * intensity2)));
					currentpos = tempfap.getLowLevelValue(32);
					tempfap.setLowLevelVariables(32, 1);
					tempfap.setLowLevelValues(32, (int) ((float) currentpos + (-200f * intensity2)));
					currentpos = tempfap.getLowLevelValue(33);
					tempfap.setLowLevelVariables(33, 1);
					tempfap.setLowLevelValues(33, (int) ((float) currentpos + (-200f * intensity2)));
					currentpos = tempfap.getLowLevelValue(34);
					tempfap.setLowLevelVariables(34, 1);
					tempfap.setLowLevelValues(34, (int) ((float) currentpos + (-200f * intensity2)));
					currentpos = tempfap.getLowLevelValue(35);
					tempfap.setLowLevelVariables(35, 1);
					tempfap.setLowLevelValues(35, (int) ((float) currentpos + (-200f * intensity2)));
					currentpos = tempfap.getLowLevelValue(36);
					tempfap.setLowLevelVariables(36, 1);
					tempfap.setLowLevelValues(36, (int) ((float) currentpos + (-200f * intensity2)));
					currentpos = tempfap.getLowLevelValue(19);
					tempfap.setLowLevelVariables(19, 1);
					tempfap.setLowLevelValues(19, (int) ((float) currentpos + (224f * intensity2)));
					currentpos = tempfap.getLowLevelValue(20);
					tempfap.setLowLevelVariables(20, 1);
					tempfap.setLowLevelValues(20, (int) ((float) currentpos + (224f * intensity2)));
					currentpos = tempfap.getLowLevelValue(21);
					tempfap.setLowLevelVariables(21, 1);
					tempfap.setLowLevelValues(21, (int) ((float) currentpos + (124f * intensity2)));
					currentpos = tempfap.getLowLevelValue(22);
					tempfap.setLowLevelVariables(22, 1);
					tempfap.setLowLevelValues(22, (int) ((float) currentpos + (124f * intensity2)));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
				x = 0;
				from = to;
				to = toframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(31);
					tempfap.setLowLevelVariables(31, 1);
					tempfap.setLowLevelValues(31, (int) (((float) currentpos + (-200f * intensity2)) - (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(32);
					tempfap.setLowLevelVariables(32, 1);
					tempfap.setLowLevelValues(32, (int) (((float) currentpos + (-200f * intensity2)) - (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(33);
					tempfap.setLowLevelVariables(33, 1);
					tempfap.setLowLevelValues(33, (int) (((float) currentpos + (-200f * intensity2)) - (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(34);
					tempfap.setLowLevelVariables(34, 1);
					tempfap.setLowLevelValues(34, (int) (((float) currentpos + (-200f * intensity2)) - (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(35);
					tempfap.setLowLevelVariables(35, 1);
					tempfap.setLowLevelValues(35, (int) (((float) currentpos + (-200f * intensity2)) - (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(36);
					tempfap.setLowLevelVariables(36, 1);
					tempfap.setLowLevelValues(36, (int) (((float) currentpos + (-200f * intensity2)) - (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(19);
					tempfap.setLowLevelVariables(19, 1);
					tempfap.setLowLevelValues(19, (int) (((float) currentpos + (224f * intensity2)) - (fraction * (224f * intensity2))));
					currentpos = tempfap.getLowLevelValue(20);
					tempfap.setLowLevelVariables(20, 1);
					tempfap.setLowLevelValues(20, (int) (((float) currentpos + (224f * intensity2)) - (fraction * (224f * intensity2))));
					currentpos = tempfap.getLowLevelValue(21);
					tempfap.setLowLevelVariables(21, 1);
					tempfap.setLowLevelValues(21, (int) (((float) currentpos + (124f * intensity2)) - (fraction * (124f * intensity2))));
					currentpos = tempfap.getLowLevelValue(22);
					tempfap.setLowLevelVariables(22, 1);
					tempfap.setLowLevelValues(22, (int) (((float) currentpos + (124f * intensity2)) - (fraction * (124f * intensity2))));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
			} else if (id == EMPHASIS) {
				x = 0;
				from = fromframe;
				to = ((toframe - fromframe) / 5) + fromframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(31);
					tempfap.setLowLevelVariables(31, 1);
					tempfap.setLowLevelValues(31, (int) ((float) currentpos + (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(32);
					tempfap.setLowLevelVariables(32, 1);
					tempfap.setLowLevelValues(32, (int) ((float) currentpos + (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(33);
					tempfap.setLowLevelVariables(33, 1);
					tempfap.setLowLevelValues(33, (int) ((float) currentpos + (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(34);
					tempfap.setLowLevelVariables(34, 1);
					tempfap.setLowLevelValues(34, (int) ((float) currentpos + (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(35);
					tempfap.setLowLevelVariables(35, 1);
					tempfap.setLowLevelValues(35, (int) ((float) currentpos + (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(36);
					tempfap.setLowLevelVariables(36, 1);
					tempfap.setLowLevelValues(36, (int) ((float) currentpos + (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(48);
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -20f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(50);
					tempfap.setLowLevelVariables(50, 1);
					tempfap.setLowLevelValues(50, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -5f)) * 100000f)) * intensity2)));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
				x = 0;
				from = to;
				to = ((4 * (toframe - fromframe)) / 5) + fromframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(31);
					tempfap.setLowLevelVariables(31, 1);
					tempfap.setLowLevelValues(31, (int) ((float) currentpos + (-200f * intensity2)));
					currentpos = tempfap.getLowLevelValue(32);
					tempfap.setLowLevelVariables(32, 1);
					tempfap.setLowLevelValues(32, (int) ((float) currentpos + (-200f * intensity2)));
					currentpos = tempfap.getLowLevelValue(33);
					tempfap.setLowLevelVariables(33, 1);
					tempfap.setLowLevelValues(33, (int) ((float) currentpos + (-200f * intensity2)));
					currentpos = tempfap.getLowLevelValue(34);
					tempfap.setLowLevelVariables(34, 1);
					tempfap.setLowLevelValues(34, (int) ((float) currentpos + (-200f * intensity2)));
					currentpos = tempfap.getLowLevelValue(35);
					tempfap.setLowLevelVariables(35, 1);
					tempfap.setLowLevelValues(35, (int) ((float) currentpos + (-200f * intensity2)));
					currentpos = tempfap.getLowLevelValue(36);
					tempfap.setLowLevelVariables(36, 1);
					tempfap.setLowLevelValues(36, (int) ((float) currentpos + (-200f * intensity2)));
					currentpos = tempfap.getLowLevelValue(48);
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, (int) ((float) currentpos + ((((((float) Math.PI / -5f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(50);
					tempfap.setLowLevelVariables(50, 1);
					tempfap.setLowLevelValues(50, (int) ((float) currentpos + ((((((float) Math.PI / -5f)) * 100000f)) * intensity2)));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
				x = 0;
				from = to;
				to = toframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(31);
					tempfap.setLowLevelVariables(31, 1);
					tempfap.setLowLevelValues(31, (int) (((float) currentpos + (-200f * intensity2)) - (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(32);
					tempfap.setLowLevelVariables(32, 1);
					tempfap.setLowLevelValues(32, (int) (((float) currentpos + (-200f * intensity2)) - (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(33);
					tempfap.setLowLevelVariables(33, 1);
					tempfap.setLowLevelValues(33, (int) (((float) currentpos + (-200f * intensity2)) - (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(34);
					tempfap.setLowLevelVariables(34, 1);
					tempfap.setLowLevelValues(34, (int) (((float) currentpos + (-200f * intensity2)) - (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(35);
					tempfap.setLowLevelVariables(35, 1);
					tempfap.setLowLevelValues(35, (int) (((float) currentpos + (-200f * intensity2)) - (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(36);
					tempfap.setLowLevelVariables(36, 1);
					tempfap.setLowLevelValues(36, (int) (((float) currentpos + (-200f * intensity2)) - (fraction * (-200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(48);
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / -20f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(50);
					tempfap.setLowLevelVariables(50, 1);
					tempfap.setLowLevelValues(50, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / -20f)) * 100000f)) * intensity2)));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
			} else if (id == SIGH) {
				x = 0;
				from = fromframe;
				to = ((toframe - fromframe) / 5) + fromframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(59);
					tempfap.setLowLevelVariables(59, 1);
					tempfap.setLowLevelValues(59, (int) ((float) currentpos + (fraction * (-300f * intensity2))));
					currentpos = tempfap.getLowLevelValue(12);
					tempfap.setLowLevelVariables(12, 1);
					tempfap.setLowLevelValues(12, (int) ((float) currentpos + (fraction * (-300f * intensity2))));
					currentpos = tempfap.getLowLevelValue(19);
					tempfap.setLowLevelVariables(19, 1);
					tempfap.setLowLevelValues(19, (int) ((float) currentpos + (fraction * (224f * intensity2))));
					currentpos = tempfap.getLowLevelValue(20);
					tempfap.setLowLevelVariables(20, 1);
					tempfap.setLowLevelValues(20, (int) ((float) currentpos + (fraction * (224f * intensity2))));
					currentpos = tempfap.getLowLevelValue(48);
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -20f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(50);
					tempfap.setLowLevelVariables(50, 1);
					tempfap.setLowLevelValues(50, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 20f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(23);
					tempfap.setLowLevelVariables(23, 1);
					tempfap.setLowLevelValues(23, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -5f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(24);
					tempfap.setLowLevelVariables(24, 1);
					tempfap.setLowLevelValues(24, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -5f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(25);
					tempfap.setLowLevelVariables(25, 1);
					tempfap.setLowLevelValues(25, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 5f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(26);
					tempfap.setLowLevelVariables(26, 1);
					tempfap.setLowLevelValues(26, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 5f)) * 100000f)) * intensity2)));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
				x = 0;
				from = to;
				to = ((4 * (toframe - fromframe)) / 5) + fromframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(59);
					tempfap.setLowLevelVariables(59, 1);
					tempfap.setLowLevelValues(59, (int) ((float) currentpos + (-300f * intensity2)));
					currentpos = tempfap.getLowLevelValue(12);
					tempfap.setLowLevelVariables(12, 1);
					tempfap.setLowLevelValues(12, (int) ((float) currentpos + (-300f * intensity2)));
					currentpos = tempfap.getLowLevelValue(19);
					tempfap.setLowLevelVariables(19, 1);
					tempfap.setLowLevelValues(19, (int) ((float) currentpos + (224f * intensity2)));
					currentpos = tempfap.getLowLevelValue(20);
					tempfap.setLowLevelVariables(20, 1);
					tempfap.setLowLevelValues(20, (int) ((float) currentpos + (224f * intensity2)));
					currentpos = tempfap.getLowLevelValue(48);
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, (int) ((float) currentpos + ((((((float) Math.PI / -20f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(50);
					tempfap.setLowLevelVariables(50, 1);
					tempfap.setLowLevelValues(50, (int) ((float) currentpos + ((((((float) Math.PI / 20f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(23);
					tempfap.setLowLevelVariables(23, 1);
					tempfap.setLowLevelValues(23, (int) ((float) currentpos + ((((((float) Math.PI / -5f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(24);
					tempfap.setLowLevelVariables(24, 1);
					tempfap.setLowLevelValues(24, (int) ((float) currentpos + ((((((float) Math.PI / -5f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(25);
					tempfap.setLowLevelVariables(25, 1);
					tempfap.setLowLevelValues(25, (int) ((float) currentpos + ((((((float) Math.PI / 5f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(26);
					tempfap.setLowLevelVariables(26, 1);
					tempfap.setLowLevelValues(26, (int) ((float) currentpos + ((((((float) Math.PI / 5f)) * 100000f)) * intensity2)));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
				x = 0;
				from = to;
				to = toframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(59);
					tempfap.setLowLevelVariables(59, 1);
					tempfap.setLowLevelValues(59, (int) ((float) currentpos + ((-300f * intensity2) - (fraction * (-300f * intensity2)))));
					currentpos = tempfap.getLowLevelValue(12);
					tempfap.setLowLevelVariables(12, 1);
					tempfap.setLowLevelValues(12, (int) ((float) currentpos + ((-300f * intensity2) - (fraction * (-300f * intensity2)))));
					currentpos = tempfap.getLowLevelValue(19);
					tempfap.setLowLevelVariables(19, 1);
					tempfap.setLowLevelValues(19, (int) (((float) currentpos + ((224f * intensity2)) - (fraction * (224f * intensity2)))));
					currentpos = tempfap.getLowLevelValue(20);
					tempfap.setLowLevelVariables(20, 1);
					tempfap.setLowLevelValues(20, (int) (((float) currentpos + ((224f * intensity2)) - (fraction * (224f * intensity2)))));
					currentpos = tempfap.getLowLevelValue(48);
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / -20f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(50);
					tempfap.setLowLevelVariables(50, 1);
					tempfap.setLowLevelValues(50, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / 20f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(23);
					tempfap.setLowLevelVariables(23, 1);
					tempfap.setLowLevelValues(23, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / -5f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(24);
					tempfap.setLowLevelVariables(24, 1);
					tempfap.setLowLevelValues(24, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / -5f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(25);
					tempfap.setLowLevelVariables(25, 1);
					tempfap.setLowLevelValues(25, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / 5f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(26);
					tempfap.setLowLevelVariables(26, 1);
					tempfap.setLowLevelValues(26, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / 5f)) * 100000f)) * intensity2)));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
			} else if (id == SMILE) {
				x = 0;
				from = fromframe;
				to = ((toframe - fromframe) / 5) + fromframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(59);
					tempfap.setLowLevelVariables(59, 1);
					tempfap.setLowLevelValues(59, (int) ((float) currentpos + (fraction * (300f * intensity2))));
					currentpos = tempfap.getLowLevelValue(60);
					tempfap.setLowLevelVariables(60, 1);
					tempfap.setLowLevelValues(60, (int) ((float) currentpos + (fraction * (300f * intensity2))));
					currentpos = tempfap.getLowLevelValue(12);
					tempfap.setLowLevelVariables(12, 1);
					tempfap.setLowLevelValues(12, (int) ((float) currentpos + (fraction * (300f * intensity2))));
					currentpos = tempfap.getLowLevelValue(13);
					tempfap.setLowLevelVariables(13, 1);
					tempfap.setLowLevelValues(13, (int) ((float) currentpos + (fraction * (300f * intensity2))));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
				x = 0;
				from = to;
				to = ((4 * (toframe - fromframe)) / 5) + fromframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(59);
					tempfap.setLowLevelVariables(59, 1);
					tempfap.setLowLevelValues(59, (int) ((float) currentpos + (300f * intensity2)));
					currentpos = tempfap.getLowLevelValue(60);
					tempfap.setLowLevelVariables(60, 1);
					tempfap.setLowLevelValues(60, (int) ((float) currentpos + (300f * intensity2)));
					currentpos = tempfap.getLowLevelValue(12);
					tempfap.setLowLevelVariables(12, 1);
					tempfap.setLowLevelValues(12, (int) ((float) currentpos + (300f * intensity2)));
					currentpos = tempfap.getLowLevelValue(13);
					tempfap.setLowLevelVariables(13, 1);
					tempfap.setLowLevelValues(13, (int) ((float) currentpos + (300f * intensity2)));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
				x = 0;
				from = to;
				to = toframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(59);
					tempfap.setLowLevelVariables(59, 1);
					tempfap.setLowLevelValues(59, (int) ((float) currentpos + ((300f * intensity2) - (fraction * (300f * intensity2)))));
					currentpos = tempfap.getLowLevelValue(60);
					tempfap.setLowLevelVariables(60, 1);
					tempfap.setLowLevelValues(60, (int) ((float) currentpos + ((300f * intensity2) - (fraction * (300f * intensity2)))));
					currentpos = tempfap.getLowLevelValue(12);
					tempfap.setLowLevelVariables(12, 1);
					tempfap.setLowLevelValues(12, (int) ((float) currentpos + ((300f * intensity2) - (fraction * (300f * intensity2)))));
					currentpos = tempfap.getLowLevelValue(13);
					tempfap.setLowLevelVariables(13, 1);
					tempfap.setLowLevelValues(13, (int) ((float) currentpos + ((300f * intensity2) - (fraction * (300f * intensity2)))));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
			} else if (id == SHRUG) {
				x = 0;
				from = fromframe;
				to = ((toframe - fromframe) / 5) + fromframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(31);
					tempfap.setLowLevelVariables(31, 1);
					tempfap.setLowLevelValues(31, (int) ((float) currentpos + (fraction * (200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(32);
					tempfap.setLowLevelVariables(32, 1);
					tempfap.setLowLevelValues(32, (int) ((float) currentpos + (fraction * (200f * intensity2))));
					currentpos = tempfap.getLowLevelValue(37);
					tempfap.setLowLevelVariables(37, 1);
					tempfap.setLowLevelValues(37, (int) ((float) currentpos + (fraction * (100f * intensity2))));
					currentpos = tempfap.getLowLevelValue(38);
					tempfap.setLowLevelVariables(38, 1);
					tempfap.setLowLevelValues(38, (int) ((float) currentpos + (fraction * (100f * intensity2))));
					currentpos = tempfap.getLowLevelValue(59);
					tempfap.setLowLevelVariables(59, 1);
					tempfap.setLowLevelValues(59, (int) ((float) currentpos + (fraction * (-150f * intensity2))));
					currentpos = tempfap.getLowLevelValue(60);
					tempfap.setLowLevelVariables(60, 1);
					tempfap.setLowLevelValues(60, (int) ((float) currentpos + (fraction * (-150f * intensity2))));
					currentpos = tempfap.getLowLevelValue(12);
					tempfap.setLowLevelVariables(12, 1);
					tempfap.setLowLevelValues(12, (int) ((float) currentpos + (fraction * (-150f * intensity2))));
					currentpos = tempfap.getLowLevelValue(13);
					tempfap.setLowLevelVariables(13, 1);
					tempfap.setLowLevelValues(13, (int) ((float) currentpos + (fraction * (-150f * intensity2))));
					currentpos = tempfap.getLowLevelValue(23);
					tempfap.setLowLevelVariables(23, 1);
					tempfap.setLowLevelValues(23, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 5f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(24);
					tempfap.setLowLevelVariables(24, 1);
					tempfap.setLowLevelValues(24, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / 5f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(25);
					tempfap.setLowLevelVariables(25, 1);
					tempfap.setLowLevelValues(25, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -5f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(26);
					tempfap.setLowLevelVariables(26, 1);
					tempfap.setLowLevelValues(26, (int) ((float) currentpos + ((((fraction * ((float) Math.PI / -5f)) * 100000f)) * intensity2)));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
				x = 0;
				from = to;
				to = ((4 * (toframe - fromframe)) / 5) + fromframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(31);
					tempfap.setLowLevelVariables(31, 1);
					tempfap.setLowLevelValues(31, (int) ((float) currentpos + (200f * intensity2)));
					currentpos = tempfap.getLowLevelValue(32);
					tempfap.setLowLevelVariables(32, 1);
					tempfap.setLowLevelValues(32, (int) ((float) currentpos + (200f * intensity2)));
					currentpos = tempfap.getLowLevelValue(37);
					tempfap.setLowLevelVariables(37, 1);
					tempfap.setLowLevelValues(37, (int) ((float) currentpos + (100f * intensity2)));
					currentpos = tempfap.getLowLevelValue(38);
					tempfap.setLowLevelVariables(38, 1);
					tempfap.setLowLevelValues(38, (int) ((float) currentpos + (100f * intensity2)));
					currentpos = tempfap.getLowLevelValue(59);
					tempfap.setLowLevelVariables(59, 1);
					tempfap.setLowLevelValues(59, (int) ((float) currentpos + (-150f * intensity2)));
					currentpos = tempfap.getLowLevelValue(60);
					tempfap.setLowLevelVariables(60, 1);
					tempfap.setLowLevelValues(60, (int) ((float) currentpos + (-150f * intensity2)));
					currentpos = tempfap.getLowLevelValue(12);
					tempfap.setLowLevelVariables(12, 1);
					tempfap.setLowLevelValues(12, (int) ((float) currentpos + (-150f * intensity2)));
					currentpos = tempfap.getLowLevelValue(13);
					tempfap.setLowLevelVariables(13, 1);
					tempfap.setLowLevelValues(13, (int) ((float) currentpos + (-150f * intensity2)));
					currentpos = tempfap.getLowLevelValue(23);
					tempfap.setLowLevelVariables(23, 1);
					tempfap.setLowLevelValues(23, (int) ((float) currentpos + ((((((float) Math.PI / 5f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(24);
					tempfap.setLowLevelVariables(24, 1);
					tempfap.setLowLevelValues(24, (int) ((float) currentpos + ((((((float) Math.PI / 5f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(25);
					tempfap.setLowLevelVariables(25, 1);
					tempfap.setLowLevelValues(25, (int) ((float) currentpos + ((((((float) Math.PI / -5f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(26);
					tempfap.setLowLevelVariables(26, 1);
					tempfap.setLowLevelValues(26, (int) ((float) currentpos + ((((((float) Math.PI / -5f)) * 100000f)) * intensity2)));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
				x = 0;
				from = to;
				to = toframe;
				for (i = from; i < to; i++) {
					fraction = (float) x / ((float) to - (float) from);
					tempfap = (FAPData) faps.elementAt(i);
					faps.remove(i);
					currentpos = tempfap.getLowLevelValue(31);
					tempfap.setLowLevelVariables(31, 1);
					tempfap.setLowLevelValues(31, (int) ((float) currentpos + ((200f * intensity2) - (fraction * (200f * intensity2)))));
					currentpos = tempfap.getLowLevelValue(32);
					tempfap.setLowLevelVariables(32, 1);
					tempfap.setLowLevelValues(32, (int) ((float) currentpos + ((200f * intensity2) - (fraction * (200f * intensity2)))));
					currentpos = tempfap.getLowLevelValue(37);
					tempfap.setLowLevelVariables(37, 1);
					tempfap.setLowLevelValues(37, (int) ((float) currentpos + ((100f * intensity2) - (fraction * (100f * intensity2)))));
					currentpos = tempfap.getLowLevelValue(38);
					tempfap.setLowLevelVariables(38, 1);
					tempfap.setLowLevelValues(38, (int) ((float) currentpos + ((100f * intensity2) - (fraction * (100f * intensity2)))));
					currentpos = tempfap.getLowLevelValue(59);
					tempfap.setLowLevelVariables(59, 1);
					tempfap.setLowLevelValues(59, (int) ((float) currentpos + ((-150f * intensity2) - (fraction * (-150f * intensity2)))));
					currentpos = tempfap.getLowLevelValue(60);
					tempfap.setLowLevelVariables(60, 1);
					tempfap.setLowLevelValues(60, (int) ((float) currentpos + ((-150f * intensity2) - (fraction * (-150f * intensity2)))));
					currentpos = tempfap.getLowLevelValue(12);
					tempfap.setLowLevelVariables(12, 1);
					tempfap.setLowLevelValues(12, (int) ((float) currentpos + ((-150f * intensity2) - (fraction * (-150f * intensity2)))));
					currentpos = tempfap.getLowLevelValue(13);
					tempfap.setLowLevelVariables(13, 1);
					tempfap.setLowLevelValues(13, (int) ((float) currentpos + ((-150f * intensity2) - (fraction * (-150f * intensity2)))));
					currentpos = tempfap.getLowLevelValue(23);
					tempfap.setLowLevelVariables(23, 1);
					tempfap.setLowLevelValues(23, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / 5f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(24);
					tempfap.setLowLevelVariables(24, 1);
					tempfap.setLowLevelValues(24, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / 5f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(25);
					tempfap.setLowLevelVariables(25, 1);
					tempfap.setLowLevelValues(25, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / -5f)) * 100000f)) * intensity2)));
					currentpos = tempfap.getLowLevelValue(26);
					tempfap.setLowLevelVariables(26, 1);
					tempfap.setLowLevelValues(26, (int) ((float) currentpos + (((((1f - fraction) * ((float) Math.PI / -5f)) * 100000f)) * intensity2)));
					faps.insertElementAt(new FAPData(tempfap), i);
					x++;
				}
			}
		}
		return faps;
	}


	/**
	 *  Apply facial expressions to MPEG-4 FAPs
	 *
	 *@param  faps       The FAPs to apply expressions
	 *@param  fromframe  The index to start expressions from
	 *@param  toframe    The index to end expressions at
	 *@param  id         The ID of the element
	 *@param  intensity  The intensity of the element
	 *@return            The modified FAPs
	 */
	public Vector applyFacialExpression(Vector faps, int fromframe, int toframe, int id, int intensity) {
		Vector temp;
		int i;
		int transframes1;
		int transframes2;
		String vals;
		float in1;
		int insertat;
		int limit;
		int x;
		int tempintensity1;
		int tempintensity2;
		int tempint;
		FAPData tempfap;
		int expression1;
		int expression2;
		int intensity1;
		int intensity2;
		int size;
		float fraction;

		id = expressionlookup[id];

		fromframe--;
		toframe++;
		toframe = toframe + expressiontransitionframes;
		if (fromframe < 0) {
			fromframe = 0;
		}
		if (toframe < 0) {
			toframe = 0;
		}
		if (toframe - fromframe < (2 * expressiontransitionframes)) {
			toframe = fromframe + (2 * expressiontransitionframes);
		}
		if (fromframe >= faps.size()) {
			return (faps);
		} else if (toframe >= faps.size()) {
			size = faps.size();
			for (i = 0; i <= toframe - size; i++) {
				tempfap = new FAPData();
				if (head_yaw != 0) {
					tempfap.setLowLevelVariables(49, 1);
					tempfap.setLowLevelValues(49, head_yaw);
				}
				if (eyes_yaw != 0) {
					tempfap.setLowLevelVariables(23, 1);
					tempfap.setLowLevelValues(23, eyes_yaw);
					tempfap.setLowLevelVariables(24, 1);
					tempfap.setLowLevelValues(24, eyes_yaw);
				}
				if (head_pitch != 0) {
					tempfap.setLowLevelVariables(48, 1);
					tempfap.setLowLevelValues(48, head_pitch);
				}
				if (eyes_pitch != 0) {
					tempfap.setLowLevelVariables(25, 1);
					tempfap.setLowLevelValues(25, eyes_pitch);
					tempfap.setLowLevelVariables(26, 1);
					tempfap.setLowLevelValues(26, eyes_pitch);
				}
				if (head_roll != 0) {
					tempfap.setLowLevelVariables(50, 1);
					tempfap.setLowLevelValues(50, head_roll);
				}
				if (eyebrow_vertical_left != 0) {
					tempfap.setLowLevelVariables(31, 1);
					tempfap.setLowLevelValues(31, eyebrow_vertical_left);
					tempfap.setLowLevelVariables(33, 1);
					tempfap.setLowLevelValues(33, eyebrow_vertical_left);
					tempfap.setLowLevelVariables(35, 1);
					tempfap.setLowLevelValues(35, eyebrow_vertical_left);
				}
				if (eyebrow_vertical_right != 0) {
					tempfap.setLowLevelVariables(32, 1);
					tempfap.setLowLevelValues(32, eyebrow_vertical_right);
					tempfap.setLowLevelVariables(34, 1);
					tempfap.setLowLevelValues(34, eyebrow_vertical_right);
					tempfap.setLowLevelVariables(36, 1);
					tempfap.setLowLevelValues(36, eyebrow_vertical_right);
				}
				if (jaw_vertical != 0) {
					tempfap.setLowLevelVariables(10, 1);
					tempfap.setLowLevelValues(10, jaw_vertical);
					tempfap.setLowLevelVariables(11, 1);
					tempfap.setLowLevelValues(11, jaw_vertical);
					tempfap.setLowLevelVariables(57, 1);
					tempfap.setLowLevelValues(57, jaw_vertical);
					tempfap.setLowLevelVariables(58, 1);
					tempfap.setLowLevelValues(58, jaw_vertical);
					tempfap.setLowLevelVariables(5, 1);
					tempfap.setLowLevelValues(5, jaw_vertical);
					tempfap.setLowLevelVariables(52, 1);
					tempfap.setLowLevelValues(52, jaw_vertical);
					tempfap.setLowLevelVariables(8, 1);
					tempfap.setLowLevelValues(8, jaw_vertical);
					tempfap.setLowLevelVariables(9, 1);
					tempfap.setLowLevelValues(9, jaw_vertical);
					tempfap.setLowLevelVariables(55, 1);
					tempfap.setLowLevelValues(55, jaw_vertical);
					tempfap.setLowLevelVariables(56, 1);
					tempfap.setLowLevelValues(56, jaw_vertical);
					tempfap.setLowLevelVariables(4, 1);
					tempfap.setLowLevelValues(4, jaw_vertical);
					tempfap.setLowLevelVariables(51, 1);
					tempfap.setLowLevelValues(51, jaw_vertical);
				}
				faps.addElement(tempfap);
			}
		}

		tempfap = (FAPData) faps.elementAt(fromframe);

		expression1 = tempfap.getExpression1();
		intensity1 = tempfap.getExpressionIntensity1();
		expression2 = tempfap.getExpression2();
		intensity2 = tempfap.getExpressionIntensity2();

		// Transition if needed
		if (expression1 == id) {
			x = 0;
			tempintensity1 = intensity1;
			tempintensity2 = intensity2;
			for (i = fromframe; i < fromframe + expressiontransitionframes; i++) {
				fraction = (float) x / (float) expressiontransitionframes;
				tempfap = (FAPData) faps.elementAt(i);
				faps.remove(i);
				intensity1 = (int) (fraction * ((float) (intensity - tempintensity1))) + tempintensity1;
				intensity2 = tempintensity2 - (int) ((float) tempintensity2 * fraction);
				if (intensity1 < 0) {
					intensity1 = 0;
				}
				if (intensity1 > 63) {
					intensity1 = 63;
				}
				if (intensity2 < 0) {
					intensity2 = 0;
				}
				if (intensity2 > 63) {
					intensity2 = 63;
				}
				tempfap.setExpressionVariables(1, 0, 0);
				tempfap.setExpressionValues(expression1, intensity1, expression2, intensity2);
				faps.insertElementAt(new FAPData(tempfap), i);
				x++;
			}
		} else if (expression2 == id) {
			x = 0;
			tempintensity1 = intensity1;
			tempintensity2 = intensity2;
			for (i = fromframe; i < fromframe + expressiontransitionframes; i++) {
				fraction = (float) x / (float) expressiontransitionframes;
				tempfap = (FAPData) faps.elementAt(i);
				faps.remove(i);
				intensity2 = (int) (fraction * ((float) (intensity - tempintensity2))) + tempintensity2;
				intensity1 = tempintensity1 - (int) ((float) tempintensity1 * fraction);
				if (intensity1 < 0) {
					intensity1 = 0;
				}
				if (intensity1 > 63) {
					intensity1 = 63;
				}
				if (intensity2 < 0) {
					intensity2 = 0;
				}
				if (intensity2 > 63) {
					intensity2 = 63;
				}
				tempfap.setExpressionVariables(1, 0, 0);
				tempfap.setExpressionValues(expression1, intensity1, expression2, intensity2);
				faps.insertElementAt(new FAPData(tempfap), i);
				x++;
			}
		} else {
			//Neither are right expression, go ahead with transition

			x = 0;
			tempintensity1 = intensity1;
			tempintensity2 = intensity2;
			for (i = fromframe; i < fromframe + expressiontransitionframes; i++) {
				fraction = (float) x / (float) expressiontransitionframes;
				tempfap = (FAPData) faps.elementAt(i);
				faps.remove(i);
				if (fraction >= 0.5f) {
					expression1 = id;
				}
				if (fraction < 0.5f) {
					intensity1 = tempintensity1 - (int) ((float) tempintensity1 * (fraction * 2f));
				} else {
					intensity1 = (int) ((float) intensity * (fraction - 0.5f) * 2f);
				}
				intensity2 = tempintensity2 - (int) ((float) tempintensity2 * fraction);
				if (intensity1 < 0) {
					intensity1 = 0;
				}
				if (intensity1 > 63) {
					intensity1 = 63;
				}
				if (intensity2 < 0) {
					intensity2 = 0;
				}
				if (intensity2 > 63) {
					intensity2 = 63;
				}
				tempfap.setExpressionVariables(1, 0, 0);
				tempfap.setExpressionValues(expression1, intensity1, expression2, intensity2);
				faps.insertElementAt(new FAPData(tempfap), i);
				x++;
			}
		}

		for (i = fromframe + expressiontransitionframes; i < toframe - expressiontransitionframes; i++) {
			tempfap = new FAPData((FAPData) faps.elementAt(i));
			faps.remove(i);
			tempfap.setExpressionVariables(1, 0, 0);
			tempfap.setExpressionValues(expression1, intensity1, expression2, intensity2);
			faps.insertElementAt(new FAPData(tempfap), i);
		}
		tempintensity1 = intensity1;
		tempintensity2 = intensity2;
		x = 0;
		for (i = toframe - expressiontransitionframes; i < toframe; i++) {
			fraction = (float) x / (float) expressiontransitionframes;
			tempfap = new FAPData((FAPData) faps.elementAt(i));
			faps.remove(i);
			intensity1 = tempintensity1 - (int) ((float) tempintensity1 * fraction);
			intensity2 = tempintensity2 - (int) ((float) tempintensity2 * fraction);
			if (intensity1 < 0) {
				intensity1 = 0;
			}
			if (intensity1 > 63) {
				intensity1 = 63;
			}
			if (intensity2 < 0) {
				intensity2 = 0;
			}
			if (intensity2 > 63) {
				intensity2 = 63;
			}
			tempfap.setExpressionVariables(1, 0, 0);
			tempfap.setExpressionValues(expression1, intensity1, expression2, intensity2);
			faps.insertElementAt(new FAPData(tempfap), i);
			x++;
		}
		return (faps);
	}
}

