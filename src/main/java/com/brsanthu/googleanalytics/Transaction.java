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

import static com.brsanthu.googleanalytics.Parameter.CURRENCY_CODE;
import static com.brsanthu.googleanalytics.Parameter.TRANSACTION_AFFILIATION;
import static com.brsanthu.googleanalytics.Parameter.TRANSACTION_ID;
import static com.brsanthu.googleanalytics.Parameter.TRANSACTION_REVENUE;
import static com.brsanthu.googleanalytics.Parameter.TRANSACTION_SHIPPING;
import static com.brsanthu.googleanalytics.Parameter.TRANSACTION_TAX;

public class Transaction extends AbstractRequest<Transaction> {

	public Transaction() {
		super("transaction");
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		<strong>Required for transaction hit type.</strong>
	 * 		<br>
	 * 		<strong>Required for item hit type.</strong>
	 * 	</p>
	 * 	<p>A unique identifier for the transaction. This value should be the same for both the Transaction hit and Items hits associated to the particular transaction.</p>
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
	 * 				<td><code>ti</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>500 Bytes
	 * 				</td>
	 * 				<td>transaction, item</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>OD564</code><br>
	 * 		Example usage: <code>ti=OD564</code>
	 * 	</div>
	 * </div>
	 */
	public Transaction txId(String value) {
		setString(TRANSACTION_ID, value);
	   	return this;
	}
	public String txId() {
		return getString(TRANSACTION_ID);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the affiliation or store name.</p>
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
	 * 				<td><code>ta</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>500 Bytes
	 * 				</td>
	 * 				<td>transaction</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>Member</code><br>
	 * 		Example usage: <code>ta=Member</code>
	 * 	</div>
	 * </div>
	 */
	public Transaction txAffiliation(String value) {
		setString(TRANSACTION_AFFILIATION, value);
	   	return this;
	}
	public String txAffiliation() {
		return getString(TRANSACTION_AFFILIATION);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the total revenue associated with the transaction. This value should include any shipping or tax costs.</p>
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
	 * 				<td><code>tr</code></td>
	 * 				<td>currency</td>
	 * 				<td><code>0</code>
	 * 				</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>transaction</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>15.47</code><br>
	 * 		Example usage: <code>tr=15.47</code>
	 * 	</div>
	 * </div>
	 */
	public Transaction txRevenue(Double value) {
		setDouble(TRANSACTION_REVENUE, value);
	   	return this;
	}
	public Double txRevenue() {
		return getDouble(TRANSACTION_REVENUE);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the total shipping cost of the transaction.</p>
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
	 * 				<td><code>ts</code></td>
	 * 				<td>currency</td>
	 * 				<td><code>0</code>
	 * 				</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>transaction</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>3.50</code><br>
	 * 		Example usage: <code>ts=3.50</code>
	 * 	</div>
	 * </div>
	 */
	public Transaction txShipping(Double value) {
		setDouble(TRANSACTION_SHIPPING, value);
	   	return this;
	}
	public Double txShipping() {
		return getDouble(TRANSACTION_SHIPPING);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>Specifies the total tax of the transaction.</p>
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
	 * 				<td><code>tt</code></td>
	 * 				<td>currency</td>
	 * 				<td><code>0</code>
	 * 				</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>transaction</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>11.20</code><br>
	 * 		Example usage: <code>tt=11.20</code>
	 * 	</div>
	 * </div>
	 */
	public Transaction txTax(Double value) {
		setDouble(TRANSACTION_TAX, value);
	   	return this;
	}
	public Double txTax() {
		return getDouble(TRANSACTION_TAX);
	}

	/**
	 * <div class="ind">
	 * 	<p>
	 * 		Optional.
	 * 	</p>
	 * 	<p>When present indicates the local currency for all transaction currency values. Value should be a valid ISO 4217 currency code.</p>
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
	 * 				<td><code>cu</code></td>
	 * 				<td>text</td>
	 * 				<td><span class="none">None</span>
	 * 				</td>
	 * 				<td>10 Bytes
	 * 				</td>
	 * 				<td>transaction, item</td>
	 * 			</tr>
	 * 		</tbody>
	 * 	</table>
	 * 	<div>
	 * 		Example value: <code>EUR</code><br>
	 * 		Example usage: <code>cu=EUR</code>
	 * 	</div>
	 * </div>
	 */
	public Transaction currencyCode(String value) {
		setString(CURRENCY_CODE, value);
	   	return this;
	}
	public String currencyCode() {
		return getString(CURRENCY_CODE);
	}

}
