package com.artagon.xacml.v30.spi.function;

import java.util.List;

import com.artagon.xacml.v30.EvaluationContext;
import com.artagon.xacml.v30.Expression;
import com.artagon.xacml.v30.FunctionInvocationException;
import com.artagon.xacml.v30.FunctionSpec;
import com.artagon.xacml.v30.ValueExpression;

/**
 * An interface for a function invocation
 * 
 * @author Giedrius Trumpickas
 */
public interface FunctionInvocation
{
	/**
	 * Invokes function
	 * 
	 * @param <T>
	 * @param spec a function spec
	 * @param context an evaluation context
	 * @param arguments a function invocation parameters
	 * @return {@link ValueExpression} representing function invocation result
	 * @throws FunctionInvocationException if function invocation
	 * fails 
	 */
	<T extends ValueExpression> T invoke(FunctionSpec spec, 
			EvaluationContext context, Expression ...arguments) 
		throws FunctionInvocationException;
	
	/**
	 * Invokes function
	 * 
	 * @param <T>
	 * @param spec a function spec
	 * @param context an evaluation context
	 * @param params a function invocation parameters
	 * @return {@link T} a function invocation result
	 * @throws FunctionInvocationException if a function
	 * invocation fails
	 */
	<T extends ValueExpression> T invoke(FunctionSpec spec, 
			EvaluationContext context, List<Expression> params) 
		throws FunctionInvocationException;
}
