package com.brsanthu.googleanalytics.httpclient;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

public class BatchUrlEncodedFormEntity extends StringEntity {

    public BatchUrlEncodedFormEntity(List<List<NameValuePair>> parameters) {
        super(constructCombinedEntityString(parameters), ContentType.create(URLEncodedUtils.CONTENT_TYPE));
    }

    private static String constructCombinedEntityString(final List<List<NameValuePair>> parameters) {
        StringBuilder builder = new StringBuilder();

        for (List<? extends NameValuePair> param : parameters) {
            builder.append(URLEncodedUtils.format(param, StandardCharsets.UTF_8));
            builder.append("\r\n");
        }

        return builder.toString();
    }
}