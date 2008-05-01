package metaface.interaction;

import java.util.*;
import java.io.*;
/**
 *  This class provides evaluate actions within a DMTL knowledge base
 *
 *@author     Simon Beard
 *@version    1.0
 *@see        InteractionManager
 */
public class Evaluate {
	/**
	 *  The variable attribute of the Evaluate element
	 */	
	private String variable;
	/**
	 *  The operator attribute of the Evaluate element
	 */		
	private String operator;
	/**
	 *  The value attribute of the Evaluate element
	 */		
	private String value;


	/**
	 *  Constructor for the Evaluate object
	 *
	 *@param  var  The DMTL variable component
	 *@param  opp  The DMTL operator component
	 *@param  val  The DMTL value component
	 */
	public Evaluate(String var, String opp, String val) {
		variable = var;
		operator = opp;
		value = val;
	}


	/**
	 *  This method is called when a state is processed while performing a match,
	 *  and this evaluate element is encountered
	 *
	 *@return    Whether the evaluation resulted in true or false
	 */
	public boolean execute() {
		//By default do nothing (designed to be overridden)
		return (true);
	}


	/**
	 *  Gets the variable attribute of the Evaluate element
	 *
	 *@return    The variable
	 */
	public String getVariable() {
		return (variable);
	}


	/**
	 *  Gets the operator attribute of the Evaluate element
	 *
	 *@return    The operator
	 */
	public String getOperator() {
		return (operator);
	}


	/**
	 *  Gets the value attribute of the Evaluate element
	 *
	 *@return    The value
	 */
	public String getValue() {
		return (value);
	}


	/**
	 *  Any buiding instructions that must be completed after the constructor is
	 *  called
	 */
	public void build() {
	}


	/**
	 *  Resets any internal state variables
	 */
	public void reset() {
	}
}
