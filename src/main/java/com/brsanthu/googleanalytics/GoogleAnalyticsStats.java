package com.brsanthu.googleanalytics;

public interface GoogleAnalyticsStats {

	long getPageViewHits();

	long getEventHits();

	long getAppViewHits();

	long getItemHits();

	long getTransactionHits();

	long getTimingHits();

	long getSocialHits();

}