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
package com.brsanthu.common.googleanalytics;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Request {

	private final static String CLIENT_ID = UUID.randomUUID().toString();
	private Map<String, String> parms = new HashMap<String, String>();

	public Request() {
		setClientId(CLIENT_ID);
		setProtocolVersion("1");
		setHitType("pageView");
	}

	public void setTrackingId(String value) {
		parms.put(Parameters.TRACKING_ID, value);
	}

	public String getTrackingId() {
		return parms.get(Parameters.TRACKING_ID);
	}

	public void setAppName(String value) {
		parms.put(Parameters.APPLICATION_NAME, value);
	}

	public String getAppName() {
		return parms.get(Parameters.APPLICATION_NAME);
	}

	public void setAppVersion(String value) {
		parms.put(Parameters.APPLICATION_VERSION, value);
	}

	public String getAppVersion() {
		return parms.get(Parameters.APPLICATION_VERSION);
	}

	public void setClientId(String value) {
		parms.put(Parameters.CLIENT_ID, value);
	}

	public String getClientId() {
		return parms.get(Parameters.CLIENT_ID);
	}

	public void setProtocolVersion(String value) {
		parms.put(Parameters.PROTOCAL_VERSION, value);
	}

	public String getProtocolVersion() {
		return parms.get(Parameters.PROTOCAL_VERSION);
	}

	public Map<String, String> getParameters() {
		return parms;
	}

	public void setHitType(String value) {
		parms.put(Parameters.HIT_TYPE, value);
	}

	public String getHitType() {
		return parms.get(Parameters.HIT_TYPE);
	}

	public void validate() {
		//
	}


}
