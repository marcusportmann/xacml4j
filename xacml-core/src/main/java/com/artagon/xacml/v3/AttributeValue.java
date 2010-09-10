package com.artagon.xacml.v3;


public interface AttributeValue extends ValueExpression
{
	/**
	 * Converts this XACML attribute value
	 * to {@link String}
	 * 
	 * @return this attribute value as {@link String}
	 */
	String toXacmlString();
	
	/**
	 * Gets attribute value XACML data type
	 * 
	 * @return {@link AttributeValueType} instance
	 */
	AttributeValueType getDataType();
	
	/**
	 * Implementation of this method 
	 * returns reference to itself
	 * 
	 * @see {@link Expression#evaluate(EvaluationContext)}
	 */
	AttributeValue evaluate(EvaluationContext context) throws EvaluationException;
}