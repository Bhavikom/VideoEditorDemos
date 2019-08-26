// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type;

import java.util.HashMap;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import android.annotation.SuppressLint;
import java.util.Map;

public enum ClazzType
{
    IntegerType(Integer.class.hashCode()), 
    intType(Integer.TYPE.hashCode()), 
    LongType(Long.class.hashCode()), 
    longType(Long.TYPE.hashCode()), 
    FloatType(Float.class.hashCode()), 
    floatType(Float.TYPE.hashCode()), 
    DoubleType(Double.class.hashCode()), 
    doubleType(Double.TYPE.hashCode()), 
    BooleanType(Boolean.class.hashCode()), 
    booleanType(Boolean.TYPE.hashCode()), 
    StringType(String.class.hashCode()), 
    DateType(Date.class.hashCode()), 
    GregorianCalendarType(GregorianCalendar.class.hashCode()), 
    CalendarType(Calendar.class.hashCode());
    
    private int a;
    @SuppressLint({ "UseSparseArrays" })
    private static final Map<Integer, ClazzType> b;
    
    private ClazzType(final int a) {
        this.a = a;
    }
    
    public int getFlag() {
        return this.a;
    }
    
    public static ClazzType getType(final int i) {
        return ClazzType.b.get(i);
    }
    
    static {
        b = new HashMap<Integer, ClazzType>();
        for (final ClazzType clazzType : values()) {
            ClazzType.b.put(clazzType.getFlag(), clazzType);
        }
    }
}
