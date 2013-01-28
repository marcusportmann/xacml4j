package org.xacml4j.v30.spi.pip;

import static org.easymock.EasyMock.createControl;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.xacml4j.v30.types.DateTimeType;
import org.xacml4j.v30.types.DateType;
import org.xacml4j.v30.types.TimeType;

import com.google.common.base.Ticker;

public class DefaultEnviromentAttributesResolverTest 
{
	private AttributeResolver r;
	private ResolverContext context;
	private IMocksControl c;
	
	@Before
	public void init(){
		this.c = createControl();
		this.r = new DefaultEnviromentAttributeResolver();
		this.context = c.createMock(ResolverContext.class);
	}
	
	@Test
	public void testResolve() throws Exception
	{
		Calendar now = Calendar.getInstance();
		expect(context.getDescriptor()).andReturn(r.getDescriptor());
		expect(context.getCurrentDateTime()).andReturn(now);
		expect(context.getTicker()).andReturn(Ticker.systemTicker());
		c.replay();
		AttributeSet a = r.resolve(context);
		assertEquals(DateTimeType.DATETIME.bagOf(DateTimeType.DATETIME.create(now)), a.get("urn:oasis:names:tc:xacml:1.0:environment:current-dateTime"));
		assertEquals(DateType.DATE.bagOf(now), a.get("urn:oasis:names:tc:xacml:1.0:environment:current-date"));
		assertEquals(TimeType.TIME.bagOf(now), a.get("urn:oasis:names:tc:xacml:1.0:environment:current-time"));
		c.verify();
	}
}
