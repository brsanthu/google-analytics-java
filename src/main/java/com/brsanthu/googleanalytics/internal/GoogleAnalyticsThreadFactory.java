package com.brsanthu.googleanalytics.internal;

import java.text.MessageFormat;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class GoogleAnalyticsThreadFactory implements ThreadFactory {
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private String threadNameFormat = null;

	public GoogleAnalyticsThreadFactory(String threadNameFormat) {
		this.threadNameFormat = threadNameFormat;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(Thread.currentThread().getThreadGroup(), r, MessageFormat.format(threadNameFormat, threadNumber.getAndIncrement()),
				0);
		thread.setDaemon(true);
		thread.setPriority(Thread.MIN_PRIORITY);
		return thread;
	}
}
