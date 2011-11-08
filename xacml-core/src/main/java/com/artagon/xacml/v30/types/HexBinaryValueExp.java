package com.artagon.xacml.v30.types;

import com.artagon.xacml.v30.core.BinaryValue;

public final class HexBinaryValueExp extends 
	BaseAttributeExpression<BinaryValue>
{
	private static final long serialVersionUID = 8087916652227967791L;

	HexBinaryValueExp(HexBinaryType type, BinaryValue value) {
		super(type, value);
	}
	
	@Override
	public String toXacmlString() {
		return getValue().toHexEncoded();
	}
}

