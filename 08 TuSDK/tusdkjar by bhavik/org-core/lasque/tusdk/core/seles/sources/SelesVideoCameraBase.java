package org.lasque.tusdk.core.seles.sources;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Build.VERSION;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.secret.ColorSpaceConvert;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.TuSdkDate;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public abstract class SelesVideoCameraBase
  extends SelesOutput
  implements SurfaceTexture.OnFrameAvailableListener, Camera.PreviewCallback, GLSurfaceView.Renderer
{
  public static final String SELES_PASSTHROUGH_FRAGMENT_SHADER_OES = "#extension GL_OES_EGL_image_external : require\nvarying highp vec2 textureCoordinate;uniform samplerExternalOES inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}";
  private static final float[] b = { -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F };
  private static final float[] c = { 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
  private FloatBuffer d;
  private FloatBuffer e;
  private SelesGLProgram f;
  private int g;
  private int h;
  private int i;
  private int j;
  private boolean k;
  private boolean l;
  private Context m;
  private EGLContext n;
  private boolean o;
  protected ImageOrientation mOutputRotation;
  private boolean p;
  private IntBuffer q;
  private int r;
  private final Queue<Runnable> s;
  private final Queue<Runnable> t;
  private boolean u;
  private boolean v;
  private long w;
  private InterfaceOrientation x = InterfaceOrientation.Portrait;
  private boolean y;
  private boolean z;
  private SurfaceTexture A;
  private boolean B;
  private TuSdkDate C;
  private long D;
  private long E;
  private boolean F = false;
  private SelesVideoCameraEngine G;
  private Camera H;
  private boolean I = true;
  
  public Context getContext()
  {
    return this.m;
  }
  
  public InterfaceOrientation getOutputImageOrientation()
  {
    return this.x;
  }
  
  public void setOutputImageOrientation(InterfaceOrientation paramInterfaceOrientation)
  {
    if (paramInterfaceOrientation == null) {
      return;
    }
    this.x = paramInterfaceOrientation;
    this.u = true;
  }
  
  public boolean isHorizontallyMirrorFrontFacingCamera()
  {
    return this.y;
  }
  
  public void setHorizontallyMirrorFrontFacingCamera(boolean paramBoolean)
  {
    this.y = paramBoolean;
    this.u = true;
  }
  
  public boolean isHorizontallyMirrorRearFacingCamera()
  {
    return this.z;
  }
  
  public void setHorizontallyMirrorRearFacingCamera(boolean paramBoolean)
  {
    this.z = paramBoolean;
    this.u = true;
  }
  
  public boolean isCapturing()
  {
    return this.p;
  }
  
  public boolean isCapturePaused()
  {
    return this.o;
  }
  
  public boolean hasCreateSurface()
  {
    return this.A != null;
  }
  
  public boolean getRunBenchmark()
  {
    return this.F;
  }
  
  public void setRunBenchmark(boolean paramBoolean)
  {
    this.F = paramBoolean;
  }
  
  public void setCameraEngine(SelesVideoCameraEngine paramSelesVideoCameraEngine)
  {
    if ((!a) && (paramSelesVideoCameraEngine == null)) {
      throw new AssertionError();
    }
    this.G = paramSelesVideoCameraEngine;
  }
  
  public Camera inputCamera()
  {
    return this.H;
  }
  
  public SelesVideoCameraBase(Context paramContext)
  {
    TLog.i("Used Camera 1 Api", new Object[0]);
    this.m = paramContext;
    this.s = new LinkedList();
    this.t = new LinkedList();
    this.mOutputRotation = ImageOrientation.Up;
    setOutputImageOrientation(InterfaceOrientation.Portrait);
    this.k = ((Build.VERSION.SDK_INT > 14) && (SelesContext.isSupportOESImageExternal()));
    a();
    m();
  }
  
  protected boolean getEnableFixedFramerate()
  {
    return this.I;
  }
  
  protected void onDestroy()
  {
    stopCameraCapture();
    f();
  }
  
  public float averageFrameDurationDuringCapture()
  {
    return (float)this.D / (float)(this.E - 1L);
  }
  
  public void resetBenchmarkAverage()
  {
    this.E = 0L;
    this.D = 0L;
  }
  
  private void a()
  {
    if ((!this.k) || (this.f != null)) {
      return;
    }
    this.d = SelesFilter.buildBuffer(b);
    this.e = SelesFilter.buildBuffer(c);
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesVideoCameraBase.a(SelesVideoCameraBase.this, SelesContext.program("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", "#extension GL_OES_EGL_image_external : require\nvarying highp vec2 textureCoordinate;uniform samplerExternalOES inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}"));
        if (!SelesVideoCameraBase.a(SelesVideoCameraBase.this).isInitialized())
        {
          SelesVideoCameraBase.a(SelesVideoCameraBase.this).addAttribute("position");
          SelesVideoCameraBase.a(SelesVideoCameraBase.this).addAttribute("inputTextureCoordinate");
          if (!SelesVideoCameraBase.a(SelesVideoCameraBase.this).link())
          {
            TLog.i("Program link log: %s", new Object[] { SelesVideoCameraBase.a(SelesVideoCameraBase.this).getProgramLog() });
            TLog.i("Fragment shader compile log: %s", new Object[] { SelesVideoCameraBase.a(SelesVideoCameraBase.this).getFragmentShaderLog() });
            TLog.i("Vertex link log: %s", new Object[] { SelesVideoCameraBase.a(SelesVideoCameraBase.this).getVertexShaderLog() });
            SelesVideoCameraBase.a(SelesVideoCameraBase.this, null);
            TLog.e("Filter shader link failed: %s", new Object[] { getClass() });
            return;
          }
        }
        SelesVideoCameraBase.a(SelesVideoCameraBase.this, SelesVideoCameraBase.a(SelesVideoCameraBase.this).attributeIndex("position"));
        SelesVideoCameraBase.b(SelesVideoCameraBase.this, SelesVideoCameraBase.a(SelesVideoCameraBase.this).attributeIndex("inputTextureCoordinate"));
        SelesVideoCameraBase.c(SelesVideoCameraBase.this, SelesVideoCameraBase.a(SelesVideoCameraBase.this).uniformIndex("inputImageTexture"));
        SelesContext.setActiveShaderProgram(SelesVideoCameraBase.a(SelesVideoCameraBase.this));
        GLES20.glEnableVertexAttribArray(SelesVideoCameraBase.b(SelesVideoCameraBase.this));
        GLES20.glEnableVertexAttribArray(SelesVideoCameraBase.c(SelesVideoCameraBase.this));
      }
    });
  }
  
  public void startCameraCapture()
  {
    if (ThreadHelper.isMainThread())
    {
      onMainThreadStart();
      return;
    }
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        SelesVideoCameraBase.this.startCameraCapture();
      }
    });
  }
  
  public void stopCameraCapture()
  {
    this.p = false;
    this.o = false;
    this.w = 0L;
    this.B = false;
    if (this.A != null)
    {
      this.A.setOnFrameAvailableListener(null);
      if (Build.VERSION.SDK_INT >= 14) {
        b();
      }
      this.A = null;
    }
    if (this.H != null) {
      try
      {
        this.H.setPreviewCallback(null);
        this.H.cancelAutoFocus();
        this.H.stopPreview();
        this.H.release();
      }
      catch (Exception localException)
      {
        TLog.e(localException, "SelesVideoCamera stopCameraCapture", new Object[0]);
      }
      finally
      {
        this.H = null;
      }
    }
    resetBenchmarkAverage();
  }
  
  protected void startPreviewCallback()
  {
    if (this.l) {
      return;
    }
    this.l = true;
  }
  
  protected void stopPreviewCallback()
  {
    if ((!this.l) || (this.H == null)) {
      return;
    }
    this.H.setPreviewCallback(null);
    if (this.q != null)
    {
      this.q.clear();
      this.q = null;
    }
    this.l = false;
  }
  
  @TargetApi(14)
  private void b()
  {
    if (this.A != null) {
      this.A.release();
    }
  }
  
  public SurfaceTexture getSurfaceTexture()
  {
    return this.A;
  }
  
  private void c()
  {
    if ((this.A != null) && (this.B)) {
      this.A.updateTexImage();
    }
  }
  
  protected void onMainThreadStart()
  {
    if (this.G == null)
    {
      TLog.d("You need setCameraEngine(SelesVideoCameraEngine engine)", new Object[0]);
      return;
    }
    stopCameraCapture();
    if (!this.G.canInitCamera()) {
      return;
    }
    d();
  }
  
  private void d()
  {
    this.H = this.G.onInitCamera();
    if (this.H == null) {
      return;
    }
    TuSdkSize localTuSdkSize = this.G.previewOptimalSize();
    if (localTuSdkSize != null) {
      this.mInputTextureSize = localTuSdkSize;
    }
    TLog.d("mInputTextureSize: %s", new Object[] { this.mInputTextureSize });
    this.u = true;
    if ((!this.k) || (this.l))
    {
      int i1 = this.mInputTextureSize.width * this.mInputTextureSize.height;
      int i2 = ImageFormat.getBitsPerPixel(this.H.getParameters().getPreviewFormat());
      this.r = (i1 * i2 / 8);
      this.q = IntBuffer.allocate(i1);
    }
    runOnDraw(new Runnable()
    {
      public void run()
      {
        if (SelesVideoCameraBase.d(SelesVideoCameraBase.this))
        {
          SelesVideoCameraBase.d(SelesVideoCameraBase.this, SelesVideoCameraBase.e(SelesVideoCameraBase.this));
          if (SelesVideoCameraBase.f(SelesVideoCameraBase.this))
          {
            SelesVideoCameraBase.g(SelesVideoCameraBase.this);
            SelesVideoCameraBase.h(SelesVideoCameraBase.this).setPreviewCallbackWithBuffer(SelesVideoCameraBase.this);
          }
        }
        else
        {
          int[] arrayOfInt = new int[1];
          GLES20.glGenTextures(1, arrayOfInt, 0);
          SelesVideoCameraBase.d(SelesVideoCameraBase.this, arrayOfInt[0]);
          SelesVideoCameraBase.g(SelesVideoCameraBase.this);
          SelesVideoCameraBase.h(SelesVideoCameraBase.this).setPreviewCallbackWithBuffer(SelesVideoCameraBase.this);
        }
        SelesVideoCameraBase.a(SelesVideoCameraBase.this, true);
        SelesVideoCameraBase.a(SelesVideoCameraBase.this, new SurfaceTexture(SelesVideoCameraBase.i(SelesVideoCameraBase.this)));
        SelesVideoCameraBase.b(SelesVideoCameraBase.this, true);
        SelesVideoCameraBase.j(SelesVideoCameraBase.this).setOnFrameAvailableListener(SelesVideoCameraBase.this);
        SelesVideoCameraBase.k(SelesVideoCameraBase.this).onCameraWillOpen(SelesVideoCameraBase.j(SelesVideoCameraBase.this));
        SelesVideoCameraBase.l(SelesVideoCameraBase.this);
      }
    });
  }
  
  private SelesFramebuffer e()
  {
    f();
    if (this.k)
    {
      localObject = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, this.mInputTextureSize);
      ((SelesFramebuffer)localObject).disableReferenceCounting();
      this.n = SelesContext.currentEGLContext();
      return (SelesFramebuffer)localObject;
    }
    Object localObject = new int[1];
    GLES20.glGenTextures(1, (int[])localObject, 0);
    GLES20.glBindTexture(3553, localObject[0]);
    GLES20.glTexParameterf(3553, 10240, 9729.0F);
    GLES20.glTexParameterf(3553, 10241, 9729.0F);
    GLES20.glTexParameterf(3553, 10242, 33071.0F);
    GLES20.glTexParameterf(3553, 10243, 33071.0F);
    GLES20.glTexImage2D(3553, 0, 6408, this.mInputTextureSize.width, this.mInputTextureSize.height, 0, 6408, 5121, null);
    return SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.PACKAGE, this.mInputTextureSize, localObject[0]);
  }
  
  private void f()
  {
    if (this.mOutputFramebuffer == null) {
      return;
    }
    this.mOutputFramebuffer.clearAllLocks();
    SelesContext.returnFramebufferToCache(this.mOutputFramebuffer);
    this.mOutputFramebuffer = null;
  }
  
  @TargetApi(15)
  private int g()
  {
    int[] arrayOfInt = new int[1];
    GLES20.glGenTextures(1, arrayOfInt, 0);
    GLES20.glBindTexture(36197, arrayOfInt[0]);
    GLES20.glTexParameteri(36197, 10241, 9729);
    GLES20.glTexParameteri(36197, 10240, 9729);
    GLES20.glTexParameteri(36197, 10242, 33071);
    GLES20.glTexParameteri(36197, 10243, 33071);
    this.j = arrayOfInt[0];
    return this.j;
  }
  
  private void h()
  {
    if (this.H == null) {
      return;
    }
    try
    {
      this.H.startPreview();
      onPreviewStarted();
    }
    catch (Exception localException)
    {
      TLog.e(localException, "startPreview", new Object[0]);
    }
  }
  
  protected void onPreviewStarted() {}
  
  protected void onCameraStarted()
  {
    if (this.G != null) {
      this.G.onCameraStarted();
    }
  }
  
  public void pauseCameraCapture()
  {
    this.o = true;
  }
  
  public void resumeCameraCapture()
  {
    if (this.o) {
      h();
    }
    this.o = false;
    i();
    if (this.A != null) {
      runOnDraw(new Runnable()
      {
        public void run()
        {
          SelesVideoCameraBase.m(SelesVideoCameraBase.this);
        }
      });
    }
  }
  
  private void i()
  {
    if (((this.k) && (!this.l)) || (this.H == null)) {
      return;
    }
    this.H.addCallbackBuffer(new byte[this.r]);
    this.H.addCallbackBuffer(new byte[this.r]);
  }
  
  public void addTarget(SelesContext.SelesInput paramSelesInput, int paramInt)
  {
    super.addTarget(paramSelesInput, paramInt);
    if (paramSelesInput != null) {
      paramSelesInput.setInputRotation(this.mOutputRotation, paramInt);
    }
  }
  
  public void onSurfaceCreated(GL10 paramGL10, EGLConfig paramEGLConfig)
  {
    GLES20.glDisable(2929);
  }
  
  public void onSurfaceChanged(GL10 paramGL10, int paramInt1, int paramInt2)
  {
    GLES20.glViewport(0, 0, paramInt1, paramInt2);
  }
  
  public void onDrawFrame(GL10 paramGL10)
  {
    GLES20.glClear(16640);
    TuSdkDate localTuSdkDate = null;
    int i1 = !isOnDrawTasksEmpty() ? 1 : 0;
    if ((this.F) && (this.p) && (i1 != 0)) {
      localTuSdkDate = TuSdkDate.create();
    }
    runPendingOnDrawTasks();
    updateTargetsForVideoCameraUsingCacheTexture();
    if ((!this.o) && (getEnableFixedFramerate())) {
      c();
    }
    runPendingOnDrawEndTasks();
    a(localTuSdkDate);
  }
  
  public void onPreviewFrame(final byte[] paramArrayOfByte, final Camera paramCamera)
  {
    if (this.o) {
      return;
    }
    if ((this.k) && (this.l))
    {
      if (!j()) {
        processFrameData(paramArrayOfByte);
      }
      paramCamera.addCallbackBuffer(paramArrayOfByte);
      return;
    }
    l();
    if ((!isOnDrawTasksEmpty()) || (j()))
    {
      paramCamera.addCallbackBuffer(paramArrayOfByte);
      return;
    }
    m();
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesVideoCameraBase.a(SelesVideoCameraBase.this, paramArrayOfByte);
        paramCamera.addCallbackBuffer(paramArrayOfByte);
      }
    });
  }
  
  protected void processFrameData(byte[] paramArrayOfByte) {}
  
  private void a(byte[] paramArrayOfByte)
  {
    ColorSpaceConvert.nv21ToRgba(paramArrayOfByte, this.mInputTextureSize.width, this.mInputTextureSize.height, this.q.array());
    k();
    GLES20.glBindTexture(3553, this.mOutputFramebuffer.getTexture());
    GLES20.glTexSubImage2D(3553, 0, 0, 0, this.mInputTextureSize.width, this.mInputTextureSize.height, 6408, 5121, this.q);
    GLES20.glBindTexture(3553, 0);
  }
  
  public void onFrameAvailable(SurfaceTexture paramSurfaceTexture)
  {
    if (!this.k) {
      return;
    }
    l();
    if (getEnableFixedFramerate())
    {
      if ((isOnDrawTasksEmpty()) && (!this.o) && (!j())) {}
    }
    else if ((this.o) || (j())) {
      return;
    }
    m();
    runOnDraw(new Runnable()
    {
      @TargetApi(15)
      public void run()
      {
        if (!SelesVideoCameraBase.this.getEnableFixedFramerate()) {
          SelesVideoCameraBase.m(SelesVideoCameraBase.this);
        }
        SelesVideoCameraBase.this.processVideoSampleBufferOES();
      }
    });
    if (!getEnableFixedFramerate()) {
      updateCameraView();
    }
  }
  
  protected void updateCameraView() {}
  
  @TargetApi(15)
  protected void processVideoSampleBufferOES()
  {
    SelesContext.setActiveShaderProgram(this.f);
    k();
    GLES20.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
    GLES20.glClear(16384);
    GLES20.glActiveTexture(33986);
    GLES20.glBindTexture(36197, this.j);
    GLES20.glUniform1i(this.i, 2);
    GLES20.glVertexAttribPointer(this.g, 2, 5126, false, 0, this.d);
    GLES20.glVertexAttribPointer(this.h, 2, 5126, false, 0, this.e);
    GLES20.glDrawArrays(5, 0, 4);
    GLES20.glBindTexture(36197, 0);
  }
  
  private boolean j()
  {
    if (this.w < 1L)
    {
      this.w += 1L;
      return true;
    }
    return false;
  }
  
  private void k()
  {
    if (this.v)
    {
      this.mOutputFramebuffer = e();
      this.v = false;
    }
    this.mOutputFramebuffer.activateFramebuffer();
  }
  
  private void l()
  {
    if (this.p) {
      return;
    }
    this.p = true;
    this.C = TuSdkDate.create();
    onCameraStarted();
  }
  
  protected void updateTargetsForVideoCameraUsingCacheTexture()
  {
    int i1 = 0;
    int i2 = this.mTargets.size();
    SelesContext.SelesInput localSelesInput;
    int i3;
    while (i1 < i2)
    {
      localSelesInput = (SelesContext.SelesInput)this.mTargets.get(i1);
      if (localSelesInput.isEnabled())
      {
        i3 = ((Integer)this.mTargetTextureIndices.get(i1)).intValue();
        localSelesInput.setInputRotation(this.mOutputRotation, i3);
        if (localSelesInput != getTargetToIgnoreForUpdates())
        {
          localSelesInput.setInputSize(this.mInputTextureSize, i3);
          localSelesInput.setCurrentlyReceivingMonochromeInput(localSelesInput.wantsMonochromeInput());
        }
        localSelesInput.setInputFramebuffer(this.mOutputFramebuffer, i3);
      }
      i1++;
    }
    i1 = 0;
    i2 = this.mTargets.size();
    while (i1 < i2)
    {
      localSelesInput = (SelesContext.SelesInput)this.mTargets.get(i1);
      if ((localSelesInput.isEnabled()) && (localSelesInput != getTargetToIgnoreForUpdates()))
      {
        i3 = ((Integer)this.mTargetTextureIndices.get(i1)).intValue();
        localSelesInput.newFrameReady(System.nanoTime(), i3);
      }
      i1++;
    }
  }
  
  private void a(TuSdkDate paramTuSdkDate)
  {
    if ((!this.F) || (paramTuSdkDate == null)) {
      return;
    }
    this.E += 1L;
    if (this.E > 1L)
    {
      long l1 = paramTuSdkDate.diffOfMillis();
      this.E %= 200L;
      if (this.E == 0L) {
        this.D = 0L;
      }
      this.D += l1;
      TLog.i("Frame time Average[%s ms], Current[%s ms]", new Object[] { Float.valueOf(averageFrameDurationDuringCapture()), Long.valueOf(l1) });
    }
  }
  
  private void m()
  {
    if (!this.u) {
      return;
    }
    this.u = false;
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesVideoCameraBase.this.mOutputRotation = (SelesVideoCameraBase.k(SelesVideoCameraBase.this) == null ? ImageOrientation.Up : SelesVideoCameraBase.k(SelesVideoCameraBase.this).previewOrientation());
        int i = 0;
        int j = SelesVideoCameraBase.this.mTargets.size();
        while (i < j)
        {
          SelesContext.SelesInput localSelesInput = (SelesContext.SelesInput)SelesVideoCameraBase.this.mTargets.get(i);
          int k = ((Integer)SelesVideoCameraBase.this.mTargetTextureIndices.get(i)).intValue();
          localSelesInput.setInputRotation(SelesVideoCameraBase.this.mOutputRotation, k);
          i++;
        }
      }
    });
  }
  
  protected void runPendingOnDrawTasks()
  {
    a(this.s);
  }
  
  protected void runPendingOnDrawEndTasks()
  {
    a(this.t);
  }
  
  protected boolean isOnDrawTasksEmpty()
  {
    boolean bool = false;
    synchronized (this.s)
    {
      bool = this.s.isEmpty();
    }
    return bool;
  }
  
  private void a(Queue<Runnable> paramQueue)
  {
    synchronized (paramQueue)
    {
      while (!paramQueue.isEmpty()) {
        ((Runnable)paramQueue.poll()).run();
      }
    }
  }
  
  protected void runOnDraw(Runnable paramRunnable)
  {
    synchronized (this.s)
    {
      this.s.add(paramRunnable);
    }
  }
  
  protected void runOnDrawEnd(Runnable paramRunnable)
  {
    synchronized (this.t)
    {
      this.t.add(paramRunnable);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\sources\SelesVideoCameraBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */