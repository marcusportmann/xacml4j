package com.artagon.xacml.v3;

import java.util.ListIterator;


final class ParamAnyAttributeSpec implements ParamSpec
{
	@Override
	public boolean isValidParamType(ValueType type) {
		return (type instanceof AttributeValueType);
	}

	@Override
	public boolean isVariadic() {
		return false;
	}

	@Override
	public boolean validate(ListIterator<Expression> it) {
		if(!it.hasNext()){
			return false;
		}
		Expression exp = it.next();
		return isValidParamType(exp.getEvaluatesTo());
	}
	
}
