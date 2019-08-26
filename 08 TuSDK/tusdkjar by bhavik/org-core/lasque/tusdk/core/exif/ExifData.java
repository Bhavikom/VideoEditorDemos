package org.lasque.tusdk.core.exif;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.lasque.tusdk.core.utils.TLog;

class ExifData
{
  private static final byte[] a = { 65, 83, 67, 73, 73, 0, 0, 0 };
  private static final byte[] b = { 74, 73, 83, 0, 0, 0, 0, 0 };
  private static final byte[] c = { 85, 78, 73, 67, 79, 68, 69, 0 };
  private List<ExifParser.Section> d;
  private final IfdData[] e = new IfdData[5];
  private final ByteOrder f;
  private byte[] g;
  private ArrayList<byte[]> h = new ArrayList();
  private int i = 0;
  private int j = -1;
  private int k = -1;
  private short l = 0;
  public int mUncompressedDataPosition = 0;
  
  ExifData(ByteOrder paramByteOrder)
  {
    this.f = paramByteOrder;
  }
  
  protected byte[] getCompressedThumbnail()
  {
    return this.g;
  }
  
  protected void setCompressedThumbnail(byte[] paramArrayOfByte)
  {
    this.g = paramArrayOfByte;
  }
  
  protected boolean hasCompressedThumbnail()
  {
    return this.g != null;
  }
  
  protected void setStripBytes(int paramInt, byte[] paramArrayOfByte)
  {
    if (paramInt < this.h.size())
    {
      this.h.set(paramInt, paramArrayOfByte);
    }
    else
    {
      for (int m = this.h.size(); m < paramInt; m++) {
        this.h.add(null);
      }
      this.h.add(paramArrayOfByte);
    }
  }
  
  protected int getStripCount()
  {
    return this.h.size();
  }
  
  protected byte[] getStrip(int paramInt)
  {
    return (byte[])this.h.get(paramInt);
  }
  
  protected boolean hasUncompressedStrip()
  {
    return this.h.size() != 0;
  }
  
  protected ByteOrder getByteOrder()
  {
    return this.f;
  }
  
  protected void addIfdData(IfdData paramIfdData)
  {
    this.e[paramIfdData.getId()] = paramIfdData;
  }
  
  protected ExifTag getTag(short paramShort, int paramInt)
  {
    IfdData localIfdData = this.e[paramInt];
    return localIfdData == null ? null : localIfdData.getTag(paramShort);
  }
  
  protected ExifTag addTag(ExifTag paramExifTag)
  {
    if (paramExifTag != null)
    {
      int m = paramExifTag.getIfd();
      return addTag(paramExifTag, m);
    }
    return null;
  }
  
  protected ExifTag addTag(ExifTag paramExifTag, int paramInt)
  {
    if ((paramExifTag != null) && (ExifTag.isValidIfd(paramInt)))
    {
      IfdData localIfdData = getOrCreateIfdData(paramInt);
      return localIfdData.setTag(paramExifTag);
    }
    return null;
  }
  
  protected IfdData getOrCreateIfdData(int paramInt)
  {
    IfdData localIfdData = this.e[paramInt];
    if (localIfdData == null)
    {
      localIfdData = new IfdData(paramInt);
      this.e[paramInt] = localIfdData;
    }
    return localIfdData;
  }
  
  protected void removeThumbnailData()
  {
    clearThumbnailAndStrips();
    this.e[1] = null;
  }
  
  protected void clearThumbnailAndStrips()
  {
    this.g = null;
    this.h.clear();
  }
  
  protected void removeTag(short paramShort, int paramInt)
  {
    IfdData localIfdData = this.e[paramInt];
    if (localIfdData == null) {
      return;
    }
    localIfdData.removeTag(paramShort);
  }
  
  protected String getUserComment()
  {
    IfdData localIfdData = this.e[0];
    if (localIfdData == null) {
      return null;
    }
    ExifTag localExifTag = localIfdData.getTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_USER_COMMENT));
    if (localExifTag == null) {
      return null;
    }
    if (localExifTag.getComponentCount() < 8) {
      return null;
    }
    byte[] arrayOfByte1 = new byte[localExifTag.getComponentCount()];
    localExifTag.getBytes(arrayOfByte1);
    byte[] arrayOfByte2 = new byte[8];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, 8);
    try
    {
      if (Arrays.equals(arrayOfByte2, a)) {
        return new String(arrayOfByte1, 8, arrayOfByte1.length - 8, "US-ASCII");
      }
      if (Arrays.equals(arrayOfByte2, b)) {
        return new String(arrayOfByte1, 8, arrayOfByte1.length - 8, "EUC-JP");
      }
      if (Arrays.equals(arrayOfByte2, c)) {
        return new String(arrayOfByte1, 8, arrayOfByte1.length - 8, "UTF-16");
      }
      return null;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      TLog.w("%s Failed to decode the user comment", new Object[] { "ExifData" });
    }
    return null;
  }
  
  protected List<ExifTag> getAllTags()
  {
    ArrayList localArrayList = new ArrayList();
    for (IfdData localIfdData : this.e) {
      if (localIfdData != null)
      {
        ExifTag[] arrayOfExifTag1 = localIfdData.getAllTags();
        if (arrayOfExifTag1 != null) {
          for (ExifTag localExifTag : arrayOfExifTag1) {
            localArrayList.add(localExifTag);
          }
        }
      }
    }
    if (localArrayList.size() == 0) {
      return null;
    }
    return localArrayList;
  }
  
  protected List<ExifTag> getAllTagsForIfd(int paramInt)
  {
    IfdData localIfdData = this.e[paramInt];
    if (localIfdData == null) {
      return null;
    }
    ExifTag[] arrayOfExifTag1 = localIfdData.getAllTags();
    if (arrayOfExifTag1 == null) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(arrayOfExifTag1.length);
    for (ExifTag localExifTag : arrayOfExifTag1) {
      localArrayList.add(localExifTag);
    }
    if (localArrayList.size() == 0) {
      return null;
    }
    return localArrayList;
  }
  
  protected List<ExifTag> getAllTagsForTagId(short paramShort)
  {
    ArrayList localArrayList = new ArrayList();
    for (IfdData localIfdData : this.e) {
      if (localIfdData != null)
      {
        ExifTag localExifTag = localIfdData.getTag(paramShort);
        if (localExifTag != null) {
          localArrayList.add(localExifTag);
        }
      }
    }
    if (localArrayList.size() == 0) {
      return null;
    }
    return localArrayList;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if ((paramObject instanceof ExifData))
    {
      ExifData localExifData = (ExifData)paramObject;
      if ((localExifData.f != this.f) || (localExifData.h.size() != this.h.size()) || (!Arrays.equals(localExifData.g, this.g))) {
        return false;
      }
      for (int m = 0; m < this.h.size(); m++) {
        if (!Arrays.equals((byte[])localExifData.h.get(m), (byte[])this.h.get(m))) {
          return false;
        }
      }
      for (m = 0; m < 5; m++)
      {
        IfdData localIfdData1 = localExifData.getIfdData(m);
        IfdData localIfdData2 = getIfdData(m);
        if ((localIfdData1 != localIfdData2) && (localIfdData1 != null) && (!localIfdData1.equals(localIfdData2))) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
  
  protected IfdData getIfdData(int paramInt)
  {
    if (ExifTag.isValidIfd(paramInt)) {
      return this.e[paramInt];
    }
    return null;
  }
  
  protected void setQualityGuess(int paramInt)
  {
    this.i = paramInt;
  }
  
  public int getQualityGuess()
  {
    return this.i;
  }
  
  protected void setImageSize(int paramInt1, int paramInt2)
  {
    this.k = paramInt1;
    this.j = paramInt2;
  }
  
  public int[] getImageSize()
  {
    return new int[] { this.k, this.j };
  }
  
  public void setJpegProcess(short paramShort)
  {
    this.l = paramShort;
  }
  
  public short getJpegProcess()
  {
    return this.l;
  }
  
  public void setSections(List<ExifParser.Section> paramList)
  {
    this.d = paramList;
  }
  
  public List<ExifParser.Section> getSections()
  {
    return this.d;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\exif\ExifData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */