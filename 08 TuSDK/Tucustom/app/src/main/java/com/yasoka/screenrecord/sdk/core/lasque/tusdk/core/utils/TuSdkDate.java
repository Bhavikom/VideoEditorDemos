// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import java.util.GregorianCalendar;
import java.util.Calendar;

public class TuSdkDate implements Comparable<TuSdkDate>
{
    private Calendar a;
    
    public static TuSdkDate create() {
        return new TuSdkDate();
    }
    
    public static TuSdkDate create(final Calendar calendar) {
        return new TuSdkDate(calendar);
    }
    
    public static TuSdkDate create(final long timeInMillis) {
        final Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(timeInMillis);
        return new TuSdkDate(instance);
    }
    
    public TuSdkDate() {
        this(Calendar.getInstance());
    }
    
    public TuSdkDate(final Calendar a) {
        this.a = a;
    }
    
    public Calendar calendar() {
        return this.a;
    }
    
    public long getTimeInMillis() {
        return this.a.getTimeInMillis();
    }
    
    public int getTimeInSeconds() {
        return (int)(this.a.getTimeInMillis() / 1000L);
    }
    
    public int year() {
        return this.a.get(1);
    }
    
    public int month() {
        return this.a.get(2);
    }
    
    public int day() {
        return this.a.get(5);
    }
    
    public int hour() {
        return this.a.get(11);
    }
    
    public int minute() {
        return this.a.get(12);
    }
    
    public int second() {
        return this.a.get(13);
    }
    
    public int weekday() {
        return this.a.get(7);
    }
    
    public int week() {
        return this.a.get(3);
    }
    
    public TuSdkDate addDay(final int n) {
        final Calendar calendar = (Calendar)this.a.clone();
        calendar.add(5, n);
        return new TuSdkDate(calendar);
    }
    
    public TuSdkDate addMonth(final int n) {
        final Calendar calendar = (Calendar)this.a.clone();
        calendar.add(2, n);
        return new TuSdkDate(calendar);
    }
    
    public TuSdkDate beginingOfDay() {
        return new TuSdkDate(new GregorianCalendar(this.year(), this.month(), this.day()));
    }
    
    public TuSdkDate endOfDay() {
        final GregorianCalendar gregorianCalendar = new GregorianCalendar(this.year(), this.month(), this.day() + 1);
        gregorianCalendar.add(14, -1);
        return new TuSdkDate(gregorianCalendar);
    }
    
    public TuSdkDate firstDayOfTheMonth() {
        return new TuSdkDate(new GregorianCalendar(this.year(), this.month(), 1));
    }
    
    public TuSdkDate lastDayOfTheMonth() {
        return new TuSdkDate(new GregorianCalendar(this.year(), this.month() + 1, 0));
    }
    
    public TuSdkDate firstDayOfThePreviousMonth() {
        return new TuSdkDate(new GregorianCalendar(this.year(), this.month() - 1, 1));
    }
    
    public TuSdkDate firstDayOfTheFollowingMonth() {
        return new TuSdkDate(new GregorianCalendar(this.year(), this.month() + 1, 1));
    }
    
    public TuSdkDate associateDayOfThePreviousMonth() {
        return new TuSdkDate(new GregorianCalendar(this.year(), this.month() - 1, this.day()));
    }
    
    public TuSdkDate associateDayOfTheFollowingMonth() {
        return new TuSdkDate(new GregorianCalendar(this.year(), this.month() + 1, this.day()));
    }
    
    public int numberOfDaysInMonth() {
        return this.a.getActualMaximum(5);
    }
    
    public int numberOfWeeksInMonth() {
        return this.a.getActualMaximum(4);
    }
    
    public TuSdkDate firstDayOfTheWeek() {
        return new TuSdkDate(new GregorianCalendar(this.year(), this.month(), this.day() - (this.weekday() - 1)));
    }
    
    public int weekOfDayInMonth() {
        return this.a.get(4);
    }
    
    public TuSdkDate previousDay() {
        return new TuSdkDate(new GregorianCalendar(this.year(), this.month(), this.day() - 1));
    }
    
    public TuSdkDate followingDay() {
        return new TuSdkDate(new GregorianCalendar(this.year(), this.month(), this.day() + 1));
    }
    
    public boolean isToday() {
        return this.sameDay(create());
    }
    
    public boolean isInModth() {
        return this.sameMonth(create());
    }
    
    public boolean sameDay(final TuSdkDate tuSdkDate) {
        return tuSdkDate != null && this.year() == tuSdkDate.year() && this.month() == tuSdkDate.month() && this.day() == tuSdkDate.day();
    }
    
    public boolean sameWeek(final TuSdkDate tuSdkDate) {
        return tuSdkDate != null && this.year() == tuSdkDate.year() && this.week() == tuSdkDate.week();
    }
    
    public boolean sameMonth(final TuSdkDate tuSdkDate) {
        return tuSdkDate != null && this.year() == tuSdkDate.year() && this.month() == tuSdkDate.month();
    }
    
    public int diffOfMonth() {
        return this.diffOfMonth(create());
    }
    
    public int diffOfMonth(final TuSdkDate tuSdkDate) {
        if (tuSdkDate == null) {
            return 0;
        }
        return (tuSdkDate.year() - this.year()) * 12 + tuSdkDate.month() - this.month();
    }
    
    public long diffOfMillis() {
        return this.diffOfMillis(create());
    }
    
    public long diffOfMillis(final TuSdkDate tuSdkDate) {
        if (tuSdkDate == null) {
            return this.getTimeInMillis();
        }
        return tuSdkDate.getTimeInMillis() - this.getTimeInMillis();
    }
    
    public long diffOfMillis(final long n) {
        return n - this.getTimeInMillis();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o != null && o instanceof TuSdkDate && this.getTimeInMillis() == ((TuSdkDate)o).getTimeInMillis();
    }
    
    @Override
    public int compareTo(final TuSdkDate tuSdkDate) {
        if (this.equals(tuSdkDate)) {
            return 0;
        }
        if (this.getTimeInMillis() > tuSdkDate.getTimeInMillis()) {
            return 1;
        }
        return -1;
    }
    
    @Override
    public String toString() {
        return DateHelper.format(this.a, "yyyy-M-d HH:mm:ss:SSS");
    }
    
    public String format(final String s) {
        return DateHelper.format(this.a, s);
    }
    
    public void test() {
        TLog.i("now: %s", this.toString());
        TLog.i("beginingOfDay(\u83b7\u53d6\u5f53\u5929\u7684\u8d77\u59cb\u65f6\u95f4): %s", this.beginingOfDay());
        TLog.i("endOfDay(\u83b7\u53d6\u5f53\u5929\u7684\u7ed3\u675f\u65f6\u95f4): %s", this.endOfDay());
        TLog.i("firstDayOfTheMonth(\u83b7\u53d6\u5f53\u6708\u7684\u7b2c\u4e00\u5929): %s", this.firstDayOfTheMonth());
        TLog.i("lastDayOfTheMonth(\u83b7\u53d6\u5f53\u6708\u7684\u6700\u540e\u4e00\u5929): %s", this.lastDayOfTheMonth());
        TLog.i("firstDayOfThePreviousMonth(\u83b7\u53d6\u524d\u4e00\u4e2a\u6708\u7684\u7b2c\u4e00\u5929): %s", this.firstDayOfThePreviousMonth());
        TLog.i("firstDayOfTheFollowingMonth(\u83b7\u53d6\u540e\u4e00\u4e2a\u6708\u7684\u7b2c\u4e00\u5929): %s", this.firstDayOfTheFollowingMonth());
        TLog.i("associateDayOfThePreviousMonth(\u83b7\u53d6\u524d\u4e00\u4e2a\u6708\u4e2d\u4e0e\u5f53\u5929\u5bf9\u5e94\u7684\u65e5\u671f): %s", this.associateDayOfThePreviousMonth());
        TLog.i("associateDayOfTheFollowingMonth(\u83b7\u53d6\u540e\u4e00\u4e2a\u6708\u4e2d\u4e0e\u5f53\u5929\u5bf9\u5e94\u7684\u65e5\u671f): %s", this.associateDayOfTheFollowingMonth());
        TLog.i("numberOfDaysInMonth(\u83b7\u53d6\u5f53\u6708\u7684\u5929\u6570): %s", this.numberOfDaysInMonth());
        TLog.i("numberOfWeeksInMonth(\u83b7\u53d6\u5f53\u6708\u7684\u5468\u6570): %s", this.numberOfWeeksInMonth());
        TLog.i("firstDayOfTheWeek(\u83b7\u53d6\u8fd9\u4e00\u5468\u7684\u7b2c\u4e00\u5929): %s", this.firstDayOfTheWeek());
        TLog.i("weekOfDayInMonth(\u83b7\u53d6\u5f53\u5929\u662f\u5f53\u6708\u7684\u7b2c\u51e0\u5468): %s", this.weekOfDayInMonth());
        TLog.i("previousDay(\u524d\u4e00\u5929): %s", this.previousDay());
        TLog.i("followingDay(\u540e\u4e00\u5929): %s", this.followingDay());
    }
}
