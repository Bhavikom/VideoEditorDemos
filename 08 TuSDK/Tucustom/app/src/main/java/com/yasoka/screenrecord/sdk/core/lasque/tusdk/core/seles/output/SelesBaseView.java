// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output;

import javax.microedition.khronos.opengles.GL10;
import java.nio.Buffer;
import java.nio.IntBuffer;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.concurrent.Semaphore;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
//import org.lasque.tusdk.core.seles.SelesEGLContextFactory;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.view.TuSdkViewInterface;
//import org.lasque.tusdk.core.seles.SelesContext;
import android.widget.FrameLayout;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesEGLContextFactory;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewInterface;

public abstract class SelesBaseView extends FrameLayout implements SelesContext.SelesInput, SelesViewInterface, TuSdkViewInterface
{
    private SelesSurfaceView a;
    private boolean b;
    protected TuSdkSize mSizeInPixels;
    private SelesSurfacePusher c;
    private SelesVerticeCoordinateBuilder d;
    
    public boolean isCreatedSurface() {
        return this.a.isCreated();
    }
    
    public SelesBaseView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.b = true;
        this.initView(context, set);
    }
    
    public SelesBaseView(final Context context, final AttributeSet set) {
        super(context, set);
        this.b = true;
        this.initView(context, set);
    }
    
    public SelesBaseView(final Context context) {
        super(context);
        this.b = true;
        this.initView(context, null);
    }
    
    protected void initView(final Context context, final AttributeSet set) {
        this.addView((View)(this.a = new SelesSurfaceView(context, set)));
        this.a.setEGLContextClientVersion(2);
        this.a.setEGLContextFactory((GLSurfaceView.EGLContextFactory)new SelesEGLContextFactory(2));
        this.mSizeInPixels = new TuSdkSize();
        this.c = this.buildWindowDisplay();
        this.d = this.buildVerticeCoordinateBuilder();
        if (this.c != null) {
            this.c.setTextureCoordinateBuilder(this.d);
        }
    }
    
    protected abstract SelesSurfacePusher buildWindowDisplay();
    
    protected abstract SelesVerticeCoordinateBuilder buildVerticeCoordinateBuilder();
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.a(TuSdkSize.create(n3 - n, n4 - n2));
    }
    
    private void a(final TuSdkSize mSizeInPixels) {
        if (mSizeInPixels.equals(this.mSizeInPixels) || !mSizeInPixels.isSize()) {
            return;
        }
        this.mSizeInPixels = mSizeInPixels;
        if (this.d != null) {
            this.d.setOutputSize(mSizeInPixels.copy());
        }
    }
    
    public void loadView() {
    }
    
    public void viewDidLoad() {
    }
    
    public void viewNeedRest() {
    }
    
    public void viewWillDestory() {
        this.setRenderModeDirty();
    }
    
    public void setZOrderOnTop(final Boolean b) {
        if (this.a != null) {
            this.a.setZOrderOnTop((boolean)b);
        }
    }
    
    public void setZOrderMediaOverlay(final Boolean b) {
        if (this.a != null) {
            this.a.setZOrderMediaOverlay((boolean)b);
        }
    }
    
    public void setRenderer(final GLSurfaceView.Renderer renderer) {
        if (renderer == null) {
            return;
        }
        this.a.setRenderer(renderer);
        this.setRenderModeDirty();
        this.requestRender();
    }
    
    public void setRenderMode(final int renderMode) {
        this.a.setRenderMode(renderMode);
    }
    
    public int getRenderMode() {
        return this.a.getRenderMode();
    }
    
    public void setRenderModeDirty() {
        this.setRenderMode(0);
    }
    
    public void setRenderModeContinuously() {
        this.setRenderMode(1);
    }
    
    public void setEnableRenderer(final boolean b) {
        this.b = b;
    }
    
    public boolean isEnableRenderer() {
        return this.b;
    }
    
    public void requestRender() {
        this.a.requestRender();
    }
    
    public void onPause() {
        this.a.onPause();
    }
    
    public void onResume() {
        this.a.onResume();
    }
    
    public int getRendererFPS() {
        return this.a.getRendererFPS();
    }
    
    public void setRendererFPS(final int rendererFPS) {
        this.a.setRendererFPS(rendererFPS);
    }
    
    public void setEnableFixedFrameRate(final boolean enableFixedFrameRate) {
        this.a.setEnableFixedFrameRate(enableFixedFrameRate);
    }
    
    public void setBackgroundColor(final float n, final float n2, final float n3, final float n4) {
        if (this.c == null) {
            return;
        }
        this.c.setBackgroundColor(n, n2, n3, n4);
    }
    
    public void setBackgroundColor(final int backgroundColor) {
        super.setBackgroundColor(backgroundColor);
        this.setBackgroundColor(Color.red(backgroundColor) / 255.0f, Color.green(backgroundColor) / 255.0f, Color.blue(backgroundColor) / 255.0f, Color.alpha(backgroundColor) / 255.0f);
    }
    
    public void mountAtGLThread(final Runnable runnable) {
        if (this.c == null) {
            return;
        }
        this.c.runOnDraw(runnable);
    }
    
    public void newFrameReady(final long n, final int n2) {
        if (!this.isEnableRenderer()) {
            return;
        }
        if (this.c == null) {
            return;
        }
        this.c.newFrameReady(n, n2);
    }
    
    public int nextAvailableTextureIndex() {
        if (this.c == null) {
            return 0;
        }
        return this.c.nextAvailableTextureIndex();
    }
    
    public void setInputFramebuffer(final SelesFramebuffer selesFramebuffer, final int n) {
        if (selesFramebuffer == null) {
            return;
        }
        if (this.c == null) {
            return;
        }
        this.c.setInputFramebuffer(selesFramebuffer, n);
    }
    
    public void setInputRotation(final ImageOrientation imageOrientation, final int n) {
        if (this.c == null) {
            return;
        }
        this.c.setInputRotation(imageOrientation, n);
    }
    
    public void setInputSize(final TuSdkSize tuSdkSize, final int n) {
        if (this.c == null) {
            return;
        }
        this.c.setInputSize(tuSdkSize, n);
    }
    
    public TuSdkSize maximumOutputSize() {
        if (this.c != null) {
            return this.c.maximumOutputSize();
        }
        final TuSdkSize create = TuSdkSize.create(this.getWidth(), this.getHeight());
        if (create.isSize()) {
            return create;
        }
        return this.mSizeInPixels;
    }
    
    public Bitmap imageFromCurrentlyProcessedOutput() {
        if (this.c == null) {
            return null;
        }
        final Bitmap bitmap = Bitmap.createBitmap(this.c.getInputImageSize().width, this.c.getInputImageSize().height, Bitmap.Config.ARGB_8888);
        final Semaphore semaphore = new Semaphore(0);
        this.c.mountAtGLThread(new Runnable() {
            @Override
            public void run() {
                TLog.d("image capture", new Object[0]);
                final GL10 currentGL = SelesContext.currentGL();
                if (currentGL != null) {
                    final IntBuffer allocate = IntBuffer.allocate(bitmap.getWidth() * bitmap.getHeight());
                    currentGL.glReadPixels(0, 0, bitmap.getWidth(), bitmap.getHeight(), 6408, 5121, (Buffer)allocate);
                    bitmap.copyPixelsFromBuffer((Buffer)allocate);
                }
                semaphore.release();
            }
        });
        this.requestRender();
        try {
            semaphore.acquire();
        }
        catch (InterruptedException ex) {
            TLog.e(ex, "imageFromCurrentlyProcessedOutput", new Object[0]);
        }
        return bitmap;
    }
    
    public void endProcessing() {
    }
    
    public boolean isShouldIgnoreUpdatesToThisTarget() {
        return false;
    }
    
    public boolean wantsMonochromeInput() {
        return false;
    }
    
    public void setCurrentlyReceivingMonochromeInput(final boolean b) {
    }
}
