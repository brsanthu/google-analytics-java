package com.brsanthu.googleanalytics.httpclient;

import static com.brsanthu.googleanalytics.internal.GaUtils.isNotEmpty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brsanthu.googleanalytics.GoogleAnalyticsConfig;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpClientImpl implements HttpClient {
    private static final Logger logger = LoggerFactory.getLogger(OkHttpClientImpl.class);

    // Recommended to be singleton
    private static OkHttpClient client;

    public OkHttpClientImpl(GoogleAnalyticsConfig config) {
        if (client == null) {
            client = createHttpClient(config);
        }
    }

    @Override
    public void close() throws Exception {
        // no close methods on OkHttp
    }

    /**
     * HTTP Client that uses the OkHttp library for request/response. <strong>Not
     * all GoogleAnalyticsCnofig options are supported</strong>. All unsupported
     * options in the configuration will be checked on first usage and a
     * RuntimeException will be thrown, so you may know your configuration is valid
     * after making its first request.
     *
     * @param config the configuration to use while building OkHttpClient
     * @return OkHttpClient to use for analytics hits
     * @throws RuntimeException if unsupported configurations are specified
     */
    protected OkHttpClient createHttpClient(GoogleAnalyticsConfig config) throws RuntimeException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        checkConfigForUnsupportedConfiguration(config);

        if (isNotEmpty(config.getProxyHost())) {
            builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.getProxyHost(), config.getProxyPort())));

            if (isNotEmpty(config.getProxyUserName())) {
                Authenticator proxyAuthenticator = new Authenticator() {

                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        String credential = Credentials.basic(config.getProxyUserName(), config.getProxyPassword());

                        return response.request().newBuilder().header("Proxy-Authorization", credential).build();
                    }
                };
                builder.proxyAuthenticator(proxyAuthenticator);
            }
        }

        return builder.build();
    }

    /**
     * OkHttp does not support everything Apache HttpClient supports at the library
     * level. Additionally, this implementation doesn't implement everything
     * supported in the ApacheHttpClientImpl
     *
     * This checks for unsupported configurations and throws immediately so
     * validation will occur at least on the first attempt to use the library
     *
     * @param config the configuration to check
     * @throws RuntimeException if any unsupported configurations are specified
     */
    private void checkConfigForUnsupportedConfiguration(GoogleAnalyticsConfig config) throws RuntimeException {

        if (config.getMaxHttpConnectionsPerRoute() != GoogleAnalyticsConfig.DEFAULT_MAX_HTTP_CONNECTIONS_PER_ROUTE) {
            throw new RuntimeException("Configuring maximum connections per route is not supported by OkHttp");
        }
        if (isNotEmpty(config.getUserAgent())) {
            throw new RuntimeException("GoogleAnalyticsConfig.userAgent is not currently supported");
        }
    }

    @Override
    public HttpResponse post(HttpRequest req) {
        HttpResponse resp = new HttpResponse();

        FormBody.Builder formBuilder = new FormBody.Builder(Charset.forName("UTF-8"));
        Map<String, String> reqParams = req.getBodyParams();
        for (String key : reqParams.keySet()) {
            if (logger.isTraceEnabled()) logger.trace("post() adding POST param " + key + " = " + reqParams.get(key));
            formBuilder.add(key, reqParams.get(key));
        }
        RequestBody body = formBuilder.build();

        Request request = new Request.Builder().url(req.getUrl()).post(body).build();
        if (logger.isDebugEnabled()) logger.debug("HttpClient.post() url/body: " + request.url() + " / " + renderBody(body));

        try {

            Response okResp = client.newCall(request).execute();
            if (logger.isDebugEnabled()) logger.debug("post() response code/success: " + okResp.code() + " / " + okResp.isSuccessful());
            resp.setStatusCode(okResp.code());
            okResp.close(); // this marks the response as consumed, and allows the connection to be re-used

        } catch (Exception e) {
            logger.warn("OkHttpClientImpl.post()/OkHttpClient.newCall() error", e);
        }

        return resp;
    }

    @Override
    public HttpBatchResponse postBatch(HttpBatchRequest batchReq) {
        HttpBatchResponse resp = new HttpBatchResponse();
        final okio.Buffer bodyBuffer = new okio.Buffer();

        // For each request in the batch, build up the encoded form string, and add it
        // to the buffer
        for (HttpRequest request : batchReq.getRequests()) {
            FormBody.Builder formBuilder = new FormBody.Builder(Charset.forName("UTF-8"));
            if (logger.isTraceEnabled()) logger.trace("postBatch() starting new request line");
            Map<String, String> reqParams = request.getBodyParams();
            for (String key : reqParams.keySet()) {
                if (logger.isTraceEnabled()) logger.trace("postBatch() adding POST param " + key + " = " + reqParams.get(key));
                formBuilder.add(key, reqParams.get(key));
            }
            if (logger.isTraceEnabled()) logger.trace("postBatch() finishing request line");

            try {
                formBuilder.build().writeTo(bodyBuffer);
            } catch (IOException ioe) {
                logger.warn("postBatch() error while rendering batch entry", ioe);
            }
            bodyBuffer.writeString("\r\n", Charset.forName("UTF-8"));
        }

        Request request = new Request.Builder().url(batchReq.getUrl()).post(RequestBody.create(null, bodyBuffer.readUtf8())).build();
        if (logger.isDebugEnabled()) logger.debug("HttpClient.postBatch() url/body: " + request.url() + " / " + renderBody(request.body()));

        try {

            Response okResp = client.newCall(request).execute();
            if (logger.isDebugEnabled()) logger.debug("postBatch() response code/success: " + okResp.code() + " / " + okResp.isSuccessful());

            resp.setStatusCode(okResp.code());
            okResp.close(); // this marks the response as consumed, and allows the connection to be re-used
        } catch (Exception e) {
            logger.warn("OkHttpClientImpl.postBatch()/OkHttpClient.newCall() error", e);
        }

        return resp;
    }

    @Override
    public boolean isBatchSupported() {
        return true;
    }

    private String renderBody(RequestBody requestBody) {
        try {
            final okio.Buffer buffer = new okio.Buffer();
            requestBody.writeTo(buffer);
            return buffer.readUtf8();
        } catch (IOException ioe) {
            logger.warn("renderBody() error writing body contents out", ioe);
            return "";
        }
    }
}
