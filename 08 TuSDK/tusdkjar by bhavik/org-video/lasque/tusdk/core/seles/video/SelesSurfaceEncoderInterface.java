package org.lasque.tusdk.core.seles.video;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoDataEncoderInterface;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption.WaterMarkPosition;

public abstract interface SelesSurfaceEncoderInterface
  extends TuSDKVideoDataEncoderInterface, SelesContext.SelesInput
{
  public abstract void startRecording(EGLContext paramEGLContext, SurfaceTexture paramSurfaceTexture);
  
  public abstract void stopRecording();
  
  public abstract void pausdRecording();
  
  public abstract boolean isRecording();
  
  public abstract boolean isPaused();
  
  public abstract void setCropRegion(RectF paramRectF);
  
  public abstract void setEnableHorizontallyFlip(boolean paramBoolean);
  
  public abstract void updateWaterMark(Bitmap paramBitmap, int paramInt, TuSdkWaterMarkOption.WaterMarkPosition paramWaterMarkPosition);
  
  public abstract void destroy();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\seles\video\SelesSurfaceEncoderInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */