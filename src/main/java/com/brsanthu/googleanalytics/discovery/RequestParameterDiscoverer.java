package com.brsanthu.googleanalytics.discovery;

import com.brsanthu.googleanalytics.GoogleAnalyticsConfig;
import com.brsanthu.googleanalytics.request.DefaultRequest;

/**
 * Mechanism to discover some default request parameters.
 */
public interface RequestParameterDiscoverer {

    public DefaultRequest discoverParameters(GoogleAnalyticsConfig config, DefaultRequest request);

}
