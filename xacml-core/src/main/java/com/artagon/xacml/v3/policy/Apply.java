package com.artagon.xacml.v3.policy;

import com.artagon.xacml.v3.EvaluationContext;
import com.artagon.xacml.v3.EvaluationException;
import com.artagon.xacml.v3.Expression;

public interface Apply extends PolicyElement, Expression
{
	/**
	 * Gets XACML function identifier
	 * 
	 * @return XACML function identifier
	 */
	String getFunctionId();

	/**
	 * Evaluates given expression by invoking function
	 * with a given parameters
	 * 
	 * @param context an evaluation context
	 * @return expression evaluation result as {@link Value}
	 * instance
	 */
	Value evaluate(EvaluationContext context)
			throws EvaluationException;

}