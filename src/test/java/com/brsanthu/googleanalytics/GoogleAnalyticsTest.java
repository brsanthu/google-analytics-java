package com.brsanthu.googleanalytics;

import org.junit.Before;
import org.junit.Test;


public class GoogleAnalyticsTest {

	private static GoogleAnalytics ga = null;

	@Before
	public void setup() {
		ga = new GoogleAnalytics("UA-44034973-2", "Junit Test", "1.0.0");
	}

	@Test
	public void testBasic() throws Exception {
		Response response = ga.send(new Event("Category", "Event Action"));
		System.out.println("response=" + response);
	}

	@Test
	public void testSocial() throws Exception {
		ga.send(new Social().socialAction("").socialActionTarget(""));
	}
}
