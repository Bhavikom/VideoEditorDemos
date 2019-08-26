// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend;

//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.util.ArrayList;
//import org.lasque.tusdk.core.seles.SelesContext;
import java.util.List;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;

public class TuSdkFilterBridge extends SelesOutInput
{
    private TuSdkSurfaceDraw a;
    private SelesFramebuffer b;
    private final List<SelesContext.SelesInput> c;
    
    public TuSdkFilterBridge() {
        this.c = new ArrayList<SelesContext.SelesInput>();
    }
    
    public void setSurfaceDraw(final TuSdkSurfaceDraw a) {
        this.a = a;
    }
    
    @Override
    public void setInputSize(final TuSdkSize mInputTextureSize, final int n) {
        if (mInputTextureSize == null) {
            return;
        }
        this.mInputTextureSize = mInputTextureSize;
    }
    
    @Override
    public void setInputFramebuffer(final SelesFramebuffer b, final int n) {
        if (b == null) {
            return;
        }
        (this.b = b).lock();
    }
    
    @Override
    protected void onDestroy() {
        if (this.b != null) {
            this.b.unlock();
            this.b = null;
        }
        if (this.mOutputFramebuffer != null) {
            this.mOutputFramebuffer.unlock();
            this.mOutputFramebuffer = null;
        }
    }
    
    @Override
    public void newFrameReady(final long n, final int n2) {
        this.runPendingOnDrawTasks();
        final SelesFramebuffer b = this.b;
        final TuSdkSize mInputTextureSize = this.mInputTextureSize;
        if (b == null || !mInputTextureSize.isSize()) {
            return;
        }
        int n3 = b.getTexture();
        if (this.a != null) {
            n3 = this.a.onDrawFrame(n3, mInputTextureSize.width, mInputTextureSize.height, n);
        }
        if (n3 == b.getTexture()) {
            this.mOutputFramebuffer = b;
        }
        else {
            b.unlock();
            this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.HOLDER, mInputTextureSize, n3);
        }
        this.b = null;
        this.a(n, b, mInputTextureSize);
    }
    
    private void a(final long n, final SelesFramebuffer selesFramebuffer, final TuSdkSize tuSdkSize) {
        this.c.clear();
        for (final SelesContext.SelesInput selesInput : this.mTargets) {
            if (!selesInput.isEnabled()) {
                continue;
            }
            if (selesInput == this.getTargetToIgnoreForUpdates()) {
                continue;
            }
            this.c.add(selesInput);
            final int intValue = this.mTargetTextureIndices.get(this.mTargets.indexOf(selesInput));
            this.setInputFramebufferForTarget(selesInput, intValue);
            selesInput.setInputSize(tuSdkSize, intValue);
        }
        if (this.mOutputFramebuffer != null) {
            this.mOutputFramebuffer.unlock();
            this.mOutputFramebuffer = null;
        }
        for (final SelesContext.SelesInput selesInput2 : this.c) {
            selesInput2.newFrameReady(n, this.mTargetTextureIndices.get(this.mTargets.indexOf(selesInput2)));
        }
        if (this.a != null) {
            this.a.onDrawFrameCompleted();
        }
    }
    
    @Override
    public int nextAvailableTextureIndex() {
        return 0;
    }
    
    @Override
    public void setInputRotation(final ImageOrientation imageOrientation, final int n) {
    }
    
    @Override
    public TuSdkSize maximumOutputSize() {
        return this.mInputTextureSize;
    }
    
    @Override
    public void endProcessing() {
    }
    
    @Override
    public boolean wantsMonochromeInput() {
        return false;
    }
    
    @Override
    public void setCurrentlyReceivingMonochromeInput(final boolean b) {
    }
}
