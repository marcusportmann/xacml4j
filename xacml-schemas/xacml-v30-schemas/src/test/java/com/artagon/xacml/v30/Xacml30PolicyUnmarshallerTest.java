package com.artagon.xacml.v30;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.Iterator;

import org.junit.BeforeClass;
import org.junit.Test;

import com.artagon.xacml.v3.CompositeDecisionRule;
import com.artagon.xacml.v3.Effect;
import com.artagon.xacml.v3.MatchAnyOf;
import com.artagon.xacml.v3.Policy;
import com.artagon.xacml.v3.PolicySet;
import com.artagon.xacml.v3.Rule;
import com.artagon.xacml.v3.Version;
import com.artagon.xacml.v3.XPathVersion;
import com.artagon.xacml.v3.marshall.PolicyUnmarshaller;
import com.artagon.xacml.v3.policy.combine.DefaultXacml30DecisionCombiningAlgorithms;
import com.artagon.xacml.v3.policy.function.DefaultXacml30Functions;

public class Xacml30PolicyUnmarshallerTest 
{
	private static PolicyUnmarshaller reader; 
	
	@BeforeClass
	public static void init_static() throws Exception
	{
		reader = new Xacml30PolicyUnmarshaller(new DefaultXacml30Functions(), 
				new DefaultXacml30DecisionCombiningAlgorithms());
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends CompositeDecisionRule> T getPolicy(String name) throws Exception
	{
		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
		assertNotNull(stream);
		return  (T)reader.unmarshal(stream);
	}
	
	
	@Test
	public void testPolicy1Mapping() throws Exception
	{
		Policy p = getPolicy("Policy1.xml");
		assertEquals("urn:oasis:names:tc:xacml:3.0:example:policyid:1", p.getId());
		assertEquals(Version.parse("1.0"), p.getVersion());
		assertEquals("urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:deny-overrides", p.getRuleCombiningAlgorithm().getId());
		assertNotNull(p.getTarget());
		assertEquals(0, p.getTarget().getAnyOf().size());
		assertEquals(1, p.getRules().size());
		Rule r = p.getRules().get(0);
		assertEquals("urn:oasis:names:tc:xacml:3.0:example:ruleid:1", r.getId());
		assertEquals(Effect.PERMIT, r.getEffect());
		assertNotNull(r.getDescription());
		assertNotNull(r.getTarget());
		assertEquals(2, r.getTarget().getAnyOf().size());
		Iterator<MatchAnyOf> it = r.getTarget().getAnyOf().iterator();
		MatchAnyOf m1 = it.next();
		MatchAnyOf m2 = it.next();
		assertEquals(1, m1.getAllOf().size());
		assertEquals(1, m2.getAllOf().size());
	}
	
	@Test
	public void testPolicy2Mapping() throws Exception
	{
		Policy p = getPolicy("Policy2.xml");
		assertEquals("urn:oasis:names:tc:xacml:3.0:example:policyid:3", p.getId());
		assertEquals(Version.parse("1.0"), p.getVersion());
		assertEquals("urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:deny-overrides", p.getRuleCombiningAlgorithm().getId());
	}
	
	@Test
	public void testPolicySet1Mapping() throws Exception
	{
		getPolicy("PolicySet1.xml");
		
	}
	
	@Test
	public void testPolicy3() throws Exception
	{
		Policy p = getPolicy("Policy3.xml");
		assertEquals(5, p.getVariableDefinitions().size());
		assertNotNull(p.getVariableDefinition("VAR01"));
		assertNotNull(p.getVariableDefinition("VAR02"));
		assertNotNull(p.getVariableDefinition("VAR03"));
		assertNotNull(p.getVariableDefinition("VAR04"));
		assertNotNull(p.getVariableDefinition("VAR05"));
	}
	
	@Test
	public void testPolicyIIIF006Mapping() throws Exception
	{
		PolicySet p0 = getPolicy("IIIF006Policy.xml");
		assertNotNull(p0);
		assertEquals("urn:oasis:names:tc:xacml:2.0:conformance-test:IIIF006:policySet", p0.getId());
		assertEquals("Policy Set for Conformance Test IIIF006.", p0.getDescription());
		assertNotNull(p0.getDefaults());
		assertEquals(XPathVersion.XPATH1, p0.getDefaults().getXPathVersion());
		assertNotNull(p0.getTarget());
		assertEquals(1, p0.getDecisions().size());
	}
	
}

