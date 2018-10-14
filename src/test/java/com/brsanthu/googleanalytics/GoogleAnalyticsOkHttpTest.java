package com.brsanthu.googleanalytics;

import static com.brsanthu.googleanalytics.internal.Constants.TEST_TRACKING_ID;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brsanthu.googleanalytics.httpclient.OkHttpClientImpl;

public class GoogleAnalyticsOkHttpTest extends GoogleAnalyticsApacheHttpTest {
    private static final Logger logger = LoggerFactory.getLogger(GoogleAnalyticsOkHttpTest.class);

    @BeforeClass
    public static void setup() {
	    logger.debug("setup() starting");
		ga = new GoogleAnalyticsOkHttpTest().getTestBuilder().build();
	}
    
    @Override
    protected GoogleAnalyticsBuilder getTestBuilder() {
        return GoogleAnalytics.builder()
                .withTrackingId(TEST_TRACKING_ID)
                .withAppName("Junit OkHttp Test")
                .withAppVersion("1.0.0")
                .withHttpClient(new OkHttpClientImpl(new GoogleAnalyticsConfig()));
    }
}
