package org.lasque.tusdk.core.seles.video;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.Queue;
import org.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
import org.lasque.tusdk.core.encoder.video.TuSDKHardVideoDataEncoder;
import org.lasque.tusdk.core.encoder.video.TuSDKHardVideoDataEncoderInterface;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import org.lasque.tusdk.core.gl.EglCore;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKLiveStickerImage;
import org.lasque.tusdk.core.sticker.LiveStickerPlayController;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption.WaterMarkPosition;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;

@TargetApi(18)
public class SelesSyncSurfaceTextureEncoder
  implements SelesSurfaceEncoderInterface
{
  private static final float[] a = { 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F };
  private static final float[] b = { -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F };
  protected SelesGLProgram mDisplayProgram;
  protected SelesFramebuffer mFirstInputFramebuffer;
  private int c;
  private int d;
  private int e;
  protected TuSdkSize mInputTextureSize = new TuSdkSize();
  protected FloatBuffer mVerticesBuffer;
  private FloatBuffer f;
  private RectF g = new RectF(0.0F, 0.0F, 1.0F, 1.0F);
  protected ImageOrientation mInputRotation;
  protected boolean mCurrentlyReceivingMonochromeInput;
  private SurfaceTexture h;
  private EglCore i;
  protected TuSDKHardVideoDataEncoderInterface mVideoEncoder;
  private boolean j;
  private LiveStickerPlayController k;
  private TuSDKLiveStickerImage l;
  private Bitmap m;
  private TuSdkWaterMarkOption.WaterMarkPosition n;
  private FloatBuffer o;
  private final FloatBuffer p;
  private TuSdkWaterMarkOption.WaterMarkPosition[] q = { TuSdkWaterMarkOption.WaterMarkPosition.TopLeft, TuSdkWaterMarkOption.WaterMarkPosition.BottomLeft, TuSdkWaterMarkOption.WaterMarkPosition.BottomRight, TuSdkWaterMarkOption.WaterMarkPosition.TopRight };
  private TuSDKVideoEncoderSetting r;
  private int s;
  private int t;
  private TuSDKVideoDataEncoderDelegate u;
  private final Queue<Runnable> v;
  private boolean w;
  private VideoEncoderState x = VideoEncoderState.UnKnow;
  private EGLSurface y;
  
  public SelesSyncSurfaceTextureEncoder()
  {
    a();
    float[] arrayOfFloat = { 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F };
    this.p = SelesFilter.buildBuffer(arrayOfFloat);
    setEnabled(true);
    this.v = new LinkedList();
  }
  
  public SelesSyncSurfaceTextureEncoder(EglCore paramEglCore)
  {
    this();
    this.i = paramEglCore;
  }
  
  private void a()
  {
    this.mInputRotation = ImageOrientation.Up;
    this.mVerticesBuffer = SelesFilter.buildBuffer(b);
    this.f = SelesFilter.buildBuffer(a);
  }
  
  public void setWaterMarkStickerPlayController(LiveStickerPlayController paramLiveStickerPlayController)
  {
    this.k = paramLiveStickerPlayController;
  }
  
  private void b()
  {
    this.mDisplayProgram = SelesGLProgram.create("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", "varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}");
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
    this.c = this.mDisplayProgram.attributeIndex("position");
    this.d = this.mDisplayProgram.attributeIndex("inputTextureCoordinate");
    this.e = this.mDisplayProgram.uniformIndex("inputImageTexture");
    SelesContext.setActiveShaderProgram(this.mDisplayProgram);
    GLES20.glEnableVertexAttribArray(this.c);
    GLES20.glEnableVertexAttribArray(this.d);
  }
  
  protected void initializeAttributes()
  {
    this.mDisplayProgram.addAttribute("position");
    this.mDisplayProgram.addAttribute("inputTextureCoordinate");
  }
  
  public void updateWaterMark(Bitmap paramBitmap, int paramInt, TuSdkWaterMarkOption.WaterMarkPosition paramWaterMarkPosition)
  {
    if ((paramInt == 0) || (paramInt == 360))
    {
      if ((paramBitmap != null) && (!paramBitmap.isRecycled())) {
        setWaterMarkImage(paramBitmap);
      }
      if (paramWaterMarkPosition != null) {
        setWaterMarkPosition(paramWaterMarkPosition);
      }
      return;
    }
    if ((paramBitmap != null) && (!paramBitmap.isRecycled())) {
      setWaterMarkImage(BitmapHelper.imageRotaing(paramBitmap, ImageOrientation.getValue(360 - paramInt, false)));
    }
    if (paramWaterMarkPosition == null) {
      return;
    }
    for (int i1 = 0; i1 < this.q.length; i1++) {
      if (paramWaterMarkPosition == this.q[i1])
      {
        int i2 = (i1 + paramInt / 90) % 4;
        setWaterMarkPosition(this.q[i2]);
        return;
      }
    }
  }
  
  public void destroy()
  {
    if (this.l != null)
    {
      this.l.removeSticker();
      this.l = null;
    }
  }
  
  private void a(VideoEncoderState paramVideoEncoderState)
  {
    this.x = paramVideoEncoderState;
  }
  
  public void mountAtGLThread(Runnable paramRunnable) {}
  
  public void makeCurrent()
  {
    if ((this.i == null) || (this.mVideoEncoder == null)) {
      return;
    }
    if (this.y == null) {
      this.y = this.i.createWindowSurface(this.mVideoEncoder.getInputSurface());
    }
    this.i.makeCurrent(this.y);
  }
  
  public void swapBuffers()
  {
    if ((this.i == null) || (this.y == null)) {
      return;
    }
    this.i.swapBuffers(this.y);
  }
  
  public void newFrameReady(long paramLong, int paramInt)
  {
    runPendingOnDrawTasks();
    if ((this.mInputTextureSize == null) || (!this.mInputTextureSize.isSize()))
    {
      TLog.i("mInputTextureSize error", new Object[0]);
      return;
    }
    if ((this.mVideoEncoder == null) || (!isRecording())) {
      return;
    }
    makeCurrent();
    GLES20.glBindFramebuffer(36160, 0);
    renderToTexture(this.mVerticesBuffer, this.f);
    this.i.setPresentationTime(this.y, paramLong * 1000L);
    this.mVideoEncoder.drainEncoder(false);
    swapBuffers();
    inputFramebufferUnlock();
  }
  
  public void setInputFramebuffer(SelesFramebuffer paramSelesFramebuffer, int paramInt)
  {
    if (paramSelesFramebuffer == null) {
      return;
    }
    this.mFirstInputFramebuffer = paramSelesFramebuffer;
    this.mFirstInputFramebuffer.lock();
  }
  
  public int nextAvailableTextureIndex()
  {
    return 0;
  }
  
  public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
  {
    TuSdkSize localTuSdkSize1 = rotatedSize(paramTuSdkSize, paramInt);
    if (localTuSdkSize1.minSide() < 1) {
      this.mInputTextureSize = localTuSdkSize1;
    } else if (!localTuSdkSize1.equals(this.mInputTextureSize)) {
      this.mInputTextureSize = localTuSdkSize1;
    }
    a(this.mInputTextureSize);
    TuSdkSize localTuSdkSize2 = new TuSdkSize();
    localTuSdkSize2.width = ((int)(this.mInputTextureSize.width * getCropRegion().width()));
    localTuSdkSize2.height = ((int)(this.mInputTextureSize.height * getCropRegion().height()));
    if (localTuSdkSize2.isSize()) {
      this.mInputTextureSize = localTuSdkSize2;
    } else if (!this.mInputTextureSize.equals(localTuSdkSize2)) {
      this.mInputTextureSize = localTuSdkSize2;
    }
    e();
  }
  
  public void setInputRotation(ImageOrientation paramImageOrientation, int paramInt)
  {
    c();
  }
  
  private void c()
  {
    RectF localRectF = getCropRegion();
    float f1 = localRectF.left;
    float f2 = localRectF.top;
    float f3 = localRectF.right;
    float f4 = localRectF.bottom;
    float[] arrayOfFloat = { 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F };
    arrayOfFloat[0] = f1;
    arrayOfFloat[1] = f4;
    arrayOfFloat[2] = f3;
    arrayOfFloat[3] = f4;
    arrayOfFloat[4] = f1;
    arrayOfFloat[5] = f2;
    arrayOfFloat[6] = f3;
    arrayOfFloat[7] = f2;
    if (isEnableHorizontallyFlip())
    {
      arrayOfFloat[0] = f3;
      arrayOfFloat[1] = f4;
      arrayOfFloat[2] = f1;
      arrayOfFloat[3] = f4;
      arrayOfFloat[4] = f3;
      arrayOfFloat[5] = f2;
      arrayOfFloat[6] = f1;
      arrayOfFloat[7] = f2;
    }
    this.f.clear();
    this.f.put(arrayOfFloat).position(0);
  }
  
  public TuSdkSize maximumOutputSize()
  {
    return new TuSdkSize();
  }
  
  public void endProcessing() {}
  
  public boolean isShouldIgnoreUpdatesToThisTarget()
  {
    return false;
  }
  
  public boolean isEnabled()
  {
    return this.w;
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    if (this.w == paramBoolean) {
      return;
    }
    this.w = paramBoolean;
  }
  
  public boolean wantsMonochromeInput()
  {
    return false;
  }
  
  public boolean isCurrentlyReceivingMonochromeInput()
  {
    return this.mCurrentlyReceivingMonochromeInput;
  }
  
  public void setCurrentlyReceivingMonochromeInput(boolean paramBoolean)
  {
    this.mCurrentlyReceivingMonochromeInput = paramBoolean;
  }
  
  public void setCropRegion(RectF paramRectF)
  {
    this.g = paramRectF;
  }
  
  public RectF getCropRegion()
  {
    if (this.g == null) {
      return calculateCenterRectPercent(getVideoEncoderSetting().videoSize.getRatioFloat(), this.mInputTextureSize);
    }
    return this.g;
  }
  
  protected RectF calculateCenterRectPercent(float paramFloat, TuSdkSize paramTuSdkSize)
  {
    if ((paramFloat == 0.0F) || (paramTuSdkSize == null) || (!paramTuSdkSize.isSize())) {
      return new RectF(0.0F, 0.0F, 1.0F, 1.0F);
    }
    TuSdkSize localTuSdkSize = TuSdkSize.create(paramTuSdkSize);
    localTuSdkSize.width = ((int)(paramTuSdkSize.height * paramFloat));
    Rect localRect = RectHelper.makeRectWithAspectRatioInsideRect(localTuSdkSize, new Rect(0, 0, paramTuSdkSize.width, paramTuSdkSize.height));
    float f1 = localRect.left / paramTuSdkSize.width;
    float f2 = localRect.top / paramTuSdkSize.height;
    float f3 = localRect.right / paramTuSdkSize.width;
    float f4 = localRect.bottom / paramTuSdkSize.height;
    RectF localRectF = new RectF(f1, f2, f3, f4);
    return localRectF;
  }
  
  private void a(TuSdkSize paramTuSdkSize)
  {
    c();
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
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    if (this.mFirstInputFramebuffer == null) {
      return;
    }
    SelesContext.setActiveShaderProgram(this.mDisplayProgram);
    GLES20.glActiveTexture(33986);
    GLES20.glBindTexture(3553, this.mFirstInputFramebuffer.getTexture());
    GLES20.glUniform1i(this.e, 2);
    GLES20.glVertexAttribPointer(this.c, 2, 5126, false, 0, paramFloatBuffer1);
    GLES20.glVertexAttribPointer(this.d, 2, 5126, false, 0, paramFloatBuffer2);
    GLES20.glDrawArrays(5, 0, 4);
    if ((g() != null) && (g().isEnabled())) {
      d();
    }
    GLES20.glBindTexture(3553, 0);
  }
  
  private void d()
  {
    if (this.o == null) {
      return;
    }
    GLES20.glEnable(3042);
    GLES20.glBlendFunc(772, 771);
    GLES20.glActiveTexture(33986);
    GLES20.glBindTexture(3553, this.l.getCurrentTextureID());
    GLES20.glUniform1i(this.e, 2);
    GLES20.glVertexAttribPointer(this.c, 2, 5126, false, 0, this.o);
    GLES20.glVertexAttribPointer(this.d, 2, 5126, false, 0, this.p);
    GLES20.glDrawArrays(5, 0, 4);
    GLES20.glDisable(3042);
  }
  
  private void e()
  {
    float f1 = 1.0F;
    float f2 = 1.0F;
    float f3 = 0.0F;
    float f4 = 0.0F;
    if ((this.mInputTextureSize == null) || (!this.mInputTextureSize.isSize()) || (this.l == null)) {
      return;
    }
    if ((getVideoEncoderSetting().videoSize == null) || (!getVideoEncoderSetting().videoSize.isSize())) {
      return;
    }
    TuSdkSize localTuSdkSize1 = this.l.getTextureSize();
    if ((localTuSdkSize1 == null) || (!localTuSdkSize1.isSize())) {
      return;
    }
    TuSdkSize localTuSdkSize2 = this.mInputTextureSize;
    float f5 = localTuSdkSize1.width * 1.0F / localTuSdkSize2.width;
    float f6 = localTuSdkSize1.height * 1.0F / localTuSdkSize2.height;
    float f7 = 16.0F / localTuSdkSize2.width;
    float f8 = 16.0F / localTuSdkSize2.height;
    TuSdkWaterMarkOption.WaterMarkPosition localWaterMarkPosition = getWaterMarkPosition();
    switch (5.a[localWaterMarkPosition.ordinal()])
    {
    case 1: 
      f3 = f1 - f5 - f7;
      f4 = f8;
      break;
    case 2: 
      f3 = f7;
      f4 = f2 - f6 - f8;
      break;
    case 3: 
      f3 = f7;
      f4 = f8;
      break;
    case 4: 
      f3 = f1 / 2.0F - f5 / 2.0F;
      f4 = f2 / 2.0F - f6 / 2.0F;
      break;
    case 5: 
    default: 
      f3 = f1 - f5 - f7;
      f4 = f2 - f6 - f8;
    }
    float[] arrayOfFloat = { 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F };
    arrayOfFloat[0] = (f3 * 2.0F - 1.0F);
    arrayOfFloat[1] = (f4 * 2.0F - 1.0F);
    arrayOfFloat[2] = ((f3 + f5) * 2.0F - 1.0F);
    arrayOfFloat[3] = (f4 * 2.0F - 1.0F);
    arrayOfFloat[4] = (f3 * 2.0F - 1.0F);
    arrayOfFloat[5] = ((f4 + f6) * 2.0F - 1.0F);
    arrayOfFloat[6] = ((f3 + f5) * 2.0F - 1.0F);
    arrayOfFloat[7] = ((f4 + f6) * 2.0F - 1.0F);
    if (this.o == null)
    {
      this.o = SelesFilter.buildBuffer(arrayOfFloat);
    }
    else
    {
      this.o.clear();
      this.o.put(arrayOfFloat).position(0);
    }
  }
  
  protected void inputFramebufferUnlock()
  {
    if (this.mFirstInputFramebuffer != null) {
      this.mFirstInputFramebuffer.unlock();
    }
    this.mFirstInputFramebuffer = null;
  }
  
  public void startRecording(final EGLContext paramEGLContext, SurfaceTexture paramSurfaceTexture)
  {
    if ((this.mVideoEncoder != null) && (isPaused()))
    {
      this.h = paramSurfaceTexture;
      runOnDraw(new Runnable()
      {
        public void run()
        {
          SelesSyncSurfaceTextureEncoder.this.mVideoEncoder.flush();
          SelesSyncSurfaceTextureEncoder.this.mVideoEncoder.requestKeyFrame();
          SelesSyncSurfaceTextureEncoder.a(SelesSyncSurfaceTextureEncoder.this, SelesSyncSurfaceTextureEncoder.VideoEncoderState.Recording);
          SelesSyncSurfaceTextureEncoder.this.setEnabled(true);
        }
      });
    }
    else
    {
      this.h = paramSurfaceTexture;
      runOnDraw(new Runnable()
      {
        public void run()
        {
          SelesSyncSurfaceTextureEncoder.a(SelesSyncSurfaceTextureEncoder.this, paramEGLContext);
        }
      });
    }
  }
  
  private void a(EGLContext paramEGLContext)
  {
    if (isRecording()) {
      return;
    }
    a(VideoEncoderState.Recording);
    f();
    prepareEncoder(getVideoEncoderSetting());
    if ((this.mVideoEncoder != null) && (this.mVideoEncoder.getInputSurface() != null))
    {
      this.mVideoEncoder.setDelegate(getDelegate());
      b();
      SelesContext.setActiveShaderProgram(this.mDisplayProgram);
      setEnabled(true);
    }
    else
    {
      TLog.e("SelesSurfaceTextureEncoder _startRecording failed", new Object[0]);
    }
  }
  
  public void pausdRecording()
  {
    runOnDraw(new Runnable()
    {
      public void run()
      {
        if (!SelesSyncSurfaceTextureEncoder.this.isRecording()) {
          return;
        }
        if (SelesSyncSurfaceTextureEncoder.this.isPaused()) {
          return;
        }
        SelesSyncSurfaceTextureEncoder.a(SelesSyncSurfaceTextureEncoder.this, SelesSyncSurfaceTextureEncoder.VideoEncoderState.Paused);
        SelesSyncSurfaceTextureEncoder.this.setEnabled(false);
      }
    });
  }
  
  protected void prepareEncoder(TuSDKVideoEncoderSetting paramTuSDKVideoEncoderSetting)
  {
    TuSDKHardVideoDataEncoder localTuSDKHardVideoDataEncoder = new TuSDKHardVideoDataEncoder();
    localTuSDKHardVideoDataEncoder.setDefaultVideoQuality(this.s, this.t);
    this.mVideoEncoder.initCodec(paramTuSDKVideoEncoderSetting);
    this.mVideoEncoder = localTuSDKHardVideoDataEncoder;
  }
  
  public void stopRecording()
  {
    runOnDraw(new Runnable()
    {
      public void run()
      {
        if ((!SelesSyncSurfaceTextureEncoder.this.isRecording()) && (!SelesSyncSurfaceTextureEncoder.this.isPaused())) {
          return;
        }
        SelesSyncSurfaceTextureEncoder.this.setEnabled(false);
        SelesSyncSurfaceTextureEncoder.a(SelesSyncSurfaceTextureEncoder.this, SelesSyncSurfaceTextureEncoder.VideoEncoderState.Stopped);
        SelesSyncSurfaceTextureEncoder.this.inputFramebufferUnlock();
        if (SelesSyncSurfaceTextureEncoder.this.mVideoEncoder != null) {
          SelesSyncSurfaceTextureEncoder.this.mVideoEncoder.drainEncoder(true);
        }
        SelesSyncSurfaceTextureEncoder.a(SelesSyncSurfaceTextureEncoder.this);
      }
    });
  }
  
  private void f()
  {
    if (this.mVideoEncoder != null)
    {
      this.mVideoEncoder.release();
      this.mVideoEncoder = null;
    }
    if (this.y != null)
    {
      this.i.destroySurface(this.y);
      this.y = null;
    }
  }
  
  public boolean isRecording()
  {
    return this.x == VideoEncoderState.Recording;
  }
  
  public boolean isPaused()
  {
    return this.x == VideoEncoderState.Paused;
  }
  
  public void setEnableHorizontallyFlip(boolean paramBoolean)
  {
    this.j = paramBoolean;
    c();
  }
  
  public boolean isEnableHorizontallyFlip()
  {
    return this.j;
  }
  
  public void setWaterMarkImage(Bitmap paramBitmap)
  {
    this.m = paramBitmap;
    this.l = null;
  }
  
  private TuSDKLiveStickerImage g()
  {
    if ((this.m == null) || (this.l != null)) {
      return this.l;
    }
    StickerData localStickerData = StickerData.create(0L, 0L, "", "", 1, 1, "");
    localStickerData.stickerType = 3;
    localStickerData.setImage(this.m);
    if ((this.l == null) && (this.k != null)) {
      this.l = new TuSDKLiveStickerImage(this.k.getLiveStickerLoader());
    }
    if (this.l != null) {
      this.l.updateSticker(localStickerData);
    }
    return this.l;
  }
  
  public Bitmap getWaterMarkImage()
  {
    return this.m;
  }
  
  public void setWaterMarkPosition(TuSdkWaterMarkOption.WaterMarkPosition paramWaterMarkPosition)
  {
    this.n = paramWaterMarkPosition;
    e();
  }
  
  public TuSdkWaterMarkOption.WaterMarkPosition getWaterMarkPosition()
  {
    return this.n;
  }
  
  protected long getTimestamp()
  {
    if (this.h == null) {
      return 0L;
    }
    long l1 = this.h.getTimestamp();
    if (l1 <= 0L) {
      l1 = System.nanoTime();
    }
    return l1;
  }
  
  public void setVideoEncoderSetting(TuSDKVideoEncoderSetting paramTuSDKVideoEncoderSetting)
  {
    this.r = paramTuSDKVideoEncoderSetting;
  }
  
  public TuSDKVideoEncoderSetting getVideoEncoderSetting()
  {
    if (this.r == null) {
      this.r = new TuSDKVideoEncoderSetting();
    }
    return this.r;
  }
  
  public void setDefaultVideoQuality(int paramInt1, int paramInt2)
  {
    this.s = paramInt1;
    this.t = paramInt2;
  }
  
  public void setDelegate(TuSDKVideoDataEncoderDelegate paramTuSDKVideoDataEncoderDelegate)
  {
    this.u = paramTuSDKVideoDataEncoderDelegate;
  }
  
  public TuSDKVideoDataEncoderDelegate getDelegate()
  {
    return this.u;
  }
  
  protected void runPendingOnDrawTasks()
  {
    a(this.v);
  }
  
  protected boolean isOnDrawTasksEmpty()
  {
    boolean bool = false;
    synchronized (this.v)
    {
      bool = this.v.isEmpty();
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
    this.v.add(paramRunnable);
  }
  
  private static enum VideoEncoderState
  {
    private VideoEncoderState() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\seles\video\SelesSyncSurfaceTextureEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */