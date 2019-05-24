package com.brsanthu.googleanalytics;

/**
 * Models a handler which can be used to handle exceptions thrown while sending GA requests to google. This can be set
 * as config parameter as below.
 *
 * <pre>
 *  GoogleAnalytics propagatingGa = GoogleAnalytics.builder()withConfig(
 *      new GoogleAnalyticsConfig().setExceptionHandler(new YourExceptionHandler())
 *  ).build();
 * </pre>
 *
 * <p>
 * Library comes with following implementations.
 * <ul>
 * <li>{@link PropagatingExceptionHandler} which propagates the exceptions.
 * </ul>
 */
@FunctionalInterface
public interface GoogleAnalyticsExceptionHandler {
    void handle(Throwable t);
}
