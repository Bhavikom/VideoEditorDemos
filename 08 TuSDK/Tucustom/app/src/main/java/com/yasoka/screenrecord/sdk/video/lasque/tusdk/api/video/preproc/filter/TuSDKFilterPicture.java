// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.video.preproc.filter;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.seles.egl.SelesEGL10Core;
import java.util.concurrent.Semaphore;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import android.opengl.GLUtils;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.TLog;
import android.graphics.Bitmap;
import java.nio.IntBuffer;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.opengl.GLSurfaceView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl.SelesEGL10Core;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.sources.SelesOutput;

public final class TuSDKFilterPicture extends SelesOutput implements GLSurfaceView.Renderer
{
    private boolean a;
    private ImageOrientation b;
    private IntBuffer c;
    
    public TuSDKFilterPicture(final Bitmap bitmap) {
        this(bitmap, false);
    }
    
    public TuSDKFilterPicture(final Bitmap bitmap, final boolean b) {
        this(bitmap, b, false);
    }
    
    public TuSDKFilterPicture(final Bitmap bitmap, final boolean shouldSmoothlyScaleOutput, final boolean b) {
        if (bitmap == null) {
            TLog.e("SelesPicture:image is null", new Object[0]);
            return;
        }
        this.b = ImageOrientation.Up;
        this.a = false;
        this.setShouldSmoothlyScaleOutput(shouldSmoothlyScaleOutput);
        this.mInputTextureSize = TuSdkSize.create(bitmap);
        if (this.mInputTextureSize.minSide() <= 0) {
            TLog.e("Passed image must not be empty - it should be at least 1px tall and wide", new Object[0]);
            return;
        }
        this.runOnDraw((Runnable)new Runnable() {
            @Override
            public void run() {
                TuSDKFilterPicture.this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.TEXTURE_ACTIVE, TuSDKFilterPicture.this.mInputTextureSize);
                TuSDKFilterPicture.this.mOutputFramebuffer.disableReferenceCounting();
                GLES20.glBindTexture(3553, TuSDKFilterPicture.this.mOutputFramebuffer.getTexture());
                if (TuSDKFilterPicture.this.isShouldSmoothlyScaleOutput()) {
                    GLES20.glTexParameteri(3553, 10241, 9987);
                }
                GLUtils.texImage2D(3553, 0, bitmap, 0);
                if (TuSDKFilterPicture.this.isShouldSmoothlyScaleOutput()) {
                    GLES20.glGenerateMipmap(3553);
                }
                GLES20.glBindTexture(3553, 0);
                if (b) {
                    bitmap.recycle();
                }
            }
        });
    }
    
    public TuSDKFilterPicture(final ByteBuffer byteBuffer, final int n, final int n2) {
        if (byteBuffer == null) {
            TLog.e("SelesPicture:singleChannalData is null", new Object[0]);
            return;
        }
        this.a = false;
        this.mInputTextureSize = TuSdkSize.create(n, n2);
        this.runOnDraw((Runnable)new Runnable() {
            @Override
            public void run() {
                TuSDKFilterPicture.this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.TEXTURE_ACTIVE, TuSDKFilterPicture.this.mInputTextureSize);
                TuSDKFilterPicture.this.mOutputFramebuffer.disableReferenceCounting();
                GLES20.glBindTexture(3553, TuSDKFilterPicture.this.mOutputFramebuffer.getTexture());
                if (TuSDKFilterPicture.this.isShouldSmoothlyScaleOutput()) {
                    GLES20.glTexParameteri(3553, 10241, 9987);
                }
                GLES20.glTexImage2D(3553, 0, 6409, n, n2, 0, 6409, 5121, (Buffer)byteBuffer);
                if (TuSDKFilterPicture.this.isShouldSmoothlyScaleOutput()) {
                    GLES20.glGenerateMipmap(3553);
                }
                GLES20.glBindTexture(3553, 0);
            }
        });
    }
    
    protected void onDestroy() {
        if (this.mOutputFramebuffer != null) {
            this.mOutputFramebuffer.enableReferenceCounting();
            this.mOutputFramebuffer.unlock();
        }
        this.mOutputFramebuffer = null;
    }
    
    public void removeAllTargets() {
        super.removeAllTargets();
        this.a = false;
    }
    
    public void setOutputRotation(final ImageOrientation b) {
        this.b = b;
    }
    
    public void processImage() {
        this.a((Runnable)null);
    }
    
    private void a(final Runnable runnable) {
        this.a = true;
        this.runOnDraw((Runnable)new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < TuSDKFilterPicture.this.mTargets.size(); ++i) {
                    final SelesContext.SelesInput selesInput = TuSDKFilterPicture.this.mTargets.get(i);
                    final int intValue = TuSDKFilterPicture.this.mTargetTextureIndices.get(i);
                    selesInput.setCurrentlyReceivingMonochromeInput(false);
                    selesInput.setInputRotation(TuSDKFilterPicture.this.b, intValue);
                    selesInput.setInputSize(TuSDKFilterPicture.this.mInputTextureSize, intValue);
                    selesInput.setInputFramebuffer(TuSDKFilterPicture.this.framebufferForOutput(), intValue);
                }
                for (int j = 0; j < TuSDKFilterPicture.this.mTargets.size(); ++j) {
                    ((SelesContext.SelesInput)TuSDKFilterPicture.this.mTargets.get(j)).newFrameReady(0L, (int)TuSDKFilterPicture.this.mTargetTextureIndices.get(j));
                }
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
    }
    
    public IntBuffer bufferFromCurrentlyProcessedOutput() {
        this.c = null;
        final Semaphore semaphore = new Semaphore(0);
        ThreadHelper.runThread((Runnable)new Runnable() {
            @Override
            public void run() {
                final SelesEGL10Core create = SelesEGL10Core.create(TuSDKFilterPicture.this.outputImageSize());
                create.setRenderer((GLSurfaceView.Renderer)TuSDKFilterPicture.this);
                TuSDKFilterPicture.this.c = create.getImageBuffer();
                create.destroy();
                semaphore.release();
            }
        });
        try {
            semaphore.acquire();
        }
        catch (InterruptedException ex) {
            TLog.e((Throwable)ex, "imageFromCurrentlyProcessedOutput", new Object[0]);
        }
        return this.c;
    }
    
    public TuSdkSize outputImageSize() {
        final TuSdkSize tuSdkSize = new TuSdkSize(this.mInputTextureSize.width, this.mInputTextureSize.height);
        if (this.b != null && this.b.isTransposed()) {
            tuSdkSize.width = this.mInputTextureSize.height;
            tuSdkSize.height = this.mInputTextureSize.width;
        }
        return tuSdkSize;
    }
    
    public void addTarget(final SelesContext.SelesInput selesInput, final int n) {
        super.addTarget(selesInput, n);
        if (selesInput == null) {
            return;
        }
        if (n > 0) {
            selesInput.mountAtGLThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    TuSDKFilterPicture.this.processImage();
                    TuSDKFilterPicture.o(TuSDKFilterPicture.this);
                }
            });
        }
        if (this.a) {
            selesInput.setInputSize(this.mInputTextureSize, n);
            selesInput.newFrameReady(0L, n);
        }
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
    
    static /* synthetic */ void o(final TuSDKFilterPicture tuSDKFilterPicture) {
        tuSDKFilterPicture.runPendingOnDrawTasks();
    }
}
