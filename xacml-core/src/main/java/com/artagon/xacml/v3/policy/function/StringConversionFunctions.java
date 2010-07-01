package com.artagon.xacml.v3.policy.function;

import com.artagon.xacml.v3.spi.function.XacmlFunc;
import com.artagon.xacml.v3.spi.function.XacmlFuncReturnType;
import com.artagon.xacml.v3.spi.function.XacmlFunctionProvider;
import com.artagon.xacml.v3.spi.function.XacmlParam;
import com.artagon.xacml.v3.types.XacmlDataTypes;
import com.artagon.xacml.v3.types.StringType.StringValue;

@XacmlFunctionProvider(description="XACML string conversion functions")
public class StringConversionFunctions 
{
	@XacmlFunc(id="urn:oasis:names:tc:xacml:1.0:function:string-normalize-space")
	@XacmlFuncReturnType(type=XacmlDataTypes.STRING)
	public static StringValue normalizeSpace(
			@XacmlParam(type=XacmlDataTypes.STRING)StringValue v)
	{
		return XacmlDataTypes.STRING.create(v.getValue().trim());
	}
	
	@XacmlFunc(id="urn:oasis:names:tc:xacml:1.0:function:string-normalize-to-lower-case")
	@XacmlFuncReturnType(type=XacmlDataTypes.STRING)
	public static StringValue normalizeToLowerCase(
			@XacmlParam(type=XacmlDataTypes.STRING)StringValue v)
	{
		return XacmlDataTypes.STRING.create(v.getValue().toLowerCase());
	}
	
	@XacmlFunc(id="urn:artagon:names:tc:xacml:1.0:function:string-normalize-to-upper-case")
	@XacmlFuncReturnType(type=XacmlDataTypes.STRING)
	public static StringValue normalizeToUpperCase(
			@XacmlParam(type=XacmlDataTypes.STRING)StringValue v)
	{
		return XacmlDataTypes.STRING.create(v.getValue().toUpperCase());
	}
}
