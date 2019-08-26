// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources;

import android.opengl.GLES20;
import java.nio.Buffer;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.nio.IntBuffer;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.SelesContext;
import java.util.List;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
import java.util.concurrent.BlockingQueue;

public abstract class SelesOutput
{
    private final BlockingQueue<Runnable> a;
    protected SelesFramebuffer mOutputFramebuffer;
    protected final List<SelesContext.SelesInput> mTargets;
    protected final List<Integer> mTargetTextureIndices;
    protected TuSdkSize mInputTextureSize;
    protected TuSdkSize mCachedMaximumOutputSize;
    protected TuSdkSize mForcedMaximumSize;
    protected boolean mOverrideInputSize;
    protected boolean mAllTargetsWantMonochromeData;
    protected boolean mUsingNextFrameForImageCapture;
    private boolean b;
    private boolean c;
    private SelesContext.SelesInput d;
    private boolean e;
    private SelesFramebuffer.SelesTextureOptions f;
    
    public boolean isShouldSmoothlyScaleOutput() {
        return this.b;
    }
    
    public void setShouldSmoothlyScaleOutput(final boolean b) {
        this.b = b;
    }
    
    public boolean isShouldIgnoreUpdatesToThisTarget() {
        return this.c;
    }
    
    public void setShouldIgnoreUpdatesToThisTarget(final boolean c) {
        this.c = c;
    }
    
    public SelesContext.SelesInput getTargetToIgnoreForUpdates() {
        return this.d;
    }
    
    public void setTargetToIgnoreForUpdates(final SelesContext.SelesInput d) {
        this.d = d;
    }
    
    public boolean isEnabled() {
        return this.e;
    }
    
    public void setEnabled(final boolean e) {
        this.e = e;
    }
    
    public SelesFramebuffer.SelesTextureOptions getOutputTextureOptions() {
        return this.f;
    }
    
    public void setOutputTextureOptions(final SelesFramebuffer.SelesTextureOptions f) {
        this.f = f;
    }
    
    public SelesOutput() {
        this.mInputTextureSize = new TuSdkSize();
        this.mCachedMaximumOutputSize = new TuSdkSize();
        this.mForcedMaximumSize = new TuSdkSize();
        this.a = new LinkedBlockingQueue<Runnable>();
        this.mTargets = new ArrayList<SelesContext.SelesInput>();
        this.mTargetTextureIndices = new ArrayList<Integer>();
        this.e = true;
        this.mAllTargetsWantMonochromeData = true;
        this.mUsingNextFrameForImageCapture = false;
        this.f = new SelesFramebuffer.SelesTextureOptions();
    }
    
    @Override
    protected void finalize() throws Throwable {
        this.destroy();
        this.removeAllTargets();
        super.finalize();
    }
    
    public final void destroy() {
        this.onDestroy();
    }
    
    protected abstract void onDestroy();
    
    public void setInputFramebufferForTarget(final SelesContext.SelesInput selesInput, final int n) {
        if (selesInput != null) {
            selesInput.setInputFramebuffer(this.framebufferForOutput(), n);
        }
        else {
            TLog.e("%s setInputFramebufferForTarget target is null", this.getClass());
        }
    }
    
    public SelesFramebuffer framebufferForOutput() {
        return this.mOutputFramebuffer;
    }
    
    public void removeOutputFramebuffer() {
        this.mOutputFramebuffer = null;
    }
    
    public void notifyTargetsAboutNewOutputTexture() {
        final List<SelesContext.SelesInput> targets = this.targets();
        for (int i = 0; i < targets.size(); ++i) {
            final SelesContext.SelesInput selesInput = targets.get(i);
            if (selesInput.isEnabled()) {
                this.setInputFramebufferForTarget(selesInput, this.mTargetTextureIndices.get(i));
            }
        }
    }
    
    public List<SelesContext.SelesInput> targets() {
        return new ArrayList<SelesContext.SelesInput>(this.mTargets);
    }
    
    public void addTarget(final SelesContext.SelesInput d) {
        if (d == null) {
            TLog.e("%s addTarget newTarget is null", this.getClass());
            return;
        }
        this.addTarget(d, d.nextAvailableTextureIndex());
        if (d.isShouldIgnoreUpdatesToThisTarget()) {
            this.d = d;
        }
    }
    
    public void addTarget(final SelesContext.SelesInput selesInput, final int n) {
        if (selesInput == null) {
            TLog.e("%s addTarget:newTarget:textureLocation is null", this.getClass());
            return;
        }
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                if (SelesOutput.this.mTargets.contains(selesInput)) {
                    return;
                }
                SelesOutput.this.mCachedMaximumOutputSize = new TuSdkSize();
                SelesOutput.this.setInputFramebufferForTarget(selesInput, n);
                SelesOutput.this.mTargets.add(selesInput);
                SelesOutput.this.mTargetTextureIndices.add(n);
                SelesOutput.this.mAllTargetsWantMonochromeData = (SelesOutput.this.mAllTargetsWantMonochromeData && selesInput.wantsMonochromeInput());
            }
        });
    }
    
    public void removeTarget(final SelesContext.SelesInput selesInput) {
        if (selesInput == null) {
            return;
        }
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                if (!SelesOutput.this.mTargets.contains(selesInput)) {
                    return;
                }
                if (selesInput.equals(SelesOutput.this.d)) {
                    SelesOutput.this.d = null;
                }
                SelesOutput.this.mCachedMaximumOutputSize = new TuSdkSize();
                final int index = SelesOutput.this.mTargets.indexOf(selesInput);
                final int intValue = SelesOutput.this.mTargetTextureIndices.get(index);
                selesInput.setInputSize(new TuSdkSize(), intValue);
                selesInput.setInputRotation(ImageOrientation.Up, intValue);
                SelesOutput.this.mTargetTextureIndices.remove(index);
                SelesOutput.this.mTargets.remove(selesInput);
                selesInput.endProcessing();
            }
        });
    }
    
    public void removeAllTargets() {
        this.mCachedMaximumOutputSize = new TuSdkSize();
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < SelesOutput.this.mTargets.size(); ++i) {
                    final SelesContext.SelesInput selesInput = SelesOutput.this.mTargets.get(i);
                    final int intValue = SelesOutput.this.mTargetTextureIndices.get(i);
                    selesInput.setInputSize(new TuSdkSize(), intValue);
                    selesInput.setInputRotation(ImageOrientation.Up, intValue);
                }
                SelesOutput.this.mTargets.clear();
                SelesOutput.this.mTargetTextureIndices.clear();
                SelesOutput.this.mAllTargetsWantMonochromeData = true;
            }
        });
    }
    
    public void forceProcessingAtSize(final TuSdkSize tuSdkSize) {
    }
    
    public void forceProcessingAtSizeRespectingAspectRatio(final TuSdkSize tuSdkSize) {
    }
    
    public void useNextFrameForImageCapture() {
    }
    
    public IntBuffer imageBufferFromCurrentlyProcessedOutput(final TuSdkSize tuSdkSize) {
        return null;
    }
    
    public Bitmap imageFromCurrentlyProcessedOutput() {
        final TuSdkSize create = TuSdkSize.create(0);
        final IntBuffer imageBufferFromCurrentlyProcessedOutput = this.imageBufferFromCurrentlyProcessedOutput(create);
        if (imageBufferFromCurrentlyProcessedOutput == null) {
            return null;
        }
        final Bitmap bitmap = Bitmap.createBitmap(create.width, create.height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer((Buffer)imageBufferFromCurrentlyProcessedOutput);
        return bitmap;
    }
    
    public Bitmap imageByFilteringImage(final Bitmap bitmap) {
        if (!(this instanceof SelesContext.SelesInput)) {
            return null;
        }
        final SelesPicture selesPicture = new SelesPicture(bitmap);
        this.useNextFrameForImageCapture();
        selesPicture.addTarget((SelesContext.SelesInput)this, 0);
        selesPicture.processImageUpToFilter();
        final Bitmap imageFromCurrentlyProcessedOutput = this.imageFromCurrentlyProcessedOutput();
        selesPicture.removeTarget((SelesContext.SelesInput)this);
        return imageFromCurrentlyProcessedOutput;
    }
    
    public boolean providesMonochromeOutput() {
        return false;
    }
    
    public void setOutputOption(final SelesFramebuffer.SelesTextureOptions f) {
        if (f == null) {
            return;
        }
        this.f = f;
        if (this.mOutputFramebuffer != null && this.mOutputFramebuffer.getTexture() > 0) {
            GLES20.glBindTexture(3553, this.mOutputFramebuffer.getTexture());
            GLES20.glTexParameteri(3553, 10242, this.f.wrapS);
            GLES20.glTexParameteri(3553, 10243, this.f.wrapT);
            GLES20.glBindTexture(3553, 0);
        }
    }
    
    protected void runPendingOnDrawTasks() {
        while (!this.a.isEmpty()) {
            try {
                this.a.take().run();
            }
            catch (InterruptedException ex) {
                TLog.e(ex, "SelesOutput: %s", this.getClass());
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
}
