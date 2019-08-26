package org.lasque.tusdk.core.media.codec.suit.imageToVideo;

import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioEncodecSync;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioEncodecSyncBase;
import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaEncodecSync;
import org.lasque.tusdk.core.media.codec.sync.TuSdkVideoEncodecSync;
import org.lasque.tusdk.core.media.codec.sync.TuSdkVideoEncodecSyncBase;
import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;

public class TuSdkMediaVideoComposeSync
  implements TuSdkMediaEncodecSync
{
  private _VideoEncodecSync a;
  private _AudioEncodecSync b;
  private boolean c = false;
  
  public TuSdkAudioEncodecSync getAudioEncodecSync()
  {
    if (this.b == null) {
      this.b = new _AudioEncodecSync();
    }
    return this.b;
  }
  
  public TuSdkVideoEncodecSync getVideoEncodecSync()
  {
    if (this.a == null) {
      this.a = new _VideoEncodecSync();
    }
    return this.a;
  }
  
  public void release()
  {
    if (this.c) {
      return;
    }
    this.c = true;
    if (this.b != null)
    {
      this.b.release();
      this.b = null;
    }
    if (this.a != null)
    {
      this.a.release();
      this.a = null;
    }
  }
  
  public void syncVideoEncodecDrawFrame(long paramLong, boolean paramBoolean, TuSdkRecordSurface paramTuSdkRecordSurface, TuSdkEncodeSurface paramTuSdkEncodeSurface)
  {
    this.a.syncVideoEncodecDrawFrame(paramLong, paramBoolean, paramTuSdkRecordSurface, paramTuSdkEncodeSurface);
  }
  
  class _AudioEncodecSync
    extends TuSdkAudioEncodecSyncBase
  {
    _AudioEncodecSync() {}
  }
  
  class _VideoEncodecSync
    extends TuSdkVideoEncodecSyncBase
  {
    _VideoEncodecSync() {}
    
    protected boolean isLastDecodeFrame(long paramLong)
    {
      return true;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\imageToVideo\TuSdkMediaVideoComposeSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */