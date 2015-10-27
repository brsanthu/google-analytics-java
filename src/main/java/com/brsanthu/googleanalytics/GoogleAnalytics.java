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

import static com.brsanthu.googleanalytics.GaUtils.isEmpty;
import static com.brsanthu.googleanalytics.GaUtils.isNotEmpty;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
 * This is the main class of this library that accepts the requests from clients and
 * sends the events to Google Analytics (GA).
 *
 * Clients needs to instantiate this object with {@link GoogleAnalyticsConfig} and {@link DefaultRequest}.
 * Configuration contains sensible defaults so one could just initialize using one of the convenience constructors.
 *
 * This object is ThreadSafe and it is intended that clients create one instance of this for each GA Tracker Id
 * and reuse each time an event needs to be posted.
 *
 * This object contains resources which needs to be shutdown/disposed. So {@link #close()} method is called
 * to release all resources. Once close method is called, this instance cannot be reused so create new instance
 * if required.
 */
public class GoogleAnalytics {

	private static final Charset UTF8 = Charset.forName("UTF-8");
	private static final Logger logger = LoggerFactory.getLogger(GoogleAnalytics.class);

	private GoogleAnalyticsConfig config = null;
	private DefaultRequest defaultRequest = null;
	private CloseableHttpClient httpClient = null;
	private ThreadPoolExecutor executor = null;
	private GoogleAnalyticsStats stats = new GoogleAnalyticsStats();

	public GoogleAnalytics(String trackingId) {
		this(new GoogleAnalyticsConfig(), new DefaultRequest().trackingId(trackingId));
	}

	public GoogleAnalytics(GoogleAnalyticsConfig config, String trackingId) {
		this(config, new DefaultRequest().trackingId(trackingId));
	}

	public GoogleAnalytics(String trackingId, String appName, String appVersion) {
		this(new GoogleAnalyticsConfig(), trackingId, appName, appVersion);
	}

	public GoogleAnalytics(GoogleAnalyticsConfig config, String trackingId, String appName, String appVersion) {
		this(config, new DefaultRequest().trackingId(trackingId).applicationName(appName).applicationVersion(appVersion));
	}

	public GoogleAnalytics(GoogleAnalyticsConfig config, DefaultRequest defaultRequest) {
		if (config.isDiscoverRequestParameters() && config.getRequestParameterDiscoverer() != null) {
			config.getRequestParameterDiscoverer().discoverParameters(config, defaultRequest);
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

	public DefaultRequest getDefaultRequest() {
		return defaultRequest;
	}

	public void setDefaultRequest(DefaultRequest request) {
		this.defaultRequest = request;
	}

	public void setHttpClient(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public GoogleAnalyticsResponse post(GoogleAnalyticsRequest request) {
		GoogleAnalyticsResponse response = new GoogleAnalyticsResponse();
		if (!config.isEnabled()) {
			return response;
		}

		CloseableHttpResponse httpResponse = null;
		try {
			List<NameValuePair> postParms = new ArrayList<NameValuePair>();

			logger.debug("Processing " + request);

			//Process the parameters
			processParameters(request, postParms);

			//Process custom dimensions
			processCustomParameters(request, postParms, defaultRequest.customDimensions, request.customDimensions);

			//Process custom metrics
			processCustomParameters(request, postParms, defaultRequest.customMetrics, request.customMetrics);

			// Process productParameters
			processCustomSubParameters(request, postParms);

			logger.debug("Processed all parameters and sending the request " + postParms);
			
			HttpPost httpPost = new HttpPost(config.getUrl());
			httpPost.setEntity(new UrlEncodedFormEntity(postParms, UTF8));

			httpResponse = (CloseableHttpResponse) httpClient.execute(httpPost);
			response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
			response.setPostedParms(postParms);
			
			EntityUtils.consumeQuietly(httpResponse.getEntity());

			if (config.isGatherStats()) {
				gatherStats(request);
			}

		} catch (Exception e) {
			if (e instanceof UnknownHostException) {
				logger.warn("Coudln't connect to Google Analytics. Internet may not be available. " + e.toString());
			} else {
				logger.warn("Exception while sending the Google Analytics tracker request " + request, e);
			}
		} finally {
			try {
				httpResponse.close();
			} catch (Exception e2) {
				//ignore
			}
		}

		return response;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void processParameters(GoogleAnalyticsRequest request, List<NameValuePair> postParms) {
		Map<GoogleAnalyticsParameter, String> requestParms = request.getParameters();
		Map<GoogleAnalyticsParameter, String> defaultParms = defaultRequest.getParameters();
		for (GoogleAnalyticsParameter parm : defaultParms.keySet()) {
			String value = requestParms.get(parm);
			String defaultValue = defaultParms.get(parm);
			if (isEmpty(value) && !isEmpty(defaultValue)) {
				requestParms.put(parm, defaultValue);
			}
		}
		for (GoogleAnalyticsParameter key : requestParms.keySet()) {
			postParms.add(new BasicNameValuePair(key.getParameterName(), requestParms.get(key)));
		}
	}
	
	/**
	 * Processes the custom dimensions/metrics and adds the values to list of parameters, which would be posted to GA.
	 * 
	 * @param request
	 * @param postParms
	 * @param defaultCustomElements
	 * @param requestCustomElements
	 */
	private void processCustomParameters(@SuppressWarnings("rawtypes") GoogleAnalyticsRequest request, List<NameValuePair> postParms,
			Map<String, String> defaultCustomElements, Map<String, String> requestCustomElements) {

		Map<String, String> customParms = new HashMap<String, String>();
		customParms.putAll(defaultCustomElements);
		customParms.putAll(requestCustomElements);
		
		for (Entry<String, String> e : customParms.entrySet()) {
			postParms.add(new BasicNameValuePair(e.getKey(), e.getValue()));
		}
	}

	private void processCustomSubParameters(@SuppressWarnings("rawtypes") GoogleAnalyticsRequest request, List<NameValuePair> postParms) {

		int productIndex = 0;

		@SuppressWarnings("unchecked")
		Map<Integer, SubParameters> productParametersMap = request.getProductParameters();
		for (Entry<Integer, SubParameters> prodocutParametersEntry : productParametersMap.entrySet()) {

			productIndex = prodocutParametersEntry.getKey();

			for (Entry<GoogleAnalyticsParameter, String> productParametersValueEntry
					: prodocutParametersEntry.getValue().getParameters().entrySet()) {

				String key = productParametersValueEntry.getKey().getParameterName()
						.replace("#", Integer.toString(productIndex));

				postParms.add(new BasicNameValuePair(key, productParametersValueEntry.getValue()));
			}

			customDimensionAndMetrics(prodocutParametersEntry.getValue(), postParms, 0, productIndex);

		}

		productIndex = 0;

		@SuppressWarnings("unchecked")
		Map<Integer, SubParameters> productImpressionParametersMap = request.getProductImpressionParameters();
		for (Entry<Integer, SubParameters> prodocutImpressionParametersEntry : productImpressionParametersMap.entrySet()) {

			int listIndex = prodocutImpressionParametersEntry.getKey();

			Map<Integer, SubParameters> productImpressionProductParametersMap = prodocutImpressionParametersEntry.getValue().subParameters();
			for (Entry<Integer, SubParameters> prodocutParametersEntry : productImpressionProductParametersMap.entrySet()) {

				productIndex = prodocutParametersEntry.getKey();

				for (Entry<GoogleAnalyticsParameter, String> productParametersValueEntry
						: prodocutParametersEntry.getValue().getParameters().entrySet()) {

					String key = productParametersValueEntry.getKey().getParameterName()
							.replace("#", Integer.toString(productIndex))
							.replace("$", Integer.toString(listIndex));

					postParms.add(new BasicNameValuePair(key, productParametersValueEntry.getValue()));
				}
				
				customDimensionAndMetrics(prodocutParametersEntry.getValue(), postParms, listIndex, productIndex);

			}

		}

		int promoIndex = 0;

		@SuppressWarnings("unchecked")
		Map<Integer, SubParameters> promotionParametersMap = request.getPromotionParameters();
		for (Entry<Integer, SubParameters> promotionParametersEntry : promotionParametersMap.entrySet()) {

			promoIndex = promotionParametersEntry.getKey();

			for (Entry<GoogleAnalyticsParameter, String> productParametersValueEntry
					: promotionParametersEntry.getValue().getParameters().entrySet()) {

				String key = productParametersValueEntry.getKey().getParameterName()
						.replace("&", Integer.toString(promoIndex));

				postParms.add(new BasicNameValuePair(key, productParametersValueEntry.getValue()));
			}
		}
	}
	
	private void customDimensionAndMetrics(SubParameters parameterSet, List<NameValuePair> postParms,
			int listIndex, int productIndex) {
		
		// custom dimensions
		for (Entry<Integer, String> customDimensionEntry : parameterSet.customDimensions().entrySet()) {
			int customIndex = customDimensionEntry.getKey();
			String key = GoogleAnalyticsParameter.PRODUCT_IMPRESSION_CUSTOM_DIMENSION.getParameterName()
					.replace("#", Integer.toString(productIndex))
					.replace("$", Integer.toString(listIndex))
					.replace("§", Integer.toString(customIndex))
				+ customDimensionEntry.getKey();
			
			postParms.add(new BasicNameValuePair(key, customDimensionEntry.getValue()));
		}

		// custom metrics
		for (Entry<Integer, Integer> customMetricsEntry : parameterSet.customMetrics().entrySet()) {
			int customIndex = customMetricsEntry.getKey();
			String key = GoogleAnalyticsParameter.PRODUCT_IMPRESSION_CUSTOM_METRIC.getParameterName()
					.replace("#", Integer.toString(productIndex))
					.replace("$", Integer.toString(listIndex))
					.replace("§", Integer.toString(customIndex))
				+ customMetricsEntry.getKey();

			postParms.add(new BasicNameValuePair(key, Integer.toString(customMetricsEntry.getValue())));
		}
	}

	private void gatherStats(@SuppressWarnings("rawtypes") GoogleAnalyticsRequest request) {
		String hitType = request.hitType();

		if ("pageview".equalsIgnoreCase(hitType)) {
			stats.pageViewHit();

		} else if ("appview".equalsIgnoreCase(hitType)) {
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
					GoogleAnalyticsRequest request = requestProvider.getRequest();
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
	public Future<GoogleAnalyticsResponse> postAsync(final GoogleAnalyticsRequest request) {
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

	protected CloseableHttpClient createHttpClient(GoogleAnalyticsConfig config) {
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		connManager.setDefaultMaxPerRoute(getDefaultMaxPerRoute(config));

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

	protected int getDefaultMaxPerRoute(GoogleAnalyticsConfig config) {
		return Math.max(config.getMaxThreads(), 1);
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
