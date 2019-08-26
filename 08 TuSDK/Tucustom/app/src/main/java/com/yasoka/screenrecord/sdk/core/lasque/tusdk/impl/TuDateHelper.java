// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl;

//import org.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.DateHelper;

import java.util.Calendar;
//import org.lasque.tusdk.core.utils.DateHelper;

public class TuDateHelper extends DateHelper
{
    public static String timestampSNS(final Calendar calendar) {
        return DateHelper.timestampSNS(calendar, TuSdkContext.getString("lsq_date_seconds_ago"), TuSdkContext.getString("lsq_date_minutes_ago"), TuSdkContext.getString("lsq_date_hours_ago"));
    }
}
