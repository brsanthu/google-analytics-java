/*
 * Licensed under the Apache License, Version 2.0 (the "License")),
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
package com.brsanthu.googleanalytics;

/**
 * Google Analytics Measurement Protocol Parameters.
 *
 * <p>For more information, see <a href="https://developers.google.com/analytics/devguides/collection/protocol/v1/parameters">GA Parameters Reference</a></p>
 *
 * @author Santhosh Kumar
 */
public enum GoogleAnalyticsParameter {
	//General
	PROTOCOL_VERSION("v", true),
	TRACKING_ID("tid", true),
	ANONYMIZE_IP("aip", "boolean"),
	QUEUE_TIME("qt", "integer"),
	CACHE_BUSTER("z"),
	DATA_SOURCE("ds"),
	
	//Visitor
	CLIENT_ID("cid", true),
	USER_ID("uid"),

	//Session
	SESSION_CONTROL("sc"),
	USER_IP("uip"),
	USER_AGENT("ua"),
	
	//Traffic Sources
	DOCUMENT_REFERRER("dr", 2048),
	CAMPAIGN_NAME("cn", 100),
	CAMPAIGN_SOURCE("cs", 100),
	CAMPAIGN_MEDIUM("cm", 50),
	CAMPAIGN_KEYWORD("ck", 500),
	CAMPAIGN_CONTENT("cc", 500),
	CAMPAIGN_ID("ci", 100),
	ADWORDS_ID("gclid"),
	DISPLAY_ADS_ID("dclid"),

	//System Info
	SCREEN_RESOLUTION("sr", 20),
	VIEWPORT_SIZE("vp", 20),
	DOCUMENT_ENCODING("de", 20),
	SCREEN_COLORS("sd", 20),
	USER_LANGUAGE("ul", 20),
	JAVA_ENABLED("je", "boolean"),
	FLASH_VERSION("fl", 20),

	//Hit
	HIT_TYPE("t", true),
	NON_INTERACTION_HIT("ni"),

	//Content Information
	DOCUMENT_URL("dl", 2048),
	DOCUMENT_HOST_NAME ("dh", 100),
	DOCUMENT_PATH ("dp", 2048),
	DOCUMENT_TITLE ("dt", 1500),
	
	CONTENT_DESCRIPTION ("cd"),

	LINK_ID ("linkid"),
	
	//App Tracking
	APPLICATION_NAME("an", 100),
	APPLICATION_ID("aid", 150),
	APPLICATION_VERSION("av", 100),
	APPLICATION_INSTALLER_ID("aiid", 150),

	//Event Tracking
	EVENT_CATEGORY("ec", new String[] {"event"}, 150),
	EVENT_ACTION("ea", new String[] {"event"}, 500),
	EVENT_LABEL("el", new String[] {"event"}, 500),
	EVENT_VALUE("ev", false, "integer", new String[] {"event"}),

	//E-Commerce
	TRANSACTION_ID("ti", new String[] {"transaction", "item"}, 500),
	TRANSACTION_AFFILIATION("ta", new String[] {"transaction"}, 500),
	TRANSACTION_REVENUE("tr", false, "currency", new String[] {"transaction"}),
	TRANSACTION_SHIPPING("ts", false, "currency", new String[] {"transaction"}),
	TRANSACTION_TAX("tt", false, "currency", new String[] {"transaction"}),
	ITEM_NAME("in", new String[] {"item"}, 500),
	ITEM_PRICE("ip", false, "currency", new String[] {"item"}),
	ITEM_QUANTITY("iq", false, "integer", new String[] {"item"}),
	ITEM_CODE("ic", new String[] {"item"}, 500),
	ITEM_CATEGORY("iv", new String[] {"item"}, 500),
	CURRENCY_CODE("cu", new String[] {"transaction", "item"}, 10),

	//Social Interactions
	SOCIAL_NETWORK("sn", new String[] {"social"}, 50),
	SOCIAL_ACTION("sa", new String[] {"social"}, 50),
	SOCIAL_ACTION_TARGET("st", new String[] {"social"}, 2048),

	//Timing
	USER_TIMING_CATEGORY("utc", new String[] {"timing"}, 150),
	USER_TIMING_VARIABLE_NAME("utv", new String[] {"timing"}, 500),
	USER_TIMING_TIME("utt", false, "integer", new String[] {"timing"}),
	USER_TIMING_LABEL("utl", new String[] {"timing"}, 500),
	PAGE_LOAD_TIME("plt", false, "integer", new String[] {"timing"}),
	DNS_TIME("dns", false, "integer", new String[] {"timing"}),
	PAGE_DOWNLOAD_TIME("pdt", false, "integer", new String[] {"timing"}),
	REDIRECT_RESPONSE_TIME("rrt", false, "integer", new String[] {"timing"}),
	TCP_CONNECT_TIME("tcp", false, "integer", new String[] {"timing"}),
	SERVER_RESPONSE_TIME("srt", false, "integer", new String[] {"timing"}),

	//Exceptions
	EXCEPTION_DESCRIPTION("exd", new String[] {"exception"}, 150),
	EXCEPTION_FATAL("exf", false, "boolean", new String[] {"exception"}),

	//Experiment Variations
	EXPERIMENT_ID("xid", 40),
	EXPERIMENT_VARIANT("xvar");

	private String parameterName = null;
	private boolean required = false;
	private String type = "text";
	private String[] supportedHitTypes = null;
	private int maxLength = 0;
	
	private GoogleAnalyticsParameter(String name) {
		this(name, false);
	}

	private GoogleAnalyticsParameter(String name, int maxLength) {
		this(name, false, null, null, maxLength);
	}

	private GoogleAnalyticsParameter(String name, boolean required) {
		this(name, required, "text", null, 0);
	}

	private GoogleAnalyticsParameter(String name, String type) {
		this(name, false, type, null, 0);
	}

	private GoogleAnalyticsParameter(String name, String[] supportedHitTypes) {
		this(name, false, "text", supportedHitTypes, 0);
	}

	private GoogleAnalyticsParameter(String name, String[] supportedHitTypes, int maxLength) {
		this(name, false, "text", supportedHitTypes, maxLength);
	}

	private GoogleAnalyticsParameter(String name, boolean required, String type, String[] supportedHitTypes) {
		this(name, required, type, supportedHitTypes, 0);
	}

	private GoogleAnalyticsParameter(String name, boolean required, String type, String[] supportedHitTypes, int maxLength) {
		this.parameterName = name;
		this.required = required;
		if (type == null) {
			type = "text";
		}
		this.type = type;
		this.supportedHitTypes = supportedHitTypes;
		this.maxLength = maxLength;
	}

	public String getParameterName() {
		return parameterName;
	}

	public String[] getSupportedHitTypes() {
		return supportedHitTypes;
	}

	public String getType() {
		return type;
	}

	public boolean isRequired() {
		return required;
	}
	
	public int getMaxLength() {
		return maxLength;
	}
<<<<<<< HEAD
}
=======
}
>>>>>>> initializing after doing dos2unix
