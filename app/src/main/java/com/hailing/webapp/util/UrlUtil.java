package com.hailing.webapp.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.regex.Pattern;

public class UrlUtil {

    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    private static final String FILE = "file://";

    /**
     * 将关键字转换成最后转换的url
     *
     * @param keyword
     * @return
     */
    public static String convertKeywordLoadOrSearch(String keyword) {

        String convertUrl;
        String IP_ADDRESS_PATTERN = "^((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))[.]){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))$";
        String DOMAIN_NAME_PATTERN = "^((?!-)\\w{1,63}(?<!-)[.])+[A-Za-z]{2,6}$";
        Pattern pattern;

        if (!Objects.equals(keyword, "")) {
            keyword = keyword.trim();

            if (keyword.startsWith("www.")) {
                keyword = HTTP + keyword;
            } else if (keyword.startsWith("ftp.")) {
                keyword = "ftp://" + keyword;
            }

            //是否为有效访问地址
            boolean validURL = (keyword.startsWith("ftp://") || keyword.startsWith(FILE)
                    || keyword.startsWith(HTTP) || keyword.startsWith(HTTPS));

            //是否为IP地址
            pattern = Pattern.compile(IP_ADDRESS_PATTERN);
            boolean isIpAddress = pattern.matcher(keyword).matches();

            //是否为域名
            pattern = Pattern.compile(DOMAIN_NAME_PATTERN);
            boolean isDomainName = pattern.matcher(keyword).matches();

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
