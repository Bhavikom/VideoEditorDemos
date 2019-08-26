package org.lasque.tusdk.core.media.codec.video;

import android.graphics.Rect;
import android.media.MediaCodecInfo.CodecProfileLevel;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.seles.extend.SelesTextureSizeAlign;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.RectHelper;

public class TuSdkVideoSupport
{
  public String name;
  public String mimeType;
  public boolean isEncoder;
  public List<Integer> colorFormats;
  public List<MediaCodecInfo.CodecProfileLevel> profileLevel;
  public int widthAlignment = 2;
  public int heightAlignment = 2;
  public int widthRangeMax;
  public int widthRangeMin;
  public int heightRangeMax;
  public int heightRangeMin;
  public int bitrateRangeMax;
  public int bitrateRangeMin;
  public int frameRatesMax;
  public int frameRatesMin;
  public boolean bitrateCQ;
  public boolean bitrateVBR;
  public boolean bitrateCBR;
  
  public boolean isSupportSize(TuSdkSize paramTuSdkSize)
  {
    if (paramTuSdkSize == null) {
      return false;
    }
    return (paramTuSdkSize.width >= this.widthRangeMin) && (paramTuSdkSize.width <= this.widthRangeMax) && (paramTuSdkSize.height >= this.heightRangeMin) && (paramTuSdkSize.height <= this.heightRangeMax);
  }
  
  public TuSdkSize getSupportSize(TuSdkSize paramTuSdkSize)
  {
    if (paramTuSdkSize == null) {
      return paramTuSdkSize;
    }
    return getSupportSize(paramTuSdkSize.width, paramTuSdkSize.height);
  }
  
  public TuSdkSize getSupportSize(int paramInt1, int paramInt2)
  {
    int i = paramInt1 < 1 ? this.widthRangeMin : paramInt1;
    int j = paramInt2 < 1 ? this.heightRangeMin : paramInt2;
    TuSdkSize localTuSdkSize = TuSdkSize.create(i, j);
    if (localTuSdkSize.minSide() < this.widthRangeMin)
    {
      if (localTuSdkSize.minSide() == localTuSdkSize.width)
      {
        i = this.widthRangeMin;
        j = i / localTuSdkSize.width * localTuSdkSize.height;
      }
      else
      {
        j = this.widthRangeMin;
        i = j / localTuSdkSize.height * localTuSdkSize.width;
      }
      localTuSdkSize.width = i;
      localTuSdkSize.height = j;
    }
    SelesTextureSizeAlign localSelesTextureSizeAlign1 = SelesTextureSizeAlign.getValue(this.widthAlignment, true, false);
    SelesTextureSizeAlign localSelesTextureSizeAlign2 = SelesTextureSizeAlign.getValue(this.heightAlignment, true, false);
    localTuSdkSize.width = localSelesTextureSizeAlign1.align(localTuSdkSize.width);
    localTuSdkSize.height = localSelesTextureSizeAlign2.align(localTuSdkSize.height);
    Rect localRect = RectHelper.makeRectWithAspectRatioInsideRect(localTuSdkSize, new Rect(0, 0, this.widthRangeMax, this.heightRangeMax));
    if ((localTuSdkSize.width > localRect.width()) || (localTuSdkSize.height > localRect.height()))
    {
      localTuSdkSize.width = localRect.width();
      localTuSdkSize.height = localRect.height();
    }
    localSelesTextureSizeAlign1 = SelesTextureSizeAlign.getValue(this.widthAlignment, false, false);
    localSelesTextureSizeAlign2 = SelesTextureSizeAlign.getValue(this.heightAlignment, false, false);
    localTuSdkSize.width = localSelesTextureSizeAlign1.align(localTuSdkSize.width);
    localTuSdkSize.height = localSelesTextureSizeAlign2.align(localTuSdkSize.height);
    return localTuSdkSize;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("TuSdkVideoSupport").append("{ \n");
    localStringBuffer.append("name: ").append(this.name).append(", \n");
    localStringBuffer.append("mime: ").append(this.mimeType).append(", \n");
    localStringBuffer.append("isEncoder: ").append(this.isEncoder).append(", \n");
    localStringBuffer.append("Alignment: [").append(this.widthAlignment).append(", ").append(this.heightAlignment).append("] , \n");
    localStringBuffer.append("widthRange: [").append(this.widthRangeMin).append("-").append(this.widthRangeMax).append("] , \n");
    localStringBuffer.append("heightRange: [").append(this.heightRangeMin).append("-").append(this.heightRangeMax).append("] , \n");
    localStringBuffer.append("bitrateRange: [").append(this.bitrateRangeMin).append("-").append(this.bitrateRangeMax).append("] , \n");
    localStringBuffer.append("frameRates: [").append(this.frameRatesMin).append("-").append(this.frameRatesMax).append("] , \n");
    if (this.colorFormats != null)
    {
      localStringBuffer.append("colorFormats: [");
      int i = 0;
      int j = this.colorFormats.size();
      while (i < j)
      {
        localStringBuffer.append(String.format("0x%X", new Object[] { this.colorFormats.get(i) })).append(", ");
        i++;
      }
      localStringBuffer.append("], \n");
    }
    if (this.profileLevel != null)
    {
      localStringBuffer.append("profileLevel: [");
      Iterator localIterator = this.profileLevel.iterator();
      while (localIterator.hasNext())
      {
        MediaCodecInfo.CodecProfileLevel localCodecProfileLevel = (MediaCodecInfo.CodecProfileLevel)localIterator.next();
        localStringBuffer.append("{Profile: ").append(String.format("0x%X", new Object[] { Integer.valueOf(localCodecProfileLevel.profile) })).append(", Level: ").append(String.format("0x%X", new Object[] { Integer.valueOf(localCodecProfileLevel.level) })).append("}, ");
      }
      localStringBuffer.append("], \n");
    }
    localStringBuffer.append("bitrateCQ: ").append(this.bitrateCQ).append(", \n");
    localStringBuffer.append("bitrateVBR: ").append(this.bitrateVBR).append(", \n");
    localStringBuffer.append("bitrateCBR: ").append(this.bitrateCBR).append(", \n");
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\video\TuSdkVideoSupport.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */