package com.brsanthu.googleanalytics;

import static com.brsanthu.googleanalytics.internal.Constants.TEST_TRACKING_ID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import com.brsanthu.googleanalytics.httpclient.HttpClient;
import com.brsanthu.googleanalytics.request.DefaultRequest;
import com.brsanthu.googleanalytics.request.GoogleAnalyticsResponse;

public class GoogleAnalyticsTest {

    private static GoogleAnalytics ga = null;

    @BeforeAll
    public static void setup() {
        ga = GoogleAnalytics.builder().withTrackingId(TEST_TRACKING_ID).withAppName("Junit Test").withAppVersion("1.0.0").build();
    }

    @Test
    public void testPageView() throws Exception {
        ga.pageView("http://www.google.com", "Search").send();
        ga.pageView("http://www.google.com", "Search").sendAsync();
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
        GoogleAnalytics lga = GoogleAnalytics.builder().withDefaultRequest(defaultRequest).withTrackingId(TEST_TRACKING_ID).build();

        GoogleAnalyticsResponse response = lga.pageView("http://www.google.com", "Search").customDimension(2, "bob").customDimension(5,
                "alice").send();

        assertEquals("foo", response.getRequestParams().get("cd1"));
        assertEquals("bob", response.getRequestParams().get("cd2"));
        assertEquals("alice", response.getRequestParams().get("cd5"));
    }

    @Test
    public void testCustomMetrics() throws Exception {
        DefaultRequest defaultRequest = new DefaultRequest();
        defaultRequest.customMetric(1, "foo");
        defaultRequest.customMetric(5, "bar");

        GoogleAnalytics lga = GoogleAnalytics.builder().withDefaultRequest(defaultRequest).withTrackingId(TEST_TRACKING_ID).build();

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

        GoogleAnalytics lga = GoogleAnalytics.builder().withDefaultRequest(defaultRequest).withTrackingId(TEST_TRACKING_ID).build();

        GoogleAnalyticsResponse response = lga.pageView("http://www.google.com", "Search").userIp("1.2.3.5").userAgent(
                "Chrome/9.80 (Windows NT 6.0) Presto/2.12.388 Version/12.14").send();

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

        GoogleAnalytics lga = GoogleAnalytics.builder().withDefaultRequest(defaultRequest).withTrackingId(TEST_TRACKING_ID).build();

        response = lga.pageView("http://www.google.com", "Search").send();
        assertEquals("1234", response.getRequestParams().get("cid"));
        assertEquals("user1", response.getRequestParams().get("uid"));

        response = lga.pageView("http://www.google.com", "Search").clientId("12345").userId("user2").send();
        assertEquals("12345", response.getRequestParams().get("cid"));
        assertEquals("user2", response.getRequestParams().get("uid"));
    }

    @Test
    void testExceptionHandler() throws Exception {
        HttpClient client = mock(HttpClient.class);
        when(client.post(ArgumentMatchers.any())).thenThrow(new RuntimeException("Testing Exception"));

        GoogleAnalytics ga = GoogleAnalytics.builder().withHttpClient(client).withConfig(new GoogleAnalyticsConfig()).build();

        // Since default behavior is to log exception, this function call should work fine.
        ga.screenView().send();

        GoogleAnalytics propagatingGa = GoogleAnalytics.builder().withHttpClient(client).withConfig(
                new GoogleAnalyticsConfig().setExceptionHandler(new PropagatingExceptionHandler())).build();

        assertThatThrownBy(() -> propagatingGa.screenView().send()).hasMessage("Testing Exception");

        assertThatThrownBy(() -> propagatingGa.screenView().sendAsync().get()).hasMessageContaining("Testing Exception");
    }
}
