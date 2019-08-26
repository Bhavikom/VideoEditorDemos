package org.lasque.tusdk.impl;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.TLog;

public class TuSpecialScreenHelper
{
  private static boolean a = false;
  private static int b = 0;
  
  public static void dealNotchScreen()
  {
    if (h())
    {
      a = true;
      b = i();
    }
    else if (f())
    {
      a = true;
      b = g();
    }
    else if (c())
    {
      a = true;
      b = d();
    }
    else if (a())
    {
      a = true;
      b = b();
    }
    else
    {
      a = false;
    }
  }
  
  public static boolean isNotchScreen()
  {
    return a;
  }
  
  public static int getNotchHeight()
  {
    return b;
  }
  
  private static boolean a()
  {
    String str = a("ro.miui.notch");
    return (!str.equals("")) && (Integer.valueOf(str).intValue() == 1);
  }
  
  private static int b()
  {
    int i = 0;
    int j = TuSdkContext.context().getResources().getIdentifier("notch_height", "dimen", "android");
    if (j > 0) {
      i = TuSdkContext.context().getResources().getDimensionPixelSize(j);
    }
    return i;
  }
  
  private static boolean c()
  {
    boolean bool = false;
    try
    {
      ClassLoader localClassLoader = TuSdkContext.context().getClassLoader();
      Class localClass = localClassLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
      Method localMethod = localClass.getMethod("hasNotchInScreen", new Class[0]);
      bool = ((Boolean)localMethod.invoke(localClass, new Object[0])).booleanValue();
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      TLog.d("hasNotchInScreen ClassNotFoundException", new Object[0]);
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      TLog.d("hasNotchInScreen NoSuchMethodException", new Object[0]);
    }
    catch (Exception localException)
    {
      TLog.d("hasNotchInScreen Exception", new Object[0]);
    }
    return bool;
  }
  
  private static int d()
  {
    int[] arrayOfInt = e();
    return arrayOfInt[1] - arrayOfInt[0];
  }
  
  private static int[] e()
  {
    int[] arrayOfInt = { 0, 0 };
    try
    {
      ClassLoader localClassLoader = TuSdkContext.context().getClassLoader();
      Class localClass = localClassLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
      Method localMethod = localClass.getMethod("getNotchSize", new Class[0]);
      arrayOfInt = (int[])localMethod.invoke(localClass, new Object[0]);
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      TLog.d("getNotchSize ClassNotFoundException", new Object[0]);
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      TLog.d("getNotchSize NoSuchMethodException", new Object[0]);
    }
    catch (Exception localException)
    {
      TLog.d("getNotchSize Exception", new Object[0]);
    }
    return arrayOfInt;
  }
  
  private static boolean f()
  {
    boolean bool = false;
    try
    {
      ClassLoader localClassLoader = TuSdkContext.context().getClassLoader();
      Class localClass = localClassLoader.loadClass("android.util.FtFeature");
      Method localMethod = localClass.getMethod("isFeatureSupport", new Class[] { Integer.TYPE });
      bool = ((Boolean)localMethod.invoke(localClass, new Object[] { Integer.valueOf(32) })).booleanValue();
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      TLog.d("hasNotchInScreen ClassNotFoundException", new Object[0]);
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      TLog.d("hasNotchInScreen NoSuchMethodException", new Object[0]);
    }
    catch (Exception localException)
    {
      TLog.d("hasNotchInScreen Exception", new Object[0]);
    }
    return bool;
  }
  
  private static int g()
  {
    return TuSdkContext.dip2px(27.0F);
  }
  
  private static boolean h()
  {
    return TuSdkContext.context().getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
  }
  
  private static int i()
  {
    int i = 0;
    String str1 = a("ro.oppo.screen.heteromorphism");
    if (!str1.equals(""))
    {
      String str2 = str1.substring(1, str1.length());
      String[] arrayOfString1 = str2.split(":");
      String[] arrayOfString2 = arrayOfString1[0].split(",");
      String[] arrayOfString3 = arrayOfString1[1].split(",");
      i = Integer.valueOf(arrayOfString3[1]).intValue() - Integer.valueOf(arrayOfString2[1]).intValue();
    }
    return i;
  }
  
  private static String a(String paramString)
  {
    String str = "";
    Class localClass = null;
    try
    {
      localClass = Class.forName("android.os.SystemProperties");
      Method localMethod = localClass.getMethod("get", new Class[] { String.class });
      Object localObject = localClass.newInstance();
      str = (String)localMethod.invoke(localObject, new Object[] { paramString });
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      TLog.d("error" + localClassNotFoundException.toString(), new Object[0]);
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      TLog.d("error" + localNoSuchMethodException.toString(), new Object[0]);
    }
    catch (InstantiationException localInstantiationException)
    {
      TLog.d("error" + localInstantiationException.toString(), new Object[0]);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      TLog.d("error" + localIllegalAccessException.toString(), new Object[0]);
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      TLog.d("error" + localIllegalArgumentException.toString(), new Object[0]);
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      TLog.d("error" + localInvocationTargetException.toString(), new Object[0]);
    }
    return str;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\TuSpecialScreenHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */