package com.brsanthu.googleanalytics;

import static com.brsanthu.googleanalytics.Parameter.CURRENCY_CODE;
import static com.brsanthu.googleanalytics.Parameter.DNS_TIME;
import static com.brsanthu.googleanalytics.Parameter.EVENT_ACTION;
import static com.brsanthu.googleanalytics.Parameter.EVENT_CATEGORY;
import static com.brsanthu.googleanalytics.Parameter.EVENT_LABEL;
import static com.brsanthu.googleanalytics.Parameter.EVENT_VALUE;
import static com.brsanthu.googleanalytics.Parameter.EXCEPTION_DESCRIPTION;
import static com.brsanthu.googleanalytics.Parameter.EXCEPTION_FATAL;
import static com.brsanthu.googleanalytics.Parameter.ITEM_CATEGORY;
import static com.brsanthu.googleanalytics.Parameter.ITEM_CODE;
import static com.brsanthu.googleanalytics.Parameter.ITEM_NAME;
import static com.brsanthu.googleanalytics.Parameter.ITEM_PRICE;
import static com.brsanthu.googleanalytics.Parameter.ITEM_QUANTITY;
import static com.brsanthu.googleanalytics.Parameter.PAGE_DOWNLOAD_TIME;
import static com.brsanthu.googleanalytics.Parameter.PAGE_LOAD_TIME;
import static com.brsanthu.googleanalytics.Parameter.REDIRECT_RESPONSE_TIME;
import static com.brsanthu.googleanalytics.Parameter.SERVER_RESPONSE_TIME;
import static com.brsanthu.googleanalytics.Parameter.SOCIAL_ACTION;
import static com.brsanthu.googleanalytics.Parameter.SOCIAL_ACTION_TARGET;
import static com.brsanthu.googleanalytics.Parameter.SOCIAL_NETWORK;
import static com.brsanthu.googleanalytics.Parameter.TCP_CONNECT_TIME;
import static com.brsanthu.googleanalytics.Parameter.TRANSACTION_AFFILIATION;
import static com.brsanthu.googleanalytics.Parameter.TRANSACTION_ID;
import static com.brsanthu.googleanalytics.Parameter.TRANSACTION_REVENUE;
import static com.brsanthu.googleanalytics.Parameter.TRANSACTION_SHIPPING;
import static com.brsanthu.googleanalytics.Parameter.TRANSACTION_TAX;
import static com.brsanthu.googleanalytics.Parameter.USER_TIMING_CATEGORY;
import static com.brsanthu.googleanalytics.Parameter.USER_TIMING_LABEL;
import static com.brsanthu.googleanalytics.Parameter.USER_TIMING_TIME;
import static com.brsanthu.googleanalytics.Parameter.USER_TIMING_VARIABLE_NAME;

public class Request extends AbstractRequest<Request>{

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
	}

	public Request eventCategory(String value) {
		setString(EVENT_CATEGORY, value);
	   	return this;
	}

	public String eventCategory() {
		return getString(EVENT_CATEGORY);
	}
	public Request eventAction(String value) {
		setString(EVENT_ACTION, value);
	   	return this;
	}
	public String eventAction() {
		return getString(EVENT_ACTION);
	}
	public Request eventLabel(String value) {
		setString(EVENT_LABEL, value);
	   	return this;
	}
	public String eventLabel() {
		return getString(EVENT_LABEL);
	}
	public Request eventValue(Integer value) {
		setInteger(EVENT_VALUE, value);
	   	return this;
	}
	public Integer eventValue() {
		return getInteger(EVENT_VALUE);
	}
	public Request exceptionDescription(String value) {
		setString(EXCEPTION_DESCRIPTION, value);
	   	return this;
	}
	public String exceptionDescription() {
		return getString(EXCEPTION_DESCRIPTION);
	}
	public Request exceptionFatal(Boolean value) {
		setBoolean(EXCEPTION_FATAL, value);
	   	return this;
	}
	public Boolean exceptionFatal() {
		return getBoolean(EXCEPTION_FATAL);
	}
	public Request itemName(String value) {
		setString(ITEM_NAME, value);
	   	return this;
	}
	public String itemName() {
		return getString(ITEM_NAME);
	}
	public Request itemPrice(Double value) {
		setDouble(ITEM_PRICE, value);
	   	return this;
	}
	public Double itemPrice() {
		return getDouble(ITEM_PRICE);
	}
	public Request itemQuantity(Integer value) {
		setInteger(ITEM_QUANTITY, value);
	   	return this;
	}
	public Integer itemQuantity() {
		return getInteger(ITEM_QUANTITY);
	}
	public Request itemCode(String value) {
		setString(ITEM_CODE, value);
	   	return this;
	}
	public String itemCode() {
		return getString(ITEM_CODE);
	}
	public Request itemCategory(String value) {
		setString(ITEM_CATEGORY, value);
	   	return this;
	}
	public String itemCategory() {
		return getString(ITEM_CATEGORY);
	}
	public Request currencyCode(String value) {
		setString(CURRENCY_CODE, value);
	   	return this;
	}
	public String currencyCode() {
		return getString(CURRENCY_CODE);
	}

	public Request socialNetwork(String value) {
		setString(SOCIAL_NETWORK, value);
	   	return this;
	}
	public String socialNetwork() {
		return getString(SOCIAL_NETWORK);
	}
	public Request socialAction(String value) {
		setString(SOCIAL_ACTION, value);
	   	return this;
	}
	public String socialAction() {
		return getString(SOCIAL_ACTION);
	}
	public Request socialActionTarget(String value) {
		setString(SOCIAL_ACTION_TARGET, value);
	   	return this;
	}
	public String socialActionTarget() {
		return getString(SOCIAL_ACTION_TARGET);
	}

	public Request userTimingCategory(String value) {
		setString(USER_TIMING_CATEGORY, value);
	   	return this;
	}
	public String userTimingCategory() {
		return getString(USER_TIMING_CATEGORY);
	}
	public Request userTimingVariableName(String value) {
		setString(USER_TIMING_VARIABLE_NAME, value);
	   	return this;
	}
	public String userTimingVariableName() {
		return getString(USER_TIMING_VARIABLE_NAME);
	}
	public Request userTimingTime(Integer value) {
		setInteger(USER_TIMING_TIME, value);
	   	return this;
	}
	public Integer userTimingTime() {
		return getInteger(USER_TIMING_TIME);
	}
	public Request userTimingLabel(String value) {
		setString(USER_TIMING_LABEL, value);
	   	return this;
	}
	public String userTimingLabel() {
		return getString(USER_TIMING_LABEL);
	}
	public Request pageLoadTime(Integer value) {
		setInteger(PAGE_LOAD_TIME, value);
	   	return this;
	}
	public Integer pageLoadTime() {
		return getInteger(PAGE_LOAD_TIME);
	}
	public Request dnsTime(Integer value) {
		setInteger(DNS_TIME, value);
	   	return this;
	}
	public Integer dnsTime() {
		return getInteger(DNS_TIME);
	}
	public Request pageDownloadTime(Integer value) {
		setInteger(PAGE_DOWNLOAD_TIME, value);
	   	return this;
	}
	public Integer pageDownloadTime() {
		return getInteger(PAGE_DOWNLOAD_TIME);
	}
	public Request redirectResponseTime(Integer value) {
		setInteger(REDIRECT_RESPONSE_TIME, value);
	   	return this;
	}
	public Integer redirectResponseTime() {
		return getInteger(REDIRECT_RESPONSE_TIME);
	}
	public Request tcpConnectTime(Integer value) {
		setInteger(TCP_CONNECT_TIME, value);
	   	return this;
	}
	public Integer tcpConnectTime() {
		return getInteger(TCP_CONNECT_TIME);
	}
	public Request serverResponseTime(Integer value) {
		setInteger(SERVER_RESPONSE_TIME, value);
	   	return this;
	}
	public Integer serverResponseTime() {
		return getInteger(SERVER_RESPONSE_TIME);
	}

	public Request transactionId(String value) {
		setString(TRANSACTION_ID, value);
	   	return this;
	}
	public String transactionId() {
		return getString(TRANSACTION_ID);
	}
	public Request transactionAffiliation(String value) {
		setString(TRANSACTION_AFFILIATION, value);
	   	return this;
	}
	public String transactionAffiliation() {
		return getString(TRANSACTION_AFFILIATION);
	}
	public Request transactionRevenue(Double value) {
		setDouble(TRANSACTION_REVENUE, value);
	   	return this;
	}
	public Double transactionRevenue() {
		return getDouble(TRANSACTION_REVENUE);
	}
	public Request transactionShipping(Double value) {
		setDouble(TRANSACTION_SHIPPING, value);
	   	return this;
	}
	public Double transactionShipping() {
		return getDouble(TRANSACTION_SHIPPING);
	}
	public Request transactionTax(Double value) {
		setDouble(TRANSACTION_TAX, value);
	   	return this;
	}
	public Double transactionTax() {
		return getDouble(TRANSACTION_TAX);
	}
}
