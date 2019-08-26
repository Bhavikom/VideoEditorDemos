package org.lasque.tusdk.core.media.codec;

import android.media.DrmInitData;
import android.media.MediaCodec.CryptoInfo;
import android.media.MediaExtractor.CasInfo;
import android.media.MediaFormat;
import android.os.PersistableBundle;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;
import org.lasque.tusdk.core.media.codec.decoder.TuSdkMediaFrameInfo;

public abstract interface TuSdkMediaExtractor
{
  public abstract void release();
  
  public abstract void syncPlay();
  
  public abstract void play();
  
  public abstract MediaFormat getTrackFormat(int paramInt);
  
  public abstract int getTrackCount();
  
  public abstract void selectTrack(int paramInt);
  
  public abstract long getSampleTime();
  
  public abstract int getSampleFlags();
  
  public abstract int getSampleTrackIndex();
  
  public abstract boolean getSampleCryptoInfo(MediaCodec.CryptoInfo paramCryptoInfo);
  
  public abstract long getCachedDuration();
  
  public abstract MediaExtractor.CasInfo getCasInfo(int paramInt);
  
  public abstract DrmInitData getDrmInitData();
  
  public abstract PersistableBundle getMetrics();
  
  public abstract Map<UUID, byte[]> getPsshInfo();
  
  public abstract boolean hasCacheReachedEndOfStream();
  
  public abstract boolean isPlaying();
  
  public abstract void pause();
  
  public abstract void resume();
  
  public abstract long seekTo(long paramLong);
  
  public abstract long seekTo(long paramLong, boolean paramBoolean);
  
  public abstract long seekTo(long paramLong, int paramInt);
  
  public abstract boolean advance();
  
  public abstract int readSampleData(ByteBuffer paramByteBuffer, int paramInt);
  
  public abstract long getFrameIntervalUs();
  
  public abstract TuSdkMediaFrameInfo getFrameInfo();
  
  public abstract long advanceNestest(long paramLong);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\TuSdkMediaExtractor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */