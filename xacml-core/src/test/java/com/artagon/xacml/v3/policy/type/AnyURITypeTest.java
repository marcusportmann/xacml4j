package com.artagon.xacml.v3.policy.type;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.artagon.xacml.v3.AttributeValue;
import com.artagon.xacml.v3.types.AnyURIType;
import com.artagon.xacml.v3.types.XacmlDataTypes;

public class AnyURITypeTest 
{
	private AnyURIType t1;
	
	@Before
	public void init(){
		this.t1 = XacmlDataTypes.ANYURI.getType();
	}
	
	@Test
	public void testEquals()
	{
		AttributeValue v0 = t1.create("http://www.test.org");
		AttributeValue v1 = t1.create("http://www.test.org");
		assertEquals(v0, v1);
	}
}
