package com.hailing.webapp.util;

import android.text.TextUtils;
import android.util.Log;

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
    public static String convertKeywordLoadOrSearch(String keyword) {

        String convertUrl;

        if (!Objects.equals(keyword, "")) {
            keyword = keyword.trim();
            int point = getCount(keyword, ".");
            String[] strArr = keyword.split("\\.");

            if (keyword.startsWith("www.")) {
                keyword = HTTP + keyword;
            } else if (keyword.startsWith("ftp.")) {
                keyword = "ftp://" + keyword;
            }

            //是否为有效访问地址
            boolean validURL = (keyword.startsWith("ftp://") || keyword.startsWith(FILE)
                    || keyword.startsWith(HTTP) || keyword.startsWith(HTTPS));

            //是否为IP地址
            boolean isIpAddress = (point == 3 && !Objects.equals(strArr[0], "")
                    && strArr.length == 4
                    && TextUtils.isDigitsOnly(keyword.replace(".", "")));

            //是否为域名
            boolean isDomainName = (strArr.length == (point + 1) && strArr[strArr.length - 1].length() > 1
                    && !Objects.equals(strArr[0], "") && strArr.length != 1);

            if (validURL) {
                convertUrl = keyword;
            } else if (isIpAddress) {
                convertUrl = HTTP + keyword;
            } else if (isDomainName) {
                convertUrl = HTTP + keyword;
            } else {
                try {
                    keyword = URLEncoder.encode(keyword, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                convertUrl = "https://cn.bing.com/search?q=" + keyword;
            }
            Log.d("url", convertUrl);
            return convertUrl;
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
