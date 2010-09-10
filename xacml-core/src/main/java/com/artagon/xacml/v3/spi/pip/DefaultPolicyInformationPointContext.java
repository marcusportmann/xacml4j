package com.artagon.xacml.v3.spi.pip;

import com.artagon.xacml.v3.AttributeCategoryId;
import com.artagon.xacml.v3.AttributeReference;
import com.artagon.xacml.v3.AttributeValue;
import com.artagon.xacml.v3.AttributeValueType;
import com.artagon.xacml.v3.BagOfAttributeValues;
import com.artagon.xacml.v3.EvaluationContext;
import com.artagon.xacml.v3.RequestContextAttributesCallback;
import com.google.common.base.Preconditions;

class DefaultPolicyInformationPointContext implements PolicyInformationPointContext
{
	private AttributeReference ref;
	private EvaluationContext context;
	private RequestContextAttributesCallback callback;
	
	public DefaultPolicyInformationPointContext(EvaluationContext context, 
			RequestContextAttributesCallback callback,
			AttributeReference ref){
		Preconditions.checkNotNull(context);
		Preconditions.checkNotNull(callback);
		Preconditions.checkNotNull(ref);
		this.context = context;
		this.ref = ref;
		this.callback = callback;
	}
	
	@Override
	public AttributeValue getCurrentDate() {
		return context.getCurrentDate();
	}

	@Override
	public AttributeValue getCurrentDateTime() {
		return context.getCurrentDateTime();
	}

	@Override
	public AttributeValue getCurrentTime() {
		return context.getCurrentTime();
	}

	@Override
	public Object getValue(Object key) {
		return context.getValue(ref.getCategory(), key);
	}

	@Override
	public Object setValue(Object key, Object v) {
		return context.setValue(ref.getCategory(), key, v);
	}	
	
	public <AV extends AttributeValue> BagOfAttributeValues<AV> getAttributeValues(
			AttributeCategoryId category, String attributeId,
			AttributeValueType dataType, String issuer) {
		return callback.<AV>getAttributeValues(category, attributeId, dataType,
				issuer);
	}

	public <AV extends AttributeValue> BagOfAttributeValues<AV> getAttributeValues(
			AttributeCategoryId category, String attributeId,
			AttributeValueType dataType) {
		return callback.<AV>getAttributeValues(category, attributeId, dataType);
	}

	public <AV extends AttributeValue> AV getAttributeValue(
			AttributeCategoryId categoryId, String attributeId,
			AttributeValueType dataType) {
		return callback.<AV>getAttributeValue(categoryId, attributeId, dataType);
	}

	public <AV extends AttributeValue> AV getAttributeValue(
			AttributeCategoryId categoryId, String attributeId,
			AttributeValueType dataType, String issuer) {
		return callback.<AV>getAttributeValue(categoryId, attributeId, dataType, issuer);
	}
}
