package com.brsanthu.googleanalytics;

/**
 * Exception handler which propagates the exceptions instead of just logging.
 */
public class PropagatingExceptionHandler implements GoogleAnalyticsExceptionHandler {

    @Override
    public void handle(Throwable t) {
        if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        }

        throw new GoogleAnalyticsException(t);
    }
}
