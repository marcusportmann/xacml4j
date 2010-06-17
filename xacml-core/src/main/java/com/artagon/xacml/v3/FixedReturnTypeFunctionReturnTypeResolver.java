package com.artagon.xacml.v3;

import com.google.common.base.Preconditions;

public final class FixedReturnTypeFunctionReturnTypeResolver implements FunctionReturnTypeResolver
{
	private ValueType returnType;
	
	public FixedReturnTypeFunctionReturnTypeResolver(ValueType returnType){
		Preconditions.checkNotNull(returnType);
		this.returnType = returnType;
	}
	
	@Override
	public ValueType resolve(FunctionSpec spec, 
			Expression... arguments) {
		return returnType;
	}
}
