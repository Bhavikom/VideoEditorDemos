package org.lasque.tusdk.core.exif;

import java.io.InputStream;
import org.lasque.tusdk.core.utils.TLog;

class ExifReader
{
  private final ExifInterface a;
  
  ExifReader(ExifInterface paramExifInterface)
  {
    this.a = paramExifInterface;
  }
  
  protected ExifData read(InputStream paramInputStream, int paramInt)
  {
    ExifParser localExifParser = ExifParser.parse(paramInputStream, paramInt, this.a);
    ExifData localExifData = new ExifData(localExifParser.getByteOrder());
    localExifData.setSections(localExifParser.getSections());
    localExifData.mUncompressedDataPosition = localExifParser.getUncompressedDataPosition();
    localExifData.setQualityGuess(localExifParser.getQualityGuess());
    localExifData.setJpegProcess(localExifParser.getJpegProcess());
    int i = localExifParser.getImageWidth();
    int j = localExifParser.getImageLength();
    if ((i > 0) && (j > 0)) {
      localExifData.setImageSize(i, j);
    }
    for (int k = localExifParser.next(); k != 5; k = localExifParser.next())
    {
      ExifTag localExifTag;
      byte[] arrayOfByte;
      switch (k)
      {
      case 0: 
        localExifData.addIfdData(new IfdData(localExifParser.getCurrentIfd()));
        break;
      case 1: 
        localExifTag = localExifParser.getTag();
        if (!localExifTag.hasValue()) {
          localExifParser.registerForTagValue(localExifTag);
        } else if (localExifParser.isDefinedTag(localExifTag.getIfd(), localExifTag.getTagId())) {
          localExifData.getIfdData(localExifTag.getIfd()).setTag(localExifTag);
        } else {
          TLog.w("%s skip tag because not registered in the tag table:%s", new Object[] { "ExifReader", localExifTag });
        }
        break;
      case 2: 
        localExifTag = localExifParser.getTag();
        if (localExifTag.getDataType() == 7) {
          localExifParser.readFullTagValue(localExifTag);
        }
        localExifData.getIfdData(localExifTag.getIfd()).setTag(localExifTag);
        break;
      case 3: 
        arrayOfByte = new byte[localExifParser.getCompressedImageSize()];
        if (arrayOfByte.length == localExifParser.read(arrayOfByte)) {
          localExifData.setCompressedThumbnail(arrayOfByte);
        } else {
          TLog.w("%s Failed to read the compressed thumbnail", new Object[] { "ExifReader" });
        }
        break;
      case 4: 
        arrayOfByte = new byte[localExifParser.getStripSize()];
        if (arrayOfByte.length == localExifParser.read(arrayOfByte)) {
          localExifData.setStripBytes(localExifParser.getStripIndex(), arrayOfByte);
        } else {
          TLog.w("%s Failed to read the strip bytes", new Object[] { "ExifReader" });
        }
        break;
      }
    }
    return localExifData;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\exif\ExifReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */