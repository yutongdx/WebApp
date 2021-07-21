package com.hailing.webapp.util;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

public class UrlUtil {

    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";
    public static final String FILE = "file://";

    /**
     * 将关键字转换成最后转换的url
     *
     * @param keyword
     * @return
     */
    public static String converKeywordLoadOrSearch(String keyword) {

        int point = getCount(keyword, ".");

        if (!Objects.equals(keyword, null)) {
            keyword = keyword.trim();

            if (keyword.startsWith("www.")) {
                keyword = HTTP + keyword;
            } else if (keyword.startsWith("ftp.")) {
                keyword = "ftp://" + keyword;
            }


            //是否为IP地址
            boolean isIPAddress = (TextUtils.isDigitsOnly(keyword.replace(".", ""))
                    && point == 3 && !keyword.contains(" "));
            if (isIPAddress) {
                keyword = HTTP + keyword;
            }

            //是否为有效访问地址
            boolean validURL = (keyword.startsWith("ftp://") || keyword.startsWith(HTTP)
                    || keyword.startsWith(FILE) || keyword.startsWith(HTTPS))
                    || isIPAddress;


            //字符串包含空格，最后的“.”后不接字符串或接的字符串长度不大于2，则为搜索内容
            String[] strArr = keyword.split("\\.");
            boolean isSearch = keyword.contains(" ")
                    || (strArr.length != (point + 1) || strArr[strArr.length - 1].length() > 1) && !isIPAddress && !validURL;


            String converUrl;
            if (isSearch) {
                try {
                    keyword = URLEncoder.encode(keyword, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                converUrl = "https://cn.bing.com/search?q=" + keyword + "&ie=UTF-8";
            } else if (!validURL) {
                converUrl = HTTP + keyword;
            } else {
                converUrl = keyword;
            }
            return converUrl;
        } else {
            return "";
        }

    }

    /**
     * 检测关键字在字符串中的个数
     *
     * @param str 字符串
     * @param key 检测的关键字
     * @return
     */
    public static int getCount(String str, String key) {
        if (str == null || key == null || "".equals(str.trim()) || "".equals(key.trim())) {
            return 0;
        }
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(key, index)) != -1) {
            index = index + key.length();
            count++;
        }
        return count;
    }

}
