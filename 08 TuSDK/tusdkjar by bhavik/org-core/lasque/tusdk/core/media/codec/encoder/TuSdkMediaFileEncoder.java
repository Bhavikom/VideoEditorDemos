package org.lasque.tusdk.core.media.codec.encoder;

import android.graphics.RectF;
import android.media.MediaFormat;
import android.opengl.EGLContext;
import java.io.File;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import org.lasque.tusdk.core.media.codec.TuSdkEncodecOperation;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaEncodecSync;
import org.lasque.tusdk.core.seles.sources.SelesWatermark;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSdkMediaFileEncoder
{
  public static final int TRANS_STATE_UNINITIALIZED = -1;
  public static final int TRANS_STATE_STARTED = 0;
  public static final int TRANS_STATE_STOPPED = 1;
  private int a = -1;
  private TuSdkMediaDataSource b;
  private File c;
  private TuSdkSurfaceRender d;
  private TuSdkAudioRender e;
  private TuSdkVideoSurfaceEncoder f;
  private TuSdkAudioEncoder g;
  private TuSdkMediaFileMuxer h;
  private TuSdkMediaEncodecSync i;
  private TuSdkEncoderListener j;
  private TuSdkVideoSurfaceEncoderListener k;
  private ImageOrientation l;
  private RectF m;
  
  public TuSdkAudioEncoder getAudioEncoder()
  {
    return this.g;
  }
  
  public TuSdkVideoSurfaceEncoder getVideoEncoder()
  {
    return this.f;
  }
  
  public boolean hasVideoEncoder()
  {
    return this.f != null;
  }
  
  public boolean hasAudioEncoder()
  {
    return this.g != null;
  }
  
  public TuSdkMediaDataSource getOutputDataSource()
  {
    return this.b;
  }
  
  public TuSdkFilterBridge getFilterBridge()
  {
    if (this.f == null)
    {
      TLog.w("%s getFilterBridge need setOutputVideoFormat first.", new Object[] { "TuSdkMediaFileEncoder" });
      return null;
    }
    return this.f.getFilterBridge();
  }
  
  public void setFilterBridge(TuSdkFilterBridge paramTuSdkFilterBridge)
  {
    if (this.f == null)
    {
      TLog.w("%s setFilterBridge need setOutputVideoFormat first.", new Object[] { "TuSdkMediaFileEncoder" });
      return;
    }
    this.f.setFilterBridge(paramTuSdkFilterBridge);
  }
  
  public void disconnect()
  {
    if (this.f == null)
    {
      TLog.w("%s disconnect need setOutputVideoFormat first.", new Object[] { "TuSdkMediaFileEncoder" });
      return;
    }
    this.f.disconnect();
  }
  
  public void setSurfacePause(boolean paramBoolean)
  {
    if (this.f == null)
    {
      TLog.w("%s setPause need setOutputVideoFormat first.", new Object[] { "TuSdkMediaFileEncoder" });
      return;
    }
    this.f.setPause(paramBoolean);
  }
  
  public TuSdkSize getOutputSize()
  {
    if (this.f == null) {
      TLog.w("%s getOutputSize need setOutputVideoFormat first", new Object[] { "TuSdkMediaFileEncoder" });
    }
    return this.f.getOutputSize();
  }
  
  public TuSdkEncodecOperation getVideoOperation()
  {
    if (this.f == null) {
      TLog.w("%s getAudioOperation need setOutputAudioFormat first", new Object[] { "TuSdkMediaFileEncoder" });
    }
    return this.f.getOperation();
  }
  
  public TuSdkAudioEncodecOperation getAudioOperation()
  {
    if (this.g == null) {
      TLog.w("%s getAudioOperation need setOutputAudioFormat first", new Object[] { "TuSdkMediaFileEncoder" });
    }
    return this.g.getOperation();
  }
  
  public void setWatermark(SelesWatermark paramSelesWatermark)
  {
    if (this.f == null) {
      return;
    }
    this.f.setWatermark(paramSelesWatermark);
  }
  
  public void setOutputOrientation(ImageOrientation paramImageOrientation)
  {
    if (paramImageOrientation == null) {
      return;
    }
    this.l = paramImageOrientation;
    if (this.f != null) {
      this.f.setOutputOrientation(paramImageOrientation);
    }
  }
  
  public void setCanvasRect(RectF paramRectF)
  {
    if (paramRectF == null) {
      return;
    }
    this.m = paramRectF;
    if (this.f != null) {
      this.f.setCanvasRect(paramRectF);
    }
  }
  
  public void setOutputFilePath(String paramString)
  {
    if (this.a != -1)
    {
      TLog.w("%s setOutputFilePath need before prepare.", new Object[] { "TuSdkMediaFileEncoder" });
      return;
    }
    if (StringHelper.isEmpty(paramString))
    {
      TLog.w("%s setOutputFilePath need a valid file path: %s", new Object[] { "TuSdkMediaFileEncoder", paramString });
      return;
    }
    this.b = new TuSdkMediaDataSource(paramString);
  }
  
  public int setOutputVideoFormat(MediaFormat paramMediaFormat)
  {
    if (this.a != -1)
    {
      TLog.w("%s setOutputVideoFormat need before prepare.", new Object[] { "TuSdkMediaFileEncoder" });
      return -1;
    }
    this.f = new TuSdkVideoSurfaceEncoder();
    int n = this.f.setOutputFormat(paramMediaFormat);
    if (n != 0)
    {
      this.f = null;
      TLog.w("%s setOutputVideoFormat has a error code: %d, %s", new Object[] { "TuSdkMediaFileEncoder", Integer.valueOf(n), paramMediaFormat });
      return n;
    }
    return 0;
  }
  
  public int setOutputAudioFormat(MediaFormat paramMediaFormat)
  {
    if (this.a != -1)
    {
      TLog.w("%s setOutputAudioFormat need before prepare.", new Object[] { "TuSdkMediaFileEncoder" });
      return -1;
    }
    this.g = new TuSdkAudioEncoder();
    int n = this.g.setOutputFormat(paramMediaFormat);
    if (n != 0)
    {
      this.g = null;
      TLog.w("%s setOutputAudioFormat has a error code: %d, %s", new Object[] { "TuSdkMediaFileEncoder", Integer.valueOf(n), paramMediaFormat });
      return n;
    }
    return 0;
  }
  
  public TuSdkAudioInfo getOutputAudioInfo()
  {
    if (this.g == null) {
      return null;
    }
    return this.g.getAudioInfo();
  }
  
  public void setSurfaceRender(TuSdkSurfaceRender paramTuSdkSurfaceRender)
  {
    this.d = paramTuSdkSurfaceRender;
    if (this.f != null) {
      this.f.setSurfaceRender(paramTuSdkSurfaceRender);
    }
  }
  
  public void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender)
  {
    this.e = paramTuSdkAudioRender;
    if (this.g != null) {
      this.g.setAudioRender(paramTuSdkAudioRender);
    }
  }
  
  public void setMediaSync(TuSdkMediaEncodecSync paramTuSdkMediaEncodecSync)
  {
    if (paramTuSdkMediaEncodecSync == null) {
      return;
    }
    this.i = paramTuSdkMediaEncodecSync;
    if (this.f != null) {
      this.f.setMediaSync(paramTuSdkMediaEncodecSync.getVideoEncodecSync());
    }
    if (this.g != null) {
      this.g.setMediaSync(paramTuSdkMediaEncodecSync.getAudioEncodecSync());
    }
  }
  
  public void setListener(TuSdkVideoSurfaceEncoderListener paramTuSdkVideoSurfaceEncoderListener, TuSdkEncoderListener paramTuSdkEncoderListener)
  {
    this.k = paramTuSdkVideoSurfaceEncoderListener;
    this.j = paramTuSdkEncoderListener;
    if (this.f != null) {
      this.f.setListener(paramTuSdkVideoSurfaceEncoderListener);
    }
    if (this.g != null) {
      this.g.setListener(paramTuSdkEncoderListener);
    }
  }
  
  public void release()
  {
    if (this.a == 1) {
      return;
    }
    this.a = 1;
    if (this.f != null) {
      this.f.release();
    }
    if (this.g != null) {
      this.g.release();
    }
    if (this.h != null) {
      this.h.release();
    }
    FileHelper.delete(this.c);
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public boolean cleanTemp()
  {
    return FileHelper.rename(this.c, new File(this.b.getPath()));
  }
  
  public boolean prepare(EGLContext paramEGLContext)
  {
    return prepare(paramEGLContext, false);
  }
  
  public boolean prepare(EGLContext paramEGLContext, boolean paramBoolean)
  {
    if (this.a > -1) {
      return false;
    }
    if ((this.f == null) && (this.g == null))
    {
      TLog.w("%s prepare need setOutputVideoFormat or setOutputAudioFormat first.", new Object[] { "TuSdkMediaFileEncoder" });
      return false;
    }
    if (this.b == null)
    {
      TLog.w("%s setOutputFilePath need a valid file path: %s", new Object[] { "TuSdkMediaFileEncoder", this.b });
      return false;
    }
    if (this.i == null)
    {
      TLog.w("%s prepare need setMediaSync first.", new Object[] { "TuSdkMediaFileEncoder" });
      return false;
    }
    this.a = 0;
    this.c = new File(TuSdk.getAppTempPath(), String.format("lsq_media_tmp_%d.tmp", new Object[] { Long.valueOf(System.currentTimeMillis()) }));
    this.h = new TuSdkMediaFileMuxer();
    this.h.setPath(this.c.getAbsolutePath());
    this.h.setMediaMuxerFormat(0);
    if (this.f != null)
    {
      this.f.setSurfaceRender(this.d);
      this.f.setListener(this.k);
      this.f.setMediaSync(this.i.getVideoEncodecSync());
      this.f.setOutputOrientation(this.l);
      this.f.setCanvasRect(this.m);
      this.f.prepare(paramEGLContext, paramBoolean);
      this.h.setVideoOperation(this.f.getOperation());
    }
    if (this.g != null)
    {
      this.g.setAudioRender(this.e);
      this.g.setListener(this.j);
      this.g.setMediaSync(this.i.getAudioEncodecSync());
      this.g.prepare();
      this.h.setAudioOperation(this.g.getOperation());
    }
    boolean bool = this.h.prepare();
    return bool;
  }
  
  public void signalVideoEndOfInputStream()
  {
    if (this.f == null) {
      return;
    }
    this.f.signalEndOfInputStream();
  }
  
  public void requestVideoKeyFrame()
  {
    if (this.f == null) {
      return;
    }
    this.f.requestKeyFrame();
  }
  
  public void requestVideoRender(long paramLong)
  {
    if (this.f == null)
    {
      TLog.e("%s video encoder is null!", new Object[] { "TuSdkMediaFileEncoder" });
      return;
    }
    this.f.requestRender(paramLong);
  }
  
  public void signalAudioEndOfInputStream(long paramLong)
  {
    if (this.g == null) {
      return;
    }
    this.g.signalEndOfInputStream(paramLong);
  }
  
  public void autoFillAudioMuteData(long paramLong1, long paramLong2, boolean paramBoolean)
  {
    if (this.g == null) {
      return;
    }
    this.g.autoFillMuteData(paramLong1, paramLong2, paramBoolean);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\encoder\TuSdkMediaFileEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */