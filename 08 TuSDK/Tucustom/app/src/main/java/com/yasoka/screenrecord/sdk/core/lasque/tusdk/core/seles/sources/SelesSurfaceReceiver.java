// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources;

import android.os.Build;

import java.nio.Buffer;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;
import java.util.LinkedHashMap;
import android.graphics.Color;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.seles.SelesContext;
import java.util.Map;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
//import org.lasque.tusdk.core.seles.SelesGLProgram;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.record.TuSdkRecordSurface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesGLProgram;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesSurfaceTexture;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.extend.SelesSurfaceTexture;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;

public class SelesSurfaceReceiver extends SelesOutput implements TuSdkRecordSurface, SelesSurfaceHolder
{
    private ImageOrientation a;
    private TuSdkSize b;
    protected SelesFramebuffer mSurfaceFBO;
    private SelesSurfaceTexture c;
    private FloatBuffer d;
    private FloatBuffer e;
    private SelesGLProgram f;
    private int g;
    private int h;
    private int i;
    private boolean j;
    private long k;
    private SelesVerticeCoordinateCorpBuilder l;
    private SurfaceTexture.OnFrameAvailableListener m;
    private RectF n;
    private boolean o;
    private float p;
    private float q;
    private float r;
    private float s;
    private final Map<SelesContext.SelesInput, Integer> t;
    
    @Override
    public boolean isInited() {
        return this.o;
    }
    
    public SurfaceTexture getSurfaceTexture() {
        return this.c;
    }
    
    @Override
    public SurfaceTexture requestSurfaceTexture() {
        if (this.mSurfaceFBO == null) {
            TLog.w("%s requestSurface need run first initInGLThread in GL Thread", "SelesSurfaceReceiver");
            return null;
        }
        this.e();
        (this.c = new SelesSurfaceTexture(this.mSurfaceFBO.getTexture())).setOnFrameAvailableListener(this.m);
        return this.c;
    }
    
    public void setSurfaceTextureListener(final SurfaceTexture.OnFrameAvailableListener onFrameAvailableListener) {
        this.m = onFrameAvailableListener;
        if (this.c != null) {
            this.c.setOnFrameAvailableListener(onFrameAvailableListener);
        }
    }
    
    @Override
    public void updateSurfaceTexImage() {
        if (this.c == null) {
            TLog.w("%s updateSurfaceTexImage need run first newFrameReadyInGLThread in GL Thread", "SelesSurfaceReceiver");
            return;
        }
        this.c.updateTexImage();
    }
    
    public void forceUpdateSurfaceTexImage() {
        if (this.c == null) {
            TLog.w("%s updateSurfaceTexImage need run first newFrameReadyInGLThread in GL Thread", "SelesSurfaceReceiver");
            return;
        }
        this.c.forceUpdateTexImage();
    }
    
    public void updateSurfaceTexImage(final long n) {
        this.updateSurfaceTexImage();
        this.newFrameReadyInGLThread(n);
    }
    
    @Override
    public long getSurfaceTexTimestampNs() {
        if (this.c == null) {
            TLog.w("%s getSurfaceTexTimestampNs need run first newFrameReadyInGLThread in GL Thread", "SelesSurfaceReceiver");
            return 0L;
        }
        return this.c.getTimestamp();
    }
    
    @Override
    public void setSurfaceTexTimestampNs(final long defindTimestamp) {
        if (this.c == null) {
            TLog.w("%s setSurfaceTexTimestampNs need run first newFrameReadyInGLThread in GL Thread", "SelesSurfaceReceiver");
            return;
        }
        this.c.setDefindTimestamp(defindTimestamp);
    }
    
    @Override
    public void setTextureCoordinateBuilder(final SelesVerticeCoordinateCorpBuilder l) {
        this.l = l;
        if (this.l != null && this.n != null) {
            this.l.setPreCropRect(this.n);
        }
    }
    
    @Override
    public void setPreCropRect(final RectF n) {
        this.n = n;
        if (this.l != null) {
            this.l.setPreCropRect(this.n);
        }
    }
    
    public void setEnableClip(final boolean enableClip) {
        if (this.l != null) {
            this.l.setEnableClip(enableClip);
        }
    }
    
    public TuSdkSize setOutputRatio(final float outputRatio) {
        if (this.l != null) {
            return this.l.setOutputRatio(outputRatio);
        }
        return null;
    }
    
    public void setOutputSize(final TuSdkSize tuSdkSize) {
        this.b = tuSdkSize;
        if (this.l != null) {
            this.l.setOutputSize(tuSdkSize);
        }
    }
    
    public void setCropRect(final RectF cropRect) {
        if (this.l != null) {
            this.l.setCropRect(cropRect);
        }
    }
    
    public void setCanvasColor(final float p4, final float q, final float r, final float s) {
        this.p = p4;
        this.q = q;
        this.r = r;
        this.s = s;
    }
    
    public void setCanvasColor(final int n) {
        this.setCanvasColor(Color.red(n) / 255.0f, Color.green(n) / 255.0f, Color.blue(n) / 255.0f, Color.alpha(n) / 255.0f);
    }
    
    public SelesSurfaceReceiver() {
        this.a = ImageOrientation.Up;
        this.j = false;
        this.k = -1L;
        this.o = false;
        this.p = 0.0f;
        this.q = 0.0f;
        this.r = 0.0f;
        this.s = 1.0f;
        this.t = new LinkedHashMap<SelesContext.SelesInput, Integer>();
        this.d = SelesFilter.buildBuffer(SelesFilter.imageVertices);
        this.e = SelesFilter.buildBuffer(SelesFilter.noRotationTextureCoordinates);
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesSurfaceReceiver.this.a();
            }
        });
    }
    
    private void a() {
        this.k = Thread.currentThread().getId();
        this.f = SelesContext.program("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", "#extension GL_OES_EGL_image_external : require\nvarying highp vec2 textureCoordinate;uniform samplerExternalOES inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}");
        if (!this.f.isInitialized()) {
            this.f.addAttribute("position");
            this.f.addAttribute("inputTextureCoordinate");
            if (!this.f.link()) {
                TLog.i("%s Program link log: %s", "SelesSurfaceReceiver", this.f.getProgramLog());
                TLog.i("%s Fragment shader compile log: %s", "SelesSurfaceReceiver", this.f.getFragmentShaderLog());
                TLog.i("%s Vertex link log: %s", "SelesSurfaceReceiver", this.f.getVertexShaderLog());
                this.f = null;
                TLog.e("%s Filter shader link failed: %s", "SelesSurfaceReceiver", this.getClass());
                return;
            }
        }
        this.g = this.f.attributeIndex("position");
        this.h = this.f.attributeIndex("inputTextureCoordinate");
        this.i = this.f.uniformIndex("inputImageTexture");
        SelesContext.setActiveShaderProgram(this.f);
        GLES20.glEnableVertexAttribArray(this.g);
        GLES20.glEnableVertexAttribArray(this.h);
        this.initSurfaceFBO();
        this.j = true;
        this.o = true;
    }
    
    protected void initSurfaceFBO() {
        this.mSurfaceFBO = SelesContext.sharedFramebufferCache().fetchTextureOES();
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
    public void newFrameReadyInGLThread(final long n) {
        if (this.k != Thread.currentThread().getId()) {
            TLog.w("%s newFrameReadyInGLThread need run in GL thread", "SelesSurfaceReceiver");
            return;
        }
        if (this.l != null && this.l.calculate(this.mInputTextureSize, this.a, this.d, this.e)) {
            this.b = this.l.outputSize();
        }
        else {
            this.e.clear();
            this.e.put(SelesFilter.textureCoordinates(this.a)).position(0);
            this.b = this.mInputTextureSize;
        }
        this.renderToTexture(this.d, this.e);
        this.a(n);
    }
    
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        this.runPendingOnDrawTasks();
        if (!this.b.isSize()) {
            return;
        }
        SelesContext.setActiveShaderProgram(this.f);
        this.b();
        GLES20.glClearColor(this.p, this.q, this.r, this.s);
        GLES20.glClear(16384);
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(36197, this.d());
        GLES20.glUniform1i(this.i, 2);
        GLES20.glEnableVertexAttribArray(this.g);
        GLES20.glEnableVertexAttribArray(this.h);
        GLES20.glVertexAttribPointer(this.g, 2, 5126, false, 0, (Buffer)floatBuffer);
        GLES20.glVertexAttribPointer(this.h, 2, 5126, false, 0, (Buffer)floatBuffer2);
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glBindTexture(36197, 0);
    }
    
    private void b() {
        if (this.j || this.mOutputFramebuffer == null) {
            this.c();
            (this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, this.outputFrameSize())).disableReferenceCounting();
            this.j = false;
        }
        this.mOutputFramebuffer.activateFramebuffer();
    }
    
    public void genOutputFrameBuffer(final TuSdkSize tuSdkSize) {
        if (this.mOutputFramebuffer == null) {
            (this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, tuSdkSize)).disableReferenceCounting();
        }
    }
    
    private void c() {
        if (this.mOutputFramebuffer == null) {
            return;
        }
        this.mOutputFramebuffer.clearAllLocks();
        SelesContext.returnFramebufferToCache(this.mOutputFramebuffer);
        this.mOutputFramebuffer = null;
    }
    
    private int d() {
        int texture = 0;
        if (this.mSurfaceFBO != null) {
            texture = this.mSurfaceFBO.getTexture();
        }
        return texture;
    }
    
    private void a(final long n) {
        this.t.clear();
        for (final SelesContext.SelesInput selesInput : this.mTargets) {
            if (!selesInput.isEnabled()) {
                continue;
            }
            if (selesInput == this.getTargetToIgnoreForUpdates()) {
                continue;
            }
            final int intValue = this.mTargetTextureIndices.get(this.mTargets.indexOf(selesInput));
            this.t.put(selesInput, intValue);
            this.setInputFramebufferForTarget(selesInput, intValue);
            selesInput.setInputSize(this.outputFrameSize(), intValue);
        }
        for (final Map.Entry<SelesContext.SelesInput, Integer> entry : this.t.entrySet()) {
            entry.getKey().newFrameReady(n, entry.getValue());
        }
    }
    
    public TuSdkSize outputFrameSize() {
        return (this.b == null) ? this.mInputTextureSize : this.b;
    }
    
    @Override
    public void setInputSize(TuSdkSize transforOrientation) {
        if (transforOrientation == null || !transforOrientation.isSize()) {
            return;
        }
        transforOrientation = transforOrientation.transforOrientation(this.a);
        if (this.mInputTextureSize.equals(transforOrientation)) {
            return;
        }
        this.mInputTextureSize = transforOrientation;
        this.j = true;
    }
    
    @Override
    public void setInputRotation(final ImageOrientation a) {
        if (a == null || a == this.a) {
            return;
        }
        final TuSdkSize transforOrientation = this.mInputTextureSize.transforOrientation(this.a);
        this.a = a;
        this.mInputTextureSize = transforOrientation.transforOrientation(a);
        this.j = true;
    }
    
    @Override
    protected void onDestroy() {
        this.c();
        this.e();
        if (this.mSurfaceFBO != null) {
            this.mSurfaceFBO.destroy();
            this.mSurfaceFBO = null;
        }
    }
    
    private void e() {
        if (this.c == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 14) {
            this.c.release();
        }
        this.c = null;
    }
}
