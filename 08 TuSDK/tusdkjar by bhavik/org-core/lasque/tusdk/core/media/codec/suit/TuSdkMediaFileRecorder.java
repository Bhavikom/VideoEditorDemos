package org.lasque.tusdk.core.media.codec.suit;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.opengl.EGLContext;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import org.lasque.tusdk.core.seles.sources.SelesWatermark;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public abstract interface TuSdkMediaFileRecorder
{
  public abstract void setRecordProgress(TuSdkMediaFileRecorderProgress paramTuSdkMediaFileRecorderProgress);
  
  public abstract void setOutputFilePath(String paramString);
  
  public abstract void setWatermark(SelesWatermark paramSelesWatermark);
  
  public abstract int setOutputVideoFormat(MediaFormat paramMediaFormat);
  
  public abstract int setOutputAudioFormat(MediaFormat paramMediaFormat);
  
  public abstract TuSdkAudioInfo getOutputAudioInfo();
  
  public abstract TuSdkFilterBridge getFilterBridge();
  
  public abstract void setFilterBridge(TuSdkFilterBridge paramTuSdkFilterBridge);
  
  public abstract void disconnect();
  
  public abstract void setSurfaceRender(TuSdkSurfaceRender paramTuSdkSurfaceRender);
  
  public abstract void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender);
  
  public abstract void changeSpeed(float paramFloat);
  
  public abstract boolean startRecord(EGLContext paramEGLContext);
  
  public abstract void stopRecord();
  
  public abstract void pauseRecord();
  
  public abstract void resumeRecord();
  
  public abstract void newFrameReadyInGLThread(long paramLong);
  
  public abstract void newFrameReadyWithAudio(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract void release();
  
  public static abstract interface TuSdkMediaFileRecorderProgress
  {
    public abstract void onProgress(long paramLong, TuSdkMediaDataSource paramTuSdkMediaDataSource);
    
    public abstract void onCompleted(Exception paramException, TuSdkMediaDataSource paramTuSdkMediaDataSource, TuSdkMediaTimeline paramTuSdkMediaTimeline);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\TuSdkMediaFileRecorder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */