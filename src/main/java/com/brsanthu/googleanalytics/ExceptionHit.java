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

import static com.brsanthu.googleanalytics.GoogleAnalyticsParameter.EXCEPTION_DESCRIPTION;
import static com.brsanthu.googleanalytics.GoogleAnalyticsParameter.EXCEPTION_FATAL;

/**
 * GA request to track exceptions.
 *
 * <p>For more information, see <a href="https://developers.google.com/analytics/devguides/collection/protocol/v1/parameters#exception">GA Parameters Reference</a></p>
 *
 * @author Santhosh Kumar
 */
public class ExceptionHit extends GoogleAnalyticsRequest<ExceptionHit> {

	public ExceptionHit() {
		this(null);
	}

	public ExceptionHit(String exceptionDescription) {
		this(exceptionDescription, false);
	}

	public ExceptionHit(String exceptionDescription, Boolean fatal) {
		super("exception");
		exceptionDescription(exceptionDescription);
		exceptionFatal(fatal);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the description of an exception.</p>
	 * 	<table border="1">
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>exd</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>150 Bytes
	 * 				</td>
	 * 				<td>exception</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>DatabaseError</code><br>
	 * 		Example usage: <code>exd=DatabaseError</code>
	 * 	</div>
	 * </div>
	 */
	public ExceptionHit exceptionDescription(String value) {
		setString(EXCEPTION_DESCRIPTION, value);
	   	return this;
	}
	public String exceptionDescription() {
		return getString(EXCEPTION_DESCRIPTION);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies whether the exception was fatal.</p>
	 * 	<table border="1">
	 * 		<tbody>
	 * 			<tr>
	 * 				<th>Parameter</th>
	 * 				<th>Value Type</th>
	 * 				<th>Default Value</th>
	 * 				<th>Max Length</th>
	 * 				<th>Supported Hit Types</th>
	 * 			</tr>
	 * 			<tr>
	 * 				<td><code>exf</code></td>
	 * 				<td>boolean</td>
	 * 				<td><code>1</code>
	 * 				</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>exception</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>0</code><br>
	 * 		Example usage: <code>exf=0</code>
	 * 	</div>
	 * </div>
	 */
	public ExceptionHit exceptionFatal(Boolean value) {
		setBoolean(EXCEPTION_FATAL, value);
	   	return this;
	}
	public Boolean exceptionFatal() {
		return getBoolean(EXCEPTION_FATAL);
	}
}
