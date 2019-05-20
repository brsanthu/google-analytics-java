/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.brsanthu.googleanalytics.request;

import static com.brsanthu.googleanalytics.request.GoogleAnalyticsParameter.SCREEN_NAME;

/**
 * GA request to track a typical web page view
 *
 * <p>
 * For more information, see
 * <a href="https://developers.google.com/analytics/devguides/collection/protocol/v1/parameters#content">GA Parameters
 * Reference</a>
 * </p>
 *
 * @author Santhosh Kumar
 */
public class ScreenViewHit extends GoogleAnalyticsRequest<ScreenViewHit> {
    public ScreenViewHit() {
        this(null, null);
    }

    public ScreenViewHit(String appName, String screenName) {
        super(HIT_SCREENVIEW);
        screenName(screenName);
        applicationName(appName);
    }

    public ScreenViewHit screenName(String value) {
        setString(SCREEN_NAME, value);
        return this;
    }

    public String screenName() {
        return getString(SCREEN_NAME);
    }

}
