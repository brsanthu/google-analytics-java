package com.brsanthu.googleanalytics;

import static com.brsanthu.googleanalytics.internal.Constants.TEST_TRACKING_ID;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.brsanthu.googleanalytics.request.GoogleAnalyticsResponse;

public class GoogleAnalyticsDebugTest {

    private static GoogleAnalytics ga = null;

    @BeforeAll
    public static void setup() {
        ga = GoogleAnalytics.builder().withTrackingId(TEST_TRACKING_ID).withAppName("Junit Test").withAppVersion("1.0.0").withConfig(
                new GoogleAnalyticsConfig().setHitDebug(true)).build();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testPageViewDebug() throws Exception {
        GoogleAnalyticsResponse response = ga.pageView("http://www.google.com", "Search").send();

        // Sample response from GA
        // @formatter:off
        /*
        {
          "hitParsingResult": [ {
            "valid": true,
            "parserMessage": [ ],
            "hit": "/debug/collect?dt=Search\u0026de=UTF-8\u0026qt=1\u0026t=pageview\u0026av=1.0.0\u0026v=1\u0026ul=en-US\u0026dl=http://www.google.com\u0026an=Junit Test\u0026tid=UA-612100-12\u0026cid=7beb1a85-2a9e-440f-8e2d-5e91f8a7e708"
          } ],
          "parserMessage": [ {
            "messageType": "INFO",
            "description": "Found 1 hit in the request."
          } ]
        }
       */
        // @formatter:on
        String originalResponse = response.getHttpResponse().getBody();
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(originalResponse);
        assertEquals(200, response.getStatusCode());
        assertEquals(true, ((Map<String, Object>) ((JSONArray) json.get("hitParsingResult")).get(0)).get("valid"));
    }
}
