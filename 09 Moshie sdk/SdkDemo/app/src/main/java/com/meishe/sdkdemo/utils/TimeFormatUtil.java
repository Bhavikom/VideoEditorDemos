package com.meishe.sdkdemo.utils;

import android.os.SystemClock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by admin on 2018/6/15.
 */

public class TimeFormatUtil {
    private static final double TIMEBASE = 1000000D;

    /**
     *  格式化时间串00:00:00.0
     */
    public static String formatUsToString1(long us){
        double totalSecond = us / TIMEBASE;
        int hour = (int)totalSecond / 3600;
        int minute = (int)totalSecond % 3600 / 60;
        double second = totalSecond % 60;
        String timeStr = hour > 0 ? String.format("%02d:%02d:%04.1f", hour, minute, second) : String.format("%02d:%04.1f", minute, second);
        return timeStr;
    }

    /**
     *  格式化时间串00:00:00
     */
    public static String formatUsToString2(long us) {
        int second = (int) (us / 1000000.0 + 0.5);
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String timeStr;
        if (us == 0) {
            return "00:00";
        }
        if (hh > 0) {
            timeStr = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            timeStr = String.format("%02d:%02d", mm, ss);
        }
        return timeStr;
    }
    /**
     *  格式化时间串00:00:00.0
     */
    public static String formatUsToString3(long us){
        double totalSecond = us / TIMEBASE;
        int hour = (int)totalSecond / 3600;
        int minute = (int)totalSecond % 3600 / 60;
        double second = totalSecond % 60;
        String timeStr = hour > 0 ? String.format("%02d:%02d:%04.1f", hour, minute, second)
                : minute > 0  ? String.format("%02d:%04.1f", minute, second)
                : second > 9 ? String.format("%4.1f", second) : String.format("%3.1f", second);
        return timeStr;
    }
    /**
     * 毫秒转换成hh:mm:ss格式
     */
    public static String formatMsToString(long ms) {
        if (ms <= 0 || ms >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        int totalSeconds = (int)ms / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    /**
     *  获取当前时间(毫秒)
     */
    public static long getCurrentTimeMS() {
        long clock_ms = SystemClock.currentThreadTimeMillis();
        return clock_ms;
    }

    /**
     *  获取当前时间的毫秒
     * @return yyyyMMddHHmmss格式的字符串
     */
    public static String getCurrentTime() {
        Date now = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
        dateformat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String sDateTime = dateformat.format(now);
        return sDateTime;
    }
}
