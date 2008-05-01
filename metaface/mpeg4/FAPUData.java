package metaface.mpeg4;
/**
 *  <p>
 *
 *  This class holds the FAPU values relevant to an MPEG-4 face.</p> <p>
 *
 *  By normalising FAP values the defined feature points can be used on and
 *  extracted from any synthetic or real face. Normalisation is achieved through
 *  use of Facial Animation Parameter Units (FAPU). A FAPU is the distance
 *  between key facial points, such as the distance between the tip of the nose
 *  and the middle of the mouth, the distance between the eyes, etc. The value
 *  of a FAP is expressed in terms of a fraction of a FAPU. In this way, the
 *  movement described by a FAP is adapted to the actual size and shape of
 *  different models.<p>
 *
 *  <IMG SRC="../../resources/fapu.jpg"/>
 *
 *@author     Simon Beard 
 *@version    1.0
 */
public class FAPUData extends java.lang.Object {

	/**
	 *  MPEG-4 FAPU value
	 */
	private float mw;
	/**
	 *  MPEG-4 FAPU value
	 */
	private float mns;
	/**
	 *  MPEG-4 FAPU value
	 */
	private float ens;
	/**
	 *  MPEG-4 FAPU value
	 */
	private float es;
	/**
	 *  MPEG-4 FAPU value
	 */
	private float irisd;
	/**
	 *  MPEG-4 FAPU value
	 */
	private float au;


	/**
	 *  Creates new FAPUData based on input
	 *
	 *@param  a  Mw
	 *@param  b  Mns
	 *@param  c  Ens
	 *@param  d  Es
	 *@param  e  IrisD
	 *@param  f  Au
	 */
	public FAPUData(float a, float b, float c, float d, float e, float f) {
		mw = a / 1024f;
		mns = b / 1024f;
		ens = c / 1024f;
		es = d / 1024f;
		irisd = e / 1024f;
		au = f;
	}


	/**
	 *  Gets the MPEG-4 FAPU value Mw
	 *
	 *@return    FAPU Mw
	 */
	public float Mw() {
		return (mw);
	}


	/**
	 *  Gets the MPEG-4 FAPU value Mns
	 *
	 *@return    FAPU Mns
	 */
	public float Mns() {
		return (mns);
	}


	/**
	 *  Gets the MPEG-4 FAPU value Ens
	 *
	 *@return    FAPU Ens
	 */
	public float Ens() {
		return (ens);
	}


	/**
	 *  Gets the MPEG-4 FAPU value Es
	 *
	 *@return    FAPU Es
	 */
	public float Es() {
		return (es);
	}


	/**
	 *  Gets the MPEG-4 FAPU value IrisD
	 *
	 *@return    FAPU IrisD
	 */
	public float IrisD() {
		return (irisd);
	}


	/**
	 *  Gets the MPEG-4 FAPU value Au
	 *
	 *@return    FAPU Au
	 */
	public float Au() {
		return (au);
	}


	/**
	 *  Sets the MPEG-4 FAPU value IrisD. Not strictly MPEG-4, but allows for
	 *  closing the eyes in disgust. It will also affect the pupils.
	 *
	 *@param  tempIrisD  The new IrisD value
	 */
	public void setIrisD(float tempIrisD) {
		irisd = tempIrisD;
	}
}

