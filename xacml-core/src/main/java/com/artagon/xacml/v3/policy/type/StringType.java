package com.artagon.xacml.v3.policy.type;

import com.artagon.xacml.v3.AttributeValueType;
import com.artagon.xacml.v3.BagOfAttributeValuesType;
import com.artagon.xacml.v3.policy.spi.type.BaseAttributeValue;


public interface StringType extends AttributeValueType
{
	StringValue create(Object v, Object ...params);
	StringValue fromXacmlString(String v, Object ...params);
	BagOfAttributeValuesType<StringValue> bagOf();
	
	final class StringValue extends BaseAttributeValue<String>
	{
		public StringValue(StringType type, String value) {
			super(type, value);
		}
		
		public boolean equalsIgnoreCase(StringValue v){
			return getValue().equalsIgnoreCase(v.getValue());
		}
	}
}