package org.lasque.tusdk.core.seles.output;

import android.graphics.Color;
import android.opengl.GLES20;
import java.nio.FloatBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.sources.SelesWatermark;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class SelesSurfacePusher
  implements SelesSurfaceDisplay
{
  public static final float[] noRotationTextureCoordinates = { 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F };
  public static final float[] rotateRightTextureCoordinates = { 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F };
  public static final float[] rotateLeftTextureCoordinates = { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F, 1.0F };
  public static final float[] verticalFlipTextureCoordinates = { 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
  public static final float[] horizontalFlipTextureCoordinates = { 1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F };
  public static final float[] rotateRightVerticalFlipTextureCoordinates = { 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F };
  public static final float[] rotateRightHorizontalFlipTextureCoordinates = { 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F };
  public static final float[] rotate180TextureCoordinates = { 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
  public static final float[] imageVertices = { -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F };
  private final BlockingQueue<Runnable> a = new LinkedBlockingQueue();
  protected SelesFramebuffer mInputFramebufferForDisplay;
  protected SelesGLProgram mDisplayProgram;
  private int b;
  private int c;
  private int d;
  protected TuSdkSize mInputImageSize = new TuSdkSize();
  private FloatBuffer e = SelesFilter.buildBuffer(imageVertices);
  private FloatBuffer f = SelesFilter.buildBuffer(noRotationTextureCoordinates);
  private float g;
  private float h;
  private float i;
  private float j = 1.0F;
  private ImageOrientation k = ImageOrientation.Up;
  private TuSdkSize l;
  private boolean m = true;
  private SelesVerticeCoordinateBuilder n;
  private SelesWatermark o;
  
  public static float[] textureCoordinates(ImageOrientation paramImageOrientation)
  {
    if (paramImageOrientation == null) {
      paramImageOrientation = ImageOrientation.Up;
    }
    switch (2.a[paramImageOrientation.ordinal()])
    {
    case 1: 
      return rotateLeftTextureCoordinates;
    case 2: 
      return rotateRightTextureCoordinates;
    case 3: 
      return verticalFlipTextureCoordinates;
    case 4: 
      return horizontalFlipTextureCoordinates;
    case 5: 
      return rotateRightVerticalFlipTextureCoordinates;
    case 6: 
      return rotateRightHorizontalFlipTextureCoordinates;
    case 7: 
      return rotate180TextureCoordinates;
    }
    return noRotationTextureCoordinates;
  }
  
  public TuSdkSize getInputImageSize()
  {
    return this.mInputImageSize;
  }
  
  public ImageOrientation getInputRotation()
  {
    return this.k;
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    this.m = paramBoolean;
  }
  
  public void setTextureCoordinateBuilder(SelesVerticeCoordinateBuilder paramSelesVerticeCoordinateBuilder)
  {
    this.n = paramSelesVerticeCoordinateBuilder;
  }
  
  public void setBackgroundColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    this.g = paramFloat1;
    this.h = paramFloat2;
    this.i = paramFloat3;
    this.j = paramFloat4;
  }
  
  public void setBackgroundColor(int paramInt)
  {
    setBackgroundColor(Color.red(paramInt) / 255.0F, Color.green(paramInt) / 255.0F, Color.blue(paramInt) / 255.0F, Color.alpha(paramInt) / 255.0F);
  }
  
  public void setWatermark(SelesWatermark paramSelesWatermark)
  {
    this.o = paramSelesWatermark;
  }
  
  public SelesSurfacePusher()
  {
    this("varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}");
  }
  
  public SelesSurfacePusher(String paramString)
  {
    this("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", paramString);
  }
  
  public SelesSurfacePusher(final String paramString1, final String paramString2)
  {
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesSurfacePusher.a(SelesSurfacePusher.this, paramString1, paramString2);
        SelesSurfacePusher.this.onInitOnGLThread();
      }
    });
  }
  
  public void destroy()
  {
    a();
    if (this.o != null)
    {
      this.o.destroy();
      this.o = null;
    }
  }
  
  protected void finalize()
  {
    destroy();
    super.finalize();
  }
  
  protected void onInitOnGLThread() {}
  
  private void a(String paramString1, String paramString2)
  {
    this.mDisplayProgram = SelesContext.program(paramString1, paramString2);
    if (!this.mDisplayProgram.isInitialized())
    {
      initializeAttributes();
      if (!this.mDisplayProgram.link())
      {
        TLog.i("Program link log: %s", new Object[] { this.mDisplayProgram.getProgramLog() });
        TLog.i("Fragment shader compile log: %s", new Object[] { this.mDisplayProgram.getFragmentShaderLog() });
        TLog.i("Vertex link log: %s", new Object[] { this.mDisplayProgram.getVertexShaderLog() });
        this.mDisplayProgram = null;
        TLog.e("Filter shader link failed: %s", new Object[] { getClass() });
        return;
      }
    }
    this.b = this.mDisplayProgram.attributeIndex("position");
    this.c = this.mDisplayProgram.attributeIndex("inputTextureCoordinate");
    this.d = this.mDisplayProgram.uniformIndex("inputImageTexture");
    SelesContext.setActiveShaderProgram(this.mDisplayProgram);
    GLES20.glEnableVertexAttribArray(this.b);
    GLES20.glEnableVertexAttribArray(this.c);
  }
  
  protected void initializeAttributes()
  {
    this.mDisplayProgram.addAttribute("position");
    this.mDisplayProgram.addAttribute("inputTextureCoordinate");
  }
  
  private void a(long paramLong, FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    if (this.mInputFramebufferForDisplay == null) {
      return;
    }
    SelesContext.setActiveShaderProgram(this.mDisplayProgram);
    GLES20.glBindFramebuffer(36160, 0);
    GLES20.glViewport(0, 0, maximumOutputSize().width, maximumOutputSize().height);
    GLES20.glClearColor(this.g, this.h, this.i, this.j);
    GLES20.glClear(16640);
    GLES20.glActiveTexture(33988);
    GLES20.glBindTexture(3553, b());
    GLES20.glUniform1i(this.d, 4);
    GLES20.glVertexAttribPointer(this.b, 2, 5126, false, 0, paramFloatBuffer1);
    GLES20.glVertexAttribPointer(this.c, 2, 5126, false, 0, paramFloatBuffer2);
    GLES20.glDrawArrays(5, 0, 4);
    GLES20.glBindTexture(3553, 0);
    if (this.o != null) {
      this.o.drawInGLThread(paramLong, this.l, this.k);
    }
  }
  
  private void a()
  {
    if (this.mInputFramebufferForDisplay == null) {
      return;
    }
    this.mInputFramebufferForDisplay.unlock();
    this.mInputFramebufferForDisplay = null;
  }
  
  private int b()
  {
    int i1 = 0;
    if (this.mInputFramebufferForDisplay != null) {
      i1 = this.mInputFramebufferForDisplay.getTexture();
    }
    return i1;
  }
  
  public void mountAtGLThread(Runnable paramRunnable)
  {
    if (paramRunnable == null) {
      return;
    }
    runOnDraw(paramRunnable);
  }
  
  public void newFrameReady(long paramLong, int paramInt)
  {
    if ((this.n != null) && (this.n.calculate(this.mInputImageSize, this.k, this.e, this.f)))
    {
      this.l = this.n.outputSize();
    }
    else
    {
      this.f.clear();
      this.f.put(textureCoordinates(this.k)).position(0);
      this.l = this.mInputImageSize;
    }
    runPendingOnDrawTasks();
    a(paramLong, this.e, this.f);
  }
  
  public void setInputFramebuffer(SelesFramebuffer paramSelesFramebuffer, int paramInt)
  {
    if (paramSelesFramebuffer == null) {
      return;
    }
    a();
    this.mInputFramebufferForDisplay = paramSelesFramebuffer;
    this.mInputFramebufferForDisplay.lock();
  }
  
  public int nextAvailableTextureIndex()
  {
    return 0;
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize())) {
      return;
    }
    TuSdkSize localTuSdkSize = paramTuSdkSize.copy();
    if (this.k.isTransposed())
    {
      localTuSdkSize.width = paramTuSdkSize.height;
      localTuSdkSize.height = paramTuSdkSize.width;
    }
    if (this.mInputImageSize.equals(localTuSdkSize)) {
      return;
    }
    this.mInputImageSize = localTuSdkSize;
  }
  
  public void setInputRotation(ImageOrientation paramImageOrientation, int paramInt)
  {
    if (paramImageOrientation == null) {
      return;
    }
    this.k = paramImageOrientation;
  }
  
  public void setPusherRotation(ImageOrientation paramImageOrientation, int paramInt)
  {
    setInputRotation(paramImageOrientation, paramInt);
  }
  
  public TuSdkSize maximumOutputSize()
  {
    return this.l == null ? this.mInputImageSize : this.l;
  }
  
  public void endProcessing() {}
  
  public boolean isShouldIgnoreUpdatesToThisTarget()
  {
    return false;
  }
  
  public boolean isEnabled()
  {
    return this.m;
  }
  
  public boolean wantsMonochromeInput()
  {
    return false;
  }
  
  public void setCurrentlyReceivingMonochromeInput(boolean paramBoolean) {}
  
  public void newFrameReadyInGLThread(long paramLong)
  {
    runPendingOnDrawTasks();
  }
  
  public void duplicateFrameReadyInGLThread(long paramLong)
  {
    if (this.mInputFramebufferForDisplay == null) {
      return;
    }
    a(paramLong, this.e, this.f);
  }
  
  protected void runPendingOnDrawTasks()
  {
    while (!this.a.isEmpty()) {
      try
      {
        ((Runnable)this.a.take()).run();
      }
      catch (InterruptedException localInterruptedException)
      {
        TLog.e(localInterruptedException, "%s: %s", new Object[] { "SelesSurfacePusher", getClass() });
      }
    }
  }
  
  protected boolean isOnDrawTasksEmpty()
  {
    return this.a.isEmpty();
  }
  
  protected void runOnDraw(Runnable paramRunnable)
  {
    if (paramRunnable == null) {
      return;
    }
    synchronized (this.a)
    {
      this.a.add(paramRunnable);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\output\SelesSurfacePusher.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */