/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    private AtomicLong appViewHits = new AtomicLong();
    private AtomicLong itemHits = new AtomicLong();
    private AtomicLong transactionHits = new AtomicLong();
    private AtomicLong timingHits = new AtomicLong();
    private AtomicLong socialHits = new AtomicLong();

    public void pageViewHit() {
        pageViewHits.incrementAndGet();
    }

    public void eventHit() {
        eventHits.incrementAndGet();
    }

    public void appViewHit() {
        appViewHits.incrementAndGet();
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

    /* (non-Javadoc)
     * @see com.brsanthu.googleanalytics.internal.GoogleAnalyticsStats#getPageViewHits()
     */
    @Override
    public long getPageViewHits() {
        return pageViewHits.get();
    }

    /* (non-Javadoc)
     * @see com.brsanthu.googleanalytics.internal.GoogleAnalyticsStats#getEventHits()
     */
    @Override
    public long getEventHits() {
        return eventHits.get();
    }

    /* (non-Javadoc)
     * @see com.brsanthu.googleanalytics.internal.GoogleAnalyticsStats#getAppViewHits()
     */
    @Override
    public long getAppViewHits() {
        return appViewHits.get();
    }

    /* (non-Javadoc)
     * @see com.brsanthu.googleanalytics.internal.GoogleAnalyticsStats#getItemHits()
     */
    @Override
    public long getItemHits() {
        return itemHits.get();
    }

    /* (non-Javadoc)
     * @see com.brsanthu.googleanalytics.internal.GoogleAnalyticsStats#getTransactionHits()
     */
    @Override
    public long getTransactionHits() {
        return transactionHits.get();
    }

    /* (non-Javadoc)
     * @see com.brsanthu.googleanalytics.internal.GoogleAnalyticsStats#getTimingHits()
     */
    @Override
    public long getTimingHits() {
        return timingHits.get();
    }

    /* (non-Javadoc)
     * @see com.brsanthu.googleanalytics.internal.GoogleAnalyticsStats#getSocialHits()
     */
    @Override
    public long getSocialHits() {
        return socialHits.get();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GoogleAnalyticsStats [");
        if (pageViewHits != null) {
            builder.append("pageViewHits=");
            builder.append(pageViewHits);
            builder.append(", ");
        }
        if (eventHits != null) {
            builder.append("eventHits=");
            builder.append(eventHits);
            builder.append(", ");
        }
        if (appViewHits != null) {
            builder.append("appViewHits=");
            builder.append(appViewHits);
            builder.append(", ");
        }
        if (itemHits != null) {
            builder.append("itemHits=");
            builder.append(itemHits);
            builder.append(", ");
        }
        if (transactionHits != null) {
            builder.append("transactionHits=");
            builder.append(transactionHits);
            builder.append(", ");
        }
        if (timingHits != null) {
            builder.append("timingHits=");
            builder.append(timingHits);
            builder.append(", ");
        }
        if (socialHits != null) {
            builder.append("socialHits=");
            builder.append(socialHits);
        }
        builder.append("]");
        return builder.toString();
    }
}
