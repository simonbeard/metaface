package metaface.embodiment;
import java.awt.geom.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import metaface.mpeg4.*;

/**
 *  This class defines the idle behaviour to be used by a MetaFace application.
 *  It is merely a default, and much more complicated behaviour can be extended
 *  from the class.<p>
 *
 *  The class implements a simple rule and parameter driven system for animating
 *  the MPEG-4 face at idle time.
 *
 *@author     Simon Beard 
 *@version    1.0
 */
public class IdleBehaviour {

	/**
	 *  Random number for adding variation to animation
	 */
	private Random random_numbers;
	/**
	 *  The frame rate for animation
	 */
	private int framerate;
	/**
	 *  Default size of idle animation buffer
	 */
	private int default_buffer_size;
	/**
	 *  The idle animation buffer
	 */
	private Vector buffer;
	/**
	 *  Internal array of MPEG-4 FAPs
	 */
	private FAPData[] fapdatabuffer;
	/**
	 *  Interval for blinking animation
	 */
	private int eye_blink_interval;
	/**
	 *  Interval margin of change for blinking animation
	 */
	private int eye_blink_interval_margin;
	/**
	 *  Duration of blinking down animation
	 */
	private int eye_blink_duration_down;
	/**
	 *  Duration for holding blinking animation
	 */
	private int eye_blink_duration_hold;
	/**
	 *  Duration of blinking up animation
	 */
	private int eye_blink_duration_up;
	/**
	 *  Duration margin of change for blinking animation
	 */
	private int eye_blink_duration_margin;
	/**
	 *  Interval for head roll animation
	 */
	private int head_roll_interval;
	/**
	 *  Duration margin of change for head roll animation
	 */
	private int head_roll_interval_margin;
	/**
	 *  Magnitude of head roll animation
	 */
	private int head_roll_amplitude;
	/**
	 *  Magnitude margin of change for head roll animation
	 */
	private int head_roll_amplitude_margin;
	/**
	 *  Duration of head roll animation
	 */
	private int head_roll_duration;
	/**
	 *  Duration margin of change for head roll animation
	 */
	private int head_roll_duration_margin;
	/**
	 *  Interval for eye movement animation
	 */
	private int eye_movement_interval;
	/**
	 *  Interval margin of change for eye movement animation
	 */
	private int eye_movement_interval_margin;
	/**
	 *  Magnitude of eye movement animation
	 */
	private int eye_movement_amplitude;
	/**
	 *  Magnitude margin of change for eye movement animation
	 */
	private int eye_movement_amplitude_margin;
	/**
	 *  Duration for eye movement animation
	 */
	private int eye_movement_duration;
	/**
	 *  Duration margin of change for eye movement animation
	 */
	private int eye_movement_duration_margin;


	/**
	 *  Constructor for the IdleBehaviour object
	 */
	public IdleBehaviour() {
		new IdleBehaviour(25);
	}


	/**
	 *  Constructor for the IdleBehaviour object
	 *
	 *@param  fps  The frame rate of animation
	 */
	public IdleBehaviour(int fps) {
		int i;

		//set variables
		framerate = fps;
		random_numbers = new Random();
		default_buffer_size = 30 * fps;

		//Set default parameters for idle animation
		setEyeBlinkParameters(fps * 3, fps, Math.round((float) fps / 6f), 1, Math.round((float) fps / 4f), 1);
		setHeadRollParameters(fps * 2, fps, fps * 2, 5, (int) ((Math.PI / 16f) * 100000f), (int) ((Math.PI / 10f) * 100000f));
		setEyeMovementParameters(fps, fps / 2, fps * 2, 5, (int) ((Math.PI / 8f) * 100000f), (int) ((Math.PI / 10f) * 100000f));

		//construct buffers
		buffer = new Vector();
		fapdatabuffer = new FAPData[125];
		for (i = 0; i < 125; i++) {
			fapdatabuffer[i] = new FAPData();
		}
	}


	/**
	 *  Sets the eyeBlinkParameters attribute of the IdleBehaviour object
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
	 *  Sets the headRollParameters attribute of the IdleBehaviour object
	 *
	 *@param  interval          Interval of head rolls
	 *@param  interval_margin   Margin of change for interval
	 *@param  duration          Duration of head rolls
	 *@param  duration_margin   Margin of change for duration
	 *@param  amplitude         Magnitude of head rolls
	 *@param  amplitude_margin  Margin of change for magnitude
	 */
	public void setHeadRollParameters(int interval, int interval_margin, int duration, int duration_margin, int amplitude, int amplitude_margin) {
		head_roll_interval = interval;
		head_roll_interval_margin = interval_margin;
		head_roll_duration = duration;
		head_roll_duration_margin = duration_margin;
		head_roll_amplitude = amplitude;
		head_roll_amplitude_margin = amplitude_margin;
	}


	/**
	 *  Sets the eyeMovementParameters attribute of the IdleBehaviour object
	 *
	 *@param  interval          Interval of eye movements
	 *@param  interval_margin   Margin of change for interval
	 *@param  duration          Duration of eye movements
	 *@param  duration_margin   Margin of change for duration
	 *@param  amplitude         Magnitude of eye movements
	 *@param  amplitude_margin  Margin of change for magnitudes
	 */
	public void setEyeMovementParameters(int interval, int interval_margin, int duration, int duration_margin, int amplitude, int amplitude_margin) {
		eye_movement_interval = interval;
		eye_movement_interval_margin = interval_margin;
		eye_movement_duration = duration;
		eye_movement_duration_margin = duration_margin;
		eye_movement_amplitude = amplitude;
		eye_movement_amplitude_margin = amplitude_margin;
	}


	/**
	 *  Generate MPEG-4 FAP eye blinks
	 */
	private void generateEyeBlinks() {
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
		int frames = 125;

		fit = true;

		position = 0;

		//While more eye blinks can fit in idle animation -- calculate random eye blinks based on parameters
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

			//Set the underlying FAP structure
			if (interval + down + hold + up < 125 - position) {
				position = position + interval;
				x = 1;
				for (i = position; i < down + position; i++) {
					fapdatabuffer[i].setLowLevelVariables(19, 1);
					fapdatabuffer[i].setLowLevelVariables(20, 1);
					fapdatabuffer[i].setLowLevelVariables(21, 1);
					fapdatabuffer[i].setLowLevelVariables(22, 1);
					fapdatabuffer[i].setLowLevelValues(19, x * (int) (1024f / (float) down));
					fapdatabuffer[i].setLowLevelValues(20, x * (int) (1024f / (float) down));
					fapdatabuffer[i].setLowLevelValues(21, x * (int) (124f / (float) down));
					fapdatabuffer[i].setLowLevelValues(22, x * (int) (124f / (float) down));
					x++;
				}
				position = position + down;
				x = 1;
				for (i = position; i < position + hold; i++) {
					fapdatabuffer[i].setLowLevelVariables(19, 1);
					fapdatabuffer[i].setLowLevelVariables(20, 1);
					fapdatabuffer[i].setLowLevelVariables(21, 1);
					fapdatabuffer[i].setLowLevelVariables(22, 1);
					fapdatabuffer[i].setLowLevelValues(19, 1024);
					fapdatabuffer[i].setLowLevelValues(20, 1024);
					fapdatabuffer[i].setLowLevelValues(21, 124);
					fapdatabuffer[i].setLowLevelValues(22, 124);
					x++;
				}
				position = position + hold;
				x = 1;
				for (i = position; i < position + up; i++) {
					fapdatabuffer[i].setLowLevelVariables(19, 1);
					fapdatabuffer[i].setLowLevelVariables(20, 1);
					fapdatabuffer[i].setLowLevelVariables(21, 1);
					fapdatabuffer[i].setLowLevelVariables(22, 1);
					fapdatabuffer[i].setLowLevelValues(19, 1024 - (x * (int) (1024f / (float) up)));
					fapdatabuffer[i].setLowLevelValues(20, 1024 - (x * (int) (1024f / (float) up)));
					fapdatabuffer[i].setLowLevelValues(21, 124 - (x * (int) (124f / (float) up)));
					fapdatabuffer[i].setLowLevelValues(22, 124 - (x * (int) (124f / (float) up)));
					x++;
				}
				position = position + up;
			} else {
				fit = false;
			}
		}
	}


	/**
	 *  Generate MPEG-4 FAP head rolls
	 */
	private void generateHeadRoll() {
		int interval;
		int left_right;
		int neg_pos;
		int amplitude;
		int duration;
		boolean fit;
		int position;
		FAPData frame;
		int i;
		int x;
		int frames = 125;

		fit = true;

		position = 0;
		//While more head rolls can fit in idle animation -- calculate random head rolls based on parameters
		while (fit) {
			left_right = random_numbers.nextInt(2);
			if (left_right == 0) {
				left_right = -1;
			}
			neg_pos = random_numbers.nextInt(2);
			if (neg_pos == 0) {
				neg_pos = -1;
			}
			interval = (head_roll_interval + ((int) ((float) (head_roll_interval_margin * neg_pos) * random_numbers.nextFloat())));
			neg_pos = random_numbers.nextInt(2);
			if (neg_pos == 0) {
				neg_pos = -1;
			}
			amplitude = left_right * (head_roll_amplitude + ((int) ((float) (head_roll_amplitude_margin * neg_pos) * random_numbers.nextFloat())));
			neg_pos = random_numbers.nextInt(2);
			if (neg_pos == 0) {
				neg_pos = -1;
			}

			//Set the underlying FAP structure
			duration = head_roll_duration + ((int) ((float) (head_roll_duration_margin * neg_pos) * random_numbers.nextFloat()));
			if (interval + duration + duration < 125 - position) {
				position = position + interval;
				x = 1;
				for (i = position; i < position + duration; i++) {
					fapdatabuffer[i].setLowLevelVariables(50, 1);
					fapdatabuffer[i].setLowLevelValues(50, x * (int) ((float) amplitude / (float) duration));
					x++;
				}
				position = position + duration;
				x = 1;
				for (i = position; i < position + duration; i++) {
					fapdatabuffer[i].setLowLevelVariables(50, 1);
					fapdatabuffer[i].setLowLevelValues(50, amplitude - (x * (int) ((float) amplitude / (float) duration)));
					x++;
				}
				position = position + duration;
			} else {
				fit = false;
			}
		}
	}


	/**
	 *  Generate MPEG-4 FAP eye movements
	 */
	private void generateEyeMovement() {
		int interval;
		int left_right;
		int up_down;
		int neg_pos;
		int amplitude_x;
		int amplitude_y;
		int duration;
		boolean fit;
		int position;
		FAPData frame;
		int i;
		int x;
		int frames = 125;

		fit = true;

		position = 0;
		//While more eye movements can fit in idle animation -- calculate random eye movements based on parameters
		while (fit) {
			left_right = random_numbers.nextInt(2);
			if (left_right == 0) {
				left_right = -1;
			}
			up_down = random_numbers.nextInt(2);
			if (up_down == 0) {
				up_down = -1;
			}
			neg_pos = random_numbers.nextInt(2);
			if (neg_pos == 0) {
				neg_pos = -1;
			}
			interval = (eye_movement_interval + ((int) ((float) (eye_movement_interval_margin * neg_pos) * random_numbers.nextFloat())));
			neg_pos = random_numbers.nextInt(2);
			if (neg_pos == 0) {
				neg_pos = -1;
			}
			amplitude_x = left_right * (eye_movement_amplitude + ((int) ((float) (eye_movement_amplitude_margin * neg_pos) * random_numbers.nextFloat())));
			neg_pos = random_numbers.nextInt(2);
			if (neg_pos == 0) {
				neg_pos = -1;
			}
			amplitude_y = up_down * (eye_movement_amplitude + ((int) ((float) (eye_movement_amplitude_margin * neg_pos) * random_numbers.nextFloat())));
			neg_pos = random_numbers.nextInt(2);
			if (neg_pos == 0) {
				neg_pos = -1;
			}

			//Set the underlying FAP structure
			duration = eye_movement_duration + ((int) ((float) (eye_movement_duration_margin * neg_pos) * random_numbers.nextFloat()));
			if (interval + duration + duration < 125 - position) {
				position = position + interval;
				x = 1;
				for (i = position; i < position + duration; i++) {
					fapdatabuffer[i].setLowLevelVariables(23, 1);
					fapdatabuffer[i].setLowLevelVariables(24, 1);
					fapdatabuffer[i].setLowLevelVariables(25, 1);
					fapdatabuffer[i].setLowLevelVariables(26, 1);
					fapdatabuffer[i].setLowLevelValues(23, x * (int) ((float) amplitude_x / (float) duration));
					fapdatabuffer[i].setLowLevelValues(24, x * (int) ((float) amplitude_x / (float) duration));
					fapdatabuffer[i].setLowLevelValues(25, x * (int) ((float) amplitude_y / (float) duration));
					fapdatabuffer[i].setLowLevelValues(26, x * (int) ((float) amplitude_y / (float) duration));
					x++;
				}
				position = position + duration;
				x = 1;
				for (i = position; i < position + duration; i++) {
					fapdatabuffer[i].setLowLevelVariables(23, 1);
					fapdatabuffer[i].setLowLevelVariables(24, 1);
					fapdatabuffer[i].setLowLevelVariables(25, 1);
					fapdatabuffer[i].setLowLevelVariables(26, 1);
					fapdatabuffer[i].setLowLevelValues(23, amplitude_x - (x * (int) ((float) amplitude_x / (float) duration)));
					fapdatabuffer[i].setLowLevelValues(24, amplitude_x - (x * (int) ((float) amplitude_x / (float) duration)));
					fapdatabuffer[i].setLowLevelValues(25, amplitude_y - (x * (int) ((float) amplitude_y / (float) duration)));
					fapdatabuffer[i].setLowLevelValues(26, amplitude_y - (x * (int) ((float) amplitude_y / (float) duration)));
					x++;
				}
				position = position + duration;
			} else {
				fit = false;
			}
		}
	}


	/**
	 *  Generates MPEG-4 FAPs for idle behaviour
	 *
	 *@param  frames  The number of FAP frames to generate
	 *@return         A list of FAPData MPEG-4 FAPs
	 *@see            metaface.mpeg4.FAPData
	 */
	public Vector generateIdleFAPs(int frames) {
		int i;
		Vector output;

		output = new Vector();

		for (i = 0; i < frames; i++) {
			fapdatabuffer[i].reset();
		}

		generateEyeBlinks();
		generateHeadRoll();
		generateEyeMovement();

		for (i = 0; i < frames; i++) {
			output.add(fapdatabuffer[i]);
		}

		return (output);
	}


	/**
	 *  Generates MPEG-4 FAPs for transitioning to the neutral position
	 *
	 *@param  lastframe  The MPEG-4 FAP frame to transition from
	 *@param  frames     The number of frames to use in the transition
	 *@return            A list of FAPData MPEG-4 FAPs
	 *@see               metaface.mpeg4.FAPData
	 */
	public Vector toNeutralFAPs(FAPData lastframe, int frames) {
		int i;
		int j;
		int val;
		int blend;
		int in1;
		int in2;
		Vector output;

		output = new Vector();

		//Linear transtion for all FAP values to zero over "i" frames
		for (i = 0; i < frames; i++) {
			output.add(new FAPData());
			((FAPData) output.elementAt(i)).reset();
			if (lastframe.visemeMaskSet()) {
				blend = lastframe.getVisemeBlend();
				blend = blend - (int) ((float) (i + 1) * ((float) blend / (float) frames));
				if (blend < 0) {
					blend = 0;
				}
				((FAPData) output.elementAt(i)).setVisemeValues(lastframe.getViseme1(), 0, blend);
				((FAPData) output.elementAt(i)).setVisemeVariables(1);
			}
			if (lastframe.expressionMaskSet()) {
				in1 = lastframe.getExpressionIntensity1();
				in2 = lastframe.getExpressionIntensity2();
				in1 = in1 - (int) ((float) (i + 1) * ((float) in1 / (float) frames));
				if (in1 < 0) {
					in1 = 0;
				}
				in2 = in2 - (int) ((float) (i + 1) * ((float) in2 / (float) frames));
				if (in2 < 0) {
					in2 = 0;
				}
				((FAPData) output.elementAt(i)).setExpressionValues(lastframe.getExpression1(), in1, lastframe.getExpression2(), in2);
				((FAPData) output.elementAt(i)).setExpressionVariables(1, 0, 0);
			}
			for (j = 3; j <= 68; j++) {
				if (lastframe.lowLevelMaskSet(j)) {
					val = lastframe.getLowLevelValue(j);
					val = val - (int) ((float) (i + 1) * ((float) val / (float) frames));
					if (val < 0) {
						val = 0;
					}
					((FAPData) output.elementAt(i)).setLowLevelValues(j, val);
					((FAPData) output.elementAt(i)).setLowLevelVariables(j, 1);
				}
			}
		}
		return (output);
	}
}

