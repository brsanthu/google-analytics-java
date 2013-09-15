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
package com.brsanthu.googleanalytics;

import static com.brsanthu.googleanalytics.Parameter.ADWORDS_ID;
import static com.brsanthu.googleanalytics.Parameter.ANONYMIZE_IP;
import static com.brsanthu.googleanalytics.Parameter.APPLICATION_NAME;
import static com.brsanthu.googleanalytics.Parameter.APPLICATION_VERSION;
import static com.brsanthu.googleanalytics.Parameter.CACHE_BUSTER;
import static com.brsanthu.googleanalytics.Parameter.CAMPAIGN_CONTENT;
import static com.brsanthu.googleanalytics.Parameter.CAMPAIGN_ID;
import static com.brsanthu.googleanalytics.Parameter.CAMPAIGN_KEYWORD;
import static com.brsanthu.googleanalytics.Parameter.CAMPAIGN_MEDIUM;
import static com.brsanthu.googleanalytics.Parameter.CAMPAIGN_NAME;
import static com.brsanthu.googleanalytics.Parameter.CAMPAIGN_SOURCE;
import static com.brsanthu.googleanalytics.Parameter.CLIENT_ID;
import static com.brsanthu.googleanalytics.Parameter.CONTENT_DESCRIPTION;
import static com.brsanthu.googleanalytics.Parameter.CURRENCY_CODE;
import static com.brsanthu.googleanalytics.Parameter.DISPLAYAD_ID;
import static com.brsanthu.googleanalytics.Parameter.DNS_TIME;
import static com.brsanthu.googleanalytics.Parameter.DOCUMENT_ENCODING;
import static com.brsanthu.googleanalytics.Parameter.DOCUMENT_HOST_NAME;
import static com.brsanthu.googleanalytics.Parameter.DOCUMENT_LOCATION_URL;
import static com.brsanthu.googleanalytics.Parameter.DOCUMENT_PATH;
import static com.brsanthu.googleanalytics.Parameter.DOCUMENT_REFERRER;
import static com.brsanthu.googleanalytics.Parameter.DOCUMENT_TITLE;
import static com.brsanthu.googleanalytics.Parameter.EVENT_ACTION;
import static com.brsanthu.googleanalytics.Parameter.EVENT_CATEGORY;
import static com.brsanthu.googleanalytics.Parameter.EVENT_LABEL;
import static com.brsanthu.googleanalytics.Parameter.EVENT_VALUE;
import static com.brsanthu.googleanalytics.Parameter.EXCEPTION_DESCRIPTION;
import static com.brsanthu.googleanalytics.Parameter.EXCEPTION_FATAL;
import static com.brsanthu.googleanalytics.Parameter.FLASH_VERSION;
import static com.brsanthu.googleanalytics.Parameter.HIT_TYPE;
import static com.brsanthu.googleanalytics.Parameter.ITEM_CATEGORY;
import static com.brsanthu.googleanalytics.Parameter.ITEM_CODE;
import static com.brsanthu.googleanalytics.Parameter.ITEM_NAME;
import static com.brsanthu.googleanalytics.Parameter.ITEM_PRICE;
import static com.brsanthu.googleanalytics.Parameter.ITEM_QUANTITY;
import static com.brsanthu.googleanalytics.Parameter.JAVA_ENABLED;
import static com.brsanthu.googleanalytics.Parameter.NON_INTERACTION_HIT;
import static com.brsanthu.googleanalytics.Parameter.PAGE_DOWNLOAD_TIME;
import static com.brsanthu.googleanalytics.Parameter.PAGE_LOAD_TIME;
import static com.brsanthu.googleanalytics.Parameter.PROTOCOL_VERSION;
import static com.brsanthu.googleanalytics.Parameter.QUEUE_TIME;
import static com.brsanthu.googleanalytics.Parameter.REDIRECT_RESPONSE_TIME;
import static com.brsanthu.googleanalytics.Parameter.SCREEN_COLORS;
import static com.brsanthu.googleanalytics.Parameter.SCREEN_RESOLUTION;
import static com.brsanthu.googleanalytics.Parameter.SERVER_RESPONSE_TIME;
import static com.brsanthu.googleanalytics.Parameter.SESSION_CONTROL;
import static com.brsanthu.googleanalytics.Parameter.SOCIAL_ACTION;
import static com.brsanthu.googleanalytics.Parameter.SOCIAL_ACTION_TARGET;
import static com.brsanthu.googleanalytics.Parameter.SOCIAL_NETWORK;
import static com.brsanthu.googleanalytics.Parameter.TCP_CONNECT_TIME;
import static com.brsanthu.googleanalytics.Parameter.TRACKING_ID;
import static com.brsanthu.googleanalytics.Parameter.TRANSACTION_AFFILIATION;
import static com.brsanthu.googleanalytics.Parameter.TRANSACTION_ID;
import static com.brsanthu.googleanalytics.Parameter.TRANSACTION_REVENUE;
import static com.brsanthu.googleanalytics.Parameter.TRANSACTION_SHIPPING;
import static com.brsanthu.googleanalytics.Parameter.TRANSACTION_TAX;
import static com.brsanthu.googleanalytics.Parameter.USER_LANGUAGE;
import static com.brsanthu.googleanalytics.Parameter.USER_TIMING_CATEGORY;
import static com.brsanthu.googleanalytics.Parameter.USER_TIMING_LABEL;
import static com.brsanthu.googleanalytics.Parameter.USER_TIMING_TIME;
import static com.brsanthu.googleanalytics.Parameter.USER_TIMING_VARIABLE_NAME;
import static com.brsanthu.googleanalytics.Parameter.VIEWPORT_SIZE;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Request {

	private final static String DEFAULT_CLIENT_ID = UUID.randomUUID().toString();

	private Map<Parameter, String> parms = new HashMap<Parameter, String>();
	private Map<String, String> customDimentions = new HashMap<String, String>();
	private Map<String, String> customMetrics = new HashMap<String, String>();

	public Request() {
		this(null, null, null, null);
	}

	public Request(String hitType) {
		this(hitType, null, null, null);
	}

	public Request(String hitType, String trackingId, String appName, String appVersion) {
		hitType(isEmpty(hitType)?"pageView":hitType);
		trackingId(trackingId);
		applicationName(appName);
		applicationVersion(appVersion);

		clientId(DEFAULT_CLIENT_ID);
		protocolVersion("1");
	}

	private boolean isEmpty(String string) {
		return string == null || string.trim().length() == 0;
	}

	private String fromBoolean(Boolean booleanString) {
		if (booleanString == null) {
			return null;
		}

		return "" + booleanString;
	}

	private Boolean toBoolean(String booleanString) {
		if (isEmpty(booleanString)) {
			return null;
		}

		return new Boolean(booleanString).booleanValue();
	}

	private String fromInteger(Integer intValue) {
		if (intValue == null) {
			return null;
		}

		return "" + intValue;
	}

	private Integer toInteger(String intString) {
		if (isEmpty(intString)) {
			return null;
		}

		return Integer.parseInt(intString);
	}

	private String fromDouble(Double doubleValue) {
		if (doubleValue == null) {
			return null;
		}

		return "" + doubleValue;
	}

	private Double toDouble(String doubleString) {
		if (isEmpty(doubleString)) {
			return null;
		}

		return Double.parseDouble(doubleString);
	}

	public Map<Parameter, String> getParameters() {
		return parms;
	}


	public Request parameter(Parameter parameter, String value) {
		if (value == null) {
			parms.remove(parameter);
		} else {
			parms.put(parameter, value);
		}
		return this;
	}

	public String parameter(Parameter parameter) {
		return parms.get(parameter);
	}

	public String customDimention(int index) {
		return customDimentions.get("cd" + index);
	}

	public Request customDimention(int index, String value) {
		customDimentions.put("cd" + index, value);
		return this;
	}

	public Request customMetric(int index, String value) {
		customMetrics.put("cm" + index, value);
		return this;
	}

	public String customMetric(int index) {
		return customMetrics.get("cm" + index);
	}

	public Map<String, String> customDimentions() {
		return customDimentions;
	}

	public Map<String, String> custommMetrics() {
		return customMetrics;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Request [");
		if (parms != null) {
			builder.append("parms=");
			builder.append(parms);
			builder.append(", ");
		}
		if (customDimentions != null) {
			builder.append("customDimentions=");
			builder.append(customDimentions);
			builder.append(", ");
		}
		if (customMetrics != null) {
			builder.append("customMetrics=");
			builder.append(customMetrics);
		}
		builder.append("]");
		return builder.toString();
	}

	/***************************** Below lines are auto generated based on ParameterGetterSetterGenerator */
	public Request protocolVersion(String value) {
		if (value == null) {
			parms.remove(PROTOCOL_VERSION);
		} else {
			String stringValue = value;
			parms.put(PROTOCOL_VERSION, stringValue);
		}
		return this;
	}

	public String protocolVersion() {
		return parms.get(PROTOCOL_VERSION);
	}

	public Request trackingId(String value) {
		if (value == null) {
			parms.remove(TRACKING_ID);
		} else {
			String stringValue = value;
			parms.put(TRACKING_ID, stringValue);
		}
		return this;
	}

	public String trackingId() {
		return parms.get(TRACKING_ID);
	}

	public Request anonymizeIp(Boolean value) {
		if (value == null) {
			parms.remove(ANONYMIZE_IP);
		} else {
			String stringValue = fromBoolean(value);
			parms.put(ANONYMIZE_IP, stringValue);
		}
		return this;
	}

	public Boolean anonymizeIp() {
		return toBoolean(parms.get(ANONYMIZE_IP));
	}

	public Request queueTime(Integer value) {
		if (value == null) {
			parms.remove(QUEUE_TIME);
		} else {
			String stringValue = fromInteger(value);
			parms.put(QUEUE_TIME, stringValue);
		}
		return this;
	}

	public Integer queueTime() {
		return toInteger(parms.get(QUEUE_TIME));
	}

	public Request cacheBuster(String value) {
		if (value == null) {
			parms.remove(CACHE_BUSTER);
		} else {
			String stringValue = value;
			parms.put(CACHE_BUSTER, stringValue);
		}
		return this;
	}

	public String cacheBuster() {
		return parms.get(CACHE_BUSTER);
	}

	public Request clientId(String value) {
		if (value == null) {
			parms.remove(CLIENT_ID);
		} else {
			String stringValue = value;
			parms.put(CLIENT_ID, stringValue);
		}
		return this;
	}

	public String clientId() {
		return parms.get(CLIENT_ID);
	}

	public Request sessionControl(String value) {
		if (value == null) {
			parms.remove(SESSION_CONTROL);
		} else {
			String stringValue = value;
			parms.put(SESSION_CONTROL, stringValue);
		}
		return this;
	}

	public String sessionControl() {
		return parms.get(SESSION_CONTROL);
	}

	public Request documentReferrer(String value) {
		if (value == null) {
			parms.remove(DOCUMENT_REFERRER);
		} else {
			String stringValue = value;
			parms.put(DOCUMENT_REFERRER, stringValue);
		}
		return this;
	}

	public String documentReferrer() {
		return parms.get(DOCUMENT_REFERRER);
	}

	public Request campaignName(String value) {
		if (value == null) {
			parms.remove(CAMPAIGN_NAME);
		} else {
			String stringValue = value;
			parms.put(CAMPAIGN_NAME, stringValue);
		}
		return this;
	}

	public String campaignName() {
		return parms.get(CAMPAIGN_NAME);
	}

	public Request campaignSource(String value) {
		if (value == null) {
			parms.remove(CAMPAIGN_SOURCE);
		} else {
			String stringValue = value;
			parms.put(CAMPAIGN_SOURCE, stringValue);
		}
		return this;
	}

	public String campaignSource() {
		return parms.get(CAMPAIGN_SOURCE);
	}

	public Request campaignMedium(String value) {
		if (value == null) {
			parms.remove(CAMPAIGN_MEDIUM);
		} else {
			String stringValue = value;
			parms.put(CAMPAIGN_MEDIUM, stringValue);
		}
		return this;
	}

	public String campaignMedium() {
		return parms.get(CAMPAIGN_MEDIUM);
	}

	public Request campaignKeyword(String value) {
		if (value == null) {
			parms.remove(CAMPAIGN_KEYWORD);
		} else {
			String stringValue = value;
			parms.put(CAMPAIGN_KEYWORD, stringValue);
		}
		return this;
	}

	public String campaignKeyword() {
		return parms.get(CAMPAIGN_KEYWORD);
	}

	public Request campaignContent(String value) {
		if (value == null) {
			parms.remove(CAMPAIGN_CONTENT);
		} else {
			String stringValue = value;
			parms.put(CAMPAIGN_CONTENT, stringValue);
		}
		return this;
	}

	public String campaignContent() {
		return parms.get(CAMPAIGN_CONTENT);
	}

	public Request campaignId(String value) {
		if (value == null) {
			parms.remove(CAMPAIGN_ID);
		} else {
			String stringValue = value;
			parms.put(CAMPAIGN_ID, stringValue);
		}
		return this;
	}

	public String campaignId() {
		return parms.get(CAMPAIGN_ID);
	}

	public Request adwordsId(String value) {
		if (value == null) {
			parms.remove(ADWORDS_ID);
		} else {
			String stringValue = value;
			parms.put(ADWORDS_ID, stringValue);
		}
		return this;
	}

	public String adwordsId() {
		return parms.get(ADWORDS_ID);
	}

	public Request displayadId(String value) {
		if (value == null) {
			parms.remove(DISPLAYAD_ID);
		} else {
			String stringValue = value;
			parms.put(DISPLAYAD_ID, stringValue);
		}
		return this;
	}

	public String displayadId() {
		return parms.get(DISPLAYAD_ID);
	}

	public Request screenResolution(String value) {
		if (value == null) {
			parms.remove(SCREEN_RESOLUTION);
		} else {
			String stringValue = value;
			parms.put(SCREEN_RESOLUTION, stringValue);
		}
		return this;
	}

	public String screenResolution() {
		return parms.get(SCREEN_RESOLUTION);
	}

	public Request viewportSize(String value) {
		if (value == null) {
			parms.remove(VIEWPORT_SIZE);
		} else {
			String stringValue = value;
			parms.put(VIEWPORT_SIZE, stringValue);
		}
		return this;
	}

	public String viewportSize() {
		return parms.get(VIEWPORT_SIZE);
	}

	public Request documentEncoding(String value) {
		if (value == null) {
			parms.remove(DOCUMENT_ENCODING);
		} else {
			String stringValue = value;
			parms.put(DOCUMENT_ENCODING, stringValue);
		}
		return this;
	}

	public String documentEncoding() {
		return parms.get(DOCUMENT_ENCODING);
	}

	public Request screenColors(String value) {
		if (value == null) {
			parms.remove(SCREEN_COLORS);
		} else {
			String stringValue = value;
			parms.put(SCREEN_COLORS, stringValue);
		}
		return this;
	}

	public String screenColors() {
		return parms.get(SCREEN_COLORS);
	}

	public Request userLanguage(String value) {
		if (value == null) {
			parms.remove(USER_LANGUAGE);
		} else {
			String stringValue = value;
			parms.put(USER_LANGUAGE, stringValue);
		}
		return this;
	}

	public String userLanguage() {
		return parms.get(USER_LANGUAGE);
	}

	public Request javaEnabled(Boolean value) {
		if (value == null) {
			parms.remove(JAVA_ENABLED);
		} else {
			String stringValue = fromBoolean(value);
			parms.put(JAVA_ENABLED, stringValue);
		}
		return this;
	}

	public Boolean javaEnabled() {
		return toBoolean(parms.get(JAVA_ENABLED));
	}

	public Request flashVersion(String value) {
		if (value == null) {
			parms.remove(FLASH_VERSION);
		} else {
			String stringValue = value;
			parms.put(FLASH_VERSION, stringValue);
		}
		return this;
	}

	public String flashVersion() {
		return parms.get(FLASH_VERSION);
	}

	public Request hitType(String value) {
		if (value == null) {
			parms.remove(HIT_TYPE);
		} else {
			String stringValue = value;
			parms.put(HIT_TYPE, stringValue);
		}
		return this;
	}

	public String hitType() {
		return parms.get(HIT_TYPE);
	}

	public Request nonInteractionHit(String value) {
		if (value == null) {
			parms.remove(NON_INTERACTION_HIT);
		} else {
			String stringValue = value;
			parms.put(NON_INTERACTION_HIT, stringValue);
		}
		return this;
	}

	public String nonInteractionHit() {
		return parms.get(NON_INTERACTION_HIT);
	}

	public Request documentLocationUrl(String value) {
		if (value == null) {
			parms.remove(DOCUMENT_LOCATION_URL);
		} else {
			String stringValue = value;
			parms.put(DOCUMENT_LOCATION_URL, stringValue);
		}
		return this;
	}

	public String documentLocationUrl() {
		return parms.get(DOCUMENT_LOCATION_URL);
	}

	public Request documentHostName(String value) {
		if (value == null) {
			parms.remove(DOCUMENT_HOST_NAME);
		} else {
			String stringValue = value;
			parms.put(DOCUMENT_HOST_NAME, stringValue);
		}
		return this;
	}

	public String documentHostName() {
		return parms.get(DOCUMENT_HOST_NAME);
	}

	public Request documentPath(String value) {
		if (value == null) {
			parms.remove(DOCUMENT_PATH);
		} else {
			String stringValue = value;
			parms.put(DOCUMENT_PATH, stringValue);
		}
		return this;
	}

	public String documentPath() {
		return parms.get(DOCUMENT_PATH);
	}

	public Request documentTitle(String value) {
		if (value == null) {
			parms.remove(DOCUMENT_TITLE);
		} else {
			String stringValue = value;
			parms.put(DOCUMENT_TITLE, stringValue);
		}
		return this;
	}

	public String documentTitle() {
		return parms.get(DOCUMENT_TITLE);
	}

	public Request contentDescription(String value) {
		if (value == null) {
			parms.remove(CONTENT_DESCRIPTION);
		} else {
			String stringValue = value;
			parms.put(CONTENT_DESCRIPTION, stringValue);
		}
		return this;
	}

	public String contentDescription() {
		return parms.get(CONTENT_DESCRIPTION);
	}

	public Request applicationName(String value) {
		if (value == null) {
			parms.remove(APPLICATION_NAME);
		} else {
			String stringValue = value;
			parms.put(APPLICATION_NAME, stringValue);
		}
		return this;
	}

	public String applicationName() {
		return parms.get(APPLICATION_NAME);
	}

	public Request applicationVersion(String value) {
		if (value == null) {
			parms.remove(APPLICATION_VERSION);
		} else {
			String stringValue = value;
			parms.put(APPLICATION_VERSION, stringValue);
		}
		return this;
	}

	public String applicationVersion() {
		return parms.get(APPLICATION_VERSION);
	}

	public Request eventCategory(String value) {
		if (value == null) {
			parms.remove(EVENT_CATEGORY);
		} else {
			String stringValue = value;
			parms.put(EVENT_CATEGORY, stringValue);
		}
		return this;
	}

	public String eventCategory() {
		return parms.get(EVENT_CATEGORY);
	}

	public Request eventAction(String value) {
		if (value == null) {
			parms.remove(EVENT_ACTION);
		} else {
			String stringValue = value;
			parms.put(EVENT_ACTION, stringValue);
		}
		return this;
	}

	public String eventAction() {
		return parms.get(EVENT_ACTION);
	}

	public Request eventLabel(String value) {
		if (value == null) {
			parms.remove(EVENT_LABEL);
		} else {
			String stringValue = value;
			parms.put(EVENT_LABEL, stringValue);
		}
		return this;
	}

	public String eventLabel() {
		return parms.get(EVENT_LABEL);
	}

	public Request eventValue(Integer value) {
		if (value == null) {
			parms.remove(EVENT_VALUE);
		} else {
			String stringValue = fromInteger(value);
			parms.put(EVENT_VALUE, stringValue);
		}
		return this;
	}

	public Integer eventValue() {
		return toInteger(parms.get(EVENT_VALUE));
	}

	public Request transactionId(String value) {
		if (value == null) {
			parms.remove(TRANSACTION_ID);
		} else {
			String stringValue = value;
			parms.put(TRANSACTION_ID, stringValue);
		}
		return this;
	}

	public String transactionId() {
		return parms.get(TRANSACTION_ID);
	}

	public Request transactionAffiliation(String value) {
		if (value == null) {
			parms.remove(TRANSACTION_AFFILIATION);
		} else {
			String stringValue = value;
			parms.put(TRANSACTION_AFFILIATION, stringValue);
		}
		return this;
	}

	public String transactionAffiliation() {
		return parms.get(TRANSACTION_AFFILIATION);
	}

	public Request transactionRevenue(Double value) {
		if (value == null) {
			parms.remove(TRANSACTION_REVENUE);
		} else {
			String stringValue = fromDouble(value);
			parms.put(TRANSACTION_REVENUE, stringValue);
		}
		return this;
	}

	public Double transactionRevenue() {
		return toDouble(parms.get(TRANSACTION_REVENUE));
	}

	public Request transactionShipping(Double value) {
		if (value == null) {
			parms.remove(TRANSACTION_SHIPPING);
		} else {
			String stringValue = fromDouble(value);
			parms.put(TRANSACTION_SHIPPING, stringValue);
		}
		return this;
	}

	public Double transactionShipping() {
		return toDouble(parms.get(TRANSACTION_SHIPPING));
	}

	public Request transactionTax(Double value) {
		if (value == null) {
			parms.remove(TRANSACTION_TAX);
		} else {
			String stringValue = fromDouble(value);
			parms.put(TRANSACTION_TAX, stringValue);
		}
		return this;
	}

	public Double transactionTax() {
		return toDouble(parms.get(TRANSACTION_TAX));
	}

	public Request itemName(String value) {
		if (value == null) {
			parms.remove(ITEM_NAME);
		} else {
			String stringValue = value;
			parms.put(ITEM_NAME, stringValue);
		}
		return this;
	}

	public String itemName() {
		return parms.get(ITEM_NAME);
	}

	public Request itemPrice(Double value) {
		if (value == null) {
			parms.remove(ITEM_PRICE);
		} else {
			String stringValue = fromDouble(value);
			parms.put(ITEM_PRICE, stringValue);
		}
		return this;
	}

	public Double itemPrice() {
		return toDouble(parms.get(ITEM_PRICE));
	}

	public Request itemQuantity(Integer value) {
		if (value == null) {
			parms.remove(ITEM_QUANTITY);
		} else {
			String stringValue = fromInteger(value);
			parms.put(ITEM_QUANTITY, stringValue);
		}
		return this;
	}

	public Integer itemQuantity() {
		return toInteger(parms.get(ITEM_QUANTITY));
	}

	public Request itemCode(String value) {
		if (value == null) {
			parms.remove(ITEM_CODE);
		} else {
			String stringValue = value;
			parms.put(ITEM_CODE, stringValue);
		}
		return this;
	}

	public String itemCode() {
		return parms.get(ITEM_CODE);
	}

	public Request itemCategory(String value) {
		if (value == null) {
			parms.remove(ITEM_CATEGORY);
		} else {
			String stringValue = value;
			parms.put(ITEM_CATEGORY, stringValue);
		}
		return this;
	}

	public String itemCategory() {
		return parms.get(ITEM_CATEGORY);
	}

	public Request currencyCode(String value) {
		if (value == null) {
			parms.remove(CURRENCY_CODE);
		} else {
			String stringValue = value;
			parms.put(CURRENCY_CODE, stringValue);
		}
		return this;
	}

	public String currencyCode() {
		return parms.get(CURRENCY_CODE);
	}

	public Request socialNetwork(String value) {
		if (value == null) {
			parms.remove(SOCIAL_NETWORK);
		} else {
			String stringValue = value;
			parms.put(SOCIAL_NETWORK, stringValue);
		}
		return this;
	}

	public String socialNetwork() {
		return parms.get(SOCIAL_NETWORK);
	}

	public Request socialAction(String value) {
		if (value == null) {
			parms.remove(SOCIAL_ACTION);
		} else {
			String stringValue = value;
			parms.put(SOCIAL_ACTION, stringValue);
		}
		return this;
	}

	public String socialAction() {
		return parms.get(SOCIAL_ACTION);
	}

	public Request socialActionTarget(String value) {
		if (value == null) {
			parms.remove(SOCIAL_ACTION_TARGET);
		} else {
			String stringValue = value;
			parms.put(SOCIAL_ACTION_TARGET, stringValue);
		}
		return this;
	}

	public String socialActionTarget() {
		return parms.get(SOCIAL_ACTION_TARGET);
	}

	public Request userTimingCategory(String value) {
		if (value == null) {
			parms.remove(USER_TIMING_CATEGORY);
		} else {
			String stringValue = value;
			parms.put(USER_TIMING_CATEGORY, stringValue);
		}
		return this;
	}

	public String userTimingCategory() {
		return parms.get(USER_TIMING_CATEGORY);
	}

	public Request userTimingVariableName(String value) {
		if (value == null) {
			parms.remove(USER_TIMING_VARIABLE_NAME);
		} else {
			String stringValue = value;
			parms.put(USER_TIMING_VARIABLE_NAME, stringValue);
		}
		return this;
	}

	public String userTimingVariableName() {
		return parms.get(USER_TIMING_VARIABLE_NAME);
	}

	public Request userTimingTime(Integer value) {
		if (value == null) {
			parms.remove(USER_TIMING_TIME);
		} else {
			String stringValue = fromInteger(value);
			parms.put(USER_TIMING_TIME, stringValue);
		}
		return this;
	}

	public Integer userTimingTime() {
		return toInteger(parms.get(USER_TIMING_TIME));
	}

	public Request userTimingLabel(String value) {
		if (value == null) {
			parms.remove(USER_TIMING_LABEL);
		} else {
			String stringValue = value;
			parms.put(USER_TIMING_LABEL, stringValue);
		}
		return this;
	}

	public String userTimingLabel() {
		return parms.get(USER_TIMING_LABEL);
	}

	public Request pageLoadTime(Integer value) {
		if (value == null) {
			parms.remove(PAGE_LOAD_TIME);
		} else {
			String stringValue = fromInteger(value);
			parms.put(PAGE_LOAD_TIME, stringValue);
		}
		return this;
	}

	public Integer pageLoadTime() {
		return toInteger(parms.get(PAGE_LOAD_TIME));
	}

	public Request dnsTime(Integer value) {
		if (value == null) {
			parms.remove(DNS_TIME);
		} else {
			String stringValue = fromInteger(value);
			parms.put(DNS_TIME, stringValue);
		}
		return this;
	}

	public Integer dnsTime() {
		return toInteger(parms.get(DNS_TIME));
	}

	public Request pageDownloadTime(Integer value) {
		if (value == null) {
			parms.remove(PAGE_DOWNLOAD_TIME);
		} else {
			String stringValue = fromInteger(value);
			parms.put(PAGE_DOWNLOAD_TIME, stringValue);
		}
		return this;
	}

	public Integer pageDownloadTime() {
		return toInteger(parms.get(PAGE_DOWNLOAD_TIME));
	}

	public Request redirectResponseTime(Integer value) {
		if (value == null) {
			parms.remove(REDIRECT_RESPONSE_TIME);
		} else {
			String stringValue = fromInteger(value);
			parms.put(REDIRECT_RESPONSE_TIME, stringValue);
		}
		return this;
	}

	public Integer redirectResponseTime() {
		return toInteger(parms.get(REDIRECT_RESPONSE_TIME));
	}

	public Request tcpConnectTime(Integer value) {
		if (value == null) {
			parms.remove(TCP_CONNECT_TIME);
		} else {
			String stringValue = fromInteger(value);
			parms.put(TCP_CONNECT_TIME, stringValue);
		}
		return this;
	}

	public Integer tcpConnectTime() {
		return toInteger(parms.get(TCP_CONNECT_TIME));
	}

	public Request serverResponseTime(Integer value) {
		if (value == null) {
			parms.remove(SERVER_RESPONSE_TIME);
		} else {
			String stringValue = fromInteger(value);
			parms.put(SERVER_RESPONSE_TIME, stringValue);
		}
		return this;
	}

	public Integer serverResponseTime() {
		return toInteger(parms.get(SERVER_RESPONSE_TIME));
	}

	public Request exceptionDescription(String value) {
		if (value == null) {
			parms.remove(EXCEPTION_DESCRIPTION);
		} else {
			String stringValue = value;
			parms.put(EXCEPTION_DESCRIPTION, stringValue);
		}
		return this;
	}

	public String exceptionDescription() {
		return parms.get(EXCEPTION_DESCRIPTION);
	}

	public Request exceptionFatal(Boolean value) {
		if (value == null) {
			parms.remove(EXCEPTION_FATAL);
		} else {
			String stringValue = fromBoolean(value);
			parms.put(EXCEPTION_FATAL, stringValue);
		}
		return this;
	}

	public Boolean exceptionFatal() {
		return toBoolean(parms.get(EXCEPTION_FATAL));
	}
	/***************************** Above lines are auto generated based on ParameterGetterSetterGenerator */

}
