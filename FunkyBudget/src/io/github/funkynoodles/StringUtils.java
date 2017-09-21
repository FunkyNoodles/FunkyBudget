package io.github.funkynoodles;

public class StringUtils {

    // Convert a number to two decimals
    public static String format2Decimals(String str) {
        int eIndex = str.indexOf("E");
        int decimalIndex = str.indexOf(".");
        int len = str.length();
        String eStr = "";
        if (eIndex != -1) {
            eStr = str.substring(eIndex);
            if (len >= decimalIndex + 3) {
                return str.substring(0, decimalIndex + 3) + eStr;
            }
        }
        // Now make sure there are two decimals
        int diff = len - decimalIndex;
        switch (diff) {
            case 1:
                str += "00";
                break;
            case 2:
                str += "0";
                break;
            default:
                break;
        }
        if (diff > 3) {
            // There is inaccuracy
            if (str.charAt(decimalIndex + 3) == '0') {
                // if more zeros, just truncate
                return str.substring(0, decimalIndex + 3);
            } else if (str.charAt(decimalIndex + 3) == '9') {
                // if 9, add 0.01 and truncate
                double d = Double.parseDouble(str);
                d = d + 0.01;
                str = Double.toString(d);
                return str.substring(0, decimalIndex + 3);
            }
        }
        return str;
    }

    public static String format2Decimals(double d) {
        String str = Double.toString(d);
        return format2Decimals(str);
    }
}
