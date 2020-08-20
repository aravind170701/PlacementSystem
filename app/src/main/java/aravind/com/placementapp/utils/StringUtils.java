package aravind.com.placementapp.utils;

public abstract class StringUtils {
    public static boolean isNotBlank(String str) {
        return (str != null) && (str.length() > 0);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return (str1 != null) && (str2 != null) && str2.equalsIgnoreCase(str2);
    }
}
