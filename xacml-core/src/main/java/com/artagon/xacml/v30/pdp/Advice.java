package com.artagon.xacml.v30.pdp;

import java.util.Collection;
import java.util.LinkedList;

import com.artagon.xacml.v30.AttributeCategory;
import com.google.common.base.Preconditions;



/**
 * In some applications it is helpful to specify supplemental 
 * information about a decision. XACML provides facilities 
 * to specify supplemental information about a decision with 
 * the {@link Advice}. Such advice may be safely ignored by the PEP.
 * 
 * @author Giedrius Trumpickas
 */
public class Advice extends BaseDecisionRuleResponse
{
	public Advice(String adviceId, 
			Effect effect,
			Iterable<AttributeAssignment> attributes){
		super(adviceId, effect, attributes);
	}
	
	public static Builder builder(String id, Effect appliesTo){
		return new Builder(id, appliesTo);
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		if(o == this){
			return true;
		}
		if(!(o instanceof Advice)){
			return false;
		}
		Advice a = (Advice)o;
		return id.equals(a.id) && 
		attributes.equals(a.attributes);
	}
	
	public static class Builder
	{
		private String id;
		private Effect appliesTo;
		private Collection<AttributeAssignment> attributes = new LinkedList<AttributeAssignment>();
		
		private Builder(String id, Effect effect){
			this.id = id;
			this.appliesTo = effect;
		}
		
		public Builder attribute(AttributeAssignment attr){
			Preconditions.checkNotNull(attr);
			this.attributes.add(attr);
			return this;
		}
		
		public Builder attribute(
				String id, AttributeExp ... values)
		{
			return attribute(id, null, null, values);
		}
		
		public Builder attribute(
				String id,  
				AttributeCategory category, 
				String issuer, 
				AttributeExp ... values)
		{
			if(values == null || 
					values.length == 0){
				return this;
			}
			for(AttributeExp v : values){
				this.attributes.add(new AttributeAssignment(id, category, issuer, v));
			}
			return this;
		}
		
		public Advice create(){
			return new Advice(id, appliesTo, attributes);
		}
	}
}
