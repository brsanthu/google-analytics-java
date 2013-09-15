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
import java.awt.Toolkit;
import java.awt.image.ColorModel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class  for sending data to google analytics
 */
public class GoogleAnalytics {

	private static final Charset UTF8 = Charset.forName("UTF-8");
	private static Logger logger = LoggerFactory.getLogger(GoogleAnalytics.class);

	private Config config = null;
	private Request defaultRequest = null;
	private HttpClient httpClient = new DefaultHttpClient();

	public GoogleAnalytics(String trackingId) {
		this(trackingId, null, null);
	}

	public GoogleAnalytics(String trackingId, String appName, String appVersion) {
		this(new Config(), new Request(null, trackingId, appName, appVersion));
	}

	public GoogleAnalytics(Config config, Request defaultRequest) {
		this.config = config;
		this.defaultRequest = defaultRequest;
		populateSystemParameters(config, defaultRequest);
	}

	public Config getConfig() {
		return config;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public Request getDefaultRequest() {
		return defaultRequest;
	}

	public Response send(Request request) {
		Response response = new Response();

		if (!config.isEnabled()) {
			return response;
		}

		try {
			//Combine request with default parms.
			Map<Parameter, String> parms = request.getParameters();
			Map<Parameter, String> defaultParms = defaultRequest.getParameters();
			for (Parameter parm : defaultParms.keySet()) {
				String value = parms.get(parm);
				String defaultValue = defaultParms.get(parm);
				if (isEmpty(value) && !isEmpty(defaultValue)) {
					parms.put(parm, defaultValue);
				}
			}

			HttpPost httpPost = new HttpPost(config.getUrl());
			List<NameValuePair> postParms = new ArrayList<NameValuePair>();
			for (Parameter key : parms.keySet()) {
				postParms.add(new BasicNameValuePair(key.getName(), parms.get(key)));
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
		if (!config.isEnabled()) {
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

	public void close() {
		getExecutor().shutdownNow();
	}

	protected Request populateSystemParameters(Config config2, Request defaultRequest) {
		try {
			if (isEmpty(defaultRequest.userLanguage())) {
			    String region = System.getProperty("user.region");
			    if (isEmpty(region)) {
			        region = System.getProperty("user.country");
			    }
			    defaultRequest.userLanguage(System.getProperty("user.language") + "-" + region);
			}

			if (isEmpty(defaultRequest.documentEncoding())) {
				defaultRequest.documentEncoding(System.getProperty("file.encoding"));
			}

			if (isEmpty(defaultRequest.screenResolution())) {
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				defaultRequest.screenResolution(((int) screenSize.getWidth()) + "x" + ((int) screenSize.getHeight()));
			}

			if (isEmpty(defaultRequest.screenColors())) {
				ColorModel colorModel = Toolkit.getDefaultToolkit().getColorModel();
				defaultRequest.screenColors(colorModel.toString());
			}
		} catch (Exception e) {
			logger.warn("Exception while deriving the System properties for request " + defaultRequest, e);
		}

		return defaultRequest;
	}
}