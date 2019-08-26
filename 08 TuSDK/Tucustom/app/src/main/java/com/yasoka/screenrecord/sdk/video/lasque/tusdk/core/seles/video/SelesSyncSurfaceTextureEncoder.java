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
import java.util.LinkedList;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;
import android.opengl.EGLSurface;
import java.util.Queue;
//import org.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
//import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
//import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKLiveStickerImage;
//import org.lasque.tusdk.core.sticker.LiveStickerPlayController;
//import org.lasque.tusdk.core.encoder.video.TuSDKHardVideoDataEncoderInterface;
//import org.lasque.tusdk.core.gl.EglCore;
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
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.gl.EglCore;

@TargetApi(18)
public class
SelesSyncSurfaceTextureEncoder implements SelesSurfaceEncoderInterface
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
    private EglCore i;
    protected TuSDKHardVideoDataEncoderInterface mVideoEncoder;
    private boolean j;
    private LiveStickerPlayController k;
    private TuSDKLiveStickerImage l;
    private Bitmap m;
    private TuSdkWaterMarkOption.WaterMarkPosition n;
    private FloatBuffer o;
    private final FloatBuffer p;
    private TuSdkWaterMarkOption.WaterMarkPosition[] q;
    private TuSDKVideoEncoderSetting r;
    private int s;
    private int t;
    private TuSDKVideoDataEncoderDelegate u;
    private final Queue<Runnable> v;
    private boolean w;
    private VideoEncoderState x;
    private EGLSurface y;
    
    public SelesSyncSurfaceTextureEncoder() {
        this.mInputTextureSize = new TuSdkSize();
        this.g = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        this.q = new TuSdkWaterMarkOption.WaterMarkPosition[] { TuSdkWaterMarkOption.WaterMarkPosition.TopLeft, TuSdkWaterMarkOption.WaterMarkPosition.BottomLeft, TuSdkWaterMarkOption.WaterMarkPosition.BottomRight, TuSdkWaterMarkOption.WaterMarkPosition.TopRight };
        this.x = VideoEncoderState.UnKnow;
        this.a();
        this.p = SelesFilter.buildBuffer(new float[] { 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f });
        this.setEnabled(true);
        this.v = new LinkedList<Runnable>();
    }
    
    public SelesSyncSurfaceTextureEncoder(final EglCore i) {
        this();
        this.i = i;
    }
    
    private void a() {
        this.mInputRotation = ImageOrientation.Up;
        this.mVerticesBuffer = SelesFilter.buildBuffer(SelesSyncSurfaceTextureEncoder.b);
        this.f = SelesFilter.buildBuffer(SelesSyncSurfaceTextureEncoder.a);
    }
    
    public void setWaterMarkStickerPlayController(final LiveStickerPlayController k) {
        this.k = k;
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
        for (int i = 0; i < this.q.length; ++i) {
            if (waterMarkPosition == this.q[i]) {
                this.setWaterMarkPosition(this.q[(i + n / 90) % 4]);
                return;
            }
        }
    }
    
    @Override
    public void destroy() {
        if (this.l != null) {
            this.l.removeSticker();
            this.l = null;
        }
    }
    
    private void a(final VideoEncoderState x) {
        this.x = x;
    }
    
    public void mountAtGLThread(final Runnable runnable) {
    }
    
    public void makeCurrent() {
        if (this.i == null || this.mVideoEncoder == null) {
            return;
        }
        if (this.y == null) {
            this.y = this.i.createWindowSurface(this.mVideoEncoder.getInputSurface());
        }
        this.i.makeCurrent(this.y);
    }
    
    public void swapBuffers() {
        if (this.i == null || this.y == null) {
            return;
        }
        this.i.swapBuffers(this.y);
    }
    
    public void newFrameReady(final long n, final int n2) {
        this.runPendingOnDrawTasks();
        if (this.mInputTextureSize == null || !this.mInputTextureSize.isSize()) {
            TLog.i("mInputTextureSize error", new Object[0]);
            return;
        }
        if (this.mVideoEncoder == null || !this.isRecording()) {
            return;
        }
        this.makeCurrent();
        GLES20.glBindFramebuffer(36160, 0);
        this.renderToTexture(this.mVerticesBuffer, this.f);
        this.i.setPresentationTime(this.y, n * 1000L);
        this.mVideoEncoder.drainEncoder(false);
        this.swapBuffers();
        this.inputFramebufferUnlock();
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
        return this.w;
    }
    
    public void setEnabled(final boolean w) {
        if (this.w == w) {
            return;
        }
        this.w = w;
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
        if (this.mFirstInputFramebuffer == null) {
            return;
        }
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
        if (this.o == null) {
            return;
        }
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(772, 771);
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(3553, this.l.getCurrentTextureID());
        GLES20.glUniform1i(this.e, 2);
        GLES20.glVertexAttribPointer(this.c, 2, 5126, false, 0, (Buffer)this.o);
        GLES20.glVertexAttribPointer(this.d, 2, 5126, false, 0, (Buffer)this.p);
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glDisable(3042);
    }
    
    private void e() {
        final float n = 1.0f;
        final float n2 = 1.0f;
        if (this.mInputTextureSize == null || !this.mInputTextureSize.isSize() || this.l == null) {
            return;
        }
        if (this.getVideoEncoderSetting().videoSize == null || !this.getVideoEncoderSetting().videoSize.isSize()) {
            return;
        }
        final TuSdkSize textureSize = this.l.getTextureSize();
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
        if (this.o == null) {
            this.o = SelesFilter.buildBuffer(src);
        }
        else {
            this.o.clear();
            this.o.put(src).position(0);
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
            this.runOnDraw(new Runnable() {
                @Override
                public void run() {
                    SelesSyncSurfaceTextureEncoder.this.mVideoEncoder.flush();
                    SelesSyncSurfaceTextureEncoder.this.mVideoEncoder.requestKeyFrame();
                    SelesSyncSurfaceTextureEncoder.this.a(VideoEncoderState.Recording);
                    SelesSyncSurfaceTextureEncoder.this.setEnabled(true);
                }
            });
        }
        else {
            this.h = surfaceTexture;
            this.runOnDraw(new Runnable() {
                @Override
                public void run() {
                    SelesSyncSurfaceTextureEncoder.this.a(eglContext);
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
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                if (!SelesSyncSurfaceTextureEncoder.this.isRecording()) {
                    return;
                }
                if (SelesSyncSurfaceTextureEncoder.this.isPaused()) {
                    return;
                }
                SelesSyncSurfaceTextureEncoder.this.a(VideoEncoderState.Paused);
                SelesSyncSurfaceTextureEncoder.this.setEnabled(false);
            }
        });
    }
    
    protected void prepareEncoder(final TuSDKVideoEncoderSetting tuSDKVideoEncoderSetting) {
        final TuSDKHardVideoDataEncoder mVideoEncoder = new TuSDKHardVideoDataEncoder();
        mVideoEncoder.setDefaultVideoQuality(this.s, this.t);
        this.mVideoEncoder.initCodec(tuSDKVideoEncoderSetting);
        this.mVideoEncoder = mVideoEncoder;
    }
    
    @Override
    public void stopRecording() {
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                if (!SelesSyncSurfaceTextureEncoder.this.isRecording() && !SelesSyncSurfaceTextureEncoder.this.isPaused()) {
                    return;
                }
                SelesSyncSurfaceTextureEncoder.this.setEnabled(false);
                SelesSyncSurfaceTextureEncoder.this.a(VideoEncoderState.Stopped);
                SelesSyncSurfaceTextureEncoder.this.inputFramebufferUnlock();
                if (SelesSyncSurfaceTextureEncoder.this.mVideoEncoder != null) {
                    SelesSyncSurfaceTextureEncoder.this.mVideoEncoder.drainEncoder(true);
                }
                SelesSyncSurfaceTextureEncoder.this.f();
            }
        });
    }
    
    private void f() {
        if (this.mVideoEncoder != null) {
            this.mVideoEncoder.release();
            this.mVideoEncoder = null;
        }
        if (this.y != null) {
            this.i.destroySurface(this.y);
            this.y = null;
        }
    }
    
    @Override
    public boolean isRecording() {
        return this.x == VideoEncoderState.Recording;
    }
    
    @Override
    public boolean isPaused() {
        return this.x == VideoEncoderState.Paused;
    }
    
    @Override
    public void setEnableHorizontallyFlip(final boolean j) {
        this.j = j;
        this.c();
    }
    
    public boolean isEnableHorizontallyFlip() {
        return this.j;
    }
    
    public void setWaterMarkImage(final Bitmap m) {
        this.m = m;
        this.l = null;
    }
    
    private TuSDKLiveStickerImage g() {
        if (this.m == null || this.l != null) {
            return this.l;
        }
        final StickerData create = StickerData.create(0L, 0L, "", "", 1, 1, "");
        create.stickerType = 3;
        create.setImage(this.m);
        if (this.l == null && this.k != null) {
            this.l = new TuSDKLiveStickerImage(this.k.getLiveStickerLoader());
        }
        if (this.l != null) {
            this.l.updateSticker(create);
        }
        return this.l;
    }
    
    public Bitmap getWaterMarkImage() {
        return this.m;
    }
    
    public void setWaterMarkPosition(final TuSdkWaterMarkOption.WaterMarkPosition n) {
        this.n = n;
        this.e();
    }
    
    public TuSdkWaterMarkOption.WaterMarkPosition getWaterMarkPosition() {
        return this.n;
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
    public void setVideoEncoderSetting(final TuSDKVideoEncoderSetting r) {
        this.r = r;
    }
    
    @Override
    public TuSDKVideoEncoderSetting getVideoEncoderSetting() {
        if (this.r == null) {
            this.r = new TuSDKVideoEncoderSetting();
        }
        return this.r;
    }
    
    public void setDefaultVideoQuality(final int s, final int t) {
        this.s = s;
        this.t = t;
    }
    
    @Override
    public void setDelegate(final TuSDKVideoDataEncoderDelegate u) {
        this.u = u;
    }
    
    public TuSDKVideoDataEncoderDelegate getDelegate() {
        return this.u;
    }
    
    protected void runPendingOnDrawTasks() {
        this.a(this.v);
    }
    
    protected boolean isOnDrawTasksEmpty() {
        boolean empty = false;
        synchronized (this.v) {
            empty = this.v.isEmpty();
        }
        return empty;
    }
    
    private void a(final Queue<Runnable> queue) {
        synchronized (queue) {
            while (!queue.isEmpty()) {
                queue.poll().run();
            }
        }
    }
    
    protected void runOnDraw(final Runnable runnable) {
        this.v.add(runnable);
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
