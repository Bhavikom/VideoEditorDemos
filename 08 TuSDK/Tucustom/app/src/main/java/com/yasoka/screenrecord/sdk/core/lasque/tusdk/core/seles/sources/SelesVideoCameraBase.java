// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources;

//import org.lasque.tusdk.core.secret.ColorSpaceConvert;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.Buffer;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.graphics.ImageFormat;
import android.annotation.TargetApi;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;
//import org.lasque.tusdk.core.seles.SelesContext;
import android.os.Build;
import java.util.LinkedList;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.TuSdkDate;
//import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import java.util.Queue;
import java.nio.IntBuffer;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import javax.microedition.khronos.egl.EGLContext;
import android.content.Context;
//import org.lasque.tusdk.core.seles.SelesGLProgram;
import java.nio.FloatBuffer;
import android.opengl.GLSurfaceView;
import android.hardware.Camera;
import android.graphics.SurfaceTexture;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.ColorSpaceConvert;
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

public abstract class SelesVideoCameraBase extends SelesOutput implements SurfaceTexture.OnFrameAvailableListener, Camera.PreviewCallback, GLSurfaceView.Renderer
{
    public static final String SELES_PASSTHROUGH_FRAGMENT_SHADER_OES = "#extension GL_OES_EGL_image_external : require\nvarying highp vec2 textureCoordinate;uniform samplerExternalOES inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}";
    private static final float[] b;
    private static final float[] c;
    private FloatBuffer d;
    private FloatBuffer e;
    private SelesGLProgram f;
    private int g;
    private int h;
    private int i;
    private int j;
    private boolean k;
    private boolean l;
    private Context m;
    private EGLContext n;
    private boolean o;
    protected ImageOrientation mOutputRotation;
    private boolean p;
    private IntBuffer q;
    private int r;
    private final Queue<Runnable> s;
    private final Queue<Runnable> t;
    private boolean u;
    private boolean v;
    private long w;
    private InterfaceOrientation x;
    private boolean y;
    private boolean z;
    private SurfaceTexture A;
    private boolean B;
    private TuSdkDate C;
    private long D;
    private long E;
    private boolean F;
    private SelesVideoCameraEngine G;
    private Camera H;
    private boolean I;
    static final /* synthetic */ boolean a;
    
    public Context getContext() {
        return this.m;
    }
    
    public InterfaceOrientation getOutputImageOrientation() {
        return this.x;
    }
    
    public void setOutputImageOrientation(final InterfaceOrientation x) {
        if (x == null) {
            return;
        }
        this.x = x;
        this.u = true;
    }
    
    public boolean isHorizontallyMirrorFrontFacingCamera() {
        return this.y;
    }
    
    public void setHorizontallyMirrorFrontFacingCamera(final boolean y) {
        this.y = y;
        this.u = true;
    }
    
    public boolean isHorizontallyMirrorRearFacingCamera() {
        return this.z;
    }
    
    public void setHorizontallyMirrorRearFacingCamera(final boolean z) {
        this.z = z;
        this.u = true;
    }
    
    public boolean isCapturing() {
        return this.p;
    }
    
    public boolean isCapturePaused() {
        return this.o;
    }
    
    public boolean hasCreateSurface() {
        return this.A != null;
    }
    
    public boolean getRunBenchmark() {
        return this.F;
    }
    
    public void setRunBenchmark(final boolean f) {
        this.F = f;
    }
    
    public void setCameraEngine(final SelesVideoCameraEngine g) {
        if (!SelesVideoCameraBase.a && g == null) {
            throw new AssertionError();
        }
        this.G = g;
    }
    
    public Camera inputCamera() {
        return this.H;
    }
    
    public SelesVideoCameraBase(final Context m) {
        this.x = InterfaceOrientation.Portrait;
        this.F = false;
        this.I = true;
        TLog.i("Used Camera 1 Api", new Object[0]);
        this.m = m;
        this.s = new LinkedList<Runnable>();
        this.t = new LinkedList<Runnable>();
        this.mOutputRotation = ImageOrientation.Up;
        this.setOutputImageOrientation(InterfaceOrientation.Portrait);
        this.k = (Build.VERSION.SDK_INT > 14 && SelesContext.isSupportOESImageExternal());
        this.a();
        this.m();
    }
    
    protected boolean getEnableFixedFramerate() {
        return this.I;
    }
    
    @Override
    protected void onDestroy() {
        this.stopCameraCapture();
        this.f();
    }
    
    public float averageFrameDurationDuringCapture() {
        return this.D / (float)(this.E - 1L);
    }
    
    public void resetBenchmarkAverage() {
        this.E = 0L;
        this.D = 0L;
    }
    
    private void a() {
        if (!this.k || this.f != null) {
            return;
        }
        this.d = SelesFilter.buildBuffer(SelesVideoCameraBase.b);
        this.e = SelesFilter.buildBuffer(SelesVideoCameraBase.c);
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesVideoCameraBase.this.f = SelesContext.program("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", "#extension GL_OES_EGL_image_external : require\nvarying highp vec2 textureCoordinate;uniform samplerExternalOES inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}");
                if (!SelesVideoCameraBase.this.f.isInitialized()) {
                    SelesVideoCameraBase.this.f.addAttribute("position");
                    SelesVideoCameraBase.this.f.addAttribute("inputTextureCoordinate");
                    if (!SelesVideoCameraBase.this.f.link()) {
                        TLog.i("Program link log: %s", SelesVideoCameraBase.this.f.getProgramLog());
                        TLog.i("Fragment shader compile log: %s", SelesVideoCameraBase.this.f.getFragmentShaderLog());
                        TLog.i("Vertex link log: %s", SelesVideoCameraBase.this.f.getVertexShaderLog());
                        SelesVideoCameraBase.this.f = null;
                        TLog.e("Filter shader link failed: %s", this.getClass());
                        return;
                    }
                }
                SelesVideoCameraBase.this.g = SelesVideoCameraBase.this.f.attributeIndex("position");
                SelesVideoCameraBase.this.h = SelesVideoCameraBase.this.f.attributeIndex("inputTextureCoordinate");
                SelesVideoCameraBase.this.i = SelesVideoCameraBase.this.f.uniformIndex("inputImageTexture");
                SelesContext.setActiveShaderProgram(SelesVideoCameraBase.this.f);
                GLES20.glEnableVertexAttribArray(SelesVideoCameraBase.this.g);
                GLES20.glEnableVertexAttribArray(SelesVideoCameraBase.this.h);
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
                SelesVideoCameraBase.this.startCameraCapture();
            }
        });
    }
    
    public void stopCameraCapture() {
        this.p = false;
        this.o = false;
        this.w = 0L;
        this.B = false;
        if (this.A != null) {
            this.A.setOnFrameAvailableListener((SurfaceTexture.OnFrameAvailableListener)null);
            if (Build.VERSION.SDK_INT >= 14) {
                this.b();
            }
            this.A = null;
        }
        if (this.H != null) {
            try {
                this.H.setPreviewCallback((Camera.PreviewCallback)null);
                this.H.cancelAutoFocus();
                this.H.stopPreview();
                this.H.release();
            }
            catch (Exception ex) {
                TLog.e(ex, "SelesVideoCamera stopCameraCapture", new Object[0]);
            }
            finally {
                this.H = null;
            }
        }
        this.resetBenchmarkAverage();
    }
    
    protected void startPreviewCallback() {
        if (this.l) {
            return;
        }
        this.l = true;
    }
    
    protected void stopPreviewCallback() {
        if (!this.l || this.H == null) {
            return;
        }
        this.H.setPreviewCallback((Camera.PreviewCallback)null);
        if (this.q != null) {
            this.q.clear();
            this.q = null;
        }
        this.l = false;
    }
    
    @TargetApi(14)
    private void b() {
        if (this.A != null) {
            this.A.release();
        }
    }
    
    public SurfaceTexture getSurfaceTexture() {
        return this.A;
    }
    
    private void c() {
        if (this.A != null && this.B) {
            this.A.updateTexImage();
        }
    }
    
    protected void onMainThreadStart() {
        if (this.G == null) {
            TLog.d("You need setCameraEngine(SelesVideoCameraEngine engine)", new Object[0]);
            return;
        }
        this.stopCameraCapture();
        if (!this.G.canInitCamera()) {
            return;
        }
        this.d();
    }
    
    private void d() {
        this.H = this.G.onInitCamera();
        if (this.H == null) {
            return;
        }
        final TuSdkSize previewOptimalSize = this.G.previewOptimalSize();
        if (previewOptimalSize != null) {
            this.mInputTextureSize = previewOptimalSize;
        }
        TLog.d("mInputTextureSize: %s", this.mInputTextureSize);
        this.u = true;
        if (!this.k || this.l) {
            final int capacity = this.mInputTextureSize.width * this.mInputTextureSize.height;
            this.r = capacity * ImageFormat.getBitsPerPixel(this.H.getParameters().getPreviewFormat()) / 8;
            this.q = IntBuffer.allocate(capacity);
        }
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                if (SelesVideoCameraBase.this.k) {
                    SelesVideoCameraBase.this.j = SelesVideoCameraBase.this.g();
                    if (SelesVideoCameraBase.this.l) {
                        SelesVideoCameraBase.this.i();
                        SelesVideoCameraBase.this.H.setPreviewCallbackWithBuffer((Camera.PreviewCallback)SelesVideoCameraBase.this);
                    }
                }
                else {
                    final int[] array = { 0 };
                    GLES20.glGenTextures(1, array, 0);
                    SelesVideoCameraBase.this.j = array[0];
                    SelesVideoCameraBase.this.i();
                    SelesVideoCameraBase.this.H.setPreviewCallbackWithBuffer((Camera.PreviewCallback)SelesVideoCameraBase.this);
                }
                SelesVideoCameraBase.this.v = true;
                SelesVideoCameraBase.this.A = new SurfaceTexture(SelesVideoCameraBase.this.j);
                SelesVideoCameraBase.this.B = true;
                SelesVideoCameraBase.this.A.setOnFrameAvailableListener((SurfaceTexture.OnFrameAvailableListener)SelesVideoCameraBase.this);
                SelesVideoCameraBase.this.G.onCameraWillOpen(SelesVideoCameraBase.this.A);
                SelesVideoCameraBase.this.h();
            }
        });
    }
    
    private SelesFramebuffer e() {
        this.f();
        if (this.k) {
            final SelesFramebuffer fetchFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, this.mInputTextureSize);
            fetchFramebuffer.disableReferenceCounting();
            this.n = SelesContext.currentEGLContext();
            return fetchFramebuffer;
        }
        final int[] array = { 0 };
        GLES20.glGenTextures(1, array, 0);
        GLES20.glBindTexture(3553, array[0]);
        GLES20.glTexParameterf(3553, 10240, 9729.0f);
        GLES20.glTexParameterf(3553, 10241, 9729.0f);
        GLES20.glTexParameterf(3553, 10242, 33071.0f);
        GLES20.glTexParameterf(3553, 10243, 33071.0f);
        GLES20.glTexImage2D(3553, 0, 6408, this.mInputTextureSize.width, this.mInputTextureSize.height, 0, 6408, 5121, (Buffer)null);
        return SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.PACKAGE, this.mInputTextureSize, array[0]);
    }
    
    private void f() {
        if (this.mOutputFramebuffer == null) {
            return;
        }
        this.mOutputFramebuffer.clearAllLocks();
        SelesContext.returnFramebufferToCache(this.mOutputFramebuffer);
        this.mOutputFramebuffer = null;
    }
    
    @TargetApi(15)
    private int g() {
        final int[] array = { 0 };
        GLES20.glGenTextures(1, array, 0);
        GLES20.glBindTexture(36197, array[0]);
        GLES20.glTexParameteri(36197, 10241, 9729);
        GLES20.glTexParameteri(36197, 10240, 9729);
        GLES20.glTexParameteri(36197, 10242, 33071);
        GLES20.glTexParameteri(36197, 10243, 33071);
        return this.j = array[0];
    }
    
    private void h() {
        if (this.H == null) {
            return;
        }
        try {
            this.H.startPreview();
            this.onPreviewStarted();
        }
        catch (Exception ex) {
            TLog.e(ex, "startPreview", new Object[0]);
        }
    }
    
    protected void onPreviewStarted() {
    }
    
    protected void onCameraStarted() {
        if (this.G != null) {
            this.G.onCameraStarted();
        }
    }
    
    public void pauseCameraCapture() {
        this.o = true;
    }
    
    public void resumeCameraCapture() {
        if (this.o) {
            this.h();
        }
        this.o = false;
        this.i();
        if (this.A != null) {
            this.runOnDraw(new Runnable() {
                @Override
                public void run() {
                    SelesVideoCameraBase.this.c();
                }
            });
        }
    }
    
    private void i() {
        if ((this.k && !this.l) || this.H == null) {
            return;
        }
        this.H.addCallbackBuffer(new byte[this.r]);
        this.H.addCallbackBuffer(new byte[this.r]);
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
        if (this.F && this.p && b) {
            create = TuSdkDate.create();
        }
        this.runPendingOnDrawTasks();
        this.updateTargetsForVideoCameraUsingCacheTexture();
        if (!this.o && this.getEnableFixedFramerate()) {
            this.c();
        }
        this.runPendingOnDrawEndTasks();
        this.a(create);
    }
    
    public void onPreviewFrame(final byte[] array, final Camera camera) {
        if (this.o) {
            return;
        }
        if (this.k && this.l) {
            if (!this.j()) {
                this.processFrameData(array);
            }
            camera.addCallbackBuffer(array);
            return;
        }
        this.l();
        if (!this.isOnDrawTasksEmpty() || this.j()) {
            camera.addCallbackBuffer(array);
            return;
        }
        this.m();
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesVideoCameraBase.this.a(array);
                camera.addCallbackBuffer(array);
            }
        });
    }
    
    protected void processFrameData(final byte[] array) {
    }
    
    private void a(final byte[] array) {
        ColorSpaceConvert.nv21ToRgba(array, this.mInputTextureSize.width, this.mInputTextureSize.height, this.q.array());
        this.k();
        GLES20.glBindTexture(3553, this.mOutputFramebuffer.getTexture());
        GLES20.glTexSubImage2D(3553, 0, 0, 0, this.mInputTextureSize.width, this.mInputTextureSize.height, 6408, 5121, (Buffer)this.q);
        GLES20.glBindTexture(3553, 0);
    }
    
    public void onFrameAvailable(final SurfaceTexture surfaceTexture) {
        if (!this.k) {
            return;
        }
        this.l();
        if (this.getEnableFixedFramerate()) {
            if (!this.isOnDrawTasksEmpty() || this.o || this.j()) {
                return;
            }
        }
        else if (this.o || this.j()) {
            return;
        }
        this.m();
        this.runOnDraw(new Runnable() {
            @TargetApi(15)
            @Override
            public void run() {
                if (!SelesVideoCameraBase.this.getEnableFixedFramerate()) {
                    SelesVideoCameraBase.this.c();
                }
                SelesVideoCameraBase.this.processVideoSampleBufferOES();
            }
        });
        if (!this.getEnableFixedFramerate()) {
            this.updateCameraView();
        }
    }
    
    protected void updateCameraView() {
    }
    
    @TargetApi(15)
    protected void processVideoSampleBufferOES() {
        SelesContext.setActiveShaderProgram(this.f);
        this.k();
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
    
    private boolean j() {
        if (this.w < 1L) {
            ++this.w;
            return true;
        }
        return false;
    }
    
    private void k() {
        if (this.v) {
            this.mOutputFramebuffer = this.e();
            this.v = false;
        }
        this.mOutputFramebuffer.activateFramebuffer();
    }
    
    private void l() {
        if (this.p) {
            return;
        }
        this.p = true;
        this.C = TuSdkDate.create();
        this.onCameraStarted();
    }
    
    protected void updateTargetsForVideoCameraUsingCacheTexture() {
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
        if (!this.F || tuSdkDate == null) {
            return;
        }
        ++this.E;
        if (this.E > 1L) {
            final long diffOfMillis = tuSdkDate.diffOfMillis();
            this.E %= 200L;
            if (this.E == 0L) {
                this.D = 0L;
            }
            this.D += diffOfMillis;
            TLog.i("Frame time Average[%s ms], Current[%s ms]", this.averageFrameDurationDuringCapture(), diffOfMillis);
        }
    }
    
    private void m() {
        if (!this.u) {
            return;
        }
        this.u = false;
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesVideoCameraBase.this.mOutputRotation = ((SelesVideoCameraBase.this.G == null) ? ImageOrientation.Up : SelesVideoCameraBase.this.G.previewOrientation());
                for (int i = 0; i < SelesVideoCameraBase.this.mTargets.size(); ++i) {
                    SelesVideoCameraBase.this.mTargets.get(i).setInputRotation(SelesVideoCameraBase.this.mOutputRotation, SelesVideoCameraBase.this.mTargetTextureIndices.get(i));
                }
            }
        });
    }
    
    @Override
    protected void runPendingOnDrawTasks() {
        this.a(this.s);
    }
    
    protected void runPendingOnDrawEndTasks() {
        this.a(this.t);
    }
    
    @Override
    protected boolean isOnDrawTasksEmpty() {
        boolean empty = false;
        synchronized (this.s) {
            empty = this.s.isEmpty();
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
        synchronized (this.s) {
            this.s.add(runnable);
        }
    }
    
    protected void runOnDrawEnd(final Runnable runnable) {
        synchronized (this.t) {
            this.t.add(runnable);
        }
    }
    
    static {
        a = !SelesVideoCameraBase.class.desiredAssertionStatus();
        b = new float[] { -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f };
        c = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f };
    }
}
