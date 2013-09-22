package com.brsanthu.googleanalytics;

/**
 * Interface which provides the request that needs to be sent to GA. This type helps creating the
 * delayed GA request inside the async thread so the cost of constructing the Request is part of
 * user related thread or cost is completely avoided if GA is disabled
 * (via {@link GoogleAnalyticsConfig.setEnabled)
 *
 * @author Santhosh Kumar
 */
public interface RequestProvider {

	/**
	 * Constructs and returns the request, that should be sent to GA. If this method throws exception,
	 * nothing will be sent to GA.
	 *
	 * @return the request that must be sent to GA. Can return <code>null</code> and if so,
	 * 		nothing will be sent to GA.
	 */
	@SuppressWarnings("rawtypes")
	AbstractRequest getRequest();
}
