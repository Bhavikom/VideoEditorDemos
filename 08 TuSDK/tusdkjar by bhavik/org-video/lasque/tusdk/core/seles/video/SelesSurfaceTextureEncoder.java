package org.lasque.tusdk.core.seles.video;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
import org.lasque.tusdk.core.encoder.video.TuSDKHardVideoDataEncoder;
import org.lasque.tusdk.core.encoder.video.TuSDKHardVideoDataEncoderInterface;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import org.lasque.tusdk.core.gl.SelesWindowsSurface;
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
public class SelesSurfaceTextureEncoder
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
  private SelesWindowsSurface i;
  protected TuSDKHardVideoDataEncoderInterface mVideoEncoder;
  private boolean j;
  private final HandlerThread k;
  private final Handler l;
  private LiveStickerPlayController m;
  private TuSDKLiveStickerImage n;
  private Bitmap o;
  private TuSdkWaterMarkOption.WaterMarkPosition p;
  private FloatBuffer q;
  private final FloatBuffer r;
  private TuSdkWaterMarkOption.WaterMarkPosition[] s = { TuSdkWaterMarkOption.WaterMarkPosition.TopLeft, TuSdkWaterMarkOption.WaterMarkPosition.BottomLeft, TuSdkWaterMarkOption.WaterMarkPosition.BottomRight, TuSdkWaterMarkOption.WaterMarkPosition.TopRight };
  private TuSDKVideoEncoderSetting t;
  private int u;
  private int v;
  private TuSDKVideoDataEncoderDelegate w;
  private boolean x;
  private VideoEncoderState y = VideoEncoderState.UnKnow;
  
  public SelesSurfaceTextureEncoder()
  {
    a();
    float[] arrayOfFloat = { 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F };
    this.r = SelesFilter.buildBuffer(arrayOfFloat);
    this.k = new HandlerThread("com.tusdk.SelesAsyncEncoder");
    this.k.start();
    this.l = new Handler(this.k.getLooper());
    setEnabled(false);
  }
  
  private void a()
  {
    this.mInputRotation = ImageOrientation.Up;
    this.mVerticesBuffer = SelesFilter.buildBuffer(b);
    this.f = SelesFilter.buildBuffer(a);
  }
  
  public void setWaterMarkStickerPlayController(LiveStickerPlayController paramLiveStickerPlayController)
  {
    this.m = paramLiveStickerPlayController;
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
    for (int i1 = 0; i1 < this.s.length; i1++) {
      if (paramWaterMarkPosition == this.s[i1])
      {
        int i2 = (i1 + paramInt / 90) % 4;
        setWaterMarkPosition(this.s[i2]);
        return;
      }
    }
  }
  
  public void destroy()
  {
    this.k.quit();
    if (this.n != null)
    {
      this.n.removeSticker();
      this.n = null;
    }
  }
  
  private void a(VideoEncoderState paramVideoEncoderState)
  {
    this.y = paramVideoEncoderState;
  }
  
  public void mountAtGLThread(Runnable paramRunnable) {}
  
  public void newFrameReady(long paramLong, int paramInt)
  {
    if ((this.mInputTextureSize == null) || (!this.mInputTextureSize.isSize())) {
      return;
    }
    if (this.mFirstInputFramebuffer == null)
    {
      TLog.d("No input fbo, skip one frame", new Object[0]);
      return;
    }
    setEnabled(false);
    GLES20.glFinish();
    a(paramLong, paramInt);
  }
  
  private void a(long paramLong, int paramInt)
  {
    if (this.mVideoEncoder == null) {
      return;
    }
    final long l1 = getTimestamp();
    this.l.post(new Runnable()
    {
      public void run()
      {
        if (!SelesSurfaceTextureEncoder.this.isRecording()) {
          return;
        }
        SelesSurfaceTextureEncoder.this.renderToTexture(SelesSurfaceTextureEncoder.this.mVerticesBuffer, SelesSurfaceTextureEncoder.a(SelesSurfaceTextureEncoder.this));
        SelesSurfaceTextureEncoder.this.mVideoEncoder.drainEncoder(false);
        SelesSurfaceTextureEncoder.this.inputFramebufferUnlock();
        SelesSurfaceTextureEncoder.b(SelesSurfaceTextureEncoder.this).setPresentationTime(l1);
        SelesSurfaceTextureEncoder.b(SelesSurfaceTextureEncoder.this).swapBuffers();
        if (SelesSurfaceTextureEncoder.this.isRecording()) {
          SelesSurfaceTextureEncoder.this.setEnabled(true);
        }
      }
    });
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
    return this.x;
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    if (this.x == paramBoolean) {
      return;
    }
    this.x = paramBoolean;
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
    if (this.q == null) {
      return;
    }
    GLES20.glEnable(3042);
    GLES20.glBlendFunc(772, 771);
    GLES20.glActiveTexture(33986);
    GLES20.glBindTexture(3553, this.n.getCurrentTextureID());
    GLES20.glUniform1i(this.e, 2);
    GLES20.glVertexAttribPointer(this.c, 2, 5126, false, 0, this.q);
    GLES20.glVertexAttribPointer(this.d, 2, 5126, false, 0, this.r);
    GLES20.glDrawArrays(5, 0, 4);
    GLES20.glDisable(3042);
  }
  
  private void e()
  {
    float f1 = 1.0F;
    float f2 = 1.0F;
    float f3 = 0.0F;
    float f4 = 0.0F;
    if ((this.mInputTextureSize == null) || (!this.mInputTextureSize.isSize()) || (this.n == null)) {
      return;
    }
    if ((getVideoEncoderSetting().videoSize == null) || (!getVideoEncoderSetting().videoSize.isSize())) {
      return;
    }
    TuSdkSize localTuSdkSize1 = this.n.getTextureSize();
    if ((localTuSdkSize1 == null) || (!localTuSdkSize1.isSize())) {
      return;
    }
    TuSdkSize localTuSdkSize2 = this.mInputTextureSize;
    float f5 = localTuSdkSize1.width * 1.0F / localTuSdkSize2.width;
    float f6 = localTuSdkSize1.height * 1.0F / localTuSdkSize2.height;
    float f7 = 16.0F / localTuSdkSize2.width;
    float f8 = 16.0F / localTuSdkSize2.height;
    TuSdkWaterMarkOption.WaterMarkPosition localWaterMarkPosition = getWaterMarkPosition();
    switch (6.a[localWaterMarkPosition.ordinal()])
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
    if (this.q == null)
    {
      this.q = SelesFilter.buildBuffer(arrayOfFloat);
    }
    else
    {
      this.q.clear();
      this.q.put(arrayOfFloat).position(0);
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
      this.l.removeCallbacksAndMessages(null);
      this.l.post(new Runnable()
      {
        public void run()
        {
          SelesSurfaceTextureEncoder.this.mVideoEncoder.flush();
          SelesSurfaceTextureEncoder.this.mVideoEncoder.requestKeyFrame();
          SelesSurfaceTextureEncoder.a(SelesSurfaceTextureEncoder.this, SelesSurfaceTextureEncoder.VideoEncoderState.Recording);
          SelesSurfaceTextureEncoder.this.setEnabled(true);
        }
      });
    }
    else
    {
      this.h = paramSurfaceTexture;
      this.l.removeCallbacksAndMessages(null);
      this.l.post(new Runnable()
      {
        public void run()
        {
          SelesSurfaceTextureEncoder.a(SelesSurfaceTextureEncoder.this, paramEGLContext);
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
      this.i = new SelesWindowsSurface(paramEGLContext, 0);
      this.i.attachSurface(this.mVideoEncoder.getInputSurface(), true);
      this.i.makeCurrent();
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
    this.l.post(new Runnable()
    {
      public void run()
      {
        if (!SelesSurfaceTextureEncoder.this.isRecording()) {
          return;
        }
        if (SelesSurfaceTextureEncoder.this.isPaused()) {
          return;
        }
        SelesSurfaceTextureEncoder.a(SelesSurfaceTextureEncoder.this, SelesSurfaceTextureEncoder.VideoEncoderState.Paused);
        SelesSurfaceTextureEncoder.this.setEnabled(false);
      }
    });
  }
  
  protected void prepareEncoder(TuSDKVideoEncoderSetting paramTuSDKVideoEncoderSetting)
  {
    TuSDKHardVideoDataEncoder localTuSDKHardVideoDataEncoder = new TuSDKHardVideoDataEncoder();
    localTuSDKHardVideoDataEncoder.setDefaultVideoQuality(this.u, this.v);
    this.mVideoEncoder.initCodec(paramTuSDKVideoEncoderSetting);
    this.mVideoEncoder = localTuSDKHardVideoDataEncoder;
  }
  
  public void stopRecording()
  {
    this.l.removeCallbacksAndMessages(null);
    this.l.post(new Runnable()
    {
      public void run()
      {
        if ((!SelesSurfaceTextureEncoder.this.isRecording()) && (!SelesSurfaceTextureEncoder.this.isPaused())) {
          return;
        }
        SelesSurfaceTextureEncoder.this.setEnabled(false);
        SelesSurfaceTextureEncoder.a(SelesSurfaceTextureEncoder.this, SelesSurfaceTextureEncoder.VideoEncoderState.Stopped);
        SelesSurfaceTextureEncoder.this.inputFramebufferUnlock();
        if (SelesSurfaceTextureEncoder.this.mVideoEncoder != null) {
          SelesSurfaceTextureEncoder.this.mVideoEncoder.drainEncoder(true);
        }
        SelesSurfaceTextureEncoder.c(SelesSurfaceTextureEncoder.this);
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
    if (this.i != null)
    {
      this.i.release();
      this.i = null;
    }
  }
  
  public boolean isRecording()
  {
    return this.y == VideoEncoderState.Recording;
  }
  
  public boolean isPaused()
  {
    return this.y == VideoEncoderState.Paused;
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
    this.o = paramBitmap;
    this.n = null;
  }
  
  private TuSDKLiveStickerImage g()
  {
    if ((this.o == null) || (this.n != null)) {
      return this.n;
    }
    StickerData localStickerData = StickerData.create(0L, 0L, "", "", 1, 1, "");
    localStickerData.stickerType = 3;
    localStickerData.setImage(this.o);
    if ((this.n == null) && (this.m != null)) {
      this.n = new TuSDKLiveStickerImage(this.m.getLiveStickerLoader());
    }
    if (this.n != null) {
      this.n.updateSticker(localStickerData);
    }
    return this.n;
  }
  
  public Bitmap getWaterMarkImage()
  {
    return this.o;
  }
  
  public void setWaterMarkPosition(TuSdkWaterMarkOption.WaterMarkPosition paramWaterMarkPosition)
  {
    this.p = paramWaterMarkPosition;
    e();
  }
  
  public TuSdkWaterMarkOption.WaterMarkPosition getWaterMarkPosition()
  {
    return this.p;
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
    this.t = paramTuSDKVideoEncoderSetting;
  }
  
  public TuSDKVideoEncoderSetting getVideoEncoderSetting()
  {
    if (this.t == null) {
      this.t = new TuSDKVideoEncoderSetting();
    }
    return this.t;
  }
  
  public void setDefaultVideoQuality(int paramInt1, int paramInt2)
  {
    this.u = paramInt1;
    this.v = paramInt2;
  }
  
  public void setDelegate(TuSDKVideoDataEncoderDelegate paramTuSDKVideoDataEncoderDelegate)
  {
    this.w = paramTuSDKVideoDataEncoderDelegate;
  }
  
  public TuSDKVideoDataEncoderDelegate getDelegate()
  {
    return this.w;
  }
  
  private static enum VideoEncoderState
  {
    private VideoEncoderState() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\seles\video\SelesSurfaceTextureEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */