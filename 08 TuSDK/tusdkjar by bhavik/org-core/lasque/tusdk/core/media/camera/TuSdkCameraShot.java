package org.lasque.tusdk.core.media.camera;

import android.graphics.Bitmap;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.sources.SelesPicture;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public abstract interface TuSdkCameraShot
{
  public abstract void configure(TuSdkCameraBuilder paramTuSdkCameraBuilder);
  
  public abstract void changeStatus(TuSdkCamera.TuSdkCameraStatus paramTuSdkCameraStatus);
  
  public abstract void setDetectionImageFace(TuSdkCameraShotFaceFaceAligment paramTuSdkCameraShotFaceFaceAligment);
  
  public abstract void setDetectionShotFilter(TuSdkCameraShotFilter paramTuSdkCameraShotFilter);
  
  public abstract void takeJpegPicture(TuSdkResult paramTuSdkResult, TuSdkCameraShotResultListener paramTuSdkCameraShotResultListener);
  
  public abstract boolean isAutoReleaseAfterCaptured();
  
  public abstract void processData(TuSdkResult paramTuSdkResult);
  
  public static abstract interface TuSdkCameraShotFilter
  {
    public abstract SelesOutInput getFilters(FaceAligment[] paramArrayOfFaceAligment, SelesPicture paramSelesPicture);
  }
  
  public static abstract interface TuSdkCameraShotFaceFaceAligment
  {
    public abstract FaceAligment[] detectionImageFace(Bitmap paramBitmap, ImageOrientation paramImageOrientation);
  }
  
  public static abstract interface TuSdkCameraShotResultListener
  {
    public abstract void onShotResule(byte[] paramArrayOfByte);
  }
  
  public static abstract interface TuSdkCameraShotListener
  {
    public abstract void onCameraWillShot(TuSdkResult paramTuSdkResult);
    
    public abstract void onCameraShotFailed(TuSdkResult paramTuSdkResult);
    
    public abstract void onCameraShotData(TuSdkResult paramTuSdkResult);
    
    public abstract void onCameraShotBitmap(TuSdkResult paramTuSdkResult);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\camera\TuSdkCameraShot.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */