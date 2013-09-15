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

/**
 * Analytics request parameter specific constants.
 */
public interface Parameters {
	String PROTOCAL_VERSION = "v";
	String TRACKING_ID = "tid";
	String CLIENT_ID = "cid";
	String APPLICATION_NAME = "an";
	String APPLICATION_VERSION = "av";
	String HIT_TYPE = "t";
	String EVENT_CATEGORY = "ec";
	String EVENT_ACTION= "ea";
	String EVENT_LABEL= "el";
	String EVENT_VALUE= "ev";
	String DEFAULT_VERSION = "1.0.0";
	String ANONYMIZE_IP = "aip";
	String QUEUE_TIME = "qt";
	String CACHE_BUSTER = "z";
}