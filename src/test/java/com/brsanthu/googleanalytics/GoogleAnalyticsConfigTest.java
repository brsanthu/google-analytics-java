package com.brsanthu.googleanalytics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GoogleAnalyticsConfigTest {

    @Test
    public void testDefaultConfig() throws Exception {
        GoogleAnalyticsConfig config = new GoogleAnalyticsConfig();

        assertEquals("googleanalytics-thread-{0}", config.getThreadNameFormat());
        assertEquals(1, config.getMaxThreads());
        assertEquals("http://www.google-analytics.com/collect", config.getHttpUrl());
        assertEquals("https://ssl.google-analytics.com/collect", config.getHttpsUrl());
        assertEquals(80, config.getProxyPort());
        assertEquals(true, config.isDiscoverRequestParameters());
        assertEquals(false, config.isGatherStats());
    }
}
