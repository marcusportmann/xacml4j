package com.artagon.xacml.v3.policy.spi.function;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import com.artagon.xacml.v3.Value;
import com.google.common.base.Preconditions;

public class CglibReflectionBasedFunctionInvocation extends AbstractReflectionBasedFunctionInvocation 
{
	private Object instance;
	private FastClass fastClass;
	private FastMethod fastMethod;
	
	public CglibReflectionBasedFunctionInvocation(
			Class<?> instanceClass,
			Method m, 
			boolean evalContextRequired)
	{
		super(evalContextRequired);
		Preconditions.checkNotNull(m);
		Preconditions.checkNotNull(instanceClass);
		Preconditions.checkArgument(instance == null || 
			!Modifier.isStatic(m.getModifiers()));
		Preconditions.checkArgument(m.getDeclaringClass().equals(instanceClass));
		this.fastClass = FastClass.create(instanceClass);
		this.fastMethod = fastClass.getMethod(m);
		Preconditions.checkState(fastMethod != null);
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends Value> T invoke(Object ...params) throws Exception
	{
		return (T)fastMethod.invoke(instance, params);
	}
}
