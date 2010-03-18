package com.artagon.xacml.v3.policy;


import java.util.List;

import com.artagon.xacml.v3.Decision;
import com.artagon.xacml.v3.EvaluationContext;

public interface DecisionCombiningAlgorithm <D extends DecisionRule> extends PolicyElement
{
	/**
	 * Gets algorithm identifier
	 * 
	 * @return algorithm identifier
	 */
	String getId();
	
	/**
	 * Combines multiple decisions to one {@link Decision} result
	 * 
	 * @param decisions a multiple decisions
	 * @param context an evaluation context
	 * @return {@link Decision} context
	 */
	Decision combine(List<D> decisions, EvaluationContext context);
}
