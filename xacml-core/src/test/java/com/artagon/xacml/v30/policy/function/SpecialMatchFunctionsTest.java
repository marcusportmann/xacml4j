package com.artagon.xacml.v30.policy.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.artagon.xacml.v30.spi.function.AnnotiationBasedFunctionProvider;
import com.artagon.xacml.v30.spi.function.FunctionProvider;
import com.artagon.xacml.v30.types.BooleanType;
import com.artagon.xacml.v30.types.RFC822NameType;
import com.artagon.xacml.v30.types.RFC822NameValueExp;
import com.artagon.xacml.v30.types.StringType;
import com.artagon.xacml.v30.types.StringValueExp;
import com.artagon.xacml.v30.types.X500NameType;
import com.artagon.xacml.v30.types.X500NameValueExp;

public class SpecialMatchFunctionsTest 
{
	private FunctionProvider p;
	
	@Before
	public void init() throws Exception
	{
		this.p = new AnnotiationBasedFunctionProvider(SpecialMatchFunctions.class);
	}
	
	@Test
	public void testIfFunctionImplemented()
	{
		assertNotNull(p.getFunction("urn:oasis:names:tc:xacml:1.0:function:rfc822Name-match"));
		assertNotNull(p.getFunction("urn:oasis:names:tc:xacml:1.0:function:x500Name-match"));
	}
	
	@Test
	public void testRFC822NameMatch()
	{
		StringValueExp p = StringType.STRING.create(".sun.com");
		RFC822NameValueExp n = RFC822NameType.RFC822NAME.create("test@east.sun.com");
		assertEquals(BooleanType.BOOLEAN.create(true), SpecialMatchFunctions.rfc822NameMatch(p, n));
		
		p = StringType.STRING.create("sun.com");
		n = RFC822NameType.RFC822NAME.create("test@sun.com");
		assertEquals(BooleanType.BOOLEAN.create(true), SpecialMatchFunctions.rfc822NameMatch(p, n));
	}
	
	@Test
	public void testX500NameMatch()
	{
		X500NameValueExp a = X500NameType.X500NAME.create("ou=org,o=com");
		X500NameValueExp b = X500NameType.X500NAME.create("cn=test, ou=org,o=com");
		
		assertEquals(BooleanType.BOOLEAN.create(true), SpecialMatchFunctions.x500NameMatch(a, b));
		
		a = X500NameType.X500NAME.create("ou=org,o=com");
		b = X500NameType.X500NAME.create("cn=test, ou=ORG,o=Com");
		
		assertEquals(BooleanType.BOOLEAN.create(true), SpecialMatchFunctions.x500NameMatch(a, b));
		
		a = X500NameType.X500NAME.create("ou=org1,o=com");
		b = X500NameType.X500NAME.create("cn=test, ou=ORG,o=com");
		
		assertEquals(BooleanType.BOOLEAN.create(false), SpecialMatchFunctions.x500NameMatch(a, b));
		
	}
}
