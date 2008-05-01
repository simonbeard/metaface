package metaface.server;
import java.io.*;

/**
 *  This class stores a VHML Facial Animation Element for recall when rendering
 *  MPEG-4 FAPs
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        VHMLParser
 *@see        VHMLReader
 */
class VHMLFAElement {
	/**
	 *  The VHMLParser ID of the VHML element
	 */
	private int id;
	/**
	 *  The wait attribute of the VHML element
	 */
	private int wait;
	/**
	 *  The duration attribute of the VHML element
	 */
	private int duration;
	/**
	 *  The intensity attribute of the VHML element
	 */
	private int intensity;
	/**
	 *  The element will be rendered from this frame
	 */
	private int fromframe;
	/**
	 *  The element will be rendered to this frame
	 */
	private int toframe;
	/**
	 *  The repeat attribute of the VHML element
	 */
	private int repeat;
	/**
	 *  Whether the VHML element end tag has been met
	 */
	private boolean closedtag;
	/**
	 *  The which attribute of the VHML element
	 */
	private String which;


	/**
	 *  Constructor for the VHMLFAElement object
	 *
	 *@param  t_id         The VHMLReader ID of the VHML element
	 *@param  t_wait       The wait attribute of the VHML element
	 *@param  t_duration   The duration attribute of the VHML element
	 *@param  t_intensity  The intensity attribute of the VHML element
	 *@param  t_fromframe  The element will be rendered from this frame
	 *@param  t_toframe    The element will be rendered to this frame
	 */
	public VHMLFAElement(int t_id, int t_wait, int t_duration, int t_intensity, int t_fromframe, int t_toframe) {
		id = t_id;
		wait = t_wait;
		duration = t_duration;
		intensity = t_intensity;
		fromframe = t_fromframe;
		toframe = t_toframe;
		repeat = 1;
		closedtag = false;
		which = null;
	}


	/**
	 *  Constructor for the VHMLFAElement object
	 *
	 *@param  t_id         The VHMLReader ID of the VHML element
	 *@param  t_wait       The wait attribute of the VHML element
	 *@param  t_duration   The duration attribute of the VHML element
	 *@param  t_intensity  The intensity attribute of the VHML element
	 *@param  t_fromframe  The element will be rendered from this frame
	 *@param  t_toframe    The element will be rendered to this frame
	 *@param  t_repeat     The repeat attribute of the VHML element
	 */
	public VHMLFAElement(int t_id, int t_wait, int t_duration, int t_intensity, int t_fromframe, int t_toframe, int t_repeat) {
		id = t_id;
		wait = t_wait;
		duration = t_duration;
		intensity = t_intensity;
		fromframe = t_fromframe;
		toframe = t_toframe;
		repeat = t_repeat;
		closedtag = false;
		which = null;
	}


	/**
	 *  Constructor for the VHMLFAElement object
	 *
	 *@param  t_id         The VHMLReader ID of the VHML element
	 *@param  t_wait       The wait attribute of the VHML element
	 *@param  t_duration   The duration attribute of the VHML element
	 *@param  t_intensity  The intensity attribute of the VHML element
	 *@param  t_fromframe  The element will be rendered from this frame
	 *@param  t_toframe    The element will be rendered to this frame
	 *@param  t_repeat     The repeat attribute of the VHML element
	 *@param  t_which      The which attribute of the VHML element
	 */
	public VHMLFAElement(int t_id, int t_wait, int t_duration, int t_intensity, int t_fromframe, int t_toframe, int t_repeat, String t_which) {
		id = t_id;
		wait = t_wait;
		duration = t_duration;
		intensity = t_intensity;
		fromframe = t_fromframe;
		toframe = t_toframe;
		repeat = t_repeat;
		closedtag = false;
		which = t_which;
	}


	/**
	 *  Constructor for the VHMLFAElement object
	 *
	 *@param  t_id         The VHMLReader ID of the VHML element
	 *@param  t_wait       The wait attribute of the VHML element
	 *@param  t_duration   The duration attribute of the VHML element
	 *@param  t_intensity  The intensity attribute of the VHML element
	 *@param  t_fromframe  The element will be rendered from this frame
	 *@param  t_toframe    The element will be rendered to this frame
	 *@param  t_which      The which attribute of the VHML element
	 */
	public VHMLFAElement(int t_id, int t_wait, int t_duration, int t_intensity, int t_fromframe, int t_toframe, String t_which) {
		id = t_id;
		wait = t_wait;
		duration = t_duration;
		intensity = t_intensity;
		fromframe = t_fromframe;
		toframe = t_toframe;
		repeat = 1;
		closedtag = false;
		which = t_which;
	}


	/**
	 *  Gets whether the end tag has been encountered for the VHML element
	 *
	 *@return    Whether the end tag has been encountered
	 */
	public boolean getIsClosedTag() {
		return closedtag;
	}


	/**
	 *  Sets whether the end tag has been encountered for the VHML element
	 *
	 *@param  ct  Whether the tag has been closed
	 */
	public void setClosedTag(boolean ct) {
		closedtag = ct;
	}


	/**
	 *  Gets whether the duration has been set for the VHML element
	 *
	 *@return    Whether the duration has been set
	 */
	public boolean isDurationSet() {
		if (duration < 0) {
			return (false);
		}
		return (true);
	}


	/**
	 *  Sets the toFrame attribute of the VHML element
	 *
	 *@param  t_toframe  The element will be rendered to this frame
	 */
	public void setToFrame(int t_toframe) {
		toframe = t_toframe;
		duration = (toframe - fromframe);
		if (wait > 0) {
			duration = duration - wait;
		}
	}


	/**
	 *  Gets the VHMLReader id attribute of the VHML element
	 *
	 *@return    The id value
	 */
	public int getId() {
		return id;
	}


	/**
	 *  Gets the wait attribute of the VHML element
	 *
	 *@return    The wait value
	 */
	public int getWait() {
		return wait;
	}


	/**
	 *  Gets the duration attribute of the VHML element
	 *
	 *@return    The duration value
	 */
	public int getDuration() {
		return duration;
	}


	/**
	 *  Gets the intensity attribute of the VHML element
	 *
	 *@return    The intensity value
	 */
	public int getIntensity() {
		return intensity;
	}


	/**
	 *  Gets the toFrame attribute of the VHML element
	 *
	 *@return    The toFrame value
	 */
	public int getToFrame() {
		return toframe;
	}


	/**
	 *  Gets the fromFrame attribute of the VHML element
	 *
	 *@return    The fromFrame value
	 */
	public int getFromFrame() {
		return fromframe;
	}


	/**
	 *  Gets the repeat attribute of the VHML element
	 *
	 *@return    The repeat value
	 */
	public int getRepeat() {
		return repeat;
	}


	/**
	 *  Gets the which attribute of the VHML element
	 *
	 *@return    The which value
	 */
	public String getWhich() {
		return which;
	}
}

