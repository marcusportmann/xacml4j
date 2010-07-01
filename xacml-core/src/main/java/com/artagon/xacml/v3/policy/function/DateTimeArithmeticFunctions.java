package com.artagon.xacml.v3.policy.function;

import com.artagon.xacml.v3.spi.function.XacmlFunc;
import com.artagon.xacml.v3.spi.function.XacmlFuncReturnType;
import com.artagon.xacml.v3.spi.function.XacmlFunctionProvider;
import com.artagon.xacml.v3.spi.function.XacmlLegacyFunc;
import com.artagon.xacml.v3.spi.function.XacmlParam;
import com.artagon.xacml.v3.types.XacmlDataTypes;
import com.artagon.xacml.v3.types.DateTimeType.DateTimeValue;
import com.artagon.xacml.v3.types.DateType.DateValue;
import com.artagon.xacml.v3.types.DayTimeDurationType.DayTimeDurationValue;
import com.artagon.xacml.v3.types.YearMonthDurationType.YearMonthDurationValue;

@XacmlFunctionProvider(description="XACML date time arithmetic functions")
public class DateTimeArithmeticFunctions 
{
	@XacmlFunc(id="urn:oasis:names:tc:xacml:3.0:function:dateTime-add-dayTimeDuration")
	@XacmlLegacyFunc(id="urn:oasis:names:tc:xacml:1.0:function:dateTime-add-dayTimeDuration")
	@XacmlFuncReturnType(type=XacmlDataTypes.DATETIME)
	public static DateTimeValue add(
			@XacmlParam(type=XacmlDataTypes.DATETIME)DateTimeValue a, 
			@XacmlParam(type=XacmlDataTypes.DAYTIMEDURATION)DayTimeDurationValue b)
	{
		return a.add(b);
	}
	
	@XacmlFunc(id="urn:oasis:names:tc:xacml:3.0:function:dateTime-subtract-dayTimeDuration")
	@XacmlLegacyFunc(id="urn:oasis:names:tc:xacml:1.0:function:dateTime-subtract-dayTimeDuration")
	@XacmlFuncReturnType(type=XacmlDataTypes.DATETIME)
	public static DateTimeValue subtract(
			@XacmlParam(type=XacmlDataTypes.DATETIME)DateTimeValue a, 
			@XacmlParam(type=XacmlDataTypes.DAYTIMEDURATION)DayTimeDurationValue b)
	{
		return a.subtract(b);
	}
	
	@XacmlFunc(id="urn:oasis:names:tc:xacml:3.0:function:dateTime-add-yearMonthDuration")
	@XacmlLegacyFunc(id="urn:oasis:names:tc:xacml:1.0:function:dateTime-add-yearMonthDuration")
	@XacmlFuncReturnType(type=XacmlDataTypes.DATETIME)
	public static DateTimeValue add(
			@XacmlParam(type=XacmlDataTypes.DATETIME)DateTimeValue a, 
			@XacmlParam(type=XacmlDataTypes.YEARMONTHDURATION)YearMonthDurationValue b)
	{
		return a.add(b);
	}
	
	@XacmlFunc(id="urn:oasis:names:tc:xacml:3.0:function:dateTime-subtract-yearMonthDuration")
	@XacmlLegacyFunc(id="urn:oasis:names:tc:xacml:1.0:function:dateTime-subtract-yearMonthDuration")
	@XacmlFuncReturnType(type=XacmlDataTypes.DATETIME)
	public static DateTimeValue subtract(
			@XacmlParam(type=XacmlDataTypes.DATETIME)DateTimeValue a, 
			@XacmlParam(type=XacmlDataTypes.YEARMONTHDURATION)YearMonthDurationValue b)
	{
		return a.subtract(b);
	}
	
	@XacmlFunc(id="urn:oasis:names:tc:xacml:3.0:function:date-add-yearMonthDuration")
	@XacmlLegacyFunc(id="urn:oasis:names:tc:xacml:1.0:function:date-add-yearMonthDuration")
	@XacmlFuncReturnType(type=XacmlDataTypes.DATE)
	public static DateValue add(
			@XacmlParam(type=XacmlDataTypes.DATE)DateValue a, 
			@XacmlParam(type=XacmlDataTypes.YEARMONTHDURATION)YearMonthDurationValue b)
	{
		return a.add(b);
	}
	
	@XacmlFunc(id="urn:oasis:names:tc:xacml:3.0:function:date-subtract-yearMonthDuration")
	@XacmlLegacyFunc(id="urn:oasis:names:tc:xacml:1.0:function:date-subtract-yearMonthDuration")
	@XacmlFuncReturnType(type=XacmlDataTypes.DATE)
	public static DateValue subtract(
			@XacmlParam(type=XacmlDataTypes.DATE)DateValue a, 
			@XacmlParam(type=XacmlDataTypes.YEARMONTHDURATION)YearMonthDurationValue b)
	{
		return a.subtract(b);
	}
}
