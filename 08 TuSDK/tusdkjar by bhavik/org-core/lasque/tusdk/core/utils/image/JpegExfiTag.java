package org.lasque.tusdk.core.utils.image;

import android.annotation.SuppressLint;
import java.util.HashMap;
import java.util.Map;
import org.lasque.tusdk.core.exif.ExifInterface;

public enum JpegExfiTag
{
  private int a;
  private String b;
  @SuppressLint({"UseSparseArrays"})
  private static final Map<Integer, JpegExfiTag> c;
  
  private JpegExfiTag(int paramInt, String paramString)
  {
    this.a = paramInt;
    this.b = paramString;
  }
  
  public int getTagId()
  {
    return this.a;
  }
  
  public String getName()
  {
    return this.b;
  }
  
  public static JpegExfiTag getTag(int paramInt)
  {
    JpegExfiTag localJpegExfiTag = (JpegExfiTag)c.get(Integer.valueOf(paramInt));
    return localJpegExfiTag;
  }
  
  public static String getTagName(int paramInt)
  {
    JpegExfiTag localJpegExfiTag = (JpegExfiTag)c.get(Integer.valueOf(paramInt));
    if (localJpegExfiTag != null) {
      return localJpegExfiTag.getName();
    }
    return null;
  }
  
  static
  {
    c = new HashMap();
    for (JpegExfiTag localJpegExfiTag : values()) {
      c.put(Integer.valueOf(localJpegExfiTag.getTagId()), localJpegExfiTag);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\image\JpegExfiTag.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */