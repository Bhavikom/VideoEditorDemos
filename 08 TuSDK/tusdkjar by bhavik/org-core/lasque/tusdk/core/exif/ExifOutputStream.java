package org.lasque.tusdk.core.exif;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.utils.TLog;

class ExifOutputStream
{
  private final ExifInterface a;
  private ExifData b;
  private ByteBuffer c = ByteBuffer.allocate(4);
  
  protected ExifOutputStream(ExifInterface paramExifInterface)
  {
    this.a = paramExifInterface;
  }
  
  protected ExifData getExifData()
  {
    return this.b;
  }
  
  protected void setExifData(ExifData paramExifData)
  {
    this.b = paramExifData;
  }
  
  public void writeExifData(OutputStream paramOutputStream)
  {
    if (this.b == null) {
      return;
    }
    TLog.i("%s Writing exif data...", new Object[] { "ExifOutputStream" });
    ArrayList localArrayList = a(this.b);
    a();
    int i = b();
    if (i + 8 > 65535) {
      throw new IOException("Exif header is too large (>64Kb)");
    }
    BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(paramOutputStream, 65536);
    OrderedDataOutputStream localOrderedDataOutputStream = new OrderedDataOutputStream(localBufferedOutputStream);
    localOrderedDataOutputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
    localOrderedDataOutputStream.write(255);
    localOrderedDataOutputStream.write(225);
    localOrderedDataOutputStream.writeShort((short)(i + 8));
    localOrderedDataOutputStream.writeInt(1165519206);
    localOrderedDataOutputStream.writeShort((short)0);
    if (this.b.getByteOrder() == ByteOrder.BIG_ENDIAN) {
      localOrderedDataOutputStream.writeShort((short)19789);
    } else {
      localOrderedDataOutputStream.writeShort((short)18761);
    }
    localOrderedDataOutputStream.setByteOrder(this.b.getByteOrder());
    localOrderedDataOutputStream.writeShort((short)42);
    localOrderedDataOutputStream.writeInt(8);
    b(localOrderedDataOutputStream);
    a(localOrderedDataOutputStream);
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      ExifTag localExifTag = (ExifTag)localIterator.next();
      this.b.addTag(localExifTag);
    }
    localOrderedDataOutputStream.flush();
  }
  
  private ArrayList<ExifTag> a(ExifData paramExifData)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramExifData.getAllTags().iterator();
    while (localIterator.hasNext())
    {
      ExifTag localExifTag = (ExifTag)localIterator.next();
      if ((localExifTag.getValue() == null) && (!ExifInterface.isOffsetTag(localExifTag.getTagId())))
      {
        paramExifData.removeTag(localExifTag.getTagId(), localExifTag.getIfd());
        localArrayList.add(localExifTag);
      }
    }
    return localArrayList;
  }
  
  private void a(OrderedDataOutputStream paramOrderedDataOutputStream)
  {
    if (this.b.hasCompressedThumbnail())
    {
      TLog.d("%s writing thumbnail..", new Object[] { "ExifOutputStream" });
      paramOrderedDataOutputStream.write(this.b.getCompressedThumbnail());
    }
    else if (this.b.hasUncompressedStrip())
    {
      TLog.d("%s writing uncompressed strip..", new Object[] { "ExifOutputStream" });
      for (int i = 0; i < this.b.getStripCount(); i++) {
        paramOrderedDataOutputStream.write(this.b.getStrip(i));
      }
    }
  }
  
  private void b(OrderedDataOutputStream paramOrderedDataOutputStream)
  {
    a(this.b.getIfdData(0), paramOrderedDataOutputStream);
    a(this.b.getIfdData(2), paramOrderedDataOutputStream);
    IfdData localIfdData1 = this.b.getIfdData(3);
    if (localIfdData1 != null) {
      a(localIfdData1, paramOrderedDataOutputStream);
    }
    IfdData localIfdData2 = this.b.getIfdData(4);
    if (localIfdData2 != null) {
      a(localIfdData2, paramOrderedDataOutputStream);
    }
    IfdData localIfdData3 = this.b.getIfdData(1);
    if (localIfdData3 != null) {
      a(this.b.getIfdData(1), paramOrderedDataOutputStream);
    }
  }
  
  private void a(IfdData paramIfdData, OrderedDataOutputStream paramOrderedDataOutputStream)
  {
    ExifTag[] arrayOfExifTag1 = paramIfdData.getAllTags();
    paramOrderedDataOutputStream.writeShort((short)arrayOfExifTag1.length);
    ExifTag localExifTag;
    for (localExifTag : arrayOfExifTag1)
    {
      paramOrderedDataOutputStream.writeShort(localExifTag.getTagId());
      paramOrderedDataOutputStream.writeShort(localExifTag.getDataType());
      paramOrderedDataOutputStream.writeInt(localExifTag.getComponentCount());
      if (localExifTag.getDataSize() > 4)
      {
        paramOrderedDataOutputStream.writeInt(localExifTag.getOffset());
      }
      else
      {
        a(localExifTag, paramOrderedDataOutputStream);
        int k = 0;
        int m = 4 - localExifTag.getDataSize();
        while (k < m)
        {
          paramOrderedDataOutputStream.write(0);
          k++;
        }
      }
    }
    paramOrderedDataOutputStream.writeInt(paramIfdData.getOffsetToNextIfd());
    for (localExifTag : arrayOfExifTag1) {
      if (localExifTag.getDataSize() > 4) {
        a(localExifTag, paramOrderedDataOutputStream);
      }
    }
  }
  
  private int a(IfdData paramIfdData, int paramInt)
  {
    paramInt += 2 + paramIfdData.getTagCount() * 12 + 4;
    ExifTag[] arrayOfExifTag1 = paramIfdData.getAllTags();
    for (ExifTag localExifTag : arrayOfExifTag1) {
      if (localExifTag.getDataSize() > 4)
      {
        localExifTag.setOffset(paramInt);
        paramInt += localExifTag.getDataSize();
      }
    }
    return paramInt;
  }
  
  private void a()
  {
    IfdData localIfdData1 = this.b.getIfdData(0);
    if (localIfdData1 == null)
    {
      localIfdData1 = new IfdData(0);
      this.b.addIfdData(localIfdData1);
    }
    ExifTag localExifTag1 = this.a.buildUninitializedTag(ExifInterface.TAG_EXIF_IFD);
    if (localExifTag1 == null) {
      throw new IOException("No definition for crucial exif tag: " + ExifInterface.TAG_EXIF_IFD);
    }
    localIfdData1.setTag(localExifTag1);
    IfdData localIfdData2 = this.b.getIfdData(2);
    if (localIfdData2 == null)
    {
      localIfdData2 = new IfdData(2);
      this.b.addIfdData(localIfdData2);
    }
    IfdData localIfdData3 = this.b.getIfdData(4);
    if (localIfdData3 != null)
    {
      localObject1 = this.a.buildUninitializedTag(ExifInterface.TAG_GPS_IFD);
      if (localObject1 == null) {
        throw new IOException("No definition for crucial exif tag: " + ExifInterface.TAG_GPS_IFD);
      }
      localIfdData1.setTag((ExifTag)localObject1);
    }
    Object localObject1 = this.b.getIfdData(3);
    if (localObject1 != null)
    {
      localObject2 = this.a.buildUninitializedTag(ExifInterface.TAG_INTEROPERABILITY_IFD);
      if (localObject2 == null) {
        throw new IOException("No definition for crucial exif tag: " + ExifInterface.TAG_INTEROPERABILITY_IFD);
      }
      localIfdData2.setTag((ExifTag)localObject2);
    }
    Object localObject2 = this.b.getIfdData(1);
    ExifTag localExifTag3;
    if (this.b.hasCompressedThumbnail())
    {
      if (localObject2 == null)
      {
        localObject2 = new IfdData(1);
        this.b.addIfdData((IfdData)localObject2);
      }
      ExifTag localExifTag2 = this.a.buildUninitializedTag(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT);
      if (localExifTag2 == null) {
        throw new IOException("No definition for crucial exif tag: " + ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT);
      }
      ((IfdData)localObject2).setTag(localExifTag2);
      localExifTag3 = this.a.buildUninitializedTag(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
      if (localExifTag3 == null) {
        throw new IOException("No definition for crucial exif tag: " + ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
      }
      localExifTag3.setValue(this.b.getCompressedThumbnail().length);
      ((IfdData)localObject2).setTag(localExifTag3);
      ((IfdData)localObject2).removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_OFFSETS));
      ((IfdData)localObject2).removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_BYTE_COUNTS));
    }
    else if (this.b.hasUncompressedStrip())
    {
      if (localObject2 == null)
      {
        localObject2 = new IfdData(1);
        this.b.addIfdData((IfdData)localObject2);
      }
      int i = this.b.getStripCount();
      localExifTag3 = this.a.buildUninitializedTag(ExifInterface.TAG_STRIP_OFFSETS);
      if (localExifTag3 == null) {
        throw new IOException("No definition for crucial exif tag: " + ExifInterface.TAG_STRIP_OFFSETS);
      }
      ExifTag localExifTag4 = this.a.buildUninitializedTag(ExifInterface.TAG_STRIP_BYTE_COUNTS);
      if (localExifTag4 == null) {
        throw new IOException("No definition for crucial exif tag: " + ExifInterface.TAG_STRIP_BYTE_COUNTS);
      }
      long[] arrayOfLong = new long[i];
      for (int j = 0; j < this.b.getStripCount(); j++) {
        arrayOfLong[j] = this.b.getStrip(j).length;
      }
      localExifTag4.setValue(arrayOfLong);
      ((IfdData)localObject2).setTag(localExifTag3);
      ((IfdData)localObject2).setTag(localExifTag4);
      ((IfdData)localObject2).removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT));
      ((IfdData)localObject2).removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH));
    }
    else if (localObject2 != null)
    {
      ((IfdData)localObject2).removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_OFFSETS));
      ((IfdData)localObject2).removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_BYTE_COUNTS));
      ((IfdData)localObject2).removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT));
      ((IfdData)localObject2).removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH));
    }
  }
  
  private int b()
  {
    int i = 8;
    IfdData localIfdData1 = this.b.getIfdData(0);
    i = a(localIfdData1, i);
    localIfdData1.getTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_EXIF_IFD)).setValue(i);
    IfdData localIfdData2 = this.b.getIfdData(2);
    i = a(localIfdData2, i);
    IfdData localIfdData3 = this.b.getIfdData(3);
    if (localIfdData3 != null)
    {
      localIfdData2.getTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_INTEROPERABILITY_IFD)).setValue(i);
      i = a(localIfdData3, i);
    }
    IfdData localIfdData4 = this.b.getIfdData(4);
    if (localIfdData4 != null)
    {
      localIfdData1.getTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_GPS_IFD)).setValue(i);
      i = a(localIfdData4, i);
    }
    IfdData localIfdData5 = this.b.getIfdData(1);
    if (localIfdData5 != null)
    {
      localIfdData1.setOffsetToNextIfd(i);
      i = a(localIfdData5, i);
    }
    if (this.b.hasCompressedThumbnail())
    {
      localIfdData5.getTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT)).setValue(i);
      i += this.b.getCompressedThumbnail().length;
    }
    else if (this.b.hasUncompressedStrip())
    {
      int j = this.b.getStripCount();
      long[] arrayOfLong = new long[j];
      for (int k = 0; k < this.b.getStripCount(); k++)
      {
        arrayOfLong[k] = i;
        i += this.b.getStrip(k).length;
      }
      localIfdData5.getTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_OFFSETS)).setValue(arrayOfLong);
    }
    return i;
  }
  
  static void a(ExifTag paramExifTag, OrderedDataOutputStream paramOrderedDataOutputStream)
  {
    byte[] arrayOfByte;
    int i;
    int j;
    switch (paramExifTag.getDataType())
    {
    case 2: 
      arrayOfByte = paramExifTag.getStringByte();
      if (arrayOfByte.length == paramExifTag.getComponentCount())
      {
        arrayOfByte[(arrayOfByte.length - 1)] = 0;
        paramOrderedDataOutputStream.write(arrayOfByte);
      }
      else
      {
        paramOrderedDataOutputStream.write(arrayOfByte);
        paramOrderedDataOutputStream.write(0);
      }
      break;
    case 4: 
    case 9: 
      i = 0;
      j = paramExifTag.getComponentCount();
      while (i < j)
      {
        paramOrderedDataOutputStream.writeInt((int)paramExifTag.getValueAt(i));
        i++;
      }
      break;
    case 5: 
    case 10: 
      i = 0;
      j = paramExifTag.getComponentCount();
      while (i < j)
      {
        paramOrderedDataOutputStream.writeRational(paramExifTag.getRational(i));
        i++;
      }
      break;
    case 1: 
    case 7: 
      arrayOfByte = new byte[paramExifTag.getComponentCount()];
      paramExifTag.getBytes(arrayOfByte);
      paramOrderedDataOutputStream.write(arrayOfByte);
      break;
    case 3: 
      i = 0;
      j = paramExifTag.getComponentCount();
      while (i < j)
      {
        paramOrderedDataOutputStream.writeShort((short)(int)paramExifTag.getValueAt(i));
        i++;
      }
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\exif\ExifOutputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */