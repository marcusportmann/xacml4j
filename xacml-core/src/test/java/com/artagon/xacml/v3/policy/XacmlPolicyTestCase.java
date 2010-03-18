package com.artagon.xacml.v3.policy;

import static org.easymock.EasyMock.createStrictMock;

import org.junit.Before;

import com.artagon.xacml.v3.EvaluationContext;
import com.artagon.xacml.v3.EvaluationContextFactory;
import com.artagon.xacml.v3.policy.impl.DefaultEvaluationContextFactory;
import com.artagon.xacml.v3.policy.spi.xpath.JDKXPathProvider;

public class XacmlPolicyTestCase
{	
	protected EvaluationContext context;
	protected EvaluationContextFactory contextFactory;
	protected AttributeResolver attributeService;
	protected DecisionRuleReferenceResolver policyResolver;
	protected Policy currentPolicy;
	
	@Before
	public void init_XACMLTestCase() throws Exception{
		this.attributeService = createStrictMock(AttributeResolver.class);
		this.policyResolver = createStrictMock(DecisionRuleReferenceResolver.class);
		this.currentPolicy = createStrictMock(Policy.class);
		this.contextFactory = new DefaultEvaluationContextFactory(attributeService, policyResolver, 
				new JDKXPathProvider());
		this.context = contextFactory.createContext(currentPolicy);
	}
}