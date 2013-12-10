package com.brsanthu.googleanalytics;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class GoogleAnalyticsRequestTest {

	@Test
	public void testExprimentArgs() throws Exception {
		PageViewHit pageViewHit = new PageViewHit().expirementId("1234567890").expirementVariant("some variation");
		assertEquals("1234567890", pageViewHit.expirementId());
		assertEquals("some variation", pageViewHit.expirementVariant());
	}

}
