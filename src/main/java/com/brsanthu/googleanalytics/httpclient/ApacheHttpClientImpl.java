package com.brsanthu.googleanalytics.httpclient;

import static com.brsanthu.googleanalytics.internal.GaUtils.isNotEmpty;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.brsanthu.googleanalytics.GoogleAnalyticsConfig;

public class ApacheHttpClientImpl implements HttpClient {

	public ApacheHttpClientImpl() {

	}

	@Override
	public HttpResponse post(HttpRequest req) {
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

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
}
