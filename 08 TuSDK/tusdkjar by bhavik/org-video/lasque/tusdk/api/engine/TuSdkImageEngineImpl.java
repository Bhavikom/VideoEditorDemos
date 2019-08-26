package org.lasque.tusdk.api.engine;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import java.nio.IntBuffer;
import org.lasque.tusdk.api.video.preproc.filter.TuSDKFilterPicture;
import org.lasque.tusdk.core.api.engine.TuSdkEngineOrientation;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.mergefilter.TuSDKComboFilterWrapChain;
import org.lasque.tusdk.core.seles.SelesParameters.FilterFacePositionInterface;
import org.lasque.tusdk.core.seles.sources.SelesOutput;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSdkImageEngineImpl
  implements TuSdkImageEngine
{
  private FaceAligment[] a;
  private TuSdkEngineOrientation b;
  private FilterWrap c;
  private boolean d;
  private TuSdkImageEngine.TuSdkPictureDataCompletedListener e;
  private static final int[][] f = { { 270, 180, 90, 0 }, { 180, 90, 0, 270 }, { 90, 0, 270, 180 }, { 0, 270, 180, 90 } };
  private static final int[][] g = { { 90, 0, 270, 180 }, { 180, 90, 0, 270 }, { 270, 180, 90, 0 }, { 0, 270, 180, 90 } };
  
  public void setFaceAligments(FaceAligment[] paramArrayOfFaceAligment)
  {
    this.a = paramArrayOfFaceAligment;
  }
  
  public void setEngineRotation(TuSdkEngineOrientation paramTuSdkEngineOrientation)
  {
    if (paramTuSdkEngineOrientation == null) {
      return;
    }
    this.b = paramTuSdkEngineOrientation;
  }
  
  public void setFilter(FilterWrap paramFilterWrap)
  {
    if (paramFilterWrap == null) {
      return;
    }
    this.c = paramFilterWrap;
  }
  
  public void setListener(TuSdkImageEngine.TuSdkPictureDataCompletedListener paramTuSdkPictureDataCompletedListener)
  {
    this.e = paramTuSdkPictureDataCompletedListener;
  }
  
  public void release() {}
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  private TuSdkSize a(ImageOrientation paramImageOrientation, TuSdkSize paramTuSdkSize)
  {
    TuSdkSize localTuSdkSize = TuSdkSize.create(paramTuSdkSize);
    if ((paramImageOrientation != null) && (paramImageOrientation.isTransposed()))
    {
      localTuSdkSize.width = paramTuSdkSize.height;
      localTuSdkSize.height = paramTuSdkSize.width;
    }
    return localTuSdkSize;
  }
  
  private ImageOrientation a(ImageOrientation paramImageOrientation)
  {
    if (paramImageOrientation == null) {
      return ImageOrientation.Up;
    }
    switch (4.a[paramImageOrientation.ordinal()])
    {
    case 1: 
      return ImageOrientation.UpMirrored;
    case 2: 
      return ImageOrientation.Up;
    case 3: 
      return ImageOrientation.DownMirrored;
    case 4: 
      return ImageOrientation.Down;
    case 5: 
      return ImageOrientation.RightMirrored;
    case 6: 
      return ImageOrientation.Right;
    case 7: 
      return ImageOrientation.LeftMirrored;
    case 8: 
      return ImageOrientation.Left;
    }
    return ImageOrientation.Up;
  }
  
  private ImageOrientation a(InterfaceOrientation paramInterfaceOrientation, int paramInt)
  {
    if (this.b == null) {
      return ImageOrientation.Up;
    }
    if (paramInterfaceOrientation == null) {
      paramInterfaceOrientation = InterfaceOrientation.Portrait;
    }
    if (this.b.getCameraFacing() == CameraConfigs.CameraFacing.Front) {
      return ImageOrientation.getValue(f[(paramInterfaceOrientation.getDegree() / 90)][(paramInt / 90)], true);
    }
    if (this.b.getCameraFacing() == CameraConfigs.CameraFacing.Back) {
      return ImageOrientation.getValue(g[(paramInterfaceOrientation.getDegree() / 90)][(paramInt / 90)], false);
    }
    return ImageOrientation.Up;
  }
  
  public void asyncProcessPictureData(byte[] paramArrayOfByte)
  {
    asyncProcessPictureData(paramArrayOfByte, InterfaceOrientation.Portrait, 0);
  }
  
  public void asyncProcessPictureData(byte[] paramArrayOfByte, InterfaceOrientation paramInterfaceOrientation)
  {
    asyncProcessPictureData(paramArrayOfByte, paramInterfaceOrientation, 0);
  }
  
  public void asyncProcessPictureData(byte[] paramArrayOfByte, InterfaceOrientation paramInterfaceOrientation, int paramInt)
  {
    if ((paramInt != 0) && (paramInt != 90) && (paramInt != 180) && (paramInt != 270)) {
      throw new IllegalStateException("Invalid rotation=" + paramInt);
    }
    asyncProcessPictureData(paramArrayOfByte, paramInterfaceOrientation, a(paramInterfaceOrientation, paramInt));
  }
  
  public final void asyncProcessPictureData(final byte[] paramArrayOfByte, InterfaceOrientation paramInterfaceOrientation, final ImageOrientation paramImageOrientation)
  {
    if ((this.d) || (paramArrayOfByte == null)) {
      return;
    }
    final InterfaceOrientation localInterfaceOrientation = paramInterfaceOrientation == null ? InterfaceOrientation.Portrait : paramInterfaceOrientation;
    int i = this.a != null ? this.a.length : 0;
    final FaceAligment[] arrayOfFaceAligment = new FaceAligment[i];
    if (this.a != null) {
      System.arraycopy(this.a, 0, arrayOfFaceAligment, 0, arrayOfFaceAligment.length);
    }
    this.d = true;
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        final FilterWrap localFilterWrap = TuSdkImageEngineImpl.a(TuSdkImageEngineImpl.this).clone();
        if ((localFilterWrap instanceof TuSDKComboFilterWrapChain)) {
          localFilterWrap.processImage();
        }
        localFilterWrap.processImage();
        final Bitmap localBitmap = BitmapHelper.imageDecode(paramArrayOfByte, true);
        ThreadHelper.runThread(new Runnable()
        {
          public void run()
          {
            TuSdkImageEngineImpl.a(TuSdkImageEngineImpl.this, localBitmap, TuSdkImageEngineImpl.1.this.b, localFilterWrap, TuSdkImageEngineImpl.1.this.c, TuSdkImageEngineImpl.1.this.d);
            localFilterWrap.destroy();
          }
        });
      }
    });
  }
  
  private void a(Bitmap paramBitmap, final FaceAligment[] paramArrayOfFaceAligment, final FilterWrap paramFilterWrap, InterfaceOrientation paramInterfaceOrientation, ImageOrientation paramImageOrientation)
  {
    if ((paramFilterWrap == null) || (paramBitmap == null) || (this.b == null))
    {
      a(null, null);
      return;
    }
    if (this.b.isOriginalCaptureOrientation())
    {
      localObject1 = TuSdkSize.create(paramBitmap.getWidth(), paramBitmap.getHeight());
      localObject2 = a(this.b.getInputRotation(), (TuSdkSize)localObject1);
      a(paramArrayOfFaceAligment, this.b.getOutputSize(), (TuSdkSize)localObject2);
    }
    Object localObject1 = new TuSDKFilterPicture(paramBitmap, false, true);
    Object localObject2 = paramImageOrientation == null ? ImageOrientation.Up : paramImageOrientation;
    boolean bool = false;
    if ((this.b.isOutputCaptureMirrorEnabled()) && (this.b.getCameraFacing() == CameraConfigs.CameraFacing.Front))
    {
      localObject2 = a((ImageOrientation)localObject2);
      bool = true;
    }
    ((TuSDKFilterPicture)localObject1).setOutputRotation((ImageOrientation)localObject2);
    a(paramArrayOfFaceAligment, paramInterfaceOrientation.getDegree(), bool);
    ((TuSDKFilterPicture)localObject1).mountAtGLThread(new Runnable()
    {
      public void run()
      {
        if ((paramFilterWrap instanceof SelesParameters.FilterFacePositionInterface)) {
          ((SelesParameters.FilterFacePositionInterface)paramFilterWrap).updateFaceFeatures(paramArrayOfFaceAligment, TuSdkImageEngineImpl.b(TuSdkImageEngineImpl.this).getDeviceAngle());
        }
      }
    });
    paramFilterWrap.addOrgin((SelesOutput)localObject1);
    ((TuSDKFilterPicture)localObject1).processImage();
    IntBuffer localIntBuffer = ((TuSDKFilterPicture)localObject1).bufferFromCurrentlyProcessedOutput();
    a(localIntBuffer, ((TuSDKFilterPicture)localObject1).outputImageSize());
  }
  
  private void a(final IntBuffer paramIntBuffer, final TuSdkSize paramTuSdkSize)
  {
    this.d = false;
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        if (TuSdkImageEngineImpl.c(TuSdkImageEngineImpl.this) != null) {
          TuSdkImageEngineImpl.c(TuSdkImageEngineImpl.this).onPictureDataCompleted(paramIntBuffer, paramTuSdkSize);
        }
      }
    });
  }
  
  public int getExifOrientation(InterfaceOrientation paramInterfaceOrientation)
  {
    if (paramInterfaceOrientation == InterfaceOrientation.Portrait) {
      return paramInterfaceOrientation.isTransposed() ? 2 : 1;
    }
    if (paramInterfaceOrientation == InterfaceOrientation.LandscapeLeft) {
      return paramInterfaceOrientation.isTransposed() ? 7 : 8;
    }
    if (paramInterfaceOrientation == InterfaceOrientation.LandscapeRight) {
      return paramInterfaceOrientation.isTransposed() ? 5 : 6;
    }
    return paramInterfaceOrientation.isTransposed() ? 4 : 3;
  }
  
  private void a(FaceAligment[] paramArrayOfFaceAligment, TuSdkSize paramTuSdkSize1, TuSdkSize paramTuSdkSize2)
  {
    if ((paramArrayOfFaceAligment == null) || (paramArrayOfFaceAligment.length == 0)) {
      return;
    }
    Rect localRect = RectHelper.makeRectWithAspectRatioInsideRect(paramTuSdkSize1, new Rect(0, 0, paramTuSdkSize2.width, paramTuSdkSize2.height));
    int i = 0;
    int j = paramArrayOfFaceAligment.length;
    while (i < j)
    {
      FaceAligment localFaceAligment = paramArrayOfFaceAligment[i];
      int k = 0;
      int m = localFaceAligment.getMarks().length;
      while (k < m)
      {
        PointF localPointF = localFaceAligment.getMarks()[k];
        localPointF.x = ((localPointF.x * localRect.width() + localRect.left) / paramTuSdkSize2.width);
        localPointF.y = ((localPointF.y * localRect.height() + localRect.top) / paramTuSdkSize2.height);
        k++;
      }
      i++;
    }
  }
  
  private void a(FaceAligment[] paramArrayOfFaceAligment, int paramInt, boolean paramBoolean)
  {
    if ((paramArrayOfFaceAligment == null) || (paramArrayOfFaceAligment.length == 0)) {
      return;
    }
    if ((paramInt <= 0) && (!paramBoolean)) {
      return;
    }
    PointF localPointF1 = new PointF(0.0F, 0.0F);
    int i = 0;
    int j = paramArrayOfFaceAligment.length;
    while (i < j)
    {
      FaceAligment localFaceAligment = paramArrayOfFaceAligment[i];
      int k = 0;
      int m = localFaceAligment.getMarks().length;
      while (k < m)
      {
        PointF localPointF2 = localFaceAligment.getMarks()[k];
        localPointF1.x = localPointF2.x;
        localPointF1.y = localPointF2.y;
        if (paramInt == 180)
        {
          localPointF2.x = (1.0F - localPointF1.x);
          localPointF2.y = (1.0F - localPointF1.y);
        }
        else if (paramInt == 90)
        {
          localPointF2.x = (1.0F - localPointF1.y);
          localPointF2.y = localPointF1.x;
        }
        else if (paramInt == 270)
        {
          localPointF2.x = localPointF1.y;
          localPointF2.y = (1.0F - localPointF1.x);
        }
        if (paramBoolean) {
          localPointF2.x = (1.0F - localPointF2.x);
        }
        k++;
      }
      i++;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\engine\TuSdkImageEngineImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */