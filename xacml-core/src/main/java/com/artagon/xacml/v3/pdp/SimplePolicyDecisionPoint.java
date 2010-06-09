package com.artagon.xacml.v3.pdp;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.artagon.xacml.v3.Decision;
import com.artagon.xacml.v3.EvaluationContext;
import com.artagon.xacml.v3.EvaluationContextFactory;
import com.artagon.xacml.v3.Request;
import com.artagon.xacml.v3.Response;
import com.artagon.xacml.v3.Result;
import com.artagon.xacml.v3.Status;
import com.artagon.xacml.v3.StatusCode;
import com.artagon.xacml.v3.policy.PolicySet;
import com.artagon.xacml.v3.profiles.RequestProfileHandler;
import com.google.common.base.Preconditions;

public class SimplePolicyDecisionPoint implements PolicyDecisionPoint 
{
	private EvaluationContextFactory factory;
	private PolicySet policySet;
	private List<RequestProfileHandler> handlers;
	
	public SimplePolicyDecisionPoint(
			List<RequestProfileHandler> handlers,
			EvaluationContextFactory factory,  
			PolicySet policySet)
	{
		Preconditions.checkNotNull(factory);
		Preconditions.checkNotNull(policySet);
		this.factory = factory;
		this.policySet = policySet;
		this.handlers = new LinkedList<RequestProfileHandler>(handlers);
	}

	@Override
	public Response decide(Request request)
	{
		if(request.hasRequestReferences()){
			return new Response(Collections.singleton(
					new Result(
							new Status(StatusCode.createSyntaxError(), 
									"Request contains multiple references", 
									"PDP misconfiguration")
							)
					)); 
		}
		if(request.hasRepeatingCategories()){
			return new Response(Collections.singleton(
					new Result(
							new Status(StatusCode.createSyntaxError(), 
									"Request contains attrobutes with repeating categores",
									"PDP misconfiguration")
							)
					)); 
		}
		EvaluationContext context = factory.createContext(policySet, request);
		Decision decision = policySet.evaluateIfApplicable(context);
		Result result = new Result(decision, 
				context.getAdvices(), 
				context.getObligations(), 
				request.getIncludeInResultAttributes(), 
				context.getEvaluatedPolicies());
		return new Response(Collections.singleton(result));
	}
}
