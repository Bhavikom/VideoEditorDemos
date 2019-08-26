package org.lasque.tusdk.core.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TuSdkDate
  implements Comparable<TuSdkDate>
{
  private Calendar a;
  
  public static TuSdkDate create()
  {
    return new TuSdkDate();
  }
  
  public static TuSdkDate create(Calendar paramCalendar)
  {
    return new TuSdkDate(paramCalendar);
  }
  
  public static TuSdkDate create(long paramLong)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(paramLong);
    return new TuSdkDate(localCalendar);
  }
  
  public TuSdkDate()
  {
    this(Calendar.getInstance());
  }
  
  public TuSdkDate(Calendar paramCalendar)
  {
    this.a = paramCalendar;
  }
  
  public Calendar calendar()
  {
    return this.a;
  }
  
  public long getTimeInMillis()
  {
    return this.a.getTimeInMillis();
  }
  
  public int getTimeInSeconds()
  {
    return (int)(this.a.getTimeInMillis() / 1000L);
  }
  
  public int year()
  {
    return this.a.get(1);
  }
  
  public int month()
  {
    return this.a.get(2);
  }
  
  public int day()
  {
    return this.a.get(5);
  }
  
  public int hour()
  {
    return this.a.get(11);
  }
  
  public int minute()
  {
    return this.a.get(12);
  }
  
  public int second()
  {
    return this.a.get(13);
  }
  
  public int weekday()
  {
    return this.a.get(7);
  }
  
  public int week()
  {
    return this.a.get(3);
  }
  
  public TuSdkDate addDay(int paramInt)
  {
    Calendar localCalendar = (Calendar)this.a.clone();
    localCalendar.add(5, paramInt);
    return new TuSdkDate(localCalendar);
  }
  
  public TuSdkDate addMonth(int paramInt)
  {
    Calendar localCalendar = (Calendar)this.a.clone();
    localCalendar.add(2, paramInt);
    return new TuSdkDate(localCalendar);
  }
  
  public TuSdkDate beginingOfDay()
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(year(), month(), day());
    return new TuSdkDate(localGregorianCalendar);
  }
  
  public TuSdkDate endOfDay()
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(year(), month(), day() + 1);
    localGregorianCalendar.add(14, -1);
    return new TuSdkDate(localGregorianCalendar);
  }
  
  public TuSdkDate firstDayOfTheMonth()
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(year(), month(), 1);
    return new TuSdkDate(localGregorianCalendar);
  }
  
  public TuSdkDate lastDayOfTheMonth()
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(year(), month() + 1, 0);
    return new TuSdkDate(localGregorianCalendar);
  }
  
  public TuSdkDate firstDayOfThePreviousMonth()
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(year(), month() - 1, 1);
    return new TuSdkDate(localGregorianCalendar);
  }
  
  public TuSdkDate firstDayOfTheFollowingMonth()
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(year(), month() + 1, 1);
    return new TuSdkDate(localGregorianCalendar);
  }
  
  public TuSdkDate associateDayOfThePreviousMonth()
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(year(), month() - 1, day());
    return new TuSdkDate(localGregorianCalendar);
  }
  
  public TuSdkDate associateDayOfTheFollowingMonth()
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(year(), month() + 1, day());
    return new TuSdkDate(localGregorianCalendar);
  }
  
  public int numberOfDaysInMonth()
  {
    return this.a.getActualMaximum(5);
  }
  
  public int numberOfWeeksInMonth()
  {
    return this.a.getActualMaximum(4);
  }
  
  public TuSdkDate firstDayOfTheWeek()
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(year(), month(), day() - (weekday() - 1));
    return new TuSdkDate(localGregorianCalendar);
  }
  
  public int weekOfDayInMonth()
  {
    return this.a.get(4);
  }
  
  public TuSdkDate previousDay()
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(year(), month(), day() - 1);
    return new TuSdkDate(localGregorianCalendar);
  }
  
  public TuSdkDate followingDay()
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(year(), month(), day() + 1);
    return new TuSdkDate(localGregorianCalendar);
  }
  
  public boolean isToday()
  {
    return sameDay(create());
  }
  
  public boolean isInModth()
  {
    return sameMonth(create());
  }
  
  public boolean sameDay(TuSdkDate paramTuSdkDate)
  {
    return (paramTuSdkDate != null) && (year() == paramTuSdkDate.year()) && (month() == paramTuSdkDate.month()) && (day() == paramTuSdkDate.day());
  }
  
  public boolean sameWeek(TuSdkDate paramTuSdkDate)
  {
    return (paramTuSdkDate != null) && (year() == paramTuSdkDate.year()) && (week() == paramTuSdkDate.week());
  }
  
  public boolean sameMonth(TuSdkDate paramTuSdkDate)
  {
    return (paramTuSdkDate != null) && (year() == paramTuSdkDate.year()) && (month() == paramTuSdkDate.month());
  }
  
  public int diffOfMonth()
  {
    return diffOfMonth(create());
  }
  
  public int diffOfMonth(TuSdkDate paramTuSdkDate)
  {
    if (paramTuSdkDate == null) {
      return 0;
    }
    int i = (paramTuSdkDate.year() - year()) * 12 + paramTuSdkDate.month() - month();
    return i;
  }
  
  public long diffOfMillis()
  {
    return diffOfMillis(create());
  }
  
  public long diffOfMillis(TuSdkDate paramTuSdkDate)
  {
    if (paramTuSdkDate == null) {
      return getTimeInMillis();
    }
    long l = paramTuSdkDate.getTimeInMillis() - getTimeInMillis();
    return l;
  }
  
  public long diffOfMillis(long paramLong)
  {
    long l = paramLong - getTimeInMillis();
    return l;
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof TuSdkDate))) {
      return false;
    }
    return getTimeInMillis() == ((TuSdkDate)paramObject).getTimeInMillis();
  }
  
  public int compareTo(TuSdkDate paramTuSdkDate)
  {
    if (equals(paramTuSdkDate)) {
      return 0;
    }
    if (getTimeInMillis() > paramTuSdkDate.getTimeInMillis()) {
      return 1;
    }
    return -1;
  }
  
  public String toString()
  {
    return DateHelper.format(this.a, "yyyy-M-d HH:mm:ss:SSS");
  }
  
  public String format(String paramString)
  {
    return DateHelper.format(this.a, paramString);
  }
  
  public void test()
  {
    TLog.i("now: %s", new Object[] { toString() });
    TLog.i("beginingOfDay(获取当天的起始时间): %s", new Object[] { beginingOfDay() });
    TLog.i("endOfDay(获取当天的结束时间): %s", new Object[] { endOfDay() });
    TLog.i("firstDayOfTheMonth(获取当月的第一天): %s", new Object[] { firstDayOfTheMonth() });
    TLog.i("lastDayOfTheMonth(获取当月的最后一天): %s", new Object[] { lastDayOfTheMonth() });
    TLog.i("firstDayOfThePreviousMonth(获取前一个月的第一天): %s", new Object[] { firstDayOfThePreviousMonth() });
    TLog.i("firstDayOfTheFollowingMonth(获取后一个月的第一天): %s", new Object[] { firstDayOfTheFollowingMonth() });
    TLog.i("associateDayOfThePreviousMonth(获取前一个月中与当天对应的日期): %s", new Object[] { associateDayOfThePreviousMonth() });
    TLog.i("associateDayOfTheFollowingMonth(获取后一个月中与当天对应的日期): %s", new Object[] { associateDayOfTheFollowingMonth() });
    TLog.i("numberOfDaysInMonth(获取当月的天数): %s", new Object[] { Integer.valueOf(numberOfDaysInMonth()) });
    TLog.i("numberOfWeeksInMonth(获取当月的周数): %s", new Object[] { Integer.valueOf(numberOfWeeksInMonth()) });
    TLog.i("firstDayOfTheWeek(获取这一周的第一天): %s", new Object[] { firstDayOfTheWeek() });
    TLog.i("weekOfDayInMonth(获取当天是当月的第几周): %s", new Object[] { Integer.valueOf(weekOfDayInMonth()) });
    TLog.i("previousDay(前一天): %s", new Object[] { previousDay() });
    TLog.i("followingDay(后一天): %s", new Object[] { followingDay() });
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\TuSdkDate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */