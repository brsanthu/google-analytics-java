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

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * This class  for sending data to google analytics
 */
public class GoogleAnalytics implements Closeable{

	private static final Charset UTF8 = Charset.forName("UTF-8");
	private static final String HTTP_URL = "http://www.google-analytics.com/collect";
	private static final String HTTPS_URL = "https://www.google-analytics.com/collect";
	private final static Random random = new Random((long) (Math.random() * Long.MAX_VALUE));

	private Request defaultRequest = null;
	private AtomicBoolean enabled = new AtomicBoolean(true);
	private int maxThreads = 1;
	private boolean useHttps = true;
	private HttpClient httpClient = new DefaultHttpClient();

	public GoogleAnalytics() {
		this(new Request());
	}

	public GoogleAnalytics(String trackingId, String appName, String appVersion) {
		defaultRequest = new Request();
		defaultRequest.setTrackingId(trackingId);
		defaultRequest.setAppName(appName);
		defaultRequest.setAppVersion(appVersion);
	}

	public GoogleAnalytics(Request defaultRequest) {
		this.defaultRequest = defaultRequest;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public int getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(int threads) {
		this.maxThreads = threads;
	}

	public void setEnabled(boolean enabled) {
		this.enabled.set(enabled);
	}

	public boolean isEnabled() {
		return this.enabled.get();
	}

	public Request getDefaultRequest() {
		return defaultRequest;
	}

	public Response send(Request request) {
		Response response = new Response();

		if (!enabled.get() ) {
			return response;
		}

		try {
			//Combine request with default parms.
			Map<String, String> parms = request.getParameters();
			Map<String, String> defaultParms = defaultRequest.getParameters();
			for (String parm : defaultParms.keySet()) {
				String value = parms.get(parm);
				String defaultValue = defaultParms.get(parm);
				if (isEmpty(value) && !isEmpty(defaultValue)) {
					parms.put(parm, defaultValue);
				}
			}

			request.validate();

			HttpPost httpPost = new HttpPost(useHttps?HTTPS_URL:HTTP_URL);
			List<NameValuePair> postParms = new ArrayList<NameValuePair>();
			for (String key : parms.keySet()) {
				postParms.add(new BasicNameValuePair(key, parms.get(key)));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(postParms, UTF8));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
			response.setBody(EntityUtils.toString(httpResponse.getEntity(), UTF8));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return response;
	}

	public Future<Response> post(final Request request) {
		if (!enabled.get() ) {
			return null;
		}

		Future<Response> future = getExecutor().submit(new Callable<Response>() {
			public Response call() throws Exception {
				return send(request);
			}
		});
		return future;
	}

	//http://stackoverflow.com/questions/9789247/concurrency-for-a-class-with-static-methods-and-initialization-method
	public static ThreadPoolExecutor getExecutor() {
         return ExecutorDelegate.executor;
    }

    private static class ExecutorDelegate {
    	private static ThreadPoolExecutor executor = null;
		static {
			executor = new ThreadPoolExecutor(0, 1, 5, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>());
		}
    }

    private boolean isEmpty(String value) {
    	return value == null || value.trim().length() == 0;
    }

	public void close() throws IOException {
		getExecutor().shutdownNow();
	}
}