package com.artagon.xacml.v3.pdp.profiles;

import java.util.Collection;
import java.util.LinkedList;

import com.artagon.xacml.v3.Attributes;
import com.artagon.xacml.v3.AttributesReference;
import com.artagon.xacml.v3.Decision;
import com.artagon.xacml.v3.RequestContext;
import com.artagon.xacml.v3.RequestReference;
import com.artagon.xacml.v3.RequestSyntaxException;
import com.artagon.xacml.v3.Result;
import com.artagon.xacml.v3.pdp.PolicyDecisionCallback;

final class MultipleDecisionRequestReferencesHandler extends AbstractRequestContextHandler
{
	
	public Collection<Result> handle(RequestContext request, PolicyDecisionCallback pdp) 
	{
		Collection<Result> results = new LinkedList<Result>();
		Collection<RequestReference> references = request.getRequestReferences();
		if(references.isEmpty()){
			return handleNext(request, pdp);
		}
		for(RequestReference ref : references){
			try{
				RequestContext resolvedRequest = resolveAttributes(request, ref);
				results.addAll(handleNext(resolvedRequest, pdp));
			}catch(RequestSyntaxException e){
				results.add(new Result(Decision.INDETERMINATE, e.getStatus(), 
						request.getIncludeInResultAttributes()));
			}
		}
		return results;
	}
	
	private RequestContext resolveAttributes(RequestContext req, 
			RequestReference reqRef) throws RequestSyntaxException
	{
		Collection<Attributes> resolved = new LinkedList<Attributes>();
		for(AttributesReference ref : reqRef.getReferencedAttributes()){
			Attributes attributes = req.getReferencedAttributes(ref);
			if(attributes == null){
				throw new RequestSyntaxException(
						"Failed to resolve attribute reference", 
						ref.getReferenceId());
			}
			resolved.add(attributes);
		}
		return new RequestContext(req.isReturnPolicyIdList(), resolved);
	}
	
}
