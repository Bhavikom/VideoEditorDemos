package org.lasque.tusdk.core.seles.filters;

import android.graphics.PointF;
import android.graphics.Rect;
import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.TuSdkSizeF;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.TuSdkSemaphore;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.core.utils.monitor.TuSdkGLMonitor;
import org.lasque.tusdk.core.utils.monitor.TuSdkMonitor;

public class SelesFilter
  extends SelesOutInput
{
  public static final String SELES_VERTEX_SHADER = "attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}";
  public static final String SELES_PASSTHROUGH_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}";
  public static final float[] noRotationTextureCoordinates = { 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
  public static final float[] rotateLeftTextureCoordinates = { 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F };
  public static final float[] rotateRightTextureCoordinates = { 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F };
  public static final float[] verticalFlipTextureCoordinates = { 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F };
  public static final float[] horizontalFlipTextureCoordinates = { 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
  public static final float[] rotateRightVerticalFlipTextureCoordinates = { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F, 1.0F };
  public static final float[] rotateRightHorizontalFlipTextureCoordinates = { 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F };
  public static final float[] rotate180TextureCoordinates = { 1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F };
  public static final float[] imageVertices = { -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F };
  protected final Map<Integer, Runnable> mUniformStateRestorationBlocks = new HashMap();
  protected final FloatBuffer mVerticesBuffer = buildBuffer(imageVertices);
  protected final FloatBuffer mTextureBuffer = buildBuffer(noRotationTextureCoordinates);
  protected SelesFramebuffer mFirstInputFramebuffer;
  protected SelesGLProgram mFilterProgram;
  protected int mFilterPositionAttribute;
  protected int mFilterTextureCoordinateAttribute;
  protected int mFilterInputTextureUniform;
  protected float mBackgroundColorRed;
  protected float mBackgroundColorGreen;
  protected float mBackgroundColorBlue;
  protected float mBackgroundColorAlpha = 1.0F;
  protected boolean mIsEndProcessing;
  protected ImageOrientation mInputRotation = ImageOrientation.Up;
  protected boolean mCurrentlyReceivingMonochromeInput;
  private boolean a;
  private FrameProcessingDelegate b;
  protected TuSdkSemaphore mImageCaptureSemaphore = new TuSdkSemaphore(0);
  private float c = 1.0F;
  private IntBuffer d;
  private final Map<SelesContext.SelesInput, Integer> e = new LinkedHashMap();
  
  public float getScale()
  {
    if (this.c <= 0.0F) {
      this.c = 1.0F;
    }
    return this.c;
  }
  
  public void setScale(float paramFloat)
  {
    this.c = paramFloat;
  }
  
  public boolean isPreventRendering()
  {
    return this.a;
  }
  
  public void setPreventRendering(boolean paramBoolean)
  {
    this.a = paramBoolean;
  }
  
  public boolean isCurrentlyReceivingMonochromeInput()
  {
    return this.mCurrentlyReceivingMonochromeInput;
  }
  
  public void setCurrentlyReceivingMonochromeInput(boolean paramBoolean)
  {
    this.mCurrentlyReceivingMonochromeInput = paramBoolean;
  }
  
  public void setFrameProcessingDelegate(FrameProcessingDelegate paramFrameProcessingDelegate)
  {
    this.b = paramFrameProcessingDelegate;
  }
  
  public FrameProcessingDelegate getFrameProcessingDelegate()
  {
    return this.b;
  }
  
  public SelesFilter()
  {
    this("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", "varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}");
  }
  
  public SelesFilter(String paramString)
  {
    this("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", paramString);
  }
  
  public SelesFilter(final String paramString1, final String paramString2)
  {
    this.mImageCaptureSemaphore.signal();
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesFilter.a(SelesFilter.this, paramString1, paramString2);
        SelesFilter.this.onInitOnGLThread();
      }
    });
  }
  
  protected void onInitOnGLThread() {}
  
  private void a(String paramString1, String paramString2)
  {
    this.mFilterProgram = SelesContext.program(paramString1, paramString2);
    if (!this.mFilterProgram.isInitialized())
    {
      initializeAttributes();
      if (!this.mFilterProgram.link())
      {
        TLog.i("Program link log: %s", new Object[] { this.mFilterProgram.getProgramLog() });
        TLog.i("Fragment shader compile log: %s", new Object[] { this.mFilterProgram.getFragmentShaderLog() });
        TLog.i("Vertex link log: %s", new Object[] { this.mFilterProgram.getVertexShaderLog() });
        this.mFilterProgram = null;
        TLog.e("Filter shader link failed: %s", new Object[] { getClass() });
        return;
      }
    }
    this.mFilterPositionAttribute = this.mFilterProgram.attributeIndex("position");
    this.mFilterTextureCoordinateAttribute = this.mFilterProgram.attributeIndex("inputTextureCoordinate");
    this.mFilterInputTextureUniform = this.mFilterProgram.uniformIndex("inputImageTexture");
    SelesContext.setActiveShaderProgram(this.mFilterProgram);
    GLES20.glEnableVertexAttribArray(this.mFilterPositionAttribute);
    GLES20.glEnableVertexAttribArray(this.mFilterTextureCoordinateAttribute);
  }
  
  protected void initializeAttributes()
  {
    this.mFilterProgram.addAttribute("position");
    this.mFilterProgram.addAttribute("inputTextureCoordinate");
  }
  
  protected void onDestroy()
  {
    this.d = null;
    if (this.mImageCaptureSemaphore != null)
    {
      this.mImageCaptureSemaphore.release();
      this.mImageCaptureSemaphore = null;
    }
  }
  
  public void setupFilterForSize(TuSdkSize paramTuSdkSize) {}
  
  public void useNextFrameForImageCapture()
  {
    this.mUsingNextFrameForImageCapture = true;
    if (this.mImageCaptureSemaphore == null) {
      return;
    }
    if (!this.mImageCaptureSemaphore.waitSignal(0L)) {}
  }
  
  public IntBuffer imageBufferFromCurrentlyProcessedOutput(TuSdkSize paramTuSdkSize)
  {
    if (this.mImageCaptureSemaphore == null) {
      return null;
    }
    if (!this.mImageCaptureSemaphore.waitSignal(3000L)) {
      return null;
    }
    SelesFramebuffer localSelesFramebuffer = framebufferForOutput();
    IntBuffer localIntBuffer = null;
    if (localSelesFramebuffer != null)
    {
      localIntBuffer = localSelesFramebuffer.imageBufferFromFramebufferContents();
      if (paramTuSdkSize != null) {
        paramTuSdkSize.set(localSelesFramebuffer.getSize());
      }
      localSelesFramebuffer.unlock();
    }
    this.mUsingNextFrameForImageCapture = false;
    if (this.mImageCaptureSemaphore != null) {
      this.mImageCaptureSemaphore.signal();
    }
    return localIntBuffer;
  }
  
  public IntBuffer readImageBuffer()
  {
    TuSdkSize localTuSdkSize = sizeOfFBO();
    if (this.d == null) {
      this.d = IntBuffer.allocate(localTuSdkSize.width * localTuSdkSize.height);
    }
    this.d.position(0);
    GLES20.glReadPixels(0, 0, localTuSdkSize.width, localTuSdkSize.height, 6408, 5121, this.d);
    return this.d;
  }
  
  public TuSdkSize sizeOfFBO()
  {
    TuSdkSize localTuSdkSize = maximumOutputSize();
    if ((localTuSdkSize.minSide() < 1) || (this.mInputTextureSize.width < localTuSdkSize.width)) {
      return this.mInputTextureSize;
    }
    return localTuSdkSize;
  }
  
  public static float[] textureCoordinates(ImageOrientation paramImageOrientation)
  {
    if (paramImageOrientation == null) {
      paramImageOrientation = ImageOrientation.Up;
    }
    switch (17.a[paramImageOrientation.ordinal()])
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
  
  public static FloatBuffer buildBuffer(float[] paramArrayOfFloat)
  {
    FloatBuffer localFloatBuffer = ByteBuffer.allocateDirect(paramArrayOfFloat.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    localFloatBuffer.put(paramArrayOfFloat).position(0);
    return localFloatBuffer;
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    runPendingOnDrawTasks();
    if (isPreventRendering())
    {
      inputFramebufferUnlock();
      return;
    }
    SelesContext.setActiveShaderProgram(this.mFilterProgram);
    TuSdkSize localTuSdkSize = sizeOfFBO();
    SelesFramebufferCache localSelesFramebufferCache = SelesContext.sharedFramebufferCache();
    if (localSelesFramebufferCache == null) {
      return;
    }
    this.mOutputFramebuffer = localSelesFramebufferCache.fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, localTuSdkSize, getOutputTextureOptions());
    this.mOutputFramebuffer.activateFramebuffer();
    if (this.mUsingNextFrameForImageCapture) {
      this.mOutputFramebuffer.lock();
    }
    setUniformsForProgramAtIndex(0);
    GLES20.glClearColor(this.mBackgroundColorRed, this.mBackgroundColorGreen, this.mBackgroundColorBlue, this.mBackgroundColorAlpha);
    GLES20.glClear(16384);
    inputFramebufferBindTexture();
    GLES20.glEnableVertexAttribArray(this.mFilterPositionAttribute);
    GLES20.glEnableVertexAttribArray(this.mFilterTextureCoordinateAttribute);
    GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, paramFloatBuffer1);
    GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, paramFloatBuffer2);
    GLES20.glDrawArrays(5, 0, 4);
    inputFramebufferUnlock();
    cacaptureImageBuffer();
  }
  
  protected void cacaptureImageBuffer()
  {
    if ((framebufferForOutput() == null) || (!this.mUsingNextFrameForImageCapture)) {
      return;
    }
    framebufferForOutput().captureImageBufferFromFramebufferContents();
    this.mImageCaptureSemaphore.signal();
  }
  
  protected void inputFramebufferUnlock()
  {
    if (this.mFirstInputFramebuffer == null) {
      return;
    }
    this.mFirstInputFramebuffer.unlock();
  }
  
  protected void inputFramebufferBindTexture()
  {
    GLES20.glActiveTexture(33986);
    GLES20.glBindTexture(3553, this.mFirstInputFramebuffer == null ? 0 : this.mFirstInputFramebuffer.getTexture());
    GLES20.glUniform1i(this.mFilterInputTextureUniform, 2);
  }
  
  protected void informTargetsAboutNewFrame(long paramLong)
  {
    if (getFrameProcessingDelegate() != null) {
      getFrameProcessingDelegate().onFrameCompletion(this, paramLong);
    }
    this.e.clear();
    Iterator localIterator = this.mTargets.iterator();
    Object localObject;
    while (localIterator.hasNext())
    {
      localObject = (SelesContext.SelesInput)localIterator.next();
      if (((SelesContext.SelesInput)localObject).isEnabled()) {
        if (localObject != getTargetToIgnoreForUpdates())
        {
          int i = this.mTargets.indexOf(localObject);
          int j = ((Integer)this.mTargetTextureIndices.get(i)).intValue();
          this.e.put(localObject, Integer.valueOf(j));
          setInputFramebufferForTarget((SelesContext.SelesInput)localObject, j);
          ((SelesContext.SelesInput)localObject).setInputSize(outputFrameSize(), j);
        }
      }
    }
    if (framebufferForOutput() != null) {
      framebufferForOutput().unlock();
    }
    if (!this.mUsingNextFrameForImageCapture) {
      removeOutputFramebuffer();
    }
    localIterator = this.e.entrySet().iterator();
    while (localIterator.hasNext())
    {
      localObject = (Map.Entry)localIterator.next();
      ((SelesContext.SelesInput)((Map.Entry)localObject).getKey()).newFrameReady(paramLong, ((Integer)((Map.Entry)localObject).getValue()).intValue());
    }
  }
  
  public TuSdkSize outputFrameSize()
  {
    return this.mInputTextureSize;
  }
  
  public void newFrameReady(long paramLong, int paramInt)
  {
    if (this.mFirstInputFramebuffer == null) {
      return;
    }
    this.mTextureBuffer.clear();
    this.mTextureBuffer.put(textureCoordinates(this.mInputRotation)).position(0);
    renderToTexture(this.mVerticesBuffer, this.mTextureBuffer);
    informTargetsAboutNewFrame(paramLong);
  }
  
  public int nextAvailableTextureIndex()
  {
    return 0;
  }
  
  public void setInputFramebuffer(SelesFramebuffer paramSelesFramebuffer, int paramInt)
  {
    if (paramSelesFramebuffer == null) {
      return;
    }
    this.mFirstInputFramebuffer = paramSelesFramebuffer;
    this.mFirstInputFramebuffer.lock();
  }
  
  public TuSdkSize rotatedSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    TuSdkSize localTuSdkSize = paramTuSdkSize.copy();
    if (this.mInputRotation.isTransposed())
    {
      localTuSdkSize.width = paramTuSdkSize.height;
      localTuSdkSize.height = paramTuSdkSize.width;
    }
    return localTuSdkSize;
  }
  
  public PointF rotatedPoint(PointF paramPointF, ImageOrientation paramImageOrientation)
  {
    PointF localPointF = new PointF();
    switch (17.a[paramImageOrientation.ordinal()])
    {
    case 8: 
      return paramPointF;
    case 4: 
      localPointF.x = (1.0F - paramPointF.x);
      localPointF.y = paramPointF.y;
      break;
    case 3: 
      localPointF.x = paramPointF.x;
      localPointF.y = (1.0F - paramPointF.y);
      break;
    case 1: 
      localPointF.x = (1.0F - paramPointF.y);
      localPointF.y = paramPointF.x;
      break;
    case 2: 
      localPointF.x = paramPointF.y;
      localPointF.y = (1.0F - paramPointF.x);
      break;
    case 5: 
      localPointF.x = paramPointF.y;
      localPointF.y = paramPointF.x;
      break;
    case 6: 
      localPointF.x = (1.0F - paramPointF.y);
      localPointF.y = (1.0F - paramPointF.x);
      break;
    case 7: 
      localPointF.x = (1.0F - paramPointF.x);
      localPointF.y = (1.0F - paramPointF.y);
    }
    return localPointF;
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    if (isPreventRendering()) {
      return;
    }
    if (paramTuSdkSize != null) {
      paramTuSdkSize = paramTuSdkSize.scale(getScale());
    }
    if (this.mOverrideInputSize)
    {
      if ((this.mForcedMaximumSize == null) || (this.mForcedMaximumSize.minSide() < 1))
      {
        setupFilterForSize(sizeOfFBO());
        return;
      }
      localObject = RectHelper.makeRectWithAspectRatioInsideRect(paramTuSdkSize, new Rect(0, 0, this.mForcedMaximumSize.width, this.mForcedMaximumSize.height));
      paramTuSdkSize = TuSdkSize.create((Rect)localObject);
    }
    Object localObject = rotatedSize(paramTuSdkSize, paramInt);
    if (((TuSdkSize)localObject).minSide() < 1) {
      this.mInputTextureSize = ((TuSdkSize)localObject);
    } else if (!((TuSdkSize)localObject).equals(this.mInputTextureSize)) {
      this.mInputTextureSize = ((TuSdkSize)localObject);
    }
    setupFilterForSize(sizeOfFBO());
  }
  
  public void setInputRotation(ImageOrientation paramImageOrientation, int paramInt)
  {
    this.mInputRotation = paramImageOrientation;
  }
  
  public void forceProcessingAtSize(TuSdkSize paramTuSdkSize)
  {
    if ((paramTuSdkSize != null) && (paramTuSdkSize.minSide() > 0))
    {
      this.mOverrideInputSize = true;
      this.mInputTextureSize = paramTuSdkSize.copy();
      this.mForcedMaximumSize = new TuSdkSize();
    }
    else
    {
      this.mOverrideInputSize = false;
    }
  }
  
  public void forceProcessingAtSizeRespectingAspectRatio(TuSdkSize paramTuSdkSize)
  {
    if ((paramTuSdkSize != null) && (paramTuSdkSize.minSide() > 0))
    {
      this.mOverrideInputSize = true;
      this.mForcedMaximumSize = paramTuSdkSize.copy();
    }
    else
    {
      this.mOverrideInputSize = false;
      this.mInputTextureSize = new TuSdkSize();
      this.mForcedMaximumSize = new TuSdkSize();
    }
  }
  
  public TuSdkSize maximumOutputSize()
  {
    return new TuSdkSize();
  }
  
  public void endProcessing()
  {
    if (!this.mIsEndProcessing)
    {
      this.mIsEndProcessing = true;
      Iterator localIterator = this.mTargets.iterator();
      while (localIterator.hasNext())
      {
        SelesContext.SelesInput localSelesInput = (SelesContext.SelesInput)localIterator.next();
        localSelesInput.endProcessing();
      }
    }
  }
  
  public boolean wantsMonochromeInput()
  {
    return false;
  }
  
  protected void checkGLError(String paramString)
  {
    try
    {
      TuSdkMonitor.glMonitor().checkGL(paramString);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  protected void captureFilterImage(String paramString, int paramInt1, int paramInt2)
  {
    try
    {
      checkGLError(paramString + " captureFilterImage");
      TuSdkMonitor.glMonitor().checkGLFrameImage(paramString, paramInt1, paramInt2);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void setBackgroundColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    this.mBackgroundColorRed = paramFloat1;
    this.mBackgroundColorGreen = paramFloat2;
    this.mBackgroundColorBlue = paramFloat3;
    this.mBackgroundColorAlpha = paramFloat4;
  }
  
  public void setInteger(final int paramInt, final String paramString)
  {
    runOnDraw(new Runnable()
    {
      public void run()
      {
        int i = SelesFilter.this.mFilterProgram.uniformIndex(paramString);
        SelesFilter.this.setInteger(paramInt, i, SelesFilter.this.mFilterProgram);
      }
    });
  }
  
  public void setFloat(final float paramFloat, final String paramString)
  {
    runOnDraw(new Runnable()
    {
      public void run()
      {
        int i = SelesFilter.this.mFilterProgram.uniformIndex(paramString);
        SelesFilter.this.setFloat(paramFloat, i, SelesFilter.this.mFilterProgram);
      }
    });
  }
  
  public void setSize(final TuSdkSizeF paramTuSdkSizeF, final String paramString)
  {
    runOnDraw(new Runnable()
    {
      public void run()
      {
        int i = SelesFilter.this.mFilterProgram.uniformIndex(paramString);
        SelesFilter.this.setSize(paramTuSdkSizeF, i, SelesFilter.this.mFilterProgram);
      }
    });
  }
  
  public void setPoint(final PointF paramPointF, final String paramString)
  {
    runOnDraw(new Runnable()
    {
      public void run()
      {
        int i = SelesFilter.this.mFilterProgram.uniformIndex(paramString);
        SelesFilter.this.setPoint(paramPointF, i, SelesFilter.this.mFilterProgram);
      }
    });
  }
  
  public void setFloatVec3(final float[] paramArrayOfFloat, final String paramString)
  {
    runOnDraw(new Runnable()
    {
      public void run()
      {
        int i = SelesFilter.this.mFilterProgram.uniformIndex(paramString);
        SelesFilter.this.setVec3(paramArrayOfFloat, i, SelesFilter.this.mFilterProgram);
      }
    });
  }
  
  public void setFloatVec4(final float[] paramArrayOfFloat, final String paramString)
  {
    runOnDraw(new Runnable()
    {
      public void run()
      {
        int i = SelesFilter.this.mFilterProgram.uniformIndex(paramString);
        SelesFilter.this.setVec4(paramArrayOfFloat, i, SelesFilter.this.mFilterProgram);
      }
    });
  }
  
  public void setFloatArray(final float[] paramArrayOfFloat, final String paramString)
  {
    runOnDraw(new Runnable()
    {
      public void run()
      {
        int i = SelesFilter.this.mFilterProgram.uniformIndex(paramString);
        SelesFilter.this.setFloatArray(paramArrayOfFloat, i, SelesFilter.this.mFilterProgram);
      }
    });
  }
  
  public void setMatrix3f(final float[] paramArrayOfFloat, final int paramInt, final SelesGLProgram paramSelesGLProgram)
  {
    if (paramSelesGLProgram == null) {
      return;
    }
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesContext.setActiveShaderProgram(paramSelesGLProgram);
        SelesFilter.this.setAndExecuteUniformStateCallbackAtIndex(paramInt, paramSelesGLProgram, new Runnable()
        {
          public void run()
          {
            GLES20.glUniformMatrix3fv(SelesFilter.9.this.b, 1, false, SelesFilter.9.this.c, 0);
          }
        });
      }
    });
  }
  
  public void setMatrix4f(final float[] paramArrayOfFloat, final int paramInt, final SelesGLProgram paramSelesGLProgram)
  {
    if (paramSelesGLProgram == null) {
      return;
    }
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesContext.setActiveShaderProgram(paramSelesGLProgram);
        SelesFilter.this.setAndExecuteUniformStateCallbackAtIndex(paramInt, paramSelesGLProgram, new Runnable()
        {
          public void run()
          {
            GLES20.glUniformMatrix4fv(SelesFilter.10.this.b, 1, false, SelesFilter.10.this.c, 0);
          }
        });
      }
    });
  }
  
  public void setFloat(final float paramFloat, final int paramInt, final SelesGLProgram paramSelesGLProgram)
  {
    if (paramSelesGLProgram == null) {
      return;
    }
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesContext.setActiveShaderProgram(paramSelesGLProgram);
        SelesFilter.this.setAndExecuteUniformStateCallbackAtIndex(paramInt, paramSelesGLProgram, new Runnable()
        {
          public void run()
          {
            GLES20.glUniform1f(SelesFilter.11.this.b, SelesFilter.11.this.c);
          }
        });
      }
    });
  }
  
  public void setPoint(PointF paramPointF, int paramInt, SelesGLProgram paramSelesGLProgram)
  {
    float[] arrayOfFloat = new float[2];
    arrayOfFloat[0] = paramPointF.x;
    arrayOfFloat[1] = paramPointF.y;
    setVec2(arrayOfFloat, paramInt, paramSelesGLProgram);
  }
  
  public void setSize(TuSdkSizeF paramTuSdkSizeF, int paramInt, SelesGLProgram paramSelesGLProgram)
  {
    float[] arrayOfFloat = new float[2];
    arrayOfFloat[0] = paramTuSdkSizeF.width;
    arrayOfFloat[1] = paramTuSdkSizeF.height;
    setVec2(arrayOfFloat, paramInt, paramSelesGLProgram);
  }
  
  public void setVec2(final float[] paramArrayOfFloat, final int paramInt, final SelesGLProgram paramSelesGLProgram)
  {
    if (paramSelesGLProgram == null) {
      return;
    }
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesContext.setActiveShaderProgram(paramSelesGLProgram);
        SelesFilter.this.setAndExecuteUniformStateCallbackAtIndex(paramInt, paramSelesGLProgram, new Runnable()
        {
          public void run()
          {
            GLES20.glUniform2fv(SelesFilter.12.this.b, 1, SelesFilter.12.this.c, 0);
          }
        });
      }
    });
  }
  
  public void setVec3(final float[] paramArrayOfFloat, final int paramInt, final SelesGLProgram paramSelesGLProgram)
  {
    if (paramSelesGLProgram == null) {
      return;
    }
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesContext.setActiveShaderProgram(paramSelesGLProgram);
        SelesFilter.this.setAndExecuteUniformStateCallbackAtIndex(paramInt, paramSelesGLProgram, new Runnable()
        {
          public void run()
          {
            GLES20.glUniform3fv(SelesFilter.13.this.b, 1, SelesFilter.13.this.c, 0);
          }
        });
      }
    });
  }
  
  public void setVec4(final float[] paramArrayOfFloat, final int paramInt, final SelesGLProgram paramSelesGLProgram)
  {
    if (paramSelesGLProgram == null) {
      return;
    }
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesContext.setActiveShaderProgram(paramSelesGLProgram);
        SelesFilter.this.setAndExecuteUniformStateCallbackAtIndex(paramInt, paramSelesGLProgram, new Runnable()
        {
          public void run()
          {
            GLES20.glUniform4fv(SelesFilter.14.this.b, 1, SelesFilter.14.this.c, 0);
          }
        });
      }
    });
  }
  
  public void setFloatArray(final float[] paramArrayOfFloat, final int paramInt, final SelesGLProgram paramSelesGLProgram)
  {
    if (paramSelesGLProgram == null) {
      return;
    }
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesContext.setActiveShaderProgram(paramSelesGLProgram);
        SelesFilter.this.setAndExecuteUniformStateCallbackAtIndex(paramInt, paramSelesGLProgram, new Runnable()
        {
          public void run()
          {
            GLES20.glUniform1fv(SelesFilter.15.this.b, SelesFilter.15.this.c.length, SelesFilter.15.this.c, 0);
          }
        });
      }
    });
  }
  
  public void setInteger(final int paramInt1, final int paramInt2, final SelesGLProgram paramSelesGLProgram)
  {
    if (paramSelesGLProgram == null) {
      return;
    }
    runOnDraw(new Runnable()
    {
      public void run()
      {
        SelesContext.setActiveShaderProgram(paramSelesGLProgram);
        SelesFilter.this.setAndExecuteUniformStateCallbackAtIndex(paramInt2, paramSelesGLProgram, new Runnable()
        {
          public void run()
          {
            GLES20.glUniform1i(SelesFilter.16.this.b, SelesFilter.16.this.c);
          }
        });
      }
    });
  }
  
  protected void setAndExecuteUniformStateCallbackAtIndex(int paramInt, SelesGLProgram paramSelesGLProgram, Runnable paramRunnable)
  {
    if (paramRunnable == null) {
      return;
    }
    this.mUniformStateRestorationBlocks.put(Integer.valueOf(paramInt), paramRunnable);
    paramRunnable.run();
  }
  
  protected void setUniformsForProgramAtIndex(int paramInt)
  {
    Iterator localIterator = this.mUniformStateRestorationBlocks.values().iterator();
    while (localIterator.hasNext())
    {
      Runnable localRunnable = (Runnable)localIterator.next();
      localRunnable.run();
    }
  }
  
  public static abstract interface FrameProcessingDelegate
  {
    public abstract void onFrameCompletion(SelesFilter paramSelesFilter, long paramLong);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\filters\SelesFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */