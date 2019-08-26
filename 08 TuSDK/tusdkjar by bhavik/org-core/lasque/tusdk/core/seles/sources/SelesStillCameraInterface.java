package org.lasque.tusdk.core.seles.sources;

import android.graphics.Bitmap;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public abstract interface SelesStillCameraInterface
  extends SelesBaseCameraInterface
{
  public abstract void setAutoReleaseAfterCaptured(boolean paramBoolean);
  
  public abstract void capturePhotoAsJPEGData(CapturePhotoAsJPEGDataCallback paramCapturePhotoAsJPEGDataCallback);
  
  public abstract void capturePhotoAsBitmap(SelesOutInput paramSelesOutInput, CapturePhotoAsBitmapCallback paramCapturePhotoAsBitmapCallback);
  
  public abstract void capturePhotoAsBitmap(SelesOutInput paramSelesOutInput, ImageOrientation paramImageOrientation, CapturePhotoAsBitmapCallback paramCapturePhotoAsBitmapCallback);
  
  public static abstract interface CapturePhotoAsBitmapCallback
  {
    public abstract void onCapturePhotoAsBitmap(Bitmap paramBitmap);
  }
  
  public static abstract interface CapturePhotoAsJPEGDataCallback
  {
    public abstract void onCapturePhotoAsJPEGData(byte[] paramArrayOfByte);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\sources\SelesStillCameraInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */