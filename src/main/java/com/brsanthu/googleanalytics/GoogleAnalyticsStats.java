package com.brsanthu.googleanalytics;

public interface GoogleAnalyticsStats {

    long getPageViewHits();

    long getEventHits();

    long getScreenViewHits();

    long getItemHits();

    long getTransactionHits();

    long getTimingHits();

    long getSocialHits();

    long getExceptionHits();
}