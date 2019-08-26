package org.lasque.tusdk.core.utils;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Build.VERSION;
import android.widget.DatePicker;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateHelper
{
  public static Date parseDate(long paramLong)
  {
    Date localDate = new Date(paramLong * 1000L);
    return localDate;
  }
  
  public static Calendar parseCalendar(long paramLong)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(paramLong * 1000L);
    return localCalendar;
  }
  
  public static GregorianCalendar parseGregorianCalendar(long paramLong)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTimeInMillis(paramLong * 1000L);
    return localGregorianCalendar;
  }
  
  public static String format(Calendar paramCalendar, String paramString)
  {
    if ((paramCalendar == null) || (paramString == null)) {
      return null;
    }
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(paramString, Locale.getDefault());
    return localSimpleDateFormat.format(paramCalendar.getTime());
  }
  
  public static String timestampSNS(Calendar paramCalendar, String paramString1, String paramString2, String paramString3)
  {
    if (paramCalendar == null) {
      return null;
    }
    long l = Calendar.getInstance().getTimeInMillis();
    int i = (int)((l - paramCalendar.getTimeInMillis()) / 1000L);
    if (i < 0) {
      i = 0;
    }
    String str;
    if (i < 60)
    {
      str = String.format("%s %s", new Object[] { Integer.valueOf(i), paramString1 });
    }
    else if (i < 3600)
    {
      i /= 60;
      str = String.format("%s %s", new Object[] { Integer.valueOf(i), paramString2 });
    }
    else if (i < 86400)
    {
      i = i / 60 / 60;
      str = String.format("%s %s", new Object[] { Integer.valueOf(i), paramString3 });
    }
    else
    {
      str = format(paramCalendar, "yyyy-M-d");
    }
    return str;
  }
  
  @TargetApi(11)
  public static void showDateDialog(DatePickerDialog paramDatePickerDialog, boolean paramBoolean)
  {
    if ((paramBoolean) && (Build.VERSION.SDK_INT > 10))
    {
      DatePicker localDatePicker = paramDatePickerDialog.getDatePicker();
      Calendar localCalendar = Calendar.getInstance();
      localDatePicker.setMaxDate(localCalendar.getTimeInMillis());
    }
    paramDatePickerDialog.show();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\DateHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */