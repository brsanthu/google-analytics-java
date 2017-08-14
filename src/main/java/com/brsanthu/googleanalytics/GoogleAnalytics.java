package com.brsanthu.googleanalytics;

import com.brsanthu.googleanalytics.request.AppViewHit;
import com.brsanthu.googleanalytics.request.EventHit;
import com.brsanthu.googleanalytics.request.ExceptionHit;
import com.brsanthu.googleanalytics.request.ItemHit;
import com.brsanthu.googleanalytics.request.PageViewHit;
import com.brsanthu.googleanalytics.request.SocialHit;
import com.brsanthu.googleanalytics.request.TimingHit;
import com.brsanthu.googleanalytics.request.TransactionHit;

public interface GoogleAnalytics extends AutoCloseable {

    AppViewHit appView();

    EventHit event();

    ExceptionHit exception();

    ItemHit item();

    PageViewHit pageView();

    PageViewHit pageView(String url, String title);

    PageViewHit pageView(String url, String title, String description);

    SocialHit social();

    SocialHit social(String socialNetwork, String socialAction, String socialTarget);

    TimingHit timing();

    TransactionHit transaction();

    GoogleAnalyticsStats getStats();

    GoogleAnalyticsConfig getConfig();

    void resetStats();

    static GoogleAnalyticsBuilder builder() {
        return new GoogleAnalyticsBuilder();
    }
}