package com.artagon.xacml.v3.impl;

import static org.easymock.EasyMock.createStrictMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;

import com.artagon.xacml.v3.AttributeCategoryId;
import com.artagon.xacml.v3.context.Attribute;
import com.artagon.xacml.v3.context.Attributes;
import com.artagon.xacml.v3.types.XacmlDataTypes;

public class DefaultAttributesTest 
{
	private Node content;
	
	@Before
	public void init(){
		this.content = createStrictMock(Node.class);
	}
	
	@Test
	public void testCreate()
	{
		Collection<Attribute> attr = new LinkedList<Attribute>();
		attr.add(new Attribute("testId10", XacmlDataTypes.STRING.create("value0")));
		attr.add(new Attribute("testId11", XacmlDataTypes.STRING.create("value1")));
		attr.add(new Attribute("testId11", "testIssuer", true, XacmlDataTypes.STRING.create("value1")));
		Attributes test = new Attributes("id", AttributeCategoryId.RESOURCE,  content, attr);
		assertTrue(attr.containsAll(test.getAttributes()));
		assertTrue(test.getAttributes().containsAll(attr));
		assertSame(content, test.getContent());
		assertEquals(AttributeCategoryId.RESOURCE, test.getCategoryId());
	}
}
