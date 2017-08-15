package com.brsanthu.googleanalytics.discovery;

import static com.brsanthu.googleanalytics.internal.GaUtils.appendSystemProperty;
import static com.brsanthu.googleanalytics.internal.GaUtils.isEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brsanthu.googleanalytics.GoogleAnalyticsConfig;
import com.brsanthu.googleanalytics.request.DefaultRequest;

/**
 * Default request parameter discoverer. Discovers following parameters.
 * <ul>
 * <li>Creates User Agent as java/1.6.0_45-b06/Sun Microsystems Inc./Java HotSpot(TM) 64-Bit Server VM/Windows
 * 7/6.1/amd64</li>
 * <li>User Language, and Country</li>
 * <li>File Encoding</li>
 * </ul>
 * 
 * @author Santhosh Kumar
 */
public class DefaultRequestParameterDiscoverer implements RequestParameterDiscoverer {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRequestParameterDiscoverer.class);
    public static final DefaultRequestParameterDiscoverer INSTANCE = new DefaultRequestParameterDiscoverer();

    @Override
    public DefaultRequest discoverParameters(GoogleAnalyticsConfig config, DefaultRequest request) {
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

        } catch (Exception e) {
            logger.warn("Exception while deriving the System properties for request " + request, e);
        }

        return request;
    }

    protected String getUserAgentString() {
        StringBuilder sb = new StringBuilder("java");
        appendSystemProperty(sb, "java.runtime.version");
        appendSystemProperty(sb, "java.specification.vendor");
        appendSystemProperty(sb, "java.vm.name");
        appendSystemProperty(sb, "os.name");
        appendSystemProperty(sb, "os.version");
        appendSystemProperty(sb, "os.arch");

        return sb.toString();
    }

}
