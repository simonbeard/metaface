package metaface.mpeg4;
/**
 *  The viseme data that makes up FAPData
 *
 *@author     Simon Beard
 *@version    1.0
 */
class VisemeData extends FAPMembers {

	/**
	 *  MPEG-4 viseme1
	 */
	public int viseme1;
	/**
	 *  MPEG-4 viseme2
	 */
	public int viseme2;
	/**
	 *  MPEG-4 viseme blend
	 */
	public int blend;
	/**
	 *  MPEG-4 viseme definition
	 */
	public int defBit;


	/**
	 *  Contructs viseme data
	 */
	public VisemeData() {
		super();
	}
}

