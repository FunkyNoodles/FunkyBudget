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
		// Now make sure there are two decimals
		decimalIndex = str.indexOf(".");
		int diff = str.length() - decimalIndex;
		switch (diff){
		case 1:
			str += "00";
			break;
		case 2:
			str += "0";
			break;
		default:
			break;
		}
		return str;
	}

	public static String format2Decimals(double d) {
		String str = Double.toString(d);
		return format2Decimals(str);
	}
}
