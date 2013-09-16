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
import static com.brsanthu.googleanalytics.Parameter.DISPLAYAD_ID;
import static com.brsanthu.googleanalytics.Parameter.DOCUMENT_ENCODING;
import static com.brsanthu.googleanalytics.Parameter.DOCUMENT_HOST_NAME;
import static com.brsanthu.googleanalytics.Parameter.DOCUMENT_LOCATION_URL;
import static com.brsanthu.googleanalytics.Parameter.DOCUMENT_PATH;
import static com.brsanthu.googleanalytics.Parameter.DOCUMENT_REFERRER;
import static com.brsanthu.googleanalytics.Parameter.DOCUMENT_TITLE;
import static com.brsanthu.googleanalytics.Parameter.FLASH_VERSION;
import static com.brsanthu.googleanalytics.Parameter.HIT_TYPE;
import static com.brsanthu.googleanalytics.Parameter.JAVA_ENABLED;
import static com.brsanthu.googleanalytics.Parameter.NON_INTERACTION_HIT;
import static com.brsanthu.googleanalytics.Parameter.PROTOCOL_VERSION;
import static com.brsanthu.googleanalytics.Parameter.QUEUE_TIME;
import static com.brsanthu.googleanalytics.Parameter.SCREEN_COLORS;
import static com.brsanthu.googleanalytics.Parameter.SCREEN_RESOLUTION;
import static com.brsanthu.googleanalytics.Parameter.SESSION_CONTROL;
import static com.brsanthu.googleanalytics.Parameter.TRACKING_ID;
import static com.brsanthu.googleanalytics.Parameter.USER_LANGUAGE;
import static com.brsanthu.googleanalytics.Parameter.VIEWPORT_SIZE;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Base Tracking Request containing the parameter maps and type safe getter/setter of all
 * common parameters. The documentation for parameter setters is copied from
 * https://developers.google.com/analytics/devguides/collection/protocol/v1/parameters
 *
 * @author Santhosh Kumar
 */
@SuppressWarnings("unchecked")
public abstract class AbstractRequest<T> {

	private final static String DEFAULT_CLIENT_ID = UUID.randomUUID().toString();

	protected Map<Parameter, String> parms = new HashMap<Parameter, String>();
	protected Map<String, String> customDimentions = new HashMap<String, String>();
	protected Map<String, String> customMetrics = new HashMap<String, String>();

	public AbstractRequest() {
		this(null, null, null, null);
	}

	public AbstractRequest(String hitType) {
		this(hitType, null, null, null);
	}

	public AbstractRequest(String hitType, String trackingId, String appName, String appVersion) {
		hitType(isEmpty(hitType)?"pageView":hitType);
		trackingId(trackingId);
		applicationName(appName);
		applicationVersion(appVersion);

		clientId(DEFAULT_CLIENT_ID);
		protocolVersion("1");
	}

	protected boolean isEmpty(String string) {
		return string == null || string.trim().length() == 0;
	}

	protected T setString(Parameter parameter, String value) {
		if (value == null) {
			parms.remove(parameter);
		} else {
			String stringValue = value;
			parms.put(parameter, stringValue);
		}
		return (T) this;
	}

	public String getString(Parameter parameter) {
		return parms.get(parameter);
	}

	protected T setInteger(Parameter parameter, Integer value) {
		if (value == null) {
			parms.remove(parameter);
		} else {
			String stringValue = fromInteger(value);
			parms.put(parameter, stringValue);
		}
		return (T) this;
	}

	public Double getDouble(Parameter parameter) {
		return toDouble(parms.get(parameter));
	}

	protected T setDouble(Parameter parameter, Double value) {
		if (value == null) {
			parms.remove(parameter);
		} else {
			String stringValue = fromDouble(value);
			parms.put(parameter, stringValue);
		}
		return (T) this;
	}

	public Boolean getBoolean(Parameter parameter) {
		return toBoolean(parms.get(parameter));
	}

	protected T setBoolean(Parameter parameter, Boolean value) {
		if (value == null) {
			parms.remove(parameter);
		} else {
			String stringValue = fromBoolean(value);
			parms.put(parameter, stringValue);
		}
		return (T) this;
	}

	public Integer getInteger(Parameter parameter) {
		return toInteger(parms.get(parameter));
	}

	protected String fromBoolean(Boolean booleanString) {
		if (booleanString == null) {
			return null;
		}

		return "" + booleanString;
	}

	protected Boolean toBoolean(String booleanString) {
		if (isEmpty(booleanString)) {
			return null;
		}

		return new Boolean(booleanString).booleanValue();
	}

	protected String fromInteger(Integer intValue) {
		if (intValue == null) {
			return null;
		}

		return "" + intValue;
	}

	protected Integer toInteger(String intString) {
		if (isEmpty(intString)) {
			return null;
		}

		return Integer.parseInt(intString);
	}

	protected String fromDouble(Double doubleValue) {
		if (doubleValue == null) {
			return null;
		}

		return "" + doubleValue;
	}

	protected Double toDouble(String doubleString) {
		if (isEmpty(doubleString)) {
			return null;
		}

		return Double.parseDouble(doubleString);
	}

	public Map<Parameter, String> getParameters() {
		return parms;
	}

	public T parameter(Parameter parameter, String value) {
		if (value == null) {
			parms.remove(parameter);
		} else {
			parms.put(parameter, value);
		}
		return (T) this;
	}

	public String parameter(Parameter parameter) {
		return parms.get(parameter);
	}

	public String customDimention(int index) {
		return customDimentions.get("cd" + index);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Each custom dimension has an associated index. There is a maximum of 20 custom dimensions (200 for Premium accounts). The name suffix must be a positive integer between 1 and 200, inclusive.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>cd[1-9][0-9]*</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>150 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>Sports</code><br>
	 * 		Example usage: <code>cd[1-9][0-9]*=Sports</code>
	 * 	</div>
	 * </div>
	 */
	public T customDimention(int index, String value) {
		customDimentions.put("cd" + index, value);
		return (T) this;
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Each custom metric has an associated index. There is a maximum of 20 custom metrics (200 for Premium accounts). The name suffix must be a positive integer between 1 and 200, inclusive.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>cm[1-9][0-9]*</code></td>
	 * 				<td>integer</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>47</code><br>
	 * 		Example usage: <code>cm[1-9][0-9]*=47</code>
	 * 	</div>
	 * </div>
	 */
	public T customMetric(int index, String value) {
		customMetrics.put("cm" + index, value);
		return (T) this;
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

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		<strong>Required for all hit types.</strong>
	 * 	</p>
	 * 	<p>The Protocol version. The current value is '1'. This will only change when there are changes made that are not backwards compatible.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>v</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>1</code><br>
	 * 		Example usage: <code>v=1</code>
	 * 	</div>
	 * </div>
	 */
	public T protocolVersion(String value) {
		setString(PROTOCOL_VERSION, value);
	   	return (T) this;
	}
	public String protocolVersion() {
		return getString(PROTOCOL_VERSION);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		<strong>Required for all hit types.</strong>
	 * 	</p>
	 * 	<p>The tracking ID / web property ID. The format is UA-XXXX-Y. All collected data is associated by this ID.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>tid</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>UA-XXXX-Y</code><br>
	 * 		Example usage: <code>tid=UA-XXXX-Y</code>
	 * 	</div>
	 * </div>
	 */
	public T trackingId(String value) {
		setString(TRACKING_ID, value);
	   	return (T) this;
	}
	public String trackingId() {
		return getString(TRACKING_ID);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>When present, the IP address of the sender will be anonymized. For example, the IP will be anonymized if any of the following parameters are present in the payload: &amp;aip=, &amp;aip=0, or &amp;aip=1</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>aip</code></td>
	 * 				<td>boolean</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>1</code><br>
	 * 		Example usage: <code>aip=1</code>
	 * 	</div>
	 * </div>
	 */
	public T anonymizeIp(Boolean value) {
		setBoolean(ANONYMIZE_IP, value);
	   	return (T) this;
	}
	public Boolean anonymizeIp() {
		return getBoolean(ANONYMIZE_IP);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Used to collect offline / latent hits. The value represents the time delta (in milliseconds) between when the hit being reported occurred and the time the hit was sent. The value must be greater than or equal to 0. Values greater than four hours may lead to hits not being processed.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>qt</code></td>
	 * 				<td>integer</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>560</code><br>
	 * 		Example usage: <code>qt=560</code>
	 * 	</div>
	 * </div>
	 */
	public T queueTime(Integer value) {
		setInteger(QUEUE_TIME, value);
	   	return (T) this;
	}
	public Integer queueTime() {
		return getInteger(QUEUE_TIME);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Used to send a random number in GET requests to ensure browsers and proxies don't cache hits. It should be sent as the final parameter of the request since we've seen some 3rd party internet filtering software add additional parameters to HTTP requests incorrectly. This value is not used in reporting.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>z</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>289372387623</code><br>
	 * 		Example usage: <code>z=289372387623</code>
	 * 	</div>
	 * </div>
	 */
	public T cacheBuster(String value) {
		setString(CACHE_BUSTER, value);
	   	return (T) this;
	}
	public String cacheBuster() {
		return getString(CACHE_BUSTER);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		<strong>Required for all hit types.</strong>
	 * 	</p>
	 * 	<p>This anonymously identifies a particular user, device, or browser instance. For the web, this is generally stored as a first-party cookie with a two-year expiration. For mobile apps, this is randomly generated for each particular instance of an application install. The value of this field should be a random UUID (version 4) as described in http://www.ietf.org/rfc/rfc4122.txt</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>cid</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>35009a79-1a05-49d7-b876-2b884d0f825b</code><br>
	 * 		Example usage: <code>cid=35009a79-1a05-49d7-b876-2b884d0f825b</code>
	 * 	</div>
	 * </div>
	 */
	public T clientId(String value) {
		setString(CLIENT_ID, value);
	   	return (T) this;
	}
	public String clientId() {
		return getString(CLIENT_ID);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Used to control the session duration. A value of 'start' forces a new session to start with this hit and 'end' forces the current session to end with this hit. All other values are ignored.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>sc</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>start</code><br>
	 * 		Example usage: <code>sc=start</code>
	 * 	</div>
	 * 	<br>
	 * 	<div>
	 * 		Example value: <code>end</code><br>
	 * 		Example usage: <code>sc=end</code>
	 * 	</div>
	 * </div>
	 */
	public T sessionControl(String value) {
		setString(SESSION_CONTROL, value);
	   	return (T) this;
	}
	public String sessionControl() {
		return getString(SESSION_CONTROL);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies which referral source brought traffic to a website. This value is also used to compute the traffic source. The format of this value is a URL.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>dr</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>2048 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>http://example.com</code><br>
	 * 		Example usage: <code>dr=http%3A%2F%2Fexample.com</code>
	 * 	</div>
	 * </div>
	 */
	public T documentReferrer(String value) {
		setString(DOCUMENT_REFERRER, value);
	   	return (T) this;
	}
	public String documentReferrer() {
		return getString(DOCUMENT_REFERRER);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the campaign name.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>cn</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>100 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>(direct)</code><br>
	 * 		Example usage: <code>cn=%28direct%29</code>
	 * 	</div>
	 * </div>
	 */
	public T campaignName(String value) {
		setString(CAMPAIGN_NAME, value);
	   	return (T) this;
	}
	public String campaignName() {
		return getString(CAMPAIGN_NAME);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the campaign source.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>cs</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>100 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>(direct)</code><br>
	 * 		Example usage: <code>cs=%28direct%29</code>
	 * 	</div>
	 * </div>
	 */
	public T campaignSource(String value) {
		setString(CAMPAIGN_SOURCE, value);
	   	return (T) this;
	}
	public String campaignSource() {
		return getString(CAMPAIGN_SOURCE);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the campaign medium.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>cm</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>50 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>organic</code><br>
	 * 		Example usage: <code>cm=organic</code>
	 * 	</div>
	 * </div>
	 */
	public T campaignMedium(String value) {
		setString(CAMPAIGN_MEDIUM, value);
	   	return (T) this;
	}
	public String campaignMedium() {
		return getString(CAMPAIGN_MEDIUM);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the campaign keyword.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>ck</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>500 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>Blue Shoes</code><br>
	 * 		Example usage: <code>ck=Blue%20Shoes</code>
	 * 	</div>
	 * </div>
	 */
	public T campaignKeyword(String value) {
		setString(CAMPAIGN_KEYWORD, value);
	   	return (T) this;
	}
	public String campaignKeyword() {
		return getString(CAMPAIGN_KEYWORD);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the campaign content.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>cc</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>500 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>content</code><br>
	 * 		Example usage: <code>cc=content</code>
	 * 	</div>
	 * </div>
	 */
	public T campaignContent(String value) {
		setString(CAMPAIGN_CONTENT, value);
	   	return (T) this;
	}
	public String campaignContent() {
		return getString(CAMPAIGN_CONTENT);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the campaign ID.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>ci</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>100 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>ID</code><br>
	 * 		Example usage: <code>ci=ID</code>
	 * 	</div>
	 * </div>
	 */
	public T campaignId(String value) {
		setString(CAMPAIGN_ID, value);
	   	return (T) this;
	}
	public String campaignId() {
		return getString(CAMPAIGN_ID);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the Google AdWords Id.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>gclid</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>CL6Q-OXyqKUCFcgK2goddQuoHg</code><br>
	 * 		Example usage: <code>gclid=CL6Q-OXyqKUCFcgK2goddQuoHg</code>
	 * 	</div>
	 * </div>
	 */
	public T adwordsId(String value) {
		setString(ADWORDS_ID, value);
	   	return (T) this;
	}
	public String adwordsId() {
		return getString(ADWORDS_ID);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the Google Display Ads Id.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>dclid</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>d_click_id</code><br>
	 * 		Example usage: <code>dclid=d_click_id</code>
	 * 	</div>
	 * </div>
	 */
	public T displayadId(String value) {
		setString(DISPLAYAD_ID, value);
	   	return (T) this;
	}
	public String displayadId() {
		return getString(DISPLAYAD_ID);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the screen resolution.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>sr</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>20 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>800x600</code><br>
	 * 		Example usage: <code>sr=800x600</code>
	 * 	</div>
	 * </div>
	 */
	public T screenResolution(String value) {
		setString(SCREEN_RESOLUTION, value);
	   	return (T) this;
	}
	public String screenResolution() {
		return getString(SCREEN_RESOLUTION);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the viewable area of the browser / device.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>vp</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>20 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>123x456</code><br>
	 * 		Example usage: <code>vp=123x456</code>
	 * 	</div>
	 * </div>
	 */
	public T viewportSize(String value) {
		setString(VIEWPORT_SIZE, value);
	   	return (T) this;
	}
	public String viewportSize() {
		return getString(VIEWPORT_SIZE);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the character set used to encode the page / document.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>de</code></td>
	 * 				<td>text</td>
	 * 				<td><code>UTF-8</code>
	 * 				</td>
	 * 				<td>20 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>UTF-8</code><br>
	 * 		Example usage: <code>de=UTF-8</code>
	 * 	</div>
	 * </div>
	 */
	public T documentEncoding(String value) {
		setString(DOCUMENT_ENCODING, value);
	   	return (T) this;
	}
	public String documentEncoding() {
		return getString(DOCUMENT_ENCODING);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the screen color depth.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>sd</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>20 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>24-bits</code><br>
	 * 		Example usage: <code>sd=24-bits</code>
	 * 	</div>
	 * </div>
	 */
	public T screenColors(String value) {
		setString(SCREEN_COLORS, value);
	   	return (T) this;
	}
	public String screenColors() {
		return getString(SCREEN_COLORS);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the language.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>ul</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>20 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>en-us</code><br>
	 * 		Example usage: <code>ul=en-us</code>
	 * 	</div>
	 * </div>
	 */
	public T userLanguage(String value) {
		setString(USER_LANGUAGE, value);
	   	return (T) this;
	}
	public String userLanguage() {
		return getString(USER_LANGUAGE);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies whether Java was enabled.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>je</code></td>
	 * 				<td>boolean</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>1</code><br>
	 * 		Example usage: <code>je=1</code>
	 * 	</div>
	 * </div>
	 */
	public T javaEnabled(Boolean value) {
		setBoolean(JAVA_ENABLED, value);
	   	return (T) this;
	}
	public Boolean javaEnabled() {
		return getBoolean(JAVA_ENABLED);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the flash version.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>fl</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>20 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>10 1 r103</code><br>
	 * 		Example usage: <code>fl=10%201%20r103</code>
	 * 	</div>
	 * </div>
	 */
	public T flashVersion(String value) {
		setString(FLASH_VERSION, value);
	   	return (T) this;
	}
	public String flashVersion() {
		return getString(FLASH_VERSION);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		<strong>Required for all hit types.</strong>
	 * 	</p>
	 * 	<p>The type of hit. Must be one of 'pageview', 'appview', 'event', 'transaction', 'item', 'social', 'exception', 'timing'.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>t</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>pageview</code><br>
	 * 		Example usage: <code>t=pageview</code>
	 * 	</div>
	 * </div>
	 */
	public T hitType(String value) {
		setString(HIT_TYPE, value);
	   	return (T) this;
	}
	public String hitType() {
		return getString(HIT_TYPE);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies that a hit be considered non-interactive.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>ni</code></td>
	 * 				<td>boolean</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>1</code><br>
	 * 		Example usage: <code>ni=1</code>
	 * 	</div>
	 * </div>
	 */
	public T nonInteractionHit(String value) {
		setString(NON_INTERACTION_HIT, value);
	   	return (T) this;
	}
	public String nonInteractionHit() {
		return getString(NON_INTERACTION_HIT);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Use this parameter to send the full URL (document location) of the page on which content resides. You can use the &amp;dh and &amp;dp parameters to override the hostname and path + query portions of the document location, accordingly. The JavaScript clients determine this parameter using the concatenation of the document.location.origin + document.location.pathname + document.location.search browser parameters. Be sure to remove any user authentication or other private information from the URL if present.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>dl</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>2048 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>http://foo.com/home?a=b</code><br>
	 * 		Example usage: <code>dl=http%3A%2F%2Ffoo.com%2Fhome%3Fa%3Db</code>
	 * 	</div>
	 * </div>
	 */
	public T documentLocationUrl(String value) {
		setString(DOCUMENT_LOCATION_URL, value);
	   	return (T) this;
	}
	public String documentLocationUrl() {
		return getString(DOCUMENT_LOCATION_URL);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the hostname from which content was hosted.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>dh</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>100 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>foo.com</code><br>
	 * 		Example usage: <code>dh=foo.com</code>
	 * 	</div>
	 * </div>
	 */
	public T documentHostName(String value) {
		setString(DOCUMENT_HOST_NAME, value);
	   	return (T) this;
	}
	public String documentHostName() {
		return getString(DOCUMENT_HOST_NAME);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>The path portion of the page URL. Should begin with '/'.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>dp</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>2048 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>/foo</code><br>
	 * 		Example usage: <code>dp=%2Ffoo</code>
	 * 	</div>
	 * </div>
	 */
	public T documentPath(String value) {
		setString(DOCUMENT_PATH, value);
	   	return (T) this;
	}
	public String documentPath() {
		return getString(DOCUMENT_PATH);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>The title of the page / document.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>dt</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>1500 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>Settings</code><br>
	 * 		Example usage: <code>dt=Settings</code>
	 * 	</div>
	 * </div>
	 */
	public T documentTitle(String value) {
		setString(DOCUMENT_TITLE, value);
	   	return (T) this;
	}
	public String documentTitle() {
		return getString(DOCUMENT_TITLE);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>If not specified, this will default to the unique URL of the page by either using the &amp;dl parameter as-is or assembling it from &amp;dh and &amp;dp. App tracking makes use of this for the 'Screen Name' of the appview hit.</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>cd</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>2048 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>High Scores</code><br>
	 * 		Example usage: <code>cd=High%20Scores</code>
	 * 	</div>
	 * </div>
	 */
	public T contentDescription(String value) {
		setString(CONTENT_DESCRIPTION, value);
	   	return (T) this;
	}
	public String contentDescription() {
		return getString(CONTENT_DESCRIPTION);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the application name. Only visible in app views (profiles).</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>an</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>100 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>My App</code><br>
	 * 		Example usage: <code>an=My%20App</code>
	 * 	</div>
	 * </div>
	 */
	public T applicationName(String value) {
		setString(APPLICATION_NAME, value);
	   	return (T) this;
	}
	public String applicationName() {
		return getString(APPLICATION_NAME);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the application version. Only visible in app views (profiles).</p>
	 * 	<table>
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>av</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>100 Bytes
	 * 				</td>
	 * 				<td>all</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>1.2</code><br>
	 * 		Example usage: <code>av=1.2</code>
	 * 	</div>
	 * </div>
	 */
	public T applicationVersion(String value) {
		setString(APPLICATION_VERSION, value);
	   	return (T) this;
	}
	public String applicationVersion() {
		return getString(APPLICATION_VERSION);
	}
	/***************************** Above lines are auto generated based on ParameterGetterSetterGenerator */
}
