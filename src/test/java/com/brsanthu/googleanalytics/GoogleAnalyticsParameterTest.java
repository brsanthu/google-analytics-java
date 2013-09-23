package com.brsanthu.googleanalytics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GoogleAnalyticsParameterTest {

	@Test
	public void testParameters() throws Exception {
		assertEquals("gclid", GoogleAnalyticsParameter.ADWORDS_ID.getName());
		assertEquals("aip", GoogleAnalyticsParameter.ANONYMIZE_IP.getName());
		assertEquals("an", GoogleAnalyticsParameter.APPLICATION_NAME.getName());
		assertEquals("av", GoogleAnalyticsParameter.APPLICATION_VERSION.getName());
		assertEquals("z", GoogleAnalyticsParameter.CACHE_BUSTER.getName());
		assertEquals("cc", GoogleAnalyticsParameter.CAMPAIGN_CONTENT.getName());
		assertEquals("ci", GoogleAnalyticsParameter.CAMPAIGN_ID.getName());
		assertEquals("ck", GoogleAnalyticsParameter.CAMPAIGN_KEYWORD.getName());
		assertEquals("cm", GoogleAnalyticsParameter.CAMPAIGN_MEDIUM.getName());
		assertEquals("cn", GoogleAnalyticsParameter.CAMPAIGN_NAME.getName());
		assertEquals("cs", GoogleAnalyticsParameter.CAMPAIGN_SOURCE.getName());
		assertEquals("cid", GoogleAnalyticsParameter.CLIENT_ID.getName());
		assertEquals("cd", GoogleAnalyticsParameter.CONTENT_DESCRIPTION.getName());
		assertEquals("cu", GoogleAnalyticsParameter.CURRENCY_CODE.getName());
		assertEquals("dclid", GoogleAnalyticsParameter.DISPLAY_ADS_ID.getName());
		assertEquals("dns", GoogleAnalyticsParameter.DNS_TIME.getName());
		assertEquals("de", GoogleAnalyticsParameter.DOCUMENT_ENCODING.getName());
		assertEquals("dh", GoogleAnalyticsParameter.DOCUMENT_HOST_NAME.getName());
		assertEquals("dl", GoogleAnalyticsParameter.DOCUMENT_URL.getName());
		assertEquals("dp", GoogleAnalyticsParameter.DOCUMENT_PATH.getName());
		assertEquals("dr", GoogleAnalyticsParameter.DOCUMENT_REFERRER.getName());
		assertEquals("dt", GoogleAnalyticsParameter.DOCUMENT_TITLE.getName());
		assertEquals("ea", GoogleAnalyticsParameter.EVENT_ACTION.getName());
		assertEquals("ec", GoogleAnalyticsParameter.EVENT_CATEGORY.getName());
		assertEquals("el", GoogleAnalyticsParameter.EVENT_LABEL.getName());
		assertEquals("ev", GoogleAnalyticsParameter.EVENT_VALUE.getName());
		assertEquals("exd", GoogleAnalyticsParameter.EXCEPTION_DESCRIPTION.getName());
		assertEquals("exf", GoogleAnalyticsParameter.EXCEPTION_FATAL.getName());
		assertEquals("fl", GoogleAnalyticsParameter.FLASH_VERSION.getName());
		assertEquals("t", GoogleAnalyticsParameter.HIT_TYPE.getName());
		assertEquals("iv", GoogleAnalyticsParameter.ITEM_CATEGORY.getName());
		assertEquals("ic", GoogleAnalyticsParameter.ITEM_CODE.getName());
		assertEquals("in", GoogleAnalyticsParameter.ITEM_NAME.getName());
		assertEquals("ip", GoogleAnalyticsParameter.ITEM_PRICE.getName());
		assertEquals("iq", GoogleAnalyticsParameter.ITEM_QUANTITY.getName());
		assertEquals("je", GoogleAnalyticsParameter.JAVA_ENABLED.getName());
		assertEquals("ni", GoogleAnalyticsParameter.NON_INTERACTION_HIT.getName());
		assertEquals("pdt", GoogleAnalyticsParameter.PAGE_DOWNLOAD_TIME.getName());
		assertEquals("plt", GoogleAnalyticsParameter.PAGE_LOAD_TIME.getName());
		assertEquals("v", GoogleAnalyticsParameter.PROTOCOL_VERSION.getName());
		assertEquals("qt", GoogleAnalyticsParameter.QUEUE_TIME.getName());
		assertEquals("rrt", GoogleAnalyticsParameter.REDIRECT_RESPONSE_TIME.getName());
		assertEquals("sd", GoogleAnalyticsParameter.SCREEN_COLORS.getName());
		assertEquals("sr", GoogleAnalyticsParameter.SCREEN_RESOLUTION.getName());
		assertEquals("srt", GoogleAnalyticsParameter.SERVER_RESPONSE_TIME.getName());
		assertEquals("sc", GoogleAnalyticsParameter.SESSION_CONTROL.getName());
		assertEquals("sa", GoogleAnalyticsParameter.SOCIAL_ACTION.getName());
		assertEquals("st", GoogleAnalyticsParameter.SOCIAL_ACTION_TARGET.getName());
		assertEquals("sn", GoogleAnalyticsParameter.SOCIAL_NETWORK.getName());
		assertEquals("tcp", GoogleAnalyticsParameter.TCP_CONNECT_TIME.getName());
		assertEquals("tid", GoogleAnalyticsParameter.TRACKING_ID.getName());
		assertEquals("ta", GoogleAnalyticsParameter.TRANSACTION_AFFILIATION.getName());
		assertEquals("ti", GoogleAnalyticsParameter.TRANSACTION_ID.getName());
		assertEquals("tr", GoogleAnalyticsParameter.TRANSACTION_REVENUE.getName());
		assertEquals("ts", GoogleAnalyticsParameter.TRANSACTION_SHIPPING.getName());
		assertEquals("tt", GoogleAnalyticsParameter.TRANSACTION_TAX.getName());
		assertEquals("ul", GoogleAnalyticsParameter.USER_LANGUAGE.getName());
		assertEquals("utc", GoogleAnalyticsParameter.USER_TIMING_CATEGORY.getName());
		assertEquals("utl", GoogleAnalyticsParameter.USER_TIMING_LABEL.getName());
		assertEquals("utt", GoogleAnalyticsParameter.USER_TIMING_TIME.getName());
		assertEquals("utv", GoogleAnalyticsParameter.USER_TIMING_VARIABLE_NAME.getName());
		assertEquals("vp", GoogleAnalyticsParameter.VIEWPORT_SIZE.getName());
	}
}
