package org.lasque.tusdk.core.seles.sources;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
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

@TargetApi(21)
public class SelesVideoCamera2Base
  extends SelesOutput
  implements SurfaceTexture.OnFrameAvailableListener, GLSurfaceView.Renderer
{
  private static final float[] b = { -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F };
  private static final float[] c = { 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
  private FloatBuffer d;
  private FloatBuffer e;
  private SelesGLProgram f;
  private int g;
  private int h;
  private int i;
  private int j;
  private final Context k;
  private boolean l;
  protected ImageOrientation mOutputRotation;
  private boolean m;
  private final Queue<Runnable> n;
  private final Queue<Runnable> o;
  private boolean p;
  private boolean q;
  private long r;
  private InterfaceOrientation s = InterfaceOrientation.Portrait;
  private boolean t;
  private boolean u;
  private SurfaceTexture v;
  private boolean w;
  private TuSdkDate x;
  private long y;
  private long z;
  private boolean A;
  private SelesVideoCamera2Engine B;
  
  public Context getContext()
  {
    return this.k;
  }
  
  public InterfaceOrientation getOutputImageOrientation()
  {
    return this.s;
  }
  
  public void setOutputImageOrientation(InterfaceOrientation paramInterfaceOrientation)
  {
    if (paramInterfaceOrientation == null) {
      return;
    }
    this.s = paramInterfaceOrientation;
    this.p = true;
  }
  
  public boolean isHorizontallyMirrorFrontFacingCamera()
  {
    return this.t;
  }
  
  public void setHorizontallyMirrorFrontFacingCamera(boolean paramBoolean)
  {
    this.t = paramBoolean;
    this.p = true;
  }
  
  public boolean isHorizontallyMirrorRearFacingCamera()
  {
    return this.u;
  }
  
  public void setHorizontallyMirrorRearFacingCamera(boolean paramBoolean)
  {
    this.u = paramBoolean;
    this.p = true;
  }
  
  public boolean isCapturing()
  {
    return this.m;
  }
  
  public boolean isCapturePaused()
  {
    return this.l;
  }
  
  public boolean hasCreateSurface()
  {
    return this.v != null;
  }
  
  public boolean getRunBenchmark()
  {
    return this.A;
  }
  
  public void setRunBenchmark(boolean paramBoolean)
  {
    this.A = paramBoolean;
  }
  
  public void setCameraEngine(SelesVideoCamera2Engine paramSelesVideoCamera2Engine)
  {
    if ((!a) && (paramSelesVideoCamera2Engine == null)) {
      throw new AssertionError();
    }
    this.B = paramSelesVideoCamera2Engine;
  }
  
  public SelesVideoCamera2Base(Context paramContext)
  {
    TLog.i("Used Camera 2 Api", new Object[0]);
    this.k = paramContext;
    this.n = new LinkedList();
    this.o = new LinkedList();
    this.mOutputRotation = ImageOrientation.Up;
    setOutputImageOrientation(InterfaceOrientation.Portrait);
    b();
    j();
  }
  
  protected void onDestroy()
  {
    stopCameraCapture();
  }
  
  public float averageFrameDurationDuringCapture()
  {
    return (float)this.y / (float)(this.z - 1L);
  }
  
  public void resetBenchmarkAverage()
  {
    this.z = 0L;
    this.y = 0L;
  }
  
  private SelesFramebuffer a()
  {
    SelesFramebuffer localSelesFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, this.mInputTextureSize);
    localSelesFramebuffer.disableReferenceCounting();
    return localSelesFramebuffer;
  }
  
  private void b()
  {
    if (this.f != null) {
      return;
    }
    this.d = SelesFilter.buildBuffer(b);
    this.e = SelesFilter.buildBuffer(c);
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesVideoCamera2Base.a(SelesVideoCamera2Base.this, SelesContext.program("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", "#extension GL_OES_EGL_image_external : require\nvarying highp vec2 textureCoordinate;uniform samplerExternalOES inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}"));
        if (!SelesVideoCamera2Base.a(SelesVideoCamera2Base.this).isInitialized())
        {
          SelesVideoCamera2Base.a(SelesVideoCamera2Base.this).addAttribute("position");
          SelesVideoCamera2Base.a(SelesVideoCamera2Base.this).addAttribute("inputTextureCoordinate");
          if (!SelesVideoCamera2Base.a(SelesVideoCamera2Base.this).link())
          {
            TLog.i("Program link log: %s", new Object[] { SelesVideoCamera2Base.a(SelesVideoCamera2Base.this).getProgramLog() });
            TLog.i("Fragment shader compile log: %s", new Object[] { SelesVideoCamera2Base.a(SelesVideoCamera2Base.this).getFragmentShaderLog() });
            TLog.i("Vertex link log: %s", new Object[] { SelesVideoCamera2Base.a(SelesVideoCamera2Base.this).getVertexShaderLog() });
            SelesVideoCamera2Base.a(SelesVideoCamera2Base.this, null);
            TLog.e("Filter shader link failed: %s", new Object[] { getClass() });
            return;
          }
        }
        SelesVideoCamera2Base.a(SelesVideoCamera2Base.this, SelesVideoCamera2Base.a(SelesVideoCamera2Base.this).attributeIndex("position"));
        SelesVideoCamera2Base.b(SelesVideoCamera2Base.this, SelesVideoCamera2Base.a(SelesVideoCamera2Base.this).attributeIndex("inputTextureCoordinate"));
        SelesVideoCamera2Base.c(SelesVideoCamera2Base.this, SelesVideoCamera2Base.a(SelesVideoCamera2Base.this).uniformIndex("inputImageTexture"));
        SelesContext.setActiveShaderProgram(SelesVideoCamera2Base.a(SelesVideoCamera2Base.this));
        GLES20.glEnableVertexAttribArray(SelesVideoCamera2Base.b(SelesVideoCamera2Base.this));
        GLES20.glEnableVertexAttribArray(SelesVideoCamera2Base.c(SelesVideoCamera2Base.this));
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
        SelesVideoCamera2Base.this.startCameraCapture();
      }
    });
  }
  
  public void stopCameraCapture()
  {
    this.m = false;
    this.l = false;
    this.r = 0L;
    this.w = false;
    if (this.v != null)
    {
      TLog.d("mSurfaceTexture.release", new Object[0]);
      this.v.setOnFrameAvailableListener(null);
      this.v.release();
      this.v = null;
    }
    if ((this.A) && (this.x != null))
    {
      TLog.d("Capture frame time: %s ms", new Object[] { Long.valueOf(this.x.diffOfMillis()) });
      TLog.i("Average frame time: %s ms", new Object[] { Float.valueOf(averageFrameDurationDuringCapture()) });
    }
    resetBenchmarkAverage();
  }
  
  private void c()
  {
    if ((this.v != null) && (this.w)) {
      this.v.updateTexImage();
    }
  }
  
  protected void onMainThreadStart()
  {
    if (this.B == null)
    {
      TLog.d("You need setCameraEngine(SelesVideoCamera2Engine engine)", new Object[0]);
      return;
    }
    stopCameraCapture();
    if (!this.B.canInitCamera()) {
      return;
    }
    d();
  }
  
  private void d()
  {
    if (!this.B.onInitCamera()) {
      return;
    }
    TuSdkSize localTuSdkSize = this.B.previewOptimalSize();
    if (localTuSdkSize != null) {
      this.mInputTextureSize = localTuSdkSize;
    }
    TLog.d("mInputTextureSize: %s", new Object[] { this.mInputTextureSize });
    this.p = true;
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesVideoCamera2Base.d(SelesVideoCamera2Base.this, SelesVideoCamera2Base.d(SelesVideoCamera2Base.this));
        SelesVideoCamera2Base.a(SelesVideoCamera2Base.this, true);
        SelesVideoCamera2Base.a(SelesVideoCamera2Base.this, new SurfaceTexture(SelesVideoCamera2Base.e(SelesVideoCamera2Base.this)));
        SelesVideoCamera2Base.b(SelesVideoCamera2Base.this, true);
        SelesVideoCamera2Base.f(SelesVideoCamera2Base.this).setDefaultBufferSize(SelesVideoCamera2Base.this.mInputTextureSize.width, SelesVideoCamera2Base.this.mInputTextureSize.height);
        SelesVideoCamera2Base.f(SelesVideoCamera2Base.this).setOnFrameAvailableListener(SelesVideoCamera2Base.this);
        SelesVideoCamera2Base.g(SelesVideoCamera2Base.this).onCameraWillOpen(SelesVideoCamera2Base.f(SelesVideoCamera2Base.this));
      }
    });
  }
  
  private int e()
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
  
  protected void onCameraStarted()
  {
    if (this.B != null) {
      this.B.onCameraStarted();
    }
  }
  
  public void pauseCameraCapture()
  {
    this.l = true;
  }
  
  public void resumeCameraCapture()
  {
    this.l = false;
    if (this.v != null) {
      runOnDraw(new Runnable()
      {
        public void run()
        {
          SelesVideoCamera2Base.h(SelesVideoCamera2Base.this);
        }
      });
    }
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
    if ((this.A) && (this.m) && (i1 != 0)) {
      localTuSdkDate = TuSdkDate.create();
    }
    runPendingOnDrawTasks();
    i();
    if (!this.l) {
      c();
    }
    runPendingOnDrawEndTasks();
    a(localTuSdkDate);
  }
  
  public void onFrameAvailable(SurfaceTexture paramSurfaceTexture)
  {
    h();
    if ((!isOnDrawTasksEmpty()) || (this.l) || (f()) || (this.v == null)) {
      return;
    }
    j();
    runOnDraw(new Runnable()
    {
      @TargetApi(15)
      public void run()
      {
        SelesVideoCamera2Base.this.processVideoSampleBufferOES();
      }
    });
  }
  
  @TargetApi(15)
  protected void processVideoSampleBufferOES()
  {
    SelesContext.setActiveShaderProgram(this.f);
    g();
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
  
  private boolean f()
  {
    if (this.r < 1L)
    {
      this.r += 1L;
      return true;
    }
    return false;
  }
  
  private void g()
  {
    if (this.q)
    {
      this.mOutputFramebuffer = a();
      this.q = false;
    }
    this.mOutputFramebuffer.activateFramebuffer();
  }
  
  private void h()
  {
    if (this.m) {
      return;
    }
    this.m = true;
    this.x = TuSdkDate.create();
    onCameraStarted();
  }
  
  private void i()
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
    if ((!this.A) || (paramTuSdkDate == null)) {
      return;
    }
    this.z += 1L;
    if (this.z > 1L)
    {
      long l1 = paramTuSdkDate.diffOfMillis();
      this.y += l1;
      TLog.i("Average frame time: %s ms", new Object[] { Float.valueOf(averageFrameDurationDuringCapture()) });
      TLog.i("Current frame time: %s ms", new Object[] { Long.valueOf(l1) });
    }
  }
  
  private void j()
  {
    if (!this.p) {
      return;
    }
    this.p = false;
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesVideoCamera2Base.this.mOutputRotation = (SelesVideoCamera2Base.g(SelesVideoCamera2Base.this) == null ? ImageOrientation.Up : SelesVideoCamera2Base.g(SelesVideoCamera2Base.this).previewOrientation());
        int i = 0;
        int j = SelesVideoCamera2Base.this.mTargets.size();
        while (i < j)
        {
          SelesContext.SelesInput localSelesInput = (SelesContext.SelesInput)SelesVideoCamera2Base.this.mTargets.get(i);
          int k = ((Integer)SelesVideoCamera2Base.this.mTargetTextureIndices.get(i)).intValue();
          localSelesInput.setInputRotation(SelesVideoCamera2Base.this.mOutputRotation, k);
          i++;
        }
      }
    });
  }
  
  protected void runPendingOnDrawTasks()
  {
    a(this.n);
  }
  
  protected void runPendingOnDrawEndTasks()
  {
    a(this.o);
  }
  
  protected boolean isOnDrawTasksEmpty()
  {
    boolean bool = false;
    synchronized (this.n)
    {
      bool = this.n.isEmpty();
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
    synchronized (this.n)
    {
      this.n.add(paramRunnable);
    }
  }
  
  protected void runOnDrawEnd(Runnable paramRunnable)
  {
    synchronized (this.o)
    {
      this.o.add(paramRunnable);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\sources\SelesVideoCamera2Base.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */