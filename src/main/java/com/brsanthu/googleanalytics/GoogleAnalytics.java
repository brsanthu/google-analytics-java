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

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class  for sending data to google analytics
 */
public class GoogleAnalytics {

	private static final Charset UTF8 = Charset.forName("UTF-8");
	private static final Logger logger = LoggerFactory.getLogger(GoogleAnalytics.class);

	private GoogleAnalyticsConfig config = null;
	private GoogleAnalyticsRequest defaultRequest = null;
	private CloseableHttpClient httpClient = null;
	private ThreadPoolExecutor executor = null;
	private GoogleAnalyticsStats stats = new GoogleAnalyticsStats();

	public GoogleAnalytics(String trackingId) {
		this(new GoogleAnalyticsConfig(), new GoogleAnalyticsRequest().trackingId(trackingId));
	}

	public GoogleAnalytics(GoogleAnalyticsConfig config, String trackingId) {
		this(config, new GoogleAnalyticsRequest().trackingId(trackingId));
	}

	public GoogleAnalytics(String trackingId, String appName, String appVersion) {
		this(new GoogleAnalyticsConfig(), trackingId, appName, appVersion);
	}

	public GoogleAnalytics(GoogleAnalyticsConfig config, String trackingId, String appName, String appVersion) {
		this(config, new GoogleAnalyticsRequest().trackingId(trackingId).applicationName(appName).applicationVersion(appVersion));
	}

	public GoogleAnalytics(GoogleAnalyticsConfig config, GoogleAnalyticsRequest defaultRequest) {
		if (config.isDeriveSystemParameters()) {
			deriveSystemParameters(config, defaultRequest);
		}

		logger.info("Initializing Google Analytics with config=" + config + " and defaultRequest=" + defaultRequest);

		this.config = config;
		this.defaultRequest = defaultRequest;
		this.httpClient = createHttpClient(config);
	}

	public GoogleAnalyticsConfig getConfig() {
		return config;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public GoogleAnalyticsRequest getDefaultRequest() {
		return defaultRequest;
	}

	public void setDefaultRequest(GoogleAnalyticsRequest request) {
		this.defaultRequest = request;
	}

	public void setHttpClient(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public GoogleAnalyticsResponse post(AbstractRequest request) {
		GoogleAnalyticsResponse response = new GoogleAnalyticsResponse();
		if (!config.isEnabled()) {
			return response;
		}

		CloseableHttpResponse httpResponse = null;
		try {
			//Combine request with default parms.
			Map<GoogleAnalyticsParameter, String> parms = request.getParameters();
			Map<GoogleAnalyticsParameter, String> defaultParms = defaultRequest.getParameters();
			for (GoogleAnalyticsParameter parm : defaultParms.keySet()) {
				String value = parms.get(parm);
				String defaultValue = defaultParms.get(parm);
				if (isEmpty(value) && !isEmpty(defaultValue)) {
					parms.put(parm, defaultValue);
				}
			}

			logger.debug("Sending " + request);

			HttpPost httpPost;
			httpPost = new HttpPost(config.getUrl());
			List<NameValuePair> postParms = new ArrayList<NameValuePair>();
			for (GoogleAnalyticsParameter key : parms.keySet()) {
				postParms.add(new BasicNameValuePair(key.getName(), parms.get(key)));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(postParms, UTF8));

			httpResponse = (CloseableHttpResponse) httpClient.execute(httpPost);
			response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
			EntityUtils.consumeQuietly(httpResponse.getEntity());

			if (config.isGatherStats()) {
				gatherStats(request);
			}

		} catch (Exception e) {
			logger.warn("Exception while sending the Google Analytics tracker request " + request, e);
		} finally {
			try {
				httpResponse.close();
			} catch (Exception e2) {
				//ignore
			}
		}

		return response;
	}

	private void gatherStats(@SuppressWarnings("rawtypes") AbstractRequest request) {
		String hitType = request.getString(GoogleAnalyticsParameter.HIT_TYPE);

		if ("pageView".equalsIgnoreCase(hitType)) {
			stats.pageViewHit();

		} else if ("appView".equalsIgnoreCase(hitType)) {
			stats.appViewHit();

		} else if ("event".equalsIgnoreCase(hitType)) {
			stats.eventHit();

		} else if ("item".equalsIgnoreCase(hitType)) {
			stats.itemHit();

		} else if ("transaction".equalsIgnoreCase(hitType)) {
			stats.transactionHit();

		} else if ("social".equalsIgnoreCase(hitType)) {
			stats.socialHit();

		} else if ("timing".equalsIgnoreCase(hitType)) {
			stats.timingHit();
		}
	}

	public Future<GoogleAnalyticsResponse> postAsync(final RequestProvider requestProvider) {
		if (!config.isEnabled()) {
			return null;
		}

		Future<GoogleAnalyticsResponse> future = getExecutor().submit(new Callable<GoogleAnalyticsResponse>() {
			public GoogleAnalyticsResponse call() throws Exception {
				try {
					@SuppressWarnings("rawtypes")
					AbstractRequest request = requestProvider.getRequest();
					if (request != null) {
						return post(request);
					}
				} catch (Exception e) {
					logger.warn("Request Provider (" + requestProvider +") thrown exception " + e.toString() + " and hence nothing is posted to GA.");
				}

				return null;
			}
		});
		return future;
	}

	@SuppressWarnings("rawtypes")
	public Future<GoogleAnalyticsResponse> postAsync(final AbstractRequest request) {
		if (!config.isEnabled()) {
			return null;
		}

		Future<GoogleAnalyticsResponse> future = getExecutor().submit(new Callable<GoogleAnalyticsResponse>() {
			public GoogleAnalyticsResponse call() throws Exception {
				return post(request);
			}
		});
		return future;
	}

	private boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}

	private boolean isEmpty(String value) {
    	return value == null || value.trim().length() == 0;
    }

	public void close() {
		try {
			executor.shutdown();
		} catch (Exception e) {
			//ignore
		}

		try {
			httpClient.close();
		} catch (IOException e) {
			//ignore
		}
	}

	protected GoogleAnalyticsRequest deriveSystemParameters(GoogleAnalyticsConfig config, GoogleAnalyticsRequest request) {
		try {
			if (isEmpty(config.getUserAgent())) {
				config.setUserAgent(getUserAgentString());
			}

			if (isEmpty(request.userLanguage())) {
			    String region = System.getProperty("user.region");
			    if (isEmpty(region)) {
			        region = System.getProperty("user.country");
			    }
			    request.userLanguage(System.getProperty("user.language") + "-" + region);
			}

			if (isEmpty(request.documentEncoding())) {
				request.documentEncoding(System.getProperty("file.encoding"));
			}

			Toolkit toolkit = Toolkit.getDefaultToolkit();

			if (isEmpty(request.screenResolution())) {
				Dimension screenSize = toolkit.getScreenSize();
				request.screenResolution(((int) screenSize.getWidth()) + "x" + ((int) screenSize.getHeight()) + ", " + toolkit.getScreenResolution() + " dpi");
			}

			if (isEmpty(request.screenColors())) {
				GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();

				StringBuilder sb = new StringBuilder();
				for (GraphicsDevice graphicsDevice : graphicsDevices) {
					if (sb.length() != 0) {
						sb.append(", ");
					}
					sb.append(graphicsDevice.getDisplayMode().getBitDepth());
				}
				request.screenColors(sb.toString());
			}
		} catch (Exception e) {
			logger.warn("Exception while deriving the System properties for request " + request, e);
		}

		return request;
	}

	protected CloseableHttpClient createHttpClient(GoogleAnalyticsConfig config) {
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		connManager.setDefaultMaxPerRoute(Math.min(config.getMaxThreads(), 1));

		HttpClientBuilder builder = HttpClients.custom().setConnectionManager(connManager);

		if (isNotEmpty(config.getUserAgent())) {
			builder.setUserAgent(config.getUserAgent());
		}

		if (isNotEmpty(config.getProxyHost())) {
			builder.setProxy(new HttpHost(config.getProxyHost(), config.getProxyPort()));

			if (isNotEmpty(config.getProxyUserName())) {
				BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
				credentialsProvider.setCredentials(new AuthScope(config.getProxyHost(), config.getProxyPort()),
						new UsernamePasswordCredentials(config.getProxyUserName(), config.getProxyPassword()));
				builder.setDefaultCredentialsProvider(credentialsProvider);
			}
		}

		return builder.build();
	}

	protected ThreadPoolExecutor getExecutor() {
		if (executor == null) {
			executor = createExecutor(config);
		}
		return executor;
	}

	protected synchronized ThreadPoolExecutor createExecutor(GoogleAnalyticsConfig config) {
		return new ThreadPoolExecutor(0, config.getMaxThreads(), 5, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>(), createThreadFactory());
	}

	protected String getUserAgentString() {
		StringBuilder sb = new StringBuilder("java");
		appendProperty(sb, "java.runtime.version");
		appendProperty(sb, "java.specification.vendor");
		appendProperty(sb, "java.vm.name");
		appendProperty(sb, "os.name");
		appendProperty(sb, "os.version");
		appendProperty(sb, "os.arch");

		return sb.toString();
	}

	protected void appendProperty(StringBuilder sb, String property) {
		String value = System.getProperty(property);
		if (isNotEmpty(value)) {
			sb.append("/").append(value);
		}
	}

	protected ThreadFactory createThreadFactory() {
		return new GoogleAnalyticsThreadFactory(config.getThreadNameFormat());
	}

	public GoogleAnalyticsStats getStats() {
		return stats;
	}

	public void resetStats() {
		stats = new GoogleAnalyticsStats();
	}
}

class GoogleAnalyticsThreadFactory implements ThreadFactory {
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private String threadNameFormat = null;

    public GoogleAnalyticsThreadFactory(String threadNameFormat) {
    	this.threadNameFormat = threadNameFormat;
	}

	public Thread newThread(Runnable r) {
        Thread thread = new Thread(Thread.currentThread().getThreadGroup(), r, MessageFormat.format(threadNameFormat, threadNumber.getAndIncrement()), 0);
        thread.setDaemon(true);
        thread.setPriority(Thread.MIN_PRIORITY);
        return thread;
    }
}
