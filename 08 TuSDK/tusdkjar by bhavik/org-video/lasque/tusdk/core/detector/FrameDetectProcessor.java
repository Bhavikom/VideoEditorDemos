package org.lasque.tusdk.core.detector;

import android.graphics.PointF;
import android.graphics.RectF;
import java.nio.Buffer;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.face.TuSdkFaceDetector;
import org.lasque.tusdk.core.listener.TuSdkOrientationEventListener;
import org.lasque.tusdk.core.listener.TuSdkOrientationEventListener.TuSdkOrientationDelegate;
import org.lasque.tusdk.core.seles.output.SelesOffscreenRotate;
import org.lasque.tusdk.core.seles.output.SelesOffscreenRotate.SelesOffscreenRotateDelegate;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;

public class FrameDetectProcessor
{
  private InterfaceOrientation a = InterfaceOrientation.Portrait;
  private TuSdkOrientationEventListener b = new TuSdkOrientationEventListener(TuSdkContext.context());
  private SelesOffscreenRotate c;
  private boolean d = false;
  private TuSdkSize e;
  private boolean f;
  private boolean g;
  private FrameDetectProcessorDelegate h;
  private TuSdkOrientationEventListener.TuSdkOrientationDelegate i = new TuSdkOrientationEventListener.TuSdkOrientationDelegate()
  {
    public void onOrientationChanged(InterfaceOrientation paramAnonymousInterfaceOrientation)
    {
      if (FrameDetectProcessor.a(FrameDetectProcessor.this) != null) {
        FrameDetectProcessor.a(FrameDetectProcessor.this).onOrientationChanged(paramAnonymousInterfaceOrientation);
      }
    }
  };
  private SelesOffscreenRotate.SelesOffscreenRotateDelegate j = new SelesOffscreenRotate.SelesOffscreenRotateDelegate()
  {
    public boolean onFrameRendered(SelesOffscreenRotate paramAnonymousSelesOffscreenRotate)
    {
      if (!FrameDetectProcessor.b(FrameDetectProcessor.this)) {
        return true;
      }
      float f = paramAnonymousSelesOffscreenRotate.getAngle();
      int i = FrameDetectProcessor.c(FrameDetectProcessor.this);
      paramAnonymousSelesOffscreenRotate.setAngle(i);
      TuSdkSize localTuSdkSize = paramAnonymousSelesOffscreenRotate.outputFrameSize();
      Buffer localBuffer = paramAnonymousSelesOffscreenRotate.readBuffer();
      FrameDetectProcessor.a(FrameDetectProcessor.this, localTuSdkSize, f, localBuffer);
      return true;
    }
  };
  
  public FrameDetectProcessor()
  {
    this(0);
  }
  
  public FrameDetectProcessor(int paramInt)
  {
    this.b.setDelegate(this.i, null);
    this.b.enable();
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        TuSdkFaceDetector.init();
        FrameDetectProcessor.a(FrameDetectProcessor.this, true);
      }
    });
  }
  
  public FrameDetectProcessorDelegate getDelegate()
  {
    return this.h;
  }
  
  public void setDelegate(FrameDetectProcessorDelegate paramFrameDetectProcessorDelegate)
  {
    this.h = paramFrameDetectProcessorDelegate;
  }
  
  public boolean inited()
  {
    return this.g;
  }
  
  public static void setDetectScale(float paramFloat)
  {
    TuSdkFaceDetector.setDetectScale(paramFloat);
  }
  
  public void destroy()
  {
    this.h = null;
    if (this.b != null)
    {
      this.b.disable();
      this.b = null;
    }
    destroyOutput();
  }
  
  public void destroyOutput()
  {
    if (this.c == null) {
      return;
    }
    this.c.setDelegate(null);
    this.c.destroy();
    this.c = null;
  }
  
  public SelesOffscreenRotate getSelesRotateShotOutput()
  {
    if (this.c != null) {
      return this.c;
    }
    this.c = new SelesOffscreenRotate();
    this.c.setSyncOutput(this.d);
    a();
    this.c.setDelegate(this.j);
    return this.c;
  }
  
  public void setSyncOutput(boolean paramBoolean)
  {
    this.d = paramBoolean;
    if (this.c != null) {
      this.c.setSyncOutput(paramBoolean);
    }
  }
  
  public void setInputTextureSize(TuSdkSize paramTuSdkSize)
  {
    if ((paramTuSdkSize == null) || (paramTuSdkSize.equals(this.e))) {
      return;
    }
    this.e = paramTuSdkSize;
    a();
  }
  
  private void a()
  {
    if (this.c == null) {
      return;
    }
    int k = this.e == null ? 256 : this.e.maxSide();
    k = k > 256 ? 256 : k;
    this.c.forceProcessingAtSize(TuSdkSize.create(k, k));
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    this.f = paramBoolean;
    if (this.c != null) {
      this.c.setEnabled(paramBoolean);
    }
  }
  
  public void setInterfaceOrientation(InterfaceOrientation paramInterfaceOrientation)
  {
    this.a = paramInterfaceOrientation;
  }
  
  public int getDeviceAngle()
  {
    return this.b.getDeviceAngle();
  }
  
  private int b()
  {
    if (this.a == null) {
      return getDeviceAngle();
    }
    return getDeviceAngle() + this.a.getDegree();
  }
  
  private void a(TuSdkSize paramTuSdkSize, float paramFloat, Buffer paramBuffer)
  {
    if (paramBuffer == null) {
      return;
    }
    FaceAligment[] arrayOfFaceAligment = TuSdkFaceDetector.markFaceVideo(paramTuSdkSize.width, paramTuSdkSize.height, -Math.toRadians(paramFloat), paramBuffer);
    a(arrayOfFaceAligment, paramTuSdkSize, paramFloat, false);
  }
  
  private void a(FaceAligment[] paramArrayOfFaceAligment, TuSdkSize paramTuSdkSize, float paramFloat, boolean paramBoolean)
  {
    if ((this.h == null) || (this.e == null)) {
      return;
    }
    if ((paramArrayOfFaceAligment != null) && (paramArrayOfFaceAligment.length >= 1))
    {
      float f1 = this.e.getRatioFloat();
      TuSdkSize localTuSdkSize = TuSdkSize.create(paramTuSdkSize);
      if (f1 < 1.0F) {
        localTuSdkSize.width = ((int)(paramTuSdkSize.height * f1));
      } else {
        localTuSdkSize.height = ((int)(paramTuSdkSize.width / f1));
      }
      float f2 = (paramTuSdkSize.width - localTuSdkSize.width) * 0.5F;
      float f3 = (paramTuSdkSize.height - localTuSdkSize.height) * 0.5F;
      for (FaceAligment localFaceAligment : paramArrayOfFaceAligment)
      {
        localFaceAligment.rect.left = ((localFaceAligment.rect.left * paramTuSdkSize.width - f2) / localTuSdkSize.width);
        localFaceAligment.rect.top = ((localFaceAligment.rect.top * paramTuSdkSize.height - f3) / localTuSdkSize.height);
        localFaceAligment.rect.right = ((localFaceAligment.rect.right * paramTuSdkSize.width - f2) / localTuSdkSize.width);
        localFaceAligment.rect.bottom = ((localFaceAligment.rect.bottom * paramTuSdkSize.height - f3) / localTuSdkSize.height);
        if (localFaceAligment.getOrginMarks() != null)
        {
          for (PointF localPointF : localFaceAligment.getOrginMarks()) {
            if (f1 < 1.0F) {
              localPointF.x = ((localPointF.x * paramTuSdkSize.width - f2) / localTuSdkSize.width);
            } else {
              localPointF.y = ((localPointF.y * paramTuSdkSize.height - f3) / localTuSdkSize.height);
            }
          }
          localFaceAligment.setOrginMarks(localFaceAligment.getOrginMarks());
        }
      }
    }
    if (getDelegate() != null) {
      getDelegate().onFrameDetectedResult(paramArrayOfFaceAligment, paramTuSdkSize, paramFloat, paramBoolean);
    }
  }
  
  public FaceAligment[] syncProcessFrameData(byte[] paramArrayOfByte, TuSdkSize paramTuSdkSize, int paramInt, double paramDouble, boolean paramBoolean)
  {
    if (!inited()) {
      return null;
    }
    return TuSdkFaceDetector.markFaceGrayImage(paramTuSdkSize.width, paramTuSdkSize.height, paramInt, paramDouble, paramBoolean, paramArrayOfByte);
  }
  
  public static abstract interface FrameDetectProcessorDelegate
    extends TuSdkOrientationEventListener.TuSdkOrientationDelegate
  {
    public abstract void onFrameDetectedResult(FaceAligment[] paramArrayOfFaceAligment, TuSdkSize paramTuSdkSize, float paramFloat, boolean paramBoolean);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\detector\FrameDetectProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */