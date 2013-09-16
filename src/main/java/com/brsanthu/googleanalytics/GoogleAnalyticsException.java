package com.brsanthu.googleanalytics;

public class GoogleAnalyticsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GoogleAnalyticsException() {
		super();
	}

	public GoogleAnalyticsException(String message, Throwable cause) {
		super(message, cause);
	}

	public GoogleAnalyticsException(String message) {
		super(message);
	}

	public GoogleAnalyticsException(Throwable cause) {
		super(cause);
	}
}
