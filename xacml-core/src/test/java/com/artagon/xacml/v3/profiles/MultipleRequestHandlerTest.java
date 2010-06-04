package com.artagon.xacml.v3.profiles;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import com.artagon.xacml.v3.AttributeCategoryId;
import com.artagon.xacml.v3.context.Attribute;
import com.artagon.xacml.v3.context.Attributes;
import com.artagon.xacml.v3.context.ContextFactory;
import com.artagon.xacml.v3.context.ContextSyntaxException;
import com.artagon.xacml.v3.context.DefaultRequestFactory;
import com.artagon.xacml.v3.context.Request;
import com.artagon.xacml.v3.context.RequestReference;
import com.artagon.xacml.v3.context.Result;
import com.artagon.xacml.v3.context.Status;
import com.artagon.xacml.v3.context.StatusCode;
import com.artagon.xacml.v3.pdp.PolicyDecisionCallback;
import com.artagon.xacml.v3.policy.AttributesReference;
import com.artagon.xacml.v3.types.XacmlDataTypes;
import com.google.common.collect.Iterables;

public class MultipleRequestHandlerTest 
{
	private PolicyDecisionCallback pdp;
	private RequestProfileHandler profile;
	private ContextFactory contextFactory;
	
	@Before
	public void init()
	{
		this.contextFactory = new DefaultRequestFactory();
		this.pdp = createStrictMock(PolicyDecisionCallback.class);
		this.profile = new MultipleRequestsHandler(new DefaultRequestFactory());
	}
	
	@Test
	public void testResolveRequestsWithValidReferences() throws ContextSyntaxException
	{
		Collection<Attribute> attributes0 = new LinkedList<Attribute>();
		attributes0.add(new Attribute("testId1", XacmlDataTypes.STRING.create("value0")));
		attributes0.add(new Attribute("testId2", XacmlDataTypes.STRING.create("value1")));
		Attributes attr0 = new Attributes("resourceAttr0",  AttributeCategoryId.RESOURCE, attributes0);
		
		Collection<Attribute> attributes1 = new LinkedList<Attribute>();
		attributes1.add(new Attribute("testId3", XacmlDataTypes.STRING.create("value0")));
		attributes1.add(new Attribute("testId4", XacmlDataTypes.STRING.create("value1")));
		Attributes attr1 = new Attributes("resourceAttr1",  AttributeCategoryId.RESOURCE, attributes1);
		
		Collection<Attribute> attributes2 = new LinkedList<Attribute>();
		attributes2.add(new Attribute("testId5", XacmlDataTypes.STRING.create("value0")));
		attributes2.add(new Attribute("testId6", XacmlDataTypes.STRING.create("value1")));
		Attributes attr2 = new Attributes("subjectAttr0",  AttributeCategoryId.SUBJECT_ACCESS, attributes2);
		
		Collection<Attribute> attributes3 = new LinkedList<Attribute>();
		attributes3.add(new Attribute("testId7", XacmlDataTypes.STRING.create("value0")));
		attributes3.add(new Attribute("testId8", XacmlDataTypes.STRING.create("value1")));
		Attributes attr3 = new Attributes("subjectAttr1",  AttributeCategoryId.SUBJECT_ACCESS, attributes3);
		
		
		Collection<AttributesReference> ref0 = new LinkedList<AttributesReference>();
		ref0.add(contextFactory.createAttributesReference("resourceAttr0"));
		ref0.add(contextFactory.createAttributesReference("subjectAttr0"));	
		RequestReference reference0 = contextFactory.createRequestReference(ref0);
		
		Collection<AttributesReference> ref1 = new LinkedList<AttributesReference>();
		ref1.add(contextFactory.createAttributesReference("resourceAttr1"));
		ref1.add(contextFactory.createAttributesReference("subjectAttr1"));	
		RequestReference reference1 = contextFactory.createRequestReference(ref1);
		
			
		Request context = new Request(false, 
				Arrays.asList(attr0, attr1, attr2, attr3), 
				Arrays.asList(reference0, reference1));
		
		Capture<Request> c0 = new Capture<Request>();
		Capture<Request> c1 = new Capture<Request>();
		
		expect(pdp.requestDecision(capture(c0))).andReturn(
				new Result(new Status(StatusCode.createProcessingError())));
		expect(pdp.requestDecision(capture(c1))).andReturn(
				new Result(new Status(StatusCode.createProcessingError())));
		replay(pdp);
		profile.handle(context, pdp).iterator();
		Request context0 = c0.getValue();
		Request context1 = c0.getValue();
		assertNotNull(Iterables.getOnlyElement(context0.getAttributes(AttributeCategoryId.SUBJECT_ACCESS)).getAttributes("testId5"));
		assertNotNull(Iterables.getOnlyElement(context0.getAttributes(AttributeCategoryId.SUBJECT_ACCESS)).getAttributes("testId6"));
		assertNotNull(Iterables.getOnlyElement(context0.getAttributes(AttributeCategoryId.RESOURCE)).getAttributes("testId1"));
		assertNotNull(Iterables.getOnlyElement(context0.getAttributes(AttributeCategoryId.RESOURCE)).getAttributes("testId2"));
		assertEquals(2, context0.getAttributes().size());
		assertEquals(1, context0.getAttributes(AttributeCategoryId.SUBJECT_ACCESS).size());
		assertEquals(1, context0.getAttributes(AttributeCategoryId.RESOURCE).size());
		
		assertNotNull(Iterables.getOnlyElement(context1.getAttributes(AttributeCategoryId.SUBJECT_ACCESS)).getAttributes("testId7"));
		assertNotNull(Iterables.getOnlyElement(context1.getAttributes(AttributeCategoryId.SUBJECT_ACCESS)).getAttributes("testId8"));
		assertNotNull(Iterables.getOnlyElement(context1.getAttributes(AttributeCategoryId.RESOURCE)).getAttributes("testId3"));
		assertNotNull(Iterables.getOnlyElement(context1.getAttributes(AttributeCategoryId.RESOURCE)).getAttributes("testId4"));
		assertEquals(2, context1.getAttributes().size());
		assertEquals(1, context1.getAttributes(AttributeCategoryId.SUBJECT_ACCESS).size());
		assertEquals(1, context1.getAttributes(AttributeCategoryId.RESOURCE).size());
		verify(pdp);
	}
	
	@Test
	public void testWithNoReferences()
	{
		Collection<Attribute> attributes0 = new LinkedList<Attribute>();
		attributes0.add(new Attribute("testId3", XacmlDataTypes.STRING.create("value0")));
		attributes0.add(new Attribute("testId4", XacmlDataTypes.STRING.create("value1")));
		Attributes attr0 = new Attributes("resourceAttr1",  AttributeCategoryId.RESOURCE, attributes0);
		
		Collection<Attribute> attributes1 = new LinkedList<Attribute>();
		attributes1.add(new Attribute("testId5", XacmlDataTypes.STRING.create("value0")));
		attributes1.add(new Attribute("testId6", XacmlDataTypes.STRING.create("value1")));
		Attributes attr1 = new Attributes("subjectAttr0",  AttributeCategoryId.SUBJECT_ACCESS, attributes1);
		
		Request request = new Request(false, 
				Arrays.asList(attr0, attr1));
		
		expect(pdp.requestDecision(request)).andReturn(
				new Result(new Status(StatusCode.createProcessingError())));
		replay(pdp);
		Collection<Result> results = profile.handle(request, pdp);
		assertEquals(new Result(new Status(StatusCode.createProcessingError())), results.iterator().next());
		verify(pdp);
	}
}
