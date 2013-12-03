package org.deegree.framework.util;


public class BooleanUtil {

	public static Boolean parseBooleanStringWithNull(String booleanString) {

		if (StringTools.isNullOrEmpty(booleanString)) {
			return null;
		}

		if (booleanString.toLowerCase().equals("true") || booleanString.equals("1")) {
			return true;
		}

		if (booleanString.toLowerCase().equals("false") || booleanString.equals("0")) {
			return false;
		}

		throw new IllegalArgumentException("Invalid boolean value");
	}

}
