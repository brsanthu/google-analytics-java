package com.brsanthu.googleanalytics;

import com.brsanthu.googleanalytics.request.GoogleAnalyticsResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static com.brsanthu.googleanalytics.internal.Constants.TEST_TRACKING_ID;
import static org.junit.Assert.assertEquals;

public class GoogleAnalyticsDebugTest {

    private static GoogleAnalytics ga = null;

    @BeforeClass
    public static void setup() {
        ga = GoogleAnalytics.builder()
                .withTrackingId(TEST_TRACKING_ID).withAppName("Junit Test").withAppVersion("1.0.0")
                .withConfig(new GoogleAnalyticsConfig().setHttpsUrl("https://www.google-analytics.com/debug/collect"))
                .build();
    }

    @Test
    public void testPageViewDebug() throws Exception {
        GoogleAnalyticsResponse response = ga.pageView("http://www.google.com", "Search").send();
        String originalResponse = response.getHttpResponse().getOriginalResponse();
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(originalResponse);
        assertEquals(200, response.getStatusCode());
        assertEquals(true, ((Map) ((JSONArray) json.get("hitParsingResult")).get(0)).get("valid"));
    }

}
