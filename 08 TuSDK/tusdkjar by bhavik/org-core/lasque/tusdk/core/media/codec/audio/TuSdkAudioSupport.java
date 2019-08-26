package org.lasque.tusdk.core.media.codec.audio;

import android.media.MediaCodecInfo.CodecProfileLevel;
import java.util.Iterator;
import java.util.List;

public class TuSdkAudioSupport
{
  public String name;
  public String mimeType;
  public boolean isEncoder;
  public List<MediaCodecInfo.CodecProfileLevel> profileLevel;
  public int maxChannelCount;
  public List<Integer> sampleRates;
  public int bitrateRangeMax;
  public int bitrateRangeMin;
  public boolean bitrateCQ;
  public boolean bitrateVBR;
  public boolean bitrateCBR;
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("TuSdkAudioSupport").append("{ \n");
    localStringBuffer.append("name: ").append(this.name).append(", \n");
    localStringBuffer.append("mime: ").append(this.mimeType).append(", \n");
    localStringBuffer.append("isEncoder: ").append(this.isEncoder).append(", \n");
    localStringBuffer.append("maxChannelCount: ").append(this.maxChannelCount).append(", \n");
    localStringBuffer.append("bitrateRange: [").append(this.bitrateRangeMin).append("-").append(this.bitrateRangeMax).append("] , \n");
    if (this.sampleRates != null)
    {
      localStringBuffer.append("sampleRates: [");
      int i = 0;
      int j = this.sampleRates.size();
      while (i < j)
      {
        localStringBuffer.append(this.sampleRates.get(i)).append(", ");
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


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioSupport.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */