package metaface.mpeg4;
/**
 *  The expression data that makes up FAPData
 *
 *@author     Simon Beard 
 *@version    1.0
 */
class ExpressionData extends FAPMembers {

	/**
	 *  MPEG-4 expression1
	 */
	public int expression1;
	/**
	 *  MPEG-4 expression intensity1
	 */
	public int expression2;
	/**
	 *  MPEG-4 expression2
	 */
	public int intensity1;
	/**
	 *  MPEG-4 expression intensity 2
	 */
	public int intensity2;
	/**
	 *  MPEG-4 expression initialize
	 */
	public int initBit;
	/**
	 *  MPEG-4 expression definition
	 */
	public int defBit;


	/**
	 *  Constructs expression data
	 */
	public ExpressionData() {
		super();
	}
}

