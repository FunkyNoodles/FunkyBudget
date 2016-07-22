package io.github.funkynoodles;

public class StringUtils {

	// Convert a number to two decimals
	public static String format2Decimals(String str) {
		int eIndex = str.indexOf("E");
		String eStr = "";
		if (eIndex != -1) {
			eStr = str.substring(eIndex);
		}
		int decimalIndex = str.indexOf(".");
		if (str.length() >= decimalIndex + 3) {
			str = str.substring(0,decimalIndex + 3) + eStr;
		}
		return str;
	}

	public static String format2Decimals(double d) {
		String str = Double.toString(d);
		return format2Decimals(str);
	}
}
