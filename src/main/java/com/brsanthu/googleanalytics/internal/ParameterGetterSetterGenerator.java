package com.brsanthu.googleanalytics.internal;

import com.brsanthu.googleanalytics.Parameter;

public class ParameterGetterSetterGenerator {

	public static void main(String[] args) {
		Parameter[] enumConstants = Parameter.class.getEnumConstants();
		for (Parameter parameter : enumConstants) {
			String methodName = null; //CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, parameter.toString());
			String constName = parameter.toString();

			String type = "String";

			if (parameter.getType().equalsIgnoreCase("integer")) {
				type = "Integer";

			} else if (parameter.getType().equalsIgnoreCase("boolean")) {
				type = "Boolean";

			} else if (parameter.getType().equalsIgnoreCase("currency")) {
				type = "Double";
			}

			System.out.println("public Request " + methodName  +"(" + type + " value) {");

			System.out.println("    if(value == null) {");
			System.out.println("        parms.remove(" + constName + ");");
			System.out.println("    } else {");

			if (type.equals("Integer")) {
				System.out.println("	String stringValue = fromInteger(value);");

			} else if (type.equals("Boolean")) {
				System.out.println("	String stringValue = fromBoolean(value);");

			} else if (type.equals("Double")){
				System.out.println("	String stringValue = fromDouble(value);");

			} else {
				System.out.println("	String stringValue = value;");
			}
			System.out.println("    	parms.put(" + parameter.toString() + ", stringValue);");
			System.out.println("	}");
			System.out.println("    	return this;");
			System.out.println("	}");

			System.out.println("public " + type + " " + methodName  +"() {");

			if (type.equals("Integer")) {
				System.out.println("	return toInteger(parms.get(" + constName + "));");

			} else if (type.equals("Boolean")) {
				System.out.println("	return toBoolean(parms.get(" + constName + "));");

			} else if (type.equals("Double")){
				System.out.println("	return toDouble(parms.get(" + constName + "));");

			} else {
				System.out.println("	return parms.get(" + constName + ");");
			}
			System.out.println("	}");

		}


	}
}
