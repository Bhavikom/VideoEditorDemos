package org.lasque.tusdk.core.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

public class AssetsHelper
{
  private static final Hashtable<String, Hashtable<String, String>> a = new Hashtable();
  
  public static boolean hasAssets(Context paramContext, String paramString)
  {
    return getAssetPath(paramContext, paramString) != null;
  }
  
  public static String getAssetsText(Context paramContext, String paramString)
  {
    InputStream localInputStream = getAssetsStream(paramContext, paramString);
    if (localInputStream == null) {
      return null;
    }
    try
    {
      byte[] arrayOfByte = new byte[localInputStream.available()];
      localInputStream.read(arrayOfByte);
      String str = new String(arrayOfByte, "UTF-8");
      return str;
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "getAssetsText: %s | %s", new Object[] { paramString, paramString });
    }
    finally
    {
      FileHelper.safeClose(localInputStream);
    }
    return null;
  }
  
  public static InputStream getAssetsStream(Context paramContext, String paramString)
  {
    String str = getAssetPath(paramContext, paramString);
    if (str == null) {
      return null;
    }
    try
    {
      return paramContext.getAssets().open(str, 1);
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "getAssetsStream: %s | %s", new Object[] { paramString, str });
    }
    return null;
  }
  
  public static AssetFileDescriptor getAssetFileDescriptor(Context paramContext, String paramString)
  {
    String str = getAssetPath(paramContext, paramString);
    if (str == null) {
      return null;
    }
    try
    {
      return paramContext.getAssets().openFd(str);
    }
    catch (Exception localException)
    {
      TLog.e(localException, "getAssetFileDescriptor: %s | %s", new Object[] { paramString, str });
    }
    return null;
  }
  
  @SuppressLint({"DefaultLocale"})
  public static String getAssetPath(Context paramContext, String paramString)
  {
    if ((paramContext == null) || (paramString == null)) {
      return null;
    }
    File localFile = new File(paramString);
    String str1 = localFile.getParent() != null ? localFile.getParent() : "";
    Hashtable localHashtable = getAssetsFiles(paramContext, str1);
    if (localHashtable == null) {
      return null;
    }
    Iterator localIterator = localHashtable.values().iterator();
    while (localIterator.hasNext())
    {
      String str2 = (String)localIterator.next();
      if (str2.equalsIgnoreCase(paramString)) {
        return str2;
      }
    }
    return (String)localHashtable.get(localFile.getName().toLowerCase());
  }
  
  public static Hashtable<String, String> getAssetsFiles(Context paramContext, String paramString)
  {
    if ((paramString == null) || (paramContext == null)) {
      return null;
    }
    Hashtable localHashtable = (Hashtable)a.get(paramString);
    if (localHashtable != null) {
      return localHashtable;
    }
    try
    {
      String[] arrayOfString = paramContext.getAssets().list(paramString);
      return a(paramString, arrayOfString);
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "getAssetsFiles: %s", new Object[] { paramString });
    }
    return null;
  }
  
  @SuppressLint({"DefaultLocale"})
  private static Hashtable<String, String> a(String paramString, String[] paramArrayOfString)
  {
    if ((paramArrayOfString == null) || (paramArrayOfString.length == 0)) {
      return null;
    }
    Hashtable localHashtable = new Hashtable(paramArrayOfString.length);
    String str1 = paramString.length() > 0 ? paramString + File.separator : paramString;
    for (String str2 : paramArrayOfString) {
      localHashtable.put(StringHelper.removeSuffix(str2).toLowerCase(), String.format("%s%s", new Object[] { str1, str2 }));
    }
    a.put(paramString, localHashtable);
    return localHashtable;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\AssetsHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */