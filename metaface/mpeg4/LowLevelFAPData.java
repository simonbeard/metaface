package metaface.mpeg4;
/**
 *  The low-level data that makes up FAPData
 *
 *@author     Simon Beard
 *@version    1.0
 */
class LowLevelFAPData extends FAPMembers {

	/**
	 *  MPEG-4 low-level value
	 */
	public int value;
	/**
	 *  MPEG-4 previous low-level value
	 */
	public int previousValue;


	/**
	 *  Constructs low-level data
	 */
	public LowLevelFAPData() {
		super();
	}
}

