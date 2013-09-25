package com.brsanthu.googleanalytics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HitTypesTest {

	@Test
	public void testHitTypes() throws Exception {
		assertEquals("item", new ItemHit().hitType());
		assertEquals("appView", new AppViewHit().hitType());
		assertEquals("event", new EventHit().hitType());
		assertEquals("exception", new ExceptionHit().hitType());
		assertEquals("pageView", new PageViewHit().hitType());
		assertEquals("social", new SocialHit().hitType());
		assertEquals("timing", new TimingHit().hitType());
		assertEquals("transaction", new TransactionHit().hitType());
	}
}
