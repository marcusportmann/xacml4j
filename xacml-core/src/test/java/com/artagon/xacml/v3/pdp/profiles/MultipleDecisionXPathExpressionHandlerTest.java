package com.artagon.xacml.v3.pdp.profiles;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.artagon.xacml.v3.Attribute;
import com.artagon.xacml.v3.AttributeCategoryId;
import com.artagon.xacml.v3.Attributes;
import com.artagon.xacml.v3.Decision;
import com.artagon.xacml.v3.RequestContext;
import com.artagon.xacml.v3.Result;
import com.artagon.xacml.v3.Status;
import com.artagon.xacml.v3.StatusCode;
import com.artagon.xacml.v3.pdp.PolicyDecisionCallback;
import com.artagon.xacml.v3.pdp.RequestProfileHandler;
import com.artagon.xacml.v3.spi.xpath.DefaultXPathProvider;
import com.artagon.xacml.v3.types.XacmlDataTypes;
import com.google.common.collect.Iterables;

public class MultipleDecisionXPathExpressionHandlerTest 
{
	
	private String testXml = "<md:record xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
	"xmlns:md=\"urn:example:med:schemas:record\">" +
	"<md:patient>" +
	"<md:patientDoB md:attrn1=\"test\" attrn2=\"v\" >1992-03-21</md:patientDoB>" +
	"<md:patient-number>555555</md:patient-number>" +
	"</md:patient>" +
	"<md:patient>" +
	"<md:patientDoB md:attrn1=\"test1\" attrn2=\"v1\">1991-01-11</md:patientDoB>" +
	"<md:patient-number>11111</md:patient-number>" +
	"</md:patient>" +
	"</md:record>";
	
	private PolicyDecisionCallback pdp;
	private RequestProfileHandler profile;
	private Node content;
	
	@Before
	public void init() throws Exception
	{
		this.pdp = createStrictMock(PolicyDecisionCallback.class);
		this.profile = new MultipleDecisionXPathExpressionHandler(new DefaultXPathProvider());
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		f.setNamespaceAware(true);
		DocumentBuilder builder = f.newDocumentBuilder();
		this.content = builder.parse(new InputSource(new StringReader(testXml)));
	}
	
	@Test
	public void testMultipleDecisionProfileWithSelectorInSingleCategory()
	{
		
		Collection<Attribute> resourceAttrs = new LinkedList<Attribute>();
		resourceAttrs.add(new Attribute("testId3", XacmlDataTypes.STRING.create("value0")));
		resourceAttrs.add(new Attribute("testId4", XacmlDataTypes.STRING.create("value1")));
		
		resourceAttrs.add(new Attribute(MultipleDecisionXPathExpressionHandler.MULTIPLE_CONTENT_SELECTOR, 
				XacmlDataTypes.XPATHEXPRESSION.create("//md:record/md:patient", AttributeCategoryId.RESOURCE)));
		
		
		Attributes resource = new Attributes(AttributeCategoryId.RESOURCE, content, resourceAttrs);
		
		Collection<Attribute> subjectAttr = new LinkedList<Attribute>();
		subjectAttr.add(new Attribute("testId7", XacmlDataTypes.STRING.create("value0")));
		subjectAttr.add(new Attribute("testId8", XacmlDataTypes.STRING.create("value1")));
		Attributes subject =  new Attributes(AttributeCategoryId.SUBJECT_ACCESS, subjectAttr);
		
		RequestContext context = new RequestContext(false, 
				Arrays.asList(subject, resource));
		
		assertFalse(context.hasRepeatingCategories());
		Capture<RequestContext> c0 = new Capture<RequestContext>();
		Capture<RequestContext> c1 = new Capture<RequestContext>();
		
		expect(pdp.requestDecision(capture(c0))).andReturn(
				new Result(Decision.INDETERMINATE, 
						new Status(StatusCode.createProcessingError()),
						Collections.<Attributes>emptyList()));
		expect(pdp.requestDecision(capture(c1))).andReturn(
				new Result(Decision.INDETERMINATE, 
						new Status(StatusCode.createProcessingError()),
						Collections.<Attributes>emptyList()));
		replay(pdp);
			
		Collection<Result> results = profile.handle(context, pdp);
		Iterator<Result> it = results.iterator();
		assertEquals(new Status(StatusCode.createProcessingError()), it.next().getStatus());
		assertEquals(new Status(StatusCode.createProcessingError()), it.next().getStatus());
		RequestContext r0 = c0.getValue();
		RequestContext r1 = c1.getValue();
		
		Attribute selector0 = Iterables.getOnlyElement(r0.getOnlyAttributes(AttributeCategoryId.RESOURCE).getAttributes(MultipleDecisionXPathExpressionHandler.CONTENT_SELECTOR));
		Attribute selector1 = Iterables.getOnlyElement(r1.getOnlyAttributes(AttributeCategoryId.RESOURCE).getAttributes(MultipleDecisionXPathExpressionHandler.CONTENT_SELECTOR));
		
		assertEquals(XacmlDataTypes.XPATHEXPRESSION.create("//md:record[1]/md:patient[1]", AttributeCategoryId.RESOURCE),  Iterables.getOnlyElement(selector0.getValues()));
		assertEquals(XacmlDataTypes.XPATHEXPRESSION.create("//md:record[1]/md:patient[2]", AttributeCategoryId.RESOURCE),  Iterables.getOnlyElement(selector1.getValues()));
		
		verify(pdp);
	}
	
	@Test
	public void testMultipleDecisionProfileWithSelectorInMultipleCategories()
	{
		
		Collection<Attribute> resourceAttrs = new LinkedList<Attribute>();
		resourceAttrs.add(new Attribute("testId3", XacmlDataTypes.STRING.create("value0")));
		resourceAttrs.add(new Attribute("testId4", XacmlDataTypes.STRING.create("value1")));
		
		resourceAttrs.add(new Attribute(MultipleDecisionXPathExpressionHandler.MULTIPLE_CONTENT_SELECTOR, 
				XacmlDataTypes.XPATHEXPRESSION.create("//md:record/md:patient", AttributeCategoryId.RESOURCE)));
		
		
		Attributes resource = new Attributes(AttributeCategoryId.RESOURCE, content, resourceAttrs);
		
		Collection<Attribute> subjectAttr = new LinkedList<Attribute>();
		subjectAttr.add(new Attribute("testId7", XacmlDataTypes.STRING.create("value0")));
		subjectAttr.add(new Attribute("testId8", XacmlDataTypes.STRING.create("value1")));
		
		subjectAttr.add(new Attribute(MultipleDecisionXPathExpressionHandler.MULTIPLE_CONTENT_SELECTOR, 
				XacmlDataTypes.XPATHEXPRESSION.create("//md:record/md:patient/md:patientDoB/@md:attrn1", AttributeCategoryId.SUBJECT_ACCESS)));
		
		Attributes subject =  new Attributes(AttributeCategoryId.SUBJECT_ACCESS, content, subjectAttr);
		
		RequestContext context = new RequestContext(false, 
				Arrays.asList(subject, resource));
		
		assertFalse(context.hasRepeatingCategories());
		Capture<RequestContext> c0 = new Capture<RequestContext>();
		Capture<RequestContext> c1 = new Capture<RequestContext>();
		Capture<RequestContext> c2 = new Capture<RequestContext>();
		Capture<RequestContext> c3 = new Capture<RequestContext>();
		
		expect(pdp.requestDecision(capture(c0))).andReturn(
				new Result(Decision.INDETERMINATE, 
						new Status(StatusCode.createProcessingError()),
						Collections.<Attributes>emptyList()));
		expect(pdp.requestDecision(capture(c1))).andReturn(
				new Result(Decision.INDETERMINATE, 
						new Status(StatusCode.createProcessingError()),
						Collections.<Attributes>emptyList()));
		expect(pdp.requestDecision(capture(c2))).andReturn(
				new Result(Decision.INDETERMINATE, 
						new Status(StatusCode.createProcessingError()),
						Collections.<Attributes>emptyList()));
		expect(pdp.requestDecision(capture(c3))).andReturn(
				new Result(Decision.INDETERMINATE, 
						new Status(StatusCode.createProcessingError()),
						Collections.<Attributes>emptyList()));
		
		replay(pdp);
			
		Collection<Result> results = profile.handle(context, pdp);
		Iterator<Result> it = results.iterator();
		assertEquals(new Status(StatusCode.createProcessingError()), it.next().getStatus());
		assertEquals(new Status(StatusCode.createProcessingError()), it.next().getStatus());
		
		RequestContext r0 = c0.getValue();
		RequestContext r1 = c1.getValue();
		RequestContext r2 = c2.getValue();
		RequestContext r3 = c3.getValue();
		
		Attribute selector00 = Iterables.getOnlyElement(r0.getOnlyAttributes(AttributeCategoryId.RESOURCE).getAttributes(MultipleDecisionXPathExpressionHandler.CONTENT_SELECTOR));
		Attribute selector01 = Iterables.getOnlyElement(r0.getOnlyAttributes(AttributeCategoryId.SUBJECT_ACCESS).getAttributes(MultipleDecisionXPathExpressionHandler.CONTENT_SELECTOR));
		
		assertEquals(XacmlDataTypes.XPATHEXPRESSION.create("//md:record[1]/md:patient[1]", AttributeCategoryId.RESOURCE),  Iterables.getOnlyElement(selector00.getValues()));
		assertEquals(XacmlDataTypes.XPATHEXPRESSION.create("//md:record[1]/md:patient[1]/md:patientDoB[1]/@md:attrn1", AttributeCategoryId.SUBJECT_ACCESS),  Iterables.getOnlyElement(selector01.getValues()));
		
		Attribute selector10 = Iterables.getOnlyElement(r1.getOnlyAttributes(AttributeCategoryId.RESOURCE).getAttributes(MultipleDecisionXPathExpressionHandler.CONTENT_SELECTOR));
		Attribute selector11 = Iterables.getOnlyElement(r1.getOnlyAttributes(AttributeCategoryId.SUBJECT_ACCESS).getAttributes(MultipleDecisionXPathExpressionHandler.CONTENT_SELECTOR));
		
		assertEquals(XacmlDataTypes.XPATHEXPRESSION.create("//md:record[1]/md:patient[1]", AttributeCategoryId.RESOURCE),  Iterables.getOnlyElement(selector10.getValues()));
		assertEquals(XacmlDataTypes.XPATHEXPRESSION.create("//md:record[1]/md:patient[2]/md:patientDoB[1]/@md:attrn1", AttributeCategoryId.SUBJECT_ACCESS),  Iterables.getOnlyElement(selector11.getValues()));
		
		Attribute selector20 = Iterables.getOnlyElement(r2.getOnlyAttributes(AttributeCategoryId.RESOURCE).getAttributes(MultipleDecisionXPathExpressionHandler.CONTENT_SELECTOR));
		Attribute selector21 = Iterables.getOnlyElement(r2.getOnlyAttributes(AttributeCategoryId.SUBJECT_ACCESS).getAttributes(MultipleDecisionXPathExpressionHandler.CONTENT_SELECTOR));
		
		assertEquals(XacmlDataTypes.XPATHEXPRESSION.create("//md:record[1]/md:patient[2]", AttributeCategoryId.RESOURCE),  Iterables.getOnlyElement(selector20.getValues()));
		assertEquals(XacmlDataTypes.XPATHEXPRESSION.create("//md:record[1]/md:patient[1]/md:patientDoB[1]/@md:attrn1", AttributeCategoryId.SUBJECT_ACCESS),  Iterables.getOnlyElement(selector21.getValues()));
		
		
		Attribute selector30 = Iterables.getOnlyElement(r3.getOnlyAttributes(AttributeCategoryId.RESOURCE).getAttributes(MultipleDecisionXPathExpressionHandler.CONTENT_SELECTOR));
		Attribute selector31 = Iterables.getOnlyElement(r3.getOnlyAttributes(AttributeCategoryId.SUBJECT_ACCESS).getAttributes(MultipleDecisionXPathExpressionHandler.CONTENT_SELECTOR));
		
		assertEquals(XacmlDataTypes.XPATHEXPRESSION.create("//md:record[1]/md:patient[2]", AttributeCategoryId.RESOURCE),  Iterables.getOnlyElement(selector30.getValues()));
		assertEquals(XacmlDataTypes.XPATHEXPRESSION.create("//md:record[1]/md:patient[2]/md:patientDoB[1]/@md:attrn1", AttributeCategoryId.SUBJECT_ACCESS),  Iterables.getOnlyElement(selector31.getValues()));
		
		verify(pdp);
	}
	
	@Test
	public void testMultipleDecisionRequestHasMultipleContentSelectorOfInvalidType()
	{
		
		Collection<Attribute> resourceAttrs = new LinkedList<Attribute>();
		resourceAttrs.add(new Attribute("testId3", XacmlDataTypes.STRING.create("value0")));
		resourceAttrs.add(new Attribute("testId4", XacmlDataTypes.STRING.create("value1")));
		
		resourceAttrs.add(new Attribute(MultipleDecisionXPathExpressionHandler.MULTIPLE_CONTENT_SELECTOR, 
				XacmlDataTypes.STRING.create("//md:record/md:patient")));
		
		
		Attributes resource = new Attributes(AttributeCategoryId.RESOURCE, content, resourceAttrs);
		
		Collection<Attribute> subjectAttr = new LinkedList<Attribute>();
		subjectAttr.add(new Attribute("testId7", XacmlDataTypes.STRING.create("value0")));
		subjectAttr.add(new Attribute("testId8", XacmlDataTypes.STRING.create("value1")));
				
		Attributes subject =  new Attributes(AttributeCategoryId.SUBJECT_ACCESS, content, subjectAttr);
		
		RequestContext request = new RequestContext(false, 
				Arrays.asList(subject, resource));
		
		assertFalse(request.hasRepeatingCategories());
		Capture<RequestContext> c0 = new Capture<RequestContext>();
		
		
		expect(pdp.requestDecision(capture(c0))).andReturn(
				new Result(Decision.INDETERMINATE, 
						new Status(StatusCode.createProcessingError()),
						Collections.<Attributes>emptyList()));
		
		replay(pdp);
		
		profile.handle(request, pdp);
		
		assertEquals(request, c0.getValue());
		verify(pdp);
	}
	
	@Test
	public void testMultipleDecisionCategoryDoesNotHaveContent()
	{
		
		Collection<Attribute> resourceAttrs = new LinkedList<Attribute>();
		resourceAttrs.add(new Attribute("testId3", XacmlDataTypes.STRING.create("value0")));
		resourceAttrs.add(new Attribute("testId4", XacmlDataTypes.STRING.create("value1")));
		
		resourceAttrs.add(new Attribute(MultipleDecisionXPathExpressionHandler.MULTIPLE_CONTENT_SELECTOR, 
				XacmlDataTypes.XPATHEXPRESSION.create("//md:record/md:patient", AttributeCategoryId.RESOURCE)));
		
		Attributes resource = new Attributes(AttributeCategoryId.RESOURCE, resourceAttrs);
		
		Collection<Attribute> subjectAttr = new LinkedList<Attribute>();
		subjectAttr.add(new Attribute("testId7", XacmlDataTypes.STRING.create("value0")));
		subjectAttr.add(new Attribute("testId8", XacmlDataTypes.STRING.create("value1")));
				
		Attributes subject =  new Attributes(AttributeCategoryId.SUBJECT_ACCESS, subjectAttr);
		
		RequestContext request = new RequestContext(false, 
				Arrays.asList(subject, resource));
		
		assertFalse(request.hasRepeatingCategories());
		
		replay(pdp);
		
		Collection<Result> results = profile.handle(request, pdp);
		assertEquals(Decision.INDETERMINATE, Iterables.getOnlyElement(results).getDecision());
		
		verify(pdp);
	}
	
	@Test
	public void testWithEmptyRequest()
	{
		RequestContext context = new RequestContext(false, 
				Collections.<Attributes>emptyList());
		
		Capture<RequestContext> c0 = new Capture<RequestContext>();
		
		expect(pdp.requestDecision(capture(c0))).andReturn(
				new Result(Decision.INDETERMINATE, 
						new Status(StatusCode.createProcessingError()),
						Collections.<Attributes>emptyList()));
		
		replay(pdp);
		Collection<Result> results = profile.handle(context, pdp);
		assertEquals(new Status(StatusCode.createProcessingError()), results.iterator().next().getStatus());
		assertEquals(1, results.size());
		assertSame(context, c0.getValue());
		verify(pdp);
	}
}

