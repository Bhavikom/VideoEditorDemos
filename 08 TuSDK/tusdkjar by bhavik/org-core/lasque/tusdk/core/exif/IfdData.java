package org.lasque.tusdk.core.exif;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class IfdData
{
  private static final int[] a = { 0, 1, 2, 3, 4 };
  private final int b;
  private final Map<Short, ExifTag> c = new HashMap();
  private int d = 0;
  
  IfdData(int paramInt)
  {
    this.b = paramInt;
  }
  
  protected static int[] getIfds()
  {
    return a;
  }
  
  protected ExifTag getTag(short paramShort)
  {
    return (ExifTag)this.c.get(Short.valueOf(paramShort));
  }
  
  protected ExifTag setTag(ExifTag paramExifTag)
  {
    paramExifTag.setIfd(this.b);
    return (ExifTag)this.c.put(Short.valueOf(paramExifTag.getTagId()), paramExifTag);
  }
  
  protected boolean checkCollision(short paramShort)
  {
    return this.c.get(Short.valueOf(paramShort)) != null;
  }
  
  protected void removeTag(short paramShort)
  {
    this.c.remove(Short.valueOf(paramShort));
  }
  
  protected int getOffsetToNextIfd()
  {
    return this.d;
  }
  
  protected void setOffsetToNextIfd(int paramInt)
  {
    this.d = paramInt;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if ((paramObject instanceof IfdData))
    {
      IfdData localIfdData = (IfdData)paramObject;
      if ((localIfdData.getId() == this.b) && (localIfdData.getTagCount() == getTagCount()))
      {
        ExifTag[] arrayOfExifTag1 = localIfdData.getAllTags();
        for (ExifTag localExifTag1 : arrayOfExifTag1) {
          if (!ExifInterface.isOffsetTag(localExifTag1.getTagId()))
          {
            ExifTag localExifTag2 = (ExifTag)this.c.get(Short.valueOf(localExifTag1.getTagId()));
            if (!localExifTag1.equals(localExifTag2)) {
              return false;
            }
          }
        }
        return true;
      }
    }
    return false;
  }
  
  protected int getTagCount()
  {
    return this.c.size();
  }
  
  protected int getId()
  {
    return this.b;
  }
  
  protected ExifTag[] getAllTags()
  {
    return (ExifTag[])this.c.values().toArray(new ExifTag[this.c.size()]);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\exif\IfdData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */