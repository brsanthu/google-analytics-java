package com.brsanthu.googleanalytics;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.BeforeClass;
import org.junit.Test;

import com.brsanthu.googleanalytics.internal.GoogleAnalyticsImpl;
import com.brsanthu.googleanalytics.request.AppViewHit;
import com.brsanthu.googleanalytics.request.DefaultRequest;
import com.brsanthu.googleanalytics.request.GoogleAnalyticsResponse;
import com.brsanthu.googleanalytics.request.ItemHit;
import com.brsanthu.googleanalytics.request.PageViewHit;
import com.brsanthu.googleanalytics.request.SocialHit;

public class GoogleAnalyticsTest {

	private static GoogleAnalyticsImpl ga = null;

	@BeforeClass
	public static void setup() {
		ga = new GoogleAnalyticsImpl("UA-44034973-2", "Junit Test", "1.0.0");
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
		new GoogleAnalyticsImpl(config, "TrackingId") {
			@Override
			protected int getDefaultMaxPerRoute(GoogleAnalyticsConfig config1) {
				value.set(super.getDefaultMaxPerRoute(config));
				
				return value.get();
			}
		};
		
		assertEquals(10, value.get());
	}
	
	@Test
	public void testCustomDimensions() throws Exception {
		DefaultRequest defaultRequest = new DefaultRequest();
		defaultRequest.customDimension(1, "foo");
		defaultRequest.customDimension(5, "bar");
		
		ga.setDefaultRequest(defaultRequest);
		PageViewHit request = new PageViewHit("http://www.google.com", "Search");
		request.customDimension(2, "bob");
		request.customDimension(5, "alice");
		
		GoogleAnalyticsResponse response = ga.post(request);
		
		assertEquals("foo", response.getPostedParmsAsMap().get("cd1"));
		assertEquals("bob", response.getPostedParmsAsMap().get("cd2"));
		assertEquals("alice", response.getPostedParmsAsMap().get("cd5"));
	}

	@Test
	public void testCustomMetrics() throws Exception {
		DefaultRequest defaultRequest = new DefaultRequest();
		defaultRequest.customMetric(1, "foo");
		defaultRequest.customMetric(5, "bar");
		
		ga.setDefaultRequest(defaultRequest);
		PageViewHit request = new PageViewHit("http://www.google.com", "Search");
		request.customMetric(2, "bob");
		request.customMetric(5, "alice");
		
		GoogleAnalyticsResponse response = ga.post(request);
		
		assertEquals("foo", response.getPostedParmsAsMap().get("cm1"));
		assertEquals("bob", response.getPostedParmsAsMap().get("cm2"));
		assertEquals("alice", response.getPostedParmsAsMap().get("cm5"));
	}

	@Test
	public void testUserIpAndAgent() throws Exception {
		DefaultRequest defaultRequest = new DefaultRequest();
		defaultRequest.userIp("1.2.3.4");
		defaultRequest.userAgent("Opera/9.80 (Windows NT 6.0) Presto/2.12.388 Version/12.14");
		
		ga.setDefaultRequest(defaultRequest);
		PageViewHit request = new PageViewHit("http://www.google.com", "Search");
		defaultRequest.userIp("1.2.3.5");
		defaultRequest.userAgent("Chrome/9.80 (Windows NT 6.0) Presto/2.12.388 Version/12.14");
		
		GoogleAnalyticsResponse response = ga.post(request);
		
		assertEquals("1.2.3.5", response.getPostedParmsAsMap().get("uip"));
		assertEquals("Chrome/9.80 (Windows NT 6.0) Presto/2.12.388 Version/12.14", response.getPostedParmsAsMap().get("ua"));
	}
	
	@Test
	public void testUserDetails() throws Exception {
		PageViewHit request = new PageViewHit("http://www.google.com", "Search");
		GoogleAnalyticsResponse response = ga.post(request);
		assertNotNull(response.getPostedParmsAsMap().get("cid"));

		DefaultRequest defaultRequest = new DefaultRequest();
		defaultRequest.clientId("1234");
		defaultRequest.userId("user1");
		ga.setDefaultRequest(defaultRequest);
		
		request = new PageViewHit("http://www.google.com", "Search");
		response = ga.post(request);
		assertEquals("1234", response.getPostedParmsAsMap().get("cid"));
		assertEquals("user1", response.getPostedParmsAsMap().get("uid"));
		
		request = new PageViewHit("http://www.google.com", "Search");
		request.clientId("12345");
		request.userId("user2");
		
		response = ga.post(request);
		assertEquals("12345", response.getPostedParmsAsMap().get("cid"));
		assertEquals("user2", response.getPostedParmsAsMap().get("uid"));
	}
}
