package com.artagon.xacml.v30.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.artagon.xacml.v30.BagOfAttributesExp;

public class BooleanTypeTest 
{
	
	private BooleanType t1;
	
	@Before
	public void init(){
		this.t1 = BooleanType.BOOLEAN;
	}
		
	@Test
	public void testCreate()
	{
		Object o = Boolean.FALSE;
		BooleanValueExp a = t1.create(o);
		assertFalse(a.getValue());
		o = "true";
		BooleanValueExp a1 = t1.create(o);
		assertTrue(a1.getValue());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFromAnyObjectWrongContentType()
	{
		Long a = 1l;
		assertFalse(t1.create(a).getValue());
	}

	@Test
	public void fromToXacmlString()
	{
		BooleanValueExp v = t1.fromXacmlString("True");
		assertEquals(Boolean.TRUE, v.getValue());
		v = t1.fromXacmlString("TRUE");
		assertEquals(Boolean.TRUE, v.getValue());
		v = t1.fromXacmlString("FALSE");
		assertEquals(Boolean.FALSE, v.getValue());
		v = t1.fromXacmlString("False");
		assertEquals(Boolean.FALSE, v.getValue());
	}
	
	@Test
	public void toXacmlString()
	{
		BooleanValueExp v1 = t1.create(Boolean.TRUE);
		BooleanValueExp v2 = t1.create(Boolean.FALSE);
		assertEquals("true", v1.toXacmlString());
		assertEquals("false", v2.toXacmlString());
	}
	
	@Test
	public void testEquals()
	{
		BooleanValueExp v1 = t1.create(Boolean.TRUE);
		BooleanValueExp v2 = t1.create(Boolean.FALSE);
		BooleanValueExp v3 = t1.create(Boolean.TRUE);
		BooleanValueExp v4 = t1.create(Boolean.FALSE);
		assertEquals(v1, v3);
		assertEquals(v2, v4);
	}
	
	@Test
	public void testBagOf()
	{
		BagOfAttributesExp b1 = t1.bagOf("true", "false");
		BagOfAttributesExp b2 = t1.bagOf(true, false);
		assertEquals(2, b1.size());
		assertEquals(b1, b2);
		assertTrue(b1.contains(t1.create(true)));
		assertTrue(b1.contains(t1.create(false)));
		
	}
}
