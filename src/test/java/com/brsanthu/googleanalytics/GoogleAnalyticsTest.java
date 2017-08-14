package com.brsanthu.googleanalytics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import com.brsanthu.googleanalytics.request.DefaultRequest;
import com.brsanthu.googleanalytics.request.GoogleAnalyticsResponse;

public class GoogleAnalyticsTest {

    private static final String TRACKING_ID = "UA-44034973-2";
    private static GoogleAnalytics ga = null;

    @BeforeClass
    public static void setup() {
        ga = GoogleAnalytics.builder().withTrackingId(TRACKING_ID).withAppName("Junit Test").withAppVersion("1.0.0").build();
    }

    @Test
    public void testPageView() throws Exception {
        ga.pageView("http://www.google.com", "Search").post();
    }

    @Test
    public void testSocial() throws Exception {
        ga.social("Facebook", "Like", "https://www.google.com").post();
        ga.social("Google+", "Post", "It is a comment").post();
        ga.social("Twitter", "Repost", "Post").post();
    }

    @Test
    public void testGatherStats() throws Exception {
        ga.getConfig().setGatherStats(false);
        ga.resetStats();
        ga.pageView().post();
        ga.pageView().post();
        ga.pageView().post();
        ga.appView().post();
        ga.appView().post();
        ga.item().post();

        assertEquals(0, ga.getStats().getPageViewHits());
        assertEquals(0, ga.getStats().getAppViewHits());
        assertEquals(0, ga.getStats().getItemHits());

        ga.getConfig().setGatherStats(true);
        ga.resetStats();
        ga.pageView().post();
        ga.pageView().post();
        ga.pageView().post();
        ga.appView().post();
        ga.appView().post();
        ga.item().post();

        assertEquals(3, ga.getStats().getPageViewHits());
        assertEquals(2, ga.getStats().getAppViewHits());
        assertEquals(1, ga.getStats().getItemHits());
    }

    @Test
    public void testCustomDimensions() throws Exception {
        DefaultRequest defaultRequest = new DefaultRequest();
        defaultRequest.customDimension(1, "foo");
        defaultRequest.customDimension(5, "bar");

        // Local ga
        GoogleAnalytics lga = GoogleAnalytics.builder().withDefaultRequest(defaultRequest).withTrackingId(TRACKING_ID).build();

        GoogleAnalyticsResponse response = lga.pageView("http://www.google.com", "Search").customDimension(2, "bob").customDimension(5, "alice")
                .post();

        assertEquals("foo", response.getRequestParams().get("cd1"));
        assertEquals("bob", response.getRequestParams().get("cd2"));
        assertEquals("alice", response.getRequestParams().get("cd5"));
    }

    @Test
    public void testCustomMetrics() throws Exception {
        DefaultRequest defaultRequest = new DefaultRequest();
        defaultRequest.customMetric(1, "foo");
        defaultRequest.customMetric(5, "bar");

        GoogleAnalytics lga = GoogleAnalytics.builder().withDefaultRequest(defaultRequest).withTrackingId(TRACKING_ID).build();

        GoogleAnalyticsResponse response = lga.pageView("http://www.google.com", "Search").customDimension(2, "bob").customDimension(5, "alice")
                .post();

        assertEquals("foo", response.getRequestParams().get("cm1"));
        assertEquals("bob", response.getRequestParams().get("cm2"));
        assertEquals("alice", response.getRequestParams().get("cm5"));
    }

    @Test
    public void testUserIpAndAgent() throws Exception {
        DefaultRequest defaultRequest = new DefaultRequest();
        defaultRequest.userIp("1.2.3.4");
        defaultRequest.userAgent("Opera/9.80 (Windows NT 6.0) Presto/2.12.388 Version/12.14");

        GoogleAnalytics lga = GoogleAnalytics.builder().withDefaultRequest(defaultRequest).withTrackingId(TRACKING_ID).build();

        GoogleAnalyticsResponse response = lga.pageView("http://www.google.com", "Search").userIp("1.2.3.5")
                .userAgent("Chrome/9.80 (Windows NT 6.0) Presto/2.12.388 Version/12.14").post();

        assertEquals("1.2.3.5", response.getRequestParams().get("uip"));
        assertEquals("Chrome/9.80 (Windows NT 6.0) Presto/2.12.388 Version/12.14", response.getRequestParams().get("ua"));
    }

    @Test
    public void testUserDetails() throws Exception {
        GoogleAnalyticsResponse response = ga.pageView("http://www.google.com", "Search").post();
        assertNotNull(response.getRequestParams().get("cid"));

        DefaultRequest defaultRequest = new DefaultRequest();
        defaultRequest.clientId("1234");
        defaultRequest.userId("user1");

        GoogleAnalytics lga = GoogleAnalytics.builder().withDefaultRequest(defaultRequest).withTrackingId(TRACKING_ID).build();

        response = lga.pageView("http://www.google.com", "Search").post();
        assertEquals("1234", response.getRequestParams().get("cid"));
        assertEquals("user1", response.getRequestParams().get("uid"));

        response = lga.pageView("http://www.google.com", "Search").clientId("12345").userId("user2").post();
        assertEquals("12345", response.getRequestParams().get("cid"));
        assertEquals("user2", response.getRequestParams().get("uid"));
    }
}
