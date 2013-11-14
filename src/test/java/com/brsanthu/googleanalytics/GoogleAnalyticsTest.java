package com.brsanthu.googleanalytics;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.BeforeClass;
import org.junit.Test;

public class GoogleAnalyticsTest {

	private static GoogleAnalytics ga = null;

	@BeforeClass
	public static void setup() {
		ga = new GoogleAnalytics("UA-44034973-2", "Junit Test", "1.0.0");
		System.out.println("Creating Google Analytis Object");
	}

	@Test
	public void testPageView() throws Exception {
		ga.post(new PageViewHit("http://www.google.com", "Search"));
	}

	@Test
	public void testSocial() throws Exception {
		ga.post(new SocialHit("Facebook", "Like", "https://www.google.com"));
		ga.post(new SocialHit("Google+", "Post", "It is a comment"));
		ga.post(new SocialHit("Twitter", "Repost", "Post"));
	}

	@Test
	public void testGatherStats() throws Exception {
		ga.getConfig().setGatherStats(false);
		ga.resetStats();
		ga.post(new PageViewHit());
		ga.post(new PageViewHit());
		ga.post(new PageViewHit());
		ga.post(new AppViewHit());
		ga.post(new AppViewHit());
		ga.post(new ItemHit());

		assertEquals(0, ga.getStats().getPageViewHits());
		assertEquals(0, ga.getStats().getAppViewHits());
		assertEquals(0, ga.getStats().getItemHits());

		ga.getConfig().setGatherStats(true);
		ga.resetStats();
		ga.post(new PageViewHit());
		ga.post(new PageViewHit());
		ga.post(new PageViewHit());
		ga.post(new AppViewHit());
		ga.post(new AppViewHit());
		ga.post(new ItemHit());

		assertEquals(3, ga.getStats().getPageViewHits());
		assertEquals(2, ga.getStats().getAppViewHits());
		assertEquals(1, ga.getStats().getItemHits());
	}
	
	@Test
	public void testHttpConfig() throws Exception {
		final AtomicInteger value = new AtomicInteger();
		
		final GoogleAnalyticsConfig config = new GoogleAnalyticsConfig().setMaxThreads(10);
		new GoogleAnalytics(config, "TrackingId") {
			@Override
			protected int getDefaultMaxPerRoute(GoogleAnalyticsConfig config1) {
				value.set(super.getDefaultMaxPerRoute(config));
				
				return value.get();
			}
		};
		
		assertEquals(10, value.get());
	}
}
