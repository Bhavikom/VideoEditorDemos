// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.video;

//import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
//import org.lasque.tusdk.core.encoder.video.TuSDKHardVideoDataEncoder;
import android.opengl.EGLContext;
import java.nio.Buffer;
//import org.lasque.tusdk.core.utils.RectHelper;
import android.graphics.Rect;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;
//import org.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
//import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
//import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKLiveStickerImage;
//import org.lasque.tusdk.core.sticker.LiveStickerPlayController;
import android.os.Handler;
import android.os.HandlerThread;
//import org.lasque.tusdk.core.encoder.video.TuSDKHardVideoDataEncoderInterface;
//import org.lasque.tusdk.core.gl.SelesWindowsSurface;
import android.graphics.SurfaceTexture;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.graphics.RectF;
import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
//import org.lasque.tusdk.core.seles.SelesGLProgram;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesGLProgram;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKLiveStickerImage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.sticker.LiveStickerPlayController;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerData;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKHardVideoDataEncoder;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKHardVideoDataEncoderInterface;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.gl.SelesWindowsSurface;

@TargetApi(18)
public class
SelesSurfaceTextureEncoder implements SelesSurfaceEncoderInterface
{
    private static final float[] a;
    private static final float[] b;
    protected SelesGLProgram mDisplayProgram;
    protected SelesFramebuffer mFirstInputFramebuffer;
    private int c;
    private int d;
    private int e;
    protected TuSdkSize mInputTextureSize;
    protected FloatBuffer mVerticesBuffer;
    private FloatBuffer f;
    private RectF g;
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
    private TuSdkWaterMarkOption.WaterMarkPosition[] s;
    private TuSDKVideoEncoderSetting t;
    private int u;
    private int v;
    private TuSDKVideoDataEncoderDelegate w;
    private boolean x;
    private VideoEncoderState y;
    
    public SelesSurfaceTextureEncoder() {
        this.mInputTextureSize = new TuSdkSize();
        this.g = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        this.s = new TuSdkWaterMarkOption.WaterMarkPosition[] { TuSdkWaterMarkOption.WaterMarkPosition.TopLeft, TuSdkWaterMarkOption.WaterMarkPosition.BottomLeft, TuSdkWaterMarkOption.WaterMarkPosition.BottomRight, TuSdkWaterMarkOption.WaterMarkPosition.TopRight };
        this.y = VideoEncoderState.UnKnow;
        this.a();
        this.r = SelesFilter.buildBuffer(new float[] { 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f });
        (this.k = new HandlerThread("com.tusdk.SelesAsyncEncoder")).start();
        this.l = new Handler(this.k.getLooper());
        this.setEnabled(false);
    }
    
    private void a() {
        this.mInputRotation = ImageOrientation.Up;
        this.mVerticesBuffer = SelesFilter.buildBuffer(SelesSurfaceTextureEncoder.b);
        this.f = SelesFilter.buildBuffer(SelesSurfaceTextureEncoder.a);
    }
    
    public void setWaterMarkStickerPlayController(final LiveStickerPlayController m) {
        this.m = m;
    }
    
    private void b() {
        this.mDisplayProgram = SelesGLProgram.create("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", "varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}");
        if (!this.mDisplayProgram.isInitialized()) {
            this.initializeAttributes();
            if (!this.mDisplayProgram.link()) {
                TLog.i("Program link log: %s", new Object[] { this.mDisplayProgram.getProgramLog() });
                TLog.i("Fragment shader compile log: %s", new Object[] { this.mDisplayProgram.getFragmentShaderLog() });
                TLog.i("Vertex link log: %s", new Object[] { this.mDisplayProgram.getVertexShaderLog() });
                this.mDisplayProgram = null;
                TLog.e("Filter shader link failed: %s", new Object[] { this.getClass() });
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
    
    protected void initializeAttributes() {
        this.mDisplayProgram.addAttribute("position");
        this.mDisplayProgram.addAttribute("inputTextureCoordinate");
    }
    
    @Override
    public void updateWaterMark(final Bitmap waterMarkImage, final int n, final TuSdkWaterMarkOption.WaterMarkPosition waterMarkPosition) {
        if (n == 0 || n == 360) {
            if (waterMarkImage != null && !waterMarkImage.isRecycled()) {
                this.setWaterMarkImage(waterMarkImage);
            }
            if (waterMarkPosition != null) {
                this.setWaterMarkPosition(waterMarkPosition);
            }
            return;
        }
        if (waterMarkImage != null && !waterMarkImage.isRecycled()) {
            this.setWaterMarkImage(BitmapHelper.imageRotaing(waterMarkImage, ImageOrientation.getValue(360 - n, false)));
        }
        if (waterMarkPosition == null) {
            return;
        }
        for (int i = 0; i < this.s.length; ++i) {
            if (waterMarkPosition == this.s[i]) {
                this.setWaterMarkPosition(this.s[(i + n / 90) % 4]);
                return;
            }
        }
    }
    
    @Override
    public void destroy() {
        this.k.quit();
        if (this.n != null) {
            this.n.removeSticker();
            this.n = null;
        }
    }
    
    private void a(final VideoEncoderState y) {
        this.y = y;
    }
    
    public void mountAtGLThread(final Runnable runnable) {
    }
    
    public void newFrameReady(final long n, final int n2) {
        if (this.mInputTextureSize == null || !this.mInputTextureSize.isSize()) {
            return;
        }
        if (this.mFirstInputFramebuffer == null) {
            TLog.d("No input fbo, skip one frame", new Object[0]);
            return;
        }
        this.setEnabled(false);
        GLES20.glFinish();
        this.a(n, n2);
    }
    
    private void a(final long n, final int n2) {
        if (this.mVideoEncoder == null) {
            return;
        }
        this.l.post((Runnable)new Runnable() {
            final /* synthetic */ long a = SelesSurfaceTextureEncoder.this.getTimestamp();
            
            @Override
            public void run() {
                if (!SelesSurfaceTextureEncoder.this.isRecording()) {
                    return;
                }
                SelesSurfaceTextureEncoder.this.renderToTexture(SelesSurfaceTextureEncoder.this.mVerticesBuffer, SelesSurfaceTextureEncoder.this.f);
                SelesSurfaceTextureEncoder.this.mVideoEncoder.drainEncoder(false);
                SelesSurfaceTextureEncoder.this.inputFramebufferUnlock();
                SelesSurfaceTextureEncoder.this.i.setPresentationTime(this.a);
                SelesSurfaceTextureEncoder.this.i.swapBuffers();
                if (SelesSurfaceTextureEncoder.this.isRecording()) {
                    SelesSurfaceTextureEncoder.this.setEnabled(true);
                }
            }
        });
    }
    
    public void setInputFramebuffer(final SelesFramebuffer mFirstInputFramebuffer, final int n) {
        if (mFirstInputFramebuffer == null) {
            return;
        }
        (this.mFirstInputFramebuffer = mFirstInputFramebuffer).lock();
    }
    
    public int nextAvailableTextureIndex() {
        return 0;
    }
    
    public void setInputSize(final TuSdkSize tuSdkSize, final int n) {
        final TuSdkSize rotatedSize = this.rotatedSize(tuSdkSize, n);
        if (rotatedSize.minSide() < 1) {
            this.mInputTextureSize = rotatedSize;
        }
        else if (!rotatedSize.equals((Object)this.mInputTextureSize)) {
            this.mInputTextureSize = rotatedSize;
        }
        this.a(this.mInputTextureSize);
        final TuSdkSize tuSdkSize2 = new TuSdkSize();
        tuSdkSize2.width = (int)(this.mInputTextureSize.width * this.getCropRegion().width());
        tuSdkSize2.height = (int)(this.mInputTextureSize.height * this.getCropRegion().height());
        if (tuSdkSize2.isSize()) {
            this.mInputTextureSize = tuSdkSize2;
        }
        else if (!this.mInputTextureSize.equals((Object)tuSdkSize2)) {
            this.mInputTextureSize = tuSdkSize2;
        }
        this.e();
    }
    
    public void setInputRotation(final ImageOrientation imageOrientation, final int n) {
        this.c();
    }
    
    private void c() {
        final RectF cropRegion = this.getCropRegion();
        final float left = cropRegion.left;
        final float top = cropRegion.top;
        final float right = cropRegion.right;
        final float bottom = cropRegion.bottom;
        final float[] src = { 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f };
        src[0] = left;
        src[1] = bottom;
        src[2] = right;
        src[3] = bottom;
        src[4] = left;
        src[5] = top;
        src[6] = right;
        src[7] = top;
        if (this.isEnableHorizontallyFlip()) {
            src[0] = right;
            src[1] = bottom;
            src[2] = left;
            src[3] = bottom;
            src[4] = right;
            src[5] = top;
            src[6] = left;
            src[7] = top;
        }
        this.f.clear();
        this.f.put(src).position(0);
    }
    
    public TuSdkSize maximumOutputSize() {
        return new TuSdkSize();
    }
    
    public void endProcessing() {
    }
    
    public boolean isShouldIgnoreUpdatesToThisTarget() {
        return false;
    }
    
    public boolean isEnabled() {
        return this.x;
    }
    
    public void setEnabled(final boolean x) {
        if (this.x == x) {
            return;
        }
        this.x = x;
    }
    
    public boolean wantsMonochromeInput() {
        return false;
    }
    
    public boolean isCurrentlyReceivingMonochromeInput() {
        return this.mCurrentlyReceivingMonochromeInput;
    }
    
    public void setCurrentlyReceivingMonochromeInput(final boolean mCurrentlyReceivingMonochromeInput) {
        this.mCurrentlyReceivingMonochromeInput = mCurrentlyReceivingMonochromeInput;
    }
    
    @Override
    public void setCropRegion(final RectF g) {
        this.g = g;
    }
    
    public RectF getCropRegion() {
        if (this.g == null) {
            return this.calculateCenterRectPercent(this.getVideoEncoderSetting().videoSize.getRatioFloat(), this.mInputTextureSize);
        }
        return this.g;
    }
    
    protected RectF calculateCenterRectPercent(final float n, final TuSdkSize tuSdkSize) {
        if (n == 0.0f || tuSdkSize == null || !tuSdkSize.isSize()) {
            return new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        }
        final TuSdkSize create = TuSdkSize.create(tuSdkSize);
        create.width = (int)(tuSdkSize.height * n);
        final Rect rectWithAspectRatioInsideRect = RectHelper.makeRectWithAspectRatioInsideRect(create, new Rect(0, 0, tuSdkSize.width, tuSdkSize.height));
        return new RectF(rectWithAspectRatioInsideRect.left / (float)tuSdkSize.width, rectWithAspectRatioInsideRect.top / (float)tuSdkSize.height, rectWithAspectRatioInsideRect.right / (float)tuSdkSize.width, rectWithAspectRatioInsideRect.bottom / (float)tuSdkSize.height);
    }
    
    private void a(final TuSdkSize tuSdkSize) {
        this.c();
    }
    
    public TuSdkSize rotatedSize(final TuSdkSize tuSdkSize, final int n) {
        final TuSdkSize copy = tuSdkSize.copy();
        if (this.mInputRotation.isTransposed()) {
            copy.width = tuSdkSize.height;
            copy.height = tuSdkSize.width;
        }
        return copy;
    }
    
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        SelesContext.setActiveShaderProgram(this.mDisplayProgram);
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(3553, this.mFirstInputFramebuffer.getTexture());
        GLES20.glUniform1i(this.e, 2);
        GLES20.glVertexAttribPointer(this.c, 2, 5126, false, 0, (Buffer)floatBuffer);
        GLES20.glVertexAttribPointer(this.d, 2, 5126, false, 0, (Buffer)floatBuffer2);
        GLES20.glDrawArrays(5, 0, 4);
        if (this.g() != null && this.g().isEnabled()) {
            this.d();
        }
        GLES20.glBindTexture(3553, 0);
    }
    
    private void d() {
        if (this.q == null) {
            return;
        }
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(772, 771);
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(3553, this.n.getCurrentTextureID());
        GLES20.glUniform1i(this.e, 2);
        GLES20.glVertexAttribPointer(this.c, 2, 5126, false, 0, (Buffer)this.q);
        GLES20.glVertexAttribPointer(this.d, 2, 5126, false, 0, (Buffer)this.r);
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glDisable(3042);
    }
    
    private void e() {
        final float n = 1.0f;
        final float n2 = 1.0f;
        if (this.mInputTextureSize == null || !this.mInputTextureSize.isSize() || this.n == null) {
            return;
        }
        if (this.getVideoEncoderSetting().videoSize == null || !this.getVideoEncoderSetting().videoSize.isSize()) {
            return;
        }
        final TuSdkSize textureSize = this.n.getTextureSize();
        if (textureSize == null || !textureSize.isSize()) {
            return;
        }
        final TuSdkSize mInputTextureSize = this.mInputTextureSize;
        final float n3 = textureSize.width * 1.0f / mInputTextureSize.width;
        final float n4 = textureSize.height * 1.0f / mInputTextureSize.height;
        final float n5 = 16.0f / mInputTextureSize.width;
        final float n6 = 16.0f / mInputTextureSize.height;
        float n7 = 0.0f;
        float n8 = 0.0f;
        switch (this.getWaterMarkPosition().ordinal()) {
            case 1: {
                n7 = n - n3 - n5;
                n8 = n6;
                break;
            }
            case 2: {
                n7 = n5;
                n8 = n2 - n4 - n6;
                break;
            }
            case 3: {
                n7 = n5;
                n8 = n6;
                break;
            }
            case 4: {
                n7 = n / 2.0f - n3 / 2.0f;
                n8 = n2 / 2.0f - n4 / 2.0f;
                break;
            }
            default: {
                n7 = n - n3 - n5;
                n8 = n2 - n4 - n6;
                break;
            }
        }
        final float[] src = { 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f };
        src[0] = n7 * 2.0f - 1.0f;
        src[1] = n8 * 2.0f - 1.0f;
        src[2] = (n7 + n3) * 2.0f - 1.0f;
        src[3] = n8 * 2.0f - 1.0f;
        src[4] = n7 * 2.0f - 1.0f;
        src[5] = (n8 + n4) * 2.0f - 1.0f;
        src[6] = (n7 + n3) * 2.0f - 1.0f;
        src[7] = (n8 + n4) * 2.0f - 1.0f;
        if (this.q == null) {
            this.q = SelesFilter.buildBuffer(src);
        }
        else {
            this.q.clear();
            this.q.put(src).position(0);
        }
    }
    
    protected void inputFramebufferUnlock() {
        if (this.mFirstInputFramebuffer != null) {
            this.mFirstInputFramebuffer.unlock();
        }
        this.mFirstInputFramebuffer = null;
    }
    
    @Override
    public void startRecording(final EGLContext eglContext, final SurfaceTexture surfaceTexture) {
        if (this.mVideoEncoder != null && this.isPaused()) {
            this.h = surfaceTexture;
            this.l.removeCallbacksAndMessages((Object)null);
            this.l.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    SelesSurfaceTextureEncoder.this.mVideoEncoder.flush();
                    SelesSurfaceTextureEncoder.this.mVideoEncoder.requestKeyFrame();
                    SelesSurfaceTextureEncoder.this.a(VideoEncoderState.Recording);
                    SelesSurfaceTextureEncoder.this.setEnabled(true);
                }
            });
        }
        else {
            this.h = surfaceTexture;
            this.l.removeCallbacksAndMessages((Object)null);
            this.l.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    SelesSurfaceTextureEncoder.this.a(eglContext);
                }
            });
        }
    }
    
    private void a(final EGLContext eglContext) {
        if (this.isRecording()) {
            return;
        }
        this.a(VideoEncoderState.Recording);
        this.f();
        this.prepareEncoder(this.getVideoEncoderSetting());
        if (this.mVideoEncoder != null && this.mVideoEncoder.getInputSurface() != null) {
            this.mVideoEncoder.setDelegate(this.getDelegate());
            (this.i = new SelesWindowsSurface(eglContext, 0)).attachSurface(this.mVideoEncoder.getInputSurface(), true);
            this.i.makeCurrent();
            this.b();
            SelesContext.setActiveShaderProgram(this.mDisplayProgram);
            this.setEnabled(true);
        }
        else {
            TLog.e("SelesSurfaceTextureEncoder _startRecording failed", new Object[0]);
        }
    }
    
    @Override
    public void pausdRecording() {
        this.l.post((Runnable)new Runnable() {
            @Override
            public void run() {
                if (!SelesSurfaceTextureEncoder.this.isRecording()) {
                    return;
                }
                if (SelesSurfaceTextureEncoder.this.isPaused()) {
                    return;
                }
                SelesSurfaceTextureEncoder.this.a(VideoEncoderState.Paused);
                SelesSurfaceTextureEncoder.this.setEnabled(false);
            }
        });
    }
    
    protected void prepareEncoder(final TuSDKVideoEncoderSetting tuSDKVideoEncoderSetting) {
        final TuSDKHardVideoDataEncoder mVideoEncoder = new TuSDKHardVideoDataEncoder();
        mVideoEncoder.setDefaultVideoQuality(this.u, this.v);
        this.mVideoEncoder.initCodec(tuSDKVideoEncoderSetting);
        this.mVideoEncoder = mVideoEncoder;
    }
    
    @Override
    public void stopRecording() {
        this.l.removeCallbacksAndMessages((Object)null);
        this.l.post((Runnable)new Runnable() {
            @Override
            public void run() {
                if (!SelesSurfaceTextureEncoder.this.isRecording() && !SelesSurfaceTextureEncoder.this.isPaused()) {
                    return;
                }
                SelesSurfaceTextureEncoder.this.setEnabled(false);
                SelesSurfaceTextureEncoder.this.a(VideoEncoderState.Stopped);
                SelesSurfaceTextureEncoder.this.inputFramebufferUnlock();
                if (SelesSurfaceTextureEncoder.this.mVideoEncoder != null) {
                    SelesSurfaceTextureEncoder.this.mVideoEncoder.drainEncoder(true);
                }
                SelesSurfaceTextureEncoder.this.f();
            }
        });
    }
    
    private void f() {
        if (this.mVideoEncoder != null) {
            this.mVideoEncoder.release();
            this.mVideoEncoder = null;
        }
        if (this.i != null) {
            this.i.release();
            this.i = null;
        }
    }
    
    @Override
    public boolean isRecording() {
        return this.y == VideoEncoderState.Recording;
    }
    
    @Override
    public boolean isPaused() {
        return this.y == VideoEncoderState.Paused;
    }
    
    @Override
    public void setEnableHorizontallyFlip(final boolean j) {
        this.j = j;
        this.c();
    }
    
    public boolean isEnableHorizontallyFlip() {
        return this.j;
    }
    
    public void setWaterMarkImage(final Bitmap o) {
        this.o = o;
        this.n = null;
    }
    
    private TuSDKLiveStickerImage g() {
        if (this.o == null || this.n != null) {
            return this.n;
        }
        final StickerData create = StickerData.create(0L, 0L, "", "", 1, 1, "");
        create.stickerType = 3;
        create.setImage(this.o);
        if (this.n == null && this.m != null) {
            this.n = new TuSDKLiveStickerImage(this.m.getLiveStickerLoader());
        }
        if (this.n != null) {
            this.n.updateSticker(create);
        }
        return this.n;
    }
    
    public Bitmap getWaterMarkImage() {
        return this.o;
    }
    
    public void setWaterMarkPosition(final TuSdkWaterMarkOption.WaterMarkPosition p) {
        this.p = p;
        this.e();
    }
    
    public TuSdkWaterMarkOption.WaterMarkPosition getWaterMarkPosition() {
        return this.p;
    }
    
    protected long getTimestamp() {
        if (this.h == null) {
            return 0L;
        }
        long n = this.h.getTimestamp();
        if (n <= 0L) {
            n = System.nanoTime();
        }
        return n;
    }
    
    @Override
    public void setVideoEncoderSetting(final TuSDKVideoEncoderSetting t) {
        this.t = t;
    }
    
    @Override
    public TuSDKVideoEncoderSetting getVideoEncoderSetting() {
        if (this.t == null) {
            this.t = new TuSDKVideoEncoderSetting();
        }
        return this.t;
    }
    
    public void setDefaultVideoQuality(final int u, final int v) {
        this.u = u;
        this.v = v;
    }
    
    @Override
    public void setDelegate(final TuSDKVideoDataEncoderDelegate w) {
        this.w = w;
    }
    
    public TuSDKVideoDataEncoderDelegate getDelegate() {
        return this.w;
    }
    
    static {
        a = new float[] { 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f };
        b = new float[] { -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f };
    }
    
    private enum VideoEncoderState
    {
        UnKnow, 
        Recording, 
        Paused, 
        Stopped;
    }
}
