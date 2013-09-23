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

/**
 * Any exception thrown (usually due to validation), it would be of this type.
 *
 * @author Santhosh Kumar
 */
public class GoogleAnalyticsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GoogleAnalyticsException() {
		super();
	}

	public GoogleAnalyticsException(String message, Throwable cause) {
		super(message, cause);
	}

	public GoogleAnalyticsException(String message) {
		super(message);
	}

	public GoogleAnalyticsException(Throwable cause) {
		super(cause);
	}
}
