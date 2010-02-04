package com.artagon.xacml.v3.policy.type;

import java.net.URI;

import com.artagon.xacml.util.Preconditions;

final class AnyURITypeImpl extends BaseAttributeType<AnyURIType.AnyURIValue> implements AnyURIType
{
	public AnyURITypeImpl(String typeId) {
		super(typeId);
	}

	@Override
	public AnyURIValue fromXacmlString(String v) {
		Preconditions.checkNotNull(v);
		return new AnyURIValue(this, URI.create(v));
	}
	
	@Override
	public boolean isConvertableFrom(Object any) {
		return URI.class.isInstance(any) || String.class.isInstance(any);
	}

	@Override
	public AnyURIValue create(Object any){
		Preconditions.checkNotNull(any);
		Preconditions.checkArgument(isConvertableFrom(any), String.format(
				"Value=\"%s\" of class=\"%s\" can't ne converted to XACML \"AnyURI\" type", 
				any, any.getClass()));
		if(String.class.isInstance(any)){
			return fromXacmlString((String)any);
		}
		return new AnyURIValue(this, (URI)any);
	}
}
