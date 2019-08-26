package org.lasque.tusdk.core.seles.sources;

import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.opengl.GLES20;
import android.os.Build.VERSION;
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
import org.lasque.tusdk.core.seles.extend.SelesSurfaceTexture;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class SelesSurfaceReceiver
  extends SelesOutput
  implements TuSdkRecordSurface, SelesSurfaceHolder
{
  private ImageOrientation a = ImageOrientation.Up;
  private TuSdkSize b;
  protected SelesFramebuffer mSurfaceFBO;
  private SelesSurfaceTexture c;
  private FloatBuffer d = SelesFilter.buildBuffer(SelesFilter.imageVertices);
  private FloatBuffer e = SelesFilter.buildBuffer(SelesFilter.noRotationTextureCoordinates);
  private SelesGLProgram f;
  private int g;
  private int h;
  private int i;
  private boolean j = false;
  private long k = -1L;
  private SelesVerticeCoordinateCorpBuilder l;
  private SurfaceTexture.OnFrameAvailableListener m;
  private RectF n;
  private boolean o = false;
  private float p = 0.0F;
  private float q = 0.0F;
  private float r = 0.0F;
  private float s = 1.0F;
  private final Map<SelesContext.SelesInput, Integer> t = new LinkedHashMap();
  
  public boolean isInited()
  {
    return this.o;
  }
  
  public SurfaceTexture getSurfaceTexture()
  {
    return this.c;
  }
  
  public SurfaceTexture requestSurfaceTexture()
  {
    if (this.mSurfaceFBO == null)
    {
      TLog.w("%s requestSurface need run first initInGLThread in GL Thread", new Object[] { "SelesSurfaceReceiver" });
      return null;
    }
    e();
    this.c = new SelesSurfaceTexture(this.mSurfaceFBO.getTexture());
    this.c.setOnFrameAvailableListener(this.m);
    return this.c;
  }
  
  public void setSurfaceTextureListener(SurfaceTexture.OnFrameAvailableListener paramOnFrameAvailableListener)
  {
    this.m = paramOnFrameAvailableListener;
    if (this.c != null) {
      this.c.setOnFrameAvailableListener(paramOnFrameAvailableListener);
    }
  }
  
  public void updateSurfaceTexImage()
  {
    if (this.c == null)
    {
      TLog.w("%s updateSurfaceTexImage need run first newFrameReadyInGLThread in GL Thread", new Object[] { "SelesSurfaceReceiver" });
      return;
    }
    this.c.updateTexImage();
  }
  
  public void forceUpdateSurfaceTexImage()
  {
    if (this.c == null)
    {
      TLog.w("%s updateSurfaceTexImage need run first newFrameReadyInGLThread in GL Thread", new Object[] { "SelesSurfaceReceiver" });
      return;
    }
    this.c.forceUpdateTexImage();
  }
  
  public void updateSurfaceTexImage(long paramLong)
  {
    updateSurfaceTexImage();
    newFrameReadyInGLThread(paramLong);
  }
  
  public long getSurfaceTexTimestampNs()
  {
    if (this.c == null)
    {
      TLog.w("%s getSurfaceTexTimestampNs need run first newFrameReadyInGLThread in GL Thread", new Object[] { "SelesSurfaceReceiver" });
      return 0L;
    }
    return this.c.getTimestamp();
  }
  
  public void setSurfaceTexTimestampNs(long paramLong)
  {
    if (this.c == null)
    {
      TLog.w("%s setSurfaceTexTimestampNs need run first newFrameReadyInGLThread in GL Thread", new Object[] { "SelesSurfaceReceiver" });
      return;
    }
    this.c.setDefindTimestamp(paramLong);
  }
  
  public void setTextureCoordinateBuilder(SelesVerticeCoordinateCorpBuilder paramSelesVerticeCoordinateCorpBuilder)
  {
    this.l = paramSelesVerticeCoordinateCorpBuilder;
    if ((this.l != null) && (this.n != null)) {
      this.l.setPreCropRect(this.n);
    }
  }
  
  public void setPreCropRect(RectF paramRectF)
  {
    this.n = paramRectF;
    if (this.l != null) {
      this.l.setPreCropRect(this.n);
    }
  }
  
  public void setEnableClip(boolean paramBoolean)
  {
    if (this.l != null) {
      this.l.setEnableClip(paramBoolean);
    }
  }
  
  public TuSdkSize setOutputRatio(float paramFloat)
  {
    if (this.l != null) {
      return this.l.setOutputRatio(paramFloat);
    }
    return null;
  }
  
  public void setOutputSize(TuSdkSize paramTuSdkSize)
  {
    this.b = paramTuSdkSize;
    if (this.l != null) {
      this.l.setOutputSize(paramTuSdkSize);
    }
  }
  
  public void setCropRect(RectF paramRectF)
  {
    if (this.l != null) {
      this.l.setCropRect(paramRectF);
    }
  }
  
  public void setCanvasColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    this.p = paramFloat1;
    this.q = paramFloat2;
    this.r = paramFloat3;
    this.s = paramFloat4;
  }
  
  public void setCanvasColor(int paramInt)
  {
    setCanvasColor(Color.red(paramInt) / 255.0F, Color.green(paramInt) / 255.0F, Color.blue(paramInt) / 255.0F, Color.alpha(paramInt) / 255.0F);
  }
  
  public SelesSurfaceReceiver()
  {
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesSurfaceReceiver.a(SelesSurfaceReceiver.this);
      }
    });
  }
  
  private void a()
  {
    this.k = Thread.currentThread().getId();
    this.f = SelesContext.program("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", "#extension GL_OES_EGL_image_external : require\nvarying highp vec2 textureCoordinate;uniform samplerExternalOES inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}");
    if (!this.f.isInitialized())
    {
      this.f.addAttribute("position");
      this.f.addAttribute("inputTextureCoordinate");
      if (!this.f.link())
      {
        TLog.i("%s Program link log: %s", new Object[] { "SelesSurfaceReceiver", this.f.getProgramLog() });
        TLog.i("%s Fragment shader compile log: %s", new Object[] { "SelesSurfaceReceiver", this.f.getFragmentShaderLog() });
        TLog.i("%s Vertex link log: %s", new Object[] { "SelesSurfaceReceiver", this.f.getVertexShaderLog() });
        this.f = null;
        TLog.e("%s Filter shader link failed: %s", new Object[] { "SelesSurfaceReceiver", getClass() });
        return;
      }
    }
    this.g = this.f.attributeIndex("position");
    this.h = this.f.attributeIndex("inputTextureCoordinate");
    this.i = this.f.uniformIndex("inputImageTexture");
    SelesContext.setActiveShaderProgram(this.f);
    GLES20.glEnableVertexAttribArray(this.g);
    GLES20.glEnableVertexAttribArray(this.h);
    initSurfaceFBO();
    this.j = true;
    this.o = true;
  }
  
  protected void initSurfaceFBO()
  {
    this.mSurfaceFBO = SelesContext.sharedFramebufferCache().fetchTextureOES();
  }
  
  public void initInGLThread()
  {
    runPendingOnDrawTasks();
  }
  
  public void newFrameReadyInGLThread(long paramLong)
  {
    if (this.k != Thread.currentThread().getId())
    {
      TLog.w("%s newFrameReadyInGLThread need run in GL thread", new Object[] { "SelesSurfaceReceiver" });
      return;
    }
    if ((this.l != null) && (this.l.calculate(this.mInputTextureSize, this.a, this.d, this.e)))
    {
      this.b = this.l.outputSize();
    }
    else
    {
      this.e.clear();
      this.e.put(SelesFilter.textureCoordinates(this.a)).position(0);
      this.b = this.mInputTextureSize;
    }
    renderToTexture(this.d, this.e);
    a(paramLong);
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    runPendingOnDrawTasks();
    if (!this.b.isSize()) {
      return;
    }
    SelesContext.setActiveShaderProgram(this.f);
    b();
    GLES20.glClearColor(this.p, this.q, this.r, this.s);
    GLES20.glClear(16384);
    GLES20.glActiveTexture(33986);
    GLES20.glBindTexture(36197, d());
    GLES20.glUniform1i(this.i, 2);
    GLES20.glEnableVertexAttribArray(this.g);
    GLES20.glEnableVertexAttribArray(this.h);
    GLES20.glVertexAttribPointer(this.g, 2, 5126, false, 0, paramFloatBuffer1);
    GLES20.glVertexAttribPointer(this.h, 2, 5126, false, 0, paramFloatBuffer2);
    GLES20.glDrawArrays(5, 0, 4);
    GLES20.glBindTexture(36197, 0);
  }
  
  private void b()
  {
    if ((this.j) || (this.mOutputFramebuffer == null))
    {
      c();
      this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, outputFrameSize());
      this.mOutputFramebuffer.disableReferenceCounting();
      this.j = false;
    }
    this.mOutputFramebuffer.activateFramebuffer();
  }
  
  public void genOutputFrameBuffer(TuSdkSize paramTuSdkSize)
  {
    if (this.mOutputFramebuffer == null)
    {
      this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, paramTuSdkSize);
      this.mOutputFramebuffer.disableReferenceCounting();
    }
  }
  
  private void c()
  {
    if (this.mOutputFramebuffer == null) {
      return;
    }
    this.mOutputFramebuffer.clearAllLocks();
    SelesContext.returnFramebufferToCache(this.mOutputFramebuffer);
    this.mOutputFramebuffer = null;
  }
  
  private int d()
  {
    int i1 = 0;
    if (this.mSurfaceFBO != null) {
      i1 = this.mSurfaceFBO.getTexture();
    }
    return i1;
  }
  
  private void a(long paramLong)
  {
    this.t.clear();
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
          this.t.put(localObject, Integer.valueOf(i2));
          setInputFramebufferForTarget((SelesContext.SelesInput)localObject, i2);
          ((SelesContext.SelesInput)localObject).setInputSize(outputFrameSize(), i2);
        }
      }
    }
    localIterator = this.t.entrySet().iterator();
    while (localIterator.hasNext())
    {
      localObject = (Map.Entry)localIterator.next();
      ((SelesContext.SelesInput)((Map.Entry)localObject).getKey()).newFrameReady(paramLong, ((Integer)((Map.Entry)localObject).getValue()).intValue());
    }
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
    this.j = true;
  }
  
  public void setInputRotation(ImageOrientation paramImageOrientation)
  {
    if ((paramImageOrientation == null) || (paramImageOrientation == this.a)) {
      return;
    }
    TuSdkSize localTuSdkSize = this.mInputTextureSize.transforOrientation(this.a);
    this.a = paramImageOrientation;
    this.mInputTextureSize = localTuSdkSize.transforOrientation(paramImageOrientation);
    this.j = true;
  }
  
  protected void onDestroy()
  {
    c();
    e();
    if (this.mSurfaceFBO != null)
    {
      this.mSurfaceFBO.destroy();
      this.mSurfaceFBO = null;
    }
  }
  
  private void e()
  {
    if (this.c == null) {
      return;
    }
    if (Build.VERSION.SDK_INT >= 14) {
      this.c.release();
    }
    this.c = null;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\sources\SelesSurfaceReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */