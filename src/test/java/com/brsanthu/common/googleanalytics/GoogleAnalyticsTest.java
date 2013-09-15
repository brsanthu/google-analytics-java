package com.brsanthu.common.googleanalytics;

import org.junit.Test;


public class GoogleAnalyticsTest {

	@Test
	public void testBasic() throws Exception {
		@SuppressWarnings("resource")
		GoogleAnalytics ga = new GoogleAnalytics("UA-44034973-2", "Junit Test", "1.0.0");
		Response response = ga.send(new Request());
		System.out.println("response=" + response);
	}
}
