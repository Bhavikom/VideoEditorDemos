package org.lasque.tusdk.core.utils.hardware;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView.Renderer;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import java.util.Calendar;
import java.util.List;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.exif.ExifInterface;
import org.lasque.tusdk.core.listener.TuSdkOrientationEventListener;
import org.lasque.tusdk.core.listener.TuSdkOrientationEventListener.TuSdkOrientationDelegate;
import org.lasque.tusdk.core.seles.output.SelesSmartView;
import org.lasque.tusdk.core.seles.sources.SelesBaseCameraInterface;
import org.lasque.tusdk.core.seles.sources.SelesBaseCameraInterface.TuSdkAutoFocusCallback;
import org.lasque.tusdk.core.seles.sources.SelesOutput;
import org.lasque.tusdk.core.seles.sources.SelesStillCameraInterface;
import org.lasque.tusdk.core.seles.sources.SelesStillCameraInterface.CapturePhotoAsBitmapCallback;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.ViewSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.image.ExifHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.impl.view.widget.RegionDefaultHandler;
import org.lasque.tusdk.impl.view.widget.RegionHandler;
import org.lasque.tusdk.impl.view.widget.RegionHandler.RegionChangerListener;

public class TuSdkStillCameraAdapter<T extends SelesOutput,  extends GLSurfaceView.Renderer,  extends TuSdkStillCameraInterface>
{
  public static final long FocusSamplingDistance = 2000L;
  private final Context a;
  private final RelativeLayout b;
  private final T c;
  private CameraState d = CameraState.StateUnknow;
  private boolean e;
  private TuSdkStillCameraInterface.TuSdkStillCameraListener f;
  private InterfaceOrientation g = InterfaceOrientation.Portrait;
  private TuSdkOrientationEventListener h;
  private SelesSmartView i;
  private FilterWrap j;
  private RegionHandler k;
  private boolean l;
  private TuSdkVideoCameraExtendViewInterface m;
  private boolean n;
  private int o;
  private MediaPlayer p;
  private boolean q;
  private boolean r;
  private boolean s;
  private int t = -16777216;
  private float u = 0.0F;
  private boolean v = false;
  private boolean w;
  private TuSdkOrientationEventListener.TuSdkOrientationDelegate x = new TuSdkOrientationEventListener.TuSdkOrientationDelegate()
  {
    public void onOrientationChanged(InterfaceOrientation paramAnonymousInterfaceOrientation)
    {
      TuSdkStillCameraAdapter.a(TuSdkStillCameraAdapter.this, paramAnonymousInterfaceOrientation);
      TuSdkStillCameraAdapter.b(TuSdkStillCameraAdapter.this);
    }
  };
  private TuSdkResult y;
  
  public InterfaceOrientation getDeviceOrient()
  {
    return this.g;
  }
  
  public int getDeviceAngle()
  {
    return this.h.getDeviceAngle();
  }
  
  public CameraState getState()
  {
    return this.d;
  }
  
  private void a(final CameraState paramCameraState)
  {
    this.d = paramCameraState;
    if (!ThreadHelper.isMainThread())
    {
      ThreadHelper.post(new Runnable()
      {
        public void run()
        {
          TuSdkStillCameraAdapter.a(TuSdkStillCameraAdapter.this, paramCameraState);
        }
      });
      return;
    }
    if (this.f != null) {
      this.f.onStillCameraStateChanged((TuSdkStillCameraInterface)this.c, this.d);
    }
    TuSdkVideoCameraExtendViewInterface localTuSdkVideoCameraExtendViewInterface = getFocusTouchView();
    if (localTuSdkVideoCameraExtendViewInterface != null) {
      localTuSdkVideoCameraExtendViewInterface.cameraStateChanged((TuSdkStillCameraInterface)this.c, this.d);
    }
  }
  
  public RegionHandler getRegionHandler()
  {
    if (this.k == null) {
      this.k = new RegionDefaultHandler();
    }
    this.k.setWrapSize(ViewSize.create(this.b));
    return this.k;
  }
  
  public void setRegionHandler(RegionHandler paramRegionHandler)
  {
    this.k = paramRegionHandler;
  }
  
  public final boolean isFilterChanging()
  {
    return this.e;
  }
  
  public TuSdkStillCameraInterface.TuSdkStillCameraListener getCameraListener()
  {
    return this.f;
  }
  
  public void setCameraListener(TuSdkStillCameraInterface.TuSdkStillCameraListener paramTuSdkStillCameraListener)
  {
    this.f = paramTuSdkStillCameraListener;
  }
  
  public TuSdkVideoCameraExtendViewInterface getFocusTouchView()
  {
    return this.m;
  }
  
  public void setFocusTouchView(TuSdkVideoCameraExtendViewInterface paramTuSdkVideoCameraExtendViewInterface)
  {
    if ((paramTuSdkVideoCameraExtendViewInterface == null) || (this.i == null)) {
      return;
    }
    if (this.m != null)
    {
      this.i.removeView((View)this.m);
      this.m.viewWillDestory();
    }
    getRegionHandler().setRatio(getRegionRatio());
    this.i.setBackgroundColor(getRegionViewColor());
    this.i.setDisplayRect(getRegionHandler().getRectPercent());
    this.m = paramTuSdkVideoCameraExtendViewInterface;
    this.m.setCamera((TuSdkStillCameraInterface)this.c);
    this.m.setEnableLongTouchCapture(isEnableLongTouchCapture());
    this.m.setDisableFocusBeep(isDisableFocusBeep());
    this.m.setDisableContinueFoucs(isDisableContinueFoucs());
    this.m.setGuideLineViewState(isDisplayGuideLine());
    this.m.setEnableFilterConfig(isEnableFilterConfig());
    this.m.setRegionPercent(getRegionHandler().getRectPercent());
    this.i.addView((View)this.m, new RelativeLayout.LayoutParams(-1, -1));
  }
  
  public void setFocusTouchView(int paramInt)
  {
    if (paramInt == 0) {
      return;
    }
    View localView = TuSdkViewHelper.buildView(this.a, paramInt, this.i);
    if ((localView == null) || (!(localView instanceof TuSdkVideoCameraExtendViewInterface)))
    {
      TLog.w("The setFocusTouchView must extend TuFocusTouchView: %s", new Object[] { localView });
      return;
    }
    setFocusTouchView((TuSdkVideoCameraExtendViewInterface)localView);
  }
  
  public boolean isOutputImageData()
  {
    return this.l;
  }
  
  public void setOutputImageData(boolean paramBoolean)
  {
    this.l = paramBoolean;
  }
  
  public boolean isEnableLongTouchCapture()
  {
    return this.q;
  }
  
  public void setEnableLongTouchCapture(boolean paramBoolean)
  {
    this.q = paramBoolean;
    if (getFocusTouchView() != null) {
      getFocusTouchView().setEnableLongTouchCapture(this.q);
    }
  }
  
  public boolean isDisableCaptureSound()
  {
    return this.n;
  }
  
  public void setDisableCaptureSound(boolean paramBoolean)
  {
    this.n = paramBoolean;
  }
  
  public boolean isDisableFocusBeep()
  {
    return this.r;
  }
  
  public void setDisableFocusBeep(boolean paramBoolean)
  {
    this.r = paramBoolean;
    if (getFocusTouchView() != null) {
      getFocusTouchView().setDisableFocusBeep(this.r);
    }
  }
  
  public boolean isDisableContinueFoucs()
  {
    return this.s;
  }
  
  public void setDisableContinueFoucs(boolean paramBoolean)
  {
    this.s = paramBoolean;
    if (getFocusTouchView() != null) {
      getFocusTouchView().setDisableContinueFoucs(this.s);
    }
  }
  
  public int getRegionViewColor()
  {
    return this.t;
  }
  
  public void setRegionViewColor(int paramInt)
  {
    this.t = paramInt;
    if (this.i != null) {
      this.i.setBackgroundColor(this.t);
    }
  }
  
  public float getRegionRatio()
  {
    if (this.u < 0.0F) {
      this.u = 0.0F;
    }
    return this.u;
  }
  
  public void setRegionRatio(float paramFloat)
  {
    this.u = paramFloat;
    if (getRegionHandler() != null)
    {
      this.k.setRatio(this.u);
      if (this.i != null) {
        this.i.setDisplayRect(this.k.getRectPercent());
      }
    }
  }
  
  public boolean isDisplayGuideLine()
  {
    return this.v;
  }
  
  public void setDisplayGuideLine(boolean paramBoolean)
  {
    this.v = paramBoolean;
    if (getFocusTouchView() != null) {
      getFocusTouchView().setGuideLineViewState(paramBoolean);
    }
  }
  
  public void changeRegionRatio(float paramFloat)
  {
    this.u = paramFloat;
    getRegionHandler().changeWithRatio(this.u, new RegionHandler.RegionChangerListener()
    {
      public void onRegionChanged(RectF paramAnonymousRectF)
      {
        if (TuSdkStillCameraAdapter.a(TuSdkStillCameraAdapter.this) != null) {
          TuSdkStillCameraAdapter.a(TuSdkStillCameraAdapter.this).setDisplayRect(paramAnonymousRectF);
        }
        if (TuSdkStillCameraAdapter.this.getFocusTouchView() != null) {
          TuSdkStillCameraAdapter.this.getFocusTouchView().setRegionPercent(paramAnonymousRectF);
        }
      }
    });
  }
  
  public boolean isEnableFilterConfig()
  {
    return this.w;
  }
  
  public void setEnableFilterConfig(boolean paramBoolean)
  {
    this.w = paramBoolean;
    if (getFocusTouchView() != null) {
      getFocusTouchView().setEnableFilterConfig(this.w);
    }
  }
  
  public int getCaptureSoundRawId()
  {
    return this.o;
  }
  
  public void setCaptureSoundRawId(int paramInt)
  {
    this.o = paramInt;
    if (this.p != null)
    {
      this.p.release();
      this.p = null;
    }
    if (this.o != 0)
    {
      setDisableCaptureSound(true);
      this.p = MediaPlayer.create(this.a, this.o);
    }
  }
  
  public TuSdkStillCameraAdapter(Context paramContext, RelativeLayout paramRelativeLayout, T paramT)
  {
    this.a = paramContext;
    this.b = paramRelativeLayout;
    this.c = paramT;
    a();
  }
  
  private void a()
  {
    ViewSize localViewSize = ViewSize.create(this.b);
    if ((localViewSize != null) && (localViewSize.isSize())) {
      ((SelesBaseCameraInterface)this.c).setPreviewMaxSize(localViewSize.maxSide());
    }
    this.d = CameraState.StateUnknow;
    this.h = new TuSdkOrientationEventListener(this.a);
    this.h.setDelegate(this.x, null);
    this.i = b();
    this.j = FilterLocalPackage.shared().getFilterWrap(null);
    this.j.bindWithCameraView(this.i);
    this.c.addTarget(this.j.getFilter(), 0);
  }
  
  public void onDestroy()
  {
    if (this.p != null)
    {
      this.p.release();
      this.p = null;
    }
    if (this.j != null)
    {
      this.j.destroy();
      this.j = null;
    }
  }
  
  private SelesSmartView b()
  {
    if (this.b == null)
    {
      TLog.e("Can not find cameraView", new Object[0]);
      return this.i;
    }
    if (this.i == null)
    {
      this.i = new SelesSmartView(this.a);
      this.i.setRenderer((GLSurfaceView.Renderer)this.c);
      RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
      this.b.addView(this.i, 0, localLayoutParams);
    }
    return this.i;
  }
  
  private void c()
  {
    if (this.j == null) {
      return;
    }
    this.j.rotationTextures(this.g);
  }
  
  private void d()
  {
    if ((this.j == null) || (getFocusTouchView() == null)) {
      return;
    }
    getFocusTouchView().notifyFilterConfigView(this.j.getFilter());
  }
  
  public void onMainThreadStart()
  {
    if (this.i != null) {
      this.i.requestRender();
    }
  }
  
  public void pauseCameraCapture()
  {
    a(false);
  }
  
  public void resumeCameraCapture()
  {
    a(true);
    if (((SelesBaseCameraInterface)this.c).isCapturing()) {
      this.d = CameraState.StateStarted;
    }
  }
  
  public void stopCameraCapture()
  {
    this.e = false;
    a(false);
    a(CameraState.StateUnknow);
    if (getFocusTouchView() != null) {
      getFocusTouchView().notifyFilterConfigView(null);
    }
    this.h.disable();
  }
  
  private void a(boolean paramBoolean)
  {
    if (this.i == null) {
      return;
    }
    if (paramBoolean) {
      this.i.setRenderModeContinuously();
    } else {
      this.i.setRenderModeDirty();
    }
  }
  
  public void setRendererFPS(int paramInt)
  {
    if ((paramInt < 1) || (this.i == null)) {
      return;
    }
    this.i.setRendererFPS(paramInt);
  }
  
  protected void onCameraStarted()
  {
    this.h.enable();
    a(CameraState.StateStarted);
    d();
    if (this.i != null) {
      resumeCameraCapture();
    }
  }
  
  public void onCameraFaceDetection(List<TuSdkFace> paramList, TuSdkSize paramTuSdkSize)
  {
    TuSdkVideoCameraExtendViewInterface localTuSdkVideoCameraExtendViewInterface = getFocusTouchView();
    if (localTuSdkVideoCameraExtendViewInterface != null) {
      localTuSdkVideoCameraExtendViewInterface.setCameraFaceDetection(paramList, paramTuSdkSize);
    }
  }
  
  public FilterWrap getFilterWrap()
  {
    return this.j;
  }
  
  public Runnable switchFilter(final String paramString)
  {
    if ((paramString == null) || (this.e) || (getState() == CameraState.StateCapturing) || (this.j.equalsCode(paramString))) {
      return null;
    }
    this.e = true;
    new Runnable()
    {
      public void run()
      {
        FilterWrap localFilterWrap = FilterLocalPackage.shared().getFilterWrap(paramString);
        TuSdkStillCameraAdapter.d(TuSdkStillCameraAdapter.this).removeTarget(TuSdkStillCameraAdapter.c(TuSdkStillCameraAdapter.this).getFilter());
        localFilterWrap.bindWithCameraView(TuSdkStillCameraAdapter.a(TuSdkStillCameraAdapter.this));
        TuSdkStillCameraAdapter.d(TuSdkStillCameraAdapter.this).addTarget(localFilterWrap.getFilter(), 0);
        localFilterWrap.processImage();
        TuSdkStillCameraAdapter.c(TuSdkStillCameraAdapter.this).destroy();
        TuSdkStillCameraAdapter.a(TuSdkStillCameraAdapter.this, localFilterWrap);
        ThreadHelper.post(new Runnable()
        {
          public void run()
          {
            TuSdkStillCameraAdapter.e(TuSdkStillCameraAdapter.this);
          }
        });
      }
    };
  }
  
  private void e()
  {
    d();
    if ((this.f != null) && (this.j != null)) {
      this.f.onFilterChanged(this.j.getFilter());
    }
    c();
    this.e = false;
  }
  
  public boolean isCreatedSurface()
  {
    return (this.i != null) && (this.i.isCreatedSurface());
  }
  
  public void captureImage()
  {
    if (this.d != CameraState.StateStarted) {
      return;
    }
    a(CameraState.StateCapturing);
    if ((f()) && (((SelesBaseCameraInterface)this.c).canSupportAutoFocus()))
    {
      g();
      ((SelesBaseCameraInterface)this.c).autoFocus(new SelesBaseCameraInterface.TuSdkAutoFocusCallback()
      {
        public void onAutoFocus(boolean paramAnonymousBoolean, SelesBaseCameraInterface paramAnonymousSelesBaseCameraInterface)
        {
          TuSdkStillCameraAdapter.a(TuSdkStillCameraAdapter.this, paramAnonymousBoolean);
          TuSdkStillCameraAdapter.b(TuSdkStillCameraAdapter.this, paramAnonymousBoolean);
        }
      });
    }
    else
    {
      c(false);
    }
  }
  
  private boolean f()
  {
    return Calendar.getInstance().getTimeInMillis() - ((SelesBaseCameraInterface)this.c).getLastFocusTime() > 2000L;
  }
  
  private void g()
  {
    TuSdkVideoCameraExtendViewInterface localTuSdkVideoCameraExtendViewInterface = getFocusTouchView();
    if (localTuSdkVideoCameraExtendViewInterface != null) {
      localTuSdkVideoCameraExtendViewInterface.showRangeView();
    }
  }
  
  private void b(boolean paramBoolean)
  {
    TuSdkVideoCameraExtendViewInterface localTuSdkVideoCameraExtendViewInterface = getFocusTouchView();
    if (localTuSdkVideoCameraExtendViewInterface != null) {
      localTuSdkVideoCameraExtendViewInterface.setRangeViewFoucsState(paramBoolean);
    }
  }
  
  private void h()
  {
    a(CameraState.StateCaptured);
    if ((this.p != null) && (isDisableCaptureSound())) {
      this.p.start();
    }
  }
  
  public void onTakePictured(byte[] paramArrayOfByte)
  {
    h();
    if ((isOutputImageData()) && (this.y != null))
    {
      this.y.imageData = paramArrayOfByte;
      a(this.y);
    }
  }
  
  protected Bitmap decodeToBitmapAtAsync(byte[] paramArrayOfByte, Bitmap paramBitmap)
  {
    if (this.y != null) {
      a(paramArrayOfByte);
    }
    return paramBitmap;
  }
  
  private void a(byte[] paramArrayOfByte)
  {
    if ((this.y == null) || (paramArrayOfByte == null)) {
      return;
    }
    this.y.metadata = ExifHelper.getExifInterface(paramArrayOfByte);
  }
  
  private void a(Bitmap paramBitmap)
  {
    if ((this.y == null) || (paramBitmap == null)) {
      return;
    }
    this.y.image = paramBitmap;
    this.y.outputSize = TuSdkSize.create(paramBitmap);
    if (this.y.metadata == null) {
      return;
    }
    this.y.metadata.setTagValue(ExifInterface.TAG_IMAGE_WIDTH, Integer.valueOf(this.y.outputSize.width));
    this.y.metadata.setTagValue(ExifInterface.TAG_IMAGE_LENGTH, Integer.valueOf(this.y.outputSize.height));
    this.y.metadata.setTagValue(ExifInterface.TAG_ORIENTATION, Integer.valueOf(ImageOrientation.Up.getExifOrientation()));
    String str = String.format("TuSDK[filter:%s]", new Object[] { this.y.filterCode });
    this.y.metadata.setTag(this.y.metadata.buildTag(ExifInterface.TAG_IMAGE_DESCRIPTION, str));
  }
  
  private void c(boolean paramBoolean)
  {
    this.y = new TuSdkResult();
    this.y.imageOrientation = ((SelesBaseCameraInterface)this.c).capturePhotoOrientation();
    this.y.outputSize = ((SelesBaseCameraInterface)this.c).getOutputSize();
    this.y.imageSizeRatio = getRegionRatio();
    this.y.filterCode = this.j.getCode();
    this.y.filterParams = this.j.getFilterParameter();
    if (isOutputImageData())
    {
      ((SelesStillCameraInterface)this.c).capturePhotoAsJPEGData(null);
      return;
    }
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        final FilterWrap localFilterWrap = TuSdkStillCameraAdapter.c(TuSdkStillCameraAdapter.this).clone();
        localFilterWrap.processImage();
        ((SelesStillCameraInterface)TuSdkStillCameraAdapter.d(TuSdkStillCameraAdapter.this)).capturePhotoAsBitmap(localFilterWrap.getFilter(), TuSdkStillCameraAdapter.f(TuSdkStillCameraAdapter.this).imageOrientation, new SelesStillCameraInterface.CapturePhotoAsBitmapCallback()
        {
          public void onCapturePhotoAsBitmap(Bitmap paramAnonymous2Bitmap)
          {
            if (TuSdkStillCameraAdapter.f(TuSdkStillCameraAdapter.this) != null)
            {
              TuSdkStillCameraAdapter.a(TuSdkStillCameraAdapter.this, paramAnonymous2Bitmap);
              localFilterWrap.destroy();
              TuSdkStillCameraAdapter.a(TuSdkStillCameraAdapter.this, TuSdkStillCameraAdapter.f(TuSdkStillCameraAdapter.this));
            }
          }
        });
      }
    });
  }
  
  private void a(TuSdkResult paramTuSdkResult)
  {
    this.y = null;
    if (this.f != null) {
      this.f.onStillCameraTakedPicture((TuSdkStillCameraInterface)this.c, paramTuSdkResult);
    }
  }
  
  public static enum CameraState
  {
    private CameraState() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\hardware\TuSdkStillCameraAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */