// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.egl.SelesEGL10Core;
//import org.lasque.tusdk.core.utils.RectHelper;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.TLog;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.utils.TuSdkSemaphore;
import android.graphics.Rect;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.opengl.GLSurfaceView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl.SelesEGL10Core;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkSemaphore;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

public final class SelesPicture extends SelesOutput implements GLSurfaceView.Renderer
{
    private boolean a;
    private ImageOrientation b;
    private Rect c;
    protected TuSdkSemaphore mImageUpdateSemaphore;
    private Bitmap d;
    
    public SelesPicture(final Bitmap bitmap) {
        this(bitmap, false);
    }
    
    public SelesPicture(final Bitmap bitmap, final boolean b) {
        this(bitmap, b, false);
    }
    
    public SelesPicture(final Bitmap bitmap, final boolean shouldSmoothlyScaleOutput, final boolean b) {
        this.b = ImageOrientation.Up;
        if (bitmap == null) {
            TLog.e("SelesPicture:image is null", new Object[0]);
            return;
        }
        this.a = false;
        this.setShouldSmoothlyScaleOutput(shouldSmoothlyScaleOutput);
        (this.mImageUpdateSemaphore = new TuSdkSemaphore(0)).signal();
        this.mInputTextureSize = TuSdkSize.create(bitmap);
        if (this.mInputTextureSize.minSide() <= 0) {
            TLog.e("%s Passed image must not be empty - it should be at least 1px tall and wide", "SelesPicture");
            return;
        }
        this.mInputTextureSize = SelesContext.sizeThatFitsWithinATexture(this.mInputTextureSize.copy());
        this.c = new Rect(0, 0, this.mInputTextureSize.width, this.mInputTextureSize.height);
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                (SelesPicture.this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.TEXTURE_ACTIVE, SelesPicture.this.mInputTextureSize)).bindTexture(bitmap, SelesPicture.this.isShouldSmoothlyScaleOutput(), b);
            }
        });
    }
    
    public SelesPicture(final ByteBuffer byteBuffer, final int n, final int n2) {
        this.b = ImageOrientation.Up;
        if (byteBuffer == null) {
            TLog.e("SelesPicture:singleChannalData is null", new Object[0]);
            return;
        }
        this.a = false;
        this.mInputTextureSize = TuSdkSize.create(n, n2);
        this.c = new Rect(0, 0, n, n2);
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                (SelesPicture.this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.TEXTURE_ACTIVE, SelesPicture.this.mInputTextureSize)).bindTextureLuminance(byteBuffer, n, n2, SelesPicture.this.isShouldSmoothlyScaleOutput());
            }
        });
    }
    
    public void setInputRotation(final ImageOrientation b) {
        if (b == null) {
            return;
        }
        this.b = b;
    }
    
    public void setScaleSize(final TuSdkSize mInputTextureSize) {
        if (mInputTextureSize == null || mInputTextureSize.width > this.mInputTextureSize.width || mInputTextureSize.height > this.mInputTextureSize.height) {
            return;
        }
        this.mInputTextureSize = mInputTextureSize;
    }
    
    public TuSdkSize getScaleSize() {
        return this.mInputTextureSize;
    }
    
    public void setOutputRect(final Rect c) {
        if (c == null || c.width() <= 0 || c.height() <= 0 || c.right > this.mInputTextureSize.width || c.bottom > this.mInputTextureSize.height) {
            return;
        }
        this.c = c;
    }
    
    public Rect getOutputRect() {
        return RectHelper.rotationWithRotation(this.c, this.mInputTextureSize, this.b);
    }
    
    public TuSdkSize outputImageSize() {
        return TuSdkSize.create(this.getOutputRect());
    }
    
    @Override
    protected void onDestroy() {
        if (this.mOutputFramebuffer != null) {
            SelesContext.returnFramebufferToCache(this.mOutputFramebuffer);
            this.mOutputFramebuffer = null;
        }
        if (this.mImageUpdateSemaphore != null) {
            this.mImageUpdateSemaphore.release();
            this.mImageUpdateSemaphore = null;
        }
    }
    
    @Override
    public void removeAllTargets() {
        super.removeAllTargets();
        this.a = false;
    }
    
    @Override
    public void addTarget(final SelesContext.SelesInput selesInput, final int n) {
        super.addTarget(selesInput, n);
        if (selesInput == null) {
            return;
        }
        if (n > 0) {
            selesInput.mountAtGLThread(new Runnable() {
                @Override
                public void run() {
                    SelesPicture.this.processImage();
                    SelesPicture.this.runPendingOnDrawTasks();
                }
            });
        }
        if (this.a) {
            selesInput.setInputSize(this.getScaleSize(), n);
            selesInput.newFrameReady(0L, n);
        }
    }
    
    public boolean processImage() {
        return this.a((Runnable)null);
    }
    
    private boolean a(final Runnable runnable) {
        if (this.mImageUpdateSemaphore == null) {
            return false;
        }
        this.a = true;
        if (!this.mImageUpdateSemaphore.waitSignal(0L)) {
            return false;
        }
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < SelesPicture.this.mTargets.size(); ++i) {
                    final SelesContext.SelesInput selesInput = SelesPicture.this.mTargets.get(i);
                    final int intValue = SelesPicture.this.mTargetTextureIndices.get(i);
                    selesInput.setCurrentlyReceivingMonochromeInput(false);
                    selesInput.setInputRotation(SelesPicture.this.b, intValue);
                    selesInput.setInputSize(SelesPicture.this.getScaleSize(), intValue);
                    selesInput.setInputFramebuffer(SelesPicture.this.framebufferForOutput(), intValue);
                    selesInput.newFrameReady(System.nanoTime(), intValue);
                }
                if (SelesPicture.this.mImageUpdateSemaphore != null) {
                    SelesPicture.this.mImageUpdateSemaphore.signal();
                }
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
        return true;
    }
    
    public boolean processImageUpToFilter() {
        this.processImage();
        ThreadHelper.runThread(new Runnable() {
            @Override
            public void run() {
                final SelesEGL10Core create = SelesEGL10Core.create(SelesPicture.this.mInputTextureSize.transforOrientation(SelesPicture.this.b));
                GLES20.glDisable(2929);
                SelesPicture.this.runPendingOnDrawTasks();
                create.destroy();
            }
        });
        return true;
    }
    
    @Override
    public Bitmap imageFromCurrentlyProcessedOutput() {
        if (this.mImageUpdateSemaphore == null) {
            return null;
        }
        this.d = null;
        ThreadHelper.runThread(new Runnable() {
            @Override
            public void run() {
                final SelesEGL10Core create = SelesEGL10Core.create(SelesPicture.this.mInputTextureSize.transforOrientation(SelesPicture.this.b));
                create.setOutputRect(SelesPicture.this.getOutputRect());
                GLES20.glDisable(2929);
                SelesPicture.this.runPendingOnDrawTasks();
                SelesPicture.this.d = create.getBitmap();
                if (SelesPicture.this.mImageUpdateSemaphore != null) {
                    SelesPicture.this.mImageUpdateSemaphore.signal();
                }
                create.destroy();
            }
        });
        if (!this.mImageUpdateSemaphore.waitSignal(2, 10000L)) {
            TLog.w("%s imageFromCurrentlyProcessedOutput timeout", "SelesPicture");
            return this.d;
        }
        if (this.mImageUpdateSemaphore != null) {
            this.mImageUpdateSemaphore.signal();
        }
        return this.d;
    }
    
    public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
        GLES20.glDisable(2929);
    }
    
    public void onSurfaceChanged(final GL10 gl10, final int n, final int n2) {
    }
    
    public void onDrawFrame(final GL10 gl10) {
        this.runPendingOnDrawTasks();
    }
    
    public void mountAtGLThread(final Runnable runnable) {
        if (runnable == null) {
            return;
        }
        this.runOnDraw(runnable);
    }
}
