package com.ths.plt.cordova.utils;

/**
 * Created by jinyunyang on 15/3/6.
 */
public class StringUtils {

    /**
     * <p>Checks if a String is empty ("") or null.</p>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * <p>Checks if a String is not empty ("") and not null.</p>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is not empty and not null
     */
    public static boolean isNotEmpty(String str) {
        return !StringUtils.isEmpty(str);
    }

    public static String subString(String s, int start, int end){

        try {
            return s.substring(start,end);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static int getMailIconColor(String str) {
        if (str == null) {
            return 0;
        }
        char codePoint;
        if (StringUtils.isFirstAlpha(str)) {
            codePoint = str.substring(0, 1).toUpperCase().charAt(0);
        } else {
            codePoint = str.charAt(0);
        }
        int number = codePoint;
        return number % 10;
    }
    /**
     *
     * @param str 指定字符串
     * @return 自定字符串是否全是字母
     */
    public static boolean isAlpha(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            char codePoint = str.charAt(i);
            if (!('A' <= codePoint && codePoint <= 'Z') && !('a' <= codePoint && codePoint <= 'z')) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param str 指定字符串
     * @return 自定字符串第一个字符是否是字母
     */
    public static boolean isFirstAlpha(String str) {
        if (str == null) {
            return false;
        }
        char codePoint = str.charAt(0);
        if (!('A' <= codePoint && codePoint <= 'Z') && !('a' <= codePoint && codePoint <= 'z')) {
            return false;
        }
        return true;
    }
    /**
     * 通过param key 获取在URL中的value
     * @param url - format key1=value1&key2=value2
     * @param key - key
     * @return
     */
    public static String getValueFromUrl(String url, String key) {
        String[] keyValueArray = url.split("&");
        for (String keyValue: keyValueArray) {
            String[] kv = keyValue.split("=");
            if (kv.length != 2) {
                continue;
            }
            String k = kv[0];
            String v = kv[1];

            if (key.equals(k)) {
                return v;
            }
        }
        return "";
    }
}
