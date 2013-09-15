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

public class Event extends Request {

	public Event (String eventCategory, String eventAction) {
		this(eventCategory, eventAction, null, null);
	}

	public Event (String eventCategory, String eventAction, String eventLabel, Integer eventValue) {
		super("event");
		eventCategory(eventCategory);
		eventAction(eventAction);
		eventLabel(eventAction);
		eventValue(eventValue);
	}
}
