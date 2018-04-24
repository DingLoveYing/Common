package com.ding.commonlibrary.utils;

/**
 * 字符串工具类
 */
public class StringUtil {

    /**
     * 判断str1中包含str2的个数
     *
     * @param str1 源字符串
     * @param str2 要查找的字符串
     * @return 指定字符串出现的次数
     */
    public static int countStr(String str1, String str2) {
        int counter = 0;
        if (!str1.contains(str2)) {
            return 0;
        } else {
            counter++;
            countStr(str1.substring(str1.indexOf(str2) + str2.length()), str2);
            return counter;
        }
    }
}
