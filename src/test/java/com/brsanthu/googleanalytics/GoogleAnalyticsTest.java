package com.brsanthu.googleanalytics;

import static com.brsanthu.googleanalytics.internal.Constants.TEST_TRACKING_ID;
import static com.brsanthu.googleanalytics.request.GoogleAnalyticsParameter.QUEUE_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import com.brsanthu.googleanalytics.httpclient.HttpClient;
import com.brsanthu.googleanalytics.httpclient.HttpResponse;
import com.brsanthu.googleanalytics.request.AnyHit;
import com.brsanthu.googleanalytics.request.DefaultRequest;
import com.brsanthu.googleanalytics.request.GoogleAnalyticsParameter;
import com.brsanthu.googleanalytics.request.GoogleAnalyticsRequest;
import com.brsanthu.googleanalytics.request.GoogleAnalyticsResponse;
import com.brsanthu.googleanalytics.request.ScreenViewHit;

public class GoogleAnalyticsTest {

    private static GoogleAnalytics ga = null;
    private static HttpClient client;

    @BeforeAll
    public static void setup() {
        ga = GoogleAnalytics.builder().withTrackingId(TEST_TRACKING_ID).withAppName("Junit Test").withAppVersion("1.0.0").build();
        client = mock(HttpClient.class);
        when(client.post(ArgumentMatchers.any())).thenReturn(new HttpResponse().setStatusCode(200));
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

    @Test
    void testAutoQueueTime() throws Exception {
        // By default queue time is added based on when hit was created and posted. In this test case, it should be
        // close to 0
        GoogleAnalytics gaAutoTimeEnabled = GoogleAnalytics.builder().withHttpClient(client).withConfig(new GoogleAnalyticsConfig()).build();
        GoogleAnalyticsResponse respEnabled = gaAutoTimeEnabled.screenView().send();
        assertThat(Integer.parseInt(respEnabled.getRequestParams().get(GoogleAnalyticsParameter.QUEUE_TIME.getParameterName()))).isCloseTo(0,
                within(100));

        // We can set occurred at to a past value and if so, it will be used to calcualte queue time.
        GoogleAnalytics gaAutoTimeEnabledOccurredAt = GoogleAnalytics.builder().withHttpClient(client).withConfig(
                new GoogleAnalyticsConfig()).build();
        GoogleAnalyticsResponse respEnabledOccurredAt = gaAutoTimeEnabledOccurredAt.screenView().occurredAt(
                ZonedDateTime.now().minusSeconds(5)).send();
        assertThat(Integer.parseInt(respEnabledOccurredAt.getRequestParams().get(GoogleAnalyticsParameter.QUEUE_TIME.getParameterName()))).isCloseTo(
                5000, within(100));

        // We can set both occurredAt and queue time, then time based on occurred at will be added to set queue time.
        GoogleAnalyticsResponse respEnabledWithSetQueueTime = gaAutoTimeEnabled.screenView().occurredAt(
                ZonedDateTime.now().minusSeconds(5)).queueTime(1000).send();
        assertThat(Integer.parseInt(
                respEnabledWithSetQueueTime.getRequestParams().get(GoogleAnalyticsParameter.QUEUE_TIME.getParameterName()))).isCloseTo(6000,
                        within(100));

        // If we disable auto queue time, then queue time is not calculated
        GoogleAnalytics gaAutoTimeDisabled = GoogleAnalytics.builder().withHttpClient(client).withConfig(
                new GoogleAnalyticsConfig().setAutoQueueTimeEnabled(false)).build();
        GoogleAnalyticsResponse respDisabled = gaAutoTimeDisabled.screenView().occurredAt(ZonedDateTime.now().minusSeconds(5)).send();
        assertThat(respDisabled.getRequestParams().get(GoogleAnalyticsParameter.QUEUE_TIME.getParameterName())).isNull();
    }

    @Test
    void testAutoQueueTimeBatch() throws Exception {
        GoogleAnalytics gaAutoTimeEnabled = GoogleAnalytics.builder().withHttpClient(client).withConfig(
                new GoogleAnalyticsConfig().setBatchingEnabled(true).setBatchSize(2)).build();

        // First request will add to batch.
        GoogleAnalyticsResponse resp1 = gaAutoTimeEnabled.screenView().send();

        Thread.sleep(500);

        GoogleAnalyticsResponse resp2 = gaAutoTimeEnabled.screenView().send();

        assertThat(Integer.parseInt(resp1.getRequestParams().get(QUEUE_TIME.getParameterName()))).isCloseTo(500, within(100));

        assertThat(Integer.parseInt(resp2.getRequestParams().get(QUEUE_TIME.getParameterName()))).isCloseTo(0, within(100));
    }

    @Test
    void testDeepClone() throws Exception {
        ScreenViewHit screenhit1 = ga.screenView().adwordsId("test1");
        ScreenViewHit screenhit2 = screenhit1.clone();

        screenhit1.adwordsId("test1updated");

        assertThat(screenhit1).isNotSameAs(screenhit2);
        assertThat(screenhit1.adwordsId()).isEqualTo("test1updated");
        assertThat(screenhit2.adwordsId()).isEqualTo("test1");
    }

    @Test
    void testStats() throws Exception {
        GoogleAnalytics ga = GoogleAnalytics.builder().withHttpClient(client).withConfig(
                new GoogleAnalyticsConfig().setGatherStats(true).setBatchingEnabled(true).setBatchSize(2)).build();

        IntStream.rangeClosed(1, 1).forEach(val -> ga.pageView().send());
        IntStream.rangeClosed(1, 2).forEach(val -> ga.event().send());
        IntStream.rangeClosed(1, 3).forEach(val -> ga.screenView().send());
        IntStream.rangeClosed(1, 4).forEach(val -> ga.item().send());
        IntStream.rangeClosed(1, 5).forEach(val -> ga.transaction().send());
        IntStream.rangeClosed(1, 6).forEach(val -> ga.timing().send());
        IntStream.rangeClosed(1, 7).forEach(val -> ga.social().send());
        IntStream.rangeClosed(1, 8).forEach(val -> ga.exception().send());

        assertThat(ga.getStats().getPageViewHits()).isEqualTo(1);
        assertThat(ga.getStats().getEventHits()).isEqualTo(2);
        assertThat(ga.getStats().getScreenViewHits()).isEqualTo(3);
        assertThat(ga.getStats().getItemHits()).isEqualTo(4);
        assertThat(ga.getStats().getTransactionHits()).isEqualTo(5);
        assertThat(ga.getStats().getTimingHits()).isEqualTo(6);
        assertThat(ga.getStats().getSocialHits()).isEqualTo(7);
        assertThat(ga.getStats().getExceptionHits()).isEqualTo(8);
        assertThat(ga.getStats().getTotalHits()).isEqualTo(36);
    }

    @Test
    void testAnyHit() throws Exception {
        ScreenViewHit sv = ga.screenView().adwordsId("adsid123");

        AnyHit anyhit1 = sv.asAnyHit();
        anyhit1.adwordsId("adsid456");
        assertThat(sv.adwordsId()).isEqualTo(anyhit1.adwordsId());

        AnyHit anyhit2 = sv.clone().asAnyHit();
        anyhit2.adwordsId("adsid789");
        assertThat(sv.adwordsId()).isNotEqualTo(anyhit2.adwordsId());
    }

    @Test
    void testAnonymizeUserIp() throws Exception {
        String userIp = "176.134.201.004";

        GoogleAnalytics ga = GoogleAnalytics.builder().withHttpClient(client).withConfig(new GoogleAnalyticsConfig()).build();
        GoogleAnalyticsResponse resp = ga.screenView().userIp(userIp).send();
        assertThat(resp.getRequestParams().get(GoogleAnalyticsParameter.USER_IP.getParameterName())).isEqualTo(userIp);

        GoogleAnalytics ga2 = GoogleAnalytics.builder().withHttpClient(client).withConfig(
                new GoogleAnalyticsConfig().setAnonymizeUserIp(true)).build();
        GoogleAnalyticsResponse resp2 = ga2.screenView().userIp(userIp).send();
        assertThat(resp2.getRequestParams().get(GoogleAnalyticsParameter.USER_IP.getParameterName())).isEqualTo("176.134.201.0");
    }

    @Test
    void testCustomExecutor() throws Exception {
        GoogleAnalyticsExecutor exector = new GoogleAnalyticsExecutor() {
            @Override
            public Future<GoogleAnalyticsResponse> postAsync(GoogleAnalyticsRequest<?> request) {
                return null;
            }

            @Override
            public GoogleAnalyticsResponse post(GoogleAnalyticsRequest<?> request) {
                return new GoogleAnalyticsResponse().setStatusCode(400);
            }
        };

        GoogleAnalytics ga = GoogleAnalytics.builder().withGoogleAnalyticsExecutor(exector).build();
        GoogleAnalyticsResponse resp = ga.screenView().send();
        assertThat(resp.getStatusCode()).isEqualTo(400);

    }
}
