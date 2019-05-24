package com.brsanthu.googleanalytics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class GoogleAnalyticsConfigTest {

    @Test
    public void testDefaultConfig() throws Exception {
        GoogleAnalyticsConfig config = new GoogleAnalyticsConfig();
        assertEquals("googleanalyticsjava-thread-{0}", config.getThreadNameFormat());
        assertEquals(0, config.getMinThreads());
        assertEquals(5, config.getMaxThreads());
        assertEquals("http://www.google-analytics.com/collect", config.getHttpUrl());
        assertEquals("https://www.google-analytics.com/collect", config.getHttpsUrl());
        assertEquals("http://www.google-analytics.com/debug/collect", config.getHttpDebugUrl());
        assertEquals("https://www.google-analytics.com/debug/collect", config.getHttpsDebugUrl());
        assertEquals(80, config.getProxyPort());
        assertEquals(true, config.isDiscoverRequestParameters());
        assertEquals(false, config.isGatherStats());
    }

    @Test
    void testGetUrl() throws Exception {

        GoogleAnalyticsConfig config = new GoogleAnalyticsConfig();

        assertThat(config.getUrl()).isEqualTo("https://www.google-analytics.com/collect");
        config.setHitDebug(true);
        assertThat(config.getUrl()).isEqualTo("https://www.google-analytics.com/debug/collect");

        config.setHitDebug(false);
        config.setUseHttps(false);

        assertThat(config.getUrl()).isEqualTo("http://www.google-analytics.com/collect");
        config.setHitDebug(true);
        assertThat(config.getUrl()).isEqualTo("http://www.google-analytics.com/debug/collect");
    }

}
