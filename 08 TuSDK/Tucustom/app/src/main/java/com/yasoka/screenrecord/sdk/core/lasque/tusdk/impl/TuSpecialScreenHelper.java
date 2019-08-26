// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.lang.reflect.InvocationTargetException;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.TuSdkContext;

public class TuSpecialScreenHelper
{
    private static boolean a;
    private static int b;
    
    public static void dealNotchScreen() {
        if (h()) {
            TuSpecialScreenHelper.a = true;
            TuSpecialScreenHelper.b = i();
        }
        else if (f()) {
            TuSpecialScreenHelper.a = true;
            TuSpecialScreenHelper.b = g();
        }
        else if (c()) {
            TuSpecialScreenHelper.a = true;
            TuSpecialScreenHelper.b = d();
        }
        else if (a()) {
            TuSpecialScreenHelper.a = true;
            TuSpecialScreenHelper.b = b();
        }
        else {
            TuSpecialScreenHelper.a = false;
        }
    }
    
    public static boolean isNotchScreen() {
        return TuSpecialScreenHelper.a;
    }
    
    public static int getNotchHeight() {
        return TuSpecialScreenHelper.b;
    }
    
    private static boolean a() {
        final String a = a("ro.miui.notch");
        return !a.equals("") && Integer.valueOf(a) == 1;
    }
    
    private static int b() {
        int dimensionPixelSize = 0;
        final int identifier = TuSdkContext.context().getResources().getIdentifier("notch_height", "dimen", "android");
        if (identifier > 0) {
            dimensionPixelSize = TuSdkContext.context().getResources().getDimensionPixelSize(identifier);
        }
        return dimensionPixelSize;
    }
    
    private static boolean c() {
        boolean booleanValue = false;
        try {
            final Class<?> loadClass = TuSdkContext.context().getClassLoader().loadClass("com.huawei.android.util.HwNotchSizeUtil");
            booleanValue = (boolean)loadClass.getMethod("hasNotchInScreen", (Class<?>[])new Class[0]).invoke(loadClass, new Object[0]);
        }
        catch (ClassNotFoundException ex) {
            TLog.d("hasNotchInScreen ClassNotFoundException", new Object[0]);
        }
        catch (NoSuchMethodException ex2) {
            TLog.d("hasNotchInScreen NoSuchMethodException", new Object[0]);
        }
        catch (Exception ex3) {
            TLog.d("hasNotchInScreen Exception", new Object[0]);
        }
        return booleanValue;
    }
    
    private static int d() {
        final int[] e = e();
        return e[1] - e[0];
    }
    
    private static int[] e() {
        int[] array = { 0, 0 };
        try {
            final Class<?> loadClass = TuSdkContext.context().getClassLoader().loadClass("com.huawei.android.util.HwNotchSizeUtil");
            array = (int[])loadClass.getMethod("getNotchSize", (Class<?>[])new Class[0]).invoke(loadClass, new Object[0]);
        }
        catch (ClassNotFoundException ex) {
            TLog.d("getNotchSize ClassNotFoundException", new Object[0]);
        }
        catch (NoSuchMethodException ex2) {
            TLog.d("getNotchSize NoSuchMethodException", new Object[0]);
        }
        catch (Exception ex3) {
            TLog.d("getNotchSize Exception", new Object[0]);
        }
        return array;
    }
    
    private static boolean f() {
        boolean booleanValue = false;
        try {
            final Class<?> loadClass = TuSdkContext.context().getClassLoader().loadClass("android.util.FtFeature");
            booleanValue = (boolean)loadClass.getMethod("isFeatureSupport", Integer.TYPE).invoke(loadClass, 32);
        }
        catch (ClassNotFoundException ex) {
            TLog.d("hasNotchInScreen ClassNotFoundException", new Object[0]);
        }
        catch (NoSuchMethodException ex2) {
            TLog.d("hasNotchInScreen NoSuchMethodException", new Object[0]);
        }
        catch (Exception ex3) {
            TLog.d("hasNotchInScreen Exception", new Object[0]);
        }
        return booleanValue;
    }
    
    private static int g() {
        return TuSdkContext.dip2px(27.0f);
    }
    
    private static boolean h() {
        return TuSdkContext.context().getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }
    
    private static int i() {
        int n = 0;
        final String a = a("ro.oppo.screen.heteromorphism");
        if (!a.equals("")) {
            final String[] split = a.substring(1, a.length()).split(":");
            n = Integer.valueOf(split[1].split(",")[1]) - Integer.valueOf(split[0].split(",")[1]);
        }
        return n;
    }
    
    private static String a(final String s) {
        String s2 = "";
        try {
            final Class<?> forName = Class.forName("android.os.SystemProperties");
            s2 = (String)forName.getMethod("get", String.class).invoke(forName.newInstance(), s);
        }
        catch (ClassNotFoundException ex) {
            TLog.d("error" + ex.toString(), new Object[0]);
        }
        catch (NoSuchMethodException ex2) {
            TLog.d("error" + ex2.toString(), new Object[0]);
        }
        catch (InstantiationException ex3) {
            TLog.d("error" + ex3.toString(), new Object[0]);
        }
        catch (IllegalAccessException ex4) {
            TLog.d("error" + ex4.toString(), new Object[0]);
        }
        catch (IllegalArgumentException ex5) {
            TLog.d("error" + ex5.toString(), new Object[0]);
        }
        catch (InvocationTargetException ex6) {
            TLog.d("error" + ex6.toString(), new Object[0]);
        }
        return s2;
    }
    
    static {
        TuSpecialScreenHelper.a = false;
        TuSpecialScreenHelper.b = 0;
    }
}
