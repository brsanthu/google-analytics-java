package com.brsanthu.googleanalytics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EventHitTest {
	
	@Test
	public void testEventHit() throws Exception {
		EventHit eventHit = new EventHit("eventCategory", "eventAction", "eventLabel", 10);
		assertEquals("eventCategory", eventHit.eventCategory());
		assertEquals("eventAction", eventHit.eventAction());
		assertEquals("eventLabel", eventHit.eventLabel());
		assertEquals(new Integer(10), eventHit.eventValue());
	}
}
