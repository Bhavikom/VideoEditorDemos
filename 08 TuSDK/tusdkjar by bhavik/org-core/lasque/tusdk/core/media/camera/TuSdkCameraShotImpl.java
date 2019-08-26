package org.lasque.tusdk.core.media.camera;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.MediaPlayer;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.exif.ExifInterface;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.seles.sources.SelesPicture;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ExifHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSdkCameraShotImpl
  implements TuSdkCameraShot
{
  private boolean a;
  private boolean b;
  private boolean c;
  private int d;
  private MediaPlayer e;
  private TuSdkCameraShot.TuSdkCameraShotListener f;
  private TuSdkCameraShot.TuSdkCameraShotFaceFaceAligment g;
  private TuSdkCameraShot.TuSdkCameraShotFilter h;
  private Camera.ShutterCallback i = new Camera.ShutterCallback()
  {
    public void onShutter() {}
  };
  private TuSdkCameraBuilder j;
  
  public boolean isAutoReleaseAfterCaptured()
  {
    return this.a;
  }
  
  public void setAutoReleaseAfterCaptured(boolean paramBoolean)
  {
    this.a = paramBoolean;
  }
  
  public boolean isOutputImageData()
  {
    return this.b;
  }
  
  public void setOutputImageData(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }
  
  public boolean isDisableCaptureSound()
  {
    return this.c;
  }
  
  public void setDisableCaptureSound(boolean paramBoolean)
  {
    this.c = paramBoolean;
  }
  
  public int getCaptureSoundRawId()
  {
    return this.d;
  }
  
  public void setCaptureSoundRawId(int paramInt)
  {
    this.d = paramInt;
    if (this.e != null)
    {
      this.e.release();
      this.e = null;
    }
    if (this.d != 0)
    {
      setDisableCaptureSound(true);
      this.e = MediaPlayer.create(TuSdkContext.context(), this.d);
    }
  }
  
  private void a()
  {
    if ((this.e == null) || (!this.c)) {
      return;
    }
    this.e.start();
  }
  
  public void setShotListener(TuSdkCameraShot.TuSdkCameraShotListener paramTuSdkCameraShotListener)
  {
    this.f = paramTuSdkCameraShotListener;
  }
  
  public void setShutterCallback(Camera.ShutterCallback paramShutterCallback)
  {
    this.i = paramShutterCallback;
  }
  
  public Camera.ShutterCallback getShutterCallback()
  {
    if (isDisableCaptureSound()) {
      return null;
    }
    return this.i;
  }
  
  public void takeJpegPicture(TuSdkResult paramTuSdkResult, final TuSdkCameraShot.TuSdkCameraShotResultListener paramTuSdkCameraShotResultListener)
  {
    if ((this.j == null) || (this.j.getOrginCamera() == null))
    {
      TLog.w("%s takeJpegPicture need OrginCamera.", new Object[] { "TuSdkCameraShotImpl" });
      return;
    }
    final Camera localCamera = this.j.getOrginCamera();
    if (this.f != null) {
      this.f.onCameraWillShot(paramTuSdkResult);
    }
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        try
        {
          localCamera.takePicture(TuSdkCameraShotImpl.this.getShutterCallback(), null, new Camera.PictureCallback()
          {
            public void onPictureTaken(byte[] paramAnonymous2ArrayOfByte, Camera paramAnonymous2Camera)
            {
              TuSdkCameraShotImpl.a(TuSdkCameraShotImpl.this);
              TuSdkCameraShotImpl.2.this.b.onShotResule(paramAnonymous2ArrayOfByte);
            }
          });
        }
        catch (RuntimeException localRuntimeException)
        {
          TLog.e(localRuntimeException, "%s takeJpegPicture failed.", new Object[] { "TuSdkCameraShotImpl" });
          paramTuSdkCameraShotResultListener.onShotResule(null);
        }
      }
    });
  }
  
  public void processData(final TuSdkResult paramTuSdkResult)
  {
    if (paramTuSdkResult.imageData == null)
    {
      if (this.f != null) {
        this.f.onCameraShotFailed(paramTuSdkResult);
      }
      return;
    }
    paramTuSdkResult.metadata = ExifHelper.getExifInterface(paramTuSdkResult.imageData);
    if (this.b)
    {
      if (this.f != null) {
        this.f.onCameraShotData(paramTuSdkResult);
      }
      return;
    }
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        Bitmap localBitmap = BitmapHelper.imageDecode(paramTuSdkResult.imageData, true);
        localBitmap = BitmapHelper.imageResize(localBitmap, paramTuSdkResult.outputSize, false, paramTuSdkResult.imageOrientation);
        if (localBitmap == null)
        {
          TLog.e("%s conver bitmap failed, result: %s", new Object[] { "TuSdkCameraShotImpl", paramTuSdkResult });
          if (TuSdkCameraShotImpl.b(TuSdkCameraShotImpl.this) != null) {
            TuSdkCameraShotImpl.b(TuSdkCameraShotImpl.this).onCameraShotFailed(paramTuSdkResult);
          }
          return;
        }
        if ((paramTuSdkResult.imageOrientation != ImageOrientation.Up) || (paramTuSdkResult.imageOrientation != ImageOrientation.UpMirrored))
        {
          paramTuSdkResult.image = BitmapHelper.imageRotaing(paramTuSdkResult.image, ImageOrientation.Up);
          paramTuSdkResult.imageOrientation = ImageOrientation.Up;
        }
        paramTuSdkResult.imageData = null;
        paramTuSdkResult.image = localBitmap;
        FaceAligment[] arrayOfFaceAligment = null;
        if (TuSdkCameraShotImpl.c(TuSdkCameraShotImpl.this) != null) {
          arrayOfFaceAligment = TuSdkCameraShotImpl.c(TuSdkCameraShotImpl.this).detectionImageFace(localBitmap, paramTuSdkResult.imageOrientation);
        }
        TuSdkSize localTuSdkSize = BitmapHelper.computerScaleSize(localBitmap, paramTuSdkResult.outputSize, false, false);
        if ((paramTuSdkResult.imageOrientation != ImageOrientation.Up) || (paramTuSdkResult.imageOrientation != ImageOrientation.UpMirrored) || (paramTuSdkResult.imageOrientation != ImageOrientation.Down) || (paramTuSdkResult.imageOrientation != ImageOrientation.DownMirrored)) {
          localTuSdkSize = TuSdkSize.create(localTuSdkSize.height, localTuSdkSize.width);
        }
        SelesPicture localSelesPicture = new SelesPicture(localBitmap, false);
        localSelesPicture.setScaleSize(localTuSdkSize);
        paramTuSdkResult.imageSizeRatio = localTuSdkSize.minMaxRatio();
        Rect localRect = RectHelper.computerMinMaxSideInRegionRatio(localSelesPicture.getScaleSize(), paramTuSdkResult.imageSizeRatio);
        localSelesPicture.setOutputRect(localRect);
        localSelesPicture.setInputRotation(paramTuSdkResult.imageOrientation);
        if (TuSdkCameraShotImpl.d(TuSdkCameraShotImpl.this) != null) {
          localSelesPicture.addTarget(TuSdkCameraShotImpl.d(TuSdkCameraShotImpl.this).getFilters(arrayOfFaceAligment, localSelesPicture), 0);
        }
        localSelesPicture.processImage();
        paramTuSdkResult.image = localSelesPicture.imageFromCurrentlyProcessedOutput();
        paramTuSdkResult.outputSize = TuSdkSize.create(paramTuSdkResult.image);
        if (paramTuSdkResult.metadata == null)
        {
          paramTuSdkResult.metadata.setTagValue(ExifInterface.TAG_IMAGE_WIDTH, Integer.valueOf(paramTuSdkResult.outputSize.width));
          paramTuSdkResult.metadata.setTagValue(ExifInterface.TAG_IMAGE_LENGTH, Integer.valueOf(paramTuSdkResult.outputSize.height));
          paramTuSdkResult.metadata.setTagValue(ExifInterface.TAG_ORIENTATION, Integer.valueOf(paramTuSdkResult.imageOrientation.getExifOrientation()));
        }
        if (TuSdkCameraShotImpl.b(TuSdkCameraShotImpl.this) != null) {
          TuSdkCameraShotImpl.b(TuSdkCameraShotImpl.this).onCameraShotBitmap(paramTuSdkResult);
        }
      }
    });
  }
  
  public void configure(TuSdkCameraBuilder paramTuSdkCameraBuilder)
  {
    if (paramTuSdkCameraBuilder == null)
    {
      TLog.e("%s configure builder is empty.", new Object[] { "TuSdkCameraShotImpl" });
      return;
    }
    this.j = paramTuSdkCameraBuilder;
  }
  
  public void changeStatus(TuSdkCamera.TuSdkCameraStatus paramTuSdkCameraStatus) {}
  
  public void setDetectionImageFace(TuSdkCameraShot.TuSdkCameraShotFaceFaceAligment paramTuSdkCameraShotFaceFaceAligment)
  {
    this.g = paramTuSdkCameraShotFaceFaceAligment;
  }
  
  public void setDetectionShotFilter(TuSdkCameraShot.TuSdkCameraShotFilter paramTuSdkCameraShotFilter)
  {
    this.h = paramTuSdkCameraShotFilter;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\camera\TuSdkCameraShotImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */