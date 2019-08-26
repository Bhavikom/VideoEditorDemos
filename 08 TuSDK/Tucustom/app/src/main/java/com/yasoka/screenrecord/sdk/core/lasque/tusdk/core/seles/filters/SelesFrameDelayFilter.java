// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.nio.IntBuffer;
//import org.lasque.tusdk.core.utils.TLog;
import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Map;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
import java.util.List;

public class SelesFrameDelayFilter extends SelesFilter
{
    private final List<SelesFramebuffer> a;
    private int b;
    private SelesContext.SelesInput c;
    private int d;
    private SelesContext.SelesInput e;
    private int f;
    private final Map<SelesContext.SelesInput, Integer> g;
    
    public int getDelaySize() {
        return this.b;
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    public void setDelaySize(final int b) {
        if (b < 0) {
            return;
        }
        this.b = b;
        this.flush();
    }
    
    public void setFirstTarget(final SelesContext.SelesInput c, final int d) {
        this.c = c;
        this.d = d;
    }
    
    public void setLastTarget(final SelesContext.SelesInput e, final int f) {
        this.e = e;
        this.f = f;
    }
    
    public SelesFrameDelayFilter() {
        this.a = new ArrayList<SelesFramebuffer>();
        this.b = 0;
        this.g = new LinkedHashMap<SelesContext.SelesInput, Integer>();
    }
    
    @Override
    protected void onDestroy() {
        this.flush();
        super.onDestroy();
    }

    public void flush() {
        ArrayList var1 = new ArrayList(this.a);
        this.a.clear();
        Iterator var2 = var1.iterator();

        while(var2.hasNext()) {
            SelesFramebuffer var3 = (SelesFramebuffer)var2.next();
            var3.unlock();
        }

    }
    
    @Override
    public void newFrameReady(final long n, final int n2) {
        if (this.mFirstInputFramebuffer == null) {
            return;
        }
        if (this.c != null && this.c.isEnabled() && this.c != this.getTargetToIgnoreForUpdates()) {
            this.c.setInputFramebuffer(this.mFirstInputFramebuffer, this.d);
            this.c.setInputRotation(this.mInputRotation, this.d);
            this.c.setInputSize(this.mFirstInputFramebuffer.getSize(), this.d);
            this.c.newFrameReady(n, this.d);
        }
        if (this.b > 0) {
            super.newFrameReady(n, n2);
            return;
        }
        this.runPendingOnDrawTasks();
        this.mOutputFramebuffer = this.mFirstInputFramebuffer;
        this.mFirstInputFramebuffer = null;
        this.informTargetsAboutNewFrame(n);
    }
    
    @Override
    protected void informTargetsAboutNewFrame(final long n) {
        if (this.framebufferForOutput() == null) {
            return;
        }
        final SelesFramebuffer mOutputFramebuffer = this.mOutputFramebuffer;
        if (this.b > 0) {
            if (this.a.size() < this.b) {
                this.mOutputFramebuffer.lock();
            }
            else {
                this.mOutputFramebuffer = this.a.remove(0);
            }
            this.a.add(mOutputFramebuffer);
        }
        this.g.clear();
        for (final SelesContext.SelesInput selesInput : this.mTargets) {
            if (!selesInput.isEnabled()) {
                continue;
            }
            if (selesInput == this.getTargetToIgnoreForUpdates()) {
                continue;
            }
            final int intValue = this.mTargetTextureIndices.get(this.mTargets.indexOf(selesInput));
            this.g.put(selesInput, intValue);
            this.setInputFramebufferForTarget(selesInput, intValue);
            if (this.b == 0) {
                selesInput.setInputRotation(this.mInputRotation, intValue);
            }
            selesInput.setInputSize(this.mInputTextureSize, intValue);
        }
        if (this.e != null && this.e.isEnabled() && this.e != this.getTargetToIgnoreForUpdates()) {
            this.g.put(this.e, this.f);
            this.e.setInputFramebuffer(mOutputFramebuffer, this.f);
            if (this.b == 0) {
                this.e.setInputRotation(this.mInputRotation, this.f);
            }
            this.e.setInputSize(this.mInputTextureSize, this.f);
        }
        if (this.framebufferForOutput() != null) {
            this.framebufferForOutput().unlock();
        }
        this.removeOutputFramebuffer();
        for (final Map.Entry<SelesContext.SelesInput, Integer> entry : this.g.entrySet()) {
            entry.getKey().newFrameReady(n, entry.getValue());
        }
        if (this.c != null && this.c.isEnabled() && this.c != this.getTargetToIgnoreForUpdates()) {
            this.c.setCurrentlyReceivingMonochromeInput(true);
        }
    }
    
    @Override
    public void setInputSize(final TuSdkSize mInputTextureSize, final int n) {
        if (this.b > 0) {
            super.setInputSize(mInputTextureSize, n);
            return;
        }
        if (mInputTextureSize == null || !mInputTextureSize.isSize()) {
            return;
        }
        this.mInputTextureSize = mInputTextureSize;
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        super.renderToTexture(floatBuffer, floatBuffer2);
        this.checkGLError(this.getClass().getSimpleName());
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    }
    
    @Override
    public void useNextFrameForImageCapture() {
        TLog.d("%s unsupport useNextFrameForImageCapture", "SelesFrameDelayFilter");
    }
    
    @Override
    public IntBuffer imageBufferFromCurrentlyProcessedOutput(final TuSdkSize tuSdkSize) {
        TLog.d("%s unsupport imageBufferFromCurrentlyProcessedOutput", "SelesFrameDelayFilter");
        return null;
    }
    
    public SelesContext.SelesInput getLastTarget() {
        return this.e;
    }
}
