package org.lasque.tusdk.api.audio.preproc.mixer;

import android.annotation.TargetApi;
import android.net.Uri;
import android.text.TextUtils;
import java.io.File;
import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import org.lasque.tusdk.core.decoder.TuSDKAudioInfo;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

@TargetApi(16)
public class TuSDKAudioEntry
  extends TuSDKMediaDataSource
{
  private TuSDKAudioInfo a;
  private boolean b;
  private TuSdkTimeRange c;
  private boolean d;
  private TuSdkTimeRange e;
  private float f = 1.0F;
  
  public TuSDKAudioEntry() {}
  
  public TuSDKAudioEntry(String paramString)
  {
    super(paramString);
  }
  
  public TuSDKAudioEntry(Uri paramUri)
  {
    super(paramUri);
  }
  
  public TuSDKAudioEntry(TuSDKAudioInfo paramTuSDKAudioInfo)
  {
    this.a = paramTuSDKAudioInfo;
  }
  
  public TuSDKAudioEntry(TuSDKMediaDataSource paramTuSDKMediaDataSource)
  {
    super(paramTuSDKMediaDataSource);
  }
  
  public TuSDKAudioEntry setRawInfo(TuSDKAudioInfo paramTuSDKAudioInfo)
  {
    this.a = paramTuSDKAudioInfo;
    return this;
  }
  
  public TuSDKAudioInfo getRawInfo()
  {
    if (this.a == null) {
      this.a = TuSDKAudioInfo.createWithMediaDataSource(this);
    }
    return this.a;
  }
  
  public TuSDKAudioEntry setTrunk(boolean paramBoolean)
  {
    this.b = paramBoolean;
    return this;
  }
  
  public boolean isTrunk()
  {
    return this.b;
  }
  
  public TuSDKAudioEntry setLooping(boolean paramBoolean)
  {
    this.d = paramBoolean;
    return this;
  }
  
  public boolean isLooping()
  {
    return this.d;
  }
  
  public TuSDKAudioEntry setTimeRange(TuSdkTimeRange paramTuSdkTimeRange)
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
  
  public TuSDKAudioEntry setCutTimeRange(TuSdkTimeRange paramTuSdkTimeRange)
  {
    this.e = paramTuSdkTimeRange;
    return this;
  }
  
  public float getVolume()
  {
    return this.f;
  }
  
  public TuSDKAudioEntry setVolume(float paramFloat)
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
    if (this.a.getFilePath() != null) {
      new File(this.a.getFilePath()).delete();
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
    if (!TextUtils.isEmpty(getFilePath())) {
      localStringBuilder.append(getFilePath());
    } else {
      localStringBuilder.append(getFileUri().getPath());
    }
    if (!TextUtils.isEmpty(paramString)) {
      localStringBuilder.append(paramString);
    }
    return StringHelper.md5(localStringBuilder.toString());
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\audio\preproc\mixer\TuSDKAudioEntry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */