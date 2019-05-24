/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.brsanthu.googleanalytics.internal;

import java.util.concurrent.atomic.AtomicLong;

import com.brsanthu.googleanalytics.GoogleAnalyticsStats;

/**
 * Collects the basic stats about successful events that have been posted to GA.
 *
 * @author Santhosh Kumar
 */
public class GoogleAnalyticsStatsImpl implements GoogleAnalyticsStats {
    private AtomicLong pageViewHits = new AtomicLong();
    private AtomicLong eventHits = new AtomicLong();
    private AtomicLong screenViewHits = new AtomicLong();
    private AtomicLong itemHits = new AtomicLong();
    private AtomicLong transactionHits = new AtomicLong();
    private AtomicLong timingHits = new AtomicLong();
    private AtomicLong socialHits = new AtomicLong();
    private AtomicLong exceptionHits = new AtomicLong();

    public void exceptionHit() {
        exceptionHits.incrementAndGet();
    }

    public void pageViewHit() {
        pageViewHits.incrementAndGet();
    }

    public void eventHit() {
        eventHits.incrementAndGet();
    }

    public void screenViewHit() {
        screenViewHits.incrementAndGet();
    }

    public void itemHit() {
        itemHits.incrementAndGet();
    }

    public void transactionHit() {
        transactionHits.incrementAndGet();
    }

    public void socialHit() {
        socialHits.incrementAndGet();
    }

    public void timingHit() {
        timingHits.incrementAndGet();
    }

    @Override
    public long getPageViewHits() {
        return pageViewHits.get();
    }

    @Override
    public long getEventHits() {
        return eventHits.get();
    }

    @Override
    public long getScreenViewHits() {
        return screenViewHits.get();
    }

    @Override
    public long getItemHits() {
        return itemHits.get();
    }

    @Override
    public long getTransactionHits() {
        return transactionHits.get();
    }

    @Override
    public long getTimingHits() {
        return timingHits.get();
    }

    @Override
    public long getSocialHits() {
        return socialHits.get();
    }

    @Override
    public long getExceptionHits() {
        return exceptionHits.get();
    }

    @Override
    public long getTotalHits() {
        return pageViewHits.get() + eventHits.get() + screenViewHits.get() + itemHits.get() + transactionHits.get() + timingHits.get()
                + socialHits.get() + exceptionHits.get();
    }

    @Override
    public String toString() {
        return "GoogleAnalyticsStatsImpl [pageViewHits=" + pageViewHits + ", eventHits=" + eventHits + ", screenViewHits=" + screenViewHits
                + ", itemHits=" + itemHits + ", transactionHits=" + transactionHits + ", timingHits=" + timingHits + ", socialHits=" + socialHits
                + ", exceptionHits=" + exceptionHits + "]";
    }
}
