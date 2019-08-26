// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output;

//import org.lasque.tusdk.core.utils.TuSdkSemaphore;
import java.nio.IntBuffer;
import java.nio.Buffer;
//import org.lasque.tusdk.core.utils.TLog;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.utils.RectHelper;
import android.graphics.Rect;
import javax.microedition.khronos.egl.EGLContext;
//import org.lasque.tusdk.core.seles.SelesContext;
import android.opengl.Matrix;
//import org.lasque.tusdk.core.utils.TuSdkDeviceInfo;
//import org.lasque.tusdk.core.seles.SelesPixelBuffer;
import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.egl.SelesEGL10Core;
import android.os.Handler;
import android.os.HandlerThread;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesPixelBuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl.SelesEGL10Core;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkDeviceInfo;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class SelesOffscreenRotate extends SelesFilter
{
    public static final String ROTATE_VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nuniform mat4 transformMatrix;\nvarying vec2 textureCoordinate;\nvoid main()\n{\n    gl_Position = transformMatrix * vec4(position.xyz, 1.0);\n    textureCoordinate = inputTextureCoordinate.xy;\n}\n";
    public static final String ROTATE_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);void main(){     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);     mediump float luminance = dot(textureColor.rgb, luminanceWeighting);     gl_FragColor = vec4(vec3(luminance), textureColor.w);}";
    private HandlerThread a;
    private Handler b;
    private SelesEGL10Core c;
    private SelesOffscreenRotateDelegate d;
    private int e;
    private float f;
    private float g;
    private float[] h;
    private TuSdkSize i;
    private final FloatBuffer j;
    private float k;
    private SelesPixelBuffer l;
    private boolean m;
    private boolean n;
    private boolean o;
    
    public float getFullScale() {
        return this.k;
    }
    
    public void setDelegate(final SelesOffscreenRotateDelegate d) {
        this.d = d;
    }
    
    public void setSyncOutput(final boolean o) {
        if (this.o == o) {
            return;
        }
        this.o = o;
        if (this.mImageCaptureSemaphore != null) {
            this.mImageCaptureSemaphore.waitSignal(0L);
            this.mImageCaptureSemaphore.signal();
        }
    }
    
    public SelesOffscreenRotate() {
        super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nuniform mat4 transformMatrix;\nvarying vec2 textureCoordinate;\nvoid main()\n{\n    gl_Position = transformMatrix * vec4(position.xyz, 1.0);\n    textureCoordinate = inputTextureCoordinate.xy;\n}\n", "varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);void main(){     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);     mediump float luminance = dot(textureColor.rgb, luminanceWeighting);     gl_FragColor = vec4(vec3(luminance), textureColor.w);}");
        this.f = 0.0f;
        this.g = 0.0f;
        this.i = TuSdkSize.create(0, 0);
        this.k = 1.0f;
        this.m = true;
        this.n = false;
        this.o = false;
        this.m = TuSdkDeviceInfo.isSupportPbo();
        (this.a = new HandlerThread("SelesOffscreenRotate")).start();
        this.b = new Handler(this.a.getLooper());
        Matrix.setIdentityM(this.h = new float[16], 0);
        this.j = SelesFilter.buildBuffer(SelesFilter.imageVertices);
    }
    
    private void a() {
        if (this.l != null) {
            this.l.destory();
        }
        this.l = null;
    }
    
    private void b() {
        this.a();
        if (this.c != null) {
            this.c.destroy();
        }
        this.c = null;
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.b != null) {
            this.b.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    SelesOffscreenRotate.this.b();
                }
            });
        }
        if (this.a != null) {
            this.a.quit();
        }
        this.b = null;
        this.a = null;
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.e = this.mFilterProgram.uniformIndex("transformMatrix");
        this.a(this.h);
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    private void a(final float[] h) {
        this.setMatrix4f(this.h = h, this.e, this.mFilterProgram);
    }
    
    private void c() {
        if (this.mInputTextureSize == null || !this.mInputTextureSize.isSize() || this.c != null) {
            return;
        }
        final EGLContext currentEGLContext = SelesContext.currentEGLContext();
        if (currentEGLContext == null) {
            return;
        }
        this.b.post((Runnable)new Runnable() {
            @Override
            public void run() {
                SelesOffscreenRotate.this.a(currentEGLContext);
            }
        });
    }
    
    private void a(final EGLContext eglContext) {
        if (this.c != null) {
            return;
        }
        this.c = SelesEGL10Core.create(this.mInputTextureSize, eglContext);
        this.runPendingOnDrawTasks();
    }
    
    @Override
    public void setInputSize(TuSdkSize create, final int n) {
        super.setInputSize(create, n);
        if (this.mInputRotation.isTransposed()) {
            create = TuSdkSize.create(create.height, create.width);
        }
        if (this.i.equals(create)) {
            return;
        }
        if (n == 0 && create.isSize()) {
            this.i = create;
            this.k = create.maxMinRatio();
            this.d();
        }
    }
    
    private void d() {
        final TuSdkSize copy = this.mInputTextureSize.copy();
        final Rect rectWithAspectRatioInsideRect = RectHelper.makeRectWithAspectRatioInsideRect(this.i, new Rect(0, 0, copy.width, copy.height));
        final float n = rectWithAspectRatioInsideRect.width() / (float)copy.width;
        final float n2 = rectWithAspectRatioInsideRect.height() / (float)copy.height;
        final float[] src = { -n, -n2, n, -n2, -n, n2, n, n2 };
        this.j.clear();
        this.j.put(src).position(0);
    }
    
    @Override
    public void newFrameReady(final long n, final int n2) {
        if (this.mFirstInputFramebuffer == null) {
            return;
        }
        this.setEnabled(false);
        this.c();
        GLES20.glFinish();
        if (this.mImageCaptureSemaphore != null && !this.mImageCaptureSemaphore.waitSignal(0L)) {
            return;
        }
        this.b.post((Runnable)new Runnable() {
            @Override
            public void run() {
                if (SelesOffscreenRotate.this.c == null) {
                    if (SelesOffscreenRotate.this.mImageCaptureSemaphore != null) {
                        SelesOffscreenRotate.this.mImageCaptureSemaphore.signal();
                    }
                    return;
                }
                SelesOffscreenRotate.this.a(n, n2);
                if (SelesOffscreenRotate.this.mImageCaptureSemaphore != null) {
                    SelesOffscreenRotate.this.mImageCaptureSemaphore.signal();
                }
            }
        });
    }
    
    @Override
    public void setCurrentlyReceivingMonochromeInput(final boolean b) {
        if (!b || !this.o) {
            return;
        }
        if (this.mImageCaptureSemaphore != null) {
            this.mImageCaptureSemaphore.waitSignal(300L);
            this.mImageCaptureSemaphore.signal();
        }
    }
    
    private void a(final long n, final int n2) {
        super.newFrameReady(n, n2);
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        super.renderToTexture(this.j, floatBuffer2);
        this.checkGLError(this.getClass().getSimpleName());
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
        final SelesOffscreenRotateDelegate d = this.d;
        try {
            final TuSdkSize outputFrameSize = this.outputFrameSize();
            if (d == null || !outputFrameSize.isSize()) {
                return;
            }
            this.a(outputFrameSize);
            this.setEnabled(d.onFrameRendered(this));
        }
        catch (Exception ex) {
            TLog.w("Screen Rotate Delegate is null !!!", new Object[0]);
        }
    }
    
    private void a(final TuSdkSize tuSdkSize) {
        if (!this.m) {
            return;
        }
        if (this.l == null || !this.l.getSize().equals(tuSdkSize)) {
            this.a();
            this.l = SelesContext.fetchPixelBuffer(tuSdkSize, 1);
        }
        this.l.preparePackBuffer();
    }
    
    public int[] getAuthors() {
        if (this.l == null) {
            return null;
        }
        return this.l.getBefferInfo();
    }
    
    public Buffer readBuffer() {
        if (this.l == null) {
            return null;
        }
        return this.l.readPackBuffer();
    }
    
    public void setAngle(final float n) {
        this.g = this.f;
        this.f = (float)(30 * (((int)n + 15) / 30 % 12));
        Matrix.setIdentityM(this.h, 0);
        Matrix.rotateM(this.h, 0, this.f, 0.0f, 0.0f, 1.0f);
        this.setMatrix4f(this.h, this.e, this.mFilterProgram);
    }
    
    public float getAngle() {
        if (this.m) {
            return this.g;
        }
        return this.f;
    }
    
    public IntBuffer renderBuffer() {
        if (this.c == null) {
            return null;
        }
        return this.c.getImageBuffer();
    }
    
    public interface SelesOffscreenRotateDelegate
    {
        boolean onFrameRendered(final SelesOffscreenRotate p0);
    }
}
