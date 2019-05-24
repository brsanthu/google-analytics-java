package com.brsanthu.googleanalytics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import com.brsanthu.googleanalytics.request.AnyHit;
import com.brsanthu.googleanalytics.request.EventHit;
import com.brsanthu.googleanalytics.request.ExceptionHit;
import com.brsanthu.googleanalytics.request.ItemHit;
import com.brsanthu.googleanalytics.request.PageViewHit;
import com.brsanthu.googleanalytics.request.ScreenViewHit;
import com.brsanthu.googleanalytics.request.SocialHit;
import com.brsanthu.googleanalytics.request.TimingHit;
import com.brsanthu.googleanalytics.request.TransactionHit;

public class HitTypesTest {

    @Test
    public void testHitTypes() throws Exception {
        assertEquals("item", new ItemHit().hitType());
        assertEquals("screenview", new ScreenViewHit().hitType());
        assertEquals("event", new EventHit().hitType());
        assertEquals("exception", new ExceptionHit().hitType());
        assertEquals("pageview", new PageViewHit().hitType());
        assertEquals("social", new SocialHit().hitType());
        assertEquals("timing", new TimingHit().hitType());
        assertEquals("transaction", new TransactionHit().hitType());
        assertThat(new AnyHit().hitType()).isEqualTo("pageview");
    }
}
