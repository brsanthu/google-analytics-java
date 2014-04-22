package com.brsanthu.googleanalytics;

import static org.junit.Assert.*;

import org.junit.Test;

public class GaUtilsTest {
	
	@Test
	public void testIsEmpty() throws Exception {
		assertEquals(true, GaUtils.isEmpty(null));
		assertEquals(true, GaUtils.isEmpty(""));
		assertEquals(true, GaUtils.isEmpty(" "));
		assertEquals(false, GaUtils.isEmpty("value"));
		assertEquals(false, GaUtils.isEmpty(" value "));
	}
	
	@Test
	public void isNotEmpty() throws Exception {
		assertEquals(false, GaUtils.isNotEmpty(null));
		assertEquals(false, GaUtils.isNotEmpty(""));
		assertEquals(false, GaUtils.isNotEmpty(" "));
		assertEquals(true, GaUtils.isNotEmpty("value"));
		assertEquals(true, GaUtils.isNotEmpty(" value "));
	}
	
	@Test
	public void testAppendSystemProperty() throws Exception {
		System.setProperty("test", "test");
		assertEquals("", GaUtils.appendSystemProperty(new StringBuilder(), "nonexistent").toString());
		assertEquals("test", GaUtils.appendSystemProperty(new StringBuilder(), "test").toString());
		assertEquals("foo/test", GaUtils.appendSystemProperty(new StringBuilder("foo"), "test").toString());
	}
}
