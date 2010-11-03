package com.artagon.xacml.v3.spi.pip;

import java.util.LinkedList;
import java.util.List;

import com.artagon.xacml.v3.AttributeCategory;
import com.artagon.xacml.v3.AttributeDesignatorKey;
import com.artagon.xacml.v3.AttributeReferenceKey;
import com.artagon.xacml.v3.AttributeSelectorKey;
import com.artagon.xacml.v3.AttributeValueType;
import com.google.common.base.Preconditions;

public final class ContentResolverDescriptorBuilder 
{
	private String id;
	private String name;
	private AttributeCategory category;
	private List<AttributeReferenceKey> keys;
	
	private ContentResolverDescriptorBuilder(String id, String name, AttributeCategory category)
	{
		Preconditions.checkNotNull(id);
		Preconditions.checkNotNull(name);
		Preconditions.checkNotNull(category);
		this.id = id;
		this.name = name;
		this.category = category;
		this.keys = new LinkedList<AttributeReferenceKey>();
	}
	
	public static ContentResolverDescriptorBuilder create(String id, String name, AttributeCategory category){
		return new ContentResolverDescriptorBuilder(id, name, category);
	}
	
	public ContentResolverDescriptorBuilder designatorRef(AttributeCategory category, 
			String attributeId, AttributeValueType dataType, String issuer)
	{
		this.keys.add(new AttributeDesignatorKey(category, attributeId, dataType, issuer));
		return this;
	}
	
	public ContentResolverDescriptorBuilder selectorRef(
			AttributeCategory category, 
			String xpath, AttributeValueType dataType, 
			String contextAttributeId)
	{
		this.keys.add(new AttributeSelectorKey(category, xpath, dataType, contextAttributeId));
		return this;
	}
	
	public ContentResolverDescriptor build(){
		return new ContentResolverDescriptorImpl();
	}
	
	public class ContentResolverDescriptorImpl 
		extends BaseResolverDescriptor implements ContentResolverDescriptor
	{

		public ContentResolverDescriptorImpl() {
			super(id, name, category, keys);
		}

		@Override
		public boolean canResolve(AttributeCategory category) {
			return getCategory().equals(category);
		}
	}
}
