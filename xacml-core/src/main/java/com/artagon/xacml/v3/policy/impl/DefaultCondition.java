package com.artagon.xacml.v3.policy.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artagon.xacml.util.Preconditions;
import com.artagon.xacml.v3.XacmlObject;
import com.artagon.xacml.v3.policy.Condition;
import com.artagon.xacml.v3.policy.ConditionResult;
import com.artagon.xacml.v3.policy.EvaluationContext;
import com.artagon.xacml.v3.policy.EvaluationException;
import com.artagon.xacml.v3.policy.Expression;
import com.artagon.xacml.v3.policy.PolicyVisitor;
import com.artagon.xacml.v3.policy.Rule;
import com.artagon.xacml.v3.policy.type.DataTypes;
import com.artagon.xacml.v3.policy.type.BooleanType.BooleanValue;

/**
 * Condition represents a Boolean expression that refines the applicability 
 * of the rule beyond the predicates implied by its target. 
 * Therefore, it may be absent in the {@link Rule}
 * 
 * @author Giedrius Trumpickas
 */
final class DefaultCondition extends XacmlObject implements Condition
{
	private final static Logger log = LoggerFactory.getLogger(DefaultCondition.class);
	
	private Expression predicate;

	/**
	 * Constructs condition with an predicate
	 * expression
	 * 
	 * @param predicate an expression which always evaluates
	 * to {@link BooleanValue}
	 */
	public DefaultCondition(Expression predicate){
		Preconditions.checkNotNull(predicate);
		Preconditions.checkArgument(predicate.getEvaluatesTo().equals(DataTypes.BOOLEAN.getType()), 
				String.format(
						"Condition expects boolean predicate, " +
						"but got expression which evaluates to=\"%s\"",
						predicate.getEvaluatesTo()));
		this.predicate = predicate;
	}
	
	@Override
	public ConditionResult evaluate(EvaluationContext context) 
	{
		try
		{
			BooleanValue result = (BooleanValue)predicate.evaluate(context);
			if(log.isDebugEnabled()){
				log.debug("Condition predicate evaluation result=\"{}\"", result);
			}
			return result.getValue()?ConditionResult.TRUE:ConditionResult.FALSE;
		}catch(EvaluationException e){
			return ConditionResult.INDETERMINATE;
		}
	}

	@Override
	public void accept(PolicyVisitor v) {
		v.visitEnter(this);
		if(predicate != null){
			predicate.accept(v);
		}
		v.visitLeave(this);
	}
}
