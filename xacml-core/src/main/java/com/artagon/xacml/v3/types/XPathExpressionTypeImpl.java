package com.artagon.xacml.v3.types;


import com.artagon.xacml.v3.AttributeCategoryId;
import com.artagon.xacml.v3.XacmlSyntaxException;
import com.artagon.xacml.v3.types.XPathExpressionType.XPathExpressionValue;
import com.google.common.base.Preconditions;

public class XPathExpressionTypeImpl extends BaseAttributeType<XPathExpressionValue> 
	implements XPathExpressionType
{
	public XPathExpressionTypeImpl(String typeId) {
		super(typeId);
	}
	
	@Override
	public boolean isConvertableFrom(Object any) {
		return any instanceof String;
	}
	
	@Override
	public XPathExpressionValue create(String xpath, AttributeCategoryId category) 
	{
		return new XPathExpressionValue(this, xpath, category);
	}
	
	@Override
	public XPathExpressionValue create(Object v, Object ... params) 
	{
		Preconditions.checkArgument(isConvertableFrom(v), 
				"Given instance=\"%s\" can not be converted to this type value", v);
		Preconditions.checkArgument(params != null && params.length > 0, 
				"XPath category must be specified");
		return new XPathExpressionValue(this, (String)v, (AttributeCategoryId)params[0]);
	}
	
	@Override
	public XPathExpressionValue fromXacmlString(String v, Object ...params) 
	{
		Preconditions.checkArgument(params != null && params.length > 0, 
				"XPath category must be specified");
		try{
			AttributeCategoryId categoryId = AttributeCategoryId.parse(String.valueOf(params[0]));
			return new XPathExpressionValue(this, v, categoryId);
		}catch(XacmlSyntaxException e){
			throw new IllegalArgumentException(e);
		}
	}
}
