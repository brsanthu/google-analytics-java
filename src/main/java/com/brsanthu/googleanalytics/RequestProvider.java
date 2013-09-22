package com.brsanthu.googleanalytics;

public interface RequestProvider {
	@SuppressWarnings("rawtypes")
	AbstractRequest getRequest();
}
