package com.brsanthu.googleanalytics;

import static com.brsanthu.googleanalytics.internal.Constants.TEST_TRACKING_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brsanthu.googleanalytics.request.DefaultRequest;
import com.brsanthu.googleanalytics.request.GoogleAnalyticsResponse;

public class GoogleAnalyticsApacheHttpTest {
    private static final Logger logger = LoggerFactory.getLogger(GoogleAnalyticsApacheHttpTest.class);

    protected static GoogleAnalytics ga = null;
    
    protected long startTime;
    protected long callStartTime;

    @BeforeClass
    public static void setup() {
        ga = new GoogleAnalyticsApacheHttpTest().getTestBuilder().build();
    }
    
    @Before
    public void start() {
        startTime = System.currentTimeMillis();
    }
    
    @After
    public void end() {
        logger.debug("Test took: " + (System.currentTimeMillis() - startTime));
    }
    
    protected void startCall() {
        callStartTime = System.currentTimeMillis();
    }
    
    protected void stopCall() {
        logger.debug("Call took: " + (System.currentTimeMillis() - callStartTime));;
    }
    
    protected GoogleAnalyticsBuilder getTestBuilder() {
        return GoogleAnalytics.builder().withTrackingId(TEST_TRACKING_ID).withAppName("Junit ApacheHttp Test").withAppVersion("1.0.0");
    }

    @Test
    public void testPageView() throws Exception {
        startCall();
        ga.pageView("http://www.google.com", "Search").send();
        stopCall();
        startCall();
        ga.pageView("http://www.google.com", "Search").sendAsync();
        stopCall();
    }

    @Test
    public void testPageViewBatch() throws Exception {
        startCall();
        GoogleAnalytics lga = getTestBuilder().withConfig(new GoogleAnalyticsConfig().setBatchingEnabled(true).setBatchSize(4)).build();
        lga.pageView("http://www.google.com", "Search").send();
        lga.pageView("http://www.google.com", "Search2").send();
        lga.pageView("http://www.google.com", "Search3").send();
        lga.pageView("http://www.google.com", "Search4").send();
        lga.pageView("http://www.google.com", "Search5").send();
        lga.flush();
        stopCall();
    }
    @Test
    public void testSocial() throws Exception {
        ga.social("Facebook", "Like", "https://www.google.com").send();
        ga.social("Google+", "Post", "It is a comment").send();
        ga.social("Twitter", "Repost", "Post").send();
    }

    @Test
    public void testGatherStats() throws Exception {
        ga.getConfig().setGatherStats(false);
        ga.resetStats();
        ga.pageView().send();
        ga.pageView().send();
        ga.pageView().send();
        ga.screenView().send();
        ga.screenView().send();
        ga.item().send();

        assertEquals(0, ga.getStats().getPageViewHits());
        assertEquals(0, ga.getStats().getScreenViewHits());
        assertEquals(0, ga.getStats().getItemHits());

        ga.getConfig().setGatherStats(true);
        ga.resetStats();
        ga.pageView().send();
        ga.pageView().send();
        ga.pageView().send();
        ga.screenView().send();
        ga.screenView().send();
        ga.item().send();

        assertEquals(3, ga.getStats().getPageViewHits());
        assertEquals(2, ga.getStats().getScreenViewHits());
        assertEquals(1, ga.getStats().getItemHits());
    }

    @Test
    public void testCustomDimensions() throws Exception {
        DefaultRequest defaultRequest = new DefaultRequest();
        defaultRequest.customDimension(1, "foo");
        defaultRequest.customDimension(5, "bar");

        // Local ga
        GoogleAnalytics lga = getTestBuilder().withDefaultRequest(defaultRequest).withTrackingId(TEST_TRACKING_ID).build();

        GoogleAnalyticsResponse response = lga.pageView("http://www.google.com", "Search").customDimension(2, "bob").customDimension(5, "alice")
                .send();

        assertEquals("foo", response.getRequestParams().get("cd1"));
        assertEquals("bob", response.getRequestParams().get("cd2"));
        assertEquals("alice", response.getRequestParams().get("cd5"));
    }

    @Test
    public void testCustomMetrics() throws Exception {
        DefaultRequest defaultRequest = new DefaultRequest();
        defaultRequest.customMetric(1, "foo");
        defaultRequest.customMetric(5, "bar");

        GoogleAnalytics lga = getTestBuilder().withDefaultRequest(defaultRequest).withTrackingId(TEST_TRACKING_ID).build();

        GoogleAnalyticsResponse response = lga.pageView("http://www.google.com", "Search").customMetric(2, "bob").customMetric(5, "alice").send();

        assertEquals("foo", response.getRequestParams().get("cm1"));
        assertEquals("bob", response.getRequestParams().get("cm2"));
        assertEquals("alice", response.getRequestParams().get("cm5"));
    }

    @Test
    public void testUserIpAndAgent() throws Exception {
        DefaultRequest defaultRequest = new DefaultRequest();
        defaultRequest.userIp("1.2.3.4");
        defaultRequest.userAgent("Opera/9.80 (Windows NT 6.0) Presto/2.12.388 Version/12.14");

        GoogleAnalytics lga = getTestBuilder().withDefaultRequest(defaultRequest).withTrackingId(TEST_TRACKING_ID).build();

        GoogleAnalyticsResponse response = lga.pageView("http://www.google.com", "Search").userIp("1.2.3.5")
                .userAgent("Chrome/9.80 (Windows NT 6.0) Presto/2.12.388 Version/12.14").send();

        assertEquals("1.2.3.5", response.getRequestParams().get("uip"));
        assertEquals("Chrome/9.80 (Windows NT 6.0) Presto/2.12.388 Version/12.14", response.getRequestParams().get("ua"));
    }

    @Test
    public void testUserDetails() throws Exception {
        GoogleAnalyticsResponse response = ga.pageView("http://www.google.com", "Search").send();
        assertNotNull(response.getRequestParams().get("cid"));

        DefaultRequest defaultRequest = new DefaultRequest();
        defaultRequest.clientId("1234");
        defaultRequest.userId("user1");

        GoogleAnalytics lga = getTestBuilder().withDefaultRequest(defaultRequest).withTrackingId(TEST_TRACKING_ID).build();

        response = lga.pageView("http://www.google.com", "Search").send();
        assertEquals("1234", response.getRequestParams().get("cid"));
        assertEquals("user1", response.getRequestParams().get("uid"));

        response = lga.pageView("http://www.google.com", "Search").clientId("12345").userId("user2").send();
        assertEquals("12345", response.getRequestParams().get("cid"));
        assertEquals("user2", response.getRequestParams().get("uid"));
    }
}
