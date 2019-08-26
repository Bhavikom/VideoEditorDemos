package org.lasque.tusdk.core.seles.sources;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.output.SelesViewInterface;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;

@TargetApi(21)
public class SelesVideoCamera2Processor
  extends SelesVideoCamera2Base
{
  private final SelesContext.SelesInput c;
  private final SelesViewInterface d;
  private FilterWrap e;
  private boolean f;
  private SelesVideoCamera2ProcessorEngine g;
  
  public final boolean isFilterChanging()
  {
    return this.f;
  }
  
  public void setCameraEngine(SelesVideoCamera2ProcessorEngine paramSelesVideoCamera2ProcessorEngine)
  {
    this.g = paramSelesVideoCamera2ProcessorEngine;
    super.setCameraEngine(paramSelesVideoCamera2ProcessorEngine);
  }
  
  @Deprecated
  public void setCameraEngine(SelesVideoCamera2Engine paramSelesVideoCamera2Engine) {}
  
  public <T extends SelesContext.SelesInput,  extends SelesViewInterface> SelesVideoCamera2Processor(Context paramContext, T paramT)
  {
    super(paramContext);
    if ((!b) && (paramT == null)) {
      throw new AssertionError();
    }
    ((SelesViewInterface)paramT).setRenderer(this);
    this.c = paramT;
    this.d = ((SelesViewInterface)paramT);
    this.e = FilterLocalPackage.shared().getFilterWrap(null);
    this.e.bindWithCameraView(this.c);
    this.e.addOrgin(this);
  }
  
  protected void onMainThreadStart()
  {
    super.onMainThreadStart();
    if (this.d != null) {
      this.d.requestRender();
    }
  }
  
  public void pauseCameraCapture()
  {
    super.pauseCameraCapture();
    a(false);
  }
  
  public void resumeCameraCapture()
  {
    super.resumeCameraCapture();
    a(true);
  }
  
  public void stopCameraCapture()
  {
    this.f = false;
    a(false);
    super.stopCameraCapture();
  }
  
  private void a(boolean paramBoolean)
  {
    if (this.d == null) {
      return;
    }
    if (paramBoolean) {
      this.d.setRenderModeContinuously();
    } else {
      this.d.setRenderModeDirty();
    }
  }
  
  public void setRendererFPS(int paramInt)
  {
    if ((paramInt < 1) || (this.d == null)) {
      return;
    }
    this.d.setRendererFPS(paramInt);
  }
  
  public void switchFilter(final String paramString)
  {
    if ((paramString == null) || (isFilterChanging()) || (!isCapturing()) || (this.e.equalsCode(paramString))) {
      return;
    }
    this.f = true;
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesVideoCamera2Processor.a(SelesVideoCamera2Processor.this, paramString);
      }
    });
  }
  
  private void a(String paramString)
  {
    FilterWrap localFilterWrap = FilterLocalPackage.shared().getFilterWrap(paramString);
    this.e.removeOrgin(this);
    localFilterWrap.bindWithCameraView(this.c);
    localFilterWrap.addOrgin(this);
    localFilterWrap.processImage();
    this.e.destroy();
    this.e = localFilterWrap;
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        SelesVideoCamera2Processor.a(SelesVideoCamera2Processor.this);
      }
    });
  }
  
  private void a()
  {
    if (this.g != null)
    {
      this.g.onFilterSwitched(this.e.getFilter());
      this.e.rotationTextures(this.g.deviceOrientation());
    }
    this.f = false;
  }
  
  public Bitmap processCaptureImage(Bitmap paramBitmap)
  {
    if ((paramBitmap == null) || (paramBitmap.isRecycled())) {
      return paramBitmap;
    }
    FilterWrap localFilterWrap = this.e.clone();
    localFilterWrap.processImage();
    SelesPicture localSelesPicture = new SelesPicture(paramBitmap, false);
    localFilterWrap.addOrgin(localSelesPicture);
    localSelesPicture.processImage();
    paramBitmap = localSelesPicture.imageFromCurrentlyProcessedOutput();
    return paramBitmap;
  }
  
  public static abstract interface SelesVideoCamera2ProcessorEngine
    extends SelesVideoCamera2Engine
  {
    public abstract InterfaceOrientation deviceOrientation();
    
    public abstract void onFilterSwitched(SelesOutInput paramSelesOutInput);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\sources\SelesVideoCamera2Processor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */