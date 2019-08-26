// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.app.DatePickerDialog;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;

public class DateHelper
{
    public static Date parseDate(final long n) {
        return new Date(n * 1000L);
    }
    
    public static Calendar parseCalendar(final long n) {
        final Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(n * 1000L);
        return instance;
    }
    
    public static GregorianCalendar parseGregorianCalendar(final long n) {
        final GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(n * 1000L);
        return gregorianCalendar;
    }
    
    public static String format(final Calendar calendar, final String pattern) {
        if (calendar == null || pattern == null) {
            return null;
        }
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(calendar.getTime());
    }
    
    public static String timestampSNS(final Calendar calendar, final String s, final String s2, final String s3) {
        if (calendar == null) {
            return null;
        }
        int i = (int)((Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis()) / 1000L);
        if (i < 0) {
            i = 0;
        }
        String s4;
        if (i < 60) {
            s4 = String.format("%s %s", i, s);
        }
        else if (i < 3600) {
            s4 = String.format("%s %s", i / 60, s2);
        }
        else if (i < 86400) {
            s4 = String.format("%s %s", i / 60 / 60, s3);
        }
        else {
            s4 = format(calendar, "yyyy-M-d");
        }
        return s4;
    }
    
    @TargetApi(11)
    public static void showDateDialog(final DatePickerDialog datePickerDialog, final boolean b) {
        if (b && Build.VERSION.SDK_INT > 10) {
            datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        }
        datePickerDialog.show();
    }
}
