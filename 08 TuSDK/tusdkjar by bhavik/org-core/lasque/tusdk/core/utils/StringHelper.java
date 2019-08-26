package org.lasque.tusdk.core.utils;

import android.annotation.SuppressLint;
import android.util.Base64;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper
{
  public static final String EMPTY = "";
  
  public static boolean isEmpty(String paramString)
  {
    return (paramString == null) || (paramString.length() == 0);
  }
  
  public static boolean isNotEmpty(String paramString)
  {
    return !isEmpty(paramString);
  }
  
  public static boolean isBlank(String paramString)
  {
    int i;
    if ((paramString == null) || ((i = paramString.length()) == 0)) {
      return true;
    }
    for (int j = 0; j < i; j++) {
      if (!Character.isWhitespace(paramString.charAt(j))) {
        return false;
      }
    }
    return true;
  }
  
  public static boolean isNotBlank(String paramString)
  {
    return !isBlank(paramString);
  }
  
  public static String trim(String paramString)
  {
    return paramString == null ? null : paramString.trim();
  }
  
  public static String trimToNull(String paramString)
  {
    String str = trim(paramString);
    return isEmpty(str) ? null : str;
  }
  
  public static String trimToEmpty(String paramString)
  {
    return paramString == null ? "" : paramString.trim();
  }
  
  public static String md5(String paramString)
  {
    return md5(paramString.getBytes());
  }
  
  public static String md5(byte[] paramArrayOfByte)
  {
    MessageDigest localMessageDigest = null;
    try
    {
      localMessageDigest = MessageDigest.getInstance("MD5");
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new RuntimeException(localNoSuchAlgorithmException);
    }
    localMessageDigest.update(paramArrayOfByte);
    BigInteger localBigInteger = new BigInteger(1, localMessageDigest.digest());
    String str = String.format("%032x", new Object[] { localBigInteger });
    return str;
  }
  
  public static String encryptMd5(String paramString)
  {
    char[] arrayOfChar = paramString.toCharArray();
    for (int i = 0; i < arrayOfChar.length; i++) {
      arrayOfChar[i] = ((char)(arrayOfChar[i] ^ 0x6C));
    }
    String str = new String(arrayOfChar);
    return str;
  }
  
  public static String Base64Encode(String paramString)
  {
    if (paramString == null) {
      return "";
    }
    return Base64.encodeToString(ByteUtils.getBytes(paramString), 2);
  }
  
  @SuppressLint({"DefaultLocale"})
  public static String uuid()
  {
    UUID localUUID = UUID.randomUUID();
    return localUUID.toString().toLowerCase();
  }
  
  public static HashMap<String, String> urlQuery(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    String[] arrayOfString1 = paramString.split("&");
    if ((arrayOfString1 == null) || (arrayOfString1.length == 0)) {
      return null;
    }
    HashMap localHashMap = new HashMap(arrayOfString1.length);
    for (String str : arrayOfString1)
    {
      String[] arrayOfString3 = str.split("=");
      if ((arrayOfString3 != null) && (arrayOfString3.length == 2)) {
        localHashMap.put(arrayOfString3[0], arrayOfString3[1]);
      }
    }
    return localHashMap;
  }
  
  @SuppressLint({"SimpleDateFormat"})
  public static String timeStampString()
  {
    String str = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
    return str;
  }
  
  public static String formatByte(long paramLong)
  {
    if (paramLong < 1024L) {
      return String.format("%sB", new Object[] { Long.valueOf(paramLong) });
    }
    long l = paramLong / 1024L;
    if (l < 1024L) {
      return String.format("%sK", new Object[] { Long.valueOf(l) });
    }
    float f = (float)paramLong / 1048576.0F;
    if (f < 1024.0F) {
      return String.format(Locale.getDefault(), "%.1fM", new Object[] { Float.valueOf(f) });
    }
    f /= 1024.0F;
    return String.format(Locale.getDefault(), "%.1fG", new Object[] { Float.valueOf(f) });
  }
  
  public static boolean isLetter(String paramString, int paramInt)
  {
    if ((isEmpty(paramString)) || (paramInt > paramString.length())) {
      return false;
    }
    if (paramInt > 0) {
      paramString = paramString.substring(0, paramInt);
    }
    return paramString.matches("[a-zA-z]");
  }
  
  public static int parserInt(String paramString)
  {
    int i = 0;
    if (paramString == null) {
      return i;
    }
    try
    {
      i = Integer.parseInt(paramString);
    }
    catch (Exception localException) {}
    return i;
  }
  
  public static int parserInt(String paramString, int paramInt)
  {
    int i = 0;
    if (paramString == null) {
      return i;
    }
    try
    {
      i = Integer.parseInt(paramString, paramInt);
    }
    catch (Exception localException) {}
    return i;
  }
  
  public static float parseFloat(String paramString)
  {
    float f = 0.0F;
    if (paramString == null) {
      return f;
    }
    try
    {
      f = Float.parseFloat(paramString);
    }
    catch (Exception localException) {}
    return f;
  }
  
  public static long parserLong(String paramString)
  {
    long l = 0L;
    if (paramString == null) {
      return l;
    }
    try
    {
      l = Long.parseLong(paramString);
    }
    catch (Exception localException) {}
    return l;
  }
  
  @SuppressLint({"DefaultLocale"})
  public static String bytesToHexString(byte[] paramArrayOfByte)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if ((paramArrayOfByte == null) || (paramArrayOfByte.length <= 0)) {
      return null;
    }
    char[] arrayOfChar = new char[2];
    for (int i = 0; i < paramArrayOfByte.length; i++)
    {
      arrayOfChar[0] = Character.forDigit(paramArrayOfByte[i] >>> 4 & 0xF, 16);
      arrayOfChar[1] = Character.forDigit(paramArrayOfByte[i] & 0xF, 16);
      localStringBuilder.append(arrayOfChar);
    }
    return localStringBuilder.toString().toUpperCase();
  }
  
  public static int matchInt(String paramString1, String paramString2)
  {
    String str = matchString(paramString1, paramString2);
    return parserInt(str);
  }
  
  public static String matchString(String paramString1, String paramString2)
  {
    if ((paramString1 == null) || (paramString2 == null)) {
      return null;
    }
    Pattern localPattern = Pattern.compile(paramString2);
    Matcher localMatcher = localPattern.matcher(paramString1);
    if (localMatcher.find()) {
      return localMatcher.group(1);
    }
    return null;
  }
  
  public static ArrayList<String> matchStrings(String paramString1, String paramString2)
  {
    if ((paramString1 == null) || (paramString2 == null)) {
      return null;
    }
    ArrayList localArrayList = new ArrayList();
    Pattern localPattern = Pattern.compile(paramString2);
    Matcher localMatcher = localPattern.matcher(paramString1);
    while (localMatcher.find())
    {
      int i = 0;
      int j = localMatcher.groupCount();
      while (i <= j)
      {
        localArrayList.add(localMatcher.group(i));
        i++;
      }
    }
    return localArrayList;
  }
  
  public static String removeSuffix(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    int i = paramString.lastIndexOf(".");
    if (i > -1) {
      return paramString.substring(0, i);
    }
    return paramString;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\StringHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */