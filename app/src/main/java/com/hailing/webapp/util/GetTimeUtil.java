package com.hailing.webapp.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTimeUtil {

    public static String getNowTimeString() {  //按照yyyy年MM月dd日的格式生成当前时间的字符串
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");//设置日期格式
        return formatter.format(System.currentTimeMillis());
    }

    public static Date dateTransform(String time) {  //将时间字符串转换为Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        ParsePosition position = new ParsePosition(0);
        return dateFormat.parse(time, position);
    }

    public static String stringTransform(Date date) {  //将Date转换为字符串
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");//设置日期格式
        return dateFormat.format(date);
    }
}
