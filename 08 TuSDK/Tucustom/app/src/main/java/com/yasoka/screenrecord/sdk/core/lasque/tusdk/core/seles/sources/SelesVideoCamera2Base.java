// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources;

import java.nio.Buffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
import java.util.LinkedList;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.TuSdkDate;
//import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import java.util.Queue;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.content.Context;
//import org.lasque.tusdk.core.seles.SelesGLProgram;
import java.nio.FloatBuffer;
import android.annotation.TargetApi;
import android.opengl.GLSurfaceView;
import android.graphics.SurfaceTexture;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesGLProgram;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkDate;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

@TargetApi(21)
public class SelesVideoCamera2Base extends SelesOutput implements SurfaceTexture.OnFrameAvailableListener, GLSurfaceView.Renderer
{
    private static final float[] b;
    private static final float[] c;
    private FloatBuffer d;
    private FloatBuffer e;
    private SelesGLProgram f;
    private int g;
    private int h;
    private int i;
    private int j;
    private final Context k;
    private boolean l;
    protected ImageOrientation mOutputRotation;
    private boolean m;
    private final Queue<Runnable> n;
    private final Queue<Runnable> o;
    private boolean p;
    private boolean q;
    private long r;
    private InterfaceOrientation s;
    private boolean t;
    private boolean u;
    private SurfaceTexture v;
    private boolean w;
    private TuSdkDate x;
    private long y;
    private long z;
    private boolean A;
    private SelesVideoCamera2Engine B;
    static final /* synthetic */ boolean a;
    
    public Context getContext() {
        return this.k;
    }
    
    public InterfaceOrientation getOutputImageOrientation() {
        return this.s;
    }
    
    public void setOutputImageOrientation(final InterfaceOrientation s) {
        if (s == null) {
            return;
        }
        this.s = s;
        this.p = true;
    }
    
    public boolean isHorizontallyMirrorFrontFacingCamera() {
        return this.t;
    }
    
    public void setHorizontallyMirrorFrontFacingCamera(final boolean t) {
        this.t = t;
        this.p = true;
    }
    
    public boolean isHorizontallyMirrorRearFacingCamera() {
        return this.u;
    }
    
    public void setHorizontallyMirrorRearFacingCamera(final boolean u) {
        this.u = u;
        this.p = true;
    }
    
    public boolean isCapturing() {
        return this.m;
    }
    
    public boolean isCapturePaused() {
        return this.l;
    }
    
    public boolean hasCreateSurface() {
        return this.v != null;
    }
    
    public boolean getRunBenchmark() {
        return this.A;
    }
    
    public void setRunBenchmark(final boolean a) {
        this.A = a;
    }
    
    public void setCameraEngine(final SelesVideoCamera2Engine b) {
        if (!SelesVideoCamera2Base.a && b == null) {
            throw new AssertionError();
        }
        this.B = b;
    }
    
    public SelesVideoCamera2Base(final Context k) {
        this.s = InterfaceOrientation.Portrait;
        TLog.i("Used Camera 2 Api", new Object[0]);
        this.k = k;
        this.n = new LinkedList<Runnable>();
        this.o = new LinkedList<Runnable>();
        this.mOutputRotation = ImageOrientation.Up;
        this.setOutputImageOrientation(InterfaceOrientation.Portrait);
        this.b();
        this.j();
    }
    
    @Override
    protected void onDestroy() {
        this.stopCameraCapture();
    }
    
    public float averageFrameDurationDuringCapture() {
        return this.y / (float)(this.z - 1L);
    }
    
    public void resetBenchmarkAverage() {
        this.z = 0L;
        this.y = 0L;
    }
    
    private SelesFramebuffer a() {
        final SelesFramebuffer fetchFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, this.mInputTextureSize);
        fetchFramebuffer.disableReferenceCounting();
        return fetchFramebuffer;
    }
    
    private void b() {
        if (this.f != null) {
            return;
        }
        this.d = SelesFilter.buildBuffer(SelesVideoCamera2Base.b);
        this.e = SelesFilter.buildBuffer(SelesVideoCamera2Base.c);
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesVideoCamera2Base.this.f = SelesContext.program("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", "#extension GL_OES_EGL_image_external : require\nvarying highp vec2 textureCoordinate;uniform samplerExternalOES inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}");
                if (!SelesVideoCamera2Base.this.f.isInitialized()) {
                    SelesVideoCamera2Base.this.f.addAttribute("position");
                    SelesVideoCamera2Base.this.f.addAttribute("inputTextureCoordinate");
                    if (!SelesVideoCamera2Base.this.f.link()) {
                        TLog.i("Program link log: %s", SelesVideoCamera2Base.this.f.getProgramLog());
                        TLog.i("Fragment shader compile log: %s", SelesVideoCamera2Base.this.f.getFragmentShaderLog());
                        TLog.i("Vertex link log: %s", SelesVideoCamera2Base.this.f.getVertexShaderLog());
                        SelesVideoCamera2Base.this.f = null;
                        TLog.e("Filter shader link failed: %s", this.getClass());
                        return;
                    }
                }
                SelesVideoCamera2Base.this.g = SelesVideoCamera2Base.this.f.attributeIndex("position");
                SelesVideoCamera2Base.this.h = SelesVideoCamera2Base.this.f.attributeIndex("inputTextureCoordinate");
                SelesVideoCamera2Base.this.i = SelesVideoCamera2Base.this.f.uniformIndex("inputImageTexture");
                SelesContext.setActiveShaderProgram(SelesVideoCamera2Base.this.f);
                GLES20.glEnableVertexAttribArray(SelesVideoCamera2Base.this.g);
                GLES20.glEnableVertexAttribArray(SelesVideoCamera2Base.this.h);
            }
        });
    }
    
    public void startCameraCapture() {
        if (ThreadHelper.isMainThread()) {
            this.onMainThreadStart();
            return;
        }
        ThreadHelper.post(new Runnable() {
            @Override
            public void run() {
                SelesVideoCamera2Base.this.startCameraCapture();
            }
        });
    }
    
    public void stopCameraCapture() {
        this.m = false;
        this.l = false;
        this.r = 0L;
        this.w = false;
        if (this.v != null) {
            TLog.d("mSurfaceTexture.release", new Object[0]);
            this.v.setOnFrameAvailableListener((SurfaceTexture.OnFrameAvailableListener)null);
            this.v.release();
            this.v = null;
        }
        if (this.A && this.x != null) {
            TLog.d("Capture frame time: %s ms", this.x.diffOfMillis());
            TLog.i("Average frame time: %s ms", this.averageFrameDurationDuringCapture());
        }
        this.resetBenchmarkAverage();
    }
    
    private void c() {
        if (this.v != null && this.w) {
            this.v.updateTexImage();
        }
    }
    
    protected void onMainThreadStart() {
        if (this.B == null) {
            TLog.d("You need setCameraEngine(SelesVideoCamera2Engine engine)", new Object[0]);
            return;
        }
        this.stopCameraCapture();
        if (!this.B.canInitCamera()) {
            return;
        }
        this.d();
    }
    
    private void d() {
        if (!this.B.onInitCamera()) {
            return;
        }
        final TuSdkSize previewOptimalSize = this.B.previewOptimalSize();
        if (previewOptimalSize != null) {
            this.mInputTextureSize = previewOptimalSize;
        }
        TLog.d("mInputTextureSize: %s", this.mInputTextureSize);
        this.p = true;
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesVideoCamera2Base.this.j = SelesVideoCamera2Base.this.e();
                SelesVideoCamera2Base.this.q = true;
                SelesVideoCamera2Base.this.v = new SurfaceTexture(SelesVideoCamera2Base.this.j);
                SelesVideoCamera2Base.this.w = true;
                SelesVideoCamera2Base.this.v.setDefaultBufferSize(SelesVideoCamera2Base.this.mInputTextureSize.width, SelesVideoCamera2Base.this.mInputTextureSize.height);
                SelesVideoCamera2Base.this.v.setOnFrameAvailableListener((SurfaceTexture.OnFrameAvailableListener)SelesVideoCamera2Base.this);
                SelesVideoCamera2Base.this.B.onCameraWillOpen(SelesVideoCamera2Base.this.v);
            }
        });
    }
    
    private int e() {
        final int[] array = { 0 };
        GLES20.glGenTextures(1, array, 0);
        GLES20.glBindTexture(36197, array[0]);
        GLES20.glTexParameteri(36197, 10241, 9729);
        GLES20.glTexParameteri(36197, 10240, 9729);
        GLES20.glTexParameteri(36197, 10242, 33071);
        GLES20.glTexParameteri(36197, 10243, 33071);
        return this.j = array[0];
    }
    
    protected void onCameraStarted() {
        if (this.B != null) {
            this.B.onCameraStarted();
        }
    }
    
    public void pauseCameraCapture() {
        this.l = true;
    }
    
    public void resumeCameraCapture() {
        this.l = false;
        if (this.v != null) {
            this.runOnDraw(new Runnable() {
                @Override
                public void run() {
                    SelesVideoCamera2Base.this.c();
                }
            });
        }
    }
    
    @Override
    public void addTarget(final SelesContext.SelesInput selesInput, final int n) {
        super.addTarget(selesInput, n);
        if (selesInput != null) {
            selesInput.setInputRotation(this.mOutputRotation, n);
        }
    }
    
    public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
        GLES20.glDisable(2929);
    }
    
    public void onSurfaceChanged(final GL10 gl10, final int n, final int n2) {
        GLES20.glViewport(0, 0, n, n2);
    }
    
    public void onDrawFrame(final GL10 gl10) {
        GLES20.glClear(16640);
        TuSdkDate create = null;
        final boolean b = !this.isOnDrawTasksEmpty();
        if (this.A && this.m && b) {
            create = TuSdkDate.create();
        }
        this.runPendingOnDrawTasks();
        this.i();
        if (!this.l) {
            this.c();
        }
        this.runPendingOnDrawEndTasks();
        this.a(create);
    }
    
    public void onFrameAvailable(final SurfaceTexture surfaceTexture) {
        this.h();
        if (!this.isOnDrawTasksEmpty() || this.l || this.f() || this.v == null) {
            return;
        }
        this.j();
        this.runOnDraw(new Runnable() {
            @TargetApi(15)
            @Override
            public void run() {
                SelesVideoCamera2Base.this.processVideoSampleBufferOES();
            }
        });
    }
    
    @TargetApi(15)
    protected void processVideoSampleBufferOES() {
        SelesContext.setActiveShaderProgram(this.f);
        this.g();
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(16384);
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(36197, this.j);
        GLES20.glUniform1i(this.i, 2);
        GLES20.glVertexAttribPointer(this.g, 2, 5126, false, 0, (Buffer)this.d);
        GLES20.glVertexAttribPointer(this.h, 2, 5126, false, 0, (Buffer)this.e);
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glBindTexture(36197, 0);
    }
    
    private boolean f() {
        if (this.r < 1L) {
            ++this.r;
            return true;
        }
        return false;
    }
    
    private void g() {
        if (this.q) {
            this.mOutputFramebuffer = this.a();
            this.q = false;
        }
        this.mOutputFramebuffer.activateFramebuffer();
    }
    
    private void h() {
        if (this.m) {
            return;
        }
        this.m = true;
        this.x = TuSdkDate.create();
        this.onCameraStarted();
    }
    
    private void i() {
        for (int i = 0; i < this.mTargets.size(); ++i) {
            final SelesContext.SelesInput selesInput = this.mTargets.get(i);
            if (selesInput.isEnabled()) {
                final int intValue = this.mTargetTextureIndices.get(i);
                selesInput.setInputRotation(this.mOutputRotation, intValue);
                if (selesInput != this.getTargetToIgnoreForUpdates()) {
                    selesInput.setInputSize(this.mInputTextureSize, intValue);
                    selesInput.setCurrentlyReceivingMonochromeInput(selesInput.wantsMonochromeInput());
                }
                selesInput.setInputFramebuffer(this.mOutputFramebuffer, intValue);
            }
        }
        for (int j = 0; j < this.mTargets.size(); ++j) {
            final SelesContext.SelesInput selesInput2 = this.mTargets.get(j);
            if (selesInput2.isEnabled()) {
                if (selesInput2 != this.getTargetToIgnoreForUpdates()) {
                    selesInput2.newFrameReady(System.nanoTime(), this.mTargetTextureIndices.get(j));
                }
            }
        }
    }
    
    private void a(final TuSdkDate tuSdkDate) {
        if (!this.A || tuSdkDate == null) {
            return;
        }
        ++this.z;
        if (this.z > 1L) {
            final long diffOfMillis = tuSdkDate.diffOfMillis();
            this.y += diffOfMillis;
            TLog.i("Average frame time: %s ms", this.averageFrameDurationDuringCapture());
            TLog.i("Current frame time: %s ms", diffOfMillis);
        }
    }
    
    private void j() {
        if (!this.p) {
            return;
        }
        this.p = false;
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesVideoCamera2Base.this.mOutputRotation = ((SelesVideoCamera2Base.this.B == null) ? ImageOrientation.Up : SelesVideoCamera2Base.this.B.previewOrientation());
                for (int i = 0; i < SelesVideoCamera2Base.this.mTargets.size(); ++i) {
                    SelesVideoCamera2Base.this.mTargets.get(i).setInputRotation(SelesVideoCamera2Base.this.mOutputRotation, SelesVideoCamera2Base.this.mTargetTextureIndices.get(i));
                }
            }
        });
    }
    
    @Override
    protected void runPendingOnDrawTasks() {
        this.a(this.n);
    }
    
    protected void runPendingOnDrawEndTasks() {
        this.a(this.o);
    }
    
    @Override
    protected boolean isOnDrawTasksEmpty() {
        boolean empty = false;
        synchronized (this.n) {
            empty = this.n.isEmpty();
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
    
    @Override
    protected void runOnDraw(final Runnable runnable) {
        synchronized (this.n) {
            this.n.add(runnable);
        }
    }
    
    protected void runOnDrawEnd(final Runnable runnable) {
        synchronized (this.o) {
            this.o.add(runnable);
        }
    }
    
    static {
        a = !SelesVideoCamera2Base.class.desiredAssertionStatus();
        b = new float[] { -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f };
        c = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f };
    }
}
