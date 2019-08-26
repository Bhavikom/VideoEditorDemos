package org.lasque.tusdk.core.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.utils.TLog;

public class URLEncodedUtils
{
  public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
  private static final BitSet a = new BitSet(256);
  private static final BitSet b = new BitSet(256);
  private static final BitSet c = new BitSet(256);
  private static final BitSet d = new BitSet(256);
  private static final BitSet e = new BitSet(256);
  private static final BitSet f = new BitSet(256);
  private static final BitSet g = new BitSet(256);
  
  public static URL getURL(String paramString)
  {
    try
    {
      return new URL(paramString);
    }
    catch (MalformedURLException localMalformedURLException)
    {
      TLog.e("getURI: %s", new Object[] { localMalformedURLException });
    }
    return null;
  }
  
  public static String format(List<BasicNameValuePair> paramList, String paramString)
  {
    return format(paramList, '&', paramString);
  }
  
  public static String format(List<BasicNameValuePair> paramList, char paramChar, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      BasicNameValuePair localBasicNameValuePair = (BasicNameValuePair)localIterator.next();
      String str1 = a(localBasicNameValuePair.getName(), paramString);
      String str2 = a(localBasicNameValuePair.getValue(), paramString);
      if (localStringBuilder.length() > 0) {
        localStringBuilder.append(paramChar);
      }
      localStringBuilder.append(str1);
      if (str2 != null)
      {
        localStringBuilder.append("=");
        localStringBuilder.append(str2);
      }
    }
    return localStringBuilder.toString();
  }
  
  private static String a(String paramString1, String paramString2)
  {
    if (paramString1 == null) {
      return null;
    }
    return a(paramString1, Charset.forName(paramString2 != null ? paramString2 : "UTF-8"), g, true);
  }
  
  private static String a(String paramString, Charset paramCharset, BitSet paramBitSet, boolean paramBoolean)
  {
    if (paramString == null) {
      return null;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    ByteBuffer localByteBuffer = paramCharset.encode(paramString);
    while (localByteBuffer.hasRemaining())
    {
      int i = localByteBuffer.get() & 0xFF;
      if (paramBitSet.get(i))
      {
        localStringBuilder.append((char)i);
      }
      else if ((paramBoolean) && (i == 32))
      {
        localStringBuilder.append('+');
      }
      else
      {
        localStringBuilder.append("%");
        char c1 = Character.toUpperCase(Character.forDigit(i >> 4 & 0xF, 16));
        char c2 = Character.toUpperCase(Character.forDigit(i & 0xF, 16));
        localStringBuilder.append(c1);
        localStringBuilder.append(c2);
      }
    }
    return localStringBuilder.toString();
  }
  
  static
  {
    for (int i = 97; i <= 122; i++) {
      a.set(i);
    }
    for (i = 65; i <= 90; i++) {
      a.set(i);
    }
    for (i = 48; i <= 57; i++) {
      a.set(i);
    }
    a.set(95);
    a.set(45);
    a.set(46);
    a.set(42);
    g.or(a);
    a.set(33);
    a.set(126);
    a.set(39);
    a.set(40);
    a.set(41);
    b.set(44);
    b.set(59);
    b.set(58);
    b.set(36);
    b.set(38);
    b.set(43);
    b.set(61);
    c.or(a);
    c.or(b);
    d.or(a);
    d.set(47);
    d.set(59);
    d.set(58);
    d.set(64);
    d.set(38);
    d.set(61);
    d.set(43);
    d.set(36);
    d.set(44);
    f.set(59);
    f.set(47);
    f.set(63);
    f.set(58);
    f.set(64);
    f.set(38);
    f.set(61);
    f.set(43);
    f.set(36);
    f.set(44);
    f.set(91);
    f.set(93);
    e.or(f);
    e.or(a);
  }
  
  public static class BasicNameValuePair
  {
    public final String mName;
    public final String mValue;
    
    public BasicNameValuePair(String paramString1, String paramString2)
    {
      this.mName = paramString1;
      this.mValue = paramString2;
    }
    
    public String getName()
    {
      return this.mName;
    }
    
    public String getValue()
    {
      return this.mValue;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\URLEncodedUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */