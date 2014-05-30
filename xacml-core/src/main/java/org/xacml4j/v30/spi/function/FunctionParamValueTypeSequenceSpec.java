package org.xacml4j.v30.spi.function;

/*
 * #%L
 * Artagon XACML 3.0 Core Engine Implementation
 * %%
 * Copyright (C) 2009 - 2014 Artagon
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xacml4j.v30.Expression;
import org.xacml4j.v30.ValueType;

import com.google.common.base.Objects;


final class FunctionParamValueTypeSequenceSpec extends BaseFunctionParamSpec
{
	private final static Logger log = LoggerFactory.getLogger(FunctionParamValueTypeSequenceSpec.class);

	private Integer min = 0;
	private Integer max = Integer.MAX_VALUE;

	private ValueType paramType;

	/**
	 * Constructs parameter specification 
	 * with a given minimum and maximum 
	 * number of parameters
	 * 
	 * @param min a minimum number
	 * @param max a maximum number
	 * @param paramType an argument type
	 */
	public FunctionParamValueTypeSequenceSpec(
			Integer min, 
			Integer max,
			ValueType paramType){
		super((min != null && min == 0), true, null);
		this.min = min;
		this.max = max;
		this.paramType = paramType;
	}

	public FunctionParamValueTypeSequenceSpec(int min,
			ValueType paramType){
		this(min, null, paramType);
	}
	
	/**
	 * Gets parameter XACML type.
	 *
	 * @return parameter XACML type
	 */
	public ValueType getParamType(){
		return paramType;
	}

	/**
	 * Gets minimum number of parameters
	 * in this sequence.
	 *
	 * @return a minimum number of parameters
	 */
	public Integer getMinParams(){
		return min;
	}

	public Integer getMaxParams(){
		return max;
	}

	@Override
	public boolean isValidParamType(ValueType type){
		return this.paramType.equals(type);
	}

	@Override
	public boolean validate(ListIterator<Expression> it) {
		int c = 0;
		boolean valid = true;
		while(it.hasNext()){
			Expression exp = it.next();
			ValueType expType = exp.getEvaluatesTo();
			if(!expType.equals(paramType)){
				log.debug("Expected type=\"{}\" but was type=\"{}\"",
						paramType, expType);
				valid = false;
				break;
			}
			c++;
		}
		log.debug("Found \"{}\" parameters", c);
		if(min != null){
			valid &= c >= min;
		}
		if(max != null){
			valid &= c <= max;
		}
		return valid;
	}

	@Override
	public String toString(){
		return Objects
				.toStringHelper(this)
				.add("min", min)
				.add("max", max)
				.add("type", paramType)
				.toString();
	}

	@Override
	public int hashCode(){
		return Objects.hashCode(min, max, paramType);
	}

	@Override
	public boolean equals(Object o){
		if(o == this){
			return true;
		}
		if(o == null){
			return false;
		}
		if(!(o instanceof FunctionParamValueTypeSequenceSpec)){
			return false;
		}
		FunctionParamValueTypeSequenceSpec s = (FunctionParamValueTypeSequenceSpec)o;
		return Objects.equal(min, s.min) &&
				Objects.equal(max, s.max) &&
				paramType.equals(s.paramType);
	}
	
	public void accept(FunctionParamSpecVisitor v){
		v.visit(this);
	}
}
