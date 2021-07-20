package com.hailing.webapp.util;

import java.text.SimpleDateFormat;

public class GetTimeUtil {

    public static String getNowTimeString() {  //按照yyyy年MM月dd日的格式生成当前时间的字符串
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");//设置日期格式
        return formatter.format(System.currentTimeMillis());
    }

}
