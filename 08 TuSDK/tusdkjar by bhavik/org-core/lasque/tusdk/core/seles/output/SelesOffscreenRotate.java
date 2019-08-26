package org.lasque.tusdk.core.seles.output;

import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.HandlerThread;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.microedition.khronos.egl.EGLContext;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesPixelBuffer;
import org.lasque.tusdk.core.seles.egl.SelesEGL10Core;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.TuSdkSemaphore;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class SelesOffscreenRotate
  extends SelesFilter
{
  public static final String ROTATE_VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nuniform mat4 transformMatrix;\nvarying vec2 textureCoordinate;\nvoid main()\n{\n    gl_Position = transformMatrix * vec4(position.xyz, 1.0);\n    textureCoordinate = inputTextureCoordinate.xy;\n}\n";
  public static final String ROTATE_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);void main(){     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);     mediump float luminance = dot(textureColor.rgb, luminanceWeighting);     gl_FragColor = vec4(vec3(luminance), textureColor.w);}";
  private HandlerThread a = new HandlerThread("SelesOffscreenRotate");
  private Handler b;
  private SelesEGL10Core c;
  private SelesOffscreenRotateDelegate d;
  private int e;
  private float f = 0.0F;
  private float g = 0.0F;
  private float[] h;
  private TuSdkSize i = TuSdkSize.create(0, 0);
  private final FloatBuffer j;
  private float k = 1.0F;
  private SelesPixelBuffer l;
  private boolean m = true;
  private boolean n = false;
  private boolean o = false;
  
  public float getFullScale()
  {
    return this.k;
  }
  
  public void setDelegate(SelesOffscreenRotateDelegate paramSelesOffscreenRotateDelegate)
  {
    this.d = paramSelesOffscreenRotateDelegate;
  }
  
  public void setSyncOutput(boolean paramBoolean)
  {
    if (this.o == paramBoolean) {
      return;
    }
    this.o = paramBoolean;
    if (this.mImageCaptureSemaphore != null)
    {
      this.mImageCaptureSemaphore.waitSignal(0L);
      this.mImageCaptureSemaphore.signal();
    }
  }
  
  public SelesOffscreenRotate()
  {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nuniform mat4 transformMatrix;\nvarying vec2 textureCoordinate;\nvoid main()\n{\n    gl_Position = transformMatrix * vec4(position.xyz, 1.0);\n    textureCoordinate = inputTextureCoordinate.xy;\n}\n", "varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);void main(){     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);     mediump float luminance = dot(textureColor.rgb, luminanceWeighting);     gl_FragColor = vec4(vec3(luminance), textureColor.w);}");
    this.a.start();
    this.b = new Handler(this.a.getLooper());
    this.h = new float[16];
    Matrix.setIdentityM(this.h, 0);
    this.j = buildBuffer(SelesFilter.imageVertices);
  }
  
  private void a()
  {
    if (this.l != null) {
      this.l.destory();
    }
    this.l = null;
  }
  
  private void b()
  {
    a();
    if (this.c != null) {
      this.c.destroy();
    }
    this.c = null;
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    if (this.b != null) {
      this.b.post(new Runnable()
      {
        public void run()
        {
          SelesOffscreenRotate.a(SelesOffscreenRotate.this);
        }
      });
    }
    if (this.a != null) {
      this.a.quit();
    }
    this.b = null;
    this.a = null;
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.e = this.mFilterProgram.uniformIndex("transformMatrix");
    a(this.h);
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  private void a(float[] paramArrayOfFloat)
  {
    this.h = paramArrayOfFloat;
    setMatrix4f(this.h, this.e, this.mFilterProgram);
  }
  
  private void c()
  {
    if ((this.mInputTextureSize == null) || (!this.mInputTextureSize.isSize()) || (this.c != null)) {
      return;
    }
    final EGLContext localEGLContext = SelesContext.currentEGLContext();
    if (localEGLContext == null) {
      return;
    }
    this.b.post(new Runnable()
    {
      public void run()
      {
        SelesOffscreenRotate.a(SelesOffscreenRotate.this, localEGLContext);
      }
    });
  }
  
  private void a(EGLContext paramEGLContext)
  {
    if (this.c != null) {
      return;
    }
    this.c = SelesEGL10Core.create(this.mInputTextureSize, paramEGLContext);
    runPendingOnDrawTasks();
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    super.setInputSize(paramTuSdkSize, paramInt);
    if (this.mInputRotation.isTransposed()) {
      paramTuSdkSize = TuSdkSize.create(paramTuSdkSize.height, paramTuSdkSize.width);
    }
    if (this.i.equals(paramTuSdkSize)) {
      return;
    }
    if ((paramInt == 0) && (paramTuSdkSize.isSize()))
    {
      this.i = paramTuSdkSize;
      this.k = paramTuSdkSize.maxMinRatio();
      d();
    }
  }
  
  private void d()
  {
    TuSdkSize localTuSdkSize = this.mInputTextureSize.copy();
    Rect localRect = RectHelper.makeRectWithAspectRatioInsideRect(this.i, new Rect(0, 0, localTuSdkSize.width, localTuSdkSize.height));
    float f2 = localRect.width() / localTuSdkSize.width;
    float f1 = localRect.height() / localTuSdkSize.height;
    float[] arrayOfFloat = { -f2, -f1, f2, -f1, -f2, f1, f2, f1 };
    this.j.clear();
    this.j.put(arrayOfFloat).position(0);
  }
  
  public void newFrameReady(final long paramLong, int paramInt)
  {
    if (this.mFirstInputFramebuffer == null) {
      return;
    }
    setEnabled(false);
    c();
    GLES20.glFinish();
    if ((this.mImageCaptureSemaphore != null) && (!this.mImageCaptureSemaphore.waitSignal(0L))) {
      return;
    }
    this.b.post(new Runnable()
    {
      public void run()
      {
        if (SelesOffscreenRotate.b(SelesOffscreenRotate.this) == null)
        {
          if (SelesOffscreenRotate.c(SelesOffscreenRotate.this) != null) {
            SelesOffscreenRotate.d(SelesOffscreenRotate.this).signal();
          }
          return;
        }
        SelesOffscreenRotate.a(SelesOffscreenRotate.this, paramLong, this.b);
        if (SelesOffscreenRotate.e(SelesOffscreenRotate.this) != null) {
          SelesOffscreenRotate.f(SelesOffscreenRotate.this).signal();
        }
      }
    });
  }
  
  public void setCurrentlyReceivingMonochromeInput(boolean paramBoolean)
  {
    if ((!paramBoolean) || (!this.o)) {
      return;
    }
    if (this.mImageCaptureSemaphore != null)
    {
      this.mImageCaptureSemaphore.waitSignal(300L);
      this.mImageCaptureSemaphore.signal();
    }
  }
  
  private void a(long paramLong, int paramInt)
  {
    super.newFrameReady(paramLong, paramInt);
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    super.renderToTexture(this.j, paramFloatBuffer2);
    checkGLError(getClass().getSimpleName());
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    SelesOffscreenRotateDelegate localSelesOffscreenRotateDelegate = this.d;
    try
    {
      TuSdkSize localTuSdkSize = outputFrameSize();
      if ((localSelesOffscreenRotateDelegate == null) || (!localTuSdkSize.isSize())) {
        return;
      }
      a(localTuSdkSize);
      boolean bool = localSelesOffscreenRotateDelegate.onFrameRendered(this);
      setEnabled(bool);
    }
    catch (Exception localException)
    {
      TLog.w("Screen Rotate Delegate is null !!!", new Object[0]);
    }
  }
  
  private void a(TuSdkSize paramTuSdkSize)
  {
    if (!this.m) {
      return;
    }
    if ((this.l == null) || (!this.l.getSize().equals(paramTuSdkSize)))
    {
      a();
      this.l = SelesContext.fetchPixelBuffer(paramTuSdkSize, 1);
    }
    this.l.preparePackBuffer();
  }
  
  public int[] getAuthors()
  {
    if (this.l == null) {
      return null;
    }
    return this.l.getBefferInfo();
  }
  
  public Buffer readBuffer()
  {
    if (this.l == null) {
      return null;
    }
    return this.l.readPackBuffer();
  }
  
  public void setAngle(float paramFloat)
  {
    this.g = this.f;
    this.f = (30 * (((int)paramFloat + 15) / 30 % 12));
    Matrix.setIdentityM(this.h, 0);
    Matrix.rotateM(this.h, 0, this.f, 0.0F, 0.0F, 1.0F);
    setMatrix4f(this.h, this.e, this.mFilterProgram);
  }
  
  public float getAngle()
  {
    if (this.m) {
      return this.g;
    }
    return this.f;
  }
  
  public IntBuffer renderBuffer()
  {
    if (this.c == null) {
      return null;
    }
    return this.c.getImageBuffer();
  }
  
  public static abstract interface SelesOffscreenRotateDelegate
  {
    public abstract boolean onFrameRendered(SelesOffscreenRotate paramSelesOffscreenRotate);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\output\SelesOffscreenRotate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */