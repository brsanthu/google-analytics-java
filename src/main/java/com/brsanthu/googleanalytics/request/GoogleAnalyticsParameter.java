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
package com.brsanthu.googleanalytics.request;

import static com.brsanthu.googleanalytics.internal.Constants.HIT_EVENT;
import static com.brsanthu.googleanalytics.internal.Constants.HIT_EXCEPTION;
import static com.brsanthu.googleanalytics.internal.Constants.HIT_ITEM;
import static com.brsanthu.googleanalytics.internal.Constants.HIT_SCREENVIEW;
import static com.brsanthu.googleanalytics.internal.Constants.HIT_SOCIAL;
import static com.brsanthu.googleanalytics.internal.Constants.HIT_TIMING;
import static com.brsanthu.googleanalytics.internal.Constants.HIT_TXN;
import static com.brsanthu.googleanalytics.internal.Constants.TYPE_BOOLEAN;
import static com.brsanthu.googleanalytics.internal.Constants.TYPE_CURRENCY;
import static com.brsanthu.googleanalytics.internal.Constants.TYPE_INTEGER;
import static com.brsanthu.googleanalytics.internal.Constants.TYPE_TEXT;

/**
 * Google Analytics Measurement Protocol Parameters.
 *
 * <p>
 * For more information, see
 * <a href="https://developers.google.com/analytics/devguides/collection/protocol/v1/parameters">GA Parameters
 * Reference</a>
 * </p>
 *
 * @author Santhosh Kumar
 */
public enum GoogleAnalyticsParameter {
    // General
    PROTOCOL_VERSION("v", true),
    TRACKING_ID("tid", true),
    ANONYMIZE_IP("aip", TYPE_BOOLEAN),
    QUEUE_TIME("qt", TYPE_INTEGER),
    CACHE_BUSTER("z"),
    DATA_SOURCE("ds"),

    // Visitor
    CLIENT_ID("cid", true),
    USER_ID("uid"),

    // Session
    SESSION_CONTROL("sc"),
    USER_IP("uip"),
    USER_AGENT("ua"),

    // geoid
    GEOID("geoid"),

    // Traffic Sources
    DOCUMENT_REFERRER("dr", 2048),
    CAMPAIGN_NAME("cn", 100),
    CAMPAIGN_SOURCE("cs", 100),
    CAMPAIGN_MEDIUM("cm", 50),
    CAMPAIGN_KEYWORD("ck", 500),
    CAMPAIGN_CONTENT("cc", 500),
    CAMPAIGN_ID("ci", 100),
    ADWORDS_ID("gclid"),
    DISPLAY_ADS_ID("dclid"),

    // System Info
    SCREEN_RESOLUTION("sr", 20),
    VIEWPORT_SIZE("vp", 20),
    DOCUMENT_ENCODING("de", 20),
    SCREEN_COLORS("sd", 20),
    USER_LANGUAGE("ul", 20),
    JAVA_ENABLED("je", TYPE_BOOLEAN),
    FLASH_VERSION("fl", 20),

    // Hit
    HIT_TYPE("t", true),
    NON_INTERACTION_HIT("ni"),

    // Content Information
    DOCUMENT_URL("dl", 2048),
    DOCUMENT_HOST_NAME("dh", 100),
    DOCUMENT_PATH("dp", 2048),
    DOCUMENT_TITLE("dt", 1500),

    CONTENT_DESCRIPTION("cd"),

    LINK_ID("linkid"),

    // App Tracking
    APPLICATION_NAME("an", 100),
    APPLICATION_ID("aid", 150),
    APPLICATION_VERSION("av", 100),
    APPLICATION_INSTALLER_ID("aiid", 150),

    // Event Tracking
    EVENT_CATEGORY("ec", new String[] { HIT_EVENT }, 150),
    EVENT_ACTION("ea", new String[] { HIT_EVENT }, 500),
    EVENT_LABEL("el", new String[] { HIT_EVENT }, 500),
    EVENT_VALUE("ev", false, TYPE_INTEGER, new String[] { HIT_EVENT }),

    // E-Commerce
    TRANSACTION_ID("ti", new String[] { HIT_TXN, HIT_ITEM }, 500),
    TRANSACTION_AFFILIATION("ta", new String[] { HIT_TXN }, 500),
    TRANSACTION_REVENUE("tr", false, TYPE_CURRENCY, new String[] { HIT_TXN }),
    TRANSACTION_SHIPPING("ts", false, TYPE_CURRENCY, new String[] { HIT_TXN }),
    TRANSACTION_TAX("tt", false, TYPE_CURRENCY, new String[] { HIT_TXN }),
    ITEM_NAME("in", new String[] { HIT_ITEM }, 500),
    ITEM_PRICE("ip", false, TYPE_CURRENCY, new String[] { HIT_ITEM }),
    ITEM_QUANTITY("iq", false, TYPE_INTEGER, new String[] { HIT_ITEM }),
    ITEM_CODE("ic", new String[] { HIT_ITEM }, 500),
    ITEM_CATEGORY("iv", new String[] { HIT_ITEM }, 500),
    CURRENCY_CODE("cu", new String[] { HIT_TXN, HIT_ITEM }, 10),

    // Social Interactions
    SOCIAL_NETWORK("sn", new String[] { HIT_SOCIAL }, 50),
    SOCIAL_ACTION("sa", new String[] { HIT_SOCIAL }, 50),
    SOCIAL_ACTION_TARGET("st", new String[] { HIT_SOCIAL }, 2048),

    // Timing
    USER_TIMING_CATEGORY("utc", new String[] { HIT_TIMING }, 150),
    USER_TIMING_VARIABLE_NAME("utv", new String[] { HIT_TIMING }, 500),
    USER_TIMING_TIME("utt", false, TYPE_INTEGER, new String[] { HIT_TIMING }),
    USER_TIMING_LABEL("utl", new String[] { HIT_TIMING }, 500),
    PAGE_LOAD_TIME("plt", false, TYPE_INTEGER, new String[] { HIT_TIMING }),
    DNS_TIME("dns", false, TYPE_INTEGER, new String[] { HIT_TIMING }),
    PAGE_DOWNLOAD_TIME("pdt", false, TYPE_INTEGER, new String[] { HIT_TIMING }),
    REDIRECT_RESPONSE_TIME("rrt", false, TYPE_INTEGER, new String[] { HIT_TIMING }),
    TCP_CONNECT_TIME("tcp", false, TYPE_INTEGER, new String[] { HIT_TIMING }),
    SERVER_RESPONSE_TIME("srt", false, TYPE_INTEGER, new String[] { HIT_TIMING }),

    // Exceptions
    EXCEPTION_DESCRIPTION("exd", new String[] { HIT_EXCEPTION }, 150),
    EXCEPTION_FATAL("exf", false, TYPE_BOOLEAN, new String[] { HIT_EXCEPTION }),

    // Experiment Variations
    EXPERIMENT_ID("xid", 40),
    EXPERIMENT_VARIANT("xvar"),

    // Screen view parameters
    SCREEN_NAME("cd", true, TYPE_TEXT, new String[] { HIT_SCREENVIEW }, 2048);

    private String parameterName = null;
    private boolean required = false;
    private String type = TYPE_TEXT;
    private String[] supportedHitTypes = null;
    private int maxLength = 0;

    private GoogleAnalyticsParameter(String name) {
        this(name, false);
    }

    private GoogleAnalyticsParameter(String name, int maxLength) {
        this(name, false, null, null, maxLength);
    }

    private GoogleAnalyticsParameter(String name, boolean required) {
        this(name, required, TYPE_TEXT, null, 0);
    }

    private GoogleAnalyticsParameter(String name, String type) {
        this(name, false, type, null, 0);
    }

    private GoogleAnalyticsParameter(String name, String[] supportedHitTypes) {
        this(name, false, TYPE_TEXT, supportedHitTypes, 0);
    }

    private GoogleAnalyticsParameter(String name, String[] supportedHitTypes, int maxLength) {
        this(name, false, TYPE_TEXT, supportedHitTypes, maxLength);
    }

    private GoogleAnalyticsParameter(String name, boolean required, String type, String[] supportedHitTypes) {
        this(name, required, type, supportedHitTypes, 0);
    }

    private GoogleAnalyticsParameter(String name, boolean required, String type, String[] supportedHitTypes, int maxLength) {
        this.parameterName = name;
        this.required = required;
        if (type == null) {
            type = TYPE_TEXT;
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
}
