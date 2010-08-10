package com.artagon.xacml.v30;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.oasis.xacml.v30.jaxb.AttributeType;
import org.oasis.xacml.v30.jaxb.AttributeValueType;
import org.oasis.xacml.v30.jaxb.AttributesReferenceType;
import org.oasis.xacml.v30.jaxb.AttributesType;
import org.oasis.xacml.v30.jaxb.ContentType;
import org.oasis.xacml.v30.jaxb.RequestReferenceType;
import org.oasis.xacml.v30.jaxb.RequestType;
import org.oasis.xacml.v30.jaxb.ResponseType;
import org.w3c.dom.Node;

import com.artagon.xacml.util.DOMUtil;
import com.artagon.xacml.v3.Attribute;
import com.artagon.xacml.v3.AttributeCategoryId;
import com.artagon.xacml.v3.AttributeValue;
import com.artagon.xacml.v3.Attributes;
import com.artagon.xacml.v3.AttributesReference;
import com.artagon.xacml.v3.RequestContext;
import com.artagon.xacml.v3.RequestReference;
import com.artagon.xacml.v3.ResponseContext;
import com.artagon.xacml.v3.XacmlSyntaxException;
import com.artagon.xacml.v3.marshall.PolicyMapperSupport;
import com.artagon.xacml.v3.types.XacmlDataTypes;

public class Xacml30ContextMapper extends PolicyMapperSupport
{
	
	public Xacml30ContextMapper(){
	}
	
	public RequestContext create(RequestType req) throws XacmlSyntaxException
	{
		Collection<Attributes> attributes = new LinkedList<Attributes>();
		for(AttributesType a : req.getAttributes()){
			attributes.add(create(a));
		}
		Collection<RequestReference> multiRequests = new LinkedList<RequestReference>();
		if(req.getMultiRequests() != null){
			for(RequestReferenceType m : req.getMultiRequests().getRequestReference()){
				multiRequests.add(create(m));
			}
		}
		return new RequestContext(req.isReturnPolicyIdList(), attributes, multiRequests, null);
	}
	
	public ResponseType create(ResponseContext res) throws XacmlSyntaxException
	{
		return null;
	}
	
	private Attributes create(AttributesType attributes) throws XacmlSyntaxException
	{
		Collection<Attribute> attr = new LinkedList<Attribute>();
		for(AttributeType a : attributes.getAttribute()){
			attr.add(create(a));
		}
		return new Attributes(AttributeCategoryId.parse(attributes.getCategory()), 
				getContent(attributes.getContent()), attr);
	}
	
	private Node getContent(ContentType content) throws XacmlSyntaxException
	{
		if(content == null){
			return null;
		}
		List<Object> o = content.getContent();
		if(o.isEmpty()){
			return null;
		}
		return DOMUtil.copyNode((Node)o.iterator().next());
	}
	
	private RequestReference create(RequestReferenceType m) throws XacmlSyntaxException
	{
		Collection<AttributesReference> references = new LinkedList<AttributesReference>();
		for(AttributesReferenceType r : m.getAttributesReference()){
			references.add(new AttributesReference(r.getReferenceId().toString()));
		}
		return new RequestReference(references);
	}
	
	private Attribute create(AttributeType a) throws XacmlSyntaxException
	{
		Collection<AttributeValue> values = new LinkedList<AttributeValue>();
		for(AttributeValueType v : a.getAttributeValue()){
			values.add(create(v));
		}
		return new Attribute(a.getAttributeId(), 
				a.getIssuer(), a.isIncludeInResult(), values);
	}
		
	private AttributeValue create(
			AttributeValueType value) 
		throws XacmlSyntaxException
	{
		List<Object> content = value.getContent();
		if(content == null || 
				content.isEmpty()){
			throw new XacmlSyntaxException(
					"Attribute does not have content");
		}
		
		return XacmlDataTypes.createAttributeValue(value.getDataType(), 
				content.iterator().next(), value.getOtherAttributes());
	}
}
