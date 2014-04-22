package com.brsanthu.googleanalytics;


public class GaUtils {
	public static boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}

	public static boolean isEmpty(String value) {
    	return value == null || value.trim().length() == 0;
    }

	public static StringBuilder appendSystemProperty(StringBuilder sb, String property) {
		String value = System.getProperty(property);
		if (isNotEmpty(value)) {
			if (isNotEmpty(sb.toString())) {
				sb.append("/");
			}
			sb.append(value);
		}
		
		return sb;
	}
	
	
}
