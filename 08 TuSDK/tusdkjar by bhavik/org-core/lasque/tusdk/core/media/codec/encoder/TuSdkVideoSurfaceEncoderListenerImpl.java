package org.lasque.tusdk.core.media.codec.encoder;

import android.media.MediaCodec.BufferInfo;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class TuSdkVideoSurfaceEncoderListenerImpl
  implements TuSdkVideoSurfaceEncoderListener
{
  public void onEncoderDrawFrame(long paramLong, boolean paramBoolean) {}
  
  public void onEncoderUpdated(MediaCodec.BufferInfo paramBufferInfo) {}
  
  public void onEncoderCompleted(Exception paramException) {}
  
  public void onSurfaceDestory(GL10 paramGL10) {}
  
  public void onSurfaceCreated(GL10 paramGL10, EGLConfig paramEGLConfig) {}
  
  public void onSurfaceChanged(GL10 paramGL10, int paramInt1, int paramInt2) {}
  
  public void onDrawFrame(GL10 paramGL10) {}
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\encoder\TuSdkVideoSurfaceEncoderListenerImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */