// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output;

import java.nio.Buffer;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;
import java.util.concurrent.LinkedBlockingQueue;
import android.graphics.Color;
//import org.lasque.tusdk.core.seles.sources.SelesWatermark;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesGLProgram;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesWatermark;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.SelesGLProgram;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
import java.util.concurrent.BlockingQueue;

public class SelesSurfacePusher implements SelesSurfaceDisplay
{
    public static final float[] noRotationTextureCoordinates;
    public static final float[] rotateRightTextureCoordinates;
    public static final float[] rotateLeftTextureCoordinates;
    public static final float[] verticalFlipTextureCoordinates;
    public static final float[] horizontalFlipTextureCoordinates;
    public static final float[] rotateRightVerticalFlipTextureCoordinates;
    public static final float[] rotateRightHorizontalFlipTextureCoordinates;
    public static final float[] rotate180TextureCoordinates;
    public static final float[] imageVertices;
    private final BlockingQueue<Runnable> a;
    protected SelesFramebuffer mInputFramebufferForDisplay;
    protected SelesGLProgram mDisplayProgram;
    private int b;
    private int c;
    private int d;
    protected TuSdkSize mInputImageSize;
    private FloatBuffer e;
    private FloatBuffer f;
    private float g;
    private float h;
    private float i;
    private float j;
    private ImageOrientation k;
    private TuSdkSize l;
    private boolean m;
    private SelesVerticeCoordinateBuilder n;
    private SelesWatermark o;
    
    public static float[] textureCoordinates(ImageOrientation up) {
        if (up == null) {
            up = ImageOrientation.Up;
        }
        switch (up.ordinal()) {
            case 1: {
                return SelesSurfacePusher.rotateLeftTextureCoordinates;
            }
            case 2: {
                return SelesSurfacePusher.rotateRightTextureCoordinates;
            }
            case 3: {
                return SelesSurfacePusher.verticalFlipTextureCoordinates;
            }
            case 4: {
                return SelesSurfacePusher.horizontalFlipTextureCoordinates;
            }
            case 5: {
                return SelesSurfacePusher.rotateRightVerticalFlipTextureCoordinates;
            }
            case 6: {
                return SelesSurfacePusher.rotateRightHorizontalFlipTextureCoordinates;
            }
            case 7: {
                return SelesSurfacePusher.rotate180TextureCoordinates;
            }
            default: {
                return SelesSurfacePusher.noRotationTextureCoordinates;
            }
        }
    }
    
    public TuSdkSize getInputImageSize() {
        return this.mInputImageSize;
    }
    
    public ImageOrientation getInputRotation() {
        return this.k;
    }
    
    @Override
    public void setEnabled(final boolean m) {
        this.m = m;
    }
    
    @Override
    public void setTextureCoordinateBuilder(final SelesVerticeCoordinateBuilder n) {
        this.n = n;
    }
    
    @Override
    public void setBackgroundColor(final float g, final float h, final float i, final float j) {
        this.g = g;
        this.h = h;
        this.i = i;
        this.j = j;
    }
    
    public void setBackgroundColor(final int n) {
        this.setBackgroundColor(Color.red(n) / 255.0f, Color.green(n) / 255.0f, Color.blue(n) / 255.0f, Color.alpha(n) / 255.0f);
    }
    
    @Override
    public void setWatermark(final SelesWatermark o) {
        this.o = o;
    }
    
    public SelesSurfacePusher() {
        this("varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}");
    }
    
    public SelesSurfacePusher(final String s) {
        this("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", s);
    }
    
    public SelesSurfacePusher(final String s, final String s2) {
        this.mInputImageSize = new TuSdkSize();
        this.j = 1.0f;
        this.a = new LinkedBlockingQueue<Runnable>();
        this.m = true;
        this.k = ImageOrientation.Up;
        this.e = SelesFilter.buildBuffer(SelesSurfacePusher.imageVertices);
        this.f = SelesFilter.buildBuffer(SelesSurfacePusher.noRotationTextureCoordinates);
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesSurfacePusher.this.a(s, s2);
                SelesSurfacePusher.this.onInitOnGLThread();
            }
        });
    }
    
    @Override
    public void destroy() {
        this.a();
        if (this.o != null) {
            this.o.destroy();
            this.o = null;
        }
    }
    
    @Override
    protected void finalize() {
        this.destroy();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    protected void onInitOnGLThread() {
    }
    
    private void a(final String s, final String s2) {
        this.mDisplayProgram = SelesContext.program(s, s2);
        if (!this.mDisplayProgram.isInitialized()) {
            this.initializeAttributes();
            if (!this.mDisplayProgram.link()) {
                TLog.i("Program link log: %s", this.mDisplayProgram.getProgramLog());
                TLog.i("Fragment shader compile log: %s", this.mDisplayProgram.getFragmentShaderLog());
                TLog.i("Vertex link log: %s", this.mDisplayProgram.getVertexShaderLog());
                this.mDisplayProgram = null;
                TLog.e("Filter shader link failed: %s", this.getClass());
                return;
            }
        }
        this.b = this.mDisplayProgram.attributeIndex("position");
        this.c = this.mDisplayProgram.attributeIndex("inputTextureCoordinate");
        this.d = this.mDisplayProgram.uniformIndex("inputImageTexture");
        SelesContext.setActiveShaderProgram(this.mDisplayProgram);
        GLES20.glEnableVertexAttribArray(this.b);
        GLES20.glEnableVertexAttribArray(this.c);
    }
    
    protected void initializeAttributes() {
        this.mDisplayProgram.addAttribute("position");
        this.mDisplayProgram.addAttribute("inputTextureCoordinate");
    }
    
    private void a(final long n, final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        if (this.mInputFramebufferForDisplay == null) {
            return;
        }
        SelesContext.setActiveShaderProgram(this.mDisplayProgram);
        GLES20.glBindFramebuffer(36160, 0);
        GLES20.glViewport(0, 0, this.maximumOutputSize().width, this.maximumOutputSize().height);
        GLES20.glClearColor(this.g, this.h, this.i, this.j);
        GLES20.glClear(16640);
        GLES20.glActiveTexture(33988);
        GLES20.glBindTexture(3553, this.b());
        GLES20.glUniform1i(this.d, 4);
        GLES20.glVertexAttribPointer(this.b, 2, 5126, false, 0, (Buffer)floatBuffer);
        GLES20.glVertexAttribPointer(this.c, 2, 5126, false, 0, (Buffer)floatBuffer2);
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glBindTexture(3553, 0);
        if (this.o != null) {
            this.o.drawInGLThread(n, this.l, this.k);
        }
    }
    
    private void a() {
        if (this.mInputFramebufferForDisplay == null) {
            return;
        }
        this.mInputFramebufferForDisplay.unlock();
        this.mInputFramebufferForDisplay = null;
    }
    
    private int b() {
        int texture = 0;
        if (this.mInputFramebufferForDisplay != null) {
            texture = this.mInputFramebufferForDisplay.getTexture();
        }
        return texture;
    }
    
    @Override
    public void mountAtGLThread(final Runnable runnable) {
        if (runnable == null) {
            return;
        }
        this.runOnDraw(runnable);
    }
    
    @Override
    public void newFrameReady(final long n, final int n2) {
        if (this.n != null && this.n.calculate(this.mInputImageSize, this.k, this.e, this.f)) {
            this.l = this.n.outputSize();
        }
        else {
            this.f.clear();
            this.f.put(textureCoordinates(this.k)).position(0);
            this.l = this.mInputImageSize;
        }
        this.runPendingOnDrawTasks();
        this.a(n, this.e, this.f);
    }
    
    @Override
    public void setInputFramebuffer(final SelesFramebuffer mInputFramebufferForDisplay, final int n) {
        if (mInputFramebufferForDisplay == null) {
            return;
        }
        this.a();
        (this.mInputFramebufferForDisplay = mInputFramebufferForDisplay).lock();
    }
    
    @Override
    public int nextAvailableTextureIndex() {
        return 0;
    }
    
    @Override
    public void setInputSize(final TuSdkSize tuSdkSize, final int n) {
        if (tuSdkSize == null || !tuSdkSize.isSize()) {
            return;
        }
        final TuSdkSize copy = tuSdkSize.copy();
        if (this.k.isTransposed()) {
            copy.width = tuSdkSize.height;
            copy.height = tuSdkSize.width;
        }
        if (this.mInputImageSize.equals(copy)) {
            return;
        }
        this.mInputImageSize = copy;
    }
    
    @Override
    public void setInputRotation(final ImageOrientation k, final int n) {
        if (k == null) {
            return;
        }
        this.k = k;
    }
    
    @Override
    public void setPusherRotation(final ImageOrientation imageOrientation, final int n) {
        this.setInputRotation(imageOrientation, n);
    }
    
    @Override
    public TuSdkSize maximumOutputSize() {
        return (this.l == null) ? this.mInputImageSize : this.l;
    }
    
    @Override
    public void endProcessing() {
    }
    
    @Override
    public boolean isShouldIgnoreUpdatesToThisTarget() {
        return false;
    }
    
    @Override
    public boolean isEnabled() {
        return this.m;
    }
    
    @Override
    public boolean wantsMonochromeInput() {
        return false;
    }
    
    @Override
    public void setCurrentlyReceivingMonochromeInput(final boolean b) {
    }
    
    @Override
    public void newFrameReadyInGLThread(final long n) {
        this.runPendingOnDrawTasks();
    }
    
    @Override
    public void duplicateFrameReadyInGLThread(final long n) {
        if (this.mInputFramebufferForDisplay == null) {
            return;
        }
        this.a(n, this.e, this.f);
    }
    
    protected void runPendingOnDrawTasks() {
        while (!this.a.isEmpty()) {
            try {
                this.a.take().run();
            }
            catch (InterruptedException ex) {
                TLog.e(ex, "%s: %s", "SelesSurfacePusher", this.getClass());
            }
        }
    }
    
    protected boolean isOnDrawTasksEmpty() {
        return this.a.isEmpty();
    }
    
    protected void runOnDraw(final Runnable runnable) {
        if (runnable == null) {
            return;
        }
        synchronized (this.a) {
            this.a.add(runnable);
        }
    }
    
    static {
        noRotationTextureCoordinates = new float[] { 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f };
        rotateRightTextureCoordinates = new float[] { 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f };
        rotateLeftTextureCoordinates = new float[] { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f };
        verticalFlipTextureCoordinates = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f };
        horizontalFlipTextureCoordinates = new float[] { 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f };
        rotateRightVerticalFlipTextureCoordinates = new float[] { 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f };
        rotateRightHorizontalFlipTextureCoordinates = new float[] { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f };
        rotate180TextureCoordinates = new float[] { 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        imageVertices = new float[] { -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f };
    }
}
