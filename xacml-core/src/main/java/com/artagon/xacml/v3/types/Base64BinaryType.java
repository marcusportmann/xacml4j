package com.artagon.xacml.v3.types;

import java.util.Collection;

import com.artagon.xacml.v3.AttributeValue;
import com.artagon.xacml.v3.AttributeValueType;
import com.artagon.xacml.v3.BagOfAttributeValues;

public interface Base64BinaryType extends AttributeValueType
{	
	Base64BinaryValue create(Object any, Object ...params);
	Base64BinaryValue fromXacmlString(String v, Object ...params);
	
	final class Base64BinaryValue extends BaseAttributeValue<BinaryValue>
	{
		public Base64BinaryValue(Base64BinaryType type, BinaryValue value) {
			super(type, value);
		}
		
		@Override
		public String toXacmlString() {
			return getValue().toBase64();
		}
	}
	
	public final class Factory
	{
		private final static Base64BinaryType INSTANCE = new Base64BinaryTypeImpl("http://www.w3.org/2001/XMLSchema#base64Binary");
		
		public static Base64BinaryType getInstance(){
			return INSTANCE;
		}
		
		public static Base64BinaryValue create(Object v, Object ...params){
			return INSTANCE.create(v, params);
		}
		
		public static Base64BinaryValue fromXacmlString(String v, Object ...params){
			return INSTANCE.fromXacmlString(v, params);
		}
		
		public static BagOfAttributeValues bagOf(AttributeValue ...values){
			return INSTANCE.bagType().create(values);
		}
		
		public static BagOfAttributeValues bagOf(Collection<AttributeValue> values){
			return INSTANCE.bagType().create(values);
		}
		
		public static BagOfAttributeValues emptyBag(){
			return INSTANCE.bagType().createEmpty();
		}
	}
}
