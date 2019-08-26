package org.lasque.tusdk.api.audio.preproc.mixer;

import android.net.Uri;
import android.text.TextUtils;
import java.io.File;
import java.io.RandomAccessFile;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

public class TuSDKAudioRenderEntry
  extends TuSdkMediaDataSource
{
  private TuSDKAudioRenderInfoWrap a;
  private boolean b;
  private TuSdkTimeRange c;
  private boolean d;
  private TuSdkTimeRange e;
  private float f = 1.0F;
  private RandomAccessFile g;
  
  public TuSDKAudioRenderEntry() {}
  
  public TuSDKAudioRenderEntry(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    switch (1.a[paramTuSdkMediaDataSource.getMediaDataType().ordinal()])
    {
    case 1: 
      setUri(paramTuSdkMediaDataSource.getContext(), paramTuSdkMediaDataSource.getUri(), paramTuSdkMediaDataSource.getRequestHeaders());
      break;
    case 2: 
      setMediaDataSource(paramTuSdkMediaDataSource.getMediaDataSource());
      break;
    case 3: 
      setFileDescriptor(paramTuSdkMediaDataSource.getFileDescriptor(), paramTuSdkMediaDataSource.getFileDescriptorOffset(), paramTuSdkMediaDataSource.getFileDescriptorLength());
      break;
    case 4: 
      setPath(paramTuSdkMediaDataSource.getPath(), paramTuSdkMediaDataSource.getRequestHeaders());
    }
  }
  
  public TuSDKAudioRenderEntry(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    this.a = TuSDKAudioRenderInfoWrap.createWithAudioInfo(paramTuSdkAudioInfo);
  }
  
  public TuSDKAudioRenderEntry(TuSDKAudioRenderInfoWrap paramTuSDKAudioRenderInfoWrap)
  {
    this.a = paramTuSDKAudioRenderInfoWrap;
  }
  
  public TuSDKAudioRenderEntry setRawInfo(TuSDKAudioRenderInfoWrap paramTuSDKAudioRenderInfoWrap)
  {
    this.a = paramTuSDKAudioRenderInfoWrap;
    return this;
  }
  
  public TuSDKAudioRenderInfoWrap getRawInfo()
  {
    if (this.a == null) {
      this.a = TuSDKAudioRenderInfoWrap.createWithMediaDataSource(this);
    }
    return this.a;
  }
  
  public TuSDKAudioRenderEntry setTrunk(boolean paramBoolean)
  {
    this.b = paramBoolean;
    return this;
  }
  
  public boolean isTrunk()
  {
    return this.b;
  }
  
  public TuSDKAudioRenderEntry setLooping(boolean paramBoolean)
  {
    this.d = paramBoolean;
    return this;
  }
  
  public boolean isLooping()
  {
    return this.d;
  }
  
  public TuSDKAudioRenderEntry setTimeRange(TuSdkTimeRange paramTuSdkTimeRange)
  {
    this.c = paramTuSdkTimeRange;
    return this;
  }
  
  public TuSdkTimeRange getTimeRange()
  {
    return this.c;
  }
  
  public boolean validateTimeRange()
  {
    return (this.c != null) && (this.c.isValid());
  }
  
  public TuSDKAudioRenderEntry setCutTimeRange(TuSdkTimeRange paramTuSdkTimeRange)
  {
    this.e = paramTuSdkTimeRange;
    return this;
  }
  
  public float getVolume()
  {
    return this.f;
  }
  
  public TuSDKAudioRenderEntry setVolume(float paramFloat)
  {
    if ((this.f <= 1.0F) && (this.f >= 0.0F)) {
      this.f = paramFloat;
    }
    return this;
  }
  
  public int bytesSizeOfTimeRangeStartPosition()
  {
    if ((!validateTimeRange()) || (getTimeRange().getStartTime() == 0.0F) || (this.a == null)) {
      return 0;
    }
    double d1 = Math.ceil(getTimeRange().getStartTime());
    return this.a.bytesCountOfTime((int)d1);
  }
  
  public int bytesSizeOfTimeRangeEndPosition()
  {
    if ((!validateTimeRange()) || (this.a == null)) {
      return 0;
    }
    double d1 = Math.ceil(getTimeRange().getEndTime());
    return this.a.bytesCountOfTime((int)d1);
  }
  
  public TuSdkTimeRange getCutTimeRange()
  {
    return this.e;
  }
  
  public boolean validateCutTimeRange()
  {
    return (this.e != null) && (this.e.isValid());
  }
  
  public boolean clearDecodeCahceInfo()
  {
    if ((this.a == null) || (!this.a.isValid())) {
      return false;
    }
    if (!TextUtils.isEmpty(this.a.getPath())) {
      new File(this.a.getPath()).delete();
    }
    this.a = null;
    return false;
  }
  
  public String getFingerprint()
  {
    return getFingerprint(null);
  }
  
  public String getFingerprint(String paramString)
  {
    if (!isValid()) {
      return null;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    if (validateCutTimeRange()) {
      localStringBuilder.append(getCutTimeRange().getStartTime()).append(getCutTimeRange().getEndTime());
    }
    if (!TextUtils.isEmpty(getPath())) {
      localStringBuilder.append(getPath());
    } else {
      localStringBuilder.append(getUri().getPath());
    }
    if (!TextUtils.isEmpty(paramString)) {
      localStringBuilder.append(paramString);
    }
    return StringHelper.md5(localStringBuilder.toString());
  }
  
  protected void finalize()
  {
    super.finalize();
  }
  
  public int readAudioData(byte[] paramArrayOfByte)
  {
    if (this.g == null) {
      this.g = new RandomAccessFile(getRawInfo().getPath(), "r");
    }
    return this.g.read(paramArrayOfByte);
  }
  
  public int readAudioDataForTimeUs(long paramLong, byte[] paramArrayOfByte)
  {
    if ((getTimeRange() != null) && (!getTimeRange().contains(paramLong))) {
      return -1;
    }
    if (this.g == null) {
      this.g = new RandomAccessFile(getRawInfo().getPath(), "r");
    }
    int i = bytesSizeOfTimeRangeStartPosition();
    return this.g.read(paramArrayOfByte);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\audio\preproc\mixer\TuSDKAudioRenderEntry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */