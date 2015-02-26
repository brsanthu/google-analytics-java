package com.brsanthu.googleanalytics;

import static org.junit.Assert.*;

import org.junit.Test;

public class GoogleAnalyticsParameterTest {

	@Test
	public void testParameters() throws Exception {
		assertParameter("v", true, "text", null, 0, GoogleAnalyticsParameter.PROTOCOL_VERSION);
		assertParameter("tid", true, "text", null, 0, GoogleAnalyticsParameter.TRACKING_ID);
		assertParameter("aip", false, "boolean", null, 0, GoogleAnalyticsParameter.ANONYMIZE_IP);
		assertParameter("qt", false, "integer", null, 0, GoogleAnalyticsParameter.QUEUE_TIME);
		assertParameter("z", false, "text", null, 0, GoogleAnalyticsParameter.CACHE_BUSTER);
		
		assertParameter("cid", true, "text", null, 0, GoogleAnalyticsParameter.CLIENT_ID);
		assertParameter("uid", false, "text", null, 0, GoogleAnalyticsParameter.USER_ID);
		
		assertParameter("sc", false, "text", null, 0, GoogleAnalyticsParameter.SESSION_CONTROL);
		assertParameter("uip", false, "text", null, 0, GoogleAnalyticsParameter.USER_IP);
		assertParameter("ua", false, "text", null, 0, GoogleAnalyticsParameter.USER_AGENT);
		assertParameter("aid", false, "text", null, 150, GoogleAnalyticsParameter.APPLICATION_ID);
		
		assertParameter("ds", false, "text", null, 0, GoogleAnalyticsParameter.DATA_SOURCE);
	}
	
	public void testParametersOld() throws Exception {
		
		assertEquals("v", GoogleAnalyticsParameter.PROTOCOL_VERSION.getParameterName());
		assertEquals("gclid", GoogleAnalyticsParameter.ADWORDS_ID.getParameterName());
		assertEquals("aip", GoogleAnalyticsParameter.ANONYMIZE_IP.getParameterName());
		assertEquals("an", GoogleAnalyticsParameter.APPLICATION_NAME.getParameterName());
		assertEquals("av", GoogleAnalyticsParameter.APPLICATION_VERSION.getParameterName());
		assertEquals("z", GoogleAnalyticsParameter.CACHE_BUSTER.getParameterName());
		assertEquals("cc", GoogleAnalyticsParameter.CAMPAIGN_CONTENT.getParameterName());
		assertEquals("ci", GoogleAnalyticsParameter.CAMPAIGN_ID.getParameterName());
		assertEquals("ck", GoogleAnalyticsParameter.CAMPAIGN_KEYWORD.getParameterName());
		assertEquals("cm", GoogleAnalyticsParameter.CAMPAIGN_MEDIUM.getParameterName());
		assertEquals("cn", GoogleAnalyticsParameter.CAMPAIGN_NAME.getParameterName());
		assertEquals("cs", GoogleAnalyticsParameter.CAMPAIGN_SOURCE.getParameterName());
		assertEquals("cid", GoogleAnalyticsParameter.CLIENT_ID.getParameterName());
		assertEquals("uid", GoogleAnalyticsParameter.USER_ID.getParameterName());
		assertEquals("cd", GoogleAnalyticsParameter.CONTENT_DESCRIPTION.getParameterName());
		assertEquals("cu", GoogleAnalyticsParameter.CURRENCY_CODE.getParameterName());
		assertEquals("dclid", GoogleAnalyticsParameter.DISPLAY_ADS_ID.getParameterName());
		assertEquals("dns", GoogleAnalyticsParameter.DNS_TIME.getParameterName());
		assertEquals("de", GoogleAnalyticsParameter.DOCUMENT_ENCODING.getParameterName());
		assertEquals("dh", GoogleAnalyticsParameter.DOCUMENT_HOST_NAME.getParameterName());
		assertEquals("dl", GoogleAnalyticsParameter.DOCUMENT_URL.getParameterName());
		assertEquals("dp", GoogleAnalyticsParameter.DOCUMENT_PATH.getParameterName());
		assertEquals("dr", GoogleAnalyticsParameter.DOCUMENT_REFERRER.getParameterName());
		assertEquals("dt", GoogleAnalyticsParameter.DOCUMENT_TITLE.getParameterName());
		assertEquals("ea", GoogleAnalyticsParameter.EVENT_ACTION.getParameterName());
		assertEquals("ec", GoogleAnalyticsParameter.EVENT_CATEGORY.getParameterName());
		assertEquals("el", GoogleAnalyticsParameter.EVENT_LABEL.getParameterName());
		assertEquals("ev", GoogleAnalyticsParameter.EVENT_VALUE.getParameterName());
		assertEquals("exd", GoogleAnalyticsParameter.EXCEPTION_DESCRIPTION.getParameterName());
		assertEquals("exf", GoogleAnalyticsParameter.EXCEPTION_FATAL.getParameterName());
		assertEquals("fl", GoogleAnalyticsParameter.FLASH_VERSION.getParameterName());
		assertEquals("t", GoogleAnalyticsParameter.HIT_TYPE.getParameterName());
		assertEquals("iv", GoogleAnalyticsParameter.ITEM_CATEGORY.getParameterName());
		assertEquals("ic", GoogleAnalyticsParameter.ITEM_CODE.getParameterName());
		assertEquals("in", GoogleAnalyticsParameter.ITEM_NAME.getParameterName());
		assertEquals("ip", GoogleAnalyticsParameter.ITEM_PRICE.getParameterName());
		assertEquals("iq", GoogleAnalyticsParameter.ITEM_QUANTITY.getParameterName());
		assertEquals("je", GoogleAnalyticsParameter.JAVA_ENABLED.getParameterName());
		assertEquals("ni", GoogleAnalyticsParameter.NON_INTERACTION_HIT.getParameterName());
		assertEquals("pdt", GoogleAnalyticsParameter.PAGE_DOWNLOAD_TIME.getParameterName());
		assertEquals("plt", GoogleAnalyticsParameter.PAGE_LOAD_TIME.getParameterName());
		assertEquals("qt", GoogleAnalyticsParameter.QUEUE_TIME.getParameterName());
		assertEquals("rrt", GoogleAnalyticsParameter.REDIRECT_RESPONSE_TIME.getParameterName());
		assertEquals("sd", GoogleAnalyticsParameter.SCREEN_COLORS.getParameterName());
		assertEquals("sr", GoogleAnalyticsParameter.SCREEN_RESOLUTION.getParameterName());
		assertEquals("srt", GoogleAnalyticsParameter.SERVER_RESPONSE_TIME.getParameterName());
		assertEquals("sc", GoogleAnalyticsParameter.SESSION_CONTROL.getParameterName());
		assertEquals("sa", GoogleAnalyticsParameter.SOCIAL_ACTION.getParameterName());
		assertEquals("st", GoogleAnalyticsParameter.SOCIAL_ACTION_TARGET.getParameterName());
		assertEquals("sn", GoogleAnalyticsParameter.SOCIAL_NETWORK.getParameterName());
		assertEquals("tcp", GoogleAnalyticsParameter.TCP_CONNECT_TIME.getParameterName());
		assertEquals("tid", GoogleAnalyticsParameter.TRACKING_ID.getParameterName());
		assertEquals("ta", GoogleAnalyticsParameter.TRANSACTION_AFFILIATION.getParameterName());
		assertEquals("ti", GoogleAnalyticsParameter.TRANSACTION_ID.getParameterName());
		assertEquals("tr", GoogleAnalyticsParameter.TRANSACTION_REVENUE.getParameterName());
		assertEquals("ts", GoogleAnalyticsParameter.TRANSACTION_SHIPPING.getParameterName());
		assertEquals("tt", GoogleAnalyticsParameter.TRANSACTION_TAX.getParameterName());
		assertEquals("ul", GoogleAnalyticsParameter.USER_LANGUAGE.getParameterName());
		assertEquals("utc", GoogleAnalyticsParameter.USER_TIMING_CATEGORY.getParameterName());
		assertEquals("utl", GoogleAnalyticsParameter.USER_TIMING_LABEL.getParameterName());
		assertEquals("utt", GoogleAnalyticsParameter.USER_TIMING_TIME.getParameterName());
		assertEquals("utv", GoogleAnalyticsParameter.USER_TIMING_VARIABLE_NAME.getParameterName());
		assertEquals("vp", GoogleAnalyticsParameter.VIEWPORT_SIZE.getParameterName());
		assertEquals("uip", GoogleAnalyticsParameter.USER_ID.getParameterName());
		assertEquals("ua", GoogleAnalyticsParameter.USER_AGENT.getParameterName());
		
		assertEquals("xid", GoogleAnalyticsParameter.EXPERIMENT_ID.getParameterName());
		assertNull(GoogleAnalyticsParameter.EXPERIMENT_ID.getSupportedHitTypes());
		assertEquals("text", GoogleAnalyticsParameter.EXPERIMENT_ID.getType());
		
		assertEquals("xvar", GoogleAnalyticsParameter.EXPERIMENT_VARIANT.getParameterName());
		assertNull(GoogleAnalyticsParameter.EXPERIMENT_VARIANT.getSupportedHitTypes());
		assertEquals("text", GoogleAnalyticsParameter.EXPERIMENT_VARIANT.getType());
	}
	
	private void assertParameter(String name, boolean required, String type, String[] hitTypes, int maxLength, GoogleAnalyticsParameter param) {
		assertEquals(name, param.getParameterName());
		assertEquals(required, param.isRequired());
		assertEquals(type, param.getType());
		assertArrayEquals(hitTypes, param.getSupportedHitTypes());
		assertEquals(maxLength, param.getMaxLength());
	}
}
