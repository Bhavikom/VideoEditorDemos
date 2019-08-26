package org.lasque.tusdk.core.media.codec.encoder;

import android.annotation.TargetApi;
import android.graphics.RectF;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.opengl.EGLContext;
import android.opengl.GLES20;
import java.nio.ByteBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import org.lasque.tusdk.core.media.codec.TuSdkCodecOutput.TuSdkEncodecOutput;
import org.lasque.tusdk.core.media.codec.TuSdkEncodecOperation;
import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import org.lasque.tusdk.core.media.codec.sync.TuSdkVideoEncodecSync;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoSurfaceEncodecOperation;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoSurfaceEncodecOperationPatch;
import org.lasque.tusdk.core.seles.egl.SelesRenderer;
import org.lasque.tusdk.core.seles.egl.SelesVirtualDisplay;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
import org.lasque.tusdk.core.seles.output.SelesSurfaceDisplay;
import org.lasque.tusdk.core.seles.output.SelesSurfacePusher;
import org.lasque.tusdk.core.seles.output.SelesSurfacePusherAsync;
import org.lasque.tusdk.core.seles.sources.SelesWatermark;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

@TargetApi(18)
public class TuSdkVideoSurfaceEncoder
  implements TuSdkEncodeSurface
{
  private int a = -1;
  private boolean b = false;
  private final SelesVerticeCoordinateBuilder c = new SelesVerticeCoordinateCropBuilderImpl(true);
  private TuSdkSize d;
  private SelesVirtualDisplay e;
  private SelesSurfaceDisplay f;
  private TuSdkFilterBridge g;
  private boolean h = false;
  private TuSdkSurfaceRender i;
  private TuSdkVideoSurfaceEncodecOperation j;
  private TuSdkVideoSurfaceEncoderListener k;
  private TuSdkVideoEncodecSync l;
  private SelesWatermark m;
  private ImageOrientation n = ImageOrientation.Up;
  private SelesRenderer o = new SelesRenderer()
  {
    public void onSurfaceDestory(GL10 paramAnonymousGL10)
    {
      TLog.d("%s on VirtualDisplay Thread will exit", new Object[] { "TuSdkVideoSurfaceEncoder" });
      if (TuSdkVideoSurfaceEncoder.a(TuSdkVideoSurfaceEncoder.this) != null) {
        TuSdkVideoSurfaceEncoder.a(TuSdkVideoSurfaceEncoder.this).onSurfaceDestory();
      }
      TuSdkVideoSurfaceEncoder.b(TuSdkVideoSurfaceEncoder.this).onSurfaceDestory(paramAnonymousGL10);
    }
    
    public void onSurfaceCreated(GL10 paramAnonymousGL10, EGLConfig paramAnonymousEGLConfig)
    {
      GLES20.glDisable(2929);
      TuSdkVideoSurfaceEncoder.b(TuSdkVideoSurfaceEncoder.this).onSurfaceCreated(paramAnonymousGL10, paramAnonymousEGLConfig);
      if (TuSdkVideoSurfaceEncoder.a(TuSdkVideoSurfaceEncoder.this) != null) {
        TuSdkVideoSurfaceEncoder.a(TuSdkVideoSurfaceEncoder.this).onSurfaceCreated();
      }
      if (TuSdkVideoSurfaceEncoder.c(TuSdkVideoSurfaceEncoder.this))
      {
        TLog.d("%s enable encodec compatibility mode", new Object[] { "TuSdkVideoSurfaceEncoder" });
        TuSdkVideoSurfaceEncoder.d(TuSdkVideoSurfaceEncoder.this).swapBuffers(0L);
      }
    }
    
    public void onSurfaceChanged(GL10 paramAnonymousGL10, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      GLES20.glViewport(0, 0, paramAnonymousInt1, paramAnonymousInt2);
      if (TuSdkVideoSurfaceEncoder.a(TuSdkVideoSurfaceEncoder.this) != null) {
        TuSdkVideoSurfaceEncoder.a(TuSdkVideoSurfaceEncoder.this).onSurfaceChanged(paramAnonymousInt1, paramAnonymousInt2);
      }
      TuSdkVideoSurfaceEncoder.b(TuSdkVideoSurfaceEncoder.this).onSurfaceChanged(paramAnonymousGL10, paramAnonymousInt1, paramAnonymousInt2);
    }
    
    public void onDrawFrame(GL10 paramAnonymousGL10)
    {
      GLES20.glClear(16640);
      if ((TuSdkVideoSurfaceEncoder.e(TuSdkVideoSurfaceEncoder.this) == null) || (TuSdkVideoSurfaceEncoder.d(TuSdkVideoSurfaceEncoder.this) == null) || (TuSdkVideoSurfaceEncoder.f(TuSdkVideoSurfaceEncoder.this) != 0)) {
        return;
      }
      long l = TuSdkVideoSurfaceEncoder.d(TuSdkVideoSurfaceEncoder.this).lastRenderTimestampNs();
      boolean bool = (TuSdkVideoSurfaceEncoder.c(TuSdkVideoSurfaceEncoder.this)) && (l == 0L);
      TuSdkVideoSurfaceEncoder.b(TuSdkVideoSurfaceEncoder.this).onEncoderDrawFrame(l, bool);
    }
  };
  private TuSdkCodecOutput.TuSdkEncodecOutput p = new TuSdkCodecOutput.TuSdkEncodecOutput()
  {
    private boolean b = false;
    
    public void outputFormatChanged(MediaFormat paramAnonymousMediaFormat)
    {
      TLog.d("%s outputFormatChanged: %s", new Object[] { "TuSdkVideoSurfaceEncoder", paramAnonymousMediaFormat });
      if ((TuSdkVideoSurfaceEncoder.g(TuSdkVideoSurfaceEncoder.this) != null) && (TuSdkVideoSurfaceEncoder.h(TuSdkVideoSurfaceEncoder.this) != null)) {
        TuSdkVideoSurfaceEncoder.g(TuSdkVideoSurfaceEncoder.this).syncEncodecVideoInfo(TuSdkVideoSurfaceEncoder.h(TuSdkVideoSurfaceEncoder.this).getVideoInfo());
      }
    }
    
    public void processOutputBuffer(TuSdkMediaMuxer paramAnonymousTuSdkMediaMuxer, int paramAnonymousInt, ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TuSdkVideoSurfaceEncoder.c(TuSdkVideoSurfaceEncoder.this))
      {
        if (paramAnonymousBufferInfo.presentationTimeUs < 1L)
        {
          TuSdkVideoSurfaceEncoder.h(TuSdkVideoSurfaceEncoder.this).requestKeyFrame();
          this.b = true;
          return;
        }
        if (this.b)
        {
          this.b = false;
          MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
          localBufferInfo.set(paramAnonymousBufferInfo.offset, paramAnonymousBufferInfo.size, 0L, paramAnonymousBufferInfo.flags);
          paramAnonymousBufferInfo = localBufferInfo;
          TuSdkVideoSurfaceEncoder.a(TuSdkVideoSurfaceEncoder.this, false);
        }
      }
      if (TuSdkVideoSurfaceEncoder.g(TuSdkVideoSurfaceEncoder.this) != null) {
        TuSdkVideoSurfaceEncoder.g(TuSdkVideoSurfaceEncoder.this).syncVideoEncodecOutputBuffer(paramAnonymousTuSdkMediaMuxer, paramAnonymousInt, paramAnonymousByteBuffer, paramAnonymousBufferInfo);
      } else {
        TuSdkMediaUtils.processOutputBuffer(paramAnonymousTuSdkMediaMuxer, paramAnonymousInt, paramAnonymousByteBuffer, paramAnonymousBufferInfo);
      }
    }
    
    public void updated(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TuSdkVideoSurfaceEncoder.g(TuSdkVideoSurfaceEncoder.this) != null) {
        TuSdkVideoSurfaceEncoder.g(TuSdkVideoSurfaceEncoder.this).syncVideoEncodecUpdated(paramAnonymousBufferInfo);
      }
      TuSdkVideoSurfaceEncoder.b(TuSdkVideoSurfaceEncoder.this).onEncoderUpdated(paramAnonymousBufferInfo);
    }
    
    public boolean updatedToEOS(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TuSdkVideoSurfaceEncoder.g(TuSdkVideoSurfaceEncoder.this) != null) {
        TuSdkVideoSurfaceEncoder.g(TuSdkVideoSurfaceEncoder.this).syncVideoEncodecCompleted();
      }
      TuSdkVideoSurfaceEncoder.b(TuSdkVideoSurfaceEncoder.this).onEncoderCompleted(null);
      return true;
    }
    
    public void onCatchedException(Exception paramAnonymousException)
    {
      if (TuSdkVideoSurfaceEncoder.g(TuSdkVideoSurfaceEncoder.this) != null) {
        TuSdkVideoSurfaceEncoder.g(TuSdkVideoSurfaceEncoder.this).syncVideoEncodecCompleted();
      }
      TuSdkVideoSurfaceEncoder.b(TuSdkVideoSurfaceEncoder.this).onEncoderCompleted(paramAnonymousException);
    }
  };
  
  public SelesVirtualDisplay getVirtualDisplay()
  {
    return this.e;
  }
  
  public void setOutputOrientation(ImageOrientation paramImageOrientation)
  {
    if (paramImageOrientation == null) {
      return;
    }
    this.n = paramImageOrientation;
    if (this.f != null) {
      this.f.setInputRotation(paramImageOrientation, 0);
    }
  }
  
  public void setCanvasRect(RectF paramRectF)
  {
    if (paramRectF == null) {
      return;
    }
    this.c.setCanvasRect(paramRectF);
  }
  
  public void setListener(TuSdkVideoSurfaceEncoderListener paramTuSdkVideoSurfaceEncoderListener)
  {
    if (paramTuSdkVideoSurfaceEncoderListener == null)
    {
      TLog.w("%s setListener can not empty.", new Object[] { "TuSdkVideoSurfaceEncoder" });
      return;
    }
    if (this.a != -1)
    {
      TLog.w("%s setListener need before prepare.", new Object[] { "TuSdkVideoSurfaceEncoder" });
      return;
    }
    this.k = paramTuSdkVideoSurfaceEncoderListener;
  }
  
  public int setOutputFormat(MediaFormat paramMediaFormat)
  {
    if (this.a != -1)
    {
      TLog.w("%s setOutputFormat need before prepare.", new Object[] { "TuSdkVideoSurfaceEncoder" });
      return -1;
    }
    this.j = new TuSdkVideoSurfaceEncodecOperation(this.p);
    int i1 = this.j.setMediaFormat(paramMediaFormat);
    if (i1 != 0)
    {
      this.j = null;
      TLog.w("%s setOutputFormat has a error code: %d, %s", new Object[] { "TuSdkVideoSurfaceEncoder", Integer.valueOf(i1), paramMediaFormat });
      return i1;
    }
    this.d = TuSdkMediaFormat.getVideoKeySize(paramMediaFormat);
    return 0;
  }
  
  public void setMediaSync(TuSdkVideoEncodecSync paramTuSdkVideoEncodecSync)
  {
    this.l = paramTuSdkVideoEncodecSync;
  }
  
  public void setSurfaceRender(TuSdkSurfaceRender paramTuSdkSurfaceRender)
  {
    this.i = paramTuSdkSurfaceRender;
    if (this.g != null) {
      this.g.setSurfaceDraw(this.i);
    }
  }
  
  public TuSdkFilterBridge getFilterBridge()
  {
    if (this.g == null)
    {
      this.g = new TuSdkFilterBridge();
      this.h = true;
    }
    if ((TLog.LOG_VIDEO_ENCODEC_INFO) && (!this.h)) {
      TLog.d("%s used external bridge.", new Object[] { "TuSdkVideoSurfaceEncoder" });
    }
    return this.g;
  }
  
  public void setFilterBridge(TuSdkFilterBridge paramTuSdkFilterBridge)
  {
    if ((paramTuSdkFilterBridge == null) || (this.g != null)) {
      return;
    }
    this.g = paramTuSdkFilterBridge;
  }
  
  public void disconnect()
  {
    if ((this.g == null) || (this.f == null) || (this.h)) {
      return;
    }
    this.g.removeTarget(this.f);
    this.g = null;
  }
  
  public TuSdkEncodecOperation getOperation()
  {
    if (this.j == null) {
      TLog.w("%s getOperation need setOutputFormat first", new Object[] { "TuSdkVideoSurfaceEncoder" });
    }
    return this.j;
  }
  
  public TuSdkSize getOutputSize()
  {
    if (this.d == null) {
      TLog.w("%s getOutputSize need setOutputFormat first", new Object[] { "TuSdkVideoSurfaceEncoder" });
    }
    return this.d;
  }
  
  public void release()
  {
    if (this.a == 1) {
      return;
    }
    this.a = 1;
    this.j = null;
    if (this.f != null) {
      this.f.destroy();
    }
    if ((this.g != null) && (this.h)) {
      this.g.destroy();
    }
    if (this.e != null)
    {
      this.e.release();
      this.e = null;
    }
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public void requestKeyFrame()
  {
    if (this.j != null) {
      this.j.requestKeyFrame();
    }
  }
  
  public void signalEndOfInputStream()
  {
    if (this.j != null) {
      this.j.signalEndOfInputStream();
    }
  }
  
  public void requestRender(long paramLong)
  {
    if (this.e != null) {
      this.e.requestRender(paramLong);
    } else {
      TLog.e("%s video virtualDisplay is null!", new Object[] { "TuSdkVideoSurfaceEncoder" });
    }
  }
  
  public boolean requestEncode(long paramLong)
  {
    newFrameReadyInGLThread(paramLong);
    return swapBuffers(paramLong);
  }
  
  public void newFrameReadyInGLThread(long paramLong)
  {
    if (this.a != 0) {
      return;
    }
    if (this.f != null) {
      this.f.newFrameReadyInGLThread(paramLong);
    }
  }
  
  public void duplicateFrameReadyInGLThread(long paramLong)
  {
    if (this.a != 0) {
      return;
    }
    if (this.f != null) {
      this.f.duplicateFrameReadyInGLThread(paramLong);
    }
  }
  
  public boolean swapBuffers(long paramLong)
  {
    if (this.a != 0) {
      return false;
    }
    if (this.j != null) {
      this.j.notifyNewFrameReady();
    }
    if (this.e != null) {
      return this.e.swapBuffers(paramLong);
    }
    return false;
  }
  
  public void setPause(boolean paramBoolean)
  {
    if (this.a != 0) {
      return;
    }
    if (this.f != null) {
      this.f.setEnabled(!paramBoolean);
    }
  }
  
  public void setWatermark(SelesWatermark paramSelesWatermark)
  {
    this.m = paramSelesWatermark;
  }
  
  public void flush()
  {
    if (this.j != null) {
      this.j.flush();
    }
  }
  
  public boolean prepare(EGLContext paramEGLContext, boolean paramBoolean)
  {
    if (this.a > -1)
    {
      TLog.w("%s has prepared.", new Object[] { "TuSdkVideoSurfaceEncoder" });
      return false;
    }
    if (this.j == null)
    {
      TLog.w("%s run need set Output Video Format.", new Object[] { "TuSdkVideoSurfaceEncoder" });
      return false;
    }
    if (this.k == null)
    {
      TLog.w("%s need setListener first.", new Object[] { "TuSdkVideoSurfaceEncoder" });
      return false;
    }
    if (!paramBoolean) {
      this.b = this.j.getCodecPatch().isEnableCompatibilityMode();
    }
    this.c.setOutputSize(this.d);
    this.e = new SelesVirtualDisplay();
    this.e.setRenderer(this.o);
    this.e.buildWindowContext(paramEGLContext);
    this.e.attachWindowSurface(this.j.getSurface(), true);
    if (paramEGLContext == null) {
      this.f = new SelesSurfacePusher();
    } else {
      this.f = new SelesSurfacePusherAsync();
    }
    this.f.setPusherRotation(this.n, 0);
    this.f.setTextureCoordinateBuilder(this.c);
    if (this.m != null) {
      this.f.setWatermark(this.m);
    }
    getFilterBridge().addTarget(this.f, 0);
    this.a = 0;
    return true;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\encoder\TuSdkVideoSurfaceEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */