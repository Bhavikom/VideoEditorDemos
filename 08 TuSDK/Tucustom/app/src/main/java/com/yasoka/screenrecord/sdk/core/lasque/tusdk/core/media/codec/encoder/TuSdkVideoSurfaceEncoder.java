// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder;

//import org.lasque.tusdk.core.seles.output.SelesSurfacePusherAsync;
//import org.lasque.tusdk.core.seles.output.SelesSurfacePusher;
import android.opengl.EGLContext;
//import org.lasque.tusdk.core.media.codec.TuSdkEncodecOperation;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.api.extend.TuSdkSurfaceDraw;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import android.graphics.RectF;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import android.media.MediaFormat;
import android.opengl.GLES20;
import javax.microedition.khronos.egl.EGLConfig;
//import org.lasque.tusdk.core.utils.TLog;
import javax.microedition.khronos.opengles.GL10;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
//import org.lasque.tusdk.core.media.codec.TuSdkCodecOutput;
//import org.lasque.tusdk.core.seles.egl.SelesRenderer;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.sources.SelesWatermark;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkVideoEncodecSync;
//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoSurfaceEncodecOperation;
//import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
//import org.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
//import org.lasque.tusdk.core.seles.output.SelesSurfaceDisplay;
//import org.lasque.tusdk.core.seles.egl.SelesVirtualDisplay;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkCodecOutput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkEncodecOperation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkVideoEncodecSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video.TuSdkVideoSurfaceEncodecOperation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl.SelesRenderer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl.SelesVirtualDisplay;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output.SelesSurfaceDisplay;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output.SelesSurfacePusher;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output.SelesSurfacePusherAsync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesWatermark;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

@TargetApi(18)
public class TuSdkVideoSurfaceEncoder implements TuSdkEncodeSurface
{
    private int a;
    private boolean b;
    private final SelesVerticeCoordinateBuilder c;
    private TuSdkSize d;
    private SelesVirtualDisplay e;
    private SelesSurfaceDisplay f;
    private TuSdkFilterBridge g;
    private boolean h;
    private TuSdkSurfaceRender i;
    private TuSdkVideoSurfaceEncodecOperation j;
    private TuSdkVideoSurfaceEncoderListener k;
    private TuSdkVideoEncodecSync l;
    private SelesWatermark m;
    private ImageOrientation n;
    private SelesRenderer o;
    private TuSdkCodecOutput.TuSdkEncodecOutput p;
    
    public TuSdkVideoSurfaceEncoder() {
        this.a = -1;
        this.b = false;
        this.c = new SelesVerticeCoordinateCropBuilderImpl(true);
        this.h = false;
        this.n = ImageOrientation.Up;
        this.o = new SelesRenderer() {
            @Override
            public void onSurfaceDestory(final GL10 gl10) {
                TLog.d("%s on VirtualDisplay Thread will exit", "TuSdkVideoSurfaceEncoder");
                if (TuSdkVideoSurfaceEncoder.this.i != null) {
                    TuSdkVideoSurfaceEncoder.this.i.onSurfaceDestory();
                }
                TuSdkVideoSurfaceEncoder.this.k.onSurfaceDestory(gl10);
            }
            
            public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
                GLES20.glDisable(2929);
                TuSdkVideoSurfaceEncoder.this.k.onSurfaceCreated(gl10, eglConfig);
                if (TuSdkVideoSurfaceEncoder.this.i != null) {
                    TuSdkVideoSurfaceEncoder.this.i.onSurfaceCreated();
                }
                if (TuSdkVideoSurfaceEncoder.this.b) {
                    TLog.d("%s enable encodec compatibility mode", "TuSdkVideoSurfaceEncoder");
                    TuSdkVideoSurfaceEncoder.this.e.swapBuffers(0L);
                }
            }
            
            public void onSurfaceChanged(final GL10 gl10, final int n, final int n2) {
                GLES20.glViewport(0, 0, n, n2);
                if (TuSdkVideoSurfaceEncoder.this.i != null) {
                    TuSdkVideoSurfaceEncoder.this.i.onSurfaceChanged(n, n2);
                }
                TuSdkVideoSurfaceEncoder.this.k.onSurfaceChanged(gl10, n, n2);
            }
            
            public void onDrawFrame(final GL10 gl10) {
                GLES20.glClear(16640);
                if (TuSdkVideoSurfaceEncoder.this.f == null || TuSdkVideoSurfaceEncoder.this.e == null || TuSdkVideoSurfaceEncoder.this.a != 0) {
                    return;
                }
                final long lastRenderTimestampNs = TuSdkVideoSurfaceEncoder.this.e.lastRenderTimestampNs();
                TuSdkVideoSurfaceEncoder.this.k.onEncoderDrawFrame(lastRenderTimestampNs, TuSdkVideoSurfaceEncoder.this.b && lastRenderTimestampNs == 0L);
            }
        };
        this.p = new TuSdkCodecOutput.TuSdkEncodecOutput() {
            private boolean b = false;
            
            @Override
            public void outputFormatChanged(final MediaFormat mediaFormat) {
                TLog.d("%s outputFormatChanged: %s", "TuSdkVideoSurfaceEncoder", mediaFormat);
                if (TuSdkVideoSurfaceEncoder.this.l != null && TuSdkVideoSurfaceEncoder.this.j != null) {
                    TuSdkVideoSurfaceEncoder.this.l.syncEncodecVideoInfo(TuSdkVideoSurfaceEncoder.this.j.getVideoInfo());
                }
            }
            
            @Override
            public void processOutputBuffer(final TuSdkMediaMuxer tuSdkMediaMuxer, final int n, final ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
                if (TuSdkVideoSurfaceEncoder.this.b) {
                    if (bufferInfo.presentationTimeUs < 1L) {
                        TuSdkVideoSurfaceEncoder.this.j.requestKeyFrame();
                        this.b = true;
                        return;
                    }
                    if (this.b) {
                        this.b = false;
                        final MediaCodec.BufferInfo bufferInfo2 = new MediaCodec.BufferInfo();
                        bufferInfo2.set(bufferInfo.offset, bufferInfo.size, 0L, bufferInfo.flags);
                        bufferInfo = bufferInfo2;
                        TuSdkVideoSurfaceEncoder.this.b = false;
                    }
                }
                if (TuSdkVideoSurfaceEncoder.this.l != null) {
                    TuSdkVideoSurfaceEncoder.this.l.syncVideoEncodecOutputBuffer(tuSdkMediaMuxer, n, byteBuffer, bufferInfo);
                }
                else {
                    TuSdkMediaUtils.processOutputBuffer(tuSdkMediaMuxer, n, byteBuffer, bufferInfo);
                }
            }
            
            @Override
            public void updated(final MediaCodec.BufferInfo bufferInfo) {
                if (TuSdkVideoSurfaceEncoder.this.l != null) {
                    TuSdkVideoSurfaceEncoder.this.l.syncVideoEncodecUpdated(bufferInfo);
                }
                TuSdkVideoSurfaceEncoder.this.k.onEncoderUpdated(bufferInfo);
            }
            
            @Override
            public boolean updatedToEOS(final MediaCodec.BufferInfo bufferInfo) {
                if (TuSdkVideoSurfaceEncoder.this.l != null) {
                    TuSdkVideoSurfaceEncoder.this.l.syncVideoEncodecCompleted();
                }
                TuSdkVideoSurfaceEncoder.this.k.onEncoderCompleted(null);
                return true;
            }
            
            @Override
            public void onCatchedException(final Exception ex) {
                if (TuSdkVideoSurfaceEncoder.this.l != null) {
                    TuSdkVideoSurfaceEncoder.this.l.syncVideoEncodecCompleted();
                }
                TuSdkVideoSurfaceEncoder.this.k.onEncoderCompleted(ex);
            }
        };
    }
    
    public SelesVirtualDisplay getVirtualDisplay() {
        return this.e;
    }
    
    public void setOutputOrientation(final ImageOrientation n) {
        if (n == null) {
            return;
        }
        this.n = n;
        if (this.f != null) {
            this.f.setInputRotation(n, 0);
        }
    }
    
    public void setCanvasRect(final RectF canvasRect) {
        if (canvasRect == null) {
            return;
        }
        this.c.setCanvasRect(canvasRect);
    }
    
    public void setListener(final TuSdkVideoSurfaceEncoderListener k) {
        if (k == null) {
            TLog.w("%s setListener can not empty.", "TuSdkVideoSurfaceEncoder");
            return;
        }
        if (this.a != -1) {
            TLog.w("%s setListener need before prepare.", "TuSdkVideoSurfaceEncoder");
            return;
        }
        this.k = k;
    }
    
    public int setOutputFormat(final MediaFormat mediaFormat) {
        if (this.a != -1) {
            TLog.w("%s setOutputFormat need before prepare.", "TuSdkVideoSurfaceEncoder");
            return -1;
        }
        this.j = new TuSdkVideoSurfaceEncodecOperation(this.p);
        final int setMediaFormat = this.j.setMediaFormat(mediaFormat);
        if (setMediaFormat != 0) {
            this.j = null;
            TLog.w("%s setOutputFormat has a error code: %d, %s", "TuSdkVideoSurfaceEncoder", setMediaFormat, mediaFormat);
            return setMediaFormat;
        }
        this.d = TuSdkMediaFormat.getVideoKeySize(mediaFormat);
        return 0;
    }
    
    public void setMediaSync(final TuSdkVideoEncodecSync l) {
        this.l = l;
    }
    
    public void setSurfaceRender(final TuSdkSurfaceRender i) {
        this.i = i;
        if (this.g != null) {
            this.g.setSurfaceDraw(this.i);
        }
    }
    
    public TuSdkFilterBridge getFilterBridge() {
        if (this.g == null) {
            this.g = new TuSdkFilterBridge();
            this.h = true;
        }
        if (TLog.LOG_VIDEO_ENCODEC_INFO && !this.h) {
            TLog.d("%s used external bridge.", "TuSdkVideoSurfaceEncoder");
        }
        return this.g;
    }
    
    public void setFilterBridge(final TuSdkFilterBridge g) {
        if (g == null || this.g != null) {
            return;
        }
        this.g = g;
    }
    
    public void disconnect() {
        if (this.g == null || this.f == null || this.h) {
            return;
        }
        this.g.removeTarget(this.f);
        this.g = null;
    }
    
    public TuSdkEncodecOperation getOperation() {
        if (this.j == null) {
            TLog.w("%s getOperation need setOutputFormat first", "TuSdkVideoSurfaceEncoder");
        }
        return this.j;
    }
    
    public TuSdkSize getOutputSize() {
        if (this.d == null) {
            TLog.w("%s getOutputSize need setOutputFormat first", "TuSdkVideoSurfaceEncoder");
        }
        return this.d;
    }
    
    public void release() {
        if (this.a == 1) {
            return;
        }
        this.a = 1;
        this.j = null;
        if (this.f != null) {
            this.f.destroy();
        }
        if (this.g != null && this.h) {
            this.g.destroy();
        }
        if (this.e != null) {
            this.e.release();
            this.e = null;
        }
    }
    
    @Override
    protected void finalize() {
        this.release();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    @Override
    public void requestKeyFrame() {
        if (this.j != null) {
            this.j.requestKeyFrame();
        }
    }
    
    public void signalEndOfInputStream() {
        if (this.j != null) {
            this.j.signalEndOfInputStream();
        }
    }
    
    public void requestRender(final long n) {
        if (this.e != null) {
            this.e.requestRender(n);
        }
        else {
            TLog.e("%s video virtualDisplay is null!", "TuSdkVideoSurfaceEncoder");
        }
    }
    
    public boolean requestEncode(final long n) {
        this.newFrameReadyInGLThread(n);
        return this.swapBuffers(n);
    }
    
    @Override
    public void newFrameReadyInGLThread(final long n) {
        if (this.a != 0) {
            return;
        }
        if (this.f != null) {
            this.f.newFrameReadyInGLThread(n);
        }
    }
    
    @Override
    public void duplicateFrameReadyInGLThread(final long n) {
        if (this.a != 0) {
            return;
        }
        if (this.f != null) {
            this.f.duplicateFrameReadyInGLThread(n);
        }
    }
    
    @Override
    public boolean swapBuffers(final long n) {
        if (this.a != 0) {
            return false;
        }
        if (this.j != null) {
            this.j.notifyNewFrameReady();
        }
        return this.e != null && this.e.swapBuffers(n);
    }
    
    @Override
    public void setPause(final boolean b) {
        if (this.a != 0) {
            return;
        }
        if (this.f != null) {
            this.f.setEnabled(!b);
        }
    }
    
    @Override
    public void setWatermark(final SelesWatermark m) {
        this.m = m;
    }
    
    @Override
    public void flush() {
        if (this.j != null) {
            this.j.flush();
        }
    }
    
    public boolean prepare(final EGLContext eglContext, final boolean b) {
        if (this.a > -1) {
            TLog.w("%s has prepared.", "TuSdkVideoSurfaceEncoder");
            return false;
        }
        if (this.j == null) {
            TLog.w("%s run need set Output Video Format.", "TuSdkVideoSurfaceEncoder");
            return false;
        }
        if (this.k == null) {
            TLog.w("%s need setListener first.", "TuSdkVideoSurfaceEncoder");
            return false;
        }
        if (!b) {
            this.b = this.j.getCodecPatch().isEnableCompatibilityMode();
        }
        this.c.setOutputSize(this.d);
        (this.e = new SelesVirtualDisplay()).setRenderer(this.o);
        this.e.buildWindowContext(eglContext);
        this.e.attachWindowSurface(this.j.getSurface(), true);
        if (eglContext == null) {
            this.f = new SelesSurfacePusher();
        }
        else {
            this.f = new SelesSurfacePusherAsync();
        }
        this.f.setPusherRotation(this.n, 0);
        this.f.setTextureCoordinateBuilder(this.c);
        if (this.m != null) {
            this.f.setWatermark(this.m);
        }
        this.getFilterBridge().addTarget(this.f, 0);
        this.a = 0;
        return true;
    }
}
