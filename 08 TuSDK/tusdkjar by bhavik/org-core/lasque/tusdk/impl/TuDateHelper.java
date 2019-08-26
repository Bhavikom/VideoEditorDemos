package org.lasque.tusdk.impl;

import java.util.Calendar;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.DateHelper;

public class TuDateHelper
  extends DateHelper
{
  public static String timestampSNS(Calendar paramCalendar)
  {
    return timestampSNS(paramCalendar, TuSdkContext.getString("lsq_date_seconds_ago"), TuSdkContext.getString("lsq_date_minutes_ago"), TuSdkContext.getString("lsq_date_hours_ago"));
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\TuDateHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */