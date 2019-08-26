package org.lasque.tusdk.core.media.codec.suit.imageToVideo;

import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.opengl.GLES20;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.sources.SelesOutput;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

class TuSdkMediaVideoComposProcesser
  extends SelesOutput
  implements TuSdkRecordSurface
{
  private ImageOrientation a = ImageOrientation.Up;
  private TuSdkSize b;
  private SelesFramebuffer c;
  private FloatBuffer d = SelesFilter.buildBuffer(SelesFilter.imageVertices);
  private FloatBuffer e = SelesFilter.buildBuffer(SelesFilter.noRotationTextureCoordinates);
  private SelesGLProgram f;
  private int g;
  private int h;
  private int i;
  private long j = -1L;
  private SelesVerticeCoordinateCorpBuilder k;
  private RectF l;
  private boolean m = false;
  private float n = 0.0F;
  private float o = 0.0F;
  private float p = 0.0F;
  private float q = 1.0F;
  private boolean r = false;
  private long s = 0L;
  private ComposProcesserListener t;
  private final Map<SelesContext.SelesInput, Integer> u = new LinkedHashMap();
  private SelesFilter v = null;
  
  public void setTextureCoordinateBuilder(SelesVerticeCoordinateCorpBuilder paramSelesVerticeCoordinateCorpBuilder)
  {
    this.k = paramSelesVerticeCoordinateCorpBuilder;
    if ((this.k != null) && (this.l != null)) {
      this.k.setPreCropRect(this.l);
    }
  }
  
  public void setPreCropRect(RectF paramRectF)
  {
    this.l = paramRectF;
    if (this.k != null) {
      this.k.setPreCropRect(this.l);
    }
  }
  
  public void setEnableClip(boolean paramBoolean)
  {
    if (this.k != null) {
      this.k.setEnableClip(paramBoolean);
    }
  }
  
  public TuSdkSize setOutputRatio(float paramFloat)
  {
    if (this.k != null) {
      return this.k.setOutputRatio(paramFloat);
    }
    return null;
  }
  
  public void setOutputSize(TuSdkSize paramTuSdkSize)
  {
    this.b = paramTuSdkSize;
    if (this.k != null) {
      this.k.setOutputSize(paramTuSdkSize);
    }
  }
  
  public void setCanvasColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    this.n = paramFloat1;
    this.o = paramFloat2;
    this.p = paramFloat3;
    this.q = paramFloat4;
  }
  
  public void setCanvasColor(int paramInt)
  {
    setCanvasColor(Color.red(paramInt) / 255.0F, Color.green(paramInt) / 255.0F, Color.blue(paramInt) / 255.0F, Color.alpha(paramInt) / 255.0F);
  }
  
  public void setComposProcesserListener(ComposProcesserListener paramComposProcesserListener)
  {
    this.t = paramComposProcesserListener;
  }
  
  public void setCurrentFrameUs(long paramLong)
  {
    this.s = paramLong;
  }
  
  public TuSdkMediaVideoComposProcesser()
  {
    runOnDraw(new Runnable()
    {
      public void run()
      {
        TuSdkMediaVideoComposProcesser.a(TuSdkMediaVideoComposProcesser.this);
      }
    });
  }
  
  private void a()
  {
    this.j = Thread.currentThread().getId();
    this.f = SelesContext.program("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", "varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}");
    if (!this.f.isInitialized())
    {
      this.f.addAttribute("position");
      this.f.addAttribute("inputTextureCoordinate");
      if (!this.f.link())
      {
        TLog.i("%s Program link log: %s", new Object[] { "TuSdkMediaVideoComposProcesser", this.f.getProgramLog() });
        TLog.i("%s Fragment shader compile log: %s", new Object[] { "TuSdkMediaVideoComposProcesser", this.f.getFragmentShaderLog() });
        TLog.i("%s Vertex link log: %s", new Object[] { "TuSdkMediaVideoComposProcesser", this.f.getVertexShaderLog() });
        this.f = null;
        TLog.e("%s Filter shader link failed: %s", new Object[] { "TuSdkMediaVideoComposProcesser", getClass() });
        return;
      }
    }
    this.g = this.f.attributeIndex("position");
    this.h = this.f.attributeIndex("inputTextureCoordinate");
    this.i = this.f.uniformIndex("inputImageTexture");
    SelesContext.setActiveShaderProgram(this.f);
    GLES20.glEnableVertexAttribArray(this.g);
    GLES20.glEnableVertexAttribArray(this.h);
    b();
    this.m = true;
  }
  
  private void b()
  {
    this.c = SelesContext.sharedFramebufferCache().fetchFramebuffer(this.mInputTextureSize, true);
  }
  
  public void initInGLThread()
  {
    runPendingOnDrawTasks();
  }
  
  public void updateSurfaceTexImage()
  {
    newFrameReadyInGLThread(this.s);
  }
  
  public void newFrameReadyInGLThread(long paramLong)
  {
    if (this.j != Thread.currentThread().getId())
    {
      TLog.w("%s newFrameReadyInGLThread need run in GL thread", new Object[] { "TuSdkMediaVideoComposProcesser" });
      return;
    }
    setInputSize(TuSdkSize.create(this.t.drawItemComposeItem().getImageBitmap()));
    if ((this.k != null) && (this.k.calculate(this.mInputTextureSize, this.a, this.d, this.e)))
    {
      this.b = this.k.outputSize();
    }
    else
    {
      this.e.clear();
      this.e.put(SelesFilter.textureCoordinates(this.a)).position(0);
      this.b = this.mInputTextureSize;
    }
    renderToTexture(this.d, this.e);
    a(paramLong);
    GLES20.glFinish();
    if (this.t != null) {
      this.t.onFrameAvailable(null);
    }
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    runPendingOnDrawTasks();
    if (!this.b.isSize()) {
      return;
    }
    this.c.activateFramebuffer();
    this.c.bindTexture(this.t.drawItemComposeItem().getImageBitmap());
    GLES20.glBindFramebuffer(36160, 0);
    SelesContext.setActiveShaderProgram(this.f);
    d();
    GLES20.glClearColor(this.n, this.o, this.p, this.q);
    GLES20.glClear(16384);
    GLES20.glActiveTexture(33986);
    GLES20.glBindTexture(3553, c());
    GLES20.glUniform1i(this.i, 2);
    GLES20.glEnableVertexAttribArray(this.g);
    GLES20.glEnableVertexAttribArray(this.h);
    GLES20.glVertexAttribPointer(this.g, 2, 5126, false, 0, paramFloatBuffer1);
    GLES20.glVertexAttribPointer(this.h, 2, 5126, false, 0, paramFloatBuffer2);
    GLES20.glDrawArrays(5, 0, 4);
    GLES20.glBindTexture(3553, 0);
  }
  
  private int c()
  {
    int i1 = 0;
    if (this.c != null) {
      i1 = this.c.getTexture();
    }
    return i1;
  }
  
  private void d()
  {
    if ((this.r) || (this.mOutputFramebuffer == null))
    {
      e();
      this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, outputFrameSize());
      this.mOutputFramebuffer.disableReferenceCounting();
      this.r = false;
    }
    this.mOutputFramebuffer.activateFramebuffer();
  }
  
  private void e()
  {
    if (this.mOutputFramebuffer == null) {
      return;
    }
    this.mOutputFramebuffer.clearAllLocks();
    SelesContext.returnFramebufferToCache(this.mOutputFramebuffer);
    this.mOutputFramebuffer = null;
  }
  
  private void a(long paramLong)
  {
    this.u.clear();
    Iterator localIterator = this.mTargets.iterator();
    Object localObject;
    while (localIterator.hasNext())
    {
      localObject = (SelesContext.SelesInput)localIterator.next();
      if (((SelesContext.SelesInput)localObject).isEnabled()) {
        if (localObject != getTargetToIgnoreForUpdates())
        {
          int i1 = this.mTargets.indexOf(localObject);
          int i2 = ((Integer)this.mTargetTextureIndices.get(i1)).intValue();
          this.u.put(localObject, Integer.valueOf(i2));
          setInputFramebufferForTarget((SelesContext.SelesInput)localObject, i2);
          ((SelesContext.SelesInput)localObject).setInputSize(outputFrameSize(), i2);
        }
      }
    }
    localIterator = this.u.entrySet().iterator();
    while (localIterator.hasNext())
    {
      localObject = (Map.Entry)localIterator.next();
      ((SelesContext.SelesInput)((Map.Entry)localObject).getKey()).newFrameReady(paramLong, ((Integer)((Map.Entry)localObject).getValue()).intValue());
    }
  }
  
  protected void runOnDraw(Runnable paramRunnable)
  {
    super.runOnDraw(paramRunnable);
  }
  
  public TuSdkSize outputFrameSize()
  {
    return this.b == null ? this.mInputTextureSize : this.b;
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize)
  {
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize())) {
      return;
    }
    paramTuSdkSize = paramTuSdkSize.transforOrientation(this.a);
    if (this.mInputTextureSize.equals(paramTuSdkSize)) {
      return;
    }
    this.mInputTextureSize = paramTuSdkSize;
    this.r = true;
  }
  
  public void setInputRotation(ImageOrientation paramImageOrientation)
  {
    if ((paramImageOrientation == null) || (paramImageOrientation == this.a)) {
      return;
    }
    TuSdkSize localTuSdkSize = this.mInputTextureSize.transforOrientation(this.a);
    this.a = paramImageOrientation;
    this.mInputTextureSize = localTuSdkSize.transforOrientation(paramImageOrientation);
    this.r = true;
  }
  
  protected void onDestroy()
  {
    e();
    if (this.c != null)
    {
      this.c.destroy();
      this.c = null;
    }
  }
  
  public static abstract interface ComposProcesserListener
    extends SurfaceTexture.OnFrameAvailableListener
  {
    public abstract TuSdkImageComposeItem drawItemComposeItem();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\imageToVideo\TuSdkMediaVideoComposProcesser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */