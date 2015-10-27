package com.brsanthu.googleanalytics;

import java.util.HashMap;
import java.util.Map;

public class SubParameters {

	protected Map<GoogleAnalyticsParameter, String> parms = new HashMap<GoogleAnalyticsParameter, String>();
	protected Map<Integer, String> customDimensions = new HashMap<Integer, String>();
	protected Map<Integer, Integer> customMetrics = new HashMap<Integer, Integer>();
	
	protected Map<Integer, SubParameters> subParameters = new HashMap<Integer, SubParameters>();

	// general parameters
	public String parameter(GoogleAnalyticsParameter parameter) {
		return parms.get(parameter);
	}

	public void parameter(GoogleAnalyticsParameter parameter, String value) {
		if (value == null) {
			parms.remove(parameter);
		} else {
			parms.put(parameter, value);
		}
	}

	public Map<GoogleAnalyticsParameter, String> getParameters() {
		return parms;
	}

	// custom dimensions
	public String customDimension(int index) {
		return customDimensions.get(index);
	}

	public void customDimension(int index, String value) {
		customDimensions.put(index, value);
	}

	public Map<Integer, String> customDimensions() {
		return customDimensions;
	}

	// custom metrics
	public int customMetric(int index) {
		return customMetrics.get(index);
	}

	public void customMetric(int index, int value) {
		customMetrics.put(index, value);
	}

	public Map<Integer, Integer> customMetrics() {
		return customMetrics;
	}

	// sub parameters
	public SubParameters subParameters(int index) {
		return subParameters.get(index);
	}

	public void subParameters(int index, SubParameters value) {
		subParameters.put(index, value);
	}

	public Map<Integer, SubParameters> subParameters() {
		return subParameters;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SubParameters [");
		if (parms != null) {
			builder.append("parms=");
			builder.append(parms);
			builder.append(", ");
		}
		if (customDimensions != null) {
			builder.append("customDimensions=");
			builder.append(customDimensions);
			builder.append(", ");
		}
		if (customMetrics != null) {
			builder.append("customMetrics=");
			builder.append(customMetrics);
		}
		if (subParameters != null) {
			builder.append("subParameters=");
			builder.append(subParameters);
		}
		builder.append("]");
		return builder.toString();
	}

}
