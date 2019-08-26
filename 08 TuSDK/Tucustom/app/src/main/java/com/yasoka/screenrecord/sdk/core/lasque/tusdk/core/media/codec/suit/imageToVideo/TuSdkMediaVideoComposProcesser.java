// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.imageToVideo;

import java.nio.Buffer;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.LinkedHashMap;
import android.graphics.Color;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;
//import org.lasque.tusdk.core.seles.SelesContext;
import java.util.Map;
import android.graphics.RectF;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
//import org.lasque.tusdk.core.seles.SelesGLProgram;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.record.TuSdkRecordSurface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesGLProgram;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;
//import org.lasque.tusdk.core.seles.sources.SelesOutput;

class TuSdkMediaVideoComposProcesser extends SelesOutput implements TuSdkRecordSurface
{
    private ImageOrientation a;
    private TuSdkSize b;
    private SelesFramebuffer c;
    private FloatBuffer d;
    private FloatBuffer e;
    private SelesGLProgram f;
    private int g;
    private int h;
    private int i;
    private long j;
    private SelesVerticeCoordinateCorpBuilder k;
    private RectF l;
    private boolean m;
    private float n;
    private float o;
    private float p;
    private float q;
    private boolean r;
    private long s;
    private ComposProcesserListener t;
    private final Map<SelesContext.SelesInput, Integer> u;
    private SelesFilter v;
    
    public void setTextureCoordinateBuilder(final SelesVerticeCoordinateCorpBuilder k) {
        this.k = k;
        if (this.k != null && this.l != null) {
            this.k.setPreCropRect(this.l);
        }
    }
    
    public void setPreCropRect(final RectF l) {
        this.l = l;
        if (this.k != null) {
            this.k.setPreCropRect(this.l);
        }
    }
    
    public void setEnableClip(final boolean enableClip) {
        if (this.k != null) {
            this.k.setEnableClip(enableClip);
        }
    }
    
    public TuSdkSize setOutputRatio(final float outputRatio) {
        if (this.k != null) {
            return this.k.setOutputRatio(outputRatio);
        }
        return null;
    }
    
    public void setOutputSize(final TuSdkSize tuSdkSize) {
        this.b = tuSdkSize;
        if (this.k != null) {
            this.k.setOutputSize(tuSdkSize);
        }
    }
    
    public void setCanvasColor(final float n, final float o, final float p4, final float q) {
        this.n = n;
        this.o = o;
        this.p = p4;
        this.q = q;
    }
    
    public void setCanvasColor(final int n) {
        this.setCanvasColor(Color.red(n) / 255.0f, Color.green(n) / 255.0f, Color.blue(n) / 255.0f, Color.alpha(n) / 255.0f);
    }
    
    public void setComposProcesserListener(final ComposProcesserListener t) {
        this.t = t;
    }
    
    public void setCurrentFrameUs(final long s) {
        this.s = s;
    }
    
    public TuSdkMediaVideoComposProcesser() {
        this.a = ImageOrientation.Up;
        this.j = -1L;
        this.m = false;
        this.n = 0.0f;
        this.o = 0.0f;
        this.p = 0.0f;
        this.q = 1.0f;
        this.r = false;
        this.s = 0L;
        this.u = new LinkedHashMap<SelesContext.SelesInput, Integer>();
        this.v = null;
        this.d = SelesFilter.buildBuffer(SelesFilter.imageVertices);
        this.e = SelesFilter.buildBuffer(SelesFilter.noRotationTextureCoordinates);
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                TuSdkMediaVideoComposProcesser.this.a();
            }
        });
    }
    
    private void a() {
        this.j = Thread.currentThread().getId();
        this.f = SelesContext.program("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", "varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}");
        if (!this.f.isInitialized()) {
            this.f.addAttribute("position");
            this.f.addAttribute("inputTextureCoordinate");
            if (!this.f.link()) {
                TLog.i("%s Program link log: %s", "TuSdkMediaVideoComposProcesser", this.f.getProgramLog());
                TLog.i("%s Fragment shader compile log: %s", "TuSdkMediaVideoComposProcesser", this.f.getFragmentShaderLog());
                TLog.i("%s Vertex link log: %s", "TuSdkMediaVideoComposProcesser", this.f.getVertexShaderLog());
                this.f = null;
                TLog.e("%s Filter shader link failed: %s", "TuSdkMediaVideoComposProcesser", this.getClass());
                return;
            }
        }
        this.g = this.f.attributeIndex("position");
        this.h = this.f.attributeIndex("inputTextureCoordinate");
        this.i = this.f.uniformIndex("inputImageTexture");
        SelesContext.setActiveShaderProgram(this.f);
        GLES20.glEnableVertexAttribArray(this.g);
        GLES20.glEnableVertexAttribArray(this.h);
        this.b();
        this.m = true;
    }
    
    private void b() {
        this.c = SelesContext.sharedFramebufferCache().fetchFramebuffer(this.mInputTextureSize, true);
    }

    @Override
    public void addTarget(SelesContext.SelesInput p0, int p1) {

    }

    @Override
    public void removeTarget(SelesContext.SelesInput p0) {

    }

    @Override
    public void initInGLThread() {
        this.runPendingOnDrawTasks();
    }
    
    @Override
    public void updateSurfaceTexImage() {
        this.newFrameReadyInGLThread(this.s);
    }
    
    @Override
    public void newFrameReadyInGLThread(final long n) {
        if (this.j != Thread.currentThread().getId()) {
            TLog.w("%s newFrameReadyInGLThread need run in GL thread", "TuSdkMediaVideoComposProcesser");
            return;
        }
        this.setInputSize(TuSdkSize.create(this.t.drawItemComposeItem().getImageBitmap()));
        if (this.k != null && this.k.calculate(this.mInputTextureSize, this.a, this.d, this.e)) {
            this.b = this.k.outputSize();
        }
        else {
            this.e.clear();
            this.e.put(SelesFilter.textureCoordinates(this.a)).position(0);
            this.b = this.mInputTextureSize;
        }
        this.renderToTexture(this.d, this.e);
        this.a(n);
        GLES20.glFinish();
        if (this.t != null) {
            this.t.onFrameAvailable((SurfaceTexture)null);
        }
    }
    
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        this.runPendingOnDrawTasks();
        if (!this.b.isSize()) {
            return;
        }
        this.c.activateFramebuffer();
        this.c.bindTexture(this.t.drawItemComposeItem().getImageBitmap());
        GLES20.glBindFramebuffer(36160, 0);
        SelesContext.setActiveShaderProgram(this.f);
        this.d();
        GLES20.glClearColor(this.n, this.o, this.p, this.q);
        GLES20.glClear(16384);
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(3553, this.c());
        GLES20.glUniform1i(this.i, 2);
        GLES20.glEnableVertexAttribArray(this.g);
        GLES20.glEnableVertexAttribArray(this.h);
        GLES20.glVertexAttribPointer(this.g, 2, 5126, false, 0, (Buffer)floatBuffer);
        GLES20.glVertexAttribPointer(this.h, 2, 5126, false, 0, (Buffer)floatBuffer2);
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glBindTexture(3553, 0);
    }
    
    private int c() {
        int texture = 0;
        if (this.c != null) {
            texture = this.c.getTexture();
        }
        return texture;
    }
    
    private void d() {
        if (this.r || this.mOutputFramebuffer == null) {
            this.e();
            (this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, this.outputFrameSize())).disableReferenceCounting();
            this.r = false;
        }
        this.mOutputFramebuffer.activateFramebuffer();
    }
    
    private void e() {
        if (this.mOutputFramebuffer == null) {
            return;
        }
        this.mOutputFramebuffer.clearAllLocks();
        SelesContext.returnFramebufferToCache(this.mOutputFramebuffer);
        this.mOutputFramebuffer = null;
    }
    
    private void a(final long n) {
        this.u.clear();
        for (final SelesContext.SelesInput selesInput : this.mTargets) {
            if (!selesInput.isEnabled()) {
                continue;
            }
            if (selesInput == this.getTargetToIgnoreForUpdates()) {
                continue;
            }
            final int intValue = this.mTargetTextureIndices.get(this.mTargets.indexOf(selesInput));
            this.u.put(selesInput, intValue);
            this.setInputFramebufferForTarget(selesInput, intValue);
            selesInput.setInputSize(this.outputFrameSize(), intValue);
        }
        for (final Map.Entry<SelesContext.SelesInput, Integer> entry : this.u.entrySet()) {
            entry.getKey().newFrameReady(n, entry.getValue());
        }
    }
    
    @Override
    protected void runOnDraw(final Runnable runnable) {
        super.runOnDraw(runnable);
    }
    
    public TuSdkSize outputFrameSize() {
        return (this.b == null) ? this.mInputTextureSize : this.b;
    }
    
    public void setInputSize(TuSdkSize transforOrientation) {
        if (transforOrientation == null || !transforOrientation.isSize()) {
            return;
        }
        transforOrientation = transforOrientation.transforOrientation(this.a);
        if (this.mInputTextureSize.equals(transforOrientation)) {
            return;
        }
        this.mInputTextureSize = transforOrientation;
        this.r = true;
    }
    
    public void setInputRotation(final ImageOrientation a) {
        if (a == null || a == this.a) {
            return;
        }
        final TuSdkSize transforOrientation = this.mInputTextureSize.transforOrientation(this.a);
        this.a = a;
        this.mInputTextureSize = transforOrientation.transforOrientation(a);
        this.r = true;
    }
    
    @Override
    protected void onDestroy() {
        this.e();
        if (this.c != null) {
            this.c.destroy();
            this.c = null;
        }
    }
    
    public interface ComposProcesserListener extends SurfaceTexture.OnFrameAvailableListener
    {
        TuSdkImageComposeItem drawItemComposeItem();
    }
}
