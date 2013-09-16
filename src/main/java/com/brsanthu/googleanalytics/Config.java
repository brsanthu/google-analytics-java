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


public class Config {
	private boolean enabled = true;
	private int maxThreads = 1;
	private boolean useHttps = true;
	private boolean validate = true;
	private String httpUrl = "http://www.google-analytics.com/collect";
	private String httpsUrl = "https://www.google-analytics.com/collect";
	private String userAgent = null;
	private String proxyHost = null;
	private int proxyPort = 0;
	private String proxyUserName = null;
	private String proxyPassword = null;
	private boolean deriveSystemProperties = true;

	public boolean isDeriveSystemProperties() {
		return deriveSystemProperties;
	}
	public void setDeriveSystemProperties(boolean deriveSystemProperties) {
		this.deriveSystemProperties = deriveSystemProperties;
	}
	public String getProxyUserName() {
		return proxyUserName;
	}
	public void setProxyUserName(String proxyUserName) {
		this.proxyUserName = proxyUserName;
	}
	public String getProxyPassword() {
		return proxyPassword;
	}
	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}
	public String getProxyHost() {
		return proxyHost;
	}
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}
	public int getProxyPort() {
		return proxyPort;
	}
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public int getMaxThreads() {
		return maxThreads;
	}
	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}
	public boolean isUseHttps() {
		return useHttps;
	}
	public void setUseHttps(boolean useHttps) {
		this.useHttps = useHttps;
	}
	public boolean isValidate() {
		return validate;
	}
	public void setValidate(boolean validate) {
		this.validate = validate;
	}
	public String getHttpUrl() {
		return httpUrl;
	}
	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}
	public String getHttpsUrl() {
		return httpsUrl;
	}
	public void setHttpsUrl(String httpsUrl) {
		this.httpsUrl = httpsUrl;
	}

	public String getUrl() {
		return useHttps?httpsUrl:httpUrl;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Config [enabled=");
		builder.append(enabled);
		builder.append(", maxThreads=");
		builder.append(maxThreads);
		builder.append(", useHttps=");
		builder.append(useHttps);
		builder.append(", validate=");
		builder.append(validate);
		builder.append(", ");
		if (httpUrl != null) {
			builder.append("httpUrl=");
			builder.append(httpUrl);
			builder.append(", ");
		}
		if (httpsUrl != null) {
			builder.append("httpsUrl=");
			builder.append(httpsUrl);
			builder.append(", ");
		}
		if (userAgent != null) {
			builder.append("userAgent=");
			builder.append(userAgent);
			builder.append(", ");
		}
		if (proxyHost != null) {
			builder.append("proxyHost=");
			builder.append(proxyHost);
			builder.append(", ");
		}
		builder.append("proxyPort=");
		builder.append(proxyPort);
		builder.append(", ");
		if (proxyUserName != null) {
			builder.append("proxyUserName=");
			builder.append(proxyUserName);
			builder.append(", ");
		}
		if (proxyPassword != null) {
			builder.append("proxyPassword=");
			builder.append(mask(proxyPassword));
			builder.append(", ");
		}
		builder.append("deriveSystemProperties=");
		builder.append(deriveSystemProperties);
		builder.append("]");
		return builder.toString();
	}

    public static String mask(String value) {
    	if (value == null) {
    		return null;
    	}

    	return "********";
    }

}
